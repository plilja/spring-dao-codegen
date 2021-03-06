package dbtests;

import dbtests.framework.BaseEntity;
import dbtests.framework.Column;
import dbtests.framework.Dao;
import dbtests.framework.QueryItem;
import dbtests.framework.TooManyRowsAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static dbtests.framework.QueryItem.contains;
import static dbtests.framework.QueryItem.endsWith;
import static dbtests.framework.QueryItem.eq;
import static dbtests.framework.QueryItem.gt;
import static dbtests.framework.QueryItem.gte;
import static dbtests.framework.QueryItem.in;
import static dbtests.framework.QueryItem.lt;
import static dbtests.framework.QueryItem.lte;
import static dbtests.framework.QueryItem.neq;
import static dbtests.framework.QueryItem.notIn;
import static dbtests.framework.QueryItem.or;
import static dbtests.framework.QueryItem.startsWith;
import static dbtests.framework.SortOrder.asc;
import static dbtests.framework.SortOrder.desc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @noinspection unchecked
 */
public abstract class QueryApiBaseTest<Entity extends BaseEntity<Integer>, Repo extends Dao<Entity, Integer>> {

    @BeforeEach
    void before() {
        clearTable();
    }

    protected abstract Repo getRepo();

    protected abstract void clearTable();

    protected abstract Entity newEntity(String name, String color);

    protected abstract Entity newEntity(String name);

    protected abstract String getName(Entity entity);

    protected abstract void setName(Entity entity, String name);

    protected abstract void setColor(Entity entity, String color);

    protected abstract Object getColor(Entity entity);

    protected abstract Object getColorByName(String color);

    protected abstract Column<Entity, ?> getColorColumn();

    protected abstract Column<Entity, Integer> getIdColumn();

    protected abstract Column<Entity, String> getNameColumn();

    @Test
    void singleAttributeQuery() {
        var entity1 = newEntity("Phil");
        getRepo().save(entity1);
        var entity2 = newEntity("David");
        getRepo().save(entity2);
        var entity3 = newEntity("David");
        getRepo().save(entity3);
        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        var result = getRepo().findAll(eq(getNameColumn(), "David"));

        result.sort(Comparator.comparing(BaseEntity::getId)); // No guaranty about sort order from db

        // then
        assertEquals(2, result.size());
        assertEquals(entity2.getId(), result.get(0).getId());
        assertEquals(entity3.getId(), result.get(1).getId());
    }

    @Test
    void multiAttributeQuery() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        setColor(entity2, "RED");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        setColor(entity4, "BLUE");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAll(List.of(
                eq(getNameColumn(), "David"),
                eq((Column) getColorColumn(), getColorByName("BLUE"))
        ));

