package se.plilja.springdaogen

fun generateDaos(schema: Schema): List<ClassGenerator> {
    return schema.tables.flatMap {
        listOf(
            generateEntity(it),
            generateConstants(it),
            generateRepository(it)
        )
    }
}
