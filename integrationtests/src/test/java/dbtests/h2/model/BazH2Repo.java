package dbtests.h2.model;

import dbtests.framework.Column;
import dbtests.framework.CurrentUserProvider;
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
public class BazH2Repo extends Dao<BazH2, Integer> {

    public static final Column<BazH2, Integer> COLUMN_BAZ_ID = new Column<>("baz_id");

    public static final Column<BazH2, String> COLUMN_BAZ_NAME = new Column<>("baz_name");

    public static final Column<BazH2, LocalDateTime> COLUMN_CHANGED_AT = new Column<>("changed_at");

    public static final Column<BazH2, String> COLUMN_CHANGED_BY = new Column<>("changed_by");

    public static final Column<BazH2, ColorEnumH2> COLUMN_COLOR = new Column<>("color");

    public static final Column<BazH2, LocalDateTime> COLUMN_CREATED_AT = new Column<>("created_at");

    public static final Column<BazH2, String> COLUMN_CREATED_BY = new Column<>("created_by");

    public static final Column<BazH2, Integer> COLUMN_VERSION = new Column<>("version");

    public static final List<Column<BazH2, ?>> ALL_COLUMNS_LIST = Arrays.asList(COLUMN_BAZ_ID,
    COLUMN_BAZ_NAME,
    COLUMN_CHANGED_AT,
    COLUMN_CHANGED_BY,
    COLUMN_COLOR,
    COLUMN_CREATED_AT,
    COLUMN_CREATED_BY,
    COLUMN_VERSION);

    private static final String ALL_COLUMNS = " baz_id, baz_name, changed_at, changed_by, color, " +
            " created_at, created_by, version ";

    private static final RowMapper<BazH2> ROW_MAPPER = (rs, i) -> {
        BazH2 r = new BazH2();
        r.setBazId(rs.getInt("baz_id"));
        r.setBazName(rs.getString("baz_name"));
        r.setChangedAt(rs.getObject("changed_at") != null ? rs.getTimestamp("changed_at").toLocalDateTime() : null);
        r.setChangedBy(rs.getString("changed_by"));
        r.setColor(ColorEnumH2.fromId(rs.getString("color")));
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        r.setCreatedBy(rs.getString("created_by"));
        r.setVersion(rs.getObject("version") != null ? rs.getInt("version") : null);
        return r;
    };

    @Autowired
    public BazH2Repo(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(Integer.class, true, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(BazH2 o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("baz_id", o.getId(), Types.INTEGER);
        m.addValue("baz_name", o.getBazName(), Types.VARCHAR);
        m.addValue("changed_at", o.getChangedAt(), Types.TIMESTAMP);
        m.addValue("changed_by", o.getChangedBy(), Types.VARCHAR);
        m.addValue("color", o.getColor() != null ? o.getColor().getId() : null, Types.VARCHAR);
        m.addValue("created_at", o.getCreatedAt(), Types.TIMESTAMP);
        m.addValue("created_by", o.getCreatedBy(), Types.VARCHAR);
        m.addValue("version", o.getVersion(), Types.SMALLINT);
        return m;
    }

    @Override
    protected RowMapper<BazH2> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM test_schema.baz_h2 " +
                "WHERE baz_id = :baz_id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.baz_h2 " +
                "WHERE baz_id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.baz_h2 " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.baz_h2 (" +
                "baz_name, " +
                "changed_at, " +
                "changed_by, " +
                "color, " +
                "created_at, " +
                "created_by, " +
                "version" +
                ") " +
                "VALUES (" +
                ":baz_name, " +
                ":changed_at, " +
                ":changed_by, " +
                ":color, " +
                ":created_at, " +
                ":created_by, " +
                ":version" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE test_schema.baz_h2 SET " +
                "baz_name = :baz_name, " +
                "changed_at = :changed_at, " +
                "changed_by = :changed_by, " +
                "color = :color, " +
                "version = (COALESCE(:version, -1) + 1) % 128 " +
                "WHERE baz_id = :baz_id AND (version = :version OR version IS NULL OR COALESCE(:version, -1) = -1)";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM test_schema.baz_h2 " +
                "WHERE baz_id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM test_schema.baz_h2";
    }

    @Override
    public Column<BazH2, ?> getColumnByName(String name) {
        for (Column<BazH2, ?> column : ALL_COLUMNS_LIST) {
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
                "FROM test_schema.baz_h2 %n" +
                "WHERE 1=1 %s %n" +
                "%s " +
                "LIMIT %d", whereClause, orderBy, maxAllowedCount);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.baz_h2 %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                "LIMIT %d OFFSET %d", whereClause, orderBy, pageSize, start);
    }

    @Override
    protected String getSelectAndLockSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.baz_h2 " +
                "WHERE baz_id = :id " +
                "FOR UPDATE";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "baz_id";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
