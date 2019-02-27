package dbtests.mysql.tests;

import dbtests.FakeSpringSecurity;
import dbtests.TransactionUtil;
import dbtests.mysql.model.MBazMysqlRepo;
import dbtests.mysql.model.MBazViewMysqlRepo;
import dbtests.mysql.model.MDataTypesMysqlRepo;
import dbtests.mysql.model.MOneColumnGeneratedIdMysqlRepo;
import dbtests.mysql.model.MOneColumnNaturalIdMysqlRepo;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Import({
        MBazMysqlRepo.class,
        MOneColumnNaturalIdMysqlRepo.class,
        MOneColumnGeneratedIdMysqlRepo.class,
        MDataTypesMysqlRepo.class,
        MBazViewMysqlRepo.class,
        TransactionUtil.class
})
@EnableTransactionManagement
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

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public FakeSpringSecurity currentUserProvider() {
        return new FakeSpringSecurity();
    }
}
