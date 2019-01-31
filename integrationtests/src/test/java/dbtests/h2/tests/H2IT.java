package dbtests.h2.tests;


import dbtests.BaseIntegrationTest;
import dbtests.h2.model.BazH2;
import dbtests.h2.model.BazH2Repo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {H2Config.class})
@RunWith(SpringRunner.class)
public class H2IT extends BaseIntegrationTest<BazH2, BazH2Repo> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazH2Repo repo;

    @Override
    protected BazH2Repo getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.baz_h2", new MapSqlParameterSource());
    }

    @Override
    protected BazH2 newEntity(String name) {
        return new BazH2(null, name);
    }

    @Override
    protected String getName(BazH2 entity) {
        return entity.getBazName();
    }

}

