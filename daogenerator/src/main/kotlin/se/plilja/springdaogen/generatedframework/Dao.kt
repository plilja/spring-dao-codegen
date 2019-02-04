package se.plilja.springdaogen.generatedframework


fun dao(_package: String): Pair<String, String> {
    return Pair(
        "Dao", """
package $_package;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Dao<T extends BaseEntity<ID>, ID> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private Class<ID> idClass;
    private boolean idIsGenerated;

    protected Dao(Class<ID> idClass, boolean idIsGenerated, NamedParameterJdbcTemplate jdbcTemplate) {
        this.idClass = idClass;
        this.idIsGenerated = idIsGenerated;
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsById(ID id) {
        String sql = getExistsByIdSql();
        Map<String, Object> params = new HashMap<>();
        params.put(getPrimaryKeyColumnName(), id);
        return jdbcTemplate.queryForObject(sql, params, Integer.class) > 0;
    }

    public T getOne(ID id) {
        String sql = getSelectIdsSql();
        Map<String, Object> params = new HashMap<>();
        params.put("ids", Collections.singletonList(id));
        return jdbcTemplate.queryForObject(sql, params, getRowMapper());
    }

    public Optional<T> findOneById(ID id) {
        try {
            T res = getOne(id);
            return Optional.of(res);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<T> findAllById(Iterable<ID> ids) {
        String sql = getSelectIdsSql();
        List<ID> idsList = new ArrayList<>();
        for (ID id : ids) {
            idsList.add(id);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", idsList);
        return jdbcTemplate.query(sql, params, getRowMapper());
    }

    public List<T> findPage(long start, int pageSize) {
        if (pageSize == 0) {
            return Collections.emptyList();
        }
        String sql = getSelectPageSql(start, pageSize);
        return jdbcTemplate.query(sql, Collections.emptyMap(), getRowMapper());
    }

    public List<T> findAll() {
        return findAll(getSelectAllDefaultMaxCount());
    }

    public List<T> findAll(int maxAllowedCount) {
        List<T> result = jdbcTemplate.query(getSelectManySql(maxAllowedCount + 1), Collections.emptyMap(), getRowMapper());
        // If queries have been correctly generated it should never be possible to select
        // more than maxAllowedCount + 1 even if the table is larger than that
        assert result.size() <= maxAllowedCount + 1;
        if (result.size() > maxAllowedCount) {
            throw new MaxAllowedCountExceededException(String.format("Max allowed count of %d rows exceeded", maxAllowedCount));
        }
        return result;
    }

    @Transactional
    public void save(T object) {
        if (object.getId() == null) {
            create(object);
        } else if (idIsGenerated) {
            update(object);
        } else if (existsById(object.getId())) {
            update(object);
        } else {
            create(object);
        }
    }

    private void create(T object) {
        setCreatedAt(object);
        if (idIsGenerated) {
            if (object.getId() != null) {
                throw new IllegalArgumentException(String.format("Attempting to create a new object with an existing id %s", object.getId()));
            }
            String sql = getInsertSql();
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            SqlParameterSource params = getParams(object);
            jdbcTemplate.update(sql, params, keyHolder, new String[]{getPrimaryKeyColumnName()});
            setId(object, keyHolder.getKey());
        } else {
            String sql = getInsertSql();
            SqlParameterSource params = getParams(object);
            jdbcTemplate.update(sql, params);
        }
    }

    private void update(T object) {
        setChangedAt(object);
        String sql = getUpdateSql();
        SqlParameterSource params = getParams(object);
        int updated = jdbcTemplate.update(sql, params);
        if (updated == 0) {
            throw new SqlUpdateException(String.format("No rows affected when trying to update object with id %s", object.getId())); // TODO more informative message
        } else if (updated > 1) {
            throw new SqlUpdateException(String.format("More than one row [%d] affected by update", updated));
        }
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

    protected abstract RowMapper<T> getRowMapper();

    protected abstract SqlParameterSource getParams(T object);

    protected abstract String getExistsByIdSql();

    protected abstract String getSelectIdsSql();

    protected abstract String getSelectManySql(int maxAllowedCount);

    protected abstract String getSelectPageSql(long start, int pageSize);

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
            throw new IllegalArgumentException(String.format("Unknown type of generated key %s", idClass.getSimpleName()));
        }
    }

    /**
     * Max allowed count when selecting all objects. Protects against
     * unexpectedly large queries that may cause performance degradation.
     */
    protected abstract int getSelectAllDefaultMaxCount();

    protected void setCreatedAt(T object) {
        // default does nothing, tables with a created_at column should override this method
    }

    protected void setChangedAt(T object) {
        // default does nothing, tables with a changed_at column should override this method
    }

}
    """.trimIndent())
}