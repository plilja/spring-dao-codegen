package se.plilja.springdaogen.dbtests.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import se.plilja.springdaogen.dbtests.framework.BaseRepository;

@Repository
public class BazRepository extends BaseRepository<BazEntity, Integer> {

    private static final RowMapper<BazEntity> ROW_MAPPER = (rs, i) -> {
        return new BazEntity()
                .setBazId(rs.getObject("baz_id") != null ? rs.getInt("baz_id") : null)
                .setBazName(rs.getString("baz_name"));
    };

    @Autowired
    public BazRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(BazEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("baz_id", o.getBazId());
        m.addValue("baz_name", o.getBazName());
        return m;
    }

    @Override
    protected String getSelectOneSql() {
        return "SELECT " +
                "   baz_id, " +
                "   baz_name " +
                "FROM test_schema.baz " +
                "WHERE baz_id = :baz_id";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   baz_id, " +
                "   baz_name " +
                "FROM test_schema.baz " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.baz (" +
                "   baz_name" +
                ") " +
                "VALUES (" +
                "   :baz_name" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE test_schema.baz SET " +
                "   baz_name = :baz_name " +
                "WHERE baz_id = :baz_id";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "baz_id";
    }

    @Override
    protected int getSelectAllDefaultMaxCount() {
        return 10;
    }

}
