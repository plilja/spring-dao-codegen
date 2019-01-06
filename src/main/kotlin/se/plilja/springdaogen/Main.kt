package se.plilja.springdaogen

fun main(args: Array<String>) {
    val schema = readSchema("jdbc:postgresql://localhost:3003/foo", "user", "password", "org.postgresql.Driver")
    generateEntities(schema)
    generateConstants(schema)
    generateRepositories(schema)
}

