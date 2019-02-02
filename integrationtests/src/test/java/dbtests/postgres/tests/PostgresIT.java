package dbtests.postgres.tests;

import dbtests.BaseIntegrationTest;
import dbtests.postgres.model.BazPostgresDao;
import dbtests.postgres.model.BazPostgresEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PostgresITConfig.class})
@ExtendWith(SpringExtension.class)
public class PostgresIT extends BaseIntegrationTest<BazPostgresEntity, BazPostgresDao> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazPostgresDao repo;

    @Override
    protected BazPostgresDao getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.baz_postgres", new MapSqlParameterSource());
    }

    @Override
    protected BazPostgresEntity newEntity(String name) {
        return new BazPostgresEntity(null, name);
    }

    @Override
    protected String getName(BazPostgresEntity entity) {
        return entity.getBazName();
    }

}
