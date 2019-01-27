package dbtests.oracle.tests;

import dbtests.OneColumnGeneratedIdBaseTest;
import dbtests.oracle.model.OneColumnGeneratedIdOracle;
import dbtests.oracle.model.OneColumnGeneratedIdOracleDao;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {OracleITConfig.class})
@Import(OneColumnGeneratedIdOracleDao.class)
@RunWith(SpringRunner.class)
public class OracleOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<OneColumnGeneratedIdOracle, OneColumnGeneratedIdOracleDao> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnGeneratedIdOracleDao repo;

    @Override
    protected OneColumnGeneratedIdOracleDao getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM DOCKER.ONE_COLUMN_GENERATED_ID_ORACLE", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnGeneratedIdOracle newEntity() {
        return new OneColumnGeneratedIdOracle();
    }
}
