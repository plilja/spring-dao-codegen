package se.plilja.springdaogen.model

import se.plilja.springdaogen.util.camelCase
import se.plilja.springdaogen.util.capitalizeFirst
import se.plilja.springdaogen.util.capitalizeLast
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


data class Schema(val tables: List<Table>)

data class Table(val schemaName: String, val name: String, val primaryKey: Column, val columns: List<Column>) {
    fun entityName(): String {
        return capitalizeFirst(camelCase(name)) + "Entity"
    }

    fun repositoryName(): String {
        return capitalizeFirst(camelCase(name)) + "Repository"
    }
}

data class Column(
    val name: String,
    val javaType: Class<out Any>
) {
    fun setter(): String {
        return "set${capitalizeFirst(fieldName())}"
    }

    fun getter(): String {
        return "get${capitalizeFirst(fieldName())}"
    }

    fun fieldName(): String {
        val s = camelCase(name)
        if (isReservedKeyword(s)) {
            return capitalizeLast(s)
        } else {
            return s
        }
    }

    fun recordSetMethod(rs: String): String {
        if (javaType == BigDecimal::class.java) {
            return "$rs.getBigDecimal(\"${name}\")"
        } else if (javaType == String::class.java) {
            return "$rs.getString(\"${name}\")"
        } else if (javaType == Boolean::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getBoolean(\"${name}\") : null"
        } else if (javaType == Integer::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getInt(\"${name}\") : null"
        } else if (javaType == Long::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getLong(\"${name}\") : null"
        } else if (javaType == Float::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getFloat(\"${name}\") : null"
        } else if (javaType == Double::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getDouble(\"${name}\") : null"
        } else if (javaType == UUID::class.java) {
            return "UUID.fromString($rs.getString(\"${name}\"))"
        } else if (javaType == LocalDate::class.java) {
            return "$rs.getDate(\"${name}\").toLocalDate()"
        } else if (javaType == LocalDateTime::class.java) {
            return "$rs.getTimestamp(\"${name}\").toLocalDateTime()"
        } else {
            return "(${javaType.simpleName}) $rs.getObject(\"${name}\")"
        }

    }
}

fun isReservedKeyword(s: String): Boolean {
    return s in setOf(
        "abstract", "assert", "boolean", "break", "byte", "case", "catch",
        "char", "class", "const", "continue", "default", "double", "do",
        "else", "enum", "extends", "false", "final", "finally", "float",
        "for", "goto", "if", "implements", "import", "instanceof", "int",
        "interface", "long", "native", "new", "null", "package", "private",
        "protected", "public", "return", "short", "static", "strictfp",
        "super", "switch", "synchronized", "this", "throw", "throws",
        "transient", "true", "try", "void", "volatile", "while"
    )
}