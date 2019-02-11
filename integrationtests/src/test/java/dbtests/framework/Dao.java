package dbtests.framework;

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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Base class providing basic CRUD access to a database
 * table.
 */
public abstract class Dao<T extends BaseEntity<ID>, ID> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private Class<ID> idClass;
    private boolean idIsGenerated;

    protected Dao(Class<ID> idClass, boolean idIsGenerated, NamedParameterJdbcTemplate jdbcTemplate) {
        this.idClass = idClass;
        this.idIsGenerated = idIsGenerated;
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean exists(ID id) {
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

    public Optional<T> findOne(ID id) {
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

    /**
     * Find a page in a table. Will return a smaller list or
     * an empty list if start + pageSize reaches outside of table.
     * An iteration over the pages should end whenever the returned
     * list is smaller than the pageSize.
     *
     * @param start    what offset to start at
     * @param pageSize size of retrieved page
     * @return a list of rows between index start (inclusive) and start + page_size (exclusive)
     */
    public List<T> findPage(long start, int pageSize) {
        if (pageSize == 0) {
            return Collections.emptyList();
        }
        String sql = getSelectPageSql(start, pageSize);
        return jdbcTemplate.query(sql, Collections.emptyMap(), getRowMapper());
    }

    /**
     * Finds all rows in the table. Note that
     * this will throw a {@link TooManyRowsAvailableException}
     * if the table is larger than expected. If you
     * expect many rows to be returned, you should either
     * use the {@link #findPage} method. If you know that the table
     * will fit in memory you can also use {@link #findAll(int)}
     * or reconfigure the limit for when to throw the exception.
     */
    public List<T> findAll() throws TooManyRowsAvailableException {
        return findAll(getSelectAllDefaultMaxCount());
    }

    /**
     * Same as {@link #findAll()} but with a parameter indicating
     * what is considered too many rows.
     */
    public List<T> findAll(int maxAllowedCount) throws TooManyRowsAvailableException {
        List<T> result = jdbcTemplate.query(getSelectManySql(maxAllowedCount + 1), Collections.emptyMap(), getRowMapper());
        ensureMaxCountNotExceeded(maxAllowedCount, result);
        return result;
    }

    private void ensureMaxCountNotExceeded(int maxAllowedCount, List<T> result) {
        // If queries have been correctly generated it should never be possible to select
        // more than maxAllowedCount + 1 even if the table is larger than that
        assert result.size() <= maxAllowedCount + 1;
        if (result.size() > maxAllowedCount) {
            throw new TooManyRowsAvailableException(String.format("Max allowed count of %d rows exceeded", maxAllowedCount));
        }
    }

    @Transactional
    public void save(T object) {
        if (object.getId() == null) {
            create(object);
        } else if (idIsGenerated) {
            update(object);
        } else if (exists(object.getId())) {
            update(object);
        } else {
            create(object);
        }
    }

    private void create(T object) {
        setCreatedAt(object);
        setChangedAt(object);
        initializeVersion(object);
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
            throw new NoRowsUpdatedException(String.format("No rows affected when trying to update object with id %s", object.getId()));
        } else if (updated > 1) {
            throw new TooManyRowsUpdatedException(String.format("More than one row (%d) affected by update of object with id %s", updated, object.getId()));
        }
        bumpVersion(object);
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
            throw new NoRowsUpdatedException(String.format("No rows affected when trying to delete object with id %s", id));
        } else if (updated > 1) {
            throw new TooManyRowsUpdatedException(String.format("More than one row [%d] deleted when trying to delete object with id %s", updated, id));
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
            throw new TooManyRowsUpdatedException(String.format("%d objects was affected by delete, but only %d was expected", updated, ids.size()));
        }
    }

    public long count() {
        String sql = getCountSql();
        HashMap<String, Object> noParams = new HashMap<>();
        return jdbcTemplate.queryForObject(sql, noParams, Long.class);
    }

    /**
     * Lock a row in the database. Note that this
     * method must be called in a @Transaction
     * scope at a higher application layer for this
     * to be useful. The lock is held while the transaction
     * is committed/rolled back and hence it doesn't need
     * to be explicitly released by the caller.
     * <p/>
     * The usability of this method depends on what
     * transaction isolation level is used. Typically
     * this is mostly useful if the isolation level is
     * READ_COMMITTED which is also most often the
     * default isolation level.
     */
    public void lock(ID id) {
        String sql = getLockSql();
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbcTemplate.query(sql, params, (rs, i) -> new Object()); // Discard result
    }
    
    public List<T> query(QueryItem<T, ?> queryItem) {
        return query(Collections.singletonList(queryItem));
    }

    /**
     * Perform a query.
     * <p/>
     * Query items are joined by AND
     * when sent to the database.
     */
    public List<T> query(List<QueryItem<T, ?>> queryItems) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder(" ");
        for (int i = 0; i < queryItems.size(); i++) {
            if (i > 0) {
                sb.append(" AND ");
            }
            QueryItem<T, ?> queryItem = queryItems.get(i);
            if (queryItem.getValue() == null) {
                if (queryItem.getOperator() == QueryItem.Operator.EQ) {
                    sb.append(String.format("%s IS NULL", queryItem.getColumn().getName()));
                } else if (queryItem.getOperator() == QueryItem.Operator.NEQ) {
                    sb.append(String.format("%s IS NOT NULL", queryItem.getColumn().getName()));
                } else {
                    throw new IllegalArgumentException(String.format("Unsupported operator %s for comparison with NULL", queryItem.getOperator().name()));
                }
            } else {
                if (queryItem.getOperator() == QueryItem.Operator.NEQ && queryItem.isIncludeNulls()) {
                    sb.append(String.format("(%s %s :q%d OR %s IS NULL)", queryItem.getColumn().getName(), queryItem.getOperator().getSymbol(), i, queryItem.getColumn().getName()));
                } else {
                    sb.append(String.format("%s %s :q%d", queryItem.getColumn().getName(), queryItem.getOperator().getSymbol(), i));
                }
            }
            if (queryItem.getValue() != null) {
                if (queryItem.getValue() instanceof BaseDatabaseEnum<?>) {
                    params.addValue(String.format("q%d", i), ((BaseDatabaseEnum<?>) queryItem.getValue()).getId());
                } else {
                    params.addValue(String.format("q%d", i), queryItem.getValue());
                }
            }
        }
        sb.append(" ");
        String sql = String.format(getQuerySql(), sb, getSelectAllDefaultMaxCount() + 1);
        List<T> result = jdbcTemplate.query(sql, params, getRowMapper());
        ensureMaxCountNotExceeded(getSelectAllDefaultMaxCount(), result);
        return result;
    }

    protected abstract String getQuerySql();
    
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

    protected abstract String getLockSql();

    @SuppressWarnings("unchecked")
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

    private void setCreatedAt(T object) {
        if (object instanceof CreatedAtTracked<?>) {
            ((CreatedAtTracked<?>) object).setCreatedNow();
        }
    }

    private void setChangedAt(T object) {
        if (object instanceof ChangedAtTracked<?>) {
            ((ChangedAtTracked<?>) object).setChangedNow();
        }
    }

    @SuppressWarnings("unchecked")
    private void bumpVersion(T object) {
        if (object instanceof VersionTracked) {
            VersionTracked versionTracked = (VersionTracked) object;
            Integer v = versionTracked.getVersion();
            if (v == null) {
                // The user hasn't supplied a version value.
                // Might be a migrated existing object where version was never set.
                versionTracked.setVersion(0);
            } else {
                versionTracked.setVersion((v + 1) % 128);
            }
        }
    }

    private void initializeVersion(T object) {
        if (object instanceof VersionTracked) {
            VersionTracked versionTracked = (VersionTracked) object;
            versionTracked.setVersion(0);
        }
    }

}