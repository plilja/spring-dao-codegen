package se.plilja.springdaogen.daogeneration

import se.plilja.springdaogen.codegeneration.ClassFileGenerator
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Schema

fun generateDaos(config: Config, schema: Schema): List<ClassFileGenerator> {
    return schema.tables.flatMap {
        listOf(generateEntity(config, it)) +
                generateRepository(config, it) +
                listOfNotNull(generateTestRepository(config, it))
    }
}
