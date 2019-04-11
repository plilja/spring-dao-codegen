package se.plilja.springdaogen.bootstrap

import schemacrawler.schema.Catalog
import schemacrawler.schemacrawler.*
import schemacrawler.utility.SchemaCrawlerUtility
import se.plilja.springdaogen.config.Config
import se.plilja.springdaogen.engine.model.*
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.JDBCType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.util.*
import java.util.regex.Pattern
import javax.sql.DataSource
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


fun readSchema(config: Config, dataSource: DataSource): List<Schema> {
    val schemaFilter = if (config.schemas.isEmpty()) {
        IncludeAll()
    } else {
        RegularExpressionInclusionRule(regularExpressionFromList(config.schemas))
    }
    val includePattern = if (!config.includeTables.isEmpty()) regularExpressionFromList(config.includeTables) else null
    val excludePattern = if (!config.excludeTables.isEmpty()) regularExpressionFromList(config.excludeTables) else null
    val tablesFilter = RegularExpressionRule(includePattern, excludePattern)
    println("Starting to crawl schema. This might be slow depending on the size of your database...")
    val options = SchemaCrawlerOptionsBuilder.builder()
            .withSchemaInfoLevel(SchemaInfoLevelBuilder.standard())
            .includeColumns(IncludeAll())
            .includeTables(tablesFilter)
            .includeSchemas(schemaFilter)
            .includeSynonyms(ExcludeAll())
            .includeRoutines(ExcludeAll())
            .toOptions()
    val catalog = SchemaCrawlerUtility.getCatalog(dataSource.connection, options)
    println("Done crawling schema.")
    return catalogToSchemas(catalog, config)
}

private fun regularExpressionFromList(rules: List<String>) =
        Pattern.compile("(?i)" + rules.map { r -> ".*\\.?$r" }.joinToString("|"))

fun catalogToSchemas(catalog: Catalog, config: Config): List<Schema> {
    println("Found these tables: ${catalog.tables.map { it.name }.joinToString(", ")}.")
    val schemas = ArrayList<Schema>()
    val tablesMap = HashMap<schemacrawler.schema.Table, TableOrView>()
    val columnsMap = HashMap<schemacrawler.schema.Column, Column>()
    val filteredTables = catalog.tables
            .filter { it.hasPrimaryKey() && it.primaryKey.columns.size == 1 } // Currently does not support tables with composite keys
    if (filteredTables.size != catalog.tables.size) {
        val excluded = catalog.tables.map { it.name }.toSet() - filteredTables.map { it.name }.toSet()
        println("Will not generate dao:s or entities for these unsupported tables: " + excluded.joinToString(", "))
    }
    val filteredViews = catalog.tables
            .filter { it.tableType.isView }
            .filter { config.featureGenerateQueryApi } // Views are only supported if query API is being generated
    for (tableOrView in filteredTables + filteredViews) {
        if (schemas.none { it.schemaName == tableOrView.schema?.name }) {
            schemas.add(Schema(tableOrView.schema?.name, ArrayList(), ArrayList()))
        }
    }
    filteredTables.forEach { convertTable(it, config, schemas, tablesMap, columnsMap) }
    filteredViews.forEach { convertView(it, config, schemas, tablesMap, columnsMap) }
    setForeignKeys(catalog, tablesMap, columnsMap)
    return schemas
}

fun convertTable(
        table: schemacrawler.schema.Table,
        config: Config,
        schemas: List<Schema>,
        tablesMap: HashMap<schemacrawler.schema.Table, TableOrView>,
        columnsMap: HashMap<schemacrawler.schema.Column, Column>
) {
    for (column in table.columns) {
        columnsMap[column] = convertColumn(table, column, config)
    }
    val sortedColumns =
            table.columns.sortedBy { c -> if (c.isPartOfPrimaryKey) "-${c.name.toLowerCase()}" else c.name.toLowerCase() } // Primary keys first, then other columns in alphabetic order

    val schema = schemas.first { it.schemaName == table.schema?.name }
    val convertedTable = Table(
            schema,
            table.name,
            columnsMap[table.primaryKey.columns[0]]!!,
            sortedColumns.map { columnsMap[it]!! },
            config
    )
    schema.tables.add(convertedTable)
    tablesMap[table] = convertedTable
}

fun convertView(
        view: schemacrawler.schema.Table,
        config: Config,
        schemas: List<Schema>,
        tablesMap: HashMap<schemacrawler.schema.Table, TableOrView>,
        columnsMap: HashMap<schemacrawler.schema.Column, Column>
) {
    for (column in view.columns) {
        columnsMap[column] = convertColumn(view, column, config)
    }
    val sortedColumns =
            view.columns.sortedBy { it.name.toLowerCase() }

    val schema = schemas.first { it.schemaName == view.schema?.name }
    val convertedView = View(
            schema,
            view.name,
            sortedColumns.map { columnsMap[it]!! },
            config
    )
    schema.views.add(convertedView)
    tablesMap[view] = convertedView
}

