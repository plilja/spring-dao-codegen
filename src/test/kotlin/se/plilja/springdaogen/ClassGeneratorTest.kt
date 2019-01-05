package se.plilja.springdaogen

import org.junit.Assert.*
import org.junit.Test
import java.sql.Connection

class ClassGeneratorTest {
    @Test
    fun testGen() {
        val g = ClassGenerator("Foo", "se.plilja.test")
        g.addImport(Connection::class.java)
        g.addField("bar", String::class.java)
        g.addField("baz", Integer::class.java)

        // when
        val act = g.generate()

        // then
        val exp = """package se.plilja.test;

import java.lang.Integer;
import java.lang.String;
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

    public Foo setBar(String bar) {
        this.bar = bar;
        return this;
    }

    public Integer getBaz() {
        return baz;
    }

    public Foo setBaz(Integer baz) {
        this.baz = baz;
        return this;
    }

}
"""
        assertEquals(exp, act)
    }

    @Test
    fun testGenExtends() {
        val g = ClassGenerator("Foo", "se.plilja.test")
        g.setExtends(A::class.java)

        // when
        val act = g.generate()

        // then
        val exp = """package se.plilja.test;

import se.plilja.springdaogen.ClassGeneratorTest.A;

public class Foo extends A {

    public Foo() {
    }

}
"""
        assertEquals(exp, act)
    }

    @Test
    fun testGenExtendsWithGenerics() {
        val g = ClassGenerator("Foo", "se.plilja.test")
        g.extends = "B<Integer>"
        g.addImport(B::class.java)

        // when
        val act = g.generate()

        // then
        val exp = """package se.plilja.test;

import se.plilja.springdaogen.ClassGeneratorTest.B;

public class Foo extends B<Integer> {

    public Foo() {
    }

}
"""
        assertEquals(exp, act)
    }

    class A
    class B<T>
}