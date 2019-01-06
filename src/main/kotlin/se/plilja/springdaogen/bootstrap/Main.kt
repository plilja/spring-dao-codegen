package se.plilja.springdaogen.bootstrap

import se.plilja.springdaogen.daogeneration.generateDaos
import se.plilja.springdaogen.model.Config
import java.io.File

fun main(args: Array<String>) {
    val config = if (args.isNotEmpty()) {
        Config.readConfig(File(args[0]))
    } else {
        Config.readConfig()
    }
    val schema = readSchema(config)
    val classes = generateDaos(config, schema)
    for (classGenerator in classes) {
        val dir = File("${config.outputFolder}${classGenerator.packageName.replace(".", "/")}")
        dir.mkdirs()
        val f = File("${dir.absolutePath}/${classGenerator.name}.java")
        f.writeText(classGenerator.generate())
    }
}

