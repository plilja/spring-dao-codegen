package se.plilja.springdaogen.bootstrap

import org.springframework.jdbc.core.JdbcTemplate
import se.plilja.springdaogen.config.Config
import se.plilja.springdaogen.engine.model.Table
import se.plilja.springdaogen.engine.model.TableContents
import se.plilja.springdaogen.engine.sql.formatTable
import javax.sql.DataSource


class TableContentsFromDb(val dataSource: DataSource) : TableContents {

    override fun getContents(config: Config, table: Table): List<HashMap<String, Any>> {
        val jdbcTemplate = JdbcTemplate(dataSource)
        return jdbcTemplate.query("select * from ${formatTable(table, config.databaseDialect)}") { rs, _ ->
            val map = HashMap<String, Any>()
            for (column in table.columns) {
                map[column.name] = rs.getObject(column.name)
            }
            map
        }
    }
}