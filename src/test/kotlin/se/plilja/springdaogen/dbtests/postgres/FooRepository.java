package se.plilja.springdaogen.dbtests.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import se.plilja.springdaogen.dbtests.framework.BaseRepository;

import java.util.UUID;

@Repository
public class FooRepository extends BaseRepository<FooEntity, Long> {

    private static final RowMapper<FooEntity> ROW_MAPPER = (rs, i) -> {
        return new FooEntity()
                .setFooId((Long) rs.getObject("FOO_ID"))
                .setBooleanBit((Boolean) rs.getObject("BOOLEAN_BIT"))
                .setBooleanB((Boolean) rs.getObject("BOOLEAN_B"))
                .setChaR(rs.getString("CHAR"))
                .setDate(rs.getDate("DATE").toLocalDate())
                .setTimestamp(rs.getTimestamp("TIMESTAMP").toLocalDateTime())
                .setBigdecimal(rs.getBigDecimal("BIGDECIMAL"))
                .setFloaT((Float) rs.getObject("FLOAT"))
                .setDoublE((Double) rs.getObject("DOUBLE"))
                .setGuid(UUID.fromString(rs.getString("GUID")))
                .setXml(rs.getString("XML"))
                .setText(rs.getString("TEXT"))
                .setBytea((byte[]) rs.getObject("BYTEA"));
    };

    @Autowired
    public FooRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Long.class, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(FooEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("FOO_ID", o.getFooId());
        m.addValue("BOOLEAN_BIT", o.getBooleanBit());
        m.addValue("BOOLEAN_B", o.getBooleanB());
        m.addValue("CHAR", o.getChaR());
        m.addValue("DATE", o.getDate());
        m.addValue("TIMESTAMP", o.getTimestamp());
        m.addValue("BIGDECIMAL", o.getBigdecimal());
        m.addValue("FLOAT", o.getFloaT());
        m.addValue("DOUBLE", o.getDoublE());
        m.addValue("GUID", o.getGuid());
        m.addValue("XML", o.getXml());
        m.addValue("TEXT", o.getText());
        m.addValue("BYTEA", o.getBytea());
        return m;
    }

    @Override
    protected String getSelectOneSql() {
        return "SELECT " +
                "   \"FOO_ID\", " +
                "   \"BOOLEAN_BIT\", " +
                "   \"BOOLEAN_B\", " +
                "   \"CHAR\", " +
                "   \"DATE\", " +
                "   \"TIMESTAMP\", " +
                "   \"BIGDECIMAL\", " +
                "   \"FLOAT\", " +
                "   \"DOUBLE\", " +
                "   \"GUID\", " +
                "   \"XML\", " +
                "   \"TEXT\", " +
                "   \"BYTEA\" " +
                "FROM public.\"FOO\" " +
                "WHERE \"FOO_ID\" = :FOO_ID";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   \"FOO_ID\", " +
                "   \"BOOLEAN_BIT\", " +
                "   \"BOOLEAN_B\", " +
                "   \"CHAR\", " +
                "   \"DATE\", " +
                "   \"TIMESTAMP\", " +
                "   \"BIGDECIMAL\", " +
                "   \"FLOAT\", " +
                "   \"DOUBLE\", " +
                "   \"GUID\", " +
                "   \"XML\", " +
                "   \"TEXT\", " +
                "   \"BYTEA\" " +
                "FROM public.\"FOO\" " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO public.\"FOO\" (" +
                "   \"BOOLEAN_BIT\", " +
                "   \"BOOLEAN_B\", " +
                "   \"CHAR\", " +
                "   \"DATE\", " +
                "   \"TIMESTAMP\", " +
                "   \"BIGDECIMAL\", " +
                "   \"FLOAT\", " +
                "   \"DOUBLE\", " +
                "   \"GUID\", " +
                "   \"XML\", " +
                "   \"TEXT\", " +
                "   \"BYTEA\"" +
                ") " +
                "VALUES (" +
                "   :BOOLEAN_BIT, " +
                "   :BOOLEAN_B, " +
                "   :CHAR, " +
                "   :DATE, " +
                "   :TIMESTAMP, " +
                "   :BIGDECIMAL, " +
                "   :FLOAT, " +
                "   :DOUBLE, " +
                "   :GUID, " +
                "   :XML, " +
                "   :TEXT, " +
                "   :BYTEA" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE public.\"FOO\" SET " +
                "   BOOLEAN_BIT = :BOOLEAN_BIT, " +
                "   BOOLEAN_B = :BOOLEAN_B, " +
                "   CHAR = :CHAR, " +
                "   DATE = :DATE, " +
                "   TIMESTAMP = :TIMESTAMP, " +
                "   BIGDECIMAL = :BIGDECIMAL, " +
                "   FLOAT = :FLOAT, " +
                "   DOUBLE = :DOUBLE, " +
                "   GUID = :GUID, " +
                "   XML = :XML, " +
                "   TEXT = :TEXT, " +
                "   BYTEA = :BYTEA " +
                "WHERE FOO_ID = :FOO_ID";
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
