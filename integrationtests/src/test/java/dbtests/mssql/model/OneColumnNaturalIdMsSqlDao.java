package dbtests.mssql.model;

import dbtests.framework.Dao;
import dbtests.framework.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OneColumnNaturalIdMsSqlDao extends Dao<OneColumnNaturalIdMsSqlEntity, String> {

    private static final RowMapper<OneColumnNaturalIdMsSqlEntity> ROW_MAPPER = (rs, i) -> {
        OneColumnNaturalIdMsSqlEntity r = new OneColumnNaturalIdMsSqlEntity();
        r.setId(rs.getString("id"));
        return r;
    };
    private static final String ALL_COLUMNS = " id ";

    @Autowired
    public OneColumnNaturalIdMsSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(String.class, false, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(OneColumnNaturalIdMsSqlEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        return m;
    }

    @Override
    protected RowMapper<OneColumnNaturalIdMsSqlEntity> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM dbo.ONE_COLUMN_NATURAL_ID_MS_SQL " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM dbo.ONE_COLUMN_NATURAL_ID_MS_SQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT TOP %d " +
                ALL_COLUMNS +
                "FROM dbo.ONE_COLUMN_NATURAL_ID_MS_SQL ", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM dbo.ONE_COLUMN_NATURAL_ID_MS_SQL %n" +
                "ORDER BY id %n" +
                "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", start, pageSize);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO dbo.ONE_COLUMN_NATURAL_ID_MS_SQL (" +
                "id" +
                ") " +
                "VALUES (" +
                ":id" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM dbo.ONE_COLUMN_NATURAL_ID_MS_SQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM dbo.ONE_COLUMN_NATURAL_ID_MS_SQL";
    }

    @Override
    protected String getLockSql() {
        return "SELECT id FROM dbo.ONE_COLUMN_NATURAL_ID_MS_SQL WITH (UPDLOCK) " +
                "WHERE id = :id";
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
