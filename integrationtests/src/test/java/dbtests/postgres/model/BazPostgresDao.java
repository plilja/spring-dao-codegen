package dbtests.postgres.model;

import dbtests.framework.Column;
import dbtests.framework.Dao;
import dbtests.framework.DatabaseException;
import java.sql.Types;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BazPostgresDao extends Dao<BazPostgresEntity, Integer> {

    public static final Column<BazPostgresEntity, Integer> COLUMN_BAZ_ID = new Column<>("baz_id");

    public static final Column<BazPostgresEntity, String> COLUMN_BAZ_NAME = new Column<>("baz_name");

    public static final Column<BazPostgresEntity, LocalDateTime> COLUMN_CHANGED_AT = new Column<>("changed_at");

    public static final Column<BazPostgresEntity, ColorEnumPostgres> COLUMN_COLOR = new Column<>("color");

    public static final Column<BazPostgresEntity, Integer> COLUMN_COUNTER = new Column<>("counter");

    public static final Column<BazPostgresEntity, LocalDateTime> COLUMN_CREATED_AT = new Column<>("created_at");

    private static final String ALL_COLUMNS = " baz_id, baz_name, changed_at, color, counter, " +
            " created_at ";

    private static final RowMapper<BazPostgresEntity> ROW_MAPPER = (rs, i) -> {
        BazPostgresEntity r = new BazPostgresEntity();
        r.setBazId(rs.getInt("baz_id"));
        r.setBazName(rs.getString("baz_name"));
        r.setChangedAt(rs.getObject("changed_at") != null ? rs.getTimestamp("changed_at").toLocalDateTime() : null);
        r.setColor(ColorEnumPostgres.fromId(rs.getString("color")));
        r.setCounter(rs.getObject("counter") != null ? rs.getInt("counter") : null);
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return r;
    };

    @Autowired
    public BazPostgresDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    protected SqlParameterSource getParams(BazPostgresEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("baz_id", o.getId(), Types.INTEGER);
        m.addValue("baz_name", o.getBazName(), Types.VARCHAR);
        m.addValue("changed_at", o.getChangedAt(), Types.TIMESTAMP);
        m.addValue("color", o.getColor() != null ? o.getColor().getId() : null, Types.VARCHAR);
        m.addValue("counter", o.getCounter(), Types.BIGINT);
        m.addValue("created_at", o.getCreatedAt(), Types.TIMESTAMP);
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
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.baz_postgres %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.baz_postgres (" +
                "baz_name, " +
                "changed_at, " +
                "color, " +
                "counter, " +
                "created_at" +
                ") " +
                "VALUES (" +
                ":baz_name, " +
                ":changed_at, " +
                ":color, " +
                ":counter, " +
                ":created_at" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE test_schema.baz_postgres SET " +
                "baz_name = :baz_name, " +
                "changed_at = :changed_at, " +
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
    protected String getQuerySql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.baz_postgres " +
                "WHERE %s " +
                "LIMIT %d";
    }

    @Override
    protected String getLockSql() {
        return "SELECT baz_id FROM test_schema.baz_postgres " +
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
