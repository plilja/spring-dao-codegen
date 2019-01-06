package se.plilja.springdaogen

import java.io.File

fun main(args: Array<String>) {
    val schema = readSchema("jdbc:postgresql://localhost:3003/foo", "user", "password", "org.postgresql.Driver")
    val classes = generateDaos(schema)
    File(Config.outputFolder()).mkdirs() // Ensure folder is present
    for (classGenerator in classes) {
        File(Config.outputFolder() + classGenerator.name + ".java").writeText(classGenerator.generate())
    }
}

