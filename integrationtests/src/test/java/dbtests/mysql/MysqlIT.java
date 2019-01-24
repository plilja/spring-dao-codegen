package dbtests.mysql;


import dbtests.BaseIntegrationTest;
import dbtests.mysql.BazMysqlEntity;
import dbtests.mysql.BazMysqlRepository;
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

@ContextConfiguration(classes = {MysqlIT.Config.class})
@Import(BazMysqlRepository.class)
@RunWith(SpringRunner.class)
public class MysqlIT extends BaseIntegrationTest<BazMysqlEntity, BazMysqlRepository> {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    BazMysqlRepository repo;

    @Override
    protected BazMysqlRepository getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM BazMysql", new MapSqlParameterSource());
    }

    @Override
    protected BazMysqlEntity newEntity(String name) {
        return new BazMysqlEntity(null, name);
    }

    @Override
    protected String getName(BazMysqlEntity entity) {
        return entity.getName();
    }

    @Configuration
    static class Config {

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:4004/docker")
                    .driverClassName("com.mysql.jdbc.Driver")
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

