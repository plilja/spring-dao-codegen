package dbtests.h2.oracle;

import dbtests.FakeSpringSecurity;
import dbtests.TransactionUtil;
import dbtests.oracle.model.BazOracleRepository;
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

@Import({BazOracleRepository.class, OneColumnNaturalIdOracleRepository.class, OneColumnGeneratedIdOracleRepository.class, DataTypesOracleRepository.class, TransactionUtil.class})
@EnableTransactionManagement
@Configuration
public class H2OracleITConfig {
    @Bean
    public DataSource dataSource() {
        String file = H2OracleITConfig.class.getClassLoader().getResource("init-h2-oracle.sql").getFile();
        return DataSourceBuilder.create()
                .url(String.format("jdbc:h2:mem:db;INIT=RUNSCRIPT FROM '%s'", file))
                .driverClassName("org.h2.Driver")
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
