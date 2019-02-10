package dbtests.mysql.model;

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
public class MDataTypesMysqlRepo extends Dao<MDataTypesMysql, Long> {

    private static final RowMapper<MDataTypesMysql> ROW_MAPPER = (rs, i) -> {
        try {
            MDataTypesMysql r = new MDataTypesMysql();
            r.setId(rs.getLong("id"));
            r.setBigint(rs.getObject("bigint") != null ? rs.getLong("bigint") : null);
            r.setBit(rs.getObject("bit") != null ? rs.getBoolean("bit") : null);
            r.setBlob(rs.getObject("blob") != null ? rs.getBlob("blob").getBinaryStream().readAllBytes() : null);
            r.setBool(rs.getObject("bool") != null ? rs.getBoolean("bool") : null);
            r.setDate(rs.getObject("date") != null ? rs.getDate("date").toLocalDate() : null);
            r.setDatetime(rs.getObject("datetime") != null ? rs.getTimestamp("datetime").toLocalDateTime() : null);
            r.setDecimalEighteenZero(rs.getObject("decimal_eighteen_zero") != null ? rs.getLong("decimal_eighteen_zero") : null);
            r.setDecimalNineZero(rs.getObject("decimal_nine_zero") != null ? rs.getInt("decimal_nine_zero") : null);
            r.setDecimalNineteenZero(rs.getObject("decimal_nineteen_zero") != null ? rs.getBigDecimal("decimal_nineteen_zero").toBigInteger() : null);
            r.setDecimalTenTwo(rs.getBigDecimal("decimal_ten_two"));
            r.setDecimalTenZero(rs.getObject("decimal_ten_zero") != null ? rs.getLong("decimal_ten_zero") : null);
            r.setDoublE(rs.getObject("double") != null ? rs.getDouble("double") : null);
            r.setFloaT(rs.getObject("float") != null ? rs.getFloat("float") : null);
            r.setInT(rs.getObject("int") != null ? rs.getInt("int") : null);
            r.setInteger(rs.getObject("integer") != null ? rs.getInt("integer") : null);
            r.setJson(rs.getString("json"));
            r.setMediumint(rs.getObject("mediumint") != null ? rs.getInt("mediumint") : null);
            r.setSmallint(rs.getObject("smallint") != null ? rs.getInt("smallint") : null);
            r.setText(rs.getString("text"));
            r.setTime(rs.getObject("time") != null ? rs.getTime("time").toLocalTime() : null);
            r.setTimestamp(rs.getObject("timestamp") != null ? rs.getTimestamp("timestamp").toLocalDateTime() : null);
            r.setTinyblob((byte[]) rs.getObject("tinyblob"));
            r.setTinyint(rs.getObject("tinyint") != null ? rs.getInt("tinyint") : null);
            r.setVarchar10(rs.getString("varchar_10"));
            r.setVarcharBinary10((byte[]) rs.getObject("varchar_binary_10"));
            r.setYear(rs.getObject("year") != null ? rs.getInt("year") : null);
            return r;
        } catch (IOException ex) {
            throw new DatabaseException("Caught exception while reading row", ex);
        }
    };
    private static final String ALL_COLUMNS = " id, `bigint`, `bit`, `blob`, `bool`, " +
            " `date`, `datetime`, decimal_eighteen_zero, decimal_nine_zero, decimal_nineteen_zero, " +
            " decimal_ten_two, decimal_ten_zero, `double`, `float`, `int`, " +
            " `integer`, `json`, `mediumint`, `smallint`, `text`, " +
            " `time`, `timestamp`, `tinyblob`, `tinyint`, varchar_10, " +
            " varchar_binary_10, `year` ";

