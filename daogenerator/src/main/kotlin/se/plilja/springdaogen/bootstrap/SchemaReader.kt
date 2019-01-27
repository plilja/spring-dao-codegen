package se.plilja.springdaogen.bootstrap

import org.springframework.boot.jdbc.DataSourceBuilder
import schemacrawler.schema.Catalog
import schemacrawler.schemacrawler.*
import schemacrawler.utility.SchemaCrawlerUtility
import se.plilja.springdaogen.model.*
import se.plilja.springdaogen.model.Config
import java.sql.Connection
import java.sql.SQLXML
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.regex.Pattern


fun readSchema(config: Config): Schema {
    val conn = getConnection(config)
    try {
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
        val catalog = SchemaCrawlerUtility.getCatalog(conn, options)
        return catalogToSchema(catalog, config)
    } finally {
        conn.close()
    }
}

fun catalogToSchema(catalog: Catalog, config: Config): Schema {
    val filteredTables = catalog.tables
        .filter { it.hasPrimaryKey() }
    val convertedTables = filteredTables
        .map { convertTable(it, config) }
    return Schema(convertedTables)
}

fun convertTable(table: schemacrawler.schema.Table, config: Config): Table {
    val sortedColumns =
        table.columns.sortedBy { c -> if (c.isPartOfPrimaryKey) "-${c.name.toLowerCase()}" else c.name.toLowerCase() } // Primary keys first, then other columns in alphabetic order
    return Table(
        if (table.schema != null) table.schema.name else null,
        table.name,
        convertColumn(table, table.primaryKey.columns[0], config),
        sortedColumns.map { convertColumn(table, it, config) },
        config.entityPrefix,
        config.entitySuffix,
        config.repositoryPrefix,
        config.repositorySuffix
    )
}

fun convertColumn(table: schemacrawler.schema.Table, column: schemacrawler.schema.Column, config: Config): Column {
    return Column(column.name, resolveType(column, config), isGenerated(table, column, config))
}

fun resolveType(column: schemacrawler.schema.Column, config: Config): Class<out Any> {
    return if (config.databaseDialect == DatabaseDialect.ORACLE && column.type.name == "NUMBER") {
        Integer::class.java
    } else if (column.type.name.toLowerCase().contains("char") && column.type.typeMappedClass.simpleName == "Array") {
        // Varchar
        String::class.java
    } else if (column.type.typeMappedClass == java.sql.Date::class.java) {
        LocalDate::class.java
    } else if (column.type.typeMappedClass == java.sql.Timestamp::class.java) {
        LocalDateTime::class.java
    } else if (column.type.name == "uuid") {
        UUID::class.java
    } else if (column.type.typeMappedClass == SQLXML::class.java) {
        String::class.java
    } else {
        column.type.typeMappedClass
    }
}

fun isGenerated(table: schemacrawler.schema.Table, column: schemacrawler.schema.Column, config: Config): Boolean {
    return column.isAutoIncremented ||
            (column.isPartOfPrimaryKey && config.hasGeneratedPrimaryKeysOverride.contains(table.name))
}

fun getConnection(config: Config): Connection {
    return DataSourceBuilder.create()
        .url(config.databaseUrl)
        .driverClassName(config.databaseDriver)
        .username(config.databaseUser)
        .password(config.databasePassword)
        .build()
        .connection
}
