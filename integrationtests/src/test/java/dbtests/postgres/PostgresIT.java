package dbtests.postgres;

import dbtests.BaseIntegrationTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@ContextConfiguration(classes = {PostgresIT.Config.class})
@Import(BazPostgresRepository.class)
@RunWith(SpringRunner.class)
public class PostgresIT extends BaseIntegrationTest<BazPostgresEntity, BazPostgresRepository> {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    BazPostgresRepository repo;

    @Override
    protected BazPostgresRepository getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.baz_postgres", new MapSqlParameterSource());
    }

    @Override
    protected BazPostgresEntity newEntity(String name) {
        return new BazPostgresEntity(null, name);
    }

    @Override
    protected String getName(BazPostgresEntity entity) {
        return entity.getBazName();
    }

    @Configuration
    static class Config {

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url("jdbc:postgresql://localhost:4003/docker")
                    .driverClassName("org.postgresql.Driver")
                    .username("docker")
                    .password("docker")
                    .build();
        }

        @Bean
        public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new NamedParameterJdbcTemplate(dataSource);
        }

    }
}
