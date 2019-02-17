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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public class DataTypesPostgresDao extends Dao<DataTypesPostgresEntity, Long> {

    public static final Column<DataTypesPostgresEntity, Long> COLUMN_ID = new Column<>("id");

    public static final Column<DataTypesPostgresEntity, Long> COLUMN_BIGINT = new Column<>("bigint");

    public static final Column<DataTypesPostgresEntity, Boolean> COLUMN_BOOLEAN_B = new Column<>("boolean_b");

    public static final Column<DataTypesPostgresEntity, byte[]> COLUMN_BYTEA = new Column<>("bytea");

    public static final Column<DataTypesPostgresEntity, String> COLUMN_CHAR = new Column<>("char");

    public static final Column<DataTypesPostgresEntity, String> COLUMN_CHAR10 = new Column<>("char10");

    public static final Column<DataTypesPostgresEntity, LocalDate> COLUMN_DATE = new Column<>("date");

    public static final Column<DataTypesPostgresEntity, Long> COLUMN_DECIMAL_EIGHTEEN_ZERO = new Column<>("decimal_eighteen_zero");

    public static final Column<DataTypesPostgresEntity, Integer> COLUMN_DECIMAL_NINE_ZERO = new Column<>("decimal_nine_zero");

    public static final Column<DataTypesPostgresEntity, BigInteger> COLUMN_DECIMAL_NINETEEN_ZERO = new Column<>("decimal_nineteen_zero");

    public static final Column<DataTypesPostgresEntity, BigDecimal> COLUMN_DECIMAL_TEN_TWO = new Column<>("decimal_ten_two");

    public static final Column<DataTypesPostgresEntity, Long> COLUMN_DECIMAL_TEN_ZERO = new Column<>("decimal_ten_zero");

    public static final Column<DataTypesPostgresEntity, Double> COLUMN_DOUBLE = new Column<>("double");

    public static final Column<DataTypesPostgresEntity, Float> COLUMN_FLOAT = new Column<>("float");

    public static final Column<DataTypesPostgresEntity, UUID> COLUMN_GUID = new Column<>("guid");

    public static final Column<DataTypesPostgresEntity, Integer> COLUMN_INTEGER = new Column<>("integer");

    public static final Column<DataTypesPostgresEntity, BigDecimal> COLUMN_NUMERIC_TEN_TWO = new Column<>("numeric_ten_two");

    public static final Column<DataTypesPostgresEntity, Integer> COLUMN_SMALLINT = new Column<>("smallint");

    public static final Column<DataTypesPostgresEntity, String> COLUMN_TEXT = new Column<>("text");

    public static final Column<DataTypesPostgresEntity, LocalDateTime> COLUMN_TIMESTAMP = new Column<>("timestamp");

    public static final Column<DataTypesPostgresEntity, String> COLUMN_VARCHAR10 = new Column<>("varchar10");

    public static final Column<DataTypesPostgresEntity, String> COLUMN_XML = new Column<>("xml");

    public static final List<Column<DataTypesPostgresEntity, ?>> ALL_COLUMNS_LIST = Arrays.asList(
            COLUMN_ID,
            COLUMN_BIGINT,
            COLUMN_BOOLEAN_B,
            COLUMN_BYTEA,
            COLUMN_CHAR,
            COLUMN_CHAR10,
            COLUMN_DATE,
            COLUMN_DECIMAL_EIGHTEEN_ZERO,
            COLUMN_DECIMAL_NINE_ZERO,
            COLUMN_DECIMAL_NINETEEN_ZERO,
            COLUMN_DECIMAL_TEN_TWO,
            COLUMN_DECIMAL_TEN_ZERO,
            COLUMN_DOUBLE,
            COLUMN_FLOAT,
            COLUMN_GUID,
            COLUMN_INTEGER,
            COLUMN_NUMERIC_TEN_TWO,
            COLUMN_SMALLINT,
            COLUMN_TEXT,
            COLUMN_TIMESTAMP,
            COLUMN_VARCHAR10,
            COLUMN_XML);

    private static final String ALL_COLUMNS = " id, bigint, boolean_b, bytea, char, " +
            " char10, date, decimal_eighteen_zero, decimal_nine_zero, decimal_nineteen_zero, " +
            " decimal_ten_two, decimal_ten_zero, double, float, guid, " +
            " integer, numeric_ten_two, smallint, text, timestamp, " +
            " varchar10, xml ";

    private static final RowMapper<DataTypesPostgresEntity> ROW_MAPPER = (rs, i) -> {
        DataTypesPostgresEntity r = new DataTypesPostgresEntity();
        r.setId(rs.getLong("id"));
        r.setBigint(rs.getObject("bigint") != null ? rs.getLong("bigint") : null);
        r.setBooleanB(rs.getObject("boolean_b") != null ? rs.getBoolean("boolean_b") : null);
        r.setBytea((byte[]) rs.getObject("bytea"));
        r.setChaR(rs.getString("char"));
        r.setChar10(rs.getString("char10"));
        r.setDate(rs.getObject("date") != null ? rs.getDate("date").toLocalDate() : null);
        r.setDecimalEighteenZero(rs.getObject("decimal_eighteen_zero") != null ? rs.getLong("decimal_eighteen_zero") : null);
        r.setDecimalNineZero(rs.getObject("decimal_nine_zero") != null ? rs.getInt("decimal_nine_zero") : null);
        r.setDecimalNineteenZero(rs.getObject("decimal_nineteen_zero") != null ? rs.getBigDecimal("decimal_nineteen_zero").toBigInteger() : null);
        r.setDecimalTenTwo(rs.getBigDecimal("decimal_ten_two"));
        r.setDecimalTenZero(rs.getObject("decimal_ten_zero") != null ? rs.getLong("decimal_ten_zero") : null);
        r.setDoublE(rs.getObject("double") != null ? rs.getDouble("double") : null);
        r.setFloaT(rs.getObject("float") != null ? rs.getFloat("float") : null);
        r.setGuid(rs.getObject("guid") != null ? UUID.fromString(rs.getString("guid")) : null);
        r.setInteger(rs.getObject("integer") != null ? rs.getInt("integer") : null);
        r.setNumericTenTwo(rs.getBigDecimal("numeric_ten_two"));
        r.setSmallint(rs.getObject("smallint") != null ? rs.getInt("smallint") : null);
        r.setText(rs.getString("text"));
        r.setTimestamp(rs.getObject("timestamp") != null ? rs.getTimestamp("timestamp").toLocalDateTime() : null);
        r.setVarchar10(rs.getString("varchar10"));
        r.setXml(rs.getString("xml"));
        return r;
    };

    @Autowired
    public DataTypesPostgresDao(NamedParameterJdbcTemplate jdbcTemplate, CurrentUserProvider currentUserProvider) {
        super(Long.class, true, jdbcTemplate, currentUserProvider);
    }

    @Override
    protected SqlParameterSource getParams(DataTypesPostgresEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId(), Types.BIGINT);
        m.addValue("bigint", o.getBigint(), Types.BIGINT);
        m.addValue("boolean_b", o.getBooleanB(), Types.BIT);
        m.addValue("bytea", o.getBytea(), Types.BINARY);
        m.addValue("char", o.getChaR(), Types.CHAR);
        m.addValue("char10", o.getChar10(), Types.CHAR);
        m.addValue("date", o.getDate(), Types.DATE);
        m.addValue("decimal_eighteen_zero", o.getDecimalEighteenZero(), Types.BIGINT);
        m.addValue("decimal_nine_zero", o.getDecimalNineZero(), Types.INTEGER);
        m.addValue("decimal_nineteen_zero", o.getDecimalNineteenZero(), Types.NUMERIC);
        m.addValue("decimal_ten_two", o.getDecimalTenTwo(), Types.NUMERIC);
        m.addValue("decimal_ten_zero", o.getDecimalTenZero(), Types.BIGINT);
        m.addValue("double", o.getDoublE(), Types.DOUBLE);
        m.addValue("float", o.getFloaT(), Types.REAL);
        m.addValue("guid", o.getGuid(), Types.OTHER);
        m.addValue("integer", o.getInteger(), Types.INTEGER);
        m.addValue("numeric_ten_two", o.getNumericTenTwo(), Types.NUMERIC);
        m.addValue("smallint", o.getSmallint(), Types.SMALLINT);
        m.addValue("text", o.getText(), Types.VARCHAR);
        m.addValue("timestamp", o.getTimestamp(), Types.TIMESTAMP);
        m.addValue("varchar10", o.getVarchar10(), Types.VARCHAR);
        m.addValue("xml", o.getXml(), Types.SQLXML);
        return m;
    }

    @Override
    protected RowMapper<DataTypesPostgresEntity> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM public.data_types_postgres " +
                "WHERE id = :id";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM public.data_types_postgres " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                ALL_COLUMNS +
                "FROM public.data_types_postgres " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO public.data_types_postgres (" +
                "bigint, " +
                "boolean_b, " +
                "bytea, " +
                "char, " +
                "char10, " +
                "date, " +
                "decimal_eighteen_zero, " +
                "decimal_nine_zero, " +
                "decimal_nineteen_zero, " +
                "decimal_ten_two, " +
                "decimal_ten_zero, " +
                "double, " +
                "float, " +
                "guid, " +
                "integer, " +
                "numeric_ten_two, " +
                "smallint, " +
                "text, " +
                "timestamp, " +
                "varchar10, " +
                "xml" +
                ") " +
                "VALUES (" +
                ":bigint, " +
                ":boolean_b, " +
                ":bytea, " +
                ":char, " +
                ":char10, " +
                ":date, " +
                ":decimal_eighteen_zero, " +
                ":decimal_nine_zero, " +
                ":decimal_nineteen_zero, " +
                ":decimal_ten_two, " +
                ":decimal_ten_zero, " +
                ":double, " +
                ":float, " +
                ":guid, " +
                ":integer, " +
                ":numeric_ten_two, " +
                ":smallint, " +
                ":text, " +
                ":timestamp, " +
                ":varchar10, " +
                ":xml" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE public.data_types_postgres SET " +
                "bigint = :bigint, " +
                "boolean_b = :boolean_b, " +
                "bytea = :bytea, " +
                "char = :char, " +
                "char10 = :char10, " +
                "date = :date, " +
                "decimal_eighteen_zero = :decimal_eighteen_zero, " +
                "decimal_nine_zero = :decimal_nine_zero, " +
                "decimal_nineteen_zero = :decimal_nineteen_zero, " +
                "decimal_ten_two = :decimal_ten_two, " +
                "decimal_ten_zero = :decimal_ten_zero, " +
                "double = :double, " +
                "float = :float, " +
                "guid = :guid, " +
                "integer = :integer, " +
                "numeric_ten_two = :numeric_ten_two, " +
                "smallint = :smallint, " +
                "text = :text, " +
                "timestamp = :timestamp, " +
                "varchar10 = :varchar10, " +
                "xml = :xml " +
                "WHERE id = :id";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM public.data_types_postgres " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM public.data_types_postgres";
    }

    @Override
    public Column<DataTypesPostgresEntity, ?> getColumnByName(String name) {
        for (Column<DataTypesPostgresEntity, ?> column : ALL_COLUMNS_LIST) {
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
                "FROM public.data_types_postgres %n" +
                "WHERE 1=1 %s %n" +
                "%s " +
                "LIMIT %d", whereClause, orderBy, maxAllowedCount);
    }

    @Override
    protected String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM public.data_types_postgres %n" +
                "WHERE 1=1 %s %n" +
                "%s %n" +
                "LIMIT %d OFFSET %d", whereClause, orderBy, pageSize, start);
    }

    @Override
    protected String getSelectAndLockSql() {
        return "SELECT " +
                ALL_COLUMNS +
                "FROM public.data_types_postgres " +
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
