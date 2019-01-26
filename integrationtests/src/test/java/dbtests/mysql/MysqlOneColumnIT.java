package dbtests.mysql;

import dbtests.OneColumnBaseTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MysqlITConfig.class})
@Import(OneColumnMysqlRepository.class)
@RunWith(SpringRunner.class)
public class MysqlOneColumnIT extends OneColumnBaseTest<OneColumnMysqlEntity, OneColumnMysqlRepository> {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnMysqlRepository repo;

    @Override
    protected OneColumnMysqlRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM ONE_COLUMN_MYSQL", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnMysqlEntity newEntity() {
        return new OneColumnMysqlEntity();
    }
}
