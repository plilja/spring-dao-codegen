package dbtests.mssql.tests;

import dbtests.QueryViewTest;
import dbtests.framework.Column;
import dbtests.mssql.model.BazMsSqlDao;
import dbtests.mssql.model.BazMsSqlEntity;
import dbtests.mssql.model.BazViewMsSqlEntity;
import dbtests.mssql.model.BazViewMsSqlQueryable;
import dbtests.mssql.model.ColorEnumMsSql;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MsSqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class MsSqlQueryViewIT extends QueryViewTest<BazViewMsSqlEntity, BazViewMsSqlQueryable> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazMsSqlDao repo;

    @Autowired
    private BazViewMsSqlQueryable viewRepo;

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM baz_ms_sql", new MapSqlParameterSource());
    }

    @Override
    protected void saveNew(String name, String color) {
        BazMsSqlEntity o = new BazMsSqlEntity();
        o.setName(name);
        o.setColor(ColorEnumMsSql.valueOf(color));
        repo.save(o);
    }

    @Override
    protected String getName(BazViewMsSqlEntity o) {
        return o.getName();
    }

    @Override
    protected String getColor(BazViewMsSqlEntity o) {
        return ColorEnumMsSql.fromId(o.getColor()).name();
    }

    @Override
    protected BazViewMsSqlQueryable getQueryable() {
        return viewRepo;
    }

    @Override
    protected Column<BazViewMsSqlEntity, String> getNameColumn() {
        return BazViewMsSqlQueryable.COLUMN_NAME;
    }
}
