package se.plilja.springdaogen.engine

import org.springframework.jdbc.core.JdbcTemplate
import se.plilja.springdaogen.classgenerators.AbstractClassGenerator
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Schema
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.sql.formatTable
import javax.sql.DataSource

fun generateCode(config: Config, schema: Schema, dataSource: DataSource): List<AbstractClassGenerator> {
    val tableClasses = schema.tables.flatMap {
        val list = ArrayList<AbstractClassGenerator>()
        if (it.isEnum()) {
            val rows = selectRows(dataSource, it, config)
            list.add(generateEnums(config, it, rows))
        } else {
            list.add(generateDao(config, it))
            list.add(generateEntity(config, it))
        }
        list
    }
    val viewClasses = schema.views.flatMap {
        val list = ArrayList<AbstractClassGenerator>()
        list.add(generateViewQueryable(config, it))
        list.add(generateViewEntity(config, it))
        list
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
