package dbtests.mysql.tests;

import dbtests.QueryApiBaseTest;
import dbtests.framework.Column;
import dbtests.mysql.model.ColorEnumMysql;
import dbtests.mysql.model.MBazMysql;
import dbtests.mysql.model.MBazMysqlRepo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MysqlITConfig.class})
@ExtendWith(SpringExtension.class)
public class MysqlQueryApiIT extends QueryApiBaseTest<MBazMysql, MBazMysqlRepo> {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private MBazMysqlRepo repo;

    @Override
    protected MBazMysqlRepo getRepo() {
        return repo;
    }

    @Override
    protected void clearTable() {
        jdbcTemplate.update("DELETE FROM BazMysql", new MapSqlParameterSource());
    }

    @Override
    protected MBazMysql newEntity(String name, String color) {
        MBazMysql e = new MBazMysql();
        e.setName(name);
        e.setColorEnumMysql(getColorByName(color));
        return e;
    }

    @Override
    protected MBazMysql newEntity(String name) {
        MBazMysql e = new MBazMysql();
        e.setName(name);
        return e;
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
    protected void setColor(MBazMysql entity, String color) {
        entity.setColorEnumMysql(getColorByName(color));
    }

    @Override
    protected ColorEnumMysql getColor(MBazMysql entity) {
        return entity.getColorEnumMysql();
    }

    @Override
    protected ColorEnumMysql getColorByName(String color) {
        return ColorEnumMysql.valueOf(color);
    }

    @Override
    protected Column<MBazMysql, ?> getColorColumn() {
        return MBazMysqlRepo.COLUMN_COLOR_ENUM_MYSQL_ID;
    }

    @Override
    protected Column<MBazMysql, Integer> getIdColumn() {
        return MBazMysqlRepo.COLUMN_ID;
    }

    @Override
    protected Column<MBazMysql, String> getNameColumn() {
        return MBazMysqlRepo.COLUMN_NAME;
    }
}
