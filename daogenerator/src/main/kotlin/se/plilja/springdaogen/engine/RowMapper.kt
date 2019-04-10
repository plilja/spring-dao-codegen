package se.plilja.springdaogen.engine

import se.plilja.springdaogen.classgenerators.ClassGenerator
import se.plilja.springdaogen.copyable.databaseException
import se.plilja.springdaogen.copyable.iOUtil
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.JavaVersion
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.model.TableOrView
import java.io.IOException
import java.sql.Types
import kotlin.reflect.full.staticProperties


fun rowMapper(tableOrView: TableOrView, classGenerator: ClassGenerator, config: Config): String {
    val containsBlobLike = tableOrView.containsClobLikeField()
    val setters = tableOrView.columns.map { "r.${it.setter()}(${it.recordSetMethod("rs")});" }.joinToString("\n")
    return if (containsBlobLike) {
        classGenerator.addImport("${config.frameworkOutputPackage}.${databaseException(config.frameworkOutputPackage).first}")
        if (config.javaVersion == JavaVersion.Java8) {
            classGenerator.addImport("${config.frameworkOutputPackage}.${iOUtil(config.frameworkOutputPackage).first}")
        }
        classGenerator.addImport(IOException::class.java)
        """(rs, i) -> {
                try {
                ${tableOrView.entityName()} r = new ${tableOrView.entityName()}();
                $setters
                return r;
            } catch (IOException ex) {
                throw new DatabaseException("Caught exception while reading row", ex);
            }
        }"""
    } else {
        """(rs, i) -> {
                ${tableOrView.entityName()} r = new ${tableOrView.entityName()}();
                $setters
                return r;
              }"""
    }
}

fun rowUnmapper(tableOrView: TableOrView, classGenerator: ClassGenerator): String {
    val attributes = tableOrView.columns.map { column ->
        val jdbcType = column.jdbcType
        var typeDeclaration = ""
        if (jdbcType != null) {
            typeDeclaration = ""
            for (member in Types::class.staticProperties) {
                if (jdbcType.vendorTypeNumber == member.get()) {
                    typeDeclaration = ", Types.${member.name}"
                }
            }
            classGenerator.addImport(Types::class.java)
        }

        if (tableOrView is Table && tableOrView.primaryKey == column) {
            "m.addValue(\"${column.name}\", o.getId()$typeDeclaration);"
        } else if (column.references != null && column.references!!.first.isEnum()) {
            "m.addValue(\"${column.name}\", o.${column.getter()}() != null ? o.${column.getter()}().getId() : null$typeDeclaration);"
        } else {
            "m.addValue(\"${column.name}\", o.${column.getter()}()$typeDeclaration);"
        }
    }.joinToString("\n")
    return """
            @Override
            protected SqlParameterSource getParams(${tableOrView.entityName()} o) {
                MapSqlParameterSource m = new MapSqlParameterSource();
                $attributes
                return m;
            }
    """.trimMargin()
}

