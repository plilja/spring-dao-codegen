package dbtests.mssql.model;

import dbtests.framework.Dao;
import java.math.BigDecimal;
import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class DataTypesMsSqlDao extends Dao<DataTypesMsSqlEntity, Long> {

    private static final RowMapper<DataTypesMsSqlEntity> ROW_MAPPER = (rs, i) -> {
        DataTypesMsSqlEntity r = new DataTypesMsSqlEntity();
        r.setId(rs.getObject("id") != null ? rs.getLong("id") : null);
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
    public DataTypesMsSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Long.class, true, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(DataTypesMsSqlEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("id", o.getId());
        m.addValue("binary10", o.getBinary10());
        m.addValue("bit", o.getBit(), Types.BOOLEAN);
        m.addValue("char", o.getChaR());
        m.addValue("char10", o.getChar10());
        m.addValue("date", o.getDate(), Types.DATE);
        m.addValue("datetime", o.getDatetime());
        m.addValue("datetime2", o.getDatetime2());
        m.addValue("decimal_eighteen_zero", o.getDecimalEighteenZero());
        m.addValue("decimal_nine_zero", o.getDecimalNineZero());
        m.addValue("decimal_nineteen_zero", o.getDecimalNineteenZero());
        m.addValue("decimal_ten_two", o.getDecimalTenTwo(), Types.NUMERIC);
        m.addValue("decimal_ten_zero", o.getDecimalTenZero());
        m.addValue("float", o.getFloaT(), Types.FLOAT);
        m.addValue("int", o.getInT());
        m.addValue("money", o.getMoney(), Types.NUMERIC);
        m.addValue("nchar10", o.getNchar10());
        m.addValue("ntext", o.getNtext());
        m.addValue("nvarchar10", o.getNvarchar10());
        m.addValue("real", o.getReal(), Types.FLOAT);
        m.addValue("smallint", o.getSmallint());
        m.addValue("smallmoney", o.getSmallmoney(), Types.NUMERIC);
        m.addValue("text", o.getText());
        m.addValue("time", o.getTime());
        m.addValue("tinyint", o.getTinyint());
        m.addValue("varbinary10", o.getVarbinary10());
        m.addValue("varchar10", o.getVarchar10());
        m.addValue("xml", o.getXml());
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
                "id, " +
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
                "xml " +
                "FROM dbo.DATA_TYPES_MS_SQL " +
                "WHERE id IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT TOP %d " +
                "   id, " +
                "   binary10, " +
                "   bit, " +
                "   char, " +
                "   char10, " +
                "   date, " +
                "   datetime, " +
                "   datetime2, " +
                "   decimal_eighteen_zero, " +
                "   decimal_nine_zero, " +
                "   decimal_nineteen_zero, " +
                "   decimal_ten_two, " +
                "   decimal_ten_zero, " +
                "   float, " +
                "   int, " +
                "   money, " +
                "   nchar10, " +
                "   ntext, " +
                "   nvarchar10, " +
                "   real, " +
                "   smallint, " +
                "   smallmoney, " +
                "   text, " +
                "   time, " +
                "   tinyint, " +
                "   varbinary10, " +
                "   varchar10, " +
                "   xml " +
                "FROM dbo.DATA_TYPES_MS_SQL ", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "id, %n" +
                "binary10, %n" +
                "bit, %n" +
                "char, %n" +
                "char10, %n" +
                "date, %n" +
                "datetime, %n" +
                "datetime2, %n" +
                "decimal_eighteen_zero, %n" +
                "decimal_nine_zero, %n" +
                "decimal_nineteen_zero, %n" +
                "decimal_ten_two, %n" +
                "decimal_ten_zero, %n" +
                "float, %n" +
                "int, %n" +
                "money, %n" +
                "nchar10, %n" +
                "ntext, %n" +
                "nvarchar10, %n" +
                "real, %n" +
                "smallint, %n" +
                "smallmoney, %n" +
                "text, %n" +
                "time, %n" +
                "tinyint, %n" +
                "varbinary10, %n" +
                "varchar10, %n" +
                "xml %n" +
                "FROM dbo.DATA_TYPES_MS_SQL %n" +
                "ORDER BY id %n" +
                "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", start, pageSize);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO dbo.DATA_TYPES_MS_SQL (" +
                "   binary10, " +
                "   bit, " +
                "   char, " +
                "   char10, " +
                "   date, " +
                "   datetime, " +
                "   datetime2, " +
                "   decimal_eighteen_zero, " +
                "   decimal_nine_zero, " +
                "   decimal_nineteen_zero, " +
                "   decimal_ten_two, " +
                "   decimal_ten_zero, " +
                "   float, " +
                "   int, " +
                "   money, " +
                "   nchar10, " +
                "   ntext, " +
                "   nvarchar10, " +
                "   real, " +
                "   smallint, " +
                "   smallmoney, " +
                "   text, " +
                "   time, " +
                "   tinyint, " +
                "   varbinary10, " +
                "   varchar10, " +
                "   xml" +
                ") " +
                "VALUES (" +
                "   :binary10, " +
                "   :bit, " +
                "   :char, " +
                "   :char10, " +
                "   :date, " +
                "   :datetime, " +
                "   :datetime2, " +
                "   :decimal_eighteen_zero, " +
                "   :decimal_nine_zero, " +
                "   :decimal_nineteen_zero, " +
                "   :decimal_ten_two, " +
                "   :decimal_ten_zero, " +
                "   :float, " +
                "   :int, " +
                "   :money, " +
                "   :nchar10, " +
                "   :ntext, " +
                "   :nvarchar10, " +
                "   :real, " +
                "   :smallint, " +
                "   :smallmoney, " +
                "   :text, " +
                "   :time, " +
                "   :tinyint, " +
                "   :varbinary10, " +
                "   :varchar10, " +
                "   :xml" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE dbo.DATA_TYPES_MS_SQL SET " +
                "   binary10 = :binary10, " +
                "   bit = :bit, " +
                "   char = :char, " +
                "   char10 = :char10, " +
                "   date = :date, " +
                "   datetime = :datetime, " +
                "   datetime2 = :datetime2, " +
                "   decimal_eighteen_zero = :decimal_eighteen_zero, " +
                "   decimal_nine_zero = :decimal_nine_zero, " +
                "   decimal_nineteen_zero = :decimal_nineteen_zero, " +
                "   decimal_ten_two = :decimal_ten_two, " +
                "   decimal_ten_zero = :decimal_ten_zero, " +
                "   float = :float, " +
                "   int = :int, " +
                "   money = :money, " +
                "   nchar10 = :nchar10, " +
                "   ntext = :ntext, " +
                "   nvarchar10 = :nvarchar10, " +
                "   real = :real, " +
                "   smallint = :smallint, " +
                "   smallmoney = :smallmoney, " +
                "   text = :text, " +
                "   time = :time, " +
                "   tinyint = :tinyint, " +
                "   varbinary10 = :varbinary10, " +
                "   varchar10 = :varchar10, " +
                "   xml = :xml " +
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
    protected String getPrimaryKeyColumnName() {
        return "id";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
