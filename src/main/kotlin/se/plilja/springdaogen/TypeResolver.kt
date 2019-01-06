package se.plilja.springdaogen

import schemacrawler.schema.Column


fun resolveType(column: Column): Class<out Any> {
    return if (column.type.name.toLowerCase().contains("char") && column.type.typeMappedClass.simpleName == "Array") {
        // Varchar
        String::class.java
    } else {
        column.type.typeMappedClass
    }
}