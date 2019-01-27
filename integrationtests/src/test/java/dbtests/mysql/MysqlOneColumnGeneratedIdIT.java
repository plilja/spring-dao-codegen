package dbtests.mysql;

import dbtests.OneColumnGeneratedIdBaseTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MysqlITConfig.class})
@Import(OneColumnGeneratedIdMysqlRepository.class)
@RunWith(SpringRunner.class)
public class MysqlOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<OneColumnGeneratedIdMysqlEntity, OneColumnGeneratedIdMysqlRepository> {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnGeneratedIdMysqlRepository repo;

    @Override
    protected OneColumnGeneratedIdMysqlRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM ONE_COLUMN_GENERATED_ID_MYSQL", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnGeneratedIdMysqlEntity newEntity() {
        return new OneColumnGeneratedIdMysqlEntity();
    }
}
