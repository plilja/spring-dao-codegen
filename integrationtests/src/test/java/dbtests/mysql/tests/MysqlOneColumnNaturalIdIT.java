package dbtests.mysql.tests;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.mysql.model.OneColumnNaturalIdMysqlEntity;
import dbtests.mysql.model.OneColumnNaturalIdMysqlRepository;
import dbtests.mysql.tests.MysqlITConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MysqlITConfig.class})
@Import(OneColumnNaturalIdMysqlRepository.class)
@RunWith(SpringRunner.class)
public class MysqlOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdMysqlEntity, OneColumnNaturalIdMysqlRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnNaturalIdMysqlRepository repo;

    @Override
    protected OneColumnNaturalIdMysqlRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM ONE_COLUMN_NATURAL_ID_MYSQL", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnNaturalIdMysqlEntity newEntity() {
        return new OneColumnNaturalIdMysqlEntity();
    }
}
