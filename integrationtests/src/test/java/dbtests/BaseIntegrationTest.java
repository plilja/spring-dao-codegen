package dbtests;

import dbtests.framework.BaseEntity;
import dbtests.framework.CreatedAtTracked;
import dbtests.framework.Dao;
import dbtests.framework.NoRowsUpdatedException;
import dbtests.framework.TooManyRowsAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    @Autowired
    private TransactionUtil transactionUtil;

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
        assertThrows(TooManyRowsAvailableException.class, () -> {
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
        Optional<Entity> retrievedEntity2 = getRepo().findOne(entity.getId());
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
        assertFalse(getRepo().exists(4711));
        Entity entity = newEntity("Bar");
        getRepo().save(entity);
        assertTrue(getRepo().exists(entity.getId()));
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

        ((CreatedAtTracked<?>) retrieved).setCreatedNow();
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
        assertFalse(getRepo().exists(bazEntity.getId()));
    }

    @Test
    void deleteById() {
        Entity bazEntity = newEntity("Bar");

        getRepo().save(bazEntity);
        getRepo().deleteById(bazEntity.getId());

        assertNotNull(bazEntity.getId());
        assertFalse(getRepo().exists(bazEntity.getId()));
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

        assertTrue(getRepo().exists(entity1.getId()));
        assertFalse(getRepo().exists(entity2.getId()));
        assertFalse(getRepo().exists(entity3.getId()));
        assertTrue(getRepo().exists(entity4.getId()));
    }

    @Test
    void getOneNonExistingThrowsException() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            getRepo().getOne(4711);
        });
    }

    @Test
    void findOneNonExistingReturnsEmpty() {
        Optional<Entity> maybeT = getRepo().findOne(4711);
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

    @Test
    void concurrentUpdatesCausesOptimisticLockingFailure() {
        Entity entity = newEntity("Bar");
        getRepo().save(entity);

        assertThrows(NoRowsUpdatedException.class, () -> {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                final int tmp = i;
                futures.add(CompletableFuture.runAsync(() -> {
                    Entity retrieved = getRepo().getOne(entity.getId());
                    setName(retrieved, String.valueOf(tmp));
                    getRepo().save(retrieved);
                }));
            }
            for (CompletableFuture<Void> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException ex) {
                    throw ex.getCause(); // Future wraps our exception in an ExecutionException
                }
            }
        });
    }

    @Test
    void lockWithoutConcurrentModification() {
        transactionUtil.inTransaction(() -> {
            Entity entity = newEntity("Bar");
            getRepo().save(entity);
            getRepo().lock(entity.getId());

            setName(entity, "Foo");
            getRepo().save(entity); // Ok since we have the lock in the transaction

            Entity retrieved = getRepo().getOne(entity.getId());
            assertEquals(entity.getId(), retrieved.getId());
            assertEquals("Foo", getName(retrieved));
            return null;
        });
    }

    @Test
    void lockWithConcurrentModification() throws Exception {
        Entity entity = newEntity("A");
        getRepo().save(entity);

        System.err.println("STARTING CHANGING");
        Future<String> change1 = changeInTransaction(entity, "B", 0, 75, 75);
        // Change2 sleeps less, but it will get stuck waiting for the lock that change1 acquires directly
        Future<String> change2 = changeInTransaction(entity, "C", 25, 0, 0);

        String beforeChange1 = change1.get();
        String beforeChange2 = change2.get();
        assertEquals("A", beforeChange1);
        assertEquals("B", beforeChange2);
        assertEquals("C", getName(getRepo().getOne(entity.getId())));
    }

    private Future<String> changeInTransaction(Entity entity, String name, int sleepBeforeLock, int sleepAfterLockBeforeUpdate, int sleepAfter) {
        return CompletableFuture.supplyAsync(() -> transactionUtil.inTransaction(() -> {
            sleep2(sleepBeforeLock);
            getRepo().lock(entity.getId());
            sleep2(sleepAfterLockBeforeUpdate);
            // Reload after lock has been acquired to avoid NoRowsUpdatedException from concurrent modification
            Entity reloaded = getRepo().getOne(entity.getId());
            String nameBefore = getName(reloaded);
            setName(reloaded, name);
            getRepo().save(reloaded);
            sleep2(sleepAfter);
            return nameBefore;
        }));
    }

    private void sleep2(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // ignore
        }
    }


}
