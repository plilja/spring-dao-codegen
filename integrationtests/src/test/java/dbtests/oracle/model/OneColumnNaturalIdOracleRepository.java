package dbtests.oracle.model;

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
public class OneColumnNaturalIdOracleRepository extends Dao<OneColumnNaturalIdOracle, String> {

    public static final Column.StringColumn<OneColumnNaturalIdOracle> COLUMN_ID = new Column.StringColumn<>("ID", "id");

    public static final List<Column<OneColumnNaturalIdOracle, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_ID);

    private static final String ALL_COLUMNS = " ID ";

    private static final RowMapper<OneColumnNaturalIdOracle> ROW_MAPPER = (rs, i) -> {
        OneColumnNaturalIdOracle r = new OneColumnNaturalIdOracle();
        r.setId(rs.getString("ID"));
        return r;
    };

    @Autowired
    public OneColumnNaturalIdOracleRepository(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(String.class, false, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(OneColumnNaturalIdOracle o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("ID", o.getId(), Types.VARCHAR);
        return m;
    }

    @Override
    protected RowMapper<OneColumnNaturalIdOracle> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE " +
                "WHERE ID = :ID";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE " +
                "WHERE ROWNUM <= %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE (" +
                "ID" +
                ") " +
                "VALUES (" +
                ":ID" +
                ")";
    }

    @Override
    protected String getUpdateSql(OneColumnNaturalIdOracle object) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE";
    }

    @Override
    protected List<Column<OneColumnNaturalIdOracle, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE %n" +
                "WHERE ROWNUM <= %d %s %n" +
                "%s", maxAllowedCount, whereClause, orderBy);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT * FROM (%n" +
                "SELECT rownum tmp_rownum_, a.* %n" +
                "FROM (SELECT %n" +
                ALL_COLUMNS +
                "FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                ") a %n" +
                "WHERE rownum < %d + %d %n" +
                ")%n" +
                "WHERE tmp_rownum_ >= %d", whereClause, orderBy, start + 1, pageSize, start + 1);
    }

    @Override
    protected String getSelectAndLockSql(String databaseProductName) {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE " +
                "WHERE ID = :id " +
                "FOR UPDATE";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "ID";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
