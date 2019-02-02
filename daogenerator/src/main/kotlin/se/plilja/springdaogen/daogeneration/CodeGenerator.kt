package se.plilja.springdaogen.daogeneration

import se.plilja.springdaogen.codegeneration.ClassGenerator
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Schema

fun generateCode(config: Config, schema: Schema): List<ClassGenerator> {
    return schema.tables.flatMap {
        listOf(
            generateEntity(config, it),
            generateDao(config, it)
        )
    }
}
