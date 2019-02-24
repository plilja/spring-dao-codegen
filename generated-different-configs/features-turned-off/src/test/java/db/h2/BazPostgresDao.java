package db.h2;

import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BazPostgresDao extends Dao<BazPostgres, Integer> {

    private static final String ALL_COLUMNS = " baz_id, baz_name, changed_at, changed_by, color, " +
            " counter, created_at, created_by ";

    private static final RowMapper<BazPostgres> ROW_MAPPER = (rs, i) -> {
        BazPostgres r = new BazPostgres();
        r.setBazId(rs.getInt("baz_id"));
        r.setBazName(rs.getString("baz_name"));
        r.setChangedAt(rs.getObject("changed_at") != null ? rs.getTimestamp("changed_at").toLocalDateTime() : null);
        r.setChangedBy(rs.getString("changed_by"));
        r.setColor(rs.getString("color"));
        r.setCounter(rs.getObject("counter") != null ? rs.getLong("counter") : null);
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        r.setCreatedBy(rs.getString("created_by"));
        return r;
    };

    @Autowired
    public BazPostgresDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    protected SqlParameterSource getParams(BazPostgres o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("baz_id", o.getId(), Types.INTEGER);
        m.addValue("baz_name", o.getBazName(), Types.VARCHAR);
        m.addValue("changed_at", o.getChangedAt(), Types.TIMESTAMP);
        m.addValue("changed_by", o.getChangedBy(), Types.VARCHAR);
        m.addValue("color", o.getColor(), Types.VARCHAR);
        m.addValue("counter", o.getCounter(), Types.BIGINT);
        m.addValue("created_at", o.getCreatedAt(), Types.TIMESTAMP);
        m.addValue("created_by", o.getCreatedBy(), Types.VARCHAR);
        return m;
    }

    @Override
    protected RowMapper<BazPostgres> getRowMapper() {
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
                "counter = :counter, " +
                "created_at = :created_at, " +
                "created_by = :created_by " +
                "WHERE baz_id = :baz_id";
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
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.baz_postgres %n" +
                "ORDER BY baz_id " +
                "LIMIT %d OFFSET %d", pageSize, start);
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
        return 1000;
    }

}
