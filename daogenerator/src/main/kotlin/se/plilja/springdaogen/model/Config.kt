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
    val changedAtColumnNames: List<String> = emptyList(),
    val versionColumnNames: List<String> = emptyList(),
    val enumTables: List<String> = emptyList(),
    val enumTablesRegexp: Regex = Regex("^$"),
    val enumNameColumnRegexp: Regex = Regex("^$"),
    val featureGenerateJavaxValidation: Boolean = false
) {

    companion object {

        fun readDefaultConfig(): Config {
            val f = File(Config::class.java.getResource("/settings.properties").file)
            return readDefaultConfig(f)
        }

        fun readDefaultConfig(file: File) = ConfigReader(file).readConfig()
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
            databaseName = properties.getProperty("database.name"),
            databaseDialect = DatabaseDialect.valueOf(properties.getProperty("database.dialect")),
            databaseUrl = properties.getProperty("database.url"),
            databaseUser = properties.getProperty("database.user"),
            databasePassword = properties.getProperty("database.password"),
            databaseDriver = properties.getProperty("database.driver"),
            entityOutputFolder = getFolderProperty("entity.output.folder"),
            entityOutputPackage = properties.getProperty("entity.output.package"),
            daoOutputFolder = getFolderProperty("dao.output.folder"),
            daoOutputPackage = properties.getProperty("dao.output.package"),
            frameworkOutputFolder = getFolderProperty("framework.output.folder"),
            frameworkOutputPackage = properties.getProperty("framework.output.package"),
            maxSelectAllCount = properties.getProperty("max.select.all.count").toInt(),
            schemas = getListProperty("database.schemas"),
            useLombok = properties.getProperty("use_lombok", "false") == "true",
            daosAreAbstract = properties.getProperty("dao.output.abstract", "false") == "true",
            hasGeneratedPrimaryKeysOverride = getListProperty("generated_primary_keys_override"),
            entityPrefix = properties.getProperty("entity.output.prefix", ""),
            entitySuffix = properties.getProperty("entity.output.suffix", ""),
            daoPrefix = properties.getProperty("dao.output.prefix", ""),
            daoSuffix = properties.getProperty("dao.output.suffix", "Dao"),
            generateTestDdl = properties.getProperty("test.generate_ddl", "false") == "true",
            testResourceFolder = properties.getProperty("test.resource_folder", null),
            createdAtColumnNames = getListProperty("entity.created_at_name").map { it.toUpperCase() },
            changedAtColumnNames = getListProperty("entity.changed_at_name").map { it.toUpperCase() },
            versionColumnNames = getListProperty("entity.version_name").map { it.toUpperCase() },
            enumTables = getListProperty("enum.tables"),
            enumTablesRegexp = Regex(properties.getProperty("enum.table_regex", "^$"), RegexOption.IGNORE_CASE),
            enumNameColumnRegexp = Regex(
                properties.getProperty("enum.name_column_regex", "^$"),
                RegexOption.IGNORE_CASE
            ),
            featureGenerateJavaxValidation = properties.getProperty(
                "features.generate_javax_validation",
                "false"
            ) == "true"
        )
    }

    private fun getFolderProperty(property: String): String {
        val f = properties.getProperty(property)
        if (f.last() != '/') {
            return "$f/"
        } else {
            return f
        }
    }

    private fun getListProperty(propertyName: String): List<String> {
        val property = properties.getProperty(propertyName, "").trim()
        return if (property == "") {
            emptyList()
        } else {
            property.split(",").map { it.trim() }
        }
    }

}