package se.plilja.springdaogen.generatedframework


fun baseRepository(_package: String): Pair<String, String> {
    return Pair(
        "BaseRepository", """
package $_package;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T extends BaseEntity<ID>, ID> {

    boolean existsById(ID id);

    T getOne(ID id);

    Optional<T> findOneById(ID id);

    List<T> findAllById(Iterable<ID> ids);

    List<T> findPage(long start, int pageSize);

    List<T> findAll();

    List<T> findAll(int maxAllowedCount);

    void save(T object);

    void delete(T object);

    void deleteById(ID id);

    void deleteAll(Iterable<T> objects);

    long count();
}
    """.trimIndent()
    )
}
