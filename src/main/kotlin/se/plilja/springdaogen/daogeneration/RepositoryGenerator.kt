package se.plilja.springdaogen.daogeneration


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository
import se.plilja.springdaogen.codegeneration.ClassGenerator
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.sqlgeneration.insert
import se.plilja.springdaogen.sqlgeneration.selectMany
import se.plilja.springdaogen.sqlgeneration.selectOne
import se.plilja.springdaogen.sqlgeneration.update


fun generateRepository(config: Config, table: Table): ClassGenerator {
    val g = ClassGenerator(table.repositoryName(), config.outputPackage)
    g.addClassAnnotation("@Repository")
    g.addImport(Repository::class.java)
    g.extends = "BaseRepository<${table.entityName()}, ${table.primaryKey.javaType.simpleName}>"
    g.addPrivateConstant(
        "ROW_MAPPER", "RowMapper<${table.entityName()}>",
        rowMapper(table)
    )
    g.addCustomMethod(rowUnmapper(table))
    g.addImport(RowMapper::class.java)
    g.addImport(Autowired::class.java)
    g.addImport(MapSqlParameterSource::class.java)
    g.addImport(SqlParameterSource::class.java)
    g.addCustomConstructor(
        """
            @Autowired
            public ${g.name}(NamedParameterJdbcTemplate jdbcTemplate) {
                super(${table.primaryKey.javaType.simpleName}.class, jdbcTemplate, ROW_MAPPER);
            }
        """
    )
    g.addImport(NamedParameterJdbcTemplate::class.java)
    g.addCustomMethod(
        """
            @Override
            protected String getSelectOneSql() {
                return ${selectOne(table)};
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getSelectManySql(int maxSelectCount) {
                return String.format(${selectMany(table, config)}, maxSelectCount);
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getInsertSql() {
                return ${insert(table)};
            }
        """
    )
    g.addCustomMethod(
        """
            @Override
            protected String getUpdateSql() {
                return ${update(table)};
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
    return g
}

private fun rowMapper(table: Table): String {
    val setters = table.columns.map {
        "                ${setterForColumn(table, it)}"
    }.joinToString("\n")
    return """(rs, i) -> {
                return new ${table.entityName()}()
                $setters;
              }"""
}

private fun setterForColumn(table: Table, column: Column): String {
    return ".${column.setter()}((${column.javaType.simpleName}) rs.getObject(\"${column.name}\"))"
}

private fun rowUnmapper(table: Table): String {
    val attributes = table.columns.map { "m.addValue(\"${it.name}\", o.${it.getter()}());" }.joinToString("\n")
    return """
            @Override
            public SqlParameterSource getParams(${table.entityName()} o) {
                MapSqlParameterSource m = new MapSqlParameterSource();
                $attributes
                return m;
            }
    """.trimMargin()
}
