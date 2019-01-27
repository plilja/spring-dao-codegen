package dbtests.mssql.model;

import dbtests.framework.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OneColumnGeneratedIdMsSqlRepository extends BaseRepository<OneColumnGeneratedIdMsSqlEntity, Integer> {

    private static final RowMapper<OneColumnGeneratedIdMsSqlEntity> ROW_MAPPER = (rs, i) -> {
        OneColumnGeneratedIdMsSqlEntity r = new OneColumnGeneratedIdMsSqlEntity();
        r.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
        return r;
    };

    @Autowired
    public OneColumnGeneratedIdMsSqlRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, true, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(OneColumnGeneratedIdMsSqlEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        return m;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM dbo.one_column_generated_id_ms_sql " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                "id " +
                "FROM dbo.one_column_generated_id_ms_sql " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT TOP %d " +
                "   id " +
                "FROM dbo.one_column_generated_id_ms_sql ", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "id %n" +
                "FROM dbo.one_column_generated_id_ms_sql %n" +
                "ORDER BY id %n" +
                "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", start, pageSize);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO dbo.one_column_generated_id_ms_sql DEFAULT VALUES";
    }

    @Override
    protected String getUpdateSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM dbo.one_column_generated_id_ms_sql " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM dbo.one_column_generated_id_ms_sql";
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
