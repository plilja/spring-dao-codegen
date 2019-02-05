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

import java.time.LocalDateTime;

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
        BazPostgresEntity r = new BazPostgresEntity();
        r.setBazName(name);
        return r;
    }

    @Override
    protected String getName(BazPostgresEntity entity) {
        return entity.getBazName();
    }

    @Override
    protected void setName(BazPostgresEntity entity, String name) {
        entity.setBazName(name);
    }

    @Override
    protected LocalDateTime getCreatedAt(BazPostgresEntity entity) {
        return entity.getCreatedAt();
    }

    @Override
    protected LocalDateTime getChangedAt(BazPostgresEntity entity) {
        return entity.getChangedAt();
    }

    @Override
    protected int getVersion(BazPostgresEntity entity) {
        return entity.getVersion().intValue();
    }

}
