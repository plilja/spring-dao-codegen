package dbtests.mssql.model;

import dbtests.framework.Column;
import dbtests.framework.CurrentUserProvider;
import dbtests.framework.Dao;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OneColumnGeneratedIdMsSqlDao extends Dao<OneColumnGeneratedIdMsSqlEntity, Integer> {

    public static final Column<OneColumnGeneratedIdMsSqlEntity, Integer> COLUMN_ID = new Column<>("id");

    public static final List<Column<OneColumnGeneratedIdMsSqlEntity, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_ID);

    private static final String ALL_COLUMNS = " id ";

    private static final RowMapper<OneColumnGeneratedIdMsSqlEntity> ROW_MAPPER = (rs, i) -> {
        OneColumnGeneratedIdMsSqlEntity r = new OneColumnGeneratedIdMsSqlEntity();
        r.setId(rs.getInt("id"));
        return r;
    };

    @Autowired
    public OneColumnGeneratedIdMsSqlDao(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(Integer.class, true, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(OneColumnGeneratedIdMsSqlEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.INTEGER);
        return m;
    }

    @Override
    protected RowMapper<OneColumnGeneratedIdMsSqlEntity> getRowMapper() {
        return ROW_MAPPER;
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
                ALL_COLUMNS +
                "FROM dbo.one_column_generated_id_ms_sql " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT TOP %d " +
                ALL_COLUMNS +
                "FROM dbo.one_column_generated_id_ms_sql ", maxSelectCount);
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
    public Column<OneColumnGeneratedIdMsSqlEntity, ?> getColumnByName(String name) {
        for (Column<OneColumnGeneratedIdMsSqlEntity, ?> column : ALL_COLUMNS_LIST) {
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
                "FROM dbo.one_column_generated_id_ms_sql %n" +
                "WHERE 1=1 %s %n" +
                "%s", maxAllowedCount, whereClause, orderBy);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM dbo.one_column_generated_id_ms_sql %n" +
                "WHERE 1=1 %s" +
                "%s %n" +
                "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", whereClause, orderBy, start, pageSize);
    }

    @Override
    protected String getSelectAndLockSql(String databaseProductName) {
        if ("H2".equals(databaseProductName)) {
            return "SELECT " +
                    ALL_COLUMNS +
                    "FROM dbo.one_column_generated_id_ms_sql " +
                    "WHERE id = :id " +
                    "FOR UPDATE";
        } else {
            return "SELECT " +
                    ALL_COLUMNS +
                    "FROM dbo.one_column_generated_id_ms_sql WITH (UPDLOCK) " +
                    "WHERE id = :id";
        }
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
