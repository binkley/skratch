package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Complex.Companion.I
import hm.binkley.labs.skratch.math.matrix.Complex.Companion.ONE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ComplexTest {
    @Test
    fun norm() {
        assertEquals(Rational(25L), Complex(3L, 4L).squareNorm)
    }

    @Test
    fun isReal() {
        assertTrue(ONE.isReal)
    }

    @Test
    fun isUnreal() {
        assertFalse(Complex(1L, 1L).isReal)
    }

    @Test
    fun isImaginary() {
        assertTrue(I.isImaginary)
    }

    @Test
    fun isUnimaginary() {
        assertFalse(Complex(1L, 1L).isImaginary)
    }
}
