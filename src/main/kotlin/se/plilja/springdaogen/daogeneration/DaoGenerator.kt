package se.plilja.springdaogen.daogeneration

import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Schema
import se.plilja.springdaogen.codegeneration.ClassGenerator

fun generateDaos(config: Config, schema: Schema): List<ClassGenerator> {
    return schema.tables.flatMap {
        listOf(
            generateEntity(config, it),
            generateRepository2(config, it)
        )
    }
}
