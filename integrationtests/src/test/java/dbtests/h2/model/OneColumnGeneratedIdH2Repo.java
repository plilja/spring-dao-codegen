package dbtests.h2.model;

import dbtests.framework.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OneColumnGeneratedIdH2Repo extends BaseRepository<OneColumnGeneratedIdH2, Integer> {

    private static final RowMapper<OneColumnGeneratedIdH2> ROW_MAPPER = (rs, i) -> {
        OneColumnGeneratedIdH2 r = new OneColumnGeneratedIdH2();
        r.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
        return r;
    };

    @Autowired
    public OneColumnGeneratedIdH2Repo(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(OneColumnGeneratedIdH2 o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        return m;
    }

    @Override
    protected RowMapper<OneColumnGeneratedIdH2> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM test_schema.one_column_generated_id_h2 " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                "id " +
                "FROM test_schema.one_column_generated_id_h2 " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   id " +
                "FROM test_schema.one_column_generated_id_h2 " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "id %n" +
                "FROM test_schema.one_column_generated_id_h2 %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.one_column_generated_id_h2 DEFAULT VALUES";
    }

    @Override
    protected String getUpdateSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM test_schema.one_column_generated_id_h2 " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM test_schema.one_column_generated_id_h2";
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
