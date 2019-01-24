package se.plilja.springdaogen.daogeneration

import org.junit.Test
import se.plilja.springdaogen.model.Column
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.DatabaseDialect
import se.plilja.springdaogen.model.Table
import kotlin.test.assertEquals


class EntityGeneratorTest {
    @Test
    fun generateEntityWithPropertyNamedId() {
        val config =
            Config(
                "",
                DatabaseDialect.MYSQL,
                "",
                "",
                "",
                "",
                "",
                "se.plilja.test",
                "",
                "",
                "",
                "se.plilja.test",
                10,
                emptyList(),
                false
            )
        val pk = Column("ID", Integer::class.java)
        val table = Table("dbo", "Table", pk, listOf(pk))

        // when
        val res = generateEntity(config, table)
        val act = res.generate()

        // then
        val exp = """
package se.plilja.test;

public class TableEntity implements BaseEntity<Integer> {

    private Integer id;

    public TableEntity() {
    }

    public TableEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}

        """.trimIndent()
        assertEquals(exp, act)
    }
}