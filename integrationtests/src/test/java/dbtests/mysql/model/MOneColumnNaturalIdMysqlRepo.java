package dbtests.mysql.model;

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
public class MOneColumnNaturalIdMysqlRepo extends Dao<MOneColumnNaturalIdMysql, String> {

    public static final Column.StringColumn<MOneColumnNaturalIdMysql> COLUMN_ID = new Column.StringColumn<>("id", "id");

    private static final List<Column<MOneColumnNaturalIdMysql, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_ID);

    private static final String ALL_COLUMNS = " id ";

    private static final RowMapper<MOneColumnNaturalIdMysql> ROW_MAPPER = (rs, i) -> {
        MOneColumnNaturalIdMysql r = new MOneColumnNaturalIdMysql();
        r.setId(rs.getString("id"));
        return r;
    };

    @Autowired
    public MOneColumnNaturalIdMysqlRepo(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(MOneColumnNaturalIdMysql.class, String.class, false, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(MOneColumnNaturalIdMysql o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.VARCHAR);
        return m;
    }

    @Override
    protected RowMapper<MOneColumnNaturalIdMysql> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM ONE_COLUMN_NATURAL_ID_MYSQL " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM ONE_COLUMN_NATURAL_ID_MYSQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM ONE_COLUMN_NATURAL_ID_MYSQL " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO ONE_COLUMN_NATURAL_ID_MYSQL (" +
                "id" +
                ") " +
                "VALUES (" +
                ":id" +
                ")";
    }

    @Override
    protected String getUpdateSql(MOneColumnNaturalIdMysql object) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM ONE_COLUMN_NATURAL_ID_MYSQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM ONE_COLUMN_NATURAL_ID_MYSQL";
    }

    @Override
    protected List<Column<MOneColumnNaturalIdMysql, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM ONE_COLUMN_NATURAL_ID_MYSQL %n" +
                "WHERE 1=1 %s %n" +
                "%s " +
                "LIMIT %d", whereClause, orderBy, maxAllowedCount);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM ONE_COLUMN_NATURAL_ID_MYSQL %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                "LIMIT %d OFFSET %d", whereClause, orderBy, pageSize, start);
    }

    @Override
    protected String getSelectAndLockSql(String databaseProductName) {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM ONE_COLUMN_NATURAL_ID_MYSQL " +
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
