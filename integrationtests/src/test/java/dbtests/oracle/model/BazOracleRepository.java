package dbtests.oracle.model;

import dbtests.framework.Column;
import dbtests.framework.Dao;
import dbtests.framework.DatabaseException;
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
public class BazOracleRepository extends Dao<BazOracle, Integer> {

    public static final Column<BazOracle, Integer> COLUMN_ID = new Column<>("ID");

    public static final Column<BazOracle, LocalDateTime> COLUMN_CHANGED_AT = new Column<>("CHANGED_AT");

    public static final Column<BazOracle, ColorEnumOracle> COLUMN_COLOR = new Column<>("COLOR");

    public static final Column<BazOracle, LocalDateTime> COLUMN_CREATED_AT = new Column<>("CREATED_AT");

    public static final Column<BazOracle, String> COLUMN_NAME = new Column<>("NAME");

    public static final Column<BazOracle, Integer> COLUMN_VERSION = new Column<>("VERSION");

    public static final List<Column<BazOracle, ?>> ALL_COLUMNS_LIST = Arrays.asList(COLUMN_ID,
    COLUMN_CHANGED_AT,
    COLUMN_COLOR,
    COLUMN_CREATED_AT,
    COLUMN_NAME,
    COLUMN_VERSION);

    private static final String ALL_COLUMNS = " ID, CHANGED_AT, COLOR, CREATED_AT, NAME, " +
            " VERSION ";

    private static final RowMapper<BazOracle> ROW_MAPPER = (rs, i) -> {
        BazOracle r = new BazOracle();
        r.setId(rs.getInt("ID"));
        r.setChangedAt(rs.getObject("CHANGED_AT") != null ? rs.getTimestamp("CHANGED_AT").toLocalDateTime() : null);
        r.setColor(ColorEnumOracle.fromId(rs.getString("COLOR")));
        r.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
        r.setName(rs.getString("NAME"));
        r.setVersion(rs.getObject("VERSION") != null ? rs.getInt("VERSION") : null);
        return r;
    };

    @Autowired
    public BazOracleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    protected SqlParameterSource getParams(BazOracle o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("ID", o.getId(), Types.INTEGER);
        m.addValue("CHANGED_AT", o.getChangedAt(), Types.TIMESTAMP);
        m.addValue("COLOR", o.getColor() != null ? o.getColor().getId() : null, Types.VARCHAR);
        m.addValue("CREATED_AT", o.getCreatedAt(), Types.TIMESTAMP);
        m.addValue("NAME", o.getName(), Types.VARCHAR);
        m.addValue("VERSION", o.getVersion(), Types.INTEGER);
        return m;
    }

    @Override
    protected RowMapper<BazOracle> getRowMapper() {
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
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT * FROM (%n" +
                "SELECT rownum tmp_rownum_, a.* %n" +
                "FROM (SELECT %n" +
                ALL_COLUMNS +
                "FROM DOCKER.BAZ_ORACLE %n" +
                "ORDER BY ID %n" +
                ") a %n" +
                "WHERE rownum < %d + %d %n" +
                ")%n" +
                "WHERE tmp_rownum_ >= %d", start + 1, pageSize, start + 1);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO DOCKER.BAZ_ORACLE (" +
                "CHANGED_AT, " +
                "COLOR, " +
                "CREATED_AT, " +
                "NAME, " +
                "VERSION" +
                ") " +
                "VALUES (" +
                ":CHANGED_AT, " +
                ":COLOR, " +
                ":CREATED_AT, " +
                ":NAME, " +
                ":VERSION" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE DOCKER.BAZ_ORACLE SET " +
                "CHANGED_AT = :CHANGED_AT, " +
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
    public Column<BazOracle, ?> getColumnByName(String name) {
        for (Column<BazOracle, ?> column : ALL_COLUMNS_LIST) {
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
