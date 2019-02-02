package dbtests.mysql.tests;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.mysql.model.MOneColumnNaturalIdMysql;
import dbtests.mysql.model.MOneColumnNaturalIdMysqlRepo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MysqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class MysqlOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<MOneColumnNaturalIdMysql, MOneColumnNaturalIdMysqlRepo> {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private MOneColumnNaturalIdMysqlRepo repo;

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
