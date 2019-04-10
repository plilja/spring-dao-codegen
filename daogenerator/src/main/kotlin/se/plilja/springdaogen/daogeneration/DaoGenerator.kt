package se.plilja.springdaogen.daogeneration


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository
import se.plilja.springdaogen.classgenerators.ClassGenerator
import se.plilja.springdaogen.copyable.columnClass
import se.plilja.springdaogen.copyable.currentUserProvider
import se.plilja.springdaogen.copyable.dao
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Left
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.sql.*
import se.plilja.springdaogen.util.snakeCase
import java.math.BigDecimal
import java.sql.Blob
import java.sql.Clob
import java.sql.NClob
import java.time.OffsetDateTime
import java.util.*
import kotlin.collections.ArrayList


fun generateDao(config: Config, table: Table): ClassGenerator {
    println("Generating dao for table '${table.name}', dao will be named '${table.daoName()}'.")
    val g = ClassGenerator(table.daoName(), config.daoOutputPackage, config.daoOutputFolder)
    if (!config.daosAreAbstract) {
        g.addClassAnnotation("@Repository")
        g.addImport(Repository::class.java)
    }
    g.extends = "Dao<${table.entityName()}, ${table.primaryKey.typeName()}>"
    if (config.featureGenerateQueryApi) {
        ensureImported(g, config) { columnClass(config.frameworkOutputPackage) }
        val columnsConstantNames = ArrayList<String>()
        for (column in table.columns) {
            val type = column.type()
            when (type) {
                is Left -> {
                    if (!type.value.packageName.contains("java.lang")) {
                        g.addImport(type.value)
                    }
                }
            }
            val name = "COLUMN_${snakeCase(column.name).toUpperCase()}"
            val (columnTypeDecl, init) = columnConstantInitializer(table, column, config)
            g.addConstant(
                    name,
                    columnTypeDecl,
                    init
            )
            columnsConstantNames.add(name)
        }
        g.addImport(Arrays::class.java)
        g.addImport(java.util.List::class.java)
        g.addConstant(
                "ALL_COLUMNS_LIST",
                "List<Column<${table.entityName()}, ?>>",
                "Arrays.asList(\n${columnsConstantNames.joinToString(",\n")})"
        )
    }
    g.addPrivateConstant("ALL_COLUMNS", "String", columnsList(table, config.databaseDialect))
    g.addPrivateConstant(
            "ROW_MAPPER", "RowMapper<${table.entityName()}>",
            rowMapper(table, g, config)
    )
    g.addCustomMethod(rowUnmapper(table, g))
    g.addImport(RowMapper::class.java)
    g.addImport(MapSqlParameterSource::class.java)
    g.addImport(SqlParameterSource::class.java)
    if (config.daosAreAbstract) {
        g.isAbstract = true
    } else {
        g.addImport(Autowired::class.java)
    }
    if (config.featureGenerateChangeTracking) {
        ensureImported(g, config) { currentUserProvider(config.frameworkOutputPackage) }
        g.addCustomConstructor(
                """
            @Autowired
            public ${g.name}(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
                super(${table.entityName()}.class, ${table.primaryKey.typeName()}.class, ${table.primaryKey.generated}, jdbcTemplate, currentUserProvider);
            }
        """
        )

    } else {
        g.addCustomConstructor(
                """
            @Autowired
            public ${g.name}(NamedParameterJdbcTemplate jdbcTemplate) {
                super(${table.entityName()}.class, ${table.primaryKey.typeName()}.class, ${table.primaryKey.generated}, jdbcTemplate);
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
            protected String getInsertSql() {
                return ${insert(table, config.databaseDialect)};
            }
        """
    )
    val updateString = update(table, config.databaseDialect)
    g.addCustomMethod(
            """
            @Override
            protected String getUpdateSql(${table.entityName()} object) {
                ${if (updateString != null) "$updateString" else "throw new UnsupportedOperationException();"}
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
    if (config.featureGenerateQueryApi) {
        g.addCustomMethod(
                """
            @Override
            protected List<Column<${table.entityName()}, ?>> getColumnsList() {
                return ALL_COLUMNS_LIST;
            }
        """
        )
        g.addCustomMethod(
                """
            @Override
            protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
                return ${selectManyQuery(table, config.databaseDialect)};
            }
        """
        )
        g.addCustomMethod(
                """
            @Override
            protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
                return ${selectPageQuery(table, config.databaseDialect)}
            }
        """
        )
    } else {
        g.addCustomMethod(
                """
            @Override
            protected String getSelectPageSql(long start, int pageSize) {
                return ${selectPage(table, config.databaseDialect)}
            }
        """
        )
    }
    g.addCustomMethod(
            """
            @Override
            protected String getSelectAndLockSql(String databaseProductName) {
                ${lock(table, config)}
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

    val mayNeedImport = listOf(
            UUID::class.java, Clob::class.java,
            NClob::class.java, Blob::class.java,
            BigDecimal::class.java, OffsetDateTime::class.java
    )
    for (column in table.columns) {
        if (column.rawType() in mayNeedImport) {
            g.addImport(column.rawType())
        }
    }
    if (config.entityOutputPackage != config.daoOutputPackage) {
        g.addImport("${config.entityOutputPackage}.${table.entityName()}")
    }
    if (config.frameworkOutputPackage != config.daoOutputPackage) {
        g.addImport("${config.frameworkOutputPackage}.${dao(config.frameworkOutputPackage, config).first}")
    }
    return g
}

