package se.plilja.springdaogen.daogeneration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import se.plilja.springdaogen.codegeneration.ClassGenerator
import se.plilja.springdaogen.generatedframework.columnClass
import se.plilja.springdaogen.generatedframework.queryable
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Left
import se.plilja.springdaogen.model.View
import se.plilja.springdaogen.sqlgeneration.*
import se.plilja.springdaogen.util.snakeCase
import java.math.BigDecimal
import java.sql.Blob
import java.sql.Clob
import java.sql.NClob
import java.time.OffsetDateTime
import java.util.*
import kotlin.collections.ArrayList


fun generateViewQueryable(config: Config, view: View): ClassGenerator {
    println("Generating queryable for view '${view.name}', queryable will be named '${view.queryableName()}'.")
    val g = ClassGenerator(view.queryableName(), config.daoOutputPackage, config.daoOutputFolder)
    if (!config.daosAreAbstract) {
        g.addClassAnnotation("@Repository")
        g.addImport(Repository::class.java)
    }
    g.extends = "Queryable<${view.entityName()}>"
    val columnsConstantNames = ArrayList<String>()
    for (column in view.columns) {
        val type = column.type()
        when (type) {
            is Left -> {
                if (!type.value.packageName.contains("java.lang")) {
                    g.addImport(type.value)
                }
            }
        }
        val name = "COLUMN_${snakeCase(column.name).toUpperCase()}"
        g.addConstant(
                name,
                "Column<${view.entityName()}, ${column.typeName()}>",
                columnConstantInitializer(column, config)
        )
        columnsConstantNames.add(name)
    }
    g.addImport(Arrays::class.java)
    g.addImport(java.util.List::class.java)
    g.addConstant(
            "ALL_COLUMNS_LIST",
            "List<Column<${view.entityName()}, ?>>",
            "Arrays.asList(\n${columnsConstantNames.joinToString(",\n")})"
    )
    g.addPrivateConstant("ALL_COLUMNS", "String", columnsList(view, config.databaseDialect))
    g.addPrivateConstant(
            "ROW_MAPPER", "RowMapper<${view.entityName()}>",
            rowMapper(view, g, config)
    )
    g.addImport(RowMapper::class.java)
    if (config.daosAreAbstract) {
        g.isAbstract = true
    } else {
        g.addImport(Autowired::class.java)
    }
    g.addCustomConstructor(
            """
            @Autowired
            public ${g.name}(NamedParameterJdbcTemplate jdbcTemplate) {
                super(jdbcTemplate);
            }
        """
    )
    g.addImport(NamedParameterJdbcTemplate::class.java)

    g.addCustomMethod(
            """
            @Override
            protected RowMapper<${view.entityName()}> getRowMapper() {
                return ROW_MAPPER;
            }
        """
    )
    g.addCustomMethod(
            """
            @Override
            protected String getSelectManySql(int maxSelectCount) {
                return String.format(${selectMany(view, config.databaseDialect)}, maxSelectCount);
            }
        """
    )
    g.addCustomMethod(
            """
            @Override
            protected String getCountSql() {
                return ${count(view, config.databaseDialect)};
            }
        """
    )
    g.addCustomMethod(
            """
            @Override
            protected List<Column<${view.entityName()}, ?>> getColumnsList() {
                return ALL_COLUMNS_LIST;
            }
        """
    )
    g.addCustomMethod(
            """
            @Override
            protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
                return ${selectManyQuery(view, config.databaseDialect)};
            }
        """
    )
    g.addCustomMethod(
            """
            @Override
            protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
                return ${selectPageQuery(view, config.databaseDialect)}
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

    val mayNeedImport = listOf(
            UUID::class.java, Clob::class.java,
            NClob::class.java, Blob::class.java,
            BigDecimal::class.java, OffsetDateTime::class.java
    )
    for (column in view.columns) {
        if (column.rawType() in mayNeedImport) {
            g.addImport(column.rawType())
        }
    }
    if (config.entityOutputPackage != config.daoOutputPackage) {
        g.addImport("${config.entityOutputPackage}.${view.entityName()}")
    }
    if (config.frameworkOutputPackage != config.daoOutputPackage) {
        ensureImported(g, config) { queryable(config.frameworkOutputPackage, config) }
        ensureImported(g, config) { columnClass(config.frameworkOutputPackage) }
    }
    return g
}
