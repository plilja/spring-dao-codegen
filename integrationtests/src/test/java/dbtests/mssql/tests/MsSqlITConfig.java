package dbtests.mssql.tests;

import dbtests.TransactionUtil;
import dbtests.mssql.model.BazMsSqlDao;
import dbtests.mssql.model.DataTypesMsSqlDao;
import dbtests.mssql.model.OneColumnGeneratedIdMsSqlDao;
import dbtests.mssql.model.OneColumnNaturalIdMsSqlDao;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Import({BazMsSqlDao.class, OneColumnNaturalIdMsSqlDao.class, OneColumnGeneratedIdMsSqlDao.class, DataTypesMsSqlDao.class, TransactionUtil.class})
@EnableTransactionManagement
@Configuration
public class MsSqlITConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:sqlserver://localhost:4005;databaseName=docker;")
                .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .username("docker")
                .password("superSecure123")
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
}
