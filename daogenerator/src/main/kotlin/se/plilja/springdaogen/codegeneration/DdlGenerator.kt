package se.plilja.springdaogen.codegeneration

import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Schema
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime


fun toH2Ddl(schema: Schema): String {
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
            var fkCounter = 1
            val references = column.references
            if (references != null) {
                val refTable = formatIdentifier(references.first.schemaName, references.first.name)
                res += """
                    ALTER TABLE ${table.schemaName}.${table.name}
                    ADD FOREIGN KEY ${table.schemaName}_${table.name}_$fkCounter
                    REFERENCES ${refTable}(${references.second.name});
                """.trimIndent()
                fkCounter++
            }
            res += "\n"
        }
    }
    return res
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
    return when (column.type()) {
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