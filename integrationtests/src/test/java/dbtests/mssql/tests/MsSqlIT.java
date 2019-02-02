package dbtests.mssql.tests;

import dbtests.BaseIntegrationTest;
import dbtests.mssql.model.BazMsSqlEntity;
import dbtests.mssql.model.BazMsSqlRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MsSqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class MsSqlIT extends BaseIntegrationTest<BazMsSqlEntity, BazMsSqlRepository> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazMsSqlRepository repo;

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
