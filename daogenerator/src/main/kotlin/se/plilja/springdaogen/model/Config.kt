package se.plilja.springdaogen.model

import java.io.File
import java.util.*


data class Config(
    val databaseName: String,
    val databaseDialect: DatabaseDialect,
    val databaseUrl: String,
    val databaseUser: String,
    val databasePassword: String,
    val databaseDriver: String,
    val entityOutputFolder: String,
    val entityOutputPackage: String,
    val daoOutputFolder: String,
    val daoOutputPackage: String,
    val frameworkOutputFolder: String,
    val frameworkOutputPackage: String,
    val maxSelectAllCount: Int,
    val schemas: List<String>,
    val useLombok: Boolean,
    val daosAreAbstract: Boolean = false,
    val hasGeneratedPrimaryKeysOverride: List<String> = emptyList(),
    val entityPrefix: String = "",
    val entitySuffix: String = "",
    val daoPrefix: String = "",
    val daoSuffix: String = "Dao",
    val generateTestDdl: Boolean = false,
    val testResourceFolder: String? = null,
    val createdAtColumnNames: List<String> = emptyList(),
    val changedAtColumnNames: List<String> = emptyList()
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
            databaseName = getDatabaseName(),
            databaseDialect = databaseDialect(),
            databaseUrl = databaseUrl(),
            databaseUser = databaseUser(),
            databasePassword = databasePassword(),
            databaseDriver = databaseDriver(),
            entityOutputFolder = entityOutputFolder(),
            entityOutputPackage = entityOutputPackage(),
            daoOutputFolder = daoOutputFolder(),
            daoOutputPackage = daoOutputPackage(),
            frameworkOutputFolder = frameworkOutputFolder(),
            frameworkOutputPackage = frameworkOutputPackage(),
            maxSelectAllCount = maxSelectAllCount(),
            schemas = getSchemas(),
            useLombok = useLombok(),
            daosAreAbstract = daoAreAbstract(),
            hasGeneratedPrimaryKeysOverride = getGeneratedPrimaryKeysOverride(),
            entityPrefix = getEntityPrefix(),
            entitySuffix = getEntitySuffix(),
            daoPrefix = getDaoPrefix(),
            daoSuffix = getDaoSuffix(),
            generateTestDdl = generateTestDdl(),
            testResourceFolder = getTestResourceFolder(),
            createdAtColumnNames = getListProperty("entity.created_at_names").map { it.toUpperCase() },
            changedAtColumnNames = getListProperty("entity.changed_at_names").map { it.toUpperCase() }
        )
    }

    private fun databaseDialect() = DatabaseDialect.valueOf(properties.getProperty("database.dialect"))

    private fun getFolderProperty(property: String): String {
        val f = properties.getProperty(property)
        if (f.last() != '/') {
            return "$f/"
        } else {
            return f
        }
    }

    private fun entityOutputFolder() = getFolderProperty("entity.output.folder")

    private fun entityOutputPackage() = properties.getProperty("entity.output.package")

    private fun daoOutputFolder() = getFolderProperty("dao.output.folder")

    private fun daoOutputPackage() = properties.getProperty("dao.output.package")

    private fun getEntityPrefix() = properties.getProperty("entity.output.prefix", "")

    private fun getEntitySuffix() = properties.getProperty("entity.output.suffix", "")

    private fun getDaoPrefix() = properties.getProperty("dao.output.prefix", "")

    private fun getDaoSuffix() = properties.getProperty("dao.output.suffix", "Dao")

    private fun getTestResourceFolder() = properties.getProperty("test.resource_folder", null)

    private fun generateTestDdl() = properties.getProperty("test.generate_ddl", "false") == "true"

    private fun frameworkOutputFolder() = getFolderProperty("framework.output.folder")

    private fun frameworkOutputPackage() = properties.getProperty("framework.output.package")

    private fun databaseUrl() = properties.getProperty("database.url")

    private fun databaseDriver() = properties.getProperty("database.driver")

    private fun databaseUser() = properties.getProperty("database.user")

    private fun databasePassword() = properties.getProperty("database.password")

    private fun maxSelectAllCount() = properties.getProperty("max.select.all.count").toInt()

    private fun getSchemas() = getListProperty("database.schemas")

    private fun getGeneratedPrimaryKeysOverride() = getListProperty("generated_primary_keys_override")

    private fun getDatabaseName() = properties.getProperty("database.name")

    private fun daoAreAbstract() = properties.getProperty("dao.output.abstract", "false") == "true"

    private fun useLombok() = properties.getProperty("use_lombok", "false") == "true"

    private fun getListProperty(propertyName: String): List<String> {
        val property = properties.getProperty(propertyName, "").trim()
        return if (property == "") {
            emptyList()
        } else {
            property.split(",").map { it.trim() }
        }
    }

}