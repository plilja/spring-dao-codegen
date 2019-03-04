package se.plilja.springdaogen.generatedframework

import se.plilja.springdaogen.model.Config


fun queryable(_package: String, config: Config): Pair<String, String> {
    return Pair(
            "Queryable", """
package $_package;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This class provides query access to a table or view
 * in the database.
 */
public abstract class Queryable<T> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    protected Queryable(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long count() {
        String sql = getCountSql();
        HashMap<String, Object> noParams = new HashMap<>();
        return jdbcTemplate.queryForObject(sql, noParams, Long.class);
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

    /**
     * Find one element using a query. Fails
     * if more than one element is found.
     *
     * @throws IllegalArgumentException if more than one element was found
     */
    public Optional<T> findOne(QueryItem<T> queryItem) throws IllegalArgumentException {
        return findOne(Collections.singletonList(queryItem));
    }

    /**
     * Find one element using a query. Fails
     * if more than one element is found.
     *
     * @throws IllegalArgumentException if more than one element was found
     */
    public Optional<T> findOne(List<QueryItem<T>> queryItems) throws IllegalArgumentException {
        List<T> results = findAll(queryItems);
        if (results.size() > 1) {
            throw new IllegalArgumentException("More than one row available, expected exactly one");
        } else if (results.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(results.get(0));
        }
    }

    /**
     * Find exactly one element using a findAllOrderBy. Fails
     * if zero elements or more than one element is found.
     *
     * @throws IllegalArgumentException if more than one element was found
     * @throws NoSuchElementException   if no element was found
     */
    public T getOne(QueryItem<T> queryItem) throws IllegalArgumentException, NoSuchElementException {
        return getOne(Collections.singletonList(queryItem));
    }

    /**
     * Find exactly one element using a query. Fails
     * if zero elements or more than one element is found.
     *
     * @throws IllegalArgumentException if more than one element was found
     * @throws NoSuchElementException   if no element was found
     */
    public T getOne(List<QueryItem<T>> queryItems) throws IllegalArgumentException, NoSuchElementException {
        return findOne(queryItems)
                .orElseThrow(() -> new NoSuchElementException("No matching row was found"));
    }

    public List<T> findAllOrderBy(SortOrder<T> orderBy) {
        return findAllOrderBy(Collections.emptyList(), Collections.singletonList(orderBy));
    }

    public List<T> findAllOrderBy(List<SortOrder<T>> orderBy) {
        return findAllOrderBy(Collections.emptyList(), orderBy);
    }

    public List<T> findAll(QueryItem<T> queryItem) {
        return findAll(Collections.singletonList(queryItem));
    }

    public List<T> findAll(List<QueryItem<T>> queryItems) {
        return findAllOrderBy(queryItems, Collections.emptyList());
    }

    public List<T> findAllOrderBy(QueryItem<T> queryItem, SortOrder<T> orderBy) {
        return findAllOrderBy(Collections.singletonList(queryItem), Collections.singletonList(orderBy));
    }

    public List<T> findAllOrderBy(List<QueryItem<T>> queryItems, SortOrder<T> orderBy) {
        return findAllOrderBy(queryItems, Collections.singletonList(orderBy));
    }

    public List<T> findAllOrderBy(List<QueryItem<T>> queryItems, List<SortOrder<T>> orderBy) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder whereClause = getWhereClause(queryItems, params);
        StringBuilder orderByClause = getOrderByClause(orderBy);
        String sql = getQueryOrderBySql(getSelectAllDefaultMaxCount() + 1, whereClause.toString(), orderByClause.toString());
        List<T> result = jdbcTemplate.query(sql, params, getRowMapper());
        ensureMaxCountNotExceeded(getSelectAllDefaultMaxCount(), result);
        return result;
    }

    public List<T> findPageOrderBy(long start, int pageSize, SortOrder<T> orderBy) {
        return findPageOrderBy(start, pageSize, Collections.emptyList(), Collections.singletonList(orderBy));
    }

    public List<T> findPageOrderBy(long start, int pageSize, List<SortOrder<T>> orderBy) {
        return findPageOrderBy(start, pageSize, Collections.emptyList(), orderBy);
    }

    public List<T> findPageOrderBy(long start, int pageSize, QueryItem<T> queryItem, SortOrder<T> orderBy) {
        return findPageOrderBy(start, pageSize, Collections.singletonList(queryItem), Collections.singletonList(orderBy));
    }

    public List<T> findPageOrderBy(long start, int pageSize, List<QueryItem<T>> queryItems, SortOrder<T> orderBy) {
        return findPageOrderBy(start, pageSize, queryItems, Collections.singletonList(orderBy));
    }

    public List<T> findPageOrderBy(long start, int pageSize, List<QueryItem<T>> queryItems, List<SortOrder<T>> orderBy) {
        if (pageSize <= 0) {
            return Collections.emptyList();
        }
        if (orderBy.isEmpty()) {
            throw new IllegalArgumentException("Must supply order by when querying for a page");
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder whereClause = getWhereClause(queryItems, params);
        StringBuilder orderByClause = getOrderByClause(orderBy);
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
                    .map(s -> s.getColumn().getEscapedColumnName() + " " + s.getOrder())
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

    private void ensureMaxCountNotExceeded(int maxAllowedCount, List<T> result) {
        // If queries have been correctly generated it should never be possible to select
        // more than maxAllowedCount + 1 even if the table is larger than that
        assert result.size() <= maxAllowedCount + 1;
        if (result.size() > maxAllowedCount) {
            throw new TooManyRowsAvailableException(String.format("Max allowed count of %d rows exceeded", maxAllowedCount));
        }
    }

    /**
     * Find a column by it's database name.
     * Returns null if no matching column was found.
     * <p/>
     * Comparison is case sensitive.
     */
    public Column<T, ?> getColumnByName(String name) {
        for (Column<T, ?> column : getColumnsList()) {
            if (column.getColumnName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    /**
     * Find a column by it's database name.
     * Returns null if no matching column was found.
     * <p/>
     * Comparison is case insensitive.
     */
    public Column<T, ?> getColumnByNameIgnoreCase(String name) {
        for (Column<T, ?> column : getColumnsList()) {
            if (column.getColumnName().equalsIgnoreCase(name)) {
                return column;
            }
        }
        return null;
    }

    /**
     * Find a column by it's name in the Java code.
     * Returns null if no matching column was found.
     * <p/>
     * Comparison is case sensitive.
     */
    public Column<T, ?> getColumnByFieldName(String name) {
        for (Column<T, ?> column : getColumnsList()) {
            if (column.getFieldName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    /**
     * Find a column by it's name in the Java code.
     * Returns null if no matching column was found.
     * <p/>
     * Comparison is case insensitive.
     */
    public Column<T, ?> getColumnByFieldNameIgnoreCase(String name) {
        for (Column<T, ?> column : getColumnsList()) {
            if (column.getFieldName().equalsIgnoreCase(name)) {
                return column;
            }
        }
        return null;
    }

    protected abstract List<Column<T, ?>> getColumnsList();

    protected abstract RowMapper<T> getRowMapper();

    /**
     * Max allowed count when selecting all objects. Protects against
     * unexpectedly large queries that may cause performance degradation.
     */
    protected abstract int getSelectAllDefaultMaxCount();

    protected abstract String getCountSql();

    protected abstract String getSelectManySql(int maxAllowedCount);

    protected abstract String getQueryPageOrderBySql(long start, int pageSize, String whereClause, String orderBy);

    protected abstract String getQueryOrderBySql(int maxAllowedCount, String whereClause, String orderBy);

    private static class ParamGenerator implements Supplier<String> {
        private int counter = 0;

        @Override
        public String get() {
            return String.format("q%d", counter++);
        }
    }
}
""".trimIndent())
}
