package se.plilja.springdaogen.dbtests


import org.junit.Before
import org.junit.Test
import org.springframework.dao.EmptyResultDataAccessException
import se.plilja.springdaogen.dbtests.framework.BaseEntity
import se.plilja.springdaogen.dbtests.framework.BaseRepository
import se.plilja.springdaogen.dbtests.framework.MaxAllowedCountExceededException
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

abstract class BaseIntegrationTest<Entity : BaseEntity<Entity, Int>, Repo : BaseRepository<Entity, Int>> {

    abstract var repo: Repo

    @Before
    fun before() {
        clearTable()
    }

    abstract fun clearTable()

    abstract fun newEntity(name: String): Entity

    abstract fun getName(entity: Entity): String

    @Test
    fun findAll() {
        assertEquals(emptyList(), repo.findAll())

        for (i in 1..10) {
            val bazEntity = newEntity("Bar $i")
            repo.create(bazEntity)
        }

        assertEquals(10, repo.findAll().size)
    }

    @Test(expected = MaxAllowedCountExceededException::class)
    fun findAllExceedsMaxLimit() {
        for (i in 1..11) {
            val bazEntity = newEntity("Bar $i")
            repo.create(bazEntity)
        }
        repo.findAll()
    }

    @Test
    fun create() {
        val bazEntity = newEntity("Bar")
        repo.create(bazEntity)
        assertNotNull(bazEntity.id)

        val retrievedEntity = repo.getOne(bazEntity.id)
        assertNotNull(retrievedEntity)
        assertEquals("Bar", getName(retrievedEntity))
    }

    @Test
    fun getOneAndFindOne() {
        val entity = newEntity("Bar")
        repo.create(entity)

        val retrievedEntity1 = repo.getOne(entity.id)
        val retrievedEntity2 = repo.findOneById(entity.id)
        assertTrue(retrievedEntity2.isPresent)
        assertEquals(retrievedEntity1.id, retrievedEntity2.get().id)
    }

    @Test
    fun update() {
        val bazEntity = newEntity("Bar")
        repo.create(bazEntity)

        val bazEntity2 = newEntity("Bar updated")
            .setId(bazEntity.id)
        repo.update(bazEntity2)

        val retrievedEntity = repo.getOne(bazEntity.id)
        assertEquals("Bar updated", getName(retrievedEntity))
    }

    @Test(expected = EmptyResultDataAccessException::class)
    fun getOneNonExistingThrowsException() {
        repo.getOne(4711)
    }

    @Test
    fun findOneNonExistingReturnsEmpty() {
        val maybeT = repo.findOneById(4711)
        assertEquals(Optional.empty(), maybeT)
    }
}