package hm.binkley.labs.skratch.math.matrix

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ComplexTest {
    @Test
    fun norm() {
        assertEquals(Rational(5L), Complex(3L, 4L).abs)
    }

    @Test
    fun isReal() {
        assertTrue(Complex(1L).isReal)
    }

    @Test
    fun isUnreal() {
        assertFalse(Complex(1L, 1L).isReal)
    }

    @Test
    fun isImaginary() {
        assertTrue(Complex(0L, 1L).isImaginary)
    }

    @Test
    fun isUnimaginary() {
        assertFalse(Complex(1L, 1L).isImaginary)
    }
}
