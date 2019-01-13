package se.plilja.springdaogen.dbtests.mysql

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import se.plilja.springdaogen.dbtests.BaseIntegrationTest
import javax.sql.DataSource


@ContextConfiguration(classes = arrayOf(Config::class))
@Import(BazMysqlRepository::class)
@RunWith(SpringRunner::class)
class MysqlIT : BaseIntegrationTest<BazMysqlEntity, BazMysqlRepository>() {

    @Autowired
    override lateinit var repo: BazMysqlRepository

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    override fun clearTable() {
        jdbcTemplate.update("DELETE FROM BazMysql", MapSqlParameterSource())
    }

    override fun newEntity(name: String): BazMysqlEntity {
        return BazMysqlEntity(null, name)
    }

    override fun getName(entity: BazMysqlEntity): String {
        return entity.name
    }

}

@Configuration
class Config {

    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .url("jdbc:mysql://localhost:4004/docker")
            .driverClassName("com.mysql.jdbc.Driver")
            .username("docker")
            .password("docker")
            .build()
    }

    @Bean
    fun jdbcTemplate(dataSource: DataSource): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }

}
