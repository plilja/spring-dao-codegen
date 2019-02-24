package dbtests.h2.mssql;

import dbtests.QueryApiBaseTest;
import dbtests.framework.Column;
import dbtests.mssql.model.BazMsSqlDao;
import dbtests.mssql.model.BazMsSqlEntity;
import dbtests.mssql.model.ColorEnumMsSql;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {H2MsSqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class H2MsSqlQueryApiIT extends QueryApiBaseTest<BazMsSqlEntity, BazMsSqlDao> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazMsSqlDao repo;

    @Override
    protected BazMsSqlDao getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM dbo.baz_ms_sql", new MapSqlParameterSource());
    }

    @Override
    protected BazMsSqlEntity newEntity(String name, String color) {
        BazMsSqlEntity e = new BazMsSqlEntity();
        e.setName(name);
        e.setColor(getColorByName(color));
        return e;
    }

    @Override
    protected BazMsSqlEntity newEntity(String name) {
        BazMsSqlEntity e = new BazMsSqlEntity();
        e.setName(name);
        return e;
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
    protected void setColor(BazMsSqlEntity entity, String color) {
        entity.setColor(getColorByName(color));
    }

    @Override
    protected ColorEnumMsSql getColor(BazMsSqlEntity entity) {
        return entity.getColor();
    }

    @Override
    protected ColorEnumMsSql getColorByName(String color) {
        return ColorEnumMsSql.valueOf(color);
    }

    @Override
    protected Column<BazMsSqlEntity, ?> getColorColumn() {
        return BazMsSqlDao.COLUMN_COLOR;
    }

    @Override
    protected Column<BazMsSqlEntity, Integer> getIdColumn() {
        return BazMsSqlDao.COLUMN_ID;
    }

    @Override
    protected Column<BazMsSqlEntity, String> getNameColumn() {
        return BazMsSqlDao.COLUMN_NAME;
    }
}
