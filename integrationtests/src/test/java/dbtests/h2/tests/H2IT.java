package dbtests.h2.tests;


import dbtests.BaseIntegrationTest;
import dbtests.h2.model.BazH2;
import dbtests.h2.model.BazH2Repo;
import dbtests.h2.model.ColorEnumH2;
import org.junit.jupiter.api.Disabled;
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

@ContextConfiguration(classes = {H2Config.class})
@ExtendWith(SpringExtension.class)
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
        BazH2 r = new BazH2();
        r.setBazName(name);
        return r;
    }

    @Override
    protected String getName(BazH2 entity) {
        return entity.getBazName();
    }

    @Override
    protected void setName(BazH2 entity, String name) {
        entity.setBazName(name);
    }

    @Override
    protected void setVersion(BazH2 entity, Integer version) {
        entity.setVersion(version);
    }

    @Override
    protected void insertObjectWithoutVersionColumn(String name) {
        var params = new MapSqlParameterSource()
                .addValue("name", "Glenn")
                .addValue("color", ColorEnumH2.GREEN.getId())
                .addValue("created_by", "foo")
                .addValue("changed_by", "foo")
                .addValue("created_at", LocalDateTime.now())
                .addValue("changed_at", LocalDateTime.now());
        jdbcTemplate.update("INSERT INTO test_schema.baz_h2 (baz_name, color, created_at, changed_at, created_by, changed_by) values (:name, :color, :created_at, :changed_at, :created_by, :changed_by)", params);
    }

    @Disabled("Doesn't seem to work with H2, look into if this is an error in generated code or a limitation in H2")
    @Override
    public void lockWithConcurrentModification() {
    }

    @Test
    void testEnum() {
        BazH2 entity = newEntity("Foo");
        repo.save(entity);

        BazH2 retrieved = repo.getOne(entity.getId());
        assertNull(retrieved.getColor());

        retrieved.setColor(ColorEnumH2.BLUE);
        repo.save(retrieved);

        BazH2 retrieved2 = repo.getOne(retrieved.getId());
        assertEquals(ColorEnumH2.BLUE, retrieved2.getColor());
    }

}


