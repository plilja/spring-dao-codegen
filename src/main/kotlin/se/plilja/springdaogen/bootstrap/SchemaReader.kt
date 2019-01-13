package se.plilja.springdaogen.bootstrap

import schemacrawler.schema.Catalog
import schemacrawler.schemacrawler.IncludeAll
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder
import schemacrawler.utility.SchemaCrawlerUtility
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Schema
import se.plilja.springdaogen.model.Table
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLXML
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


fun readSchema(config: Config): Schema {
    val conn = getConnection(config)
    val options = SchemaCrawlerOptionsBuilder.builder()
        .includeColumns(IncludeAll())
        .includeTables(IncludeAll())
        .includeSchemas(IncludeAll())
        .toOptions()
    val catalog = SchemaCrawlerUtility.getCatalog(conn, options)
    return catalogToSchema(catalog)
}

fun catalogToSchema(catalog: Catalog): Schema {
    return Schema(catalog.tables.map { convertTable(it) })
}

fun convertTable(table: schemacrawler.schema.Table): Table {
    return Table(
        if (table.schema != null) table.schema.name else null,
        table.name,
        convertColumn(table.primaryKey.columns[0]),
        table.columns.map { convertColumn(it) })
}

fun convertColumn(column: schemacrawler.schema.Column): Column {
    return Column(column.name, resolveType(column))
}

fun resolveType(column: schemacrawler.schema.Column): Class<out Any> {
    return if (column.type.name.toLowerCase().contains("char") && column.type.typeMappedClass.simpleName == "Array") {
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

fun getConnection(config: Config): Connection {
    Class.forName(config.databaseDriver)
    return DriverManager.getConnection(config.databaseUrl, config.databaseUser, config.databasePassword)
}
