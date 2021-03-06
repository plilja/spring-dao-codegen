package se.plilja.springdaogen.engine.model

import se.plilja.springdaogen.config.Config
import se.plilja.springdaogen.config.JavaVersion
import se.plilja.springdaogen.util.camelCase
import se.plilja.springdaogen.util.capitalizeFirst
import se.plilja.springdaogen.util.capitalizeLast
import se.plilja.springdaogen.util.snakeCase
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.util.*


data class Schema(
        val schemaName: String?,
        val tables: MutableList<Table>,
        val views: MutableList<View>
)

interface TableOrView {
    var schema: Schema
    val name: String
    val columns: List<Column>
    val config: Config

    fun isEnum(): Boolean {
        return name.toUpperCase() in config.enumTables || config.enumTablesRegexp.matches(name)
    }

    fun entityName(): String {
        return if (isEnum()) {
            capitalizeFirst(camelCase(name))
        } else {
            config.entityPrefix + capitalizeFirst(camelCase(name)) + config.entitySuffix
        }
    }

    fun nameColumn(): Column? {
        return columns.firstOrNull { it.name.toLowerCase().contains("name") }
    }

    fun containsClobLikeField(): Boolean {
        return columns.map { it.isClobLike() }.any { it }
    }
}

data class View(
        override var schema: Schema,
        override val name: String,
        override val columns: List<Column>,
        override val config: Config
) : TableOrView {

    fun queryableName(): String {
        return config.viewPrefix + capitalizeFirst(camelCase(name)) + config.viewSuffix
    }
}

data class Table(
        override var schema: Schema,
        override val name: String,
        val primaryKey: Column,
        override val columns: List<Column>,
        override val config: Config
) : TableOrView {

    fun createdAtColumn(): Column? {
        return columns.firstOrNull { it.isCreatedAtColumn() }
    }

    fun changedAtColumn(): Column? {
        return columns.firstOrNull { it.isChangedAtColumn() }
    }

    fun createdByColumn(): Column? {
        return columns.firstOrNull { it.isCreatedByColumn() }
    }

    fun changedByColumn(): Column? {
        return columns.firstOrNull { it.isChangedByColumn() }
    }

    fun versionColumn(): Column? {
        return columns.firstOrNull { it.isVersionColumn() }
    }

    fun daoName(): String {
        return config.daoPrefix + capitalizeFirst(camelCase(name)) + config.daoSuffix
    }

}

