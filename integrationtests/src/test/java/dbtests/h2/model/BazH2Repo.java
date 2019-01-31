package dbtests.h2.model;

import dbtests.framework.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BazH2Repo extends BaseRepository<BazH2, Integer> {

    private static final RowMapper<BazH2> ROW_MAPPER = (rs, i) -> {
        BazH2 r = new BazH2();
        r.setBazId(rs.getObject("baz_id") != null ? rs.getInt("baz_id") : null);
        r.setBazName(rs.getString("baz_name"));
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
                "baz_id, " +
                "baz_name " +
                "FROM test_schema.baz_h2 " +
                "WHERE baz_id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   baz_id, " +
                "   baz_name " +
                "FROM test_schema.baz_h2 " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "baz_id, %n" +
                "baz_name %n" +
                "FROM test_schema.baz_h2 %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.baz_h2 (" +
                "   baz_name" +
                ") " +
                "VALUES (" +
                "   :baz_name" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE test_schema.baz_h2 SET " +
                "   baz_name = :baz_name " +
                "WHERE baz_id = :baz_id";
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
