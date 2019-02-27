package dbtests.postgres.tests;

import dbtests.FakeSpringSecurity;
import dbtests.TransactionUtil;
import dbtests.postgres.model.BazPostgresDao;
import dbtests.postgres.model.BazViewPostgresDao;
import dbtests.postgres.model.DataTypesPostgresDao;
import dbtests.postgres.model.OneColumnGeneratedIdPostgresDao;
import dbtests.postgres.model.OneColumnNaturalIdPostgresDao;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Import({BazPostgresDao.class,
        OneColumnNaturalIdPostgresDao.class,
        OneColumnGeneratedIdPostgresDao.class,
        DataTypesPostgresDao.class,
        BazViewPostgresDao.class,
        TransactionUtil.class})
@EnableTransactionManagement
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

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public FakeSpringSecurity currentUserProvider() {
        return new FakeSpringSecurity();
    }
}
