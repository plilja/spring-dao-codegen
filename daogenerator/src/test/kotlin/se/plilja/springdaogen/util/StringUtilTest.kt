package se.plilja.springdaogen.util

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class StringUtilTest {

    @Test
    fun `camel case`() {
        assertEquals("", camelCase(""))
        assertEquals("foo", camelCase("foo"))
        assertEquals("foo", camelCase("FOO"))
        assertEquals("fooBar", camelCase("fOo_bAR"))
        assertEquals("fooBar", camelCase("fooBar"))
        assertEquals("foo1", camelCase("FOO1"))
        assertEquals("foo1bar", camelCase("FOO1BAR"))
        assertEquals("fooBar", camelCase("foo bar"))
        assertEquals("fooBar", camelCase("Foo Bar"))
    }

    @Test
    fun `snake case`() {
//        assertEquals("", snakeCase(""))
//        assertEquals("foo", snakeCase("foo"))
//        assertEquals("foo_bar", snakeCase("fooBar"))
//        assertEquals("foo_bar", snakeCase("FooBar"))
//        assertEquals("foo_bar", snakeCase("foo_bar"))
//        assertEquals("foo", snakeCase("FOO"))
//        assertEquals("foo1", snakeCase("FOO1"))
//        assertEquals("foo1bar", snakeCase("FOO1BAR"))
        assertEquals("foo_bar", snakeCase("foo bar"))
    }

}