data class Column(
        val name: String,
        private val javaType: Class<out Any>,
        private val config: Config,
        val generated: Boolean = false,
        val jdbcType: JDBCType? = null,
        val nullable: Boolean = false,
        val size: Int = 100,
        val precision: Int = 0
) {
    var references: Pair<TableOrView, Column>? = null

    fun isChangedAtColumn(): Boolean {
        val types = listOf(
                LocalDate::class.java,
                LocalDateTime::class.java
        )
        return config.changedAtColumnNames.contains(name.toUpperCase()) && javaType in types
    }

    fun isCreatedAtColumn(): Boolean {
        val types = listOf(
                LocalDate::class.java,
                LocalDateTime::class.java
        )
        return config.createdAtColumnNames.contains(name.toUpperCase()) && javaType in types
    }

    fun isCreatedByColumn(): Boolean {
        val types = listOf(
                String::class.java
        )
        return config.createdByColumnNames.contains(name.toUpperCase()) && javaType in types
    }

    fun isChangedByColumn(): Boolean {
        val types = listOf(
                String::class.java
        )
        return config.changedByColumnNames.contains(name.toUpperCase()) && javaType in types
    }

    fun isVersionColumn(): Boolean {
        val types = listOf(
                java.lang.Integer::class.java,
                java.lang.Long::class.java,
                java.lang.Short::class.java,
                java.lang.Byte::class.java
        )
        return config.versionColumnNames.contains(name.toUpperCase()) && javaType in types
    }

    fun setter(): String {
        return "set${capitalizeFirst(fieldName())}"
    }

    fun typeName(): String {
        val t = type()
        return when (t) {
            is Left -> t.value.simpleName
            is Right -> t.value
        }
    }

    fun type(): Either<Class<out Any>, String> {
        return if (referencesEnum()) {
            Right(references!!.first.entityName())
        } else {
            Left(rawType())
        }
    }

    fun rawType(): Class<out Any> {
        if (isVersionColumn()) {
            return java.lang.Integer::class.java
        }
        return when (javaType) {
            Clob::class.java -> String::class.java
            Blob::class.java -> ByteArray::class.java
            NClob::class.java -> String::class.java
            SQLXML::class.java -> String::class.java
            else -> javaType
        }
    }

    fun isClobLike(): Boolean {
        return listOf(Clob::class.java, Blob::class.java, NClob::class.java).contains(javaType)
    }

    fun getter(): String {
        return "get${capitalizeFirst(fieldName())}"
    }

    fun fieldName(): String {
        val adjustedName = if (referencesEnum()) {
            if (snakeCase(name).endsWith("_id")) {
                name.substring(0, name.length - 3)
            } else {
                name
            }
        } else {
            name
        }
        val s = camelCase(adjustedName)
        return if (isReservedJavaKeyword(s)) {
            capitalizeLast(s)
        } else {
            s
        }
    }

    fun recordSetMethod(rs: String): String {
        var method: String? = null

        // These method are the same no matter if the column is nullable or not
        if (javaType == BigDecimal::class.java) {
            method = "$rs.getBigDecimal(\"$name\")"
        } else if (javaType == String::class.java || javaType == SQLXML::class.java) {
            method = "$rs.getString(\"$name\")"
        } else if (javaType == OffsetDateTime::class.java) {
            method = "$rs.getObject(\"$name\", OffsetDateTime.class)"
        }

        if (nullable) {
            if (javaType == java.lang.Boolean::class.java) {
                method = "$rs.getObject(\"$name\") != null ? $rs.getBoolean(\"$name\") : null"
            } else if (isVersionColumn() || javaType == Integer::class.java) {
                method = "$rs.getObject(\"$name\") != null ? $rs.getInt(\"$name\") : null"
            } else if (javaType == java.lang.Long::class.java) {
                method = "$rs.getObject(\"$name\") != null ? $rs.getLong(\"$name\") : null"
            } else if (javaType == java.lang.Float::class.java) {
                method = "$rs.getObject(\"$name\") != null ? $rs.getFloat(\"$name\") : null"
            } else if (javaType == java.lang.Double::class.java) {
                method = "$rs.getObject(\"$name\") != null ? $rs.getDouble(\"$name\") : null"
            } else if (javaType == UUID::class.java) {
                method = "$rs.getObject(\"$name\") != null ? UUID.fromString($rs.getString(\"$name\")) : null"
            } else if (javaType == LocalDate::class.java) {
                method = "$rs.getObject(\"$name\") != null ? $rs.getDate(\"$name\").toLocalDate() : null"
            } else if (javaType == LocalTime::class.java) {
                method = "$rs.getObject(\"$name\") != null ? $rs.getTime(\"$name\").toLocalTime() : null"
            } else if (javaType == LocalDateTime::class.java) {
                method = "$rs.getObject(\"$name\") != null ? $rs.getTimestamp(\"$name\").toLocalDateTime() : null"
            } else if (javaType == Clob::class.java) {
                method =
                        "$rs.getObject(\"$name\") != null ? $rs.getClob(\"$name\").getSubString(1, (int) $rs.getClob(\"$name\").length()) : null"
            } else if (javaType == Blob::class.java) {
                if (config.javaVersion == JavaVersion.Java8) {
                    method = "$rs.getObject(\"$name\") != null ? IOUtil.readAllBytes($rs.getBlob(\"$name\").getBinaryStream()) : null"
                } else {
                    method = "$rs.getObject(\"$name\") != null ? $rs.getBlob(\"$name\").getBinaryStream().readAllBytes() : null"
                }
            } else if (javaType == NClob::class.java) {
                method =
                        "$rs.getObject(\"$name\") != null ? $rs.getNClob(\"$name\").getSubString(1, (int) $rs.getClob(\"$name\").length()) : null"
            } else if (javaType == BigInteger::class.java) {
                method = "$rs.getObject(\"$name\") != null ? $rs.getBigDecimal(\"$name\").toBigInteger() : null"
            }
        } else {
            if (javaType == java.lang.Boolean::class.java) {
                method = "$rs.getBoolean(\"$name\")"
            } else if (isVersionColumn() || javaType == Integer::class.java) {
                method = "$rs.getInt(\"$name\")"
            } else if (javaType == java.lang.Long::class.java) {
                method = "$rs.getLong(\"$name\")"
            } else if (javaType == java.lang.Float::class.java) {
                method = "$rs.getFloat(\"$name\")"
            } else if (javaType == java.lang.Double::class.java) {
                method = "$rs.getDouble(\"$name\")"
            } else if (javaType == UUID::class.java) {
                method = "UUID.fromString($rs.getString(\"$name\"))"
            } else if (javaType == LocalDate::class.java) {
                method = "$rs.getDate(\"$name\").toLocalDate()"
            } else if (javaType == LocalTime::class.java) {
                method = "$rs.getTime(\"$name\").toLocalTime()"
            } else if (javaType == LocalDateTime::class.java) {
                method = "$rs.getTimestamp(\"$name\").toLocalDateTime()"
            } else if (javaType == Clob::class.java) {
                method = "$rs.getClob(\"$name\").getSubString(1, (int) $rs.getClob(\"$name\").length())"
            } else if (javaType == Blob::class.java) {
                if (config.javaVersion == JavaVersion.Java8) {
                    method = "IOUtil.readAllBytes($rs.getBlob(\"$name\").getBinaryStream())"
                } else {
                    method = "$rs.getBlob(\"$name\").getBinaryStream().readAllBytes()"
                }
            } else if (javaType == NClob::class.java) {
                method = "$rs.getNClob(\"$name\").getSubString(1, (int) $rs.getClob(\"$name\").length())"
            } else if (javaType == BigInteger::class.java) {
                method = "$rs.getBigDecimal(\"$name\").toBigInteger()"
            }
        }
        if (method == null) {
            method = "(${javaType.simpleName}) $rs.getObject(\"$name\")"
        }
        return if (referencesEnum()) {
            "${references!!.first.entityName()}.fromId($method)"
        } else {
            method
        }
    }

    private fun referencesEnum() = references != null && references!!.first.isEnum()
}

fun isReservedJavaKeyword(s: String): Boolean {
    return s in setOf(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "double", "do",
            "else", "enum", "extends", "false", "final", "finally", "float",
            "for", "goto", "if", "implements", "import", "instanceof", "int",
            "interface", "long", "native", "new", "null", "package", "private",
            "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "true", "try", "void", "volatile", "while"
    )
}

sealed class Either<out A, out B>
data class Left<A>(val value: A) : Either<A, Nothing>()
data class Right<B>(val value: B) : Either<Nothing, B>()
