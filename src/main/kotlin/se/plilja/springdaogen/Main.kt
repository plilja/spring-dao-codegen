package se.plilja.springdaogen

import schemacrawler.schemacrawler.IncludeAll
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder
import schemacrawler.utility.SchemaCrawlerUtility
import java.sql.Connection
import java.sql.DriverManager

fun main(args: Array<String>) {
    val conn = getConnection()
    val options = SchemaCrawlerOptionsBuilder.builder()
        .includeColumns(IncludeAll())
        .includeTables(IncludeAll())
        .includeSchemas(IncludeAll())
        .toOptions()
    val catalog = SchemaCrawlerUtility.getCatalog(conn, options)
    for (table in catalog.tables) {
        println(table.name)
        for (column in table.columns) {
            println("${column.name} ${column.type}")
        }
    }
    println(catalog.tables)
    println("Hello, World")
}

fun getConnection(): Connection {
    val DB_URL = "jdbc:postgresql://localhost:3003/foo"

    //  Database credentials
    val USER = "user"
    val PASS = "password"

    //STEP 2: Register JDBC driver
    Class.forName("org.postgresql.Driver")

    //STEP 3: Open a connection
    println("Connecting to database...")
    val conn = DriverManager.getConnection(DB_URL, USER, PASS)

    //STEP 4: Execute a query
    println("Creating database...")
    return conn
}

