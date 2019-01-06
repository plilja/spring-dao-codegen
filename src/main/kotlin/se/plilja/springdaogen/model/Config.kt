package se.plilja.springdaogen.model

import java.io.File
import java.util.*


data class Config(
    val databaseDialect: DatabaseDialect,
    val databaseUrl: String,
    val databaseUser: String,
    val databasePassword: String,
    val databaseDriver: String,
    val outputFolder: String,
    val outputPackage: String,
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
            outputFolder(),
            outputPackage(),
            maxSelectAllCount()
        )
    }

    private fun databaseDialect(): DatabaseDialect {
        return DatabaseDialect.valueOf(properties.getProperty("database.dialect"))
    }

    private fun outputFolder(): String {
        val f = properties.getProperty("output.folder")
        if (f.last() != '/') {
            return "$f/"
        } else {
            return f
        }
    }

    private fun outputPackage(): String {
        return properties.getProperty("output.package")
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