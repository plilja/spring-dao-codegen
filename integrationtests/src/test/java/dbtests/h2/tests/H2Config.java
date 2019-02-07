package dbtests.h2.tests;

import dbtests.TransactionUtil;
import dbtests.h2.model.BazH2Repo;
import dbtests.h2.model.OneColumnGeneratedIdH2Repo;
import dbtests.h2.model.OneColumnNaturalIdH2Repo;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Import({BazH2Repo.class, OneColumnNaturalIdH2Repo.class, OneColumnGeneratedIdH2Repo.class, TransactionUtil.class})
@EnableTransactionManagement
@Configuration
public class H2Config {

    @Bean
    public DataSource dataSource() {
        String file = H2Config.class.getClassLoader().getResource("init.sql").getFile();
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
}
