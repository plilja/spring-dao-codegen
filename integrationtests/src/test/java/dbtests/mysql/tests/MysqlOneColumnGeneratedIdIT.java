package dbtests.mysql.tests;

import dbtests.OneColumnGeneratedIdBaseTest;
import dbtests.mysql.model.MOneColumnGeneratedIdMysql;
import dbtests.mysql.model.MOneColumnGeneratedIdMysqlRepo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MysqlITConfig.class})
@ExtendWith(SpringExtension.class)
class MysqlOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<MOneColumnGeneratedIdMysql, MOneColumnGeneratedIdMysqlRepo> {

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
