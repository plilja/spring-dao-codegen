package dbtests.oracle.model;

import dbtests.framework.Column;
import dbtests.framework.Queryable;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BazViewOracleQueryable extends Queryable<BazViewOracle> {

    public static final Column.StringColumn<BazViewOracle> COLUMN_COLOR = new Column.StringColumn<>("COLOR", "color");

    public static final Column.IntColumn<BazViewOracle> COLUMN_ID = new Column.IntColumn<>("ID", "id");

    public static final Column.StringColumn<BazViewOracle> COLUMN_NAME = new Column.StringColumn<>("NAME", "name");

    public static final List<Column<BazViewOracle, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_COLOR,
            COLUMN_ID,
            COLUMN_NAME);

    private static final String ALL_COLUMNS = " COLOR, ID, NAME ";

    private static final RowMapper<BazViewOracle> ROW_MAPPER = (rs, i) -> {
        BazViewOracle r = new BazViewOracle();
        r.setColor(rs.getString("COLOR"));
        r.setId(rs.getInt("ID"));
        r.setName(rs.getString("NAME"));
        return r;
    };

    @Autowired
    public BazViewOracleQueryable(NamedParameterJdbcTemplate jdbcTemplate) {
        super(BazViewOracle.class, jdbcTemplate);
    }

    @Override
    protected RowMapper<BazViewOracle> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_VIEW_ORACLE " +
                "WHERE ROWNUM <= %d", maxSelectCount);
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM DOCKER.BAZ_VIEW_ORACLE";
    }

    @Override
    protected List<Column<BazViewOracle, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_VIEW_ORACLE %n" +
                "WHERE ROWNUM <= %d %s %n" +
                "%s", maxAllowedCount, whereClause, orderBy);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT * FROM (%n" +
                "SELECT rownum tmp_rownum_, a.* %n" +
                "FROM (SELECT %n" +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_VIEW_ORACLE %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                ") a %n" +
                "WHERE rownum < %d + %d %n" +
                ")%n" +
                "WHERE tmp_rownum_ >= %d", whereClause, orderBy, start + 1, pageSize, start + 1);
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
