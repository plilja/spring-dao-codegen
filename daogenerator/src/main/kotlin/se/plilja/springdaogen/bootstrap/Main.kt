package se.plilja.springdaogen.bootstrap

import org.apache.log4j.Level
import org.slf4j.bridge.SLF4JBridgeHandler
import org.springframework.boot.jdbc.DataSourceBuilder
import se.plilja.springdaogen.codegeneration.toH2Ddl
import se.plilja.springdaogen.daogeneration.generateCode
import se.plilja.springdaogen.copyable.*
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.JavaVersion
import se.plilja.springdaogen.model.Schema
import java.io.File
import java.util.logging.LogManager
import javax.sql.DataSource
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    initLogging(args)
    if (args.isEmpty()) {
        System.err.println("Usage: program pathToPropertiesFile")
        System.err.println("You can also supply the flag --verbose to get extra log output")
        exitProcess(1)
    }
    println("Starting...")
    val config = readConfig(args)
    val dataSource = getDataSource(config)
    val schema = readSchema(config, dataSource)
    writeDaos(config, schema, dataSource)
    copyFrameworkClasses(config)
    writeTestDdl(config, schema, dataSource)
    close(dataSource)
    println("Done.")
}

fun initLogging(args: Array<String>) {
    LogManager.getLogManager().reset()
    SLF4JBridgeHandler.install() // Schemacrawler uses java.util.logging
    for (arg in args) {
        if (arg == "--verbose") {
            org.apache.log4j.LogManager.getRootLogger().level = Level.DEBUG
        }
    }
}

private fun writeDaos(config: Config, schema: Schema, dataSource: DataSource) {
    val classes = generateCode(config, schema, dataSource)
    for (classGenerator in classes) {
        val dir = File(classGenerator.getOutputFolder())
        dir.mkdirs()
        val f = File(classGenerator.getOutputFileName())
        f.writeText(classGenerator.generate())
    }
}

private fun readConfig(args: Array<String>): Config {
    for (arg in args) {
        if (arg != "--verbose") {
            return Config.readDefaultConfig(File(arg))
        }
    }
    throw IllegalStateException("Properties file must be provided")
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

    println("Writing framework classes")
    writeFrameworkClass(baseEntity(config.frameworkOutputPackage))
    if (config.featureGenerateQueryApi) {
        writeFrameworkClass(queryItem(config.frameworkOutputPackage))
        writeFrameworkClass(columnClass(config.frameworkOutputPackage))
        writeFrameworkClass(sortOrder(config.frameworkOutputPackage))
        writeFrameworkClass(queryable(config.frameworkOutputPackage, config))
    }
    writeFrameworkClass(baseDatabaseEnum(config.frameworkOutputPackage))
    writeFrameworkClass(dao(config.frameworkOutputPackage, config))
    for (exceptionClass in frameworkExceptions(config.frameworkOutputPackage)) {
        writeFrameworkClass(exceptionClass)
    }
    if (config.featureGenerateChangeTracking) {
        writeFrameworkClass(currentUserProvider(config.frameworkOutputPackage))
        for (entityInterface in entityInterfaces(config.frameworkOutputPackage)) {
            writeFrameworkClass(entityInterface)
        }
    }
    if (config.javaVersion == JavaVersion.Java8) {
        writeFrameworkClass(iOUtil(config.frameworkOutputPackage))
    }
}

/**
 * Writes test sql-files if configured that creates an
 * H2-database to be used for testing.
 */
fun writeTestDdl(config: Config, schema: Schema, dataSource: DataSource) {
    if (config.featureGenerateTestDdl) {
        val ddl = toH2Ddl(config, schema, dataSource)
        File(config.testResourceFolder).mkdirs()
        File(config.testResourceFolder + "/" + config.testDdlFileName).writeText(ddl)
    }
}

fun getDataSource(config: Config): DataSource {
    return DataSourceBuilder.create()
        .url(config.databaseUrl)
        .driverClassName(config.databaseDriver)
        .username(config.databaseUser)
        .password(config.databasePassword)
        .build()
}

fun close(dataSource: DataSource) {
    dataSource.connection?.close()
}
