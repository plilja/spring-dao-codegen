package dbtests.mysql.tests;


import dbtests.BaseIntegrationTest;
import dbtests.mysql.model.ColorEnumMysql;
import dbtests.mysql.model.MBazMysql;
import dbtests.mysql.model.MBazMysqlRepo;
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

@ContextConfiguration(classes = {MysqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class MysqlIT extends BaseIntegrationTest<MBazMysql, MBazMysqlRepo> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private MBazMysqlRepo repo;

    @Override
    protected MBazMysqlRepo getRepo() {
        return repo;
    }

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM BazMysql", new MapSqlParameterSource());
    }

    @Override
    protected MBazMysql newEntity(String name) {
        MBazMysql r = new MBazMysql();
        r.setName(name);
        return r;
    }

    @Override
    protected String getName(MBazMysql entity) {
        return entity.getName();
    }

    @Override
    protected void setName(MBazMysql entity, String name) {
        entity.setName(name);
    }

    @Override
    protected LocalDateTime getCreatedAt(MBazMysql entity) {
        return entity.getCreatedAt();
    }

    @Override
    protected LocalDateTime getChangedAt(MBazMysql entity) {
        return entity.getChangedAt();
    }

    @Override
    protected int getVersion(MBazMysql entity) {
        return entity.getVersion();
    }

    @Test
    void testEnum() {
        MBazMysql entity = newEntity("Foo");
        repo.save(entity);

        MBazMysql retrieved = repo.getOne(entity.getId());
        assertNull(retrieved.getColorEnumMysql());

        retrieved.setColorEnumMysql(ColorEnumMysql.BLUE);
        repo.save(retrieved);

        MBazMysql retrieved2 = repo.getOne(retrieved.getId());
        assertEquals(ColorEnumMysql.BLUE, retrieved2.getColorEnumMysql());
    }

}

