package dbtests.oracle.tests;

import dbtests.BaseIntegrationTest;
import dbtests.oracle.model.BazOracle;
import dbtests.oracle.model.BazOracleDao;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {OracleITConfig.class})
@Import(BazOracleDao.class)
@RunWith(SpringRunner.class)
public class OracleIT extends BaseIntegrationTest<BazOracle, BazOracleDao> {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    BazOracleDao repo;

    @Override
    protected BazOracleDao getRepo() {
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
