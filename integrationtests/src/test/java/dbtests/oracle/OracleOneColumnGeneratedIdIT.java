package dbtests.oracle;

import dbtests.OneColumnGeneratedIdBaseTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {OracleITConfig.class})
@Import(OneColumnGeneratedIdOracleRepository.class)
@RunWith(SpringRunner.class)
public class OracleOneColumnGeneratedIdIT extends OneColumnGeneratedIdBaseTest<OneColumnGeneratedIdOracleEntity, OneColumnGeneratedIdOracleRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnGeneratedIdOracleRepository repo;

    @Override
    protected OneColumnGeneratedIdOracleRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM DOCKER.ONE_COLUMN_GENERATED_ID_ORACLE", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnGeneratedIdOracleEntity newEntity() {
        return new OneColumnGeneratedIdOracleEntity();
    }
}
