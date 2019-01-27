package dbtests.mysql.tests;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.mysql.model.MOneColumnNaturalIdMysql;
import dbtests.mysql.model.MOneColumnNaturalIdMysqlRepo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MysqlITConfig.class})
@Import(MOneColumnNaturalIdMysqlRepo.class)
@RunWith(SpringRunner.class)
public class MysqlOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<MOneColumnNaturalIdMysql, MOneColumnNaturalIdMysqlRepo> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    MOneColumnNaturalIdMysqlRepo repo;

    @Override
    protected MOneColumnNaturalIdMysqlRepo getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM ONE_COLUMN_NATURAL_ID_MYSQL", new MapSqlParameterSource());

    }

    @Override
    protected MOneColumnNaturalIdMysql newEntity() {
        return new MOneColumnNaturalIdMysql();
    }
}
