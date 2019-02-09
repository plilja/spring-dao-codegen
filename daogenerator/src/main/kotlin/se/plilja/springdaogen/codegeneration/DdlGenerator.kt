package se.plilja.springdaogen.codegeneration

import org.springframework.jdbc.core.JdbcTemplate
import se.plilja.springdaogen.daogeneration.selectRows
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Schema
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.sql.DataSource


fun toH2Ddl(config: Config, schema: Schema, dataSource: DataSource): String {
    val schemaNames = schema.tables
        .filter { it.schemaName != null }
        .map { it.schemaName }
        .toSet()
    var res = ""
    for (schemaName in schemaNames) {
        res += "CREATE SCHEMA $schemaName;\n"
    }
    res += "\n"
    res += "\n"
    for (table in schema.tables) {
        res += "CREATE TABLE ${formatIdentifier(table.schemaName, table.name)} (\n"
        for (column in table.columns) {
            res += "${column.name} ${dataType(column)},\n"
        }
        res += "PRIMARY KEY(${table.primaryKey.name})"
        res += ");\n"
        res += "\n"
    }
    for (table in schema.tables) {
        for (column in table.columns) {
            val references = column.references
            if (references != null) {
                val refTable = formatIdentifier(references.first.schemaName, references.first.name)
                res += """
                    ALTER TABLE ${table.schemaName}.${table.name}
                    ADD FOREIGN KEY (${column.name})
                    REFERENCES ${refTable}(${references.second.name});
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
                res += "INSERT INTO $t($columns) VALUES($values);"
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
    return when (column.rawType()) {
        Integer::class.java -> "INTEGER"
        LocalDate::class.java -> "DATE"
        LocalDateTime::class.java -> "TIMESTAMP"
        BigDecimal::class.java -> "DECIMAL(30, 10)"
        Double::class.java -> "DOUBLE"
        Float::class.java -> "REAL"
        Boolean::class.java -> "BOOLEAN"
        else -> return "VARCHAR(100)"
    }
}