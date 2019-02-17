package dbtests.mysql.model;

import dbtests.framework.Column;
import dbtests.framework.CurrentUserProvider;
import dbtests.framework.Dao;
import dbtests.framework.DatabaseException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class MDataTypesMysqlRepo extends Dao<MDataTypesMysql, Long> {

    public static final Column<MDataTypesMysql, Long> COLUMN_ID = new Column<>("id");

    public static final Column<MDataTypesMysql, Long> COLUMN_BIGINT = new Column<>("`bigint`");

    public static final Column<MDataTypesMysql, Boolean> COLUMN_BIT = new Column<>("`bit`");

    public static final Column<MDataTypesMysql, byte[]> COLUMN_BLOB = new Column<>("`blob`");

    public static final Column<MDataTypesMysql, Boolean> COLUMN_BOOL = new Column<>("`bool`");

    public static final Column<MDataTypesMysql, LocalDate> COLUMN_DATE = new Column<>("`date`");

    public static final Column<MDataTypesMysql, LocalDateTime> COLUMN_DATETIME = new Column<>("`datetime`");

    public static final Column<MDataTypesMysql, Long> COLUMN_DECIMAL_EIGHTEEN_ZERO = new Column<>("decimal_eighteen_zero");

    public static final Column<MDataTypesMysql, Integer> COLUMN_DECIMAL_NINE_ZERO = new Column<>("decimal_nine_zero");

    public static final Column<MDataTypesMysql, BigInteger> COLUMN_DECIMAL_NINETEEN_ZERO = new Column<>("decimal_nineteen_zero");

    public static final Column<MDataTypesMysql, BigDecimal> COLUMN_DECIMAL_TEN_TWO = new Column<>("decimal_ten_two");

    public static final Column<MDataTypesMysql, Long> COLUMN_DECIMAL_TEN_ZERO = new Column<>("decimal_ten_zero");

    public static final Column<MDataTypesMysql, Double> COLUMN_DOUBLE = new Column<>("`double`");

    public static final Column<MDataTypesMysql, Float> COLUMN_FLOAT = new Column<>("`float`");

    public static final Column<MDataTypesMysql, Integer> COLUMN_INT = new Column<>("`int`");

    public static final Column<MDataTypesMysql, Integer> COLUMN_INTEGER = new Column<>("`integer`");

    public static final Column<MDataTypesMysql, String> COLUMN_JSON = new Column<>("`json`");

    public static final Column<MDataTypesMysql, Integer> COLUMN_MEDIUMINT = new Column<>("`mediumint`");

    public static final Column<MDataTypesMysql, Integer> COLUMN_SMALLINT = new Column<>("`smallint`");

    public static final Column<MDataTypesMysql, String> COLUMN_TEXT = new Column<>("`text`");

    public static final Column<MDataTypesMysql, LocalTime> COLUMN_TIME = new Column<>("`time`");

    public static final Column<MDataTypesMysql, LocalDateTime> COLUMN_TIMESTAMP = new Column<>("`timestamp`");

    public static final Column<MDataTypesMysql, byte[]> COLUMN_TINYBLOB = new Column<>("`tinyblob`");

    public static final Column<MDataTypesMysql, Integer> COLUMN_TINYINT = new Column<>("`tinyint`");

    public static final Column<MDataTypesMysql, String> COLUMN_VARCHAR_10 = new Column<>("varchar_10");

    public static final Column<MDataTypesMysql, byte[]> COLUMN_VARCHAR_BINARY_10 = new Column<>("varchar_binary_10");

    public static final Column<MDataTypesMysql, Integer> COLUMN_YEAR = new Column<>("`year`");

    public static final List<Column<MDataTypesMysql, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_ID,
            COLUMN_BIGINT,
            COLUMN_BIT,
            COLUMN_BLOB,
            COLUMN_BOOL,
            COLUMN_DATE,
            COLUMN_DATETIME,
            COLUMN_DECIMAL_EIGHTEEN_ZERO,
            COLUMN_DECIMAL_NINE_ZERO,
            COLUMN_DECIMAL_NINETEEN_ZERO,
            COLUMN_DECIMAL_TEN_TWO,
            COLUMN_DECIMAL_TEN_ZERO,
            COLUMN_DOUBLE,
            COLUMN_FLOAT,
            COLUMN_INT,
            COLUMN_INTEGER,
            COLUMN_JSON,
            COLUMN_MEDIUMINT,
            COLUMN_SMALLINT,
            COLUMN_TEXT,
            COLUMN_TIME,
            COLUMN_TIMESTAMP,
            COLUMN_TINYBLOB,
            COLUMN_TINYINT,
            COLUMN_VARCHAR_10,
            COLUMN_VARCHAR_BINARY_10,
            COLUMN_YEAR);

    private static final String ALL_COLUMNS = " id, `bigint`, `bit`, `blob`, `bool`, " +
            " `date`, `datetime`, decimal_eighteen_zero, decimal_nine_zero, decimal_nineteen_zero, " +
            " decimal_ten_two, decimal_ten_zero, `double`, `float`, `int`, " +
            " `integer`, `json`, `mediumint`, `smallint`, `text`, " +
            " `time`, `timestamp`, `tinyblob`, `tinyint`, varchar_10, " +
            " varchar_binary_10, `year` ";

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

    @Autowired
    public MDataTypesMysqlRepo(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(Long.class, true, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(MDataTypesMysql o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.BIGINT);
        m.addValue("bigint", o.getBigint(), Types.BIGINT);
        m.addValue("bit", o.getBit(), Types.BIT);
        m.addValue("blob", o.getBlob(), Types.LONGVARBINARY);
        m.addValue("bool", o.getBool(), Types.BIT);
        m.addValue("date", o.getDate(), Types.DATE);
        m.addValue("datetime", o.getDatetime(), Types.TIMESTAMP);
        m.addValue("decimal_eighteen_zero", o.getDecimalEighteenZero(), Types.BIGINT);
        m.addValue("decimal_nine_zero", o.getDecimalNineZero(), Types.INTEGER);
        m.addValue("decimal_nineteen_zero", o.getDecimalNineteenZero(), Types.NUMERIC);
        m.addValue("decimal_ten_two", o.getDecimalTenTwo(), Types.NUMERIC);
        m.addValue("decimal_ten_zero", o.getDecimalTenZero(), Types.BIGINT);
        m.addValue("double", o.getDoublE(), Types.DOUBLE);
        m.addValue("float", o.getFloaT(), Types.REAL);
        m.addValue("int", o.getInT(), Types.INTEGER);
        m.addValue("integer", o.getInteger(), Types.INTEGER);
        m.addValue("json", o.getJson(), Types.CHAR);
        m.addValue("mediumint", o.getMediumint(), Types.INTEGER);
        m.addValue("smallint", o.getSmallint(), Types.SMALLINT);
        m.addValue("text", o.getText(), Types.LONGVARCHAR);
        m.addValue("time", o.getTime(), Types.TIME);
        m.addValue("timestamp", o.getTimestamp(), Types.TIMESTAMP);
        m.addValue("tinyblob", o.getTinyblob(), Types.LONGVARBINARY);
        m.addValue("tinyint", o.getTinyint(), Types.TINYINT);
        m.addValue("varchar_10", o.getVarchar10(), Types.VARCHAR);
        m.addValue("varchar_binary_10", o.getVarcharBinary10(), Types.VARBINARY);
        m.addValue("year", o.getYear(), Types.INTEGER);
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
    public Column<MDataTypesMysql, ?> getColumnByName(String name) {
        for (Column<MDataTypesMysql, ?> column : ALL_COLUMNS_LIST) {
            if (column.getName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM DATA_TYPES_MYSQL %n" +
                "WHERE 1=1 %s %n" +
                "%s " +
                "LIMIT %d", whereClause, orderBy, maxAllowedCount);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM DATA_TYPES_MYSQL %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                "LIMIT %d OFFSET %d", whereClause, orderBy, pageSize, start);
    }

    @Override
    protected String getSelectAndLockSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM DATA_TYPES_MYSQL " +
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
