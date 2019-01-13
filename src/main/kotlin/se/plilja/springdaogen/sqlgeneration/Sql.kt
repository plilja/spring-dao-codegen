package se.plilja.springdaogen.sqlgeneration

import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table


fun insert(table: Table, config: Config): String {
    val columns = table.columns.filter { it != table.primaryKey }
    return """
            |"INSERT INTO ${formatIdentifier(table.schemaName, config)}.${formatIdentifier(table.name, config)} (" +
            |${columns.map { "\"   ${formatIdentifier(it.name, config)}" }.joinToString(", \" +\n")}" +
            |") " +
            |"VALUES (" +
            |${columns.map { "\"   :${it.name}" }.joinToString(", \" +\n")}" +
            |")"
            """.trimMargin()
}

fun update(table: Table, config: Config): String {
    val columns = table.columns.filter { it != table.primaryKey }
    return """
            |"UPDATE ${formatIdentifier(table.schemaName, config)}.${formatIdentifier(table.name, config)} SET " +
            |${columns.map { "\"   ${it.name} = :${it.name}" }.joinToString(", \" +\n")} " +
            |"WHERE ${table.primaryKey.name} = :${table.primaryKey.name}"
            """.trimMargin()
}

fun selectOne(table: Table, config: Config): String {
    val columns = table.columns
    return """
            |"SELECT " +
            |${columns.map { "\"   ${formatIdentifier(it.name, config)}" }.joinToString(", \" +\n")} " +
            |"FROM ${formatIdentifier(table.schemaName, config)}.${formatIdentifier(table.name, config)} " +
            |"WHERE ${formatIdentifier(table.primaryKey.name, config)} = :${table.primaryKey.name}"
            """.trimMargin()
}

fun selectMany(table: Table, config: Config): String {
    val columns = table.columns
    var result = """
            |"SELECT${if (config.databaseDialect == DatabaseDialect.MSSQL_SERVER) " TOP %d" else ""} " +
            |${columns.map { "\"   ${formatIdentifier(it.name, config)}" }.joinToString(", \" +\n")} " +
            |"FROM ${formatIdentifier(table.schemaName, config)}.${formatIdentifier(table.name, config)} "
            """.trimMargin()
    if (config.databaseDialect == DatabaseDialect.ORACLE) {
        result += """ +
            |"WHERE ROWNUM <= %d"
        """.trimMargin()
    } else if (config.databaseDialect in listOf(DatabaseDialect.MYSQL, DatabaseDialect.POSTGRES)) {
        result += """ +
            |"LIMIT %d"
        """.trimMargin()
    } else if (config.databaseDialect != DatabaseDialect.MSSQL_SERVER) {
        throw IllegalArgumentException("Unknown database dialect ${config.databaseDialect}")
    }
    return result
}

fun formatIdentifier(id: String, config: Config): String {
    if (config.databaseDialect == DatabaseDialect.POSTGRES && id.toLowerCase() != id) {
        return "\\\"$id\\\""
    } else {
        return id
    }
}
