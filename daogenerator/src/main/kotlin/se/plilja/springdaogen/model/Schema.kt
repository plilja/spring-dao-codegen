package se.plilja.springdaogen.model

import se.plilja.springdaogen.util.camelCase
import se.plilja.springdaogen.util.capitalizeFirst
import se.plilja.springdaogen.util.capitalizeLast
import se.plilja.springdaogen.util.snakeCase
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Blob
import java.sql.Clob
import java.sql.NClob
import java.sql.SQLXML
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*


data class Schema(
    val tables: List<Table>
)

data class Table(
    val schemaName: String?, // TODO should this be lifted to the schema object?
    val name: String,
    val primaryKey: Column,
    val columns: List<Column>,
    val config: Config
) {

    fun isEnum(): Boolean {
        return name in config.enumTables || config.enumTablesRegexp.matches(name)
    }

    fun entityName(): String {
        return if (isEnum()) {
            capitalizeFirst(camelCase(name))
        } else {
            config.entityPrefix + capitalizeFirst(camelCase(name)) + config.entitySuffix
        }
    }

    fun daoName(): String {
        return config.daoPrefix + capitalizeFirst(camelCase(name)) + config.daoSuffix
    }

    fun containsClobLikeField(): Boolean {
        return columns.map { it.isClobLike() }.any { it }
    }

    fun changedAtColumn(): Column? {
        return columns.firstOrNull { it.isChangedAtColumn() }
    }

    fun createdAtColumn(): Column? {
        return columns.firstOrNull { it.isCreatedAtColumn() }
    }

    fun versionColumn(): Column? {
        return columns.firstOrNull { it.isVersionColumn() }
    }
}

data class Column(
    val name: String,
    private val javaType: Class<out Any>,
    private val config: Config,
    val generated: Boolean = false
) {
    var references: Pair<Table, Column>? = null

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

    fun jdbcSqlType(): String? {
        return when (javaType) {
            // This is not a complete list. It seems like sometimes
            // the type hint helps and sometimes it causes trouble.
            java.lang.Double::class.java -> "Types.DOUBLE"
            java.lang.Float::class.java -> "Types.FLOAT"
            java.lang.Boolean::class.java -> "Types.BOOLEAN"
            BigDecimal::class.java -> "Types.NUMERIC"
            LocalDate::class.java -> "Types.DATE"
            SQLXML::class.java -> "Types.SQLXML"
            else -> null
        }
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
        if (isReservedJavaKeyword(s)) {
            return capitalizeLast(s)
        } else {
            return s
        }
    }

    fun recordSetMethod(rs: String): String {
        val method = if (javaType == BigDecimal::class.java) {
            "$rs.getBigDecimal(\"$name\")"
        } else if (javaType == String::class.java || javaType == SQLXML::class.java) {
            "$rs.getString(\"$name\")"
        } else if (javaType == java.lang.Boolean::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getBoolean(\"$name\") : null"
        } else if (isVersionColumn() || javaType == Integer::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getInt(\"$name\") : null"
        } else if (javaType == java.lang.Long::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getLong(\"$name\") : null"
        } else if (javaType == java.lang.Float::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getFloat(\"$name\") : null"
        } else if (javaType == java.lang.Double::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getDouble(\"$name\") : null"
        } else if (javaType == UUID::class.java) {
            "UUID.fromString($rs.getString(\"$name\"))"
        } else if (javaType == LocalDate::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getDate(\"$name\").toLocalDate() : null"
        } else if (javaType == LocalTime::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getTime(\"$name\").toLocalTime() : null"
        } else if (javaType == LocalDateTime::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getTimestamp(\"$name\").toLocalDateTime() : null"
        } else if (javaType == Clob::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getClob(\"$name\").getSubString(1, (int) $rs.getClob(\"$name\").length()) : null"
        } else if (javaType == Blob::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getBlob(\"$name\").getBinaryStream().readAllBytes() : null"
        } else if (javaType == NClob::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getNClob(\"$name\").getSubString(1, (int) $rs.getClob(\"$name\").length()) : null"
        } else if (javaType == BigInteger::class.java) {
            "$rs.getObject(\"$name\") != null ? $rs.getBigDecimal(\"$name\").toBigInteger() : null"
        } else {
            "(${javaType.simpleName}) $rs.getObject(\"$name\")"
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
