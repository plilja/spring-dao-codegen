package se.plilja.springdaogen.bootstrap

import schemacrawler.schema.Catalog
import schemacrawler.schemacrawler.ExcludeAll
import schemacrawler.schemacrawler.IncludeAll
import schemacrawler.schemacrawler.RegularExpressionInclusionRule
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder
import schemacrawler.utility.SchemaCrawlerUtility
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Schema
import se.plilja.springdaogen.model.Table
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.util.regex.Pattern
import javax.sql.DataSource
import kotlin.collections.HashMap


fun readSchema(config: Config, dataSource: DataSource): Schema {
    val schemaFilter = if (config.schemas.isEmpty()) {
        IncludeAll()
    } else {
        RegularExpressionInclusionRule(Pattern.compile("(?i)" + config.schemas.joinToString("|")))
    }
    val options = SchemaCrawlerOptionsBuilder.builder()
        .withSchemaInfoLevel(SchemaInfoLevelBuilder.standard())
        .includeColumns(IncludeAll())
        .includeTables(IncludeAll())
        .includeSchemas(schemaFilter)
        .includeSynonyms(ExcludeAll())
        .includeRoutines(ExcludeAll())
        .toOptions()
    val catalog = SchemaCrawlerUtility.getCatalog(dataSource.connection, options)
    return catalogToSchema(catalog, config)
}

fun catalogToSchema(catalog: Catalog, config: Config): Schema {
    val tablesMap = HashMap<schemacrawler.schema.Table, Table>()
    val columnsMap = HashMap<schemacrawler.schema.Column, Column>()
    catalog.tables
        .filter { it.hasPrimaryKey() }
        .forEach { convertTable(it, config, tablesMap, columnsMap) }
    setForeignKeys(catalog, tablesMap, columnsMap)
    return Schema(tablesMap.values.toList())
}

fun convertTable(
    table: schemacrawler.schema.Table,
    config: Config,
    tablesMap: HashMap<schemacrawler.schema.Table, Table>,
    columnsMap: HashMap<schemacrawler.schema.Column, Column>
) {
    for (column in table.columns) {
        columnsMap[column] = convertColumn(table, column, config)
    }
    val sortedColumns =
        table.columns.sortedBy { c -> if (c.isPartOfPrimaryKey) "-${c.name.toLowerCase()}" else c.name.toLowerCase() } // Primary keys first, then other columns in alphabetic order

    val convertedTable = Table(
        if (table.schema != null) table.schema.name else null,
        table.name,
        columnsMap[table.primaryKey.columns[0]]!!,
        sortedColumns.map { columnsMap[it]!! },
        config
    )
    tablesMap[table] = convertedTable
}

fun setForeignKeys(
    catalog: Catalog,
    tablesMap: HashMap<schemacrawler.schema.Table, Table>,
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
    return Column(
        column.name,
        resolveType(column, config),
        config,
        isGenerated(table, column, config),
        column.isNullable
    )
}

fun resolveType(column: schemacrawler.schema.Column, config: Config): Class<out Any> {
    if (config.databaseDialect == DatabaseDialect.ORACLE) {
        if (column.type.name == "BINARY_DOUBLE") {
            return java.lang.Double::class.java
        } else if (column.type.name == "BINARY_FLOAT") {
            return java.lang.Float::class.java
        } else if (column.type.name == "NUMBER") {
            // These doesn't get cleanly mapped by Schemacrawler for some reason
            return when {
                column.decimalDigits > 0 -> BigDecimal::class.java
                column.size < 10 -> java.lang.Integer::class.java
                column.size < 19 -> java.lang.Long::class.java
                else -> BigInteger::class.java
            }
        }
    }
    if (column.type.typeMappedClass == BigDecimal::class.java && column.decimalDigits == 0) {
        return when {
            column.size < 10 -> java.lang.Integer::class.java
            column.size < 19 -> java.lang.Long::class.java
            else -> BigInteger::class.java
        }
    }
    return if (config.databaseDialect == DatabaseDialect.MYSQL && column.type.name == "YEAR") {
        Integer::class.java
    } else if (column.type.name.toLowerCase().contains("char") && column.type.typeMappedClass.simpleName == "Array") {
        // Varchar
        String::class.java
    } else if (column.type.name.toUpperCase() == "FLOAT") {
        java.lang.Float::class.java
    } else if (column.type.typeMappedClass == java.sql.Date::class.java) {
        LocalDate::class.java
    } else if (column.type.typeMappedClass == java.sql.Timestamp::class.java) {
        LocalDateTime::class.java
    } else if (column.type.typeMappedClass == java.sql.Time::class.java) {
        LocalTime::class.java
    } else if (column.type.name == "uuid") {
        UUID::class.java
    } else {
        column.type.typeMappedClass
    }
}

fun isGenerated(table: schemacrawler.schema.Table, column: schemacrawler.schema.Column, config: Config): Boolean {
    return column.isAutoIncremented ||
            (column.isPartOfPrimaryKey && config.hasGeneratedPrimaryKeysOverride.contains(table.name))
}

