package dbtests.mssql.model;

import dbtests.framework.Column;
import dbtests.framework.CurrentUserProvider;
import dbtests.framework.Dao;
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
public class BazMsSqlDao extends Dao<BazMsSqlEntity, Integer> {

    public static final Column<BazMsSqlEntity, Integer> COLUMN_ID = new Column<>("id");

    public static final Column<BazMsSqlEntity, ColorEnumMsSql> COLUMN_COLOR = new Column<>("color");

    public static final Column<BazMsSqlEntity, LocalDateTime> COLUMN_INSERTED_AT = new Column<>("inserted_at");

    public static final Column<BazMsSqlEntity, String> COLUMN_INSERTED_BY = new Column<>("inserted_by");

    public static final Column<BazMsSqlEntity, LocalDateTime> COLUMN_MODIFIED_AT = new Column<>("modified_at");

    public static final Column<BazMsSqlEntity, String> COLUMN_MODIFIED_BY = new Column<>("modified_by");

    public static final Column<BazMsSqlEntity, String> COLUMN_NAME = new Column<>("name");

    public static final Column<BazMsSqlEntity, Integer> COLUMN_VERSION = new Column<>("version");

    public static final List<Column<BazMsSqlEntity, ?>> ALL_COLUMNS_LIST = Arrays.asList(COLUMN_ID,
    COLUMN_COLOR,
    COLUMN_INSERTED_AT,
    COLUMN_INSERTED_BY,
    COLUMN_MODIFIED_AT,
    COLUMN_MODIFIED_BY,
    COLUMN_NAME,
    COLUMN_VERSION);

    private static final String ALL_COLUMNS = " id, color, inserted_at, inserted_by, modified_at, " +
            " modified_by, name, version ";

    private static final RowMapper<BazMsSqlEntity> ROW_MAPPER = (rs, i) -> {
        BazMsSqlEntity r = new BazMsSqlEntity();
        r.setId(rs.getInt("id"));
        r.setColor(ColorEnumMsSql.fromId(rs.getString("color")));
        r.setInsertedAt(rs.getTimestamp("inserted_at").toLocalDateTime());
        r.setInsertedBy(rs.getString("inserted_by"));
        r.setModifiedAt(rs.getObject("modified_at") != null ? rs.getTimestamp("modified_at").toLocalDateTime() : null);
        r.setModifiedBy(rs.getString("modified_by"));
        r.setName(rs.getString("name"));
        r.setVersion(rs.getObject("version") != null ? rs.getInt("version") : null);
        return r;
    };

    @Autowired
    public BazMsSqlDao(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(Integer.class, true, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(BazMsSqlEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.INTEGER);
        m.addValue("color", o.getColor() != null ? o.getColor().getId() : null, Types.VARCHAR);
        m.addValue("inserted_at", o.getInsertedAt(), Types.TIMESTAMP);
        m.addValue("inserted_by", o.getInsertedBy(), Types.VARCHAR);
        m.addValue("modified_at", o.getModifiedAt(), Types.TIMESTAMP);
        m.addValue("modified_by", o.getModifiedBy(), Types.VARCHAR);
        m.addValue("name", o.getName(), Types.VARCHAR);
        m.addValue("version", o.getVersion(), Types.TINYINT);
        return m;
    }

    @Override
    protected RowMapper<BazMsSqlEntity> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM dbo.baz_ms_sql " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM dbo.baz_ms_sql " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT TOP %d " +
                ALL_COLUMNS +
                "FROM dbo.baz_ms_sql ", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO dbo.baz_ms_sql (" +
                "color, " +
                "inserted_at, " +
                "inserted_by, " +
                "modified_at, " +
                "modified_by, " +
                "name, " +
                "version" +
                ") " +
                "VALUES (" +
                ":color, " +
                ":inserted_at, " +
                ":inserted_by, " +
                ":modified_at, " +
                ":modified_by, " +
                ":name, " +
                ":version" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE dbo.baz_ms_sql SET " +
                "color = :color, " +
                "modified_at = :modified_at, " +
                "modified_by = :modified_by, " +
                "name = :name, " +
                "version = (COALESCE(:version, -1) + 1) % 128 " +
                "WHERE id = :id AND (version = :version OR version IS NULL OR :version IS NULL)";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM dbo.baz_ms_sql " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM dbo.baz_ms_sql";
    }

    @Override
    public Column<BazMsSqlEntity, ?> getColumnByName(String name) {
        for (Column<BazMsSqlEntity, ?> column : ALL_COLUMNS_LIST) {
            if (column.getName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT TOP %d %n" +
                ALL_COLUMNS +
                "FROM dbo.baz_ms_sql %n" +
                "WHERE 1=1 %s %n" +
                "%s", maxAllowedCount, whereClause, orderBy);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM dbo.baz_ms_sql %n" +
                "WHERE 1=1 %s" +
                "%s %n" +
                "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", whereClause, orderBy, start, pageSize);
    }

    @Override
    protected String getSelectAndLockSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM dbo.baz_ms_sql WITH (UPDLOCK) " +
                "WHERE id = :id";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "id";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
