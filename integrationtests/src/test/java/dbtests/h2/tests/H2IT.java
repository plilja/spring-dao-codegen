package dbtests.h2.tests;


import dbtests.BaseIntegrationTest;
import dbtests.framework.QueryItem;
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
import java.util.List;

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
    protected LocalDateTime getCreatedAt(BazH2 entity) {
        return entity.getCreatedAt();
    }

    @Override
    protected LocalDateTime getChangedAt(BazH2 entity) {
        return entity.getChangedAt();
    }

    @Override
    protected int getVersion(BazH2 entity) {
        return entity.getVersion();
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


