package dbtests.postgres.model;

import dbtests.framework.Column;
import dbtests.framework.Queryable;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BazViewPostgresQueryable extends Queryable<BazViewPostgresEntity> {

    public static final Column<BazViewPostgresEntity, Integer> COLUMN_BAZ_ID = new Column.IntColumn<>("baz_id", "bazId");

    public static final Column<BazViewPostgresEntity, String> COLUMN_BAZ_NAME = new Column.StringColumn<>("baz_name", "bazName");

    public static final Column<BazViewPostgresEntity, String> COLUMN_COLOR = new Column.StringColumn<>("color", "color");

    public static final List<Column<BazViewPostgresEntity, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_BAZ_ID,
            COLUMN_BAZ_NAME,
            COLUMN_COLOR);

    private static final String ALL_COLUMNS = " baz_id, baz_name, color ";

    private static final RowMapper<BazViewPostgresEntity> ROW_MAPPER = (rs, i) -> {
        BazViewPostgresEntity r = new BazViewPostgresEntity();
        r.setBazId(rs.getObject("baz_id") != null ? rs.getInt("baz_id") : null);
        r.setBazName(rs.getString("baz_name"));
        r.setColor(rs.getString("color"));
        return r;
    };

    @Autowired
    public BazViewPostgresQueryable(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<BazViewPostgresEntity> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.baz_view_postgres " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM test_schema.baz_view_postgres";
    }

    @Override
    protected List<Column<BazViewPostgresEntity, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.baz_view_postgres %n" +
                "WHERE 1=1 %s %n" +
                "%s " +
                "LIMIT %d", whereClause, orderBy, maxAllowedCount);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.baz_view_postgres %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                "LIMIT %d OFFSET %d", whereClause, orderBy, pageSize, start);
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
