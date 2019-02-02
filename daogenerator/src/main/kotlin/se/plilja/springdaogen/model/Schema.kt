package se.plilja.springdaogen.model

import se.plilja.springdaogen.util.camelCase
import se.plilja.springdaogen.util.capitalizeFirst
import se.plilja.springdaogen.util.capitalizeLast
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Blob
import java.sql.Clob
import java.sql.NClob
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


data class Schema(
    val tables: List<Table>
)

data class Table(
    val schemaName: String?, // TODO should this be lifted to the schema object?
    val name: String,
    val primaryKey: Column,
    val columns: List<Column>,
    val entityPrefix: String = "",
    val entitySuffix: String = "",
    val daoPrefix: String = "", // TODO better to get the config object?
    val daoSuffix: String = ""
) {

    fun entityName(): String {
        return entityPrefix + capitalizeFirst(camelCase(name)) + entitySuffix
    }

    fun daoName(): String {
        return daoPrefix + capitalizeFirst(camelCase(name)) + daoSuffix
    }
}

data class Column(
    val name: String,
    private val javaType: Class<out Any>,
    val generated: Boolean = false
) {
    var references: Pair<Table, Column>? = null

    fun setter(): String {
        return "set${capitalizeFirst(fieldName())}"
    }

    fun type() : Class<out Any> {
        return when (javaType) {
            Clob::class.java -> ByteArray::class.java
            Blob::class.java -> ByteArray::class.java
            NClob::class.java -> ByteArray::class.java
            else -> javaType
        }
    }

    fun getter(): String {
        return "get${capitalizeFirst(fieldName())}"
    }

    fun fieldName(): String {
        val s = camelCase(name)
        if (isReservedJavaKeyword(s)) {
            return capitalizeLast(s)
        } else {
            return s
        }
    }

    fun recordSetMethod(rs: String): String {
        if (javaType == BigDecimal::class.java) {
            return "$rs.getBigDecimal(\"${name}\")"
        } else if (javaType == String::class.java) {
            return "$rs.getString(\"${name}\")"
        } else if (javaType == java.lang.Boolean::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getBoolean(\"${name}\") : null"
        } else if (javaType == Integer::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getInt(\"${name}\") : null"
        } else if (javaType == java.lang.Long::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getLong(\"${name}\") : null"
        } else if (javaType == java.lang.Float::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getFloat(\"${name}\") : null"
        } else if (javaType == java.lang.Double::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getDouble(\"${name}\") : null"
        } else if (javaType == UUID::class.java) {
            return "UUID.fromString($rs.getString(\"${name}\"))"
        } else if (javaType == LocalDate::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getDate(\"${name}\").toLocalDate() : null"
        } else if (javaType == LocalDateTime::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getTimestamp(\"${name}\").toLocalDateTime() : null"
        } else if (javaType == Clob::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getClob(\"${name}\").getAsciiStream().readAllBytes() : null"
        } else if (javaType == Blob::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getBlob(\"${name}\").getBinaryStream().readAllBytes() : null"
        } else if (javaType == NClob::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getNClob(\"${name}\").getAsciiStream().readAllBytes() : null"
        } else if (javaType == BigInteger::class.java) {
            return "$rs.getObject(\"${name}\") != null ? $rs.getBigDecimal(\"${name}\").toBigInteger() : null"
        } else {
            return "(${javaType.simpleName}) $rs.getObject(\"${name}\")"
        }
    }
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