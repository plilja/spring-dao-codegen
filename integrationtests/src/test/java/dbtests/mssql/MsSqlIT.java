package dbtests.mssql;

import dbtests.BaseIntegrationTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {MsSqlITConfig.class})
@Import(BazMsSqlRepository.class)
@RunWith(SpringRunner.class)
public class MsSqlIT extends BaseIntegrationTest<BazMsSqlEntity, BazMsSqlRepository> {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    BazMsSqlRepository repo;

    @Override
    protected BazMsSqlRepository getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM baz_ms_sql", new MapSqlParameterSource());
    }

    @Override
    protected BazMsSqlEntity newEntity(String name) {
        return new BazMsSqlEntity(null, name);
    }

    @Override
    protected String getName(BazMsSqlEntity entity) {
        return entity.getName();
    }

}
