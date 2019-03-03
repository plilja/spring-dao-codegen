package dbtests;

import dbtests.framework.Column;
import dbtests.framework.QueryItem;
import dbtests.framework.Queryable;
import dbtests.framework.SortOrder;
import dbtests.framework.TooManyRowsAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class QueryViewTest<Entity, Repo extends Queryable<Entity>> {

    protected abstract void clearTable();

    protected abstract void saveNew(String name, String color);

    protected abstract String getName(Entity entity);

    protected abstract String getColor(Entity entity);

    protected abstract Repo getQueryable();

    protected abstract Column<Entity, String> getNameColumn();

    @BeforeEach
    void before() {
        clearTable();
    }

    @Test
    void selectAll() {
        saveNew("Sven", "RED");
        saveNew("Victoria", "GREEN");

        // when
        List<Entity> res = getQueryable().findAll();

        // then
        assertEquals(
                Set.of("Sven", "Victoria"),
                res.stream().map(this::getName).collect(Collectors.toSet())
        );
        assertEquals(
                Set.of("RED", "GREEN"),
                res.stream().map(this::getColor).collect(Collectors.toSet())
        );
    }

    @Test
    void selectAllWithOrderBy() {
        saveNew("Sven", "RED");
        saveNew("Victoria", "GREEN");
        saveNew("Brett", "BLUE");
        saveNew("Ally", "BLUE");

        // when
        List<Entity> res = getQueryable().findAllOrderBy(SortOrder.asc(getNameColumn()));

        // then
        assertEquals(4, res.size());
        assertEquals("Ally", getName(res.get(0)));
        assertEquals("Brett", getName(res.get(1)));
        assertEquals("Sven", getName(res.get(2)));
        assertEquals("Victoria", getName(res.get(3)));
    }

    @Test
    void selectAllWithMaxCountExceeded() {
        saveNew("Sven", "RED");
        saveNew("Victoria", "GREEN");

        // when
        assertThrows(TooManyRowsAvailableException.class, () -> {
            getQueryable().findAll(1);
        });
    }

    @Test
    void queryWithEq() {
        saveNew("Sven", "RED");
        saveNew("Victoria", "GREEN");
        saveNew("Victoria", "BLUE");
        saveNew("Brett", "BLUE");
        saveNew("Brett", "GREEN");
        saveNew("Ally", "BLUE");

        // when
        List<Entity> res = getQueryable().findAll(
                QueryItem.eq(getNameColumn(), "Victoria")
        );

        // then
        assertEquals(2, res.size());
        assertEquals("Victoria", getName(res.get(0)));
        assertEquals("Victoria", getName(res.get(1)));
    }
}
