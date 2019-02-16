package se.plilja.springdaogen.bootstrap

import org.springframework.boot.jdbc.DataSourceBuilder
import se.plilja.springdaogen.codegeneration.toH2Ddl
import se.plilja.springdaogen.daogeneration.generateCode
import se.plilja.springdaogen.generatedframework.baseDatabaseEnum
import se.plilja.springdaogen.generatedframework.baseEntity
import se.plilja.springdaogen.generatedframework.columnClass
import se.plilja.springdaogen.generatedframework.currentUserProvider
import se.plilja.springdaogen.generatedframework.dao
import se.plilja.springdaogen.generatedframework.entityInterfaces
import se.plilja.springdaogen.generatedframework.frameworkExceptions
import se.plilja.springdaogen.generatedframework.queryItem
import se.plilja.springdaogen.generatedframework.sortOrder
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Schema
import java.io.File
import javax.sql.DataSource

fun main(args: Array<String>) {
    val config = readConfig(args)
    val dataSource = getDataSource(config)
    val schema = readSchema(config, dataSource)
    writeDaos(config, schema, dataSource)
    copyFrameworkClasses(config)
    writeTestDdl(config, schema, dataSource)
    close(dataSource)
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
    return if (args.isNotEmpty()) {
        Config.readDefaultConfig(File(args[0]))
    } else {
        Config.readDefaultConfig()
    }
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
    if (config.featureGenerateQueryApi) {
        writeFrameworkClass(queryItem(config.frameworkOutputPackage))
        writeFrameworkClass(columnClass(config.frameworkOutputPackage))
        writeFrameworkClass(sortOrder(config.frameworkOutputPackage))
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
}

/**
 * Writes test sql-files if configured that creates an
 * H2-database to be used for testing.
 */
fun writeTestDdl(config: Config, schema: Schema, dataSource: DataSource) {
    if (config.generateTestDdl) {
        val ddl = toH2Ddl(config, schema, dataSource)
        File(config.testResourceFolder).mkdirs()
        File(config.testResourceFolder + "/init.sql").writeText(ddl)
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
    val conn = dataSource.connection
    if (conn != null) {
        conn.close()
    }
}
