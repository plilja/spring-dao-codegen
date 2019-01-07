package se.plilja.springdaogen.model

import java.io.File
import java.util.*


data class Config(
    val databaseDialect: DatabaseDialect,
    val databaseUrl: String,
    val databaseUser: String,
    val databasePassword: String,
    val databaseDriver: String,
    val entityOutputFolder: String,
    val entityOutputPackage: String,
    val repositoryOutputFolder: String,
    val repositoryOutputPackage: String,
    val frameworkOutputFolder: String,
    val frameworkOutputPackage: String,
    val maxSelectAllCount: Int
) {

    companion object {

        fun readConfig(): Config {
            val f = File(Config::class.java.getResource("/settings.properties").file)
            return readConfig(f)
        }

        fun readConfig(file: File) = ConfigReader(file).readConfig()

    }
}

private class ConfigReader {
    private val properties: Properties;

    constructor(file: File) {
        val r = Properties()
        file.inputStream().use {
            r.load(it)
        }
        properties = r
    }

    fun readConfig(): Config {
        return Config(
            databaseDialect(),
            databaseUrl(),
            databaseUser(),
            databasePassword(),
            databaseDriver(),
            entityOutputFolder(),
            entityOutputPackage(),
            repositoryOutputFolder(),
            repositoryOutputPackage(),
            frameworkOutputFolder(),
            frameworkOutputPackage(),
            maxSelectAllCount()
        )
    }

    private fun databaseDialect(): DatabaseDialect {
        return DatabaseDialect.valueOf(properties.getProperty("database.dialect"))
    }

    private fun getFolderProperty(property: String): String {
        val f = properties.getProperty(property)
        if (f.last() != '/') {
            return "$f/"
        } else {
            return f
        }
    }

    private fun entityOutputFolder(): String {
        return getFolderProperty("entity.output.folder")
    }

    private fun entityOutputPackage(): String {
        return properties.getProperty("entity.output.package")
    }

    private fun repositoryOutputFolder(): String {
        return getFolderProperty("repository.output.folder")
    }

    private fun repositoryOutputPackage(): String {
        return properties.getProperty("repository.output.package")
    }

    private fun frameworkOutputFolder(): String {
        return getFolderProperty("framework.output.folder")
    }

    private fun frameworkOutputPackage(): String {
        return properties.getProperty("framework.output.package")
    }

    private fun databaseUrl(): String {
        return properties.getProperty("database.url")
    }

    private fun databaseDriver(): String {
        return properties.getProperty("database.driver")
    }

    private fun databaseUser(): String {
        return properties.getProperty("database.user")
    }

    private fun databasePassword(): String {
        return properties.getProperty("database.password")
    }

    private fun maxSelectAllCount(): Int {
        return properties.getProperty("max.select.all.count").toInt()
    }
}