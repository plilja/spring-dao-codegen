package dbtests.mssql.model;

import dbtests.framework.Column;
import dbtests.framework.Dao;
import dbtests.framework.DatabaseException;
import java.time.LocalDateTime;
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

    public static final Column<BazMsSqlEntity, LocalDateTime> COLUMN_MODIFIED_AT = new Column<>("modified_at");

    public static final Column<BazMsSqlEntity, String> COLUMN_NAME = new Column<>("name");

    public static final Column<BazMsSqlEntity, Integer> COLUMN_VERSION = new Column<>("version");

    private static final String ALL_COLUMNS = " id, color, inserted_at, modified_at, name, " +
            " version ";

    private static final RowMapper<BazMsSqlEntity> ROW_MAPPER = (rs, i) -> {
        BazMsSqlEntity r = new BazMsSqlEntity();
        r.setId(rs.getInt("id"));
        r.setColor(ColorEnumMsSql.fromId(rs.getString("color")));
        r.setInsertedAt(rs.getTimestamp("inserted_at").toLocalDateTime());
        r.setModifiedAt(rs.getObject("modified_at") != null ? rs.getTimestamp("modified_at").toLocalDateTime() : null);
        r.setName(rs.getString("name"));
        r.setVersion(rs.getInt("version"));
        return r;
    };

    @Autowired
    public BazMsSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(BazMsSqlEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        m.addValue("color", o.getColor() != null ? o.getColor().getId() : null);
        m.addValue("inserted_at", o.getInsertedAt());
        m.addValue("modified_at", o.getModifiedAt());
        m.addValue("name", o.getName());
        m.addValue("version", o.getVersion());
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
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM dbo.baz_ms_sql %n" +
                "ORDER BY id %n" +
                "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", start, pageSize);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO dbo.baz_ms_sql (" +
                "color, " +
                "inserted_at, " +
                "modified_at, " +
                "name, " +
                "version" +
                ") " +
                "VALUES (" +
                ":color, " +
                ":inserted_at, " +
                ":modified_at, " +
                ":name, " +
                ":version" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE dbo.baz_ms_sql SET " +
                "color = :color, " +
                "modified_at = :modified_at, " +
                "name = :name, " +
                "version = (version + 1) % 128 " +
                "WHERE id = :id AND version = :version";
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
    protected String getQuerySql() {
        return "SELECT TOP %d " +
                ALL_COLUMNS +
                "FROM dbo.baz_ms_sql " +
                "WHERE %s ";
    }

    @Override
    protected String getLockSql() {
        return "SELECT id FROM dbo.baz_ms_sql WITH (UPDLOCK) " +
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
