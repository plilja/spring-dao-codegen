package se.plilja.springdaogen.dbtests.oracle

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
@Import(BazOracleRepository::class)
@RunWith(SpringRunner::class)
class OracleIT : BaseIntegrationTest<BazOracleEntity, BazOracleRepository>() {

    @Autowired
    override lateinit var repo: BazOracleRepository

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    override fun clearTable() {
        jdbcTemplate.update("DELETE FROM DOCKER.BAZ_ORACLE", MapSqlParameterSource())
    }

    override fun newEntity(name: String): BazOracleEntity {
        return BazOracleEntity(null, name)
    }

    override fun getName(entity: BazOracleEntity): String {
        return entity.name
    }

}

@Configuration
class Config {

    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .url("jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS =(PROTOCOL=TCP)(HOST=localhost)(PORT=4006)))(CONNECT_DATA=(SID=xe)(GLOBAL_NAME=xe.WORLD)(SERVER=DEDICATED)))")
            .driverClassName("oracle.jdbc.OracleDriver")
            .username("docker")
            .password("password")
            .build()
    }

    @Bean
    fun jdbcTemplate(dataSource: DataSource): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }

}
