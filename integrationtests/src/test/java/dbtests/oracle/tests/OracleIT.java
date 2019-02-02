package dbtests.oracle.tests;

import dbtests.BaseIntegrationTest;
import dbtests.oracle.model.BazOracle;
import dbtests.oracle.model.BazOracleRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OracleITConfig.class})
@ExtendWith(SpringExtension.class)
public class OracleIT extends BaseIntegrationTest<BazOracle, BazOracleRepository> {

    @Autowired
    private
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private
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
    protected BazOracle newEntity(String name) {
        return new BazOracle(null, name);
    }

    @Override
    protected String getName(BazOracle entity) {
        return entity.getName();
    }

}
