package se.plilja.springdaogen.daogeneration


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository
import se.plilja.springdaogen.codegeneration.ClassGenerator
import se.plilja.springdaogen.generatedframework.baseRepository
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.sqlgeneration.*
import java.util.*


fun generateRepository(config: Config, table: Table): ClassGenerator {
    val g = ClassGenerator(table.repositoryName(), config.repositoryOutputPackage, config.repositoryOutputFolder)
    if (!config.repositoriesAreAbstract) {
        g.addClassAnnotation("@Repository")
        g.addImport(Repository::class.java)
    }
    g.extends = "BaseRepository<${table.entityName()}, ${table.primaryKey.javaType.simpleName}>"
    g.addPrivateConstant(
        "ROW_MAPPER", "RowMapper<${table.entityName()}>",
        rowMapper(table)
    )
    g.addCustomMethod(rowUnmapper(table))
    g.addImport(RowMapper::class.java)
    g.addImport(MapSqlParameterSource::class.java)
    g.addImport(SqlParameterSource::class.java)
    if (config.repositoriesAreAbstract) {
        g.isAbstract = true
        g.addCustomConstructor(
            """
            public ${g.name}(NamedParameterJdbcTemplate jdbcTemplate) {
                super(${table.primaryKey.javaType.simpleName}.class, ${table.primaryKey.generated}, jdbcTemplate);
            }
        """
        )
    } else {
        g.addImport(Autowired::class.java)
        g.addCustomConstructor(
            """
            @Autowired
            public ${g.name}(NamedParameterJdbcTemplate jdbcTemplate) {
                super(${table.primaryKey.javaType.simpleName}.class, ${table.primaryKey.generated}, jdbcTemplate);
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

    for (column in table.columns) {
        if (column.javaType == UUID::class.java) {
            g.addImport(column.javaType)
        }
    }
    if (config.entityOutputPackage != config.repositoryOutputPackage) {
        g.addImport("${config.entityOutputPackage}.${table.entityName()}")
    }
    if (config.frameworkOutputPackage != config.repositoryOutputPackage) {
        g.addImport("${config.frameworkOutputPackage}.${baseRepository(config.frameworkOutputPackage).first}")
    }
    return g
}

private fun rowMapper(table: Table): String {
    val setters = table.columns.map { "r.${it.setter()}(${it.recordSetMethod("rs")});" }.joinToString("\n")
    return """(rs, i) -> {
                ${table.entityName()} r = new ${table.entityName()}();
                $setters
                return r;
              }"""
}

private fun rowUnmapper(table: Table): String {
    val attributes = table.columns.map {
        if (table.primaryKey == it) {
            "m.addValue(\"${it.name}\", o.getId());"
        } else {
            "m.addValue(\"${it.name}\", o.${it.getter()}());"
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

