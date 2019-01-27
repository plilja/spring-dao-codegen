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
    val repositoryOutputFolder: String,
    val repositoryOutputPackage: String,
    val frameworkOutputFolder: String,
    val frameworkOutputPackage: String,
    val maxSelectAllCount: Int,
    val schemas: List<String>,
    val useLombok: Boolean,
    val repositoriesAreAbstract: Boolean = false,
    val hasGeneratedPrimaryKeysOverride: List<String> = emptyList(),
    val entityPrefix: String = "",
    val entitySuffix: String = "",
    val repositoryPrefix: String = "",
    val repositorySuffix: String = "Repository",
    val testRepositoryPrefix: String = "",
    val testRepositorySuffix: String = "",
    val testRepositoryOutputFolder: String? = null,
    val testRepositoryOutputPackage: String? = null
) {

    fun generateTestClasses() = testRepositoryOutputFolder != null && testRepositoryOutputPackage != null

    companion object {

        fun readConfig(): Config {
            val f = File(Config::class.java.getResource("/settings.properties").file)
            return readConfig(f)
        }

        fun readConfig(file: File) = ConfigReader(file).readConfig()

    }
}

private class ConfigReader(file: File) {
    private val properties: Properties;

    init {
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
            repositoryOutputFolder = repositoryOutputFolder(),
            repositoryOutputPackage = repositoryOutputPackage(),
            frameworkOutputFolder = frameworkOutputFolder(),
            frameworkOutputPackage = frameworkOutputPackage(),
            maxSelectAllCount = maxSelectAllCount(),
            schemas = getSchemas(),
            useLombok = useLombok(),
            repositoriesAreAbstract = repositoriesAreAbstract(),
            hasGeneratedPrimaryKeysOverride = getGeneratedPrimaryKeysOverride(),
            entityPrefix = getEntityPrefix(),
            entitySuffix = getEntitySuffix(),
            repositoryPrefix = getRepositoryPrefix(),
            repositorySuffix = getRepositorySuffix(),
            testRepositoryPrefix = getTestRepositoryPrefix(),
            testRepositorySuffix = getTestRepositorySuffix(),
            testRepositoryOutputFolder = testRepositoryOutputFolder(),
            testRepositoryOutputPackage = testRepositoryOutputPackage()
        )
    }

    private fun databaseDialect() = DatabaseDialect.valueOf(properties.getProperty("database.dialect"))

    private fun getFolderProperty(property: String) = getOptionalFolderProperty(property)!!

    private fun getOptionalFolderProperty(property: String): String? {
        if (properties.containsKey(property)) {
            val f = properties.getProperty(property)
            if (f.last() != '/') {
                return "$f/"
            } else {
                return f
            }
        } else {
            return null
        }
    }

    private fun entityOutputFolder() = getFolderProperty("entity.output.folder")

    private fun entityOutputPackage() = properties.getProperty("entity.output.package")

    private fun repositoryOutputFolder() = getFolderProperty("repository.output.folder")

    private fun repositoryOutputPackage() = properties.getProperty("repository.output.package")

    private fun testRepositoryOutputFolder() = getOptionalFolderProperty("test_repository.output.folder")

    private fun testRepositoryOutputPackage() = properties.getProperty("test_repository.output.package", null)

    private fun getEntityPrefix() = properties.getProperty("entity.output.prefix", "")

    private fun getEntitySuffix() = properties.getProperty("entity.output.suffix", "")

    private fun getRepositoryPrefix() = properties.getProperty("repository.output.prefix", "")

    private fun getRepositorySuffix() = properties.getProperty("repository.output.suffix", "Repository")

    private fun getTestRepositoryPrefix() = properties.getProperty("test_repository.output.prefix", "")

    private fun getTestRepositorySuffix() = properties.getProperty("test_repository.output.suffix", "Repository")

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

    private fun repositoriesAreAbstract() = properties.getProperty("repository.output.abstract", "false") == "true"

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