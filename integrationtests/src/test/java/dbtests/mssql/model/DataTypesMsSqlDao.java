package dbtests.mssql.model;

import dbtests.framework.Column;
import dbtests.framework.CurrentUserProvider;
import dbtests.framework.Dao;
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
public class DataTypesMsSqlDao extends Dao<DataTypesMsSqlEntity, Long> {

    public static final Column<DataTypesMsSqlEntity, Long> COLUMN_ID = new Column.LongColumn<>("id", "id");

    public static final Column<DataTypesMsSqlEntity, byte[]> COLUMN_BINARY10 = new Column<>("binary10", "binary10", byte[].class);

    public static final Column<DataTypesMsSqlEntity, Boolean> COLUMN_BIT = new Column.BooleanColumn<>("bit", "bit");

    public static final Column<DataTypesMsSqlEntity, String> COLUMN_CHAR = new Column.StringColumn<>("char", "chaR");

    public static final Column<DataTypesMsSqlEntity, String> COLUMN_CHAR10 = new Column.StringColumn<>("char10", "char10");

    public static final Column<DataTypesMsSqlEntity, LocalDate> COLUMN_DATE = new Column.DateColumn<>("date", "date");

    public static final Column<DataTypesMsSqlEntity, LocalDateTime> COLUMN_DATETIME = new Column.DateTimeColumn<>("datetime", "datetime");

    public static final Column<DataTypesMsSqlEntity, LocalDateTime> COLUMN_DATETIME2 = new Column.DateTimeColumn<>("datetime2", "datetime2");

    public static final Column<DataTypesMsSqlEntity, Long> COLUMN_DECIMAL_EIGHTEEN_ZERO = new Column.LongColumn<>("decimal_eighteen_zero", "decimalEighteenZero");

    public static final Column<DataTypesMsSqlEntity, Integer> COLUMN_DECIMAL_NINE_ZERO = new Column.IntColumn<>("decimal_nine_zero", "decimalNineZero");

    public static final Column<DataTypesMsSqlEntity, BigInteger> COLUMN_DECIMAL_NINETEEN_ZERO = new Column<>("decimal_nineteen_zero", "decimalNineteenZero", BigInteger.class);

    public static final Column<DataTypesMsSqlEntity, BigDecimal> COLUMN_DECIMAL_TEN_TWO = new Column.BigDecimalColumn<>("decimal_ten_two", "decimalTenTwo");

    public static final Column<DataTypesMsSqlEntity, Long> COLUMN_DECIMAL_TEN_ZERO = new Column.LongColumn<>("decimal_ten_zero", "decimalTenZero");

    public static final Column<DataTypesMsSqlEntity, Float> COLUMN_FLOAT = new Column<>("float", "floaT", Float.class);

    public static final Column<DataTypesMsSqlEntity, Integer> COLUMN_INT = new Column.IntColumn<>("int", "inT");

    public static final Column<DataTypesMsSqlEntity, BigDecimal> COLUMN_MONEY = new Column.BigDecimalColumn<>("money", "money");

    public static final Column<DataTypesMsSqlEntity, String> COLUMN_NCHAR10 = new Column.StringColumn<>("nchar10", "nchar10");

    public static final Column<DataTypesMsSqlEntity, String> COLUMN_NTEXT = new Column.StringColumn<>("ntext", "ntext");

    public static final Column<DataTypesMsSqlEntity, String> COLUMN_NVARCHAR10 = new Column.StringColumn<>("nvarchar10", "nvarchar10");

    public static final Column<DataTypesMsSqlEntity, Float> COLUMN_REAL = new Column<>("real", "real", Float.class);

    public static final Column<DataTypesMsSqlEntity, Integer> COLUMN_SMALLINT = new Column.IntColumn<>("smallint", "smallint");

    public static final Column<DataTypesMsSqlEntity, BigDecimal> COLUMN_SMALLMONEY = new Column.BigDecimalColumn<>("smallmoney", "smallmoney");

    public static final Column<DataTypesMsSqlEntity, String> COLUMN_TEXT = new Column.StringColumn<>("text", "text");

    public static final Column<DataTypesMsSqlEntity, LocalTime> COLUMN_TIME = new Column<>("time", "time", LocalTime.class);

    public static final Column<DataTypesMsSqlEntity, Integer> COLUMN_TINYINT = new Column.IntColumn<>("tinyint", "tinyint");

    public static final Column<DataTypesMsSqlEntity, byte[]> COLUMN_VARBINARY10 = new Column<>("varbinary10", "varbinary10", byte[].class);

    public static final Column<DataTypesMsSqlEntity, String> COLUMN_VARCHAR10 = new Column.StringColumn<>("varchar10", "varchar10");

    public static final Column<DataTypesMsSqlEntity, String> COLUMN_XML = new Column.StringColumn<>("xml", "xml");

    public static final List<Column<DataTypesMsSqlEntity, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_ID,
            COLUMN_BINARY10,
            COLUMN_BIT,
            COLUMN_CHAR,
            COLUMN_CHAR10,
            COLUMN_DATE,
            COLUMN_DATETIME,
            COLUMN_DATETIME2,
            COLUMN_DECIMAL_EIGHTEEN_ZERO,
            COLUMN_DECIMAL_NINE_ZERO,
            COLUMN_DECIMAL_NINETEEN_ZERO,
            COLUMN_DECIMAL_TEN_TWO,
            COLUMN_DECIMAL_TEN_ZERO,
            COLUMN_FLOAT,
            COLUMN_INT,
            COLUMN_MONEY,
            COLUMN_NCHAR10,
            COLUMN_NTEXT,
            COLUMN_NVARCHAR10,
            COLUMN_REAL,
            COLUMN_SMALLINT,
            COLUMN_SMALLMONEY,
            COLUMN_TEXT,
            COLUMN_TIME,
            COLUMN_TINYINT,
            COLUMN_VARBINARY10,
            COLUMN_VARCHAR10,
            COLUMN_XML);

    private static final String ALL_COLUMNS = " id, binary10, bit, char, char10, " +
            " date, datetime, datetime2, decimal_eighteen_zero, decimal_nine_zero, " +
            " decimal_nineteen_zero, decimal_ten_two, decimal_ten_zero, float, int, " +
            " money, nchar10, ntext, nvarchar10, real, " +
            " smallint, smallmoney, text, time, tinyint, " +
            " varbinary10, varchar10, xml ";

    private static final RowMapper<DataTypesMsSqlEntity> ROW_MAPPER = (rs, i) -> {
        DataTypesMsSqlEntity r = new DataTypesMsSqlEntity();
        r.setId(rs.getLong("id"));
        r.setBinary10((byte[]) rs.getObject("binary10"));
        r.setBit(rs.getObject("bit") != null ? rs.getBoolean("bit") : null);
        r.setChaR(rs.getString("char"));
        r.setChar10(rs.getString("char10"));
        r.setDate(rs.getObject("date") != null ? rs.getDate("date").toLocalDate() : null);
        r.setDatetime(rs.getObject("datetime") != null ? rs.getTimestamp("datetime").toLocalDateTime() : null);
        r.setDatetime2(rs.getObject("datetime2") != null ? rs.getTimestamp("datetime2").toLocalDateTime() : null);
        r.setDecimalEighteenZero(rs.getObject("decimal_eighteen_zero") != null ? rs.getLong("decimal_eighteen_zero") : null);
        r.setDecimalNineZero(rs.getObject("decimal_nine_zero") != null ? rs.getInt("decimal_nine_zero") : null);
        r.setDecimalNineteenZero(rs.getObject("decimal_nineteen_zero") != null ? rs.getBigDecimal("decimal_nineteen_zero").toBigInteger() : null);
        r.setDecimalTenTwo(rs.getBigDecimal("decimal_ten_two"));
        r.setDecimalTenZero(rs.getObject("decimal_ten_zero") != null ? rs.getLong("decimal_ten_zero") : null);
        r.setFloaT(rs.getObject("float") != null ? rs.getFloat("float") : null);
        r.setInT(rs.getObject("int") != null ? rs.getInt("int") : null);
        r.setMoney(rs.getBigDecimal("money"));
        r.setNchar10(rs.getString("nchar10"));
        r.setNtext(rs.getString("ntext"));
        r.setNvarchar10(rs.getString("nvarchar10"));
        r.setReal(rs.getObject("real") != null ? rs.getFloat("real") : null);
        r.setSmallint(rs.getObject("smallint") != null ? rs.getInt("smallint") : null);
        r.setSmallmoney(rs.getBigDecimal("smallmoney"));
        r.setText(rs.getString("text"));
        r.setTime(rs.getObject("time") != null ? rs.getTime("time").toLocalTime() : null);
        r.setTinyint(rs.getObject("tinyint") != null ? rs.getInt("tinyint") : null);
        r.setVarbinary10((byte[]) rs.getObject("varbinary10"));
        r.setVarchar10(rs.getString("varchar10"));
        r.setXml(rs.getString("xml"));
        return r;
    };

    @Autowired
    public DataTypesMsSqlDao(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(Long.class, true, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(DataTypesMsSqlEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.BIGINT);
        m.addValue("binary10", o.getBinary10(), Types.BINARY);
        m.addValue("bit", o.getBit(), Types.BIT);
        m.addValue("char", o.getChaR(), Types.CHAR);
        m.addValue("char10", o.getChar10(), Types.CHAR);
        m.addValue("date", o.getDate(), Types.DATE);
        m.addValue("datetime", o.getDatetime(), Types.TIMESTAMP);
        m.addValue("datetime2", o.getDatetime2(), Types.TIMESTAMP);
        m.addValue("decimal_eighteen_zero", o.getDecimalEighteenZero(), Types.BIGINT);
        m.addValue("decimal_nine_zero", o.getDecimalNineZero(), Types.INTEGER);
        m.addValue("decimal_nineteen_zero", o.getDecimalNineteenZero(), Types.NUMERIC);
        m.addValue("decimal_ten_two", o.getDecimalTenTwo(), Types.NUMERIC);
        m.addValue("decimal_ten_zero", o.getDecimalTenZero(), Types.BIGINT);
        m.addValue("float", o.getFloaT(), Types.REAL);
        m.addValue("int", o.getInT(), Types.INTEGER);
        m.addValue("money", o.getMoney(), Types.NUMERIC);
        m.addValue("nchar10", o.getNchar10(), Types.NCHAR);
        m.addValue("ntext", o.getNtext(), Types.LONGNVARCHAR);
        m.addValue("nvarchar10", o.getNvarchar10(), Types.NVARCHAR);
        m.addValue("real", o.getReal(), Types.REAL);
        m.addValue("smallint", o.getSmallint(), Types.SMALLINT);
        m.addValue("smallmoney", o.getSmallmoney(), Types.NUMERIC);
        m.addValue("text", o.getText(), Types.LONGVARCHAR);
        m.addValue("time", o.getTime(), Types.TIME);
        m.addValue("tinyint", o.getTinyint(), Types.TINYINT);
        m.addValue("varbinary10", o.getVarbinary10(), Types.VARBINARY);
        m.addValue("varchar10", o.getVarchar10(), Types.VARCHAR);
        m.addValue("xml", o.getXml(), Types.LONGNVARCHAR);
        return m;
    }

    @Override
    protected RowMapper<DataTypesMsSqlEntity> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM dbo.DATA_TYPES_MS_SQL " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM dbo.DATA_TYPES_MS_SQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT TOP %d " +
                ALL_COLUMNS +
                "FROM dbo.DATA_TYPES_MS_SQL ", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO dbo.DATA_TYPES_MS_SQL (" +
                "binary10, " +
                "bit, " +
                "char, " +
                "char10, " +
                "date, " +
                "datetime, " +
                "datetime2, " +
                "decimal_eighteen_zero, " +
                "decimal_nine_zero, " +
                "decimal_nineteen_zero, " +
                "decimal_ten_two, " +
                "decimal_ten_zero, " +
                "float, " +
                "int, " +
                "money, " +
                "nchar10, " +
                "ntext, " +
                "nvarchar10, " +
                "real, " +
                "smallint, " +
                "smallmoney, " +
                "text, " +
                "time, " +
                "tinyint, " +
                "varbinary10, " +
                "varchar10, " +
                "xml" +
                ") " +
                "VALUES (" +
                ":binary10, " +
                ":bit, " +
                ":char, " +
                ":char10, " +
                ":date, " +
                ":datetime, " +
                ":datetime2, " +
                ":decimal_eighteen_zero, " +
                ":decimal_nine_zero, " +
                ":decimal_nineteen_zero, " +
                ":decimal_ten_two, " +
                ":decimal_ten_zero, " +
                ":float, " +
                ":int, " +
                ":money, " +
                ":nchar10, " +
                ":ntext, " +
                ":nvarchar10, " +
                ":real, " +
                ":smallint, " +
                ":smallmoney, " +
                ":text, " +
                ":time, " +
                ":tinyint, " +
                ":varbinary10, " +
                ":varchar10, " +
                ":xml" +
                ")";
    }

    @Override
    protected String getUpdateSql(DataTypesMsSqlEntity object) {
        return "UPDATE dbo.DATA_TYPES_MS_SQL SET " +
                "binary10 = :binary10, " +
                "bit = :bit, " +
                "char = :char, " +
                "char10 = :char10, " +
                "date = :date, " +
                "datetime = :datetime, " +
                "datetime2 = :datetime2, " +
                "decimal_eighteen_zero = :decimal_eighteen_zero, " +
                "decimal_nine_zero = :decimal_nine_zero, " +
                "decimal_nineteen_zero = :decimal_nineteen_zero, " +
                "decimal_ten_two = :decimal_ten_two, " +
                "decimal_ten_zero = :decimal_ten_zero, " +
                "float = :float, " +
                "int = :int, " +
                "money = :money, " +
                "nchar10 = :nchar10, " +
                "ntext = :ntext, " +
                "nvarchar10 = :nvarchar10, " +
                "real = :real, " +
                "smallint = :smallint, " +
                "smallmoney = :smallmoney, " +
                "text = :text, " +
                "time = :time, " +
                "tinyint = :tinyint, " +
                "varbinary10 = :varbinary10, " +
                "varchar10 = :varchar10, " +
                "xml = :xml " +
                "WHERE id = :id";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM dbo.DATA_TYPES_MS_SQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM dbo.DATA_TYPES_MS_SQL";
    }

    @Override
    protected List<Column<DataTypesMsSqlEntity, ?>> getColumnsList() {
        return ALL_COLUMNS_LIST;
    }

    @Override
    protected String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy) {
        return String.format("SELECT TOP %d %n" +
                ALL_COLUMNS +
                "FROM dbo.DATA_TYPES_MS_SQL %n" +
                "WHERE 1=1 %s %n" +
                "%s", maxAllowedCount, whereClause, orderBy);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM dbo.DATA_TYPES_MS_SQL %n" +
                "WHERE 1=1 %s" +
                "%s %n" +
                "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", whereClause, orderBy, start, pageSize);
    }

    @Override
    protected String getSelectAndLockSql(String databaseProductName) {
        if ("H2".equals(databaseProductName)) {
            return "SELECT " +
                    ALL_COLUMNS +
                    "FROM dbo.DATA_TYPES_MS_SQL " +
                    "WHERE id = :id " +
                    "FOR UPDATE";
        } else {
            return "SELECT " +
                    ALL_COLUMNS +
                    "FROM dbo.DATA_TYPES_MS_SQL WITH (UPDLOCK) " +
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
