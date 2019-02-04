package dbtests.mssql.tests;

import dbtests.BaseIntegrationTest;
import dbtests.mssql.model.BazMsSqlDao;
import dbtests.mssql.model.BazMsSqlEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ContextConfiguration(classes = {MsSqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class MsSqlIT extends BaseIntegrationTest<BazMsSqlEntity, BazMsSqlDao> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazMsSqlDao repo;

    @Override
    protected BazMsSqlDao getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM baz_ms_sql", new MapSqlParameterSource());
    }

    @Override
    protected BazMsSqlEntity newEntity(String name) {
        BazMsSqlEntity r = new BazMsSqlEntity();
        r.setName(name);
        return r;
    }

    @Override
    protected String getName(BazMsSqlEntity entity) {
        return entity.getName();
    }

    @Override
    protected void setName(BazMsSqlEntity entity, String name) {
        entity.setName(name);
    }

    @Override
    protected LocalDateTime getCreatedAt(BazMsSqlEntity entity) {
        return entity.getInsertedAt();
    }

    @Override
    protected LocalDateTime getChangedAt(BazMsSqlEntity entity) {
        return entity.getModifiedAt();
    }

}