    @Autowired
    public MDataTypesMysqlRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Long.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(MDataTypesMysql o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        m.addValue("bigint", o.getBigint());
        m.addValue("bit", o.getBit(), Types.BOOLEAN);
        m.addValue("blob", o.getBlob());
        m.addValue("bool", o.getBool(), Types.BOOLEAN);
        m.addValue("date", o.getDate(), Types.DATE);
        m.addValue("datetime", o.getDatetime());
        m.addValue("decimal_eighteen_zero", o.getDecimalEighteenZero());
        m.addValue("decimal_nine_zero", o.getDecimalNineZero());
        m.addValue("decimal_nineteen_zero", o.getDecimalNineteenZero());
        m.addValue("decimal_ten_two", o.getDecimalTenTwo(), Types.NUMERIC);
        m.addValue("decimal_ten_zero", o.getDecimalTenZero());
        m.addValue("double", o.getDoublE(), Types.DOUBLE);
        m.addValue("float", o.getFloaT(), Types.FLOAT);
        m.addValue("int", o.getInT());
        m.addValue("integer", o.getInteger());
        m.addValue("json", o.getJson());
        m.addValue("mediumint", o.getMediumint());
        m.addValue("smallint", o.getSmallint());
        m.addValue("text", o.getText());
        m.addValue("time", o.getTime());
        m.addValue("timestamp", o.getTimestamp());
        m.addValue("tinyblob", o.getTinyblob());
        m.addValue("tinyint", o.getTinyint());
        m.addValue("varchar_10", o.getVarchar10());
        m.addValue("varchar_binary_10", o.getVarcharBinary10());
        m.addValue("year", o.getYear());
        return m;
    }

    @Override
    protected RowMapper<MDataTypesMysql> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM DATA_TYPES_MYSQL " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM DATA_TYPES_MYSQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM DATA_TYPES_MYSQL " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM DATA_TYPES_MYSQL %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO DATA_TYPES_MYSQL (" +
                "`bigint`, " +
                "`bit`, " +
                "`blob`, " +
                "`bool`, " +
                "`date`, " +
                "`datetime`, " +
                "decimal_eighteen_zero, " +
                "decimal_nine_zero, " +
                "decimal_nineteen_zero, " +
                "decimal_ten_two, " +
                "decimal_ten_zero, " +
                "`double`, " +
                "`float`, " +
                "`int`, " +
                "`integer`, " +
                "`json`, " +
                "`mediumint`, " +
                "`smallint`, " +
                "`text`, " +
                "`time`, " +
                "`timestamp`, " +
                "`tinyblob`, " +
                "`tinyint`, " +
                "varchar_10, " +
                "varchar_binary_10, " +
                "`year`" +
                ") " +
                "VALUES (" +
                ":bigint, " +
                ":bit, " +
                ":blob, " +
                ":bool, " +
                ":date, " +
                ":datetime, " +
                ":decimal_eighteen_zero, " +
                ":decimal_nine_zero, " +
                ":decimal_nineteen_zero, " +
                ":decimal_ten_two, " +
                ":decimal_ten_zero, " +
                ":double, " +
                ":float, " +
                ":int, " +
                ":integer, " +
                ":json, " +
                ":mediumint, " +
                ":smallint, " +
                ":text, " +
                ":time, " +
                ":timestamp, " +
                ":tinyblob, " +
                ":tinyint, " +
                ":varchar_10, " +
                ":varchar_binary_10, " +
                ":year" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE DATA_TYPES_MYSQL SET " +
                "bigint = :bigint, " +
                "bit = :bit, " +
                "blob = :blob, " +
                "bool = :bool, " +
                "date = :date, " +
                "datetime = :datetime, " +
                "decimal_eighteen_zero = :decimal_eighteen_zero, " +
                "decimal_nine_zero = :decimal_nine_zero, " +
                "decimal_nineteen_zero = :decimal_nineteen_zero, " +
                "decimal_ten_two = :decimal_ten_two, " +
                "decimal_ten_zero = :decimal_ten_zero, " +
                "double = :double, " +
                "float = :float, " +
                "int = :int, " +
                "integer = :integer, " +
                "json = :json, " +
                "mediumint = :mediumint, " +
                "smallint = :smallint, " +
                "text = :text, " +
                "time = :time, " +
                "timestamp = :timestamp, " +
                "tinyblob = :tinyblob, " +
                "tinyint = :tinyint, " +
                "varchar_10 = :varchar_10, " +
                "varchar_binary_10 = :varchar_binary_10, " +
                "year = :year " +
                "WHERE id = :id";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM DATA_TYPES_MYSQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM DATA_TYPES_MYSQL";
    }

    @Override
    protected String getLockSql() {
        return "SELECT id FROM DATA_TYPES_MYSQL " +
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
