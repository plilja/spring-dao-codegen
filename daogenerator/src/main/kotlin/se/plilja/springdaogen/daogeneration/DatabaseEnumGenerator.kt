package se.plilja.springdaogen.daogeneration

import se.plilja.springdaogen.codegeneration.EnumGenerator
import se.plilja.springdaogen.copyable.baseDatabaseEnum
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Left
import se.plilja.springdaogen.model.Right
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.util.snakeCase
import java.math.BigDecimal
import java.util.*


fun generateEnums(config: Config, table: Table, tableContents: List<Map<String, Any>>): EnumGenerator {

    val nameColumn = table.columns
        .first { config.enumNameColumnRegexp.matches(it.name) }

    val g = EnumGenerator(table.entityName(), config.entityOutputPackage, config.entityOutputFolder)
    g.addImplements("BaseDatabaseEnum<${table.primaryKey.typeName()}>")

    for (column in table.columns) {
        val type = column.type()
        when (type) {
            is Left -> g.addImport(type.value)
        }
        if (column == table.primaryKey) {
            // Let's call it id no matter what it's called in the db.
            // Let's hope another column that is also named id but isn't
            // the primary key exists. If the schema is that bad the
            // user is probably in trouble.
            g.addPrivateField("id", column.typeName())
        } else if (column != nameColumn) {
            g.addReadOnlyField(column.fieldName(), column.typeName())
        }
    }

    val enumValues = TreeMap<Any?, Pair<String, Map<String, Any>>>()
    for (row in tableContents) {
        val id = row[table.primaryKey.name]
        val name = row[nameColumn.name] as String
        enumValues[id] = Pair(name, row)
    }
    for (enumValue in enumValues) {
        val name = enumValue.value.first
        val rowContent = enumValue.value.second
        var init = "${snakeCase(name).toUpperCase()}("
        init += table.columns
            .filter { it != nameColumn || it == table.primaryKey }
            .map { getValueAsJavaCode(it, rowContent, g) }
            .joinToString(", ")
        init += ")"
        g.addEnumValue(init)
    }

    g.addCustomMethod(
        """
        @Override
        public ${table.primaryKey.typeName()} getId() {
           return id;
        }
    """.trimIndent()
    )
    g.addCustomMethod(
        """
        public static ${table.entityName()} fromId(${table.primaryKey.typeName()} id) {
           if (id != null) {
               for (${table.entityName()} value : values()) {
                  if (value.getId().equals(id)) {
                     return value;
                  }
               }
           }
           return null;
        }
    """.trimIndent()
    )
    ensureImported(g, config) { baseDatabaseEnum(config.frameworkOutputPackage) }
    return g
}

fun getValueAsJavaCode(column: Column, row: Map<String, Any>, enumGenerator: EnumGenerator): String {
    val type = column.type()
    return when (type) {
        is Left -> {
            when (type.value) {
                BigDecimal::class.java -> {
                    enumGenerator.addImport(BigDecimal::class.java)
                    "new BigDecimal(${row[column.name]})"
                }
                Float::class.java -> "${row[column.name]}F"
                Long::class.java -> "${row[column.name]}L"
                String::class.java -> "\"${row[column.name]}\""
                else -> row[column.name].toString()
            }
        }
        is Right -> {
            return "${type.value}.fromId(${row[column.name]})"
        }

    }
}
