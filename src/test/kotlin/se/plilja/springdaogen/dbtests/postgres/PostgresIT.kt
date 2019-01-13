package se.plilja.springdaogen.dbtests.postgres

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
@Import(BazPostgresRepository::class)
@RunWith(SpringRunner::class)
class PostgresIT : BaseIntegrationTest<BazPostgresEntity, BazPostgresRepository>() {

    @Autowired
    override lateinit var repo: BazPostgresRepository

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    override fun clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.baz_postgres", MapSqlParameterSource())
    }

    override fun newEntity(name: String): BazPostgresEntity {
        return BazPostgresEntity(null, name)
    }

    override fun getName(entity: BazPostgresEntity): String {
        return entity.bazName
    }

}

@Configuration
class Config {

    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .url("jdbc:postgresql://localhost:4003/docker")
            .driverClassName("org.postgresql.Driver")
            .username("docker")
            .password("docker")
            .build()
    }

    @Bean
    fun jdbcTemplate(dataSource: DataSource): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }

}
