package dbtests.mssql.tests;

import dbtests.BaseIntegrationTest;
import dbtests.mssql.model.BazMsSqlDao;
import dbtests.mssql.model.BazMsSqlEntity;
import dbtests.mssql.model.ColorEnumMsSql;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    protected void setVersion(BazMsSqlEntity entity, Integer version) {
        entity.setVersion(version);
    }

    @Override
    protected void insertObjectWithoutVersionColumn(String name) {
        var params = new MapSqlParameterSource()
                .addValue("name", "Glenn")
                .addValue("color", ColorEnumMsSql.GREEN.getId())
                .addValue("insertedBy", "foo")
                .addValue("modifiedBy", "foo")
                .addValue("insertedAt", LocalDateTime.now())
                .addValue("modifiedAt", LocalDateTime.now());
        jdbcTemplate.update("INSERT INTO dbo.baz_ms_sql (name, color, inserted_at, modified_at, inserted_by, modified_by) values (:name, :color, :insertedAt, :modifiedAt, :insertedBy, :modifiedBy)", params);
    }

    @Test
    void testEnum() {
        BazMsSqlEntity entity = newEntity("Foo");
        repo.save(entity);

        BazMsSqlEntity retrieved = repo.getOne(entity.getId());
        assertNull(retrieved.getColor());

        retrieved.setColor(ColorEnumMsSql.BLUE);
        repo.save(retrieved);

        BazMsSqlEntity retrieved2 = repo.getOne(retrieved.getId());
        assertEquals(ColorEnumMsSql.BLUE, retrieved2.getColor());
    }
}
