package se.plilja.springdaogen.syntaxgenerator

import se.plilja.springdaogen.engine.model.Column
import se.plilja.springdaogen.engine.model.Schema
import se.plilja.springdaogen.engine.model.TableContents
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime


fun toH2Ddl(schemas: List<Schema>, tableContents: TableContents): String {
    val schemaNames = schemas
            .map { it.schemaName }
            .filter { it != null }
    var res = ""
    for (schemaName in schemaNames) {
        if (schemaName != "public") {
            // public exists by default
            res += "CREATE SCHEMA IF NOT EXISTS $schemaName;\n"
        }
    }
    res += "\n"
    res += "\n"
    for (table in schemas.flatMap { it.tables }) {
        res += "CREATE TABLE IF NOT EXISTS ${formatIdentifier(table.schema.schemaName, table.name)} (\n"
        for (column in table.columns) {
            val maybeNotNull = if (!column.nullable) " NOT NULL" else ""
            res += "${column.name} ${dataType(column)}$maybeNotNull,\n"
        }
        res += "PRIMARY KEY(${table.primaryKey.name})"
        res += ");\n"
        res += "\n"
    }
    for (table in schemas.flatMap { it.tables }) {
        for (column in table.columns) {
            val references = column.references
            if (references != null) {
                val table_ = formatIdentifier(table.schema.schemaName, table.name)
                val refTable = formatIdentifier(references.first.schema.schemaName, references.first.name)
                res += """
                    ALTER TABLE $table_
                    ADD FOREIGN KEY (${column.name})
                    REFERENCES $refTable(${references.second.name});
                """.trimIndent()
                res += "\n"
            }
        }
    }
    res += "\n"
    for (table in schemas.flatMap { it.tables }) {
        if (table.isEnum()) {
            val rows = tableContents.getContents(table)
            val t = formatIdentifier(table.schema.schemaName, table.name)
            for (row in rows) {
                val columns = table.columns.map { it.name }.joinToString(", ")
                val values = table.columns.map { extractValue(row[it.name], it) }.joinToString(", ")
                res += "MERGE INTO $t($columns) VALUES($values);"
                res += "\n"
            }
        }
    }
    return res
}

fun extractValue(o: Any?, column: Column): String {
    return when (column.rawType()) {
        String::class.java -> "'$o'"
        else -> o?.toString() ?: "null"
    }
}

private fun formatIdentifier(schema: String?, id: String): String {
    return if (schema == null) {
        id
    } else {
        "$schema.$id"
    }
}

private fun dataType(column: Column): String {
    if (column.generated) {
        return "IDENTITY"
    }
    when (column.typeName()) {
        "byte[]" -> {
            return if (column.size > 4000) {
                "BLOB"
            } else {
                "BINARY(${column.size})"
            }
        }
    }
    return when (column.rawType()) {
        java.lang.Integer::class.java -> "INTEGER"
        LocalDate::class.java -> "DATE"
        LocalDateTime::class.java -> "TIMESTAMP"
        OffsetDateTime::class.java -> "TIMESTAMP WITH TIME ZONE"
        LocalTime::class.java -> "TIME"
        BigDecimal::class.java -> "DECIMAL(${column.size}, ${column.precision})"
        BigInteger::class.java -> "DECIMAL(${column.size}, 0)"
        java.lang.Double::class.java -> "DOUBLE"
        java.lang.Float::class.java -> "REAL"
        java.lang.Long::class.java -> "BIGINT"
        java.lang.Boolean::class.java -> "BOOLEAN"
        java.util.UUID::class.java -> "UUID"
        java.lang.String::class.java -> {
            return if (column.size == Integer.MAX_VALUE) {
                "CLOB"
            } else {
                "VARCHAR(${column.size})"
            }
        }
        else -> return "VARCHAR(${column.size})"
    }
}