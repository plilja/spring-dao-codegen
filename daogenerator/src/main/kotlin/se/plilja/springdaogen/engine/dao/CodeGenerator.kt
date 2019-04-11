package se.plilja.springdaogen.engine.dao

import org.springframework.jdbc.core.JdbcTemplate
import se.plilja.springdaogen.config.Config
import se.plilja.springdaogen.engine.model.Schema
import se.plilja.springdaogen.engine.model.Table
import se.plilja.springdaogen.engine.sql.formatTable
import se.plilja.springdaogen.syntaxgenerator.AbstractClassGenerator
import javax.sql.DataSource

fun generateCode(config: Config, schemas: List<Schema>, dataSource: DataSource): List<AbstractClassGenerator> {
    val tableClasses = schemas.flatMap { schema ->
        schema.tables.flatMap { table ->
        val list = ArrayList<AbstractClassGenerator>()
        if (table.isEnum()) {
            val rows = selectRows(dataSource, table, config)
            list.add(generateEnums(config, table, rows))
        } else {
            list.add(generateDao(config, table))
            list.add(generateEntity(config, table))
        }
        list
        }
    }
    val viewClasses = schemas.flatMap { schema ->
        schema.views.flatMap { table ->
        val list = ArrayList<AbstractClassGenerator>()
        list.add(generateViewQueryable(config, table))
        list.add(generateViewEntity(config, table))
        list
        }
    }
    return tableClasses + viewClasses
}

// TODO extract to appropriate util
fun selectRows(
    dataSource: DataSource,
    it: Table,
    config: Config
): List<HashMap<String, Any>> {
    val jdbcTemplate = JdbcTemplate(dataSource)
    return jdbcTemplate.query("select * from ${formatTable(it, config.databaseDialect)}") { rs, _ ->
        val map = HashMap<String, Any>()
        for (column in it.columns) {
            map[column.name] = rs.getObject(column.name)
        }
        map
    }
}
