package hm.binkley.labs.skratch.math.matrix

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ComplexTest {
    @Test
    fun norm() {
        assertEquals(Rational(5L), Complex(3L, 4L).abs)
    }
}
