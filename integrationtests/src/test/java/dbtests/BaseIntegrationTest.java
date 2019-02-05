package dbtests;

import dbtests.framework.BaseEntity;
import dbtests.framework.ChangedAtTracked;
import dbtests.framework.CreatedAtTracked;
import dbtests.framework.Dao;
import dbtests.framework.MaxAllowedCountExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public abstract class BaseIntegrationTest<Entity extends BaseEntity<Integer>, Repo extends Dao<Entity, Integer>> {

    @BeforeEach
    void before() {
        clearTable();
    }

    protected abstract Repo getRepo();

    protected abstract void clearTable();

    protected abstract Entity newEntity(String name);

    protected abstract String getName(Entity entity);

    protected abstract void setName(Entity entity, String name);

    protected abstract LocalDateTime getCreatedAt(Entity entity);

    protected abstract LocalDateTime getChangedAt(Entity entity);

    protected abstract int getVersion(Entity entity);

    @Test
    void findAll() {
        assertEquals(emptyList(), getRepo().findAll());

        for (int i = 1; i <= 10; i++) {
            Entity bazEntity = newEntity(String.format("Bar %d", i));
            getRepo().save(bazEntity);
        }

        assertEquals(10, getRepo().findAll().size());
    }

    @Test
    void findAllExceedsMaxLimit() {
        assertThrows(MaxAllowedCountExceededException.class, () -> {
            for (int i = 0; i < 11; i++) {
                Entity bazEntity = newEntity(String.format("Bar %d", i));
                getRepo().save(bazEntity);
            }
            getRepo().findAll();
        });
    }

    @Test
    void create() {
        Entity bazEntity = newEntity("Bar");
        getRepo().save(bazEntity);
        assertNotNull(bazEntity.getId());
        assertNotNull(getCreatedAt(bazEntity));
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS), getCreatedAt(bazEntity).truncatedTo(ChronoUnit.HOURS));
        assertNull(getChangedAt(bazEntity));

        Entity retrievedEntity = getRepo().getOne(bazEntity.getId());
        assertNotNull(retrievedEntity);
        assertEquals("Bar", getName(retrievedEntity));
        // Truncate to avoid false error due to precision loss from db
        assertEquals(getCreatedAt(bazEntity).truncatedTo(ChronoUnit.HOURS), getCreatedAt(retrievedEntity).truncatedTo(ChronoUnit.HOURS));
        assertNull(getChangedAt(retrievedEntity));
    }

    @Test
    void getOneAndFindOne() {
        Entity entity = newEntity("Bar");
        getRepo().save(entity);

        Entity retrievedEntity1 = getRepo().getOne(entity.getId());
        Optional<Entity> retrievedEntity2 = getRepo().findOneById(entity.getId());
        assertTrue(((Optional) retrievedEntity2).isPresent());
        assertEquals(retrievedEntity1.getId(), retrievedEntity2.get().getId());
    }

    @Test
    void findAllByIds() {
        assertEquals(emptyList(), getRepo().findAllById(List.of(0, 1, 2, 3)));

        Entity entity1 = newEntity("Bar1");
        getRepo().save(entity1);

        assertEquals(List.of("Bar1"), getRepo().findAllById(List.of(entity1.getId(), 1, entity1.getId(), 3)).stream().map(this::getName).collect(toList()));

        Entity entity2 = newEntity("Bar2");
        getRepo().save(entity2);

        assertEquals(List.of("Bar1", "Bar2"), getRepo().findAllById(List.of(entity1.getId(), 1, entity2.getId(), 3)).stream().map(this::getName).collect(toList()));
    }

    @Test
    void findPage() {
        assertEquals(emptyList(), getRepo().findPage(0, 10));

        for (int i = 1; i <= 10; i++) {
            Entity bazEntity = newEntity(String.format("Bar %d", i));
            getRepo().save(bazEntity);
        }

        assertEquals(List.of("Bar 1", "Bar 2"), getRepo().findPage(0, 2).stream().map(this::getName).collect(toList()));
        assertEquals(List.of("Bar 3", "Bar 4"), getRepo().findPage(2, 2).stream().map(this::getName).collect(toList()));
        assertEquals(List.of("Bar 10"), getRepo().findPage(9, 2).stream().map(this::getName).collect(toList()));
        assertEquals(emptyList(), getRepo().findPage(10, 2));
        assertEquals(emptyList(), getRepo().findPage(0, 0));
    }

    @Test
    void existsById() {
        assertFalse(getRepo().existsById(4711));
        Entity entity = newEntity("Bar");
        getRepo().save(entity);
        assertTrue(getRepo().existsById(entity.getId()));
    }

    @Test
    void updateExistingObject() {
        Entity bazEntity = newEntity("Bar");
        getRepo().save(bazEntity);

        assertNull(getChangedAt(bazEntity));
        Entity bazEntity2 = getRepo().getOne(bazEntity.getId());
        setName(bazEntity2, "Bar updated");
        bazEntity2.setId(bazEntity.getId());

        getRepo().save(bazEntity2);

        Entity retrievedEntity = getRepo().getOne(bazEntity.getId());
        assertEquals("Bar updated", getName(retrievedEntity));
        assertNotNull(getChangedAt(bazEntity2));
        assertNotNull(getChangedAt(retrievedEntity));
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS), getChangedAt(retrievedEntity).truncatedTo(ChronoUnit.HOURS));
    }

    @Test
    void updateEnsureVersionChangesCorrectly() {
        Entity entity = newEntity("Bar");
        for (int i = 0; i < 130; i++) {
            getRepo().save(entity);
            assertEquals(i % 128, getVersion(entity));
        }
    }

    @Test
    void createdAtCannotBeModified() {
        Entity entity = newEntity("Bar");
        getRepo().save(entity);

        Entity retrieved = getRepo().getOne(entity.getId());
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }

        LocalDateTime createdAtBeforeSave = getCreatedAt(retrieved);

        ((CreatedAtTracked<?>)retrieved).setCreatedNow();
        getRepo().save(retrieved);

        Entity retrievedAgain = getRepo().getOne(entity.getId());

        assertEquals(createdAtBeforeSave, getCreatedAt(retrievedAgain));
    }

    @Test
    void delete() {
        Entity bazEntity = newEntity("Bar");

        getRepo().save(bazEntity);
        getRepo().delete(bazEntity);

        assertNotNull(bazEntity.getId());
        assertFalse(getRepo().existsById(bazEntity.getId()));
    }

    @Test
    void deleteById() {
        Entity bazEntity = newEntity("Bar");

        getRepo().save(bazEntity);
        getRepo().deleteById(bazEntity.getId());

        assertNotNull(bazEntity.getId());
        assertFalse(getRepo().existsById(bazEntity.getId()));
    }

    @Test
    void deleteAll() {
        Entity entity1 = newEntity("Bar1");
        Entity entity2 = newEntity("Bar2");
        Entity entity3 = newEntity("Bar3");
        Entity entity4 = newEntity("Bar3");

        getRepo().save(entity1);
        getRepo().save(entity2);
        getRepo().save(entity3);
        getRepo().save(entity4);

        getRepo().deleteAll(List.of(entity2, entity3));

        assertTrue(getRepo().existsById(entity1.getId()));
        assertFalse(getRepo().existsById(entity2.getId()));
        assertFalse(getRepo().existsById(entity3.getId()));
        assertTrue(getRepo().existsById(entity4.getId()));
    }

    @Test
    void getOneNonExistingThrowsException() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            getRepo().getOne(4711);
        });
    }

    @Test
    void findOneNonExistingReturnsEmpty() {
        Optional<Entity> maybeT = getRepo().findOneById(4711);
        assertEquals(Optional.empty(), maybeT);
    }

    @Test
    void count() {
        assertEquals(0, getRepo().count());

        Entity entity1 = newEntity("Bar3");
        getRepo().save(entity1);

        assertEquals(1, getRepo().count());

        Entity entity2 = newEntity("Bar3");
        getRepo().save(entity2);

        assertEquals(2, getRepo().count());
    }
}
