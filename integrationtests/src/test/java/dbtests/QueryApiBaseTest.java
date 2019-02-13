package dbtests;

import dbtests.framework.BaseEntity;
import dbtests.framework.Column;
import dbtests.framework.Dao;
import dbtests.framework.QueryItem;
import dbtests.framework.SortOrder;
import dbtests.framework.TooManyRowsAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Autowired
    private TransactionUtil transactionUtil;

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
        var result = getRepo().query(List.of(
                QueryItem.eq(getNameColumn(), "David")
        ));

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
        List<Entity> result = getRepo().query(List.of(
                QueryItem.eq(getNameColumn(), "David"),
                QueryItem.eq((Column) getColorColumn(), getColorByName("BLUE"))
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
        List<Entity> result = getRepo().query(
                List.of(QueryItem.eq(getColorColumn(), null)),
                SortOrder.asc(getIdColumn())
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
        List<Entity> result = getRepo().query(List.of(
                QueryItem.neq(getNameColumn(), "David", true),
                QueryItem.neq((Column) getColorColumn(), getColorByName("GREEN"), true)
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
        List<Entity> result = getRepo().query(List.of(
                QueryItem.lt(getIdColumn(), entity3.getId())
        ));

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
        List<Entity> result = getRepo().query(List.of(
                QueryItem.lte(getIdColumn(), entity2.getId())
        ));

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
        List<Entity> result = getRepo().query(List.of(
                QueryItem.gt(getIdColumn(), entity2.getId())
        ));

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
        List<Entity> result = getRepo().query(List.of(
                QueryItem.gte(getIdColumn(), entity3.getId())
        ));

        // then
        assertEquals(2, result.size());
        assertEquals(entity3.getId(), result.get(0).getId());
        assertEquals(entity4.getId(), result.get(1).getId());
    }

    @Test
    void shouldRespectMaxSelectCount() {
        for (int i = 1; i <= 10; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
        }
        var res = getRepo().query(QueryItem.gte(getIdColumn(), 0));
        assertEquals(10, res.size()); // Should be fine as we are below limit

        var entity = newEntity("Name 11");
        getRepo().save(entity);

        assertThrows(TooManyRowsAvailableException.class, () -> {
            getRepo().query(QueryItem.gte(getIdColumn(), 0));
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
        List<Entity> result = getRepo().query(Collections.emptyList());

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
        List<Entity> result = getRepo().query(
                QueryItem.eq((Column) getColorColumn(), getColorByName("GREEN")),
                SortOrder.asc(getNameColumn())
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
        List<Entity> result = getRepo().query(
                QueryItem.eq((Column) getColorColumn(), getColorByName("GREEN")),
                SortOrder.desc(getNameColumn())
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
        List<Entity> result = getRepo().findAll(
                SortOrder.asc(getNameColumn())
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
        assertThrows(TooManyRowsAvailableException.class, () -> getRepo().findAll(SortOrder.asc(getNameColumn())));
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
            getRepo().query(QueryItem.eq((Column) getColorColumn(), getColorByName("BLUE")));
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
        List<Entity> firstPage = getRepo().queryForPage(0, 10, List.of(QueryItem.eq((Column) getColorColumn(), getColorByName("BLUE"))));
        assertEquals(10, firstPage.size());
        for (var entity : firstPage) {
            assertEquals(0, entity.getId() % 2);
            assertEquals(getColorByName("BLUE"), getColor(entity));
        }

        // Second page
        List<Entity> secondPage = getRepo().queryForPage(10, 10, List.of(QueryItem.eq((Column) getColorColumn(), getColorByName("BLUE"))));
        assertEquals(10, secondPage.size());
        for (var entity : secondPage) {
            assertTrue(entity.getId() >= 20);
            assertEquals(0, entity.getId() % 2);
            assertEquals(getColorByName("BLUE"), getColor(entity));
        }

        // Third page
        var thirdPage = getRepo().queryForPage(20, 10, List.of(QueryItem.eq((Column) getColorColumn(), getColorByName("BLUE"))));
        assertEquals(1, thirdPage.size());
    }

    @Test
    void findPageWithSortOrder() {
        for (int i = 0; i < 12; i++) {
            var entity = newEntity(String.format("Name %d", i));
            getRepo().save(entity);
        }
        System.out.println(getRepo().findAll(100).size());

        // First page
        List<Entity> firstPage = getRepo().findPage(0, 10, SortOrder.asc(getIdColumn()));
        assertEquals(10, firstPage.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(String.format("Name %d", i), getName(firstPage.get(i)));
        }

        List<Entity> secondPage = getRepo().findPage(10, 10, SortOrder.asc(getIdColumn()));
        assertEquals(2, secondPage.size());
        assertEquals("Name 10", getName(secondPage.get(0)));
        assertEquals("Name 11", getName(secondPage.get(1)));
    }
}
