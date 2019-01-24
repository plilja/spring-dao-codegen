package se.plilja.springdaogen.util

import org.junit.Assert.assertEquals
import org.junit.Test
import se.plilja.springdaogen.util.camelCase
import se.plilja.springdaogen.util.snakeCase

class StringUtilTest {

    @Test
    fun testCamelCase() {
        assertEquals("", camelCase(""))
        assertEquals("foo", camelCase("foo"))
        assertEquals("foo", camelCase("FOO"))
        assertEquals("fooBar", camelCase("fOo_bAR"))
        assertEquals("fooBar", camelCase("fooBar"))
    }

    @Test
    fun testSnakeCase() {
        assertEquals("", snakeCase(""))
        assertEquals("foo", snakeCase("foo"))
        assertEquals("foo_bar", snakeCase("fooBar"))
        assertEquals("foo_bar", snakeCase("FooBar"))
        assertEquals("foo_bar", snakeCase("foo_bar"))
        assertEquals("foo", snakeCase("FOO"))
    }

}