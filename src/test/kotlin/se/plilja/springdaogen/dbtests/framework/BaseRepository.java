package se.plilja.springdaogen.dbtests.framework;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public abstract class BaseRepository<T extends BaseEntity<?, ID>, ID> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private Class<ID> idClass;
    private final RowMapper<T> rowMapper;

    protected BaseRepository(Class<ID> idClass, NamedParameterJdbcTemplate jdbcTemplate, RowMapper<T> rowMapper) {
        this.idClass = idClass;
        this.rowMapper = rowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsById(ID id) {
        String sql = getExistsByIdSql();
        Map<String, Object> params = new HashMap<>();
        params.put(getPrimaryKeyColumnName(), id);
        return jdbcTemplate.queryForObject(sql, params, Integer.class) > 0;
    }

    public T getOne(ID id) {
        String sql = getSelectOneSql();
        Map<String, Object> params = new HashMap<>();
        params.put(getPrimaryKeyColumnName(), id);
        return jdbcTemplate.queryForObject(sql, params, rowMapper);
    }

    public Optional<T> findOneById(ID id) {
        try {
            T res = getOne(id);
            return Optional.of(res);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<T> findAll() {
        return findAll(getSelectAllDefaultMaxCount());
    }

    public List<T> findAll(int maxAllowedCount) {
        List<T> result = jdbcTemplate.query(getSelectManySql(maxAllowedCount + 1), Collections.emptyMap(), rowMapper);
        if (result.size() > maxAllowedCount) {
            throw new MaxAllowedCountExceededException(String.format("Max allowed count of %d rows exceeded", maxAllowedCount));
        }
        return result;
    }

    @Transactional
    public void update(T object) {
        String sql = getUpdateSql();
        SqlParameterSource params = getParams(object);
        int updated = jdbcTemplate.update(sql, params);
        if (updated == 0) {
            throw new SqlUpdateException(String.format("No rows affected when trying to update object with id %s", object.getId())); // TODO more informative message
        } else if (updated > 1) {
            throw new SqlUpdateException(String.format("More than one row [%d] affected by update", updated));
        }
    }

    public void create(T object) {
        if (object.getId() != null) {
            // TODO non generated key
            throw new IllegalArgumentException(String.format("Attempting to create a new object with an existing id %s", object.getId()));
        }
        String sql = getInsertSql();
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = getParams(object);
        jdbcTemplate.update(sql, params, keyHolder, new String[]{getPrimaryKeyColumnName()});
        setId(object, keyHolder.getKey());
    }

    @Transactional
    public void delete(T object) {
        deleteById(object.getId());
    }

    @Transactional
    public void deleteById(ID id) {
        String sql = getDeleteSql();
        Map<String, Object> params = new HashMap<>();
        params.put("ids", Collections.singletonList(id));
        int updated = jdbcTemplate.update(sql, params);
        if (updated == 0) {
            throw new SqlUpdateException(String.format("No rows affected when trying to delete object with id %s", id));
        } else if (updated > 1) {
            throw new SqlUpdateException(String.format("More than one row [%d] affected by update", updated));
        }
    }

    @Transactional
    public void deleteAll(Iterable<T> objects) {
        String sql = getDeleteSql();
        Map<String, Object> params = new HashMap<>();
        List<ID> ids = new ArrayList<>();
        for (T object : objects) {
            ids.add(object.getId());
        }
        params.put("ids", ids);
        int updated = jdbcTemplate.update(sql, params);
        if (updated > ids.size()) {
            throw new SqlUpdateException(String.format("%d objects was affected by delete, but only %d was expected", updated, ids.size()));
        }
    }

    public long count() {
        String sql = getCountSql();
        HashMap<String, Object> noParams = new HashMap<>();
        return jdbcTemplate.queryForObject(sql, noParams, Long.class);
    }

    protected abstract SqlParameterSource getParams(T object);

    protected abstract String getExistsByIdSql();

    protected abstract String getSelectOneSql();

    protected abstract String getSelectManySql(int maxAllowedCount);

    protected abstract String getInsertSql();

    protected abstract String getUpdateSql();

    protected abstract String getPrimaryKeyColumnName();

    protected abstract String getDeleteSql();

    protected abstract String getCountSql();

    /**
     * @noinspection unchecked
     */
    private void setId(T object, Number newKey) {
        if (idClass.isAssignableFrom(Integer.class)) {
            object.setId((ID) Integer.valueOf(newKey.intValue()));
        } else if (idClass.isAssignableFrom(Long.class)) {
            object.setId((ID) Long.valueOf(newKey.longValue()));
        } else {
            throw new IllegalArgumentException(String.format("Unkown type of generated key %s", idClass.getSimpleName()));
        }
    }

    /**
     * Max allowed count when selecting all objects. Protects against
     * unexpectedly large queries that may cause performance degradation.
     */
    protected abstract int getSelectAllDefaultMaxCount();
}