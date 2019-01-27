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
        val pk = Column("ID", Integer::class.java, true)
        val table = Table("dbo", "Table", pk, listOf(pk))

        // when
        val res = generateEntity(config, table)
        val act = res.generate()

        // then
        val exp = """
package se.plilja.test;

public class Table implements BaseEntity<Integer> {

    private Integer id;

    public Table() {
    }

    public Table(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

}

        """.trimIndent()
        assertEquals(exp, act)
    }

    @Test
    fun entityWithOneField() {
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
                false,
                entitySuffix = "Entity"
            )
        val pk = Column("TABLE_ID", Integer::class.java, true)
        val name = Column("NAME", String::class.java)
        val table = Table("dbo", "TABLE", pk, listOf(pk, name), entitySuffix = "Entity")

        // when
        val res = generateEntity(config, table)
        val act = res.generate()

        // then
        val exp = """
package se.plilja.test;

public class TableEntity implements BaseEntity<Integer> {

    private Integer tableId;
    private String name;

    public TableEntity() {
    }

    public TableEntity(Integer tableId, String name) {
        this.tableId = tableId;
        this.name = name;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return tableId;
    }

    @Override
    public void setId(Integer id) {
        this.tableId = id;
    }

}

        """.trimIndent()
        assertEquals(exp, act)
    }

}