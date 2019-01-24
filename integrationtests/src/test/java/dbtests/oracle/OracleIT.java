package dbtests.oracle;

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

@ContextConfiguration(classes = {OracleIT.Config.class})
@Import(BazOracleRepository.class)
@RunWith(SpringRunner.class)
public class OracleIT extends BaseIntegrationTest<BazOracleEntity, BazOracleRepository> {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    BazOracleRepository repo;

    @Override
    protected BazOracleRepository getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM DOCKER.BAZ_ORACLE", new MapSqlParameterSource());
    }

    @Override
    protected BazOracleEntity newEntity(String name) {
        return new BazOracleEntity(null, name);
    }

    @Override
    protected String getName(BazOracleEntity entity) {
        return entity.getName();
    }

    @Configuration
    static class Config {

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url("jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS =(PROTOCOL=TCP)(HOST=localhost)(PORT=4006)))(CONNECT_DATA=(SID=xe)(GLOBAL_NAME=xe.WORLD)(SERVER=DEDICATED)))")
                    .driverClassName("oracle.jdbc.OracleDriver")
                    .username("docker")
                    .password("password")
                    .build();
        }

        @Bean
        public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new NamedParameterJdbcTemplate(dataSource);
        }

    }
}
