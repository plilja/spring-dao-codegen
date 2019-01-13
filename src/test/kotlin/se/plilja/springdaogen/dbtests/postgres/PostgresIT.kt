package se.plilja.springdaogen.dbtests.postgres

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import se.plilja.springdaogen.dbtests.framework.MaxAllowedCountExceededException
import javax.sql.DataSource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@ContextConfiguration(classes = arrayOf(Config::class))
@Import(BazRepository::class)
@RunWith(SpringRunner::class)
class PostgresIT {

    @Autowired
    lateinit var bazRepository: BazRepository
    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Before
    fun before() {
        jdbcTemplate.update("DELETE from test_schema.baz", MapSqlParameterSource())
    }

    @Test
    fun findAll() {
        assertEquals(emptyList(), bazRepository.findAll())

        for (i in 1..bazRepository.selectAllDefaultMaxCount) {
            val bazEntity = BazEntity(null, "Bar $i")
            bazRepository.create(bazEntity)
        }

        assertEquals(10, bazRepository.findAll().size)
    }

    @Test(expected = MaxAllowedCountExceededException::class)
    fun findAllExceedsMaxLimit() {
        for (i in 1..bazRepository.selectAllDefaultMaxCount + 1) {
            val bazEntity = BazEntity(null, "Bar $i")
            bazRepository.create(bazEntity)
        }
        bazRepository.findAll()
    }

    @Test
    fun create() {
        val bazEntity = BazEntity(null, "Bar")
        bazRepository.create(bazEntity)
        assertNotNull(bazEntity.bazId)

        val retrievedEntity = bazRepository.getOne(bazEntity.bazId)
        assertNotNull(retrievedEntity)
        assertEquals("Bar", retrievedEntity.bazName)
    }

    @Test
    fun update() {
        val bazEntity = BazEntity(null, "Bar")
        bazRepository.create(bazEntity)

        val bazEntity2 = BazEntity(bazEntity.bazId, "Bar updated")
        bazRepository.update(bazEntity2)

        val retrievedEntity = bazRepository.getOne(bazEntity.bazId)
        assertEquals("Bar updated", retrievedEntity.bazName)
    }

    @Test(expected = EmptyResultDataAccessException::class)
    fun findOneNonExistingReturnsNull() {
        val baz = bazRepository.getOne(4711)
        assertEquals(null, baz)
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
