package dbtests.h2.model;

import dbtests.framework.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BazH2Repo extends Dao<BazH2, Integer> {

    private static final RowMapper<BazH2> ROW_MAPPER = (rs, i) -> {
        BazH2 r = new BazH2();
        r.setBazId(rs.getObject("baz_id") != null ? rs.getInt("baz_id") : null);
        r.setBazName(rs.getString("baz_name"));
        r.setChangedAt(rs.getObject("changed_at") != null ? rs.getTimestamp("changed_at").toLocalDateTime() : null);
        r.setCreatedAt(rs.getObject("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        r.setVersion(rs.getObject("version") != null ? rs.getInt("version") : null);
        return r;
    };
    private static final String ALL_COLUMNS = " baz_id, baz_name, changed_at, created_at, version ";

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
                "created_at, " +
                "version" +
                ") " +
                "VALUES (" +
                ":baz_name, " +
                ":changed_at, " +
                ":created_at, " +
                ":version" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE test_schema.baz_h2 SET " +
                "baz_name = :baz_name, " +
                "changed_at = :changed_at, " +
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
    protected String getPrimaryKeyColumnName() {
        return "baz_id";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
