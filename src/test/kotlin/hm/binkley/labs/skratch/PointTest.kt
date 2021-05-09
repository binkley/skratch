package hm.binkley.labs.skratch

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PointTest {
    @Test
    fun `should interop with Java`() {
        val point = Point(1, 2)

        assertEquals(1, point.x)
        assertEquals(2, point.y)
    }
}
