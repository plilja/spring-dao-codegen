package dbtests.postgres.tests;

import dbtests.BaseIntegrationTest;
import dbtests.postgres.model.BazPostgresDao;
import dbtests.postgres.model.BazPostgresEntity;
import dbtests.postgres.model.ColorEnumPostgres;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    protected void setVersion(BazPostgresEntity entity, Integer version) {
        entity.setVersion(version);
    }

    @Override
    protected void insertObjectWithoutVersionColumn(String name) {
        var params = new MapSqlParameterSource()
                .addValue("name", "Glenn")
                .addValue("color", ColorEnumPostgres.GREEN.getId())
                .addValue("created_by", "foo")
                .addValue("changed_by", "foo")
                .addValue("created_at", LocalDateTime.now())
                .addValue("changed_at", LocalDateTime.now());
        jdbcTemplate.update("INSERT INTO test_schema.baz_postgres (baz_name, color, created_at, changed_at, created_by, changed_by) values (:name, :color, :created_at, :changed_at, :created_by, :changed_by)", params);
    }

    @Test
    void testEnum() {
        BazPostgresEntity entity = newEntity("Foo");
        repo.save(entity);

        BazPostgresEntity retrieved = repo.getOne(entity.getId());
        assertNull(retrieved.getColor());

        retrieved.setColor(ColorEnumPostgres.BLUE);
        repo.save(retrieved);

        BazPostgresEntity retrieved2 = repo.getOne(retrieved.getId());
        assertEquals(ColorEnumPostgres.BLUE, retrieved2.getColor());
    }

    @Test
    void testEnumGeneration() {
        // Doesn't test the runtime interaction with the database,
        // only that the generated enum getters have been correctly generated
        assertEquals("#FF0000", ColorEnumPostgres.RED.getHex());
        assertEquals("#00FF00", ColorEnumPostgres.GREEN.getHex());
        assertEquals("#0000FF", ColorEnumPostgres.BLUE.getHex());
    }
}
