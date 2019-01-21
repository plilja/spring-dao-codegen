package se.plilja.springdaogen.sqlgeneration

import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table


fun insert(table: Table, databaseDialect: DatabaseDialect): String {
    val columns = table.columns.filter { it != table.primaryKey }
    return """
            |"INSERT INTO ${formatTable(table, databaseDialect)} (" +
            |${columns.map { "\"   ${formatIdentifier(it.name, databaseDialect)}" }.joinToString(", \" +\n")}" +
            |") " +
            |"VALUES (" +
            |${columns.map { "\"   :${it.name}" }.joinToString(", \" +\n")}" +
            |")"
            """.trimMargin()
}

fun update(table: Table, databaseDialect: DatabaseDialect): String {
    val columns = table.columns.filter { it != table.primaryKey }
    return """
            |"UPDATE ${formatTable(table, databaseDialect)} SET " +
            |${columns.map { "\"   ${it.name} = :${it.name}" }.joinToString(", \" +\n")} " +
            |"WHERE ${table.primaryKey.name} = :${table.primaryKey.name}"
            """.trimMargin()
}

fun selectOne(table: Table, databaseDialect: DatabaseDialect): String {
    val columns = table.columns
    return """
            |"SELECT " +
            |${columns.map { "\"${formatIdentifier(it.name, databaseDialect)}" }.joinToString(", \" +\n")} " +
            |"FROM ${formatTable(table, databaseDialect)} " +
            |"WHERE ${formatIdentifier(table.primaryKey.name, databaseDialect)} IN (:ids)"
            """.trimMargin()
}

fun delete(table: Table, databaseDialect: DatabaseDialect): String {
    return """
            |"DELETE FROM ${formatTable(table, databaseDialect)} " +
            |"WHERE ${formatIdentifier(table.primaryKey.name, databaseDialect)} IN (:ids)"
            """.trimMargin()
}

fun existsById(table: Table, databaseDialect: DatabaseDialect): String {
    return """
            |"SELECT " +
            |"COUNT(*) " +
            |"FROM ${formatTable(table, databaseDialect)} " +
            |"WHERE ${formatIdentifier(table.primaryKey.name, databaseDialect)} = :${table.primaryKey.name}"
            """.trimMargin()
}

fun selectMany(table: Table, databaseDialect: DatabaseDialect): String {
    val columns = table.columns
    var result = """
            |"SELECT${if (databaseDialect == DatabaseDialect.MSSQL_SERVER) " TOP %d" else ""} " +
            |${columns.map { "\"   ${formatIdentifier(it.name, databaseDialect)}" }.joinToString(", \" +\n")} " +
            |"FROM ${formatTable(table, databaseDialect)} "
            """.trimMargin()
    if (databaseDialect == DatabaseDialect.ORACLE) {
        result += """ +
            |"WHERE ROWNUM <= %d"
        """.trimMargin()
    } else if (databaseDialect in listOf(DatabaseDialect.MYSQL, DatabaseDialect.POSTGRES)) {
        result += """ +
            |"LIMIT %d"
        """.trimMargin()
    } else if (databaseDialect != DatabaseDialect.MSSQL_SERVER) {
        throw IllegalArgumentException("Unknown database dialect ${databaseDialect}")
    }
    return result
}

fun count(table: Table, databaseDialect: DatabaseDialect): String {
    return "\"SELECT COUNT(*) FROM ${formatTable(table, databaseDialect)}\""
}

fun formatTable(table: Table, databaseDialect: DatabaseDialect): String {
    val identifier = formatIdentifier(table.name, databaseDialect)
    val schema = if (table.schemaName != null) {
        "${formatIdentifier(table.schemaName, databaseDialect)}."
    } else {
        ""
    }
    return schema + identifier
}

fun formatIdentifier(id: String, databaseDialect: DatabaseDialect): String {
    if (databaseDialect == DatabaseDialect.POSTGRES && id.toLowerCase() != id) {
        return "\\\"$id\\\""
    } else {
        return id
    }
}


