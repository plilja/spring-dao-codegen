package se.plilja.springdaogen.generatedframework

import se.plilja.springdaogen.model.Config


fun dao(_package: String, config: Config): Pair<String, String> {
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
${if (config.featureGenerateQueryApi) {
            """
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

"""
        } else {
            ""
        }
        }
/**
 * Base class providing basic CRUD access to a database
 * table.
 */
public abstract class Dao<T extends BaseEntity<ID>, ID> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Class<ID> idClass;
    private final boolean idIsGenerated;
${if (config.featureGenerateChangeTracking) {
            """    private final CurrentUserProvider currentUserProvider;"""
        } else {
            ""
        }
        }

    protected Dao(Class<ID> idClass, boolean idIsGenerated, NamedParameterJdbcTemplate jdbcTemplate${if (config.featureGenerateChangeTracking) ", CurrentUserProvider currentUserProvider" else ""}) {
        this.idClass = idClass;
        this.idIsGenerated = idIsGenerated;
        this.jdbcTemplate = jdbcTemplate;${if (config.featureGenerateChangeTracking) {
            """
        this.currentUserProvider = currentUserProvider;"""
        } else ""}
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
        String sql = getSelectAndLockSql();
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.queryForObject(sql, params, getRowMapper());
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
${if (config.featureGenerateQueryApi) {
            """        return queryForPage(start, pageSize, Collections.emptyList(), Collections.emptyList());"""
        } else {
            """        if (pageSize <= 0) {
            return Collections.emptyList();
        }
        String sql = getSelectPageSql(start, pageSize);
        return jdbcTemplate.query(sql, Collections.emptyMap(), getRowMapper());"""
        }}
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
${if (config.featureGenerateChangeTracking) {
            """        setCreatedAt(object);
        setCreatedBy(object);
        setChangedAt(object);
        setChangedBy(object);
        initializeVersion(object);
"""
        } else {
            ""
        }
        }
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
${if (config.featureGenerateChangeTracking) {
            """        setChangedAt(object);
        setChangedBy(object);"""
        } else {
            ""
        }
        }
        String sql = getUpdateSql();
        SqlParameterSource params = getParams(object);
        int updated = jdbcTemplate.update(sql, params);
        if (updated == 0) {
            throw new NoRowsUpdatedException(String.format("No rows affected when trying to update object with id %s", object.getId()));
        } else if (updated > 1) {
            throw new TooManyRowsUpdatedException(String.format("More than one row (%d) affected by update of object with id %s", updated, object.getId()));
        }
${if (config.featureGenerateChangeTracking) {
            """
        bumpVersion(object);
"""
        } else {
            ""
        }
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

    public long count() {
        String sql = getCountSql();
        HashMap<String, Object> noParams = new HashMap<>();
        return jdbcTemplate.queryForObject(sql, noParams, Long.class);
    }

${if (config.featureGenerateQueryApi) {
            """
    public List<T> findAll(SortOrder<T> orderBy) {
        return query(Collections.emptyList(), Collections.singletonList(orderBy));
    }

    public List<T> findAll(List<SortOrder<T>> orderBy) {
        return query(Collections.emptyList(), orderBy);
    }

    public List<T> query(QueryItem<T> queryItem) {
        return query(Collections.singletonList(queryItem));
    }

    public List<T> query(List<QueryItem<T>> queryItems) {
        return query(queryItems, Collections.emptyList());
    }

    public List<T> query(QueryItem<T> queryItem, SortOrder<T> orderBy) {
        return query(Collections.singletonList(queryItem), Collections.singletonList(orderBy));
    }

    public List<T> query(List<QueryItem<T>> queryItems, SortOrder<T> orderBy) {
        return query(queryItems, Collections.singletonList(orderBy));
    }

    public List<T> query(List<QueryItem<T>> queryItems, List<SortOrder<T>> orderBy) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder whereClause = getWhereClause(queryItems, params);
        StringBuilder orderByClause = getOrderByClause(orderBy);
        String sql = getQueryOrderBySql(getSelectAllDefaultMaxCount() + 1, whereClause.toString(), orderByClause.toString());
        List<T> result = jdbcTemplate.query(sql, params, getRowMapper());
        ensureMaxCountNotExceeded(getSelectAllDefaultMaxCount(), result);
        return result;
    }

    public List<T> findPage(long start, int pageSize, SortOrder<T> orderBy) {
        return queryForPage(start, pageSize, Collections.emptyList(), Collections.singletonList(orderBy));
    }

    public List<T> findPage(long start, int pageSize, List<SortOrder<T>> orderBy) {
        return queryForPage(start, pageSize, Collections.emptyList(), orderBy);
    }

    public List<T> queryForPage(long start, int pageSize, QueryItem<T> queryItem) {
        return queryForPage(start, pageSize, Collections.singletonList(queryItem));
    }

    public List<T> queryForPage(long start, int pageSize, List<QueryItem<T>> queryItems) {
        return queryForPage(start, pageSize, queryItems, Collections.emptyList());
    }

    public List<T> queryForPage(long start, int pageSize, QueryItem<T> queryItem, SortOrder<T> orderBy) {
        return queryForPage(start, pageSize, Collections.singletonList(queryItem), Collections.singletonList(orderBy));
    }

    public List<T> queryForPage(long start, int pageSize, List<QueryItem<T>> queryItems, SortOrder<T> orderBy) {
        return queryForPage(start, pageSize, queryItems, Collections.singletonList(orderBy));
    }

    public List<T> queryForPage(long start, int pageSize, List<QueryItem<T>> queryItems, List<SortOrder<T>> orderBy) {
        if (pageSize <= 0) {
            return Collections.emptyList();
        }
        List<SortOrder<T>> adjustedSortOrder;
        if (orderBy.isEmpty()) {
            adjustedSortOrder = Collections.singletonList(SortOrder.asc(getColumnByName(getPrimaryKeyColumnName())));
        } else {
            adjustedSortOrder = orderBy;
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder whereClause = getWhereClause(queryItems, params);
        StringBuilder orderByClause = getOrderByClause(adjustedSortOrder);
        String sql = getQueryPageOrderBySql(start, pageSize, whereClause.toString(), orderByClause.toString());
        List<T> result = jdbcTemplate.query(sql, params, getRowMapper());
        ensureMaxCountNotExceeded(pageSize, result);
        return result;
    }

    private StringBuilder getOrderByClause(List<SortOrder<T>> orderBy) {
        StringBuilder orderByClause = new StringBuilder();
        if (!orderBy.isEmpty()) {
            orderByClause.append("ORDER BY ");
            List<String> columnNames = orderBy.stream()
                    .map(s -> s.getColumn().getName() + " " + s.getOrder())
                    .collect(Collectors.toList());
            orderByClause.append(String.join(", ", columnNames));
        }
        return orderByClause;
    }

    private StringBuilder getWhereClause(List<QueryItem<T>> queryItems, MapSqlParameterSource params) {
        StringBuilder whereClause = new StringBuilder(" ");
        ParamGenerator paramGenerator = new ParamGenerator();
        for (QueryItem<T> queryItem : queryItems) {
            whereClause.append(" AND ");
            whereClause.append(queryItem.getClause(params, paramGenerator));
        }
        whereClause.append(" ");
        return whereClause;
    }

    private static class ParamGenerator implements Supplier<String> {
        private int counter = 0;

        @Override
        public String get() {
            return String.format("q%d", counter++);
        }
    }

    public abstract Column<T, ?> getColumnByName(String name);

    protected abstract String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy);

    protected abstract String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy);
"""
        } else {
            """    protected abstract String getSelectPageSql(long start, int pageSize);
"""
        }}

    protected abstract RowMapper<T> getRowMapper();

    protected abstract SqlParameterSource getParams(T object);

    protected abstract String getExistsByIdSql();

    protected abstract String getSelectIdsSql();

    protected abstract String getSelectManySql(int maxAllowedCount);

    protected abstract String getInsertSql();

    protected abstract String getUpdateSql();

    protected abstract String getPrimaryKeyColumnName();

    protected abstract String getDeleteSql();

    protected abstract String getCountSql();

    protected abstract String getSelectAndLockSql();

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
${if (config.featureGenerateChangeTracking) {
            """
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

    private void setCreatedBy(T object) {
        if (object instanceof CreatedByTracked) {
            ((CreatedByTracked) object).setCreatedBy(currentUserProvider.getCurrentUser());
        }
    }

    private void setChangedBy(T object) {
        if (object instanceof ChangedByTracked) {
            ((ChangedByTracked) object).setChangedBy(currentUserProvider.getCurrentUser());
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
"""
        } else {
            ""
        }
        }
}
    """.trimIndent()
    )
}