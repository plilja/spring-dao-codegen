package dbtests.mssql.model;

import dbtests.framework.Column;
import dbtests.framework.Queryable;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BazViewMsSqlQueryable extends Queryable<BazViewMsSqlEntity> {

    public static final Column.StringColumn<BazViewMsSqlEntity> COLUMN_COLOR = new Column.StringColumn<>("color", "color");

    public static final Column.IntColumn<BazViewMsSqlEntity> COLUMN_ID = new Column.IntColumn<>("id", "id");

    public static final Column.StringColumn<BazViewMsSqlEntity> COLUMN_NAME_WITH_SPACE = new Column.StringColumn<>("name With space", "\"name With space\"", "nameWithSpace");

    public static final List<Column<BazViewMsSqlEntity, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_COLOR,
            COLUMN_ID,
            COLUMN_NAME_WITH_SPACE);

    private static final String ALL_COLUMNS = " color, id, \"name With space\" ";

    private static final RowMapper<BazViewMsSqlEntity> ROW_MAPPER = (rs, i) -> {
        BazViewMsSqlEntity r = new BazViewMsSqlEntity();
        r.setColor(rs.getString("color"));
        r.setId(rs.getInt("id"));
        r.setNameWithSpace(rs.getString("name With space"));
        return r;
    };

    @Autowired
    public BazViewMsSqlQueryable(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<BazViewMsSqlEntity> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT TOP %d " +
                ALL_COLUMNS +
                "FROM dbo.baz_view_ms_sql ", maxSelectCount);
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM dbo.baz_view_ms_sql";
    }

    @Override
    protected List<Column<BazViewMsSqlEntity, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT TOP %d %n" +
                ALL_COLUMNS +
                "FROM dbo.baz_view_ms_sql %n" +
                "WHERE 1=1 %s %n" +
                "%s", maxAllowedCount, whereClause, orderBy);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM dbo.baz_view_ms_sql %n" +
                "WHERE 1=1 %s" +
                "%s %n" +
                "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", whereClause, orderBy, start, pageSize);
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
