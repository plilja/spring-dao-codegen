package dbtests.oracle.model;

import dbtests.framework.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OneColumnNaturalIdOracleRepository extends Dao<OneColumnNaturalIdOracle, String> {

    private static final RowMapper<OneColumnNaturalIdOracle> ROW_MAPPER = (rs, i) -> {
        OneColumnNaturalIdOracle r = new OneColumnNaturalIdOracle();
        r.setId(rs.getString("ID"));
        return r;
    };
    private static final String ALL_COLUMNS = " ID ";

    @Autowired
    public OneColumnNaturalIdOracleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(String.class, false, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(OneColumnNaturalIdOracle o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("ID", o.getId());
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
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT * FROM (%n" +
                "SELECT rownum tmp_rownum_, a.* %n" +
                "FROM (SELECT %n" +
                ALL_COLUMNS +
                "FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE %n" +
                "ORDER BY ID %n" +
                ") a %n" +
                "WHERE rownum < %d + %d %n" +
                ")%n" +
                "WHERE tmp_rownum_ >= %d", start + 1, pageSize, start + 1);
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
    protected String getUpdateSql() {
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
    protected String getLockSql() {
        return "SELECT ID FROM DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE " +
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
