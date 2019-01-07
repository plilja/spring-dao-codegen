package se.plilja.springdaogen.model

import se.plilja.springdaogen.util.camelCase
import se.plilja.springdaogen.util.capitalizeFirst


data class Schema(val tables: List<Table>)

data class Table(val name: String, val primaryKey: Column, val columns: List<Column>) {
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
            return s + "2"
        } else {
            return s
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