package dbtests.postgres.model;

import dbtests.framework.Dao;
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
        return r;
    };
    private static final String ALL_COLUMNS = " baz_id, baz_name ";

    @Autowired
    public BazPostgresDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(BazPostgresEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("baz_id", o.getId());
        m.addValue("baz_name", o.getBazName());
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
                "baz_name" +
                ") " +
                "VALUES (" +
                ":baz_name" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE test_schema.baz_postgres SET " +
                "baz_name = :baz_name " +
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
    protected String getPrimaryKeyColumnName() {
        return "baz_id";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
