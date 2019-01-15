package se.plilja.springdaogen.dbtests.mssql

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
@Import(BazMsSqlRepository::class)
@RunWith(SpringRunner::class)
class MsSqlIT : BaseIntegrationTest<BazMsSqlEntity, BazMsSqlRepository>() {

    @Autowired
    override lateinit var repo: BazMsSqlRepository

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    override fun clearTable() {
        jdbcTemplate.update("DELETE FROM baz_ms_sql", MapSqlParameterSource())
    }

    override fun newEntity(name: String): BazMsSqlEntity {
        return BazMsSqlEntity(null, name)
    }

    override fun getName(entity: BazMsSqlEntity): String {
        return entity.name
    }
}

@Configuration
class Config {

    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .url("jdbc:sqlserver://localhost:4005;databaseName=docker;")
            .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            .username("docker")
            .password("superSecure123")
            .build()
    }

    @Bean
    fun jdbcTemplate(dataSource: DataSource): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }

}
