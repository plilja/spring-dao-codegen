package dbtests.postgres.model;

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
public class BazPostgresDao extends Dao<BazPostgresEntity, Integer> {

    public static final Column<BazPostgresEntity, Integer> COLUMN_BAZ_ID = new Column.IntColumn<>("baz_id", "bazId");

    public static final Column<BazPostgresEntity, String> COLUMN_BAZ_NAME = new Column.StringColumn<>("baz_name", "bazName");

    public static final Column<BazPostgresEntity, LocalDateTime> COLUMN_CHANGED_AT = new Column.DateTimeColumn<>("changed_at", "changedAt");

    public static final Column<BazPostgresEntity, String> COLUMN_CHANGED_BY = new Column.StringColumn<>("changed_by", "changedBy");

    public static final Column<BazPostgresEntity, ColorEnumPostgres> COLUMN_COLOR = new Column<>("color", "color", ColorEnumPostgres.class);

    public static final Column<BazPostgresEntity, Integer> COLUMN_COUNTER = new Column.IntColumn<>("counter", "counter");

    public static final Column<BazPostgresEntity, LocalDateTime> COLUMN_CREATED_AT = new Column.DateTimeColumn<>("created_at", "createdAt");

    public static final Column<BazPostgresEntity, String> COLUMN_CREATED_BY = new Column.StringColumn<>("created_by", "createdBy");

    public static final List<Column<BazPostgresEntity, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_BAZ_ID,
            COLUMN_BAZ_NAME,
            COLUMN_CHANGED_AT,
            COLUMN_CHANGED_BY,
            COLUMN_COLOR,
            COLUMN_COUNTER,
            COLUMN_CREATED_AT,
            COLUMN_CREATED_BY);

    private static final String ALL_COLUMNS = " baz_id, baz_name, changed_at, changed_by, color, " +
            " counter, created_at, created_by ";

    private static final RowMapper<BazPostgresEntity> ROW_MAPPER = (rs, i) -> {
        BazPostgresEntity r = new BazPostgresEntity();
        r.setBazId(rs.getInt("baz_id"));
        r.setBazName(rs.getString("baz_name"));
        r.setChangedAt(rs.getObject("changed_at") != null ? rs.getTimestamp("changed_at").toLocalDateTime() : null);
        r.setChangedBy(rs.getString("changed_by"));
        r.setColor(ColorEnumPostgres.fromId(rs.getString("color")));
        r.setCounter(rs.getObject("counter") != null ? rs.getInt("counter") : null);
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        r.setCreatedBy(rs.getString("created_by"));
        return r;
    };

    @Autowired
    public BazPostgresDao(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(Integer.class, true, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(BazPostgresEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("baz_id", o.getId(), Types.INTEGER);
        m.addValue("baz_name", o.getBazName(), Types.VARCHAR);
        m.addValue("changed_at", o.getChangedAt(), Types.TIMESTAMP);
        m.addValue("changed_by", o.getChangedBy(), Types.VARCHAR);
        m.addValue("color", o.getColor() != null ? o.getColor().getId() : null, Types.VARCHAR);
        m.addValue("counter", o.getCounter(), Types.BIGINT);
        m.addValue("created_at", o.getCreatedAt(), Types.TIMESTAMP);
        m.addValue("created_by", o.getCreatedBy(), Types.VARCHAR);
        return m;
    }

    @Override
    protected RowMapper<BazPostgresEntity> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM test_schema.baz_postgres " +
                "WHERE baz_id = :baz_id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.baz_postgres " +
                "WHERE baz_id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.baz_postgres " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.baz_postgres (" +
                "baz_name, " +
                "changed_at, " +
                "changed_by, " +
                "color, " +
                "counter, " +
                "created_at, " +
                "created_by" +
                ") " +
                "VALUES (" +
                ":baz_name, " +
                ":changed_at, " +
                ":changed_by, " +
                ":color, " +
                ":counter, " +
                ":created_at, " +
                ":created_by" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE test_schema.baz_postgres SET " +
                "baz_name = :baz_name, " +
                "changed_at = :changed_at, " +
                "changed_by = :changed_by, " +
                "color = :color, " +
                "counter = (COALESCE(:counter, -1) + 1) % 128 " +
                "WHERE baz_id = :baz_id AND (counter = :counter OR counter IS NULL OR COALESCE(:counter, -1) = -1)";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM test_schema.baz_postgres " +
                "WHERE baz_id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM test_schema.baz_postgres";
    }

    @Override
    protected List<Column<BazPostgresEntity, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.baz_postgres %n" +
                "WHERE 1=1 %s %n" +
                "%s " +
                "LIMIT %d", whereClause, orderBy, maxAllowedCount);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.baz_postgres %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                "LIMIT %d OFFSET %d", whereClause, orderBy, pageSize, start);
    }

    @Override
    protected String getSelectAndLockSql(String databaseProductName) {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.baz_postgres " +
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
