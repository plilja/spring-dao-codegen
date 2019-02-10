package dbtests.oracle.model;

import dbtests.framework.Dao;
import dbtests.framework.DatabaseException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class DataTypesOracleRepository extends Dao<DataTypesOracle, String> {

    private static final RowMapper<DataTypesOracle> ROW_MAPPER = (rs, i) -> {
        try {
            DataTypesOracle r = new DataTypesOracle();
            r.setId(rs.getString("ID"));
            r.setBinaryDouble(rs.getDouble("BINARY_DOUBLE"));
            r.setBinaryFloat(rs.getFloat("BINARY_FLOAT"));
            r.setBlob(rs.getBlob("BLOB").getBinaryStream().readAllBytes());
            r.setChar1(rs.getString("CHAR1"));
            r.setChar10(rs.getString("CHAR10"));
            r.setClob(rs.getClob("CLOB").getSubString(1, (int) rs.getClob("CLOB").length()));
            r.setDate(rs.getDate("DATE").toLocalDate());
            r.setNlob(rs.getNClob("NLOB").getSubString(1, (int) rs.getClob("NLOB").length()));
            r.setNumberEighteenZero(rs.getLong("NUMBER_EIGHTEEN_ZERO"));
            r.setNumberNineZero(rs.getInt("NUMBER_NINE_ZERO"));
            r.setNumberNineteenZero(rs.getBigDecimal("NUMBER_NINETEEN_ZERO").toBigInteger());
            r.setNumberTenTwo(rs.getBigDecimal("NUMBER_TEN_TWO"));
            r.setNumberTenZero(rs.getLong("NUMBER_TEN_ZERO"));
            r.setTimestamp(rs.getTimestamp("TIMESTAMP").toLocalDateTime());
            r.setVarchar(rs.getString("VARCHAR"));
            r.setVarchar2(rs.getString("VARCHAR2"));
            return r;
        } catch (IOException ex) {
            throw new DatabaseException("Caught exception while reading row", ex);
        }
    };
    private static final String ALL_COLUMNS = " ID, BINARY_DOUBLE, BINARY_FLOAT, BLOB, CHAR1, " +
            " CHAR10, CLOB, \"DATE\", NLOB, NUMBER_EIGHTEEN_ZERO, " +
            " NUMBER_NINE_ZERO, NUMBER_NINETEEN_ZERO, NUMBER_TEN_TWO, NUMBER_TEN_ZERO, TIMESTAMP, " +
            " \"VARCHAR\", \"VARCHAR2\" ";

    @Autowired
    public DataTypesOracleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(String.class, false, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(DataTypesOracle o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("ID", o.getId());
        m.addValue("BINARY_DOUBLE", o.getBinaryDouble(), Types.DOUBLE);
        m.addValue("BINARY_FLOAT", o.getBinaryFloat(), Types.FLOAT);
        m.addValue("BLOB", o.getBlob());
        m.addValue("CHAR1", o.getChar1());
        m.addValue("CHAR10", o.getChar10());
        m.addValue("CLOB", o.getClob());
        m.addValue("DATE", o.getDate(), Types.DATE);
        m.addValue("NLOB", o.getNlob());
        m.addValue("NUMBER_EIGHTEEN_ZERO", o.getNumberEighteenZero());
        m.addValue("NUMBER_NINE_ZERO", o.getNumberNineZero());
        m.addValue("NUMBER_NINETEEN_ZERO", o.getNumberNineteenZero());
        m.addValue("NUMBER_TEN_TWO", o.getNumberTenTwo(), Types.NUMERIC);
        m.addValue("NUMBER_TEN_ZERO", o.getNumberTenZero());
        m.addValue("TIMESTAMP", o.getTimestamp());
        m.addValue("VARCHAR", o.getVarchar());
        m.addValue("VARCHAR2", o.getVarchar2());
        return m;
    }

    @Override
    protected RowMapper<DataTypesOracle> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM DOCKER.DATA_TYPES_ORACLE " +
                "WHERE ID = :ID";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM DOCKER.DATA_TYPES_ORACLE " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM DOCKER.DATA_TYPES_ORACLE " +
                "WHERE ROWNUM <= %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT * FROM (%n" +
                "SELECT rownum tmp_rownum_, a.* %n" +
                "FROM (SELECT %n" +
                ALL_COLUMNS +
                "FROM DOCKER.DATA_TYPES_ORACLE %n" +
                "ORDER BY ID %n" +
                ") a %n" +
                "WHERE rownum < %d + %d %n" +
                ")%n" +
                "WHERE tmp_rownum_ >= %d", start + 1, pageSize, start + 1);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO DOCKER.DATA_TYPES_ORACLE (" +
                "ID, " +
                "BINARY_DOUBLE, " +
                "BINARY_FLOAT, " +
                "BLOB, " +
                "CHAR1, " +
                "CHAR10, " +
                "CLOB, " +
                "\"DATE\", " +
                "NLOB, " +
                "NUMBER_EIGHTEEN_ZERO, " +
                "NUMBER_NINE_ZERO, " +
                "NUMBER_NINETEEN_ZERO, " +
                "NUMBER_TEN_TWO, " +
                "NUMBER_TEN_ZERO, " +
                "TIMESTAMP, " +
                "\"VARCHAR\", " +
                "\"VARCHAR2\"" +
                ") " +
                "VALUES (" +
                ":ID, " +
                ":BINARY_DOUBLE, " +
                ":BINARY_FLOAT, " +
                ":BLOB, " +
                ":CHAR1, " +
                ":CHAR10, " +
                ":CLOB, " +
                ":DATE, " +
                ":NLOB, " +
                ":NUMBER_EIGHTEEN_ZERO, " +
                ":NUMBER_NINE_ZERO, " +
                ":NUMBER_NINETEEN_ZERO, " +
                ":NUMBER_TEN_TWO, " +
                ":NUMBER_TEN_ZERO, " +
                ":TIMESTAMP, " +
                ":VARCHAR, " +
                ":VARCHAR2" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE DOCKER.DATA_TYPES_ORACLE SET " +
                "BINARY_DOUBLE = :BINARY_DOUBLE, " +
                "BINARY_FLOAT = :BINARY_FLOAT, " +
                "BLOB = :BLOB, " +
                "CHAR1 = :CHAR1, " +
                "CHAR10 = :CHAR10, " +
                "CLOB = :CLOB, " +
                "DATE = :DATE, " +
                "NLOB = :NLOB, " +
                "NUMBER_EIGHTEEN_ZERO = :NUMBER_EIGHTEEN_ZERO, " +
                "NUMBER_NINE_ZERO = :NUMBER_NINE_ZERO, " +
                "NUMBER_NINETEEN_ZERO = :NUMBER_NINETEEN_ZERO, " +
                "NUMBER_TEN_TWO = :NUMBER_TEN_TWO, " +
                "NUMBER_TEN_ZERO = :NUMBER_TEN_ZERO, " +
                "TIMESTAMP = :TIMESTAMP, " +
                "VARCHAR = :VARCHAR, " +
                "VARCHAR2 = :VARCHAR2 " +
                "WHERE ID = :ID";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM DOCKER.DATA_TYPES_ORACLE " +
                "WHERE ID IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM DOCKER.DATA_TYPES_ORACLE";
    }

    @Override
    protected String getLockSql() {
        return "SELECT ID FROM DOCKER.DATA_TYPES_ORACLE " +
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
