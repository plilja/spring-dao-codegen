package se.plilja.springdaogen.model

import se.plilja.springdaogen.util.camelCase
import se.plilja.springdaogen.util.capitalizeFirst
import se.plilja.springdaogen.util.snakeCase


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
        return "set${capitalizeFirst(camelCase(name))}"
    }

    fun getter(): String {
        return "get${capitalizeFirst(camelCase(name))}"
    }

    fun fieldName() : String {
        return camelCase(name)
    }
}