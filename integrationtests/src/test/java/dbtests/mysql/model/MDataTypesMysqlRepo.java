package dbtests.mysql.model;

import dbtests.framework.Column;
import dbtests.framework.CurrentUserProvider;
import dbtests.framework.Dao;
import dbtests.framework.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Types;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Repository
public class MDataTypesMysqlRepo extends Dao<MDataTypesMysql, Long> {

    public static final Column.LongColumn<MDataTypesMysql> COLUMN_ID = new Column.LongColumn<>("id", "id");

    public static final Column.LongColumn<MDataTypesMysql> COLUMN_BIGINT = new Column.LongColumn<>("bigint", "`bigint`", "bigint");

    public static final Column.BooleanColumn<MDataTypesMysql> COLUMN_BIT = new Column.BooleanColumn<>("bit", "`bit`", "bit");

    public static final Column<MDataTypesMysql, byte[]> COLUMN_BLOB = new Column<>("blob", "`blob`", "blob", byte[].class);

    public static final Column.BooleanColumn<MDataTypesMysql> COLUMN_BOOL = new Column.BooleanColumn<>("bool", "`bool`", "bool");

    public static final Column.DateColumn<MDataTypesMysql> COLUMN_DATE = new Column.DateColumn<>("date", "`date`", "date");

    public static final Column.DateTimeColumn<MDataTypesMysql> COLUMN_DATETIME = new Column.DateTimeColumn<>("datetime", "`datetime`", "datetime");

    public static final Column.LongColumn<MDataTypesMysql> COLUMN_DECIMAL_EIGHTEEN_ZERO = new Column.LongColumn<>("decimal_eighteen_zero", "decimalEighteenZero");

    public static final Column.IntColumn<MDataTypesMysql> COLUMN_DECIMAL_NINE_ZERO = new Column.IntColumn<>("decimal_nine_zero", "decimalNineZero");

    public static final Column<MDataTypesMysql, BigInteger> COLUMN_DECIMAL_NINETEEN_ZERO = new Column<>("decimal_nineteen_zero", "decimalNineteenZero", BigInteger.class);

    public static final Column.BigDecimalColumn<MDataTypesMysql> COLUMN_DECIMAL_TEN_TWO = new Column.BigDecimalColumn<>("decimal_ten_two", "decimalTenTwo");

    public static final Column.LongColumn<MDataTypesMysql> COLUMN_DECIMAL_TEN_ZERO = new Column.LongColumn<>("decimal_ten_zero", "decimalTenZero");

    public static final Column.DoubleColumn<MDataTypesMysql> COLUMN_DOUBLE = new Column.DoubleColumn<>("double", "`double`", "doublE");

    public static final Column<MDataTypesMysql, Float> COLUMN_FLOAT = new Column<>("float", "`float`", "floaT", Float.class);

    public static final Column.IntColumn<MDataTypesMysql> COLUMN_INT = new Column.IntColumn<>("int", "`int`", "inT");

    public static final Column.IntColumn<MDataTypesMysql> COLUMN_INTEGER = new Column.IntColumn<>("integer", "`integer`", "integer");

    public static final Column.StringColumn<MDataTypesMysql> COLUMN_JSON = new Column.StringColumn<>("json", "`json`", "json");

    public static final Column.IntColumn<MDataTypesMysql> COLUMN_MEDIUMINT = new Column.IntColumn<>("mediumint", "`mediumint`", "mediumint");

    public static final Column.IntColumn<MDataTypesMysql> COLUMN_SMALLINT = new Column.IntColumn<>("smallint", "`smallint`", "smallint");

    public static final Column.StringColumn<MDataTypesMysql> COLUMN_TEXT = new Column.StringColumn<>("text", "`text`", "text");

    public static final Column<MDataTypesMysql, LocalTime> COLUMN_TIME = new Column<>("time", "`time`", "time", LocalTime.class);

    public static final Column.DateTimeColumn<MDataTypesMysql> COLUMN_TIMESTAMP = new Column.DateTimeColumn<>("timestamp", "`timestamp`", "timestamp");

    public static final Column<MDataTypesMysql, byte[]> COLUMN_TINYBLOB = new Column<>("tinyblob", "`tinyblob`", "tinyblob", byte[].class);

    public static final Column.IntColumn<MDataTypesMysql> COLUMN_TINYINT = new Column.IntColumn<>("tinyint", "`tinyint`", "tinyint");

    public static final Column.StringColumn<MDataTypesMysql> COLUMN_VARCHAR_10 = new Column.StringColumn<>("varchar_10", "varchar10");

    public static final Column<MDataTypesMysql, byte[]> COLUMN_VARCHAR_BINARY_10 = new Column<>("varchar_binary_10", "varcharBinary10", byte[].class);

    public static final Column.IntColumn<MDataTypesMysql> COLUMN_YEAR = new Column.IntColumn<>("year", "`year`", "year");

    private static final List<Column<MDataTypesMysql, ?>> ALL_COLUMNS_LIST = Arrays.asList(
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
        super(MDataTypesMysql.class, Long.class, true, jdbcTemplate, currentUserProvider);
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
    protected String getUpdateSql(MDataTypesMysql object) {
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
    protected List<Column<MDataTypesMysql, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
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
    protected String getSelectAndLockSql(String databaseProductName) {
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
