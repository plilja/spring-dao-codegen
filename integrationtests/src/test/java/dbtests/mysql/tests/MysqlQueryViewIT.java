package dbtests.mysql.tests;

import dbtests.QueryViewTest;
import dbtests.framework.Column;
import dbtests.mysql.model.BazViewMysqlQueryable;
import dbtests.mysql.model.ColorEnumMysql;
import dbtests.mysql.model.MBazMysql;
import dbtests.mysql.model.MBazMysqlRepo;
import dbtests.mysql.model.MBazViewMysql;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MysqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class MysqlQueryViewIT extends QueryViewTest<MBazViewMysql, BazViewMysqlQueryable> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private MBazMysqlRepo bazRepo;

    @Autowired
    private BazViewMysqlQueryable viewQueryable;

    @Override
    public void clearTable() {
        jdbcTemplate.update("DELETE FROM BazMysql", new MapSqlParameterSource());
    }

    @Override
    protected void saveNew(String name, String color) {
        MBazMysql o = new MBazMysql();
        o.setName(name);
        o.setColorEnumMysql(ColorEnumMysql.valueOf(color));
        bazRepo.save(o);
    }

    @Override
    protected String getName(MBazViewMysql o) {
        return o.getNameWithSpace();
    }

    @Override
    protected String getColor(MBazViewMysql o) {
        return ColorEnumMysql.fromId(o.getColorEnumMysqlId()).name();
    }

    @Override
    protected BazViewMysqlQueryable getQueryable() {
        return viewQueryable;
    }

    @Override
    protected Column<MBazViewMysql, String> getNameColumn() {
        return BazViewMysqlQueryable.COLUMN_NAME_WITH_SPACE;
    }
}
