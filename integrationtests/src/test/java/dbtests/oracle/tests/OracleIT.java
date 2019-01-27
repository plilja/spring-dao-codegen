package dbtests.oracle.tests;

import dbtests.BaseIntegrationTest;
import dbtests.oracle.model.BazOracleEntity;
import dbtests.oracle.model.BazOracleRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {OracleITConfig.class})
@Import(BazOracleRepository.class)
@RunWith(SpringRunner.class)
public class OracleIT extends BaseIntegrationTest<BazOracleEntity, BazOracleRepository> {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    BazOracleRepository repo;

    @Override
    protected BazOracleRepository getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM DOCKER.BAZ_ORACLE", new MapSqlParameterSource());
    }

    @Override
    protected BazOracleEntity newEntity(String name) {
        return new BazOracleEntity(null, name);
    }

    @Override
    protected String getName(BazOracleEntity entity) {
        return entity.getName();
    }

}
