package dbtests.oracle.tests;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.oracle.model.OneColumnNaturalIdOracle;
import dbtests.oracle.model.OneColumnNaturalIdOracleDao;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {OracleITConfig.class})
@Import(OneColumnNaturalIdOracleDao.class)
@RunWith(SpringRunner.class)
public class OracleOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdOracle, OneColumnNaturalIdOracleDao> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnNaturalIdOracleDao repo;

    @Override
    protected OneColumnNaturalIdOracleDao getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnNaturalIdOracle newEntity() {
        return new OneColumnNaturalIdOracle();
    }
}
