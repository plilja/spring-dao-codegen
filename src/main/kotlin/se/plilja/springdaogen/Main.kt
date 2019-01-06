package se.plilja.springdaogen

import java.io.File

fun main(args: Array<String>) {
    val config = Config.readConfig()
    val schema = readSchema(config)
    val classes = generateDaos(config, schema)
    for (classGenerator in classes) {
        val dir = File("${config.outputFolder}${classGenerator.packageName.replace(".", "/")}")
        dir.mkdirs()
        val f = File("${dir.absolutePath}/${classGenerator.name}.java")
        f.writeText(classGenerator.generate())
    }
}

