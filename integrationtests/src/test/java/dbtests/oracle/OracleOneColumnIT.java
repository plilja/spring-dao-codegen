package dbtests.oracle;

import dbtests.OneColumnBaseTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {OracleITConfig.class})
@Import(OneColumnOracleRepository.class)
@RunWith(SpringRunner.class)
public class OracleOneColumnIT extends OneColumnBaseTest<OneColumnOracleEntity, OneColumnOracleRepository> {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    OneColumnOracleRepository repo;

    @Override
    protected OneColumnOracleRepository getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM DOCKER.ONE_COLUMN_ORACLE", new MapSqlParameterSource());

    }

    @Override
    protected OneColumnOracleEntity newEntity() {
        return new OneColumnOracleEntity();
    }
}
