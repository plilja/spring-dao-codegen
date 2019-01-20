package se.plilja.springdaogen.dbtests.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import se.plilja.springdaogen.dbtests.framework.BaseRepository;

@Repository
public class BazPostgresRepository extends BaseRepository<BazPostgresEntity, Integer> {

    private static final RowMapper<BazPostgresEntity> ROW_MAPPER = (rs, i) -> {
        return new BazPostgresEntity()
                .setId(rs.getObject("baz_id") != null ? rs.getInt("baz_id") : null)
                .setBazName(rs.getString("baz_name"));
    };

    @Autowired
    public BazPostgresRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(Integer.class, jdbcTemplate, ROW_MAPPER);
    }

    @Override
    public SqlParameterSource getParams(BazPostgresEntity o) {
        MapSqlParameterSource m = new MapSqlParameterSource();
        m.addValue("baz_id", o.getId());
        m.addValue("baz_name", o.getBazName());
        return m;
    }

    @Override
    protected String getExistsByIdSql() {
        return "SELECT " +
                "COUNT(*) " +
                "FROM test_schema.baz_postgres " +
                "WHERE baz_id = :baz_id";
    }

    @Override
    protected String getSelectOneSql() {
        return "SELECT " +
                "   baz_id, " +
                "   baz_name " +
                "FROM test_schema.baz_postgres " +
                "WHERE baz_id = :baz_id";
    }

    @Override
    protected String getSelectManySql(int maxSelectCount) {
        return String.format("SELECT " +
                "   baz_id, " +
                "   baz_name " +
                "FROM test_schema.baz_postgres " +
                "LIMIT %d", maxSelectCount);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO test_schema.baz_postgres (" +
                "   baz_name" +
                ") " +
                "VALUES (" +
                "   :baz_name" +
                ")";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE test_schema.baz_postgres SET " +
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
