package db;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BazOracleH2Dao extends Dao<BazOracleH2, Integer> {

    public static final Column<BazOracleH2, Integer> COLUMN_ID = new Column<>("ID");

    public static final Column<BazOracleH2, LocalDateTime> COLUMN_CHANGED_AT = new Column<>("CHANGED_AT");

    public static final Column<BazOracleH2, String> COLUMN_CHANGED_BY = new Column<>("CHANGED_BY");

    public static final Column<BazOracleH2, ColorEnumOracle> COLUMN_COLOR = new Column<>("COLOR");

    public static final Column<BazOracleH2, LocalDateTime> COLUMN_CREATED_AT = new Column<>("CREATED_AT");

    public static final Column<BazOracleH2, String> COLUMN_CREATED_BY = new Column<>("CREATED_BY");

    public static final Column<BazOracleH2, String> COLUMN_NAME = new Column<>("NAME");

    public static final Column<BazOracleH2, Integer> COLUMN_VERSION = new Column<>("VERSION");

    public static final List<Column<BazOracleH2, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_ID,
            COLUMN_CHANGED_AT,
            COLUMN_CHANGED_BY,
            COLUMN_COLOR,
            COLUMN_CREATED_AT,
            COLUMN_CREATED_BY,
            COLUMN_NAME,
            COLUMN_VERSION);

    private static final String ALL_COLUMNS = " ID, CHANGED_AT, CHANGED_BY, COLOR, CREATED_AT, " +
            " CREATED_BY, NAME, VERSION ";

    private static final RowMapper<BazOracleH2> ROW_MAPPER = (rs, i) -> {
        BazOracleH2 r = new BazOracleH2();
        r.setId(rs.getInt("ID"));
        r.setChangedAt(rs.getObject("CHANGED_AT") != null ? rs.getTimestamp("CHANGED_AT").toLocalDateTime() : null);
        r.setChangedBy(rs.getString("CHANGED_BY"));
        r.setColor(ColorEnumOracle.fromId(rs.getString("COLOR")));
        r.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
        r.setCreatedBy(rs.getString("CREATED_BY"));
        r.setName(rs.getString("NAME"));
        r.setVersion(rs.getObject("VERSION") != null ? rs.getInt("VERSION") : null);
        return r;
    };

    @Autowired
    public BazOracleH2Dao(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(Integer.class, false, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(BazOracleH2 o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("ID", o.getId(), Types.INTEGER);
        m.addValue("CHANGED_AT", o.getChangedAt(), Types.TIMESTAMP);
        m.addValue("CHANGED_BY", o.getChangedBy(), Types.VARCHAR);
        m.addValue("COLOR", o.getColor() != null ? o.getColor().getId() : null, Types.VARCHAR);
        m.addValue("CREATED_AT", o.getCreatedAt(), Types.TIMESTAMP);
        m.addValue("CREATED_BY", o.getCreatedBy(), Types.VARCHAR);
        m.addValue("NAME", o.getName(), Types.VARCHAR);
        m.addValue("VERSION", o.getVersion(), Types.INTEGER);
        return m;
    }

    @Override
    protected RowMapper<BazOracleH2> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ID = :ID";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ROWNUM <= %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO DOCKER.BAZ_ORACLE (" +
                "ID, " +
                "CHANGED_AT, " +
                "CHANGED_BY, " +
                "COLOR, " +
                "CREATED_AT, " +
                "CREATED_BY, " +
                "NAME, " +
                "VERSION" +
                ") " +
                "VALUES (" +
                ":ID, " +
                ":CHANGED_AT, " +
                ":CHANGED_BY, " +
                ":COLOR, " +
                ":CREATED_AT, " +
                ":CREATED_BY, " +
                ":NAME, " +
                ":VERSION" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE DOCKER.BAZ_ORACLE SET " +
                "CHANGED_AT = :CHANGED_AT, " +
                "CHANGED_BY = :CHANGED_BY, " +
                "COLOR = :COLOR, " +
                "NAME = :NAME, " +
                "VERSION = MOD(NVL(:VERSION, -1) + 1, 128) " +
                "WHERE ID = :ID AND (VERSION = :VERSION OR VERSION IS NULL OR :VERSION IS NULL)";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM DOCKER.BAZ_ORACLE " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM DOCKER.BAZ_ORACLE";
    }

    @Override
    public Column<BazOracleH2, ?> getColumnByName(String name) {
        for (Column<BazOracleH2, ?> column : ALL_COLUMNS_LIST) {
            if (column.getName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_ORACLE %n" +
                "WHERE ROWNUM <= %d %s %n" +
                "%s", maxAllowedCount, whereClause, orderBy);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT * FROM (%n" +
                "SELECT rownum tmp_rownum_, a.* %n" +
                "FROM (SELECT %n" +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_ORACLE %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                ") a %n" +
                "WHERE rownum < %d + %d %n" +
                ")%n" +
                "WHERE tmp_rownum_ >= %d", whereClause, orderBy, start + 1, pageSize, start + 1);
    }

    @Override
    protected String getSelectAndLockSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_ORACLE " +
                "WHERE ID = :id " +
                "FOR UPDATE";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "ID";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
