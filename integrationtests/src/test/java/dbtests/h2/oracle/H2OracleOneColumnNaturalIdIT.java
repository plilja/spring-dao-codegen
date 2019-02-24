package dbtests.h2.oracle;

import dbtests.OneColumnNaturalIdBaseTest;
import dbtests.oracle.model.OneColumnNaturalIdOracle;
import dbtests.oracle.model.OneColumnNaturalIdOracleRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {H2OracleITConfig.class})
@ExtendWith(SpringExtension.class)
public class H2OracleOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdOracle, OneColumnNaturalIdOracleRepository> {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private OneColumnNaturalIdOracleRepository repo;

    @Override
    protected OneColumnNaturalIdOracleRepository getRepo() {
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
