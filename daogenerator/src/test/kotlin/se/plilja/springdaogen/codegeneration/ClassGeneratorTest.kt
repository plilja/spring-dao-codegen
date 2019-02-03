package se.plilja.springdaogen.codegeneration

import org.junit.jupiter.api.Test
import java.sql.Connection
import kotlin.test.assertEquals

class ClassGeneratorTest {
    @Test
    fun `base case`() {
        val g = ClassGenerator("Foo", "se.plilja.test", "")
        g.addImport(Connection::class.java)
        g.addField("bar", String::class.java)
        g.addField("baz", Integer::class.java)

        // when
        val act = g.generate()

        // then
        val exp = """package se.plilja.test;

import java.sql.Connection;

public class Foo {

    private String bar;
    private Integer baz;

    public Foo() {
    }

    public Foo(String bar, Integer baz) {
        this.bar = bar;
        this.baz = baz;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public Integer getBaz() {
        return baz;
    }

    public void setBaz(Integer baz) {
        this.baz = baz;
    }

}
"""
        assertEquals(exp, act)
    }

    @Test
    fun `with extends`() {
        val g = ClassGenerator("Foo", "se.plilja.test", "")
        g.setExtends(A::class.java)

        // when
        val act = g.generate()

        // then
        val exp = """package se.plilja.test;

import se.plilja.springdaogen.codegeneration.ClassGeneratorTest.A;

public class Foo extends A {

    public Foo() {
    }

}
"""
        assertEquals(exp, act)
    }

    @Test
    fun `extends with generics`() {
        val g = ClassGenerator("Foo", "se.plilja.test", "")
        g.extends = "B<Integer>"
        g.addImport(B::class.java)

        // when
        val act = g.generate()

        // then
        val exp = """package se.plilja.test;

import se.plilja.springdaogen.codegeneration.ClassGeneratorTest.B;

public class Foo extends B<Integer> {

    public Foo() {
    }

}
"""
        assertEquals(exp, act)
    }

    @Test
    fun `with static constant`() {
        val g = ClassGenerator("Foo", "se.plilja.test", "")
        g.addConstant("BAR", String::class.java, "\"BAZ\"")
        g.isConstantsClass = true

        // when
        val act = g.generate()

        // then
        val exp = """package se.plilja.test;

public class Foo {

    public static final String BAR = "BAZ";

    private Foo() {
    }

}
"""
        assertEquals(exp, act)
    }

    @Test
    fun `abstract class`() {
        val g = ClassGenerator("Foo", "se.plilja.test", "")
        g.isAbstract = true

        // when
        val act = g.generate()

        // then
        val exp = """package se.plilja.test;

public abstract class Foo {

    public Foo() {
    }

}
"""
        assertEquals(exp, act)

    }

    @Test
    fun `static lambda constant with try catch`() {
        val g = ClassGenerator("Foo", "se.plilja.test", "")
        g.addPrivateConstant(
            "rowMapper", "RowMapper<Foo>", """private static final RowMapper<DataTypesOracle> ROW_MAPPER = (rs, i) -> {
            try {
                Foo r = new Foo();
                r.setBlob(rs.getObject("BLOB") != null ? rs.getBlob("BLOB").getBinaryStream().readAllBytes() : null);
                return r;
            } catch (IOException ex) {
                // A comment that doesn't end with semicolon
                throw new RuntimeException(ex);
            }
        }
        """.trimMargin()
        )

        // when
        val act = g.generate()

        // then
        val exp = """package se.plilja.test;

public class Foo {

    private static final RowMapper<Foo> rowMapper = private static final RowMapper<DataTypesOracle> ROW_MAPPER = (rs, i) -> {
        try {
            Foo r = new Foo();
            r.setBlob(rs.getObject("BLOB") != null ? rs.getBlob("BLOB").getBinaryStream().readAllBytes() : null);
            return r;
        } catch (IOException ex) {
            // A comment that doesn't end with semicolon
            throw new RuntimeException(ex);
        }
    };

    public Foo() {
    }

}
"""
        assertEquals(exp, act)
    }

    class A
    class B<T>
}