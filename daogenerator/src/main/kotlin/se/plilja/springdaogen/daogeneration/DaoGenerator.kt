package se.plilja.springdaogen.daogeneration


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository
import se.plilja.springdaogen.codegeneration.ClassGenerator
import se.plilja.springdaogen.generatedframework.dao
import se.plilja.springdaogen.generatedframework.databaseException
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.sqlgeneration.*
import java.io.IOException
import java.math.BigDecimal
import java.sql.Blob
import java.sql.Clob
import java.sql.NClob
import java.sql.Types
import java.util.*


fun generateDao(config: Config, table: Table): ClassGenerator {
    val g = ClassGenerator(table.daoName(), config.daoOutputPackage, config.daoOutputFolder)
    if (!config.daosAreAbstract) {
        g.addClassAnnotation("@Repository")
        g.addImport(Repository::class.java)
    }
    g.extends = "Dao<${table.entityName()}, ${table.primaryKey.typeName()}>"
    g.addPrivateConstant(
        "ROW_MAPPER", "RowMapper<${table.entityName()}>",
        rowMapper(table, g)
    )
    g.addPrivateConstant("ALL_COLUMNS", "String", columnsList(table, config.databaseDialect))
    g.addCustomMethod(rowUnmapper(table, g))
    g.addImport(RowMapper::class.java)
    g.addImport(MapSqlParameterSource::class.java)
    g.addImport(SqlParameterSource::class.java)
    if (config.daosAreAbstract) {
        g.isAbstract = true
        g.addCustomConstructor(
            """
            public ${g.name}(NamedParameterJdbcTemplate jdbcTemplate) {
                super(${table.primaryKey.typeName()}.class, ${table.primaryKey.generated}, jdbcTemplate);
            }
        """
        )
    } else {
        g.addImport(Autowired::class.java)
        g.addCustomConstructor(
            """
            @Autowired
            public ${g.name}(NamedParameterJdbcTemplate jdbcTemplate) {
                super(${table.primaryKey.typeName()}.class, ${table.primaryKey.generated}, jdbcTemplate);
            }
        """
        )
    }
    g.addImport(NamedParameterJdbcTemplate::class.java)

    g.addCustomMethod(
        """
            @Override
            protected RowMapper<${table.entityName()}> getRowMapper() {
                return ROW_MAPPER;
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getExistsByIdSql() {
                return ${existsById(table, config.databaseDialect)};
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getSelectIdsSql() {
                return ${selectOne(table, config.databaseDialect)};
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getSelectManySql(int maxSelectCount) {
                return String.format(${selectMany(table, config.databaseDialect)}, maxSelectCount);
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getSelectPageSql(long start, int pageSize) {
                return ${selectPage(table, config.databaseDialect)}
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getInsertSql() {
                return ${insert(table, config.databaseDialect)};
            }
        """
    )
    val updateString = update(table, config.databaseDialect)
    g.addCustomMethod(
        """
            @Override
            protected String getUpdateSql() {
                ${if (updateString != null) "return $updateString;" else "throw new UnsupportedOperationException();"}
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getDeleteSql() {
                return ${delete(table, config.databaseDialect)};
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getCountSql() {
                return ${count(table, config.databaseDialect)};
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getLockSql() {
                return ${lock(table, config.databaseDialect)};
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getPrimaryKeyColumnName() {
                return "${table.primaryKey.name}";
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected int getSelectAllDefaultMaxCount() {
                return ${config.maxSelectAllCount};
            }
        """
    )

    val mayNeedImport =
        listOf(UUID::class.java, Clob::class.java, NClob::class.java, Blob::class.java, BigDecimal::class.java)
    for (column in table.columns) {
        if (column.rawType() in mayNeedImport) {
            g.addImport(column.rawType())
        }
    }
    if (config.entityOutputPackage != config.daoOutputPackage) {
        g.addImport("${config.entityOutputPackage}.${table.entityName()}")
    }
    if (config.frameworkOutputPackage != config.daoOutputPackage) {
        g.addImport("${config.frameworkOutputPackage}.${dao(config.frameworkOutputPackage).first}")
        g.addImport("${config.frameworkOutputPackage}.${databaseException(config.frameworkOutputPackage).first}")
    }
    return g
}

private fun rowMapper(table: Table, classGenerator: ClassGenerator): String {
    val needsTryCatch = table.containsClobLikeField()
    val setters = table.columns.map { "r.${it.setter()}(${it.recordSetMethod("rs")});" }.joinToString("\n")
    return if (needsTryCatch) {
        classGenerator.addImport(IOException::class.java)
        """(rs, i) -> {
                try {
                ${table.entityName()} r = new ${table.entityName()}();
                $setters
                return r;
            } catch (IOException ex) {
                throw new DatabaseException("Caught exception while reading row", ex);
            }
        }"""
    } else {
        """(rs, i) -> {
                ${table.entityName()} r = new ${table.entityName()}();
                $setters
                return r;
              }"""
    }
}

private fun rowUnmapper(table: Table, classGenerator: ClassGenerator): String {
    val attributes = table.columns.map {
        val jdbcSqlType = it.jdbcSqlType()
        var typeDeclaration = ""
        if (jdbcSqlType != null) {
            typeDeclaration = ", $jdbcSqlType"
            classGenerator.addImport(Types::class.java)
        }

        if (table.primaryKey == it) {
            "m.addValue(\"${it.name}\", o.getId()$typeDeclaration);"
        } else if (it.references != null && it.references!!.first.isEnum()) {
            "m.addValue(\"${it.name}\", o.${it.getter()}() != null ? o.${it.getter()}().getId() : null$typeDeclaration);"
        } else {
            "m.addValue(\"${it.name}\", o.${it.getter()}()$typeDeclaration);"
        }
    }.joinToString("\n")
    return """
            @Override
            public SqlParameterSource getParams(${table.entityName()} o) {
                MapSqlParameterSource m = new MapSqlParameterSource();
                $attributes
                return m;
            }
    """.trimMargin()
}

