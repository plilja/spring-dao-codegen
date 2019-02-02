package se.plilja.springdaogen.sqlgeneration

import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table


fun insert(table: Table, databaseDialect: DatabaseDialect): String {
    val insertColumns = table.columns.filter { it != table.primaryKey || !table.primaryKey.generated }
    return if (insertColumns.isEmpty()) {
        when (databaseDialect) {
            DatabaseDialect.MYSQL -> "\"INSERT INTO ${formatTable(table, databaseDialect)}() VALUES()\""
            DatabaseDialect.ORACLE -> {
                // TODO will this work with Oracle12c identity style columns?
                "\"INSERT INTO ${formatTable(table, databaseDialect)}(${formatIdentifier(
                    table.primaryKey.name,
                    databaseDialect
                )}) VALUES(null)\""
            }
            else -> "\"INSERT INTO ${formatTable(table, databaseDialect)} DEFAULT VALUES\""
        }
    } else {
        """
            |"INSERT INTO ${formatTable(table, databaseDialect)} (" +
            |${insertColumns.map { "\"   ${formatIdentifier(it.name, databaseDialect)}" }.joinToString(", \" +\n")}" +
            |") " +
            |"VALUES (" +
            |${insertColumns.map { "\"   :${it.name}" }.joinToString(", \" +\n")}" +
            |")"
            """.trimMargin()
    }
}

fun update(table: Table, databaseDialect: DatabaseDialect): String? {
    val updateColumns = table.columns.filter { it != table.primaryKey }
    return if (updateColumns.isEmpty()) {
        null // Special case, table only consists of PK-columns. Update is not supported.
    } else {
        """
            |"UPDATE ${formatTable(table, databaseDialect)} SET " +
            |${updateColumns.map { "\"   ${it.name} = :${it.name}" }.joinToString(", \" +\n")} " +
            |"WHERE ${table.primaryKey.name} = :${table.primaryKey.name}"
            """.trimMargin()
    }
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

fun selectPage(table: Table, databaseDialect: DatabaseDialect): String {
    val columns = table.columns
    return when {
        databaseDialect in listOf(DatabaseDialect.MYSQL, DatabaseDialect.POSTGRES) -> """
            |String.format("SELECT %n" +
            |${columns.map { "\"${formatIdentifier(it.name, databaseDialect)}" }.joinToString(", %n\" +\n")} %n" +
            |"FROM ${formatTable(table, databaseDialect)} %n" +
            |"LIMIT %d OFFSET %d", pageSize, start);
        """.trimMargin()

        databaseDialect == DatabaseDialect.MSSQL_SERVER -> """
            |String.format("SELECT %n" +
            |${columns.map { "\"${formatIdentifier(it.name, databaseDialect)}" }.joinToString(", %n\" +\n")} %n" +
            |"FROM ${formatTable(table, databaseDialect)} %n" +
            |"ORDER BY ${formatIdentifier(table.primaryKey.name, databaseDialect)} %n" +
            |"OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", start, pageSize);
            """.trimMargin()

        databaseDialect == DatabaseDialect.ORACLE -> """
            |String.format("SELECT * FROM (%n" +
            |"    SELECT rownum tmp_rownum_, a.* %n" +
            |"    FROM (SELECT %n" +
            |${columns.map { "\"${formatIdentifier(it.name, databaseDialect)}" }.joinToString(", %n\" +\n")} %n" +
            |"        FROM ${formatTable(table, databaseDialect)} %n" +
            |"        ORDER BY ${formatIdentifier(table.primaryKey.name, databaseDialect)} %n" +
            |"    ) a %n" +
            |"    WHERE rownum < %d + %d %n" +
            |")%n" +
            |"WHERE tmp_rownum_ >= %d", start + 1, pageSize, start + 1);
        """.trimMargin()

        else -> throw RuntimeException("Unsupported database dialect $databaseDialect")
    }
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

fun formatIdentifier(identifier: String, databaseDialect: DatabaseDialect): String {
    val needsPostgresCaseEscaping =
        databaseDialect == DatabaseDialect.POSTGRES && identifier.toLowerCase() != identifier
    if (needsPostgresCaseEscaping || isDatabaseIdentifier(identifier, databaseDialect)) {
        return "\\\"$identifier\\\""
    } else {
        return identifier
    }
}

fun isDatabaseIdentifier(identifier: String, databaseDialect: DatabaseDialect): Boolean {
    return SqlKeywords.get(databaseDialect).contains(identifier.toUpperCase())
}


