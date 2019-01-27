package dbtests.oracle;

import dbtests.OneColumnNaturalIdBaseTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {OracleITConfig.class})
@Import(OneColumnNaturalIdOracleRepository.class)
@RunWith(SpringRunner.class)
public class OracleOneColumnNaturalIdIT extends OneColumnNaturalIdBaseTest<OneColumnNaturalIdOracleEntity, OneColumnNaturalIdOracleRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnNaturalIdOracleRepository repo;

    @Override
    protected OneColumnNaturalIdOracleRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnNaturalIdOracleEntity newEntity() {
        return new OneColumnNaturalIdOracleEntity();
    }
}
