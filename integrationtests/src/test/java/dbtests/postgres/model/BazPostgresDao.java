package dbtests.postgres.model;

import dbtests.framework.Dao;
import dbtests.framework.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BazPostgresDao extends Dao<BazPostgresEntity, Integer> {

    private static final RowMapper<BazPostgresEntity> ROW_MAPPER = (rs, i) -> {
        BazPostgresEntity r = new BazPostgresEntity();
        r.setBazId(rs.getObject("baz_id") != null ? rs.getInt("baz_id") : null);
        r.setBazName(rs.getString("baz_name"));
        r.setChangedAt(rs.getObject("changed_at") != null ? rs.getTimestamp("changed_at").toLocalDateTime() : null);
        r.setColor(ColorEnumPostgres.fromId(rs.getString("color")));
        r.setCounter(rs.getObject("counter") != null ? rs.getInt("counter") : null);
        r.setCreatedAt(rs.getObject("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        return r;
    };
    private static final String ALL_COLUMNS = " baz_id, baz_name, changed_at, color, counter, " +
            " created_at ";

    @Autowired
    public BazPostgresDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(BazPostgresEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("baz_id", o.getId());
        m.addValue("baz_name", o.getBazName());
        m.addValue("changed_at", o.getChangedAt());
        m.addValue("color", o.getColor() != null ? o.getColor().getId() : null);
        m.addValue("counter", o.getCounter());
        m.addValue("created_at", o.getCreatedAt());
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
                "counter = (counter + 1) % 128 " +
                "WHERE baz_id = :baz_id AND counter = :counter";
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
