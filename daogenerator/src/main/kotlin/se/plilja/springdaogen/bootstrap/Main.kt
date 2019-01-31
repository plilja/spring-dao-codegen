package se.plilja.springdaogen.bootstrap

import se.plilja.springdaogen.codegeneration.toH2Ddl
import se.plilja.springdaogen.daogeneration.generateDaos
import se.plilja.springdaogen.generatedframework.baseEntity
import se.plilja.springdaogen.generatedframework.baseRepository
import se.plilja.springdaogen.generatedframework.frameworkExceptions
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Schema
import java.io.File

fun main(args: Array<String>) {
    val config = readConfig(args)
    val schema = readSchema(config)
    writeDaos(config, schema)
    copyFrameworkClasses(config)
    writeTestDdl(config, schema)
}

private fun writeDaos(config: Config, schema: Schema) {
    val classes = generateDaos(config, schema)
    for (classGenerator in classes) {
        val dir = File(classGenerator.getOutputFolder())
        dir.mkdirs()
        val f = File(classGenerator.getOutputFileName())
        f.writeText(classGenerator.generate())
    }
}

private fun readConfig(args: Array<String>): Config {
    val config = if (args.isNotEmpty()) {
        Config.readConfig(File(args[0]))
    } else {
        Config.readConfig()
    }
    return config
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

    writeFrameworkClass(baseEntity(config.frameworkOutputPackage))
    writeFrameworkClass(baseRepository(config.frameworkOutputPackage))
    for (exceptionClass in frameworkExceptions(config.frameworkOutputPackage)) {
        writeFrameworkClass(exceptionClass)
    }
}

/**
 * Writes test sql-files if configured that creates an
 * H2-database to be used for testing.
 */
fun writeTestDdl(config: Config, schema: Schema) {
    if (config.generateTestDdl) {
        val ddl = toH2Ddl(schema)
        File(config.testResourceFolder).mkdirs()
        File(config.testResourceFolder + "/init.sql").writeText(ddl)
    }
}