package db.h2;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Base class providing CRUD access to a database
 * table.
 */
public abstract class Dao<T extends BaseEntity<ID>, ID> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Class<T> entityClass;
    private final Class<ID> idClass;
    private boolean databaseProductNameInitialized = false;
    private String databaseProductName = null;
    private final boolean idIsGenerated;

    protected Dao(Class<T> entityClass, Class<ID> idClass, boolean idIsGenerated, NamedParameterJdbcTemplate jdbcTemplate) {
        this.entityClass = entityClass;
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

    /**
     * Select and also lock a row in the database.
     * Only use it if you know that you actually
     * want to lock the row. If doubtful, use the
     * {@link #getOne(ID)} or {@link #findOne(ID)} instead.
     * <p/>
     * Note that this method must be called in a @Transaction
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
    public T getOneAndLock(ID id) {
        String sql = getSelectAndLockSql(getDatabaseProductName());
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.queryForObject(sql, params, getRowMapper());
    }

    /**
     * Finds all rows. Note that this will throw a
     * {@link TooManyRowsAvailableException} if the table/view
     * is larger than expected. If you expect many rows to be
     * returned, you should either use one of the findPage-methods
     * or if you know that the table will fit in memory
     * you can also use {@link #findAll(int)} or reconfigure
     * the limit for when to throw the exception.
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
        if (pageSize <= 0) {
            return Collections.emptyList();
        }
        String sql = getSelectPageSql(start, pageSize);
        return jdbcTemplate.query(sql, Collections.emptyMap(), getRowMapper());
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
        String sql = getUpdateSql(object);
        SqlParameterSource params = getParams(object);
        int updated = jdbcTemplate.update(sql, params);
        if (updated == 0) {
            throw new NoRowsUpdatedException(String.format("No rows affected when trying to update object with id %s", object.getId()));
        } else if (updated > 1) {
            throw new TooManyRowsUpdatedException(String.format("More than one row (%d) affected by update of object with id %s", updated, object.getId()));
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

    protected abstract int getSelectAllDefaultMaxCount();

    protected abstract RowMapper<T> getRowMapper();

    protected abstract SqlParameterSource getParams(T object);

    protected abstract String getExistsByIdSql();

    protected abstract String getSelectIdsSql();

    protected abstract String getSelectManySql(int maxAllowedCount);

    protected abstract String getInsertSql();

    protected abstract String getUpdateSql(T object);

    protected abstract String getPrimaryKeyColumnName();

    protected abstract String getDeleteSql();

    protected abstract String getCountSql();

    protected abstract String getSelectAndLockSql(String databaseProductName);

    protected abstract String getSelectPageSql(long start, int pageSize);

    public Class<T> getEntityClass() {
        return entityClass;
    }

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

    private String getDatabaseProductName() {
        if (!databaseProductNameInitialized) {
            DataSource dataSource = jdbcTemplate.getJdbcTemplate().getDataSource();
            try (Connection connection = dataSource.getConnection()) {
                databaseProductName = connection.getMetaData().getDatabaseProductName();
            } catch (SQLException | RuntimeException ex) {
                throw new DatabaseException("Error retrieving database product name", ex);
            }
            databaseProductNameInitialized = true;
        }
        return databaseProductName;
    }

}