package se.plilja.springdaogen.generatedframework

import se.plilja.springdaogen.model.Config


fun baseTestRepository(config: Config): Pair<String, String> {
    val extraImports = if (config.frameworkOutputPackage != config.testRepositoryOutputPackage) {
        """
import ${config.frameworkOutputPackage}.BaseEntity;
import ${config.frameworkOutputPackage}.BaseRepository;
import ${config.frameworkOutputPackage}.MaxAllowedCountExceededException;
        """.trimIndent()
    } else {
        ""
    }
    return Pair(
        "BaseTestRepository", """
package ${config.testRepositoryOutputPackage!!};

import org.springframework.dao.EmptyResultDataAccessException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
$extraImports

import static java.util.stream.Collectors.toList;

public abstract class BaseTestRepository<T extends BaseEntity<ID>, ID extends Comparable<ID>> implements BaseRepository<T, ID> {

    private final ConcurrentSkipListMap<ID, T> table = new ConcurrentSkipListMap<>();
    private final AtomicInteger idSequence = new AtomicInteger(0);
    private final boolean idIsGenerated;
    private final int defaultMaxAllowedCount;
    private final boolean updatesAreSupported;

    protected BaseTestRepository(boolean idIsGenerated, int defaultMaxAllowedCount, boolean updatesAreSupported) {
        this.idIsGenerated = idIsGenerated;
        this.defaultMaxAllowedCount = defaultMaxAllowedCount;
        this.updatesAreSupported = updatesAreSupported;
    }

    @Override
    public boolean existsById(ID id) {
        return table.containsKey(id);
    }

    @Override
    public T getOne(ID id) {
        if (existsById(id)) {
            return table.get(id);
        } else {
            throw new EmptyResultDataAccessException(1);
        }
    }

    @Override
    public Optional<T> findOneById(ID id) {
        return Optional.ofNullable(table.getOrDefault(id, null));
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        List<T> result = new ArrayList<>();
        TreeSet<ID> unique = new TreeSet<>();
        for (ID id : ids) {
            unique.add(id);
        }
        for (ID id : unique) {
            findOneById(id).ifPresent(result::add);
        }
        return result;
    }

    @Override
    public List<T> findPage(long start, int pageSize) {
        return table.values().stream()
                .skip(start)
                .limit(pageSize)
                .collect(toList());
    }

    public void clear() {
        table.clear();
    }

    @Override
    public List<T> findAll() {
        return findAll(defaultMaxAllowedCount);
    }

    @Override
    public List<T> findAll(int maxAllowedCount) {
        if (table.size() > maxAllowedCount) {
            throw new MaxAllowedCountExceededException(String.format("Max allowed count of %d exceeded", maxAllowedCount));
        } else {
            return new ArrayList<>(table.values());
        }
    }

    /**
     * @noinspection unchecked
     */
    @Override
    public void save(T object) {
        T copiedObject = makeCopy(object);
        if (copiedObject.getId() == null) {
            if (idIsGenerated) {
                copiedObject.setId((ID) (Integer) idSequence.getAndIncrement());
            } else {
                throw new IllegalArgumentException("Must provide primary key when saving copiedObject");
            }
        } else if (!updatesAreSupported && existsById(copiedObject.getId())) {
            throw new UnsupportedOperationException("Updates are not supported");
        }
        table.put(copiedObject.getId(), copiedObject);
        object.setId(copiedObject.getId());
    }

    /**
     * @noinspection unchecked
     */
    private T makeCopy(T object) {
        try {
            // Some dirty reflection to avoid having to pollute the
            // prod entities with cloning support.
            // TODO is it useful if the prod entities supported cloning?
            Class<? extends BaseEntity> clazz = object.getClass();
            T result = (T) clazz.getConstructor(null).newInstance();
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(result, field.get(object));
            }
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(T object) {
        deleteById(object.getId());
    }

    @Override
    public void deleteById(ID id) {
        table.remove(id);
    }

    @Override
    public void deleteAll(Iterable<T> objects) {
        for (T object : objects) {
            delete(object);
        }
    }

    @Override
    public long count() {
        return (long) table.size();
    }
}
        """.trimIndent()
    )
}
