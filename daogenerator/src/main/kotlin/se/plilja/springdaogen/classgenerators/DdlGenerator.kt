package se.plilja.springdaogen.classgenerators

import se.plilja.springdaogen.engine.selectRows
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Schema
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import javax.sql.DataSource


fun toH2Ddl(config: Config, schema: Schema, dataSource: DataSource): String {
    val schemaNames = schema.tables
        .filter { it.schemaName != null }
        .map { it.schemaName }
        .toSet()
    var res = ""
    for (schemaName in schemaNames) {
        if (schemaName != "public") {
            // public exists by default
            res += "CREATE SCHEMA IF NOT EXISTS $schemaName;\n"
        }
    }
    res += "\n"
    res += "\n"
    for (table in schema.tables) {
        res += "CREATE TABLE IF NOT EXISTS ${formatIdentifier(table.schemaName, table.name)} (\n"
        for (column in table.columns) {
            val maybeNotNull = if (!column.nullable) " NOT NULL" else ""
            res += "${column.name} ${dataType(column)}$maybeNotNull,\n"
        }
        res += "PRIMARY KEY(${table.primaryKey.name})"
        res += ");\n"
        res += "\n"
    }
    for (table in schema.tables) {
        for (column in table.columns) {
            val references = column.references
            if (references != null) {
                val table_ = formatIdentifier(table.schemaName, table.name)
                val refTable = formatIdentifier(references.first.schemaName, references.first.name)
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
    for (table in schema.tables) {
        if (table.isEnum()) {
            val rows = selectRows(dataSource, table, config)
            val t = formatIdentifier(table.schemaName, table.name)
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