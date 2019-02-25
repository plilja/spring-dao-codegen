package dbtests;

import dbtests.framework.BaseEntity;
import dbtests.framework.ChangedAtTracked;
import dbtests.framework.ChangedByTracked;
import dbtests.framework.CreatedAtTracked;
import dbtests.framework.CreatedByTracked;
import dbtests.framework.Dao;
import dbtests.framework.NoRowsUpdatedException;
import dbtests.framework.TooManyRowsAvailableException;
import dbtests.framework.VersionTracked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class BaseIntegrationTest<
        Entity extends BaseEntity<Integer> & ChangedByTracked & CreatedByTracked & CreatedAtTracked<LocalDateTime> & ChangedAtTracked<LocalDateTime> & VersionTracked,
        Repo extends Dao<Entity, Integer>> {

    @BeforeEach
    void before() {
        clearTable();
    }

    @Autowired
    private FakeSpringSecurity fakeSpringSecurity;

    @Autowired
    private TransactionUtil transactionUtil;

    protected abstract Repo getRepo();

    protected abstract void clearTable();

    protected abstract Entity newEntity(String name);

    protected abstract String getName(Entity entity);

    protected abstract void setName(Entity entity, String name);

    protected abstract void setVersion(Entity entity, Integer version);

    protected abstract void insertObjectWithoutVersionColumn(String name);

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
        fakeSpringSecurity.setCurrentUser("user1");
        getRepo().save(bazEntity);
        assertNotNull(bazEntity.getId());
        assertNotNull(bazEntity.getCreatedAt());
        assertNotNull(bazEntity.getChangedAt());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS), bazEntity.getCreatedAt().truncatedTo(ChronoUnit.HOURS));
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS), bazEntity.getChangedAt().truncatedTo(ChronoUnit.HOURS));
        assertEquals("user1", bazEntity.getCreatedBy());
        assertEquals("user1", bazEntity.getChangedBy());

        Entity retrievedEntity = getRepo().getOne(bazEntity.getId());
        assertNotNull(retrievedEntity);
        assertEquals("Bar", getName(retrievedEntity));
        // Truncate to avoid false error due to precision loss from db
        assertEquals(bazEntity.getCreatedAt().truncatedTo(ChronoUnit.HOURS), retrievedEntity.getCreatedAt().truncatedTo(ChronoUnit.HOURS));
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS), retrievedEntity.getChangedAt().truncatedTo(ChronoUnit.HOURS));
        assertEquals("user1", retrievedEntity.getCreatedBy());
        assertEquals("user1", retrievedEntity.getChangedBy());
    }

    @Test
    void getOneAndFindOne() {
        Entity entity = newEntity("Bar");
        getRepo().save(entity);

        Entity retrievedEntity1 = getRepo().getOne(entity.getId());
        Optional<Entity> retrievedEntity2 = getRepo().findOne(entity.getId());
        assertTrue(retrievedEntity2.isPresent());
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

        assertEquals(Set.of("Bar1", "Bar2"), getRepo().findAllById(List.of(entity1.getId(), 1, entity2.getId(), 3)).stream().map(this::getName).collect(toSet()));
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
        fakeSpringSecurity.setCurrentUser("user1");
        getRepo().save(bazEntity);

        // This is sort of semantics. But we call the object changed from when it is created.
        // It could be argued that this is wrong. But it does provide more flexibility
        // if people have declared their schema fields as NOT NULL.
        assertNotNull(bazEntity.getChangedAt());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS), bazEntity.getChangedAt().truncatedTo(ChronoUnit.HOURS));
        Entity bazEntity2 = getRepo().getOne(bazEntity.getId());
        setName(bazEntity2, "Bar updated");
        bazEntity2.setId(bazEntity.getId());
        fakeSpringSecurity.setCurrentUser("user2");

        getRepo().save(bazEntity2);

        Entity retrievedEntity = getRepo().getOne(bazEntity.getId());
        assertEquals("Bar updated", getName(retrievedEntity));
        assertNotNull(bazEntity2.getChangedAt());
        assertNotNull(retrievedEntity.getChangedAt());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS), retrievedEntity.getChangedAt().truncatedTo(ChronoUnit.HOURS));
        assertEquals("user1", retrievedEntity.getCreatedBy());
        assertEquals("user2", retrievedEntity.getChangedBy());
        assertEquals("user2", bazEntity2.getChangedBy());
    }

    @Test
    void updateEnsureVersionChangesCorrectly() {
        Entity entity = newEntity("Bar");
        for (int i = 0; i < 130; i++) {
            getRepo().save(entity);
            assertEquals(i % 128, (int) entity.getVersion());
        }
    }

    @Test
    void createdAtAndCreatedByCannotBeModified() {
        Entity entity = newEntity("Bar");
        fakeSpringSecurity.setCurrentUser("user1");
        getRepo().save(entity);

        Entity retrieved = getRepo().getOne(entity.getId());

        LocalDateTime createdAtBeforeSave = retrieved.getCreatedAt();

        fakeSpringSecurity.setCurrentUser("user2");
        retrieved.setCreatedBy("user2");
        retrieved.setCreatedNow();
        getRepo().save(retrieved);

        Entity retrievedAgain = getRepo().getOne(entity.getId());

        assertEquals(createdAtBeforeSave, retrievedAgain.getCreatedAt());
        assertEquals("user1", retrievedAgain.getCreatedBy());
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
    void concurrentUpdatesCausesOptimisticLockingFailure() throws Exception {
        Entity entity = newEntity("Bar");
        getRepo().save(entity);

        boolean exception = false;

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
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
                if (ex.getCause() instanceof NoRowsUpdatedException) {
                    exception = true;
                }
            }
        }
        assertTrue(exception);
    }

    @Test
    void updateOnObjectWithNullVersion() {
        // A not entirely unlikely scenario is that someone has
        // added a version column on a really large existing table
        // and left the existing rows with null because migrating
        // with an initial value would take to long.
        // Therefore we want to support null version-column values.

        insertObjectWithoutVersionColumn("Glenn");

        var retrieved = getRepo().findAll().get(0);

        assertNull(retrieved.getVersion());

        setName(retrieved, "Sven");
        getRepo().save(retrieved);

        var retrieved2 = getRepo().getOne(retrieved.getId());
        assertEquals(Integer.valueOf(0), retrieved.getVersion());
        assertEquals("Sven", getName(retrieved2));
        assertEquals(Integer.valueOf(0), retrieved2.getVersion());

        // If the user continues to use the object is should behave as normal again
        setName(retrieved, "David");
        getRepo().save(retrieved);

        var retrieved3 = getRepo().getOne(retrieved.getId());
        assertEquals(Integer.valueOf(1), retrieved3.getVersion());
        assertEquals("David", getName(retrieved3));
    }

    @Test
    void updateOnObjectWithoutProvidingVersion() {
        // We probably don't wan't to expose the version
        // columns in external API's. But we might wan't
        // people using external API's to be allowed to perform
        // updating operations. Hence we allow updates without the version
        // column provided. A developer who wants the extra version
        // check can refetch the object to get the version value.

        var entity = newEntity("Sven");

        getRepo().save(entity);

        setVersion(entity, null);

        setName(entity, "Bart");
        getRepo().save(entity);

        var retrieved = getRepo().getOne(entity.getId());
        assertEquals(Integer.valueOf(0), retrieved.getVersion());
        assertEquals("Bart", getName(retrieved));

        // If the user continues to use the object is should behave as normal again
        setName(entity, "Glenn");
        getRepo().save(entity);

        var retrieved2 = getRepo().getOne(entity.getId());
        assertEquals(Integer.valueOf(1), retrieved2.getVersion());
        assertEquals("Glenn", getName(retrieved2));
    }

    @Test
    void lockWithoutConcurrentModification() {
        transactionUtil.inTransaction(() -> {
            Entity entity = newEntity("Bar");
            getRepo().save(entity);

            getRepo().getOneAndLock(entity.getId());
            Entity retrievedAndLocked = getRepo().getOneAndLock(entity.getId()); // Retrieving and locking multiple times should be if in the same transaction

            assertEquals(entity.getId(), retrievedAndLocked.getId());
            assertEquals("Bar", getName(retrievedAndLocked));
            setName(retrievedAndLocked, "Foo");
            getRepo().save(retrievedAndLocked); // Ok since we have the lock in the transaction

            Entity retrieved = getRepo().getOne(entity.getId());
            assertEquals(entity.getId(), retrieved.getId());
            assertEquals("Foo", getName(retrieved));
            return null;
        });
    }

    @Test
    public void lockWithConcurrentModification() throws Exception {
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
            getRepo().getOneAndLock(entity.getId());
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
