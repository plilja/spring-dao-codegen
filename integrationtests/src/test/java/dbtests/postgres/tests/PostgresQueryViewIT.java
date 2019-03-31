package dbtests.postgres.tests;

import dbtests.QueryViewTest;
import dbtests.framework.Column;
import dbtests.postgres.model.BazPostgresDao;
import dbtests.postgres.model.BazPostgresEntity;
import dbtests.postgres.model.BazViewPostgresEntity;
import dbtests.postgres.model.BazViewPostgresView;
import dbtests.postgres.model.ColorEnumPostgres;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PostgresITConfig.class})
@ExtendWith(SpringExtension.class)
public class PostgresQueryViewIT extends QueryViewTest<BazViewPostgresEntity, BazViewPostgresView> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazPostgresDao repo;

    @Autowired
    private BazViewPostgresView viewQueryable;

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.baz_postgres", new MapSqlParameterSource());
    }

    @Override
    protected void saveNew(String name, String color) {
        BazPostgresEntity o = new BazPostgresEntity();
        o.setBazName(name);
        o.setColor(ColorEnumPostgres.valueOf(color));
        repo.save(o);
    }

    @Override
    protected String getName(BazViewPostgresEntity o) {
        return o.getNameWithSpace();
    }

    @Override
    protected String getColor(BazViewPostgresEntity o) {
        return ColorEnumPostgres.fromId(o.getColor()).name();
    }

    @Override
    protected BazViewPostgresView getQueryable() {
        return viewQueryable;
    }

    @Override
    protected Column<BazViewPostgresEntity, String> getNameColumn() {
        return BazViewPostgresView.COLUMN_NAME_WITH_SPACE;
    }
}
