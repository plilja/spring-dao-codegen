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
        val daoOutputFolder: String,
        val daoOutputPackage: String,
        val frameworkOutputFolder: String,
        val frameworkOutputPackage: String,
        val maxSelectAllCount: Int,
        val schemas: List<String>,
        val featuresUseLombok: Boolean,
        val daosAreAbstract: Boolean = false,
        val includeTables: List<String> = emptyList(),
        val excludeTables: List<String> = emptyList(),
        val hasGeneratedPrimaryKeysOverride: List<String> = emptyList(),
        val entityPrefix: String = "",
        val entitySuffix: String = "",
        val daoPrefix: String = "",
        val daoSuffix: String = "Dao",
        val viewPrefix: String = "",
        val viewSuffix: String = "View",
        val featureGenerateTestDdl: Boolean = false,
        val testResourceFolder: String? = null,
        val testDdlFileName: String = "init.sql",
        val createdAtColumnNames: List<String> = emptyList(),
        val changedAtColumnNames: List<String> = emptyList(),
        val createdByColumnNames: List<String> = emptyList(),
        val changedByColumnNames: List<String> = emptyList(),
        val versionColumnNames: List<String> = emptyList(),
        val javaVersion: JavaVersion = JavaVersion.Java11,
        val enumTables: List<String> = emptyList(),
        val enumTablesRegexp: Regex = Regex("^$"),
        val enumNameColumnRegexp: Regex = Regex("^$"),
        val featureGenerateJavaxValidation: Boolean = false,
        val featureGenerateJacksonAnnotations: Boolean = false,
        val featureGenerateQueryApi: Boolean = true,
        val featureGenerateChangeTracking: Boolean = false
) {

    companion object {
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
                databaseDialect = DatabaseDialect.valueOf(properties.getProperty("database.dialect")),
                databaseUrl = properties.getProperty("database.url"),
                databaseUser = properties.getProperty("database.user"),
                databasePassword = properties.getProperty("database.password"),
                databaseDriver = properties.getProperty("database.driver"),
                entityOutputFolder = getFolderProperty("entity.output_folder"),
                entityOutputPackage = properties.getProperty("entity.output_package"),
                daoOutputFolder = getFolderProperty("dao.output_folder"),
                daoOutputPackage = properties.getProperty("dao.output_package"),
                frameworkOutputFolder = getFolderProperty("framework.output_folder"),
                frameworkOutputPackage = properties.getProperty("framework.output_package"),
                maxSelectAllCount = properties.getProperty("dao.max_select_count", "1500").toInt(),
                schemas = getListProperty("database.schemas"),
                includeTables = getListProperty("database.include_tables"),
                excludeTables = getListProperty("database.exclude_tables"),
                daosAreAbstract = properties.getProperty("dao.generate_abstract", "false") == "true",
                hasGeneratedPrimaryKeysOverride = getListProperty("generated_primary_keys_override"),
                entityPrefix = properties.getProperty("entity.output_prefix", ""),
                entitySuffix = properties.getProperty("entity.output_suffix", ""),
                daoPrefix = properties.getProperty("dao.output_prefix", ""),
                daoSuffix = properties.getProperty("dao.output_suffix", "Dao"),
                viewPrefix = properties.getProperty("view.output_prefix", ""),
                viewSuffix = properties.getProperty("view.output_suffix", "View"),
                testResourceFolder = properties.getProperty("test.resource_folder", null),
                testDdlFileName = properties.getProperty("test.ddl_file_name", "init.sql"),
                createdAtColumnNames = getListProperty("entity.created_at_name").map { it.toUpperCase() },
                changedAtColumnNames = getListProperty("entity.changed_at_name").map { it.toUpperCase() },
                createdByColumnNames = getListProperty("entity.created_by_name").map { it.toUpperCase() },
                changedByColumnNames = getListProperty("entity.changed_by_name").map { it.toUpperCase() },
                versionColumnNames = getListProperty("entity.version_name").map { it.toUpperCase() },
                javaVersion = JavaVersion.fromVersionNumber(properties.getProperty("java.version", "11")),
                enumTables = getListProperty("enum.tables"),
                enumTablesRegexp = Regex(properties.getProperty("enum.table_regex", "^$"), RegexOption.IGNORE_CASE),
                enumNameColumnRegexp = Regex(
                        properties.getProperty("enum.name_column_regex", "^$"),
                        RegexOption.IGNORE_CASE
                ),
                featuresUseLombok = properties.getProperty("features.use_lombok", "false") == "true",
                featureGenerateTestDdl = properties.getProperty("features.generate_test_ddl", "false") == "true",
                featureGenerateJavaxValidation = properties.getProperty(
                        "features.generate_javax_validation",
                        "false"
                ) == "true",
                featureGenerateJacksonAnnotations = properties.getProperty(
                        "features.generate_jackson_annotations",
                        "false"
                ) == "true",
                featureGenerateQueryApi = properties.getProperty(
                        "features.generate_query_api",
                        "true"
                ) == "true",
                featureGenerateChangeTracking = properties.getProperty(
                        "features.generate_change_tracking",
                        "false"
                ) == "true"
        )
    }

    private fun getFolderProperty(property: String): String {
        val f = properties.getProperty(property, null) ?: throw IllegalStateException("Property $property missing")
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