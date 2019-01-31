package dbtests.mysql.tests;

import dbtests.OneColumnGeneratedIdBaseTest;
import dbtests.mysql.model.MOneColumnGeneratedIdMysql;
import dbtests.mysql.model.MOneColumnGeneratedIdMysqlRepo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MysqlITConfig.class})
@RunWith(SpringRunner.class)
public class MysqlOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<MOneColumnGeneratedIdMysql, MOneColumnGeneratedIdMysqlRepo> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private MOneColumnGeneratedIdMysqlRepo repo;

    @Override
    protected MOneColumnGeneratedIdMysqlRepo getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM ONE_COLUMN_GENERATED_ID_MYSQL", new MapSqlParameterSource());

    }

    @Override
    protected MOneColumnGeneratedIdMysql newEntity() {
        return new MOneColumnGeneratedIdMysql();
    }
}
