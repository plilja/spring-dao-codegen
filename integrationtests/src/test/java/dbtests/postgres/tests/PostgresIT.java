package dbtests.postgres.tests;

import dbtests.BaseIntegrationTest;
import dbtests.postgres.model.BazPostgresEntity;
import dbtests.postgres.model.BazPostgresRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {PostgresITConfig.class})
@Import(BazPostgresRepository.class)
@RunWith(SpringRunner.class)
public class PostgresIT extends BaseIntegrationTest<BazPostgresEntity, BazPostgresRepository> {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    BazPostgresRepository repo;

    @Override
    protected BazPostgresRepository getRepo() {
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
