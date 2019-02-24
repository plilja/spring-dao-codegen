package dbtests.h2.postgres;

import dbtests.QueryApiBaseTest;
import dbtests.framework.Column;
import dbtests.postgres.model.BazPostgresDao;
import dbtests.postgres.model.BazPostgresEntity;
import dbtests.postgres.model.ColorEnumPostgres;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {H2PostgresConfig.class})
@ExtendWith(SpringExtension.class)
class H2PostgresQueryApiIT extends QueryApiBaseTest<BazPostgresEntity, BazPostgresDao> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazPostgresDao repo;

    @Override
    protected BazPostgresDao getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.baz_postgres", new MapSqlParameterSource());
    }

    @Override
    protected BazPostgresEntity newEntity(String name, String color) {
        var r = new BazPostgresEntity();
        r.setBazName(name);
        r.setColor(getColorByName(color));
        return r;
    }

    @Override
    protected BazPostgresEntity newEntity(String name) {
        var r = new BazPostgresEntity();
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
    protected void setColor(BazPostgresEntity entity, String color) {
        entity.setColor(getColorByName(color));
    }

    @Override
    protected ColorEnumPostgres getColor(BazPostgresEntity entity) {
        return entity.getColor();
    }

    @Override
    protected ColorEnumPostgres getColorByName(String color) {
        return ColorEnumPostgres.valueOf(color);
    }

    @Override
    protected Column<BazPostgresEntity, ?> getColorColumn() {
        return BazPostgresDao.COLUMN_COLOR;
    }

    @Override
    protected Column<BazPostgresEntity, Integer> getIdColumn() {
        return BazPostgresDao.COLUMN_BAZ_ID;
    }

    @Override
    protected Column<BazPostgresEntity, String> getNameColumn() {
        return BazPostgresDao.COLUMN_BAZ_NAME;
    }

}
