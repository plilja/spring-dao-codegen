package dbtests.postgres.tests;

import dbtests.postgres.model.BazPostgresRepository;
import dbtests.postgres.model.OneColumnGeneratedIdPostgresRepository;
import dbtests.postgres.model.OneColumnNaturalIdPostgresRepository;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Import({BazPostgresRepository.class, OneColumnNaturalIdPostgresRepository.class, OneColumnGeneratedIdPostgresRepository.class})
@Configuration
public class PostgresITConfig {
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
