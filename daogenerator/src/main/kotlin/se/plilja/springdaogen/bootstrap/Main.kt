package se.plilja.springdaogen.bootstrap

import se.plilja.springdaogen.daogeneration.generateDaos
import se.plilja.springdaogen.generatedframework.*
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
        val dir = File(classGenerator.getOutputFolder())
        dir.mkdirs()
        val f = File(classGenerator.getOutputFileName())
        f.writeText(classGenerator.generate())
    }
    copyFrameworkClasses(config)
}

/**
 * Copies base classes for the generated classes to
 * the appropriate target folder.
 */
fun copyFrameworkClasses(config: Config) {
    fun writeFrameworkClass(clazz: Pair<String, String>) {
        val dir = File(config.frameworkOutputFolder + config.frameworkOutputPackage.replace(".", "/"))
        dir.mkdirs()
        File(dir.absolutePath + "/" + clazz.first + ".java").writeText(clazz.second)
    }

    fun writeTestFrameworkClass(clazz: Pair<String, String>) {
        assert(config.testRepositoryOutputFolder != null)
        assert(config.testRepositoryOutputPackage != null)
        val dir = File(config.testRepositoryOutputFolder + config.testRepositoryOutputPackage!!.replace(".", "/"))
        dir.mkdirs()
        File(dir.absolutePath + "/" + clazz.first + ".java").writeText(clazz.second)
    }

    writeFrameworkClass(baseEntity(config.frameworkOutputPackage))
    writeFrameworkClass(abstractBaseRepository(config.frameworkOutputPackage))
    writeFrameworkClass(baseRepository(config.frameworkOutputPackage))
    if (config.testRepositoryOutputFolder != null && config.testRepositoryOutputPackage != null) {
        writeTestFrameworkClass(baseTestRepository(config))
    }
    for (exceptionClass in frameworkExceptions(config.frameworkOutputPackage)) {
        writeFrameworkClass(exceptionClass)
    }
}

