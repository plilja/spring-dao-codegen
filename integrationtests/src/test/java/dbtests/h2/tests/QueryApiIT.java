package dbtests.h2.tests;

import dbtests.framework.QueryItem;
import dbtests.framework.TooManyRowsAvailableException;
import dbtests.h2.model.BazH2;
import dbtests.h2.model.BazH2Repo;
import dbtests.h2.model.ColorEnumH2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = {H2Config.class})
@ExtendWith(SpringExtension.class)
public class QueryApiIT {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BazH2Repo repo;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM test_schema.baz_h2", new MapSqlParameterSource());
    }

    private BazH2 newEntity(String name) {
        BazH2 r = new BazH2();
        r.setBazName(name);
        return r;
    }

    @Test
    void singleAttributeQuery() {
        BazH2 entity1 = newEntity("Phil");
        repo.save(entity1);
        BazH2 entity2 = newEntity("David");
        repo.save(entity2);
        BazH2 entity3 = newEntity("David");
        repo.save(entity3);
        BazH2 entity4 = newEntity("Chris");
        repo.save(entity4);

        // when
        List<BazH2> result = repo.query(List.of(
                QueryItem.eq(BazH2Repo.COLUMN_BAZ_NAME, "David")
        ));

        // then
        assertEquals(2, result.size());
        assertEquals(entity2.getId(), result.get(0).getId());
        assertEquals(entity3.getId(), result.get(1).getId());
    }

    @Test
    void multiAttributeQuery() {
        BazH2 entity1 = newEntity("Phil");
        entity1.setColor(ColorEnumH2.GREEN);
        repo.save(entity1);
        BazH2 entity2 = newEntity("David");
        entity2.setColor(ColorEnumH2.RED);
        repo.save(entity2);
        BazH2 entity3 = newEntity("David");
        entity3.setColor(ColorEnumH2.BLUE);
        repo.save(entity3);
        BazH2 entity4 = newEntity("Chris");
        entity4.setColor(ColorEnumH2.BLUE);
        repo.save(entity4);

        // when
        List<BazH2> result = repo.query(List.of(
                QueryItem.eq(BazH2Repo.COLUMN_BAZ_NAME, "David"),
                QueryItem.eq(BazH2Repo.COLUMN_COLOR, ColorEnumH2.BLUE)
        ));

        // then
        assertEquals(1, result.size());
        assertEquals(entity3.getId(), result.get(0).getId());
    }

    @Test
    void queryForNull() {
        BazH2 entity1 = newEntity("Phil");
        entity1.setColor(ColorEnumH2.GREEN);
        repo.save(entity1);
        BazH2 entity2 = newEntity("David");
        entity2.setColor(null);
        repo.save(entity2);
        BazH2 entity3 = newEntity("David");
        entity3.setColor(ColorEnumH2.BLUE);
        repo.save(entity3);
        BazH2 entity4 = newEntity("Chris");
        entity4.setColor(null);
        repo.save(entity4);

        // when
        List<BazH2> result = repo.query(List.of(
                QueryItem.eq(BazH2Repo.COLUMN_COLOR, null)
        ));

        // then
        assertEquals(2, result.size());
        assertEquals(entity2.getId(), result.get(0).getId());
        assertEquals(entity4.getId(), result.get(1).getId());
    }

    @Test
    void queryWithNeq() {
        BazH2 entity1 = newEntity("Phil");
        entity1.setColor(ColorEnumH2.GREEN);
        repo.save(entity1);
        BazH2 entity2 = newEntity("David");
        entity2.setColor(null);
        repo.save(entity2);
        BazH2 entity3 = newEntity("David");
        entity3.setColor(ColorEnumH2.BLUE);
        repo.save(entity3);
        BazH2 entity4 = newEntity("Chris");
        entity4.setColor(null);
        repo.save(entity4);

        // when
        List<BazH2> result = repo.query(List.of(
                QueryItem.neq(BazH2Repo.COLUMN_BAZ_NAME, "David", true),
                QueryItem.neq(BazH2Repo.COLUMN_COLOR, ColorEnumH2.GREEN, true)
        ));

        // then
        assertEquals(1, result.size());
        assertEquals(entity4.getId(), result.get(0).getId());
    }

    @Test
    void queryWithLT() {
        BazH2 entity1 = newEntity("Phil");
        entity1.setColor(ColorEnumH2.GREEN);
        repo.save(entity1);
        BazH2 entity2 = newEntity("David");
        entity2.setColor(null);
        repo.save(entity2);
        BazH2 entity3 = newEntity("David");
        entity3.setColor(ColorEnumH2.BLUE);
        repo.save(entity3);
        BazH2 entity4 = newEntity("Chris");
        entity4.setColor(null);
        repo.save(entity4);

        // when
        List<BazH2> result = repo.query(List.of(
                QueryItem.lt(BazH2Repo.COLUMN_BAZ_ID, entity3.getId())
        ));

        // then
        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    @Test
    void queryWithLTE() {
        BazH2 entity1 = newEntity("Phil");
        entity1.setColor(ColorEnumH2.GREEN);
        repo.save(entity1);
        BazH2 entity2 = newEntity("David");
        entity2.setColor(null);
        repo.save(entity2);
        BazH2 entity3 = newEntity("David");
        entity3.setColor(ColorEnumH2.BLUE);
        repo.save(entity3);
        BazH2 entity4 = newEntity("Chris");
        entity4.setColor(null);
        repo.save(entity4);

        // when
        List<BazH2> result = repo.query(List.of(
                QueryItem.lte(BazH2Repo.COLUMN_BAZ_ID, entity2.getId())
        ));

        // then
        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    @Test
    void queryWithGT() {
        BazH2 entity1 = newEntity("Phil");
        entity1.setColor(ColorEnumH2.GREEN);
        repo.save(entity1);
        BazH2 entity2 = newEntity("David");
        entity2.setColor(null);
        repo.save(entity2);
        BazH2 entity3 = newEntity("David");
        entity3.setColor(ColorEnumH2.BLUE);
        repo.save(entity3);
        BazH2 entity4 = newEntity("Chris");
        entity4.setColor(null);
        repo.save(entity4);

        // when
        List<BazH2> result = repo.query(List.of(
                QueryItem.gt(BazH2Repo.COLUMN_BAZ_ID, entity2.getId())
        ));

        // then
        assertEquals(2, result.size());
        assertEquals(entity3.getId(), result.get(0).getId());
        assertEquals(entity4.getId(), result.get(1).getId());
    }

    @Test
    void queryWithGTE() {
        BazH2 entity1 = newEntity("Phil");
        entity1.setColor(ColorEnumH2.GREEN);
        repo.save(entity1);
        BazH2 entity2 = newEntity("David");
        entity2.setColor(null);
        repo.save(entity2);
        BazH2 entity3 = newEntity("David");
        entity3.setColor(ColorEnumH2.BLUE);
        repo.save(entity3);
        BazH2 entity4 = newEntity("Chris");
        entity4.setColor(null);
        repo.save(entity4);

        // when
        List<BazH2> result = repo.query(List.of(
                QueryItem.gte(BazH2Repo.COLUMN_BAZ_ID, entity3.getId())
        ));

        // then
        assertEquals(2, result.size());
        assertEquals(entity3.getId(), result.get(0).getId());
        assertEquals(entity4.getId(), result.get(1).getId());
    }

    @Test
    void shouldRespectMaxSelectCount() {
        for (int i = 1; i <= 10; i++) {
            BazH2 entity = newEntity(String.format("Name %d", i));
            repo.save(entity);
            System.out.println(entity.getId());
        }
        List<BazH2> res = repo.query(QueryItem.gte(BazH2Repo.COLUMN_BAZ_ID, 0));
        assertEquals(10, res.size()); // Should be fine as we are below limit

        BazH2 entity = newEntity("Name 11");
        repo.save(entity);

        assertThrows(TooManyRowsAvailableException.class, () -> {
            repo.query(QueryItem.gte(BazH2Repo.COLUMN_BAZ_ID, 0));
        });
    }
}
