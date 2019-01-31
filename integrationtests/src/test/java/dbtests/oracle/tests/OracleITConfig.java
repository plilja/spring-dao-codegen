package dbtests.oracle.tests;

import dbtests.oracle.model.BazOracleDao;
import dbtests.oracle.model.OneColumnGeneratedIdOracleDao;
import dbtests.oracle.model.OneColumnNaturalIdOracleDao;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Import({BazOracleDao.class, OneColumnNaturalIdOracleDao.class, OneColumnGeneratedIdOracleDao.class})
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
}
