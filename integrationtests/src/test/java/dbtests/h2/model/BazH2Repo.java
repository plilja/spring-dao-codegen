package dbtests.h2.model;

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
public class BazH2Repo extends Dao<BazH2, Integer> {

    public static final Column<BazH2, Integer> COLUMN_BAZ_ID = new Column<>("baz_id");

    public static final Column<BazH2, String> COLUMN_BAZ_NAME = new Column<>("baz_name");

    public static final Column<BazH2, LocalDateTime> COLUMN_CHANGED_AT = new Column<>("changed_at");

    public static final Column<BazH2, ColorEnumH2> COLUMN_COLOR = new Column<>("color");

    public static final Column<BazH2, LocalDateTime> COLUMN_CREATED_AT = new Column<>("created_at");

    public static final Column<BazH2, Integer> COLUMN_VERSION = new Column<>("version");

    private static final String ALL_COLUMNS = " baz_id, baz_name, changed_at, color, created_at, " +
            " version ";

    private static final RowMapper<BazH2> ROW_MAPPER = (rs, i) -> {
        BazH2 r = new BazH2();
        r.setBazId(rs.getInt("baz_id"));
        r.setBazName(rs.getString("baz_name"));
        r.setChangedAt(rs.getObject("changed_at") != null ? rs.getTimestamp("changed_at").toLocalDateTime() : null);
        r.setColor(ColorEnumH2.fromId(rs.getString("color")));
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        r.setVersion(rs.getInt("version"));
        return r;
    };

    @Autowired
    public BazH2Repo(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(BazH2 o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("baz_id", o.getId());
        m.addValue("baz_name", o.getBazName());
        m.addValue("changed_at", o.getChangedAt());
        m.addValue("color", o.getColor() != null ? o.getColor().getId() : null);
        m.addValue("created_at", o.getCreatedAt());
        m.addValue("version", o.getVersion());
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
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.baz_h2 %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.baz_h2 (" +
                "baz_name, " +
                "changed_at, " +
                "color, " +
                "created_at, " +
                "version" +
                ") " +
                "VALUES (" +
                ":baz_name, " +
                ":changed_at, " +
                ":color, " +
                ":created_at, " +
                ":version" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE test_schema.baz_h2 SET " +
                "baz_name = :baz_name, " +
                "changed_at = :changed_at, " +
                "color = :color, " +
                "version = (version + 1) % 128 " +
                "WHERE baz_id = :baz_id AND version = :version";
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
    protected String getQuerySql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.baz_h2 " +
                "WHERE %s " +
                "LIMIT %d";
    }

    @Override
    protected String getLockSql() {
        return "SELECT baz_id FROM test_schema.baz_h2 " +
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
