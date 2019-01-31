package dbtests.postgres.model;

import dbtests.framework.BaseRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class FooPostgresRepository extends BaseRepository<FooPostgresEntity, Long> {

    private static final RowMapper<FooPostgresEntity> ROW_MAPPER = (rs, i) -> {
        FooPostgresEntity r = new FooPostgresEntity();
        r.setFooId((Long) rs.getObject("FOO_ID"));
        r.setBigdecimal(rs.getBigDecimal("BIGDECIMAL"));
        r.setBooleanB((Boolean) rs.getObject("BOOLEAN_B"));
        r.setBooleanBit((Boolean) rs.getObject("BOOLEAN_BIT"));
        r.setBytea((byte[]) rs.getObject("BYTEA"));
        r.setChaR(rs.getString("CHAR"));
        r.setDate(rs.getDate("DATE").toLocalDate());
        r.setDoublE((Double) rs.getObject("DOUBLE"));
        r.setFloaT((Float) rs.getObject("FLOAT"));
        r.setGuid(UUID.fromString(rs.getString("GUID")));
        r.setText(rs.getString("TEXT"));
        r.setTimestamp(rs.getTimestamp("TIMESTAMP").toLocalDateTime());
        r.setXml(rs.getString("XML"));
        return r;
    };

    @Autowired
    public FooPostgresRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Long.class, false, jdbcTemplate);
    }

    @Override
    public SqlParameterSource getParams(FooPostgresEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("FOO_ID", o.getId());
        m.addValue("BIGDECIMAL", o.getBigdecimal());
        m.addValue("BOOLEAN_B", o.getBooleanB());
        m.addValue("BOOLEAN_BIT", o.getBooleanBit());
        m.addValue("BYTEA", o.getBytea());
        m.addValue("CHAR", o.getChaR());
        m.addValue("DATE", o.getDate());
        m.addValue("DOUBLE", o.getDoublE());
        m.addValue("FLOAT", o.getFloaT());
        m.addValue("GUID", o.getGuid());
        m.addValue("TEXT", o.getText());
        m.addValue("TIMESTAMP", o.getTimestamp());
        m.addValue("XML", o.getXml());
        return m;
    }

    @Override
    protected RowMapper<FooPostgresEntity> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM public.\"FOO_POSTGRES\" " +
                "WHERE \"FOO_ID\" = :FOO_ID";
    }

    @Override
    protected String getSelectIdsSql() {
        return "SELECT " +
                "\"FOO_ID\", " +
                "\"BIGDECIMAL\", " +
                "\"BOOLEAN_B\", " +
                "\"BOOLEAN_BIT\", " +
                "\"BYTEA\", " +
                "\"CHAR\", " +
                "\"DATE\", " +
                "\"DOUBLE\", " +
                "\"FLOAT\", " +
                "\"GUID\", " +
                "\"TEXT\", " +
                "\"TIMESTAMP\", " +
                "\"XML\" " +
                "FROM public.\"FOO_POSTGRES\" " +
                "WHERE \"FOO_ID\" IN (:ids)";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   \"FOO_ID\", " +
                "   \"BIGDECIMAL\", " +
                "   \"BOOLEAN_B\", " +
                "   \"BOOLEAN_BIT\", " +
                "   \"BYTEA\", " +
                "   \"CHAR\", " +
                "   \"DATE\", " +
                "   \"DOUBLE\", " +
                "   \"FLOAT\", " +
                "   \"GUID\", " +
                "   \"TEXT\", " +
                "   \"TIMESTAMP\", " +
                "   \"XML\" " +
                "FROM public.\"FOO_POSTGRES\" " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getSelectPageSql(long start, int pageSize) {
        return String.format("SELECT %n" +
                "\"FOO_ID\", %n" +
                "\"BIGDECIMAL\", %n" +
                "\"BOOLEAN_B\", %n" +
                "\"BOOLEAN_BIT\", %n" +
                "\"BYTEA\", %n" +
                "\"CHAR\", %n" +
                "\"DATE\", %n" +
                "\"DOUBLE\", %n" +
                "\"FLOAT\", %n" +
                "\"GUID\", %n" +
                "\"TEXT\", %n" +
                "\"TIMESTAMP\", %n" +
                "\"XML\" %n" +
                "FROM public.\"FOO_POSTGRES\" %n" +
                "LIMIT %d OFFSET %d", pageSize, start);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO public.\"FOO_POSTGRES\" (" +
                "   \"FOO_ID\", " +
                "   \"BIGDECIMAL\", " +
                "   \"BOOLEAN_B\", " +
                "   \"BOOLEAN_BIT\", " +
                "   \"BYTEA\", " +
                "   \"CHAR\", " +
                "   \"DATE\", " +
                "   \"DOUBLE\", " +
                "   \"FLOAT\", " +
                "   \"GUID\", " +
                "   \"TEXT\", " +
                "   \"TIMESTAMP\", " +
                "   \"XML\"" +
                ") " +
                "VALUES (" +
                "   :FOO_ID, " +
                "   :BIGDECIMAL, " +
                "   :BOOLEAN_B, " +
                "   :BOOLEAN_BIT, " +
                "   :BYTEA, " +
                "   :CHAR, " +
                "   :DATE, " +
                "   :DOUBLE, " +
                "   :FLOAT, " +
                "   :GUID, " +
                "   :TEXT, " +
                "   :TIMESTAMP, " +
                "   :XML" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE public.\"FOO_POSTGRES\" SET " +
                "   BIGDECIMAL = :BIGDECIMAL, " +
                "   BOOLEAN_B = :BOOLEAN_B, " +
                "   BOOLEAN_BIT = :BOOLEAN_BIT, " +
                "   BYTEA = :BYTEA, " +
                "   CHAR = :CHAR, " +
                "   DATE = :DATE, " +
                "   DOUBLE = :DOUBLE, " +
                "   FLOAT = :FLOAT, " +
                "   GUID = :GUID, " +
                "   TEXT = :TEXT, " +
                "   TIMESTAMP = :TIMESTAMP, " +
                "   XML = :XML " +
                "WHERE FOO_ID = :FOO_ID";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM public.\"FOO_POSTGRES\" " +
                "WHERE \"FOO_ID\" IN (:ids)";
    }

    @Override
    protected String getCountSql() {
        return "SELECT COUNT(*) FROM public.\"FOO_POSTGRES\"";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "FOO_ID";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