        // then
        assertEquals(1, result.size());
        assertEquals(entity3.getId(), result.get(0).getId());
    }

    @Test
    void queryForNull() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAllOrderBy(
                eq(getColorColumn(), null),
                asc(getIdColumn())
        );

        // then
        assertEquals(2, result.size());
        assertEquals(entity2.getId(), result.get(0).getId());
        assertEquals(entity4.getId(), result.get(1).getId());
    }

    @Test
    void queryWithNeq() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAll(List.of(
                neq(getNameColumn(), "David", true),
                neq((Column) getColorColumn(), getColorByName("GREEN"), true)
        ));

        // then
        assertEquals(1, result.size());
        assertEquals(entity4.getId(), result.get(0).getId());
    }

    @Test
    void queryWithLT() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAll(lt(getIdColumn(), entity3.getId()));

        // then
        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    @Test
    void queryWithLTE() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAll(lte(getIdColumn(), entity2.getId()));

        // then
        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    @Test
    void queryWithGT() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAll(gt(getIdColumn(), entity2.getId()));

        // then
        assertEquals(2, result.size());
        assertEquals(entity3.getId(), result.get(0).getId());
        assertEquals(entity4.getId(), result.get(1).getId());
    }

    @Test
    void queryWithGTE() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAll(gte(getIdColumn(), entity3.getId()));

        // then
        assertEquals(2, result.size());
        assertEquals(entity3.getId(), result.get(0).getId());
        assertEquals(entity4.getId(), result.get(1).getId());
    }

    @Test
    void queryWithStartsWith() {
        var entity1 = newEntity("Davide");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("Dave");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        List<Entity> result1 = getRepo().findAll(startsWith(getNameColumn(), ""));
        assertEquals(4, result1.size());

        List<Entity> result2 = getRepo().findAll(startsWith(getNameColumn(), "D"));
        assertEquals(3, result2.size());

        List<Entity> result3 = getRepo().findAllOrderBy(startsWith(getNameColumn(), "David"), asc(getNameColumn()));
        assertEquals(2, result3.size());
        assertEquals(entity2.getId(), result3.get(0).getId());
        assertEquals(entity1.getId(), result3.get(1).getId());

        List<Entity> result4 = getRepo().findAll(startsWith(getNameColumn(), "Dave"));
        assertEquals(1, result4.size());
        assertEquals(entity3.getId(), result4.get(0).getId());
    }

    @Test
    void queryWithEndsWith() {
        var entity1 = newEntity("Davide");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("Dave");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        List<Entity> result1 = getRepo().findAll(endsWith(getNameColumn(), ""));
        assertEquals(4, result1.size());

        List<Entity> result2 = getRepo().findAllOrderBy(endsWith(getNameColumn(), "e"), asc(getNameColumn()));
        assertEquals(2, result2.size());
        assertEquals(entity3.getId(), result2.get(0).getId());
        assertEquals(entity1.getId(), result2.get(1).getId());

        List<Entity> result3 = getRepo().findAll(endsWith(getNameColumn(), "Dave"));
        assertEquals(1, result3.size());
        assertEquals(entity3.getId(), result3.get(0).getId());
    }

    @Test
    void queryWithContains() {
        var entity1 = newEntity("Davide");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("Dave");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        List<Entity> result1 = getRepo().findAll(contains(getNameColumn(), ""));
        assertEquals(4, result1.size());

        List<Entity> result2 = getRepo().findAllOrderBy(contains(getNameColumn(), "v"), asc(getNameColumn()));
        assertEquals(3, result2.size());
        assertEquals(entity3.getId(), result2.get(0).getId());
        assertEquals(entity2.getId(), result2.get(1).getId());
        assertEquals(entity1.getId(), result2.get(2).getId());

        List<Entity> result3 = getRepo().findAll(contains(getNameColumn(), "Chris"));
        assertEquals(1, result3.size());
        assertEquals(entity4.getId(), result3.get(0).getId());
    }

    @Test
    void queryWithOR() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAllOrderBy(
                or(
                        eq(getIdColumn(), entity1.getId()),
                        eq(getIdColumn(), entity3.getId())
                ),
                asc(getIdColumn())
        );

        // then
        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity3.getId(), result.get(1).getId());
    }

    @Test
    void queryWithORAndOtherClause() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAllOrderBy(
                List.of(or(
                        eq(getIdColumn(), entity1.getId()),
                        eq(getIdColumn(), entity3.getId())
                        ),
                        eq(getNameColumn(), "David")
                ),
                asc(getIdColumn())
        );

        // then
        assertEquals(1, result.size());
        assertEquals(entity3.getId(), result.get(0).getId());
        assertEquals("David", getName(result.get(0)));
    }

    @Test
    void queryWithIn() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAllOrderBy(
                in(getNameColumn(), List.of("Chris", "Phil")),
                asc(getNameColumn())
        );

        // then
        assertEquals(2, result.size());
        assertEquals(entity4.getId(), result.get(0).getId());
        assertEquals(entity1.getId(), result.get(1).getId());
    }

    @Test
    void queryWithNotIn() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAllOrderBy(
                notIn(getIdColumn(), List.of(entity2.getId(), entity4.getId())),
                asc(getIdColumn())
        );

        // then
        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity3.getId(), result.get(1).getId());
    }

    @Test
    void queryWithInEmptyList() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAllOrderBy(
                in(getNameColumn(), List.of()),
                asc(getNameColumn())
        );

        // then
        assertEquals(List.of(), result);
    }

    @Test
    void queryWithNotInEmptyList() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAllOrderBy(
                notIn(getIdColumn(), List.of()),
                asc(getIdColumn())
        );

        // then
        assertEquals(4, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
        assertEquals(entity3.getId(), result.get(2).getId());
        assertEquals(entity4.getId(), result.get(3).getId());
    }

    @Test
    void shouldRespectMaxSelectCount() {
        for (int i = 1; i <= 10; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
        }
        var res = getRepo().findAll(gte(getIdColumn(), 0));
        assertEquals(10, res.size()); // Should be fine as we are below limit

        var entity = newEntity("Name 11");
        getRepo().save(entity);

        assertThrows(TooManyRowsAvailableException.class, () -> {
            getRepo().findAll(gte(getIdColumn(), 0));
        });
    }

    @Test
    void queryWithNoArgs() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAll(Collections.emptyList());

        result.sort(Comparator.comparing(BaseEntity::getId)); // No guaranty about sort order from DB

        // then
        assertEquals(4, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
        assertEquals(entity3.getId(), result.get(2).getId());
        assertEquals(entity4.getId(), result.get(3).getId());
    }

    @Test
    void queryWithOrderByAsc() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        setColor(entity2, "RED");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        setColor(entity4, "GREEN");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAllOrderBy(
                eq((Column) getColorColumn(), getColorByName("GREEN")),
                asc(getNameColumn())
        );

        // then
        assertEquals(2, result.size());
        assertEquals(entity4.getId(), result.get(0).getId());
        assertEquals(entity1.getId(), result.get(1).getId());
    }

    @Test
    void queryWithOrderByDesc() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        setColor(entity2, "RED");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        setColor(entity4, "GREEN");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAllOrderBy(
                eq((Column) getColorColumn(), getColorByName("GREEN")),
                desc(getNameColumn())
        );

        // then
        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity4.getId(), result.get(1).getId());
    }

    @Test
    void findAllWithOrderBy() {
        var entity1 = newEntity("Phil");
        setColor(entity1, "GREEN");
        getRepo().save(entity1);

        var entity2 = newEntity("David");
        setColor(entity2, "RED");
        getRepo().save(entity2);

        var entity3 = newEntity("David");
        setColor(entity3, "BLUE");
        getRepo().save(entity3);

        var entity4 = newEntity("Chris");
        setColor(entity4, "GREEN");
        getRepo().save(entity4);

        // when
        List<Entity> result = getRepo().findAllOrderBy(
                asc(getNameColumn())
        );

        // then
        assertEquals(4, result.size());
        assertEquals("Chris", getName(result.get(0)));
        assertEquals("David", getName(result.get(1)));
        assertEquals("David", getName(result.get(2)));
        assertEquals("Phil", getName(result.get(3)));
    }

    @Test
    void findAllWithOrderByWhenTooManyRowsExceeded() {
        for (int i = 0; i < 11; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
        }

        // when, then
        assertThrows(TooManyRowsAvailableException.class, () -> getRepo().findAllOrderBy(asc(getNameColumn())));
    }

    @Test
    void queryWhenByTooManyRowsExceeded() {
        for (int i = 0; i < 11; i++) {
            var entity = newEntity(String.format("Name %d", i));
            setColor(entity, "BLUE");
            getRepo().save(entity);
        }

        // when, then
        assertThrows(TooManyRowsAvailableException.class, () -> {
            getRepo().findAll(eq((Column) getColorColumn(), getColorByName("BLUE")));
        });
    }

    @Test
    void queryForPage() {
        for (int i = 0; i < 42; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
            if (entity.getId() % 2 == 0) {
                setColor(entity, "BLUE");
            } else {
                setColor(entity, "GREEN");
            }
            getRepo().save(entity);
        }

        // First page
        List<Entity> firstPage = getRepo().findPage(0, 10, List.of(eq((Column) getColorColumn(), getColorByName("BLUE"))));
        assertEquals(10, firstPage.size());
        for (var entity : firstPage) {
            assertEquals(0, entity.getId() % 2);
            assertEquals(getColorByName("BLUE"), getColor(entity));
        }

        // Second page
        List<Entity> secondPage = getRepo().findPage(10, 10, List.of(eq((Column) getColorColumn(), getColorByName("BLUE"))));
        assertEquals(10, secondPage.size());
        for (var entity : secondPage) {
            assertTrue(entity.getId() >= 20);
            assertEquals(0, entity.getId() % 2);
            assertEquals(getColorByName("BLUE"), getColor(entity));
        }

        // Third page
        var thirdPage = getRepo().findPage(20, 10, List.of(eq((Column) getColorColumn(), getColorByName("BLUE"))));
        assertEquals(1, thirdPage.size());
    }

    @Test
    void findPageWithSortOrder() {
        for (int i = 0; i < 12; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
        }

        // First page
        List<Entity> firstPage = getRepo().findPageOrderBy(0, 10, asc(getIdColumn()));
        assertEquals(10, firstPage.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(String.format("Name %d", i), getName(firstPage.get(i)));
        }

        List<Entity> secondPage = getRepo().findPageOrderBy(10, 10, asc(getIdColumn()));
        assertEquals(2, secondPage.size());
        assertEquals("Name 10", getName(secondPage.get(0)));
        assertEquals("Name 11", getName(secondPage.get(1)));
    }

    @Test
    void getOneByQuery() {
        for (int i = 0; i < 12; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
        }

        // when
        Entity entity = getRepo().getOne(QueryItem.eq(getNameColumn(), "Name 5"));

        // then
        assertNotNull(entity);
        assertEquals("Name 5", getName(entity));
    }

    @Test
    void getOneByQueryMoreThanOne() {
        for (int i = 0; i < 12; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
        }

        assertThrows(IllegalArgumentException.class, () -> {
            getRepo().getOne(QueryItem.gt(getNameColumn(), "Name 5"));

        });
    }

    @Test
    void getOneNothingFound() {
        for (int i = 0; i < 12; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
        }

        assertThrows(NoSuchElementException.class, () -> {
            getRepo().getOne(QueryItem.lt(getNameColumn(), "Name 0"));
        });
    }

    @Test
    void findOneByQuery() {
        for (int i = 0; i < 12; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
        }

        // when
        var maybeEntity = getRepo().findOne(QueryItem.eq(getNameColumn(), "Name 5"));

        // then
        assertTrue(maybeEntity.isPresent());
        assertEquals("Name 5", getName(maybeEntity.get()));
    }

    @Test
    void findOneByQueryMoreThanOne() {
        for (int i = 0; i < 12; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
        }

        assertThrows(IllegalArgumentException.class, () -> {
            getRepo().findOne(QueryItem.gt(getNameColumn(), "Name 5"));

        });
    }

    @Test
    void findOneNothingFound() {
        for (int i = 0; i < 12; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
        }

        // when
        var maybeEntity = getRepo().findOne(QueryItem.lt(getNameColumn(), "Name 0"));

        // then
        assertEquals(Optional.empty(), maybeEntity);
    }

}
