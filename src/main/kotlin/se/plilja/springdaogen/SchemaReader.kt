package se.plilja.springdaogen

import schemacrawler.schema.Catalog
import schemacrawler.schemacrawler.IncludeAll
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder
import schemacrawler.utility.SchemaCrawlerUtility
import java.sql.Connection
import java.sql.DriverManager


fun readSchema(connString: String, user: String, pass: String, driver: String): Schema {
    val conn = getConnection(connString, user, pass, driver)
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
    return Table(table.name, convertColumn(table.primaryKey.columns[0]), table.columns.map { convertColumn(it) })
}

fun convertColumn(column: schemacrawler.schema.Column): Column {
    return Column(column.name, resolveType(column))
}

fun resolveType(column: schemacrawler.schema.Column): Class<out Any> {
    return if (column.type.name.toLowerCase().contains("char") && column.type.typeMappedClass.simpleName == "Array") {
        // Varchar
        String::class.java
    } else {
        column.type.typeMappedClass
    }
}

fun getConnection(connString: String, user: String, pass: String, driver: String): Connection {
    Class.forName(driver)
    val conn = DriverManager.getConnection(connString, user, pass)
    return conn
}
