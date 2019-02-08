package dbtests.postgres.model;

import dbtests.framework.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.UUID;

@Repository
public class DataTypesPostgresDao extends Dao<DataTypesPostgresEntity, Long> {

    private static final RowMapper<DataTypesPostgresEntity> ROW_MAPPER = (rs, i) -> {
        DataTypesPostgresEntity r = new DataTypesPostgresEntity();
        r.setId(rs.getObject("id") != null ? rs.getLong("id") : null);
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
        r.setGuid(UUID.fromString(rs.getString("guid")));
        r.setInteger(rs.getObject("integer") != null ? rs.getInt("integer") : null);
        r.setNumericTenTwo(rs.getBigDecimal("numeric_ten_two"));
        r.setSmallint(rs.getObject("smallint") != null ? rs.getInt("smallint") : null);
        r.setText(rs.getString("text"));
        r.setTimestamp(rs.getObject("timestamp") != null ? rs.getTimestamp("timestamp").toLocalDateTime() : null);
        r.setVarchar10(rs.getString("varchar10"));
        r.setXml(rs.getString("xml"));
        return r;
    };
    private static final String ALL_COLUMNS = " id, bigint, boolean_b, bytea, char, " +
            " char10, date, decimal_eighteen_zero, decimal_nine_zero, decimal_nineteen_zero, " +
            " decimal_ten_two, decimal_ten_zero, double, float, guid, " +
            " integer, numeric_ten_two, smallint, text, timestamp, " +
            " varchar10, xml ";

    @Autowired
    public DataTypesPostgresDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Long.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(DataTypesPostgresEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        m.addValue("bigint", o.getBigint());
        m.addValue("boolean_b", o.getBooleanB(), Types.BOOLEAN);
        m.addValue("bytea", o.getBytea());
        m.addValue("char", o.getChaR());
        m.addValue("char10", o.getChar10());
        m.addValue("date", o.getDate(), Types.DATE);
        m.addValue("decimal_eighteen_zero", o.getDecimalEighteenZero());
        m.addValue("decimal_nine_zero", o.getDecimalNineZero());
        m.addValue("decimal_nineteen_zero", o.getDecimalNineteenZero());
        m.addValue("decimal_ten_two", o.getDecimalTenTwo(), Types.NUMERIC);
        m.addValue("decimal_ten_zero", o.getDecimalTenZero());
        m.addValue("double", o.getDoublE(), Types.DOUBLE);
        m.addValue("float", o.getFloaT(), Types.FLOAT);
        m.addValue("guid", o.getGuid());
        m.addValue("integer", o.getInteger());
        m.addValue("numeric_ten_two", o.getNumericTenTwo(), Types.NUMERIC);
        m.addValue("smallint", o.getSmallint());
        m.addValue("text", o.getText());
        m.addValue("timestamp", o.getTimestamp());
        m.addValue("varchar10", o.getVarchar10());
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
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                ALL_COLUMNS +
                "FROM public.data_types_postgres %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
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
    protected String getLockSql() {
        return "SELECT id FROM public.data_types_postgres " +
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
