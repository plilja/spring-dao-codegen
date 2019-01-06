package se.plilja.springdaogen.model

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
        private val properties: Properties by lazy {
            val r = Properties()
            Config::class.java.getResource("/settings.properties").openStream().use {
                r.load(it)
            }
            r
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

        private fun databaseDialect() : DatabaseDialect {
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
}