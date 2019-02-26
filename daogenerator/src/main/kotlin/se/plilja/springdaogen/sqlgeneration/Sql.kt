package se.plilja.springdaogen.sqlgeneration

import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table


fun insert(table: Table, databaseDialect: DatabaseDialect): String {
    val insertColumns = table.columns.filter { it != table.primaryKey || !table.primaryKey.generated }
    return if (insertColumns.isEmpty()) {
        when (databaseDialect) {
            DatabaseDialect.MYSQL -> "\"INSERT INTO ${formatTable(table, databaseDialect)}() VALUES()\""
            DatabaseDialect.ORACLE12 -> "\"INSERT INTO ${formatTable(table, databaseDialect)}() VALUES()\""
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
            |${insertColumns.map { "\"${formatIdentifier(it.name, databaseDialect)}" }.joinToString(", \" +\n")}" +
            |") " +
            |"VALUES (" +
            |${insertColumns.map { "\":${it.name}" }.joinToString(", \" +\n")}" +
            |")"
            """.trimMargin()
    }
}

fun update(table: Table, databaseDialect: DatabaseDialect): String? {
    fun assignment(column: Column): String {
        return if (table.versionColumn() == column) {
            return if (table.versionColumn() == column && databaseDialect in listOf(
                            DatabaseDialect.ORACLE,
                            DatabaseDialect.ORACLE12
                    )
            ) {
                if (column.nullable) {
                    "${column.name} = MOD(NVL(:${column.name}, -1) + 1, 128)"
                } else {
                    "${column.name} = MOD(:${column.name} + 1, 128)"
                }
            } else if (databaseDialect == DatabaseDialect.MYSQL) {
                if (column.nullable) {
                    "${column.name} = (IFNULL(:${column.name}, -1) + 1) %% 128"
                } else {
                    "${column.name} = (:${column.name} + 1) %% 128"
                }
            } else {
                if (column.nullable) {
                    "${column.name} = (COALESCE(:${column.name}, -1) + 1) %% 128"
                } else {
                    "${column.name} = (:${column.name} + 1) %% 128"
                }
            }
        } else {
            "${column.name} = :${column.name}"
        }
    }

    val updateColumns = table.columns
            .filter { it != table.primaryKey }
            .filter { it != table.createdAtColumn() }
            .filter { it != table.createdByColumn() }
    return if (updateColumns.isEmpty()) {
        null // Special case, table only consists of PK-columns. Update is not supported.
    } else {
        val versionColumn = table.versionColumn()
        if (versionColumn != null) {
            val updateSql = """
            |"UPDATE ${formatTable(table, databaseDialect)} SET " +
            |${updateColumns.map { "\"${assignment(it)}" }.joinToString(", \" +\n")} " +
            |"WHERE ${table.primaryKey.name} = :${table.primaryKey.name} %s"
            """.trimMargin()
            val versionClause = """
                   |String versionClause;
                   |if (object.getVersion() != null) {
                   |    versionClause = "AND (${versionColumn.name} = :${versionColumn.name}${if (versionColumn.nullable) " OR ${versionColumn.name} IS NULL)" else ""}";
                   |} else {
                   |    versionClause = "";
                   |}
                   """.trimMargin()
            return """
                   |String updateSql = $updateSql;
                   |$versionClause
                   |return String.format(updateSql, versionClause);
                """.trimMargin()

        } else {
            return """
            |return "UPDATE ${formatTable(table, databaseDialect)} SET " +
            |${updateColumns.map { "\"${assignment(it)}" }.joinToString(", \" +\n")} " +
            |"WHERE ${table.primaryKey.name} = :${table.primaryKey.name}";
            """.trimMargin()
        }
    }
}

fun columnsList(table: Table, databaseDialect: DatabaseDialect): String {
    return table.columns
            .map { formatIdentifier(it.name, databaseDialect) }
            .chunked(5).map { "\" ${it.joinToString(", ")}" }
            .joinToString(", \" +\n") + " \""
}

fun selectOne(table: Table, databaseDialect: DatabaseDialect): String {
    return """
            |"SELECT " +
            |ALL_COLUMNS +
            |"FROM ${formatTable(table, databaseDialect)} " +
            |"WHERE ${formatIdentifier(table.primaryKey.name, databaseDialect)} IN (:ids)"
            """.trimMargin()
}

fun lock(table: Table, config: Config): String {
    val pk = formatIdentifier(table.primaryKey.name, config.databaseDialect)
    val tb = formatTable(table, config.databaseDialect)
    return when {
        config.featureGenerateTestDdl && config.databaseDialect == DatabaseDialect.MSSQL_SERVER -> """
            |if ("H2".equals(databaseProductName)) {
            |   return "SELECT " +
            |       ALL_COLUMNS +
            |       "FROM $tb " +
            |       "WHERE $pk = :id " +
            |       "FOR UPDATE";
            |} else {
            |   return "SELECT " +
            |       ALL_COLUMNS +
            |       "FROM $tb WITH (UPDLOCK) " +
            |       "WHERE $pk = :id";
            |}
        """.trimMargin()
        !config.featureGenerateTestDdl && config.databaseDialect == DatabaseDialect.MSSQL_SERVER -> """
            |return "SELECT " +
            |ALL_COLUMNS +
            |"FROM $tb WITH (UPDLOCK) " +
            |"WHERE $pk = :id";
        """.trimMargin()
        else -> """
            |return "SELECT " +
            |ALL_COLUMNS +
            |"FROM $tb " +
            |"WHERE $pk = :id " +
            |"FOR UPDATE";
        """.trimMargin()
    }
}

fun selectPage(table: Table, databaseDialect: DatabaseDialect): String {
    return when (databaseDialect) {
        in listOf(DatabaseDialect.MYSQL, DatabaseDialect.POSTGRES) -> """
            |String.format("SELECT %n" +
            |ALL_COLUMNS +
            |"FROM ${formatTable(table, databaseDialect)} %n" +
            |"ORDER BY ${formatIdentifier(table.primaryKey.name, databaseDialect)} " +
            |"LIMIT %d OFFSET %d", pageSize, start);
        """.trimMargin()
        in listOf(DatabaseDialect.MSSQL_SERVER, DatabaseDialect.ORACLE12) -> """
            |String.format("SELECT %n" +
            |ALL_COLUMNS +
            |"FROM ${formatTable(table, databaseDialect)} %n" +
            |"ORDER BY ${formatIdentifier(table.primaryKey.name, databaseDialect)} %n" +
            |"OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", start, pageSize);
            """.trimMargin()
        DatabaseDialect.ORACLE -> """
            |String.format("SELECT * FROM (%n" +
            |"SELECT rownum tmp_rownum_, a.* %n" +
            |"FROM (SELECT %n" +
            |ALL_COLUMNS +
            |"FROM ${formatTable(table, databaseDialect)} %n" +
            |"ORDER BY ${formatIdentifier(table.primaryKey.name, databaseDialect)} %n" +
            |") a %n" +
            |"WHERE rownum < %d + %d %n" +
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
    var result = """
            |"SELECT${if (databaseDialect == DatabaseDialect.MSSQL_SERVER) " TOP %d" else ""} " +
            |ALL_COLUMNS +
            |"FROM ${formatTable(table, databaseDialect)} "
            """.trimMargin()
    if (databaseDialect in listOf(DatabaseDialect.ORACLE, DatabaseDialect.ORACLE12)) {
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

fun selectManyQuery(table: Table, databaseDialect: DatabaseDialect): String {
    return when {
        databaseDialect == DatabaseDialect.MSSQL_SERVER -> """
            |String.format("SELECT TOP %d %n" +
            |ALL_COLUMNS +
            |"FROM ${formatTable(table, databaseDialect)} %n" +
            |"WHERE 1=1 %s %n" +
            |"%s", maxAllowedCount, whereClause, orderBy)
            """.trimMargin()
        databaseDialect in listOf(DatabaseDialect.ORACLE, DatabaseDialect.ORACLE12) -> """
            |String.format("SELECT %n" +
            |ALL_COLUMNS +
            |"FROM ${formatTable(table, databaseDialect)} %n" +
            |"WHERE ROWNUM <= %d %s %n" +
            |"%s", maxAllowedCount, whereClause, orderBy)
            """.trimMargin()
        else -> """
            |String.format("SELECT %n" +
            |ALL_COLUMNS +
            |"FROM ${formatTable(table, databaseDialect)} %n" +
            |"WHERE 1=1 %s %n" +
            |"%s " +
            |"LIMIT %d", whereClause, orderBy, maxAllowedCount)
            """.trimMargin()
    }
}

fun selectPageQuery(table: Table, databaseDialect: DatabaseDialect): String {
    return when {
        databaseDialect in listOf(DatabaseDialect.MYSQL, DatabaseDialect.POSTGRES) -> """
            |String.format("SELECT %n" +
            |ALL_COLUMNS +
            |"FROM ${formatTable(table, databaseDialect)} %n" +
            |"WHERE 1=1 %s %n" +
            |"%s %n" +
            |"LIMIT %d OFFSET %d", whereClause, orderBy, pageSize, start);
        """.trimMargin()

        databaseDialect in listOf(DatabaseDialect.MSSQL_SERVER, DatabaseDialect.ORACLE12) -> """
            |String.format("SELECT %n" +
            |ALL_COLUMNS +
            |"FROM ${formatTable(table, databaseDialect)} %n" +
            |"WHERE 1=1 %s" +
            |"%s %n" +
            |"OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", whereClause, orderBy, start, pageSize);
            """.trimMargin()

        databaseDialect == DatabaseDialect.ORACLE -> """
            |String.format("SELECT * FROM (%n" +
            |"SELECT rownum tmp_rownum_, a.* %n" +
            |"FROM (SELECT %n" +
            |ALL_COLUMNS +
            |"FROM ${formatTable(table, databaseDialect)} %n" +
            |"WHERE 1=1 %s %n" +
            |"%s %n" +
            |") a %n" +
            |"WHERE rownum < %d + %d %n" +
            |")%n" +
            |"WHERE tmp_rownum_ >= %d", whereClause, orderBy, start + 1, pageSize, start + 1);
        """.trimMargin()

        else -> throw RuntimeException("Unsupported database dialect $databaseDialect")
    }
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
    val escapeSeq = when (databaseDialect) {
        DatabaseDialect.MYSQL -> "`"
        else -> "\\\""
    }
    val needsPostgresCaseEscaping =
            databaseDialect == DatabaseDialect.POSTGRES && identifier.toLowerCase() != identifier
    if (needsPostgresCaseEscaping || isDatabaseIdentifier(identifier, databaseDialect)) {
        return "$escapeSeq$identifier$escapeSeq"
    } else {
        return identifier
    }
}

fun isDatabaseIdentifier(identifier: String, databaseDialect: DatabaseDialect): Boolean {
    return SqlKeywords.get(databaseDialect).contains(identifier.toUpperCase())
}


