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
public class MOneColumnGeneratedIdMysqlRepo extends Dao<MOneColumnGeneratedIdMysql, Integer> {

    public static final Column.IntColumn<MOneColumnGeneratedIdMysql> COLUMN_ID = new Column.IntColumn<>("id", "id");

    private static final List<Column<MOneColumnGeneratedIdMysql, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_ID);

    private static final String ALL_COLUMNS = " id ";

    private static final RowMapper<MOneColumnGeneratedIdMysql> ROW_MAPPER = (rs, i) -> {
        MOneColumnGeneratedIdMysql r = new MOneColumnGeneratedIdMysql();
        r.setId(rs.getInt("id"));
        return r;
    };

    @Autowired
    public MOneColumnGeneratedIdMysqlRepo(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(MOneColumnGeneratedIdMysql.class, Integer.class, true, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(MOneColumnGeneratedIdMysql o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.INTEGER);
        return m;
    }

    @Override
    protected RowMapper<MOneColumnGeneratedIdMysql> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM ONE_COLUMN_GENERATED_ID_MYSQL " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM ONE_COLUMN_GENERATED_ID_MYSQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM ONE_COLUMN_GENERATED_ID_MYSQL " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO ONE_COLUMN_GENERATED_ID_MYSQL() VALUES()";
    }

    @Override
    protected String getUpdateSql(MOneColumnGeneratedIdMysql object) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM ONE_COLUMN_GENERATED_ID_MYSQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM ONE_COLUMN_GENERATED_ID_MYSQL";
    }

    @Override
    protected List<Column<MOneColumnGeneratedIdMysql, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM ONE_COLUMN_GENERATED_ID_MYSQL %n" +
                "WHERE 1=1 %s %n" +
                "%s " +
                "LIMIT %d", whereClause, orderBy, maxAllowedCount);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM ONE_COLUMN_GENERATED_ID_MYSQL %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                "LIMIT %d OFFSET %d", whereClause, orderBy, pageSize, start);
    }

    @Override
    protected String getSelectAndLockSql(String databaseProductName) {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM ONE_COLUMN_GENERATED_ID_MYSQL " +
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
