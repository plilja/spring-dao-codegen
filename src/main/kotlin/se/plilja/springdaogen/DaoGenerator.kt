package se.plilja.springdaogen

fun generateDaos(config: Config, schema: Schema): List<ClassGenerator> {
    return schema.tables.flatMap {
        listOf(
            generateEntity(config, it),
            generateConstants(config, it),
            generateRepository(config, it)
        )
    }
}
