package se.plilja.springdaogen.daogeneration

import org.springframework.jdbc.core.JdbcTemplate
import se.plilja.springdaogen.codegeneration.AbstractClassGenerator
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Schema
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.sqlgeneration.formatTable
import javax.sql.DataSource

fun generateCode(config: Config, schema: Schema, dataSource: DataSource): List<AbstractClassGenerator> {
    return schema.tables.flatMap {
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
}

// TODO extract to appropriate util
fun selectRows(
    dataSource: DataSource,
    it: Table,
    config: Config
): List<HashMap<String, Any>> {
    val jdbcTemplate = JdbcTemplate(dataSource)
    val rows = jdbcTemplate.query("select * from ${formatTable(it, config.databaseDialect)}") { rs, _ ->
        val map = HashMap<String, Any>()
        for (column in it.columns) {
            map[column.name] = rs.getObject(column.name)
        }
        map
    }
    return rows
}
