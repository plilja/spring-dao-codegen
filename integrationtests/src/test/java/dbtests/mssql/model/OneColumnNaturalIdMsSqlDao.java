package dbtests.mssql.model;

import dbtests.framework.Column;
import dbtests.framework.CurrentUserProvider;
import dbtests.framework.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

@Repository
public class OneColumnNaturalIdMsSqlDao extends Dao<OneColumnNaturalIdMsSqlEntity, String> {

    public static final Column<OneColumnNaturalIdMsSqlEntity, String> COLUMN_ID = new Column<>("id");

    public static final List<Column<OneColumnNaturalIdMsSqlEntity, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_ID);

    private static final String ALL_COLUMNS = " id ";

    private static final RowMapper<OneColumnNaturalIdMsSqlEntity> ROW_MAPPER = (rs, i) -> {
        OneColumnNaturalIdMsSqlEntity r = new OneColumnNaturalIdMsSqlEntity();
        r.setId(rs.getString("id"));
        return r;
    };

    @Autowired
    public OneColumnNaturalIdMsSqlDao(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(String.class, false, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(OneColumnNaturalIdMsSqlEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.VARCHAR);
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
    public Column<OneColumnNaturalIdMsSqlEntity, ?> getColumnByName(String name) {
        for (Column<OneColumnNaturalIdMsSqlEntity, ?> column : ALL_COLUMNS_LIST) {
            if (column.getName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT TOP %d %n" +
                ALL_COLUMNS +
                "FROM dbo.ONE_COLUMN_NATURAL_ID_MS_SQL %n" +
                "WHERE 1=1 %s %n" +
                "%s", maxAllowedCount, whereClause, orderBy);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM dbo.ONE_COLUMN_NATURAL_ID_MS_SQL %n" +
                "WHERE 1=1 %s" +
                "%s %n" +
                "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", whereClause, orderBy, start, pageSize);
    }

    @Override
    protected String getSelectAndLockSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM dbo.ONE_COLUMN_NATURAL_ID_MS_SQL WITH (UPDLOCK) " +
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
