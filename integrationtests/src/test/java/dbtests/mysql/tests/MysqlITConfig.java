package dbtests.mysql.tests;

import dbtests.mysql.model.MBazMysqlRepo;
import dbtests.mysql.model.MDataTypesMysqlRepo;
import dbtests.mysql.model.MOneColumnGeneratedIdMysqlRepo;
import dbtests.mysql.model.MOneColumnNaturalIdMysqlRepo;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Import({MBazMysqlRepo.class, MOneColumnNaturalIdMysqlRepo.class, MOneColumnGeneratedIdMysqlRepo.class, MDataTypesMysqlRepo.class})
@Configuration
public class MysqlITConfig {
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
