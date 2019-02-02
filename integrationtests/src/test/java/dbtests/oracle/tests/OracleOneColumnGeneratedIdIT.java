package dbtests.oracle.tests;

import dbtests.OneColumnGeneratedIdBaseTest;
import dbtests.oracle.model.OneColumnGeneratedIdOracle;
import dbtests.oracle.model.OneColumnGeneratedIdOracleRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OracleITConfig.class})
@ExtendWith(SpringExtension.class)
public class OracleOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<OneColumnGeneratedIdOracle, OneColumnGeneratedIdOracleRepository> {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private OneColumnGeneratedIdOracleRepository repo;

    @Override
    protected OneColumnGeneratedIdOracleRepository getRepo() {
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
