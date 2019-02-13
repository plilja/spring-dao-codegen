package dbtests.h2.tests;

import dbtests.QueryApiBaseTest;
import dbtests.framework.Column;
import dbtests.h2.model.BazH2;
import dbtests.h2.model.BazH2Repo;
import dbtests.h2.model.ColorEnumH2;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {H2Config.class})
@ExtendWith(SpringExtension.class)
class H2QueryApiIT extends QueryApiBaseTest<BazH2, BazH2Repo> {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazH2Repo repo;

    @Override
    protected BazH2Repo getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM test_schema.baz_h2", new MapSqlParameterSource());
    }

    @Override
    protected BazH2 newEntity(String name, String color) {
        BazH2 r = new BazH2();
        r.setBazName(name);
        r.setColor(getColorByName(color));
        return r;
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
    protected void setColor(BazH2 entity, String color) {
        entity.setColor(getColorByName(color));
    }

    @Override
    protected ColorEnumH2 getColor(BazH2 entity) {
        return entity.getColor();
    }

    @Override
    protected ColorEnumH2 getColorByName(String color) {
        return ColorEnumH2.valueOf(color);
    }

    @Override
    protected Column<BazH2, ?> getColorColumn() {
        return BazH2Repo.COLUMN_COLOR;
    }

    @Override
    protected Column<BazH2, Integer> getIdColumn() {
        return BazH2Repo.COLUMN_BAZ_ID;
    }

    @Override
    protected Column<BazH2, String> getNameColumn() {
        return BazH2Repo.COLUMN_BAZ_NAME;
    }

}
