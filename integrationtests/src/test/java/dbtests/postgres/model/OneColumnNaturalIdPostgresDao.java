package dbtests.postgres.model;

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
public class OneColumnNaturalIdPostgresDao extends Dao<OneColumnNaturalIdPostgresEntity, String> {

    public static final Column.StringColumn<OneColumnNaturalIdPostgresEntity> COLUMN_ID = new Column.StringColumn<>("id", "id");

    private static final List<Column<OneColumnNaturalIdPostgresEntity, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_ID);

    private static final String ALL_COLUMNS = " id ";

    private static final RowMapper<OneColumnNaturalIdPostgresEntity> ROW_MAPPER = (rs, i) -> {
        OneColumnNaturalIdPostgresEntity r = new OneColumnNaturalIdPostgresEntity();
        r.setId(rs.getString("id"));
        return r;
    };

    @Autowired
    public OneColumnNaturalIdPostgresDao(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(OneColumnNaturalIdPostgresEntity.class, String.class, false, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(OneColumnNaturalIdPostgresEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.VARCHAR);
        return m;
    }

    @Override
    protected RowMapper<OneColumnNaturalIdPostgresEntity> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM test_schema.one_column_natural_id_postgres " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.one_column_natural_id_postgres " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.one_column_natural_id_postgres " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.one_column_natural_id_postgres (" +
                "id" +
                ") " +
                "VALUES (" +
                ":id" +
                ")";
    }

    @Override
    protected String getUpdateSql(OneColumnNaturalIdPostgresEntity object) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM test_schema.one_column_natural_id_postgres " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM test_schema.one_column_natural_id_postgres";
    }

    @Override
    protected List<Column<OneColumnNaturalIdPostgresEntity, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.one_column_natural_id_postgres %n" +
                "WHERE 1=1 %s %n" +
                "%s " +
                "LIMIT %d", whereClause, orderBy, maxAllowedCount);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM test_schema.one_column_natural_id_postgres %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                "LIMIT %d OFFSET %d", whereClause, orderBy, pageSize, start);
    }

    @Override
    protected String getSelectAndLockSql(String databaseProductName) {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM test_schema.one_column_natural_id_postgres " +
                "WHERE id = :id " +
                "FOR UPDATE";
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