fun setForeignKeys(
        catalog: Catalog,
        tablesMap: HashMap<schemacrawler.schema.Table, TableOrView>,
        columnsMap: HashMap<schemacrawler.schema.Column, Column>
) {
    val columnToTable = catalog.tables.flatMap { t -> t.columns.map { Pair(it, t) } }.toMap()
    for (table in catalog.tables) {
        for (column in table.columns) {
            val referencedColumn = column.referencedColumn
            if (referencedColumn != null) {

                columnsMap[column]?.references =
                        Pair(tablesMap[columnToTable[referencedColumn]]!!, columnsMap[referencedColumn]!!)
            }
        }
    }
}

fun convertColumn(table: schemacrawler.schema.Table, column: schemacrawler.schema.Column, config: Config): Column {
    val (type, jdbcType) = resolveType(column, config)
    return Column(
            name = column.name,
            javaType = type,
            config = config,
            jdbcType = jdbcType,
            generated = isGenerated(table, column, config),
            nullable = column.isNullable,
            size = column.size,
            precision = column.decimalDigits
    )
}


fun resolveType(column: schemacrawler.schema.Column, config: Config): Pair<Class<out Any>, JDBCType?> {
    val isOracle = config.databaseDialect in listOf(DatabaseDialect.ORACLE, DatabaseDialect.ORACLE12)
    if (isOracle) {
        if (column.type.name == "BINARY_DOUBLE") {
            return Pair(java.lang.Double::class.java, JDBCType.DOUBLE)
        } else if (column.type.name == "BINARY_FLOAT") {
            return Pair(java.lang.Float::class.java, JDBCType.REAL)
        } else if (column.type.name == "NUMBER") {
            // These doesn't get cleanly mapped by Schemacrawler for some reason
            return when {
                column.decimalDigits > 0 -> Pair(BigDecimal::class.java, JDBCType.NUMERIC)
                column.size < 10 -> Pair(java.lang.Integer::class.java, JDBCType.INTEGER)
                column.size < 19 -> Pair(java.lang.Long::class.java, JDBCType.BIGINT)
                else -> Pair(BigInteger::class.java, JDBCType.NUMERIC)
            }
        }
    }
    if (column.type.typeMappedClass == BigDecimal::class.java && column.decimalDigits == 0) {
        return when {
            column.size < 10 -> Pair(java.lang.Integer::class.java, JDBCType.INTEGER)
            column.size < 19 -> Pair(java.lang.Long::class.java, JDBCType.BIGINT)
            else -> Pair(BigInteger::class.java, JDBCType.NUMERIC)
        }
    }

    if (config.databaseDialect == DatabaseDialect.MYSQL && column.type.name == "YEAR") {
        return Pair(Integer::class.java, JDBCType.INTEGER)
    } else if (column.type.typeMappedClass == java.sql.Date::class.java) {
        return Pair(LocalDate::class.java, JDBCType.DATE)
    } else if (column.type.name.toUpperCase() == "FLOAT") {
        return Pair(java.lang.Float::class.java, JDBCType.REAL)
    }

    var jdbcType = try {
        JDBCType.valueOf(column.type.javaSqlType.name)
    } catch (ex: IllegalArgumentException) {
        null
    }
    if (jdbcType == JDBCType.BLOB) {
        // Doesn't work, let spring deduct type from param instead
        jdbcType = null
    }
    if (jdbcType == null && config.databaseDialect == DatabaseDialect.MSSQL_SERVER && column.type.name.toLowerCase() == "datetimeoffset") {
        return Pair(OffsetDateTime::class.java, JDBCType.TIMESTAMP_WITH_TIMEZONE)
    }
    if ((jdbcType == JDBCType.TIMESTAMP || (jdbcType == null && isOracle)) &&
            (column.type.name.toLowerCase().contains("tz") || column.type.name.toLowerCase().contains("zone"))
    ) {
        return Pair(OffsetDateTime::class.java, JDBCType.TIMESTAMP_WITH_TIMEZONE)
    }

    val javaType =
            if (column.type.name.toLowerCase().contains("char") && column.type.typeMappedClass.simpleName == "Array") {
                // Varchar
                String::class.java
            } else if (column.type.typeMappedClass == java.sql.Timestamp::class.java) {
                LocalDateTime::class.java
            } else if (column.type.typeMappedClass == java.sql.Time::class.java) {
                LocalTime::class.java
            } else if (column.type.name == "uuid") {
                UUID::class.java
            } else if (column.type.typeMappedClass == java.math.BigDecimal::class.java) {
                // Numeric and decimal seem to behave the same in almost all cases
                // but numeric behave a little nicer for Oracle. According to the spec
                // numeric is supposed to be slightly more precise.
                return Pair(BigDecimal::class.java, JDBCType.NUMERIC)
            } else {
                column.type.typeMappedClass
            }
    return Pair(javaType, jdbcType)
}

fun isGenerated(table: schemacrawler.schema.Table, column: schemacrawler.schema.Column, config: Config): Boolean {
    return column.isAutoIncremented ||
            (column.isPartOfPrimaryKey && config.hasGeneratedPrimaryKeysOverride.contains(table.name))
}

