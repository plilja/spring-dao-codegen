package dbtests.mysql.model;

import dbtests.framework.Column;
import dbtests.framework.Queryable;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BazViewMysqlQueryable extends Queryable<MBazViewMysql> {

    public static final Column<MBazViewMysql, Integer> COLUMN_COLOR_ENUM_MYSQL_ID = new Column.IntColumn<>("color_enum_mysql_id", "colorEnumMysqlId");

    public static final Column<MBazViewMysql, Integer> COLUMN_ID = new Column.IntColumn<>("id", "id");

    public static final Column<MBazViewMysql, String> COLUMN_NAME = new Column.StringColumn<>("name", "name");

    public static final List<Column<MBazViewMysql, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_COLOR_ENUM_MYSQL_ID,
            COLUMN_ID,
            COLUMN_NAME);

    private static final String ALL_COLUMNS = " color_enum_mysql_id, id, name ";

    private static final RowMapper<MBazViewMysql> ROW_MAPPER = (rs, i) -> {
        MBazViewMysql r = new MBazViewMysql();
        r.setColorEnumMysqlId(rs.getObject("color_enum_mysql_id") != null ? rs.getInt("color_enum_mysql_id") : null);
        r.setId(rs.getInt("id"));
        r.setName(rs.getString("name"));
        return r;
    };

    @Autowired
    public BazViewMysqlQueryable(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<MBazViewMysql> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM BazViewMysql " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM BazViewMysql";
    }

    @Override
    protected List<Column<MBazViewMysql, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM BazViewMysql %n" +
                "WHERE 1=1 %s %n" +
                "%s " +
                "LIMIT %d", whereClause, orderBy, maxAllowedCount);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM BazViewMysql %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                "LIMIT %d OFFSET %d", whereClause, orderBy, pageSize, start);
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
