package dbtests.oracle.tests;

import dbtests.FakeSpringSecurity;
import dbtests.TransactionUtil;
import dbtests.oracle.model.BazOracleRepository;
import dbtests.oracle.model.BazViewOracleQueryable;
import dbtests.oracle.model.DataTypesOracleRepository;
import dbtests.oracle.model.OneColumnGeneratedIdOracleRepository;
import dbtests.oracle.model.OneColumnNaturalIdOracleRepository;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Import({BazOracleRepository.class,
        OneColumnNaturalIdOracleRepository.class,
        OneColumnGeneratedIdOracleRepository.class,
        DataTypesOracleRepository.class,
        BazViewOracleQueryable.class,
        TransactionUtil.class})
@EnableTransactionManagement
@Configuration
public class OracleITConfig {
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

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public FakeSpringSecurity currentUserProvider() {
        return new FakeSpringSecurity();
    }

}
