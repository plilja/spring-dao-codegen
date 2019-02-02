package dbtests.oracle.tests;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.oracle.model.OneColumnNaturalIdOracle;
import dbtests.oracle.model.OneColumnNaturalIdOracleDao;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OracleITConfig.class})
@ExtendWith(SpringExtension.class)
public class OracleOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdOracle, OneColumnNaturalIdOracleDao> {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private OneColumnNaturalIdOracleDao repo;

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
