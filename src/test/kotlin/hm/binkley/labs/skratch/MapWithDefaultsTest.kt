package hm.binkley.labs.skratch

import org.junit.Before
import org.junit.Test
import java.util.concurrent.ConcurrentHashMap
import kotlin.test.assertEquals

class MapWithDefaultsTest {
    private lateinit var map: MutableMap<Int, Int>

    @Before
    fun setUp() {
        map = object : ConcurrentHashMap<Int, Int>() {
            override fun get(key: Int) = super.get(key) ?: 0
        }
    }

    @Test
    fun withValue() {
        map[0] = 1

        assertEquals(1, map[0])
    }

    @Test
    fun withoutValue() {
        assertEquals(0, map[0])
    }
}
