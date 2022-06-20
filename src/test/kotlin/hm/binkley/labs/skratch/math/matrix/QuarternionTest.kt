package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Quarternion.Companion.I
import hm.binkley.labs.skratch.math.matrix.Quarternion.Companion.J
import hm.binkley.labs.skratch.math.matrix.Quarternion.Companion.K
import hm.binkley.labs.skratch.math.matrix.Quarternion.Companion.ONE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals
import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ONE as RONE

private val RTWO = Rational(2L)

internal class QuarternionTest {
    @Test
    fun norm() {
        assertEquals(Rational(54L), Quarternion(2L, 3L, 4L, 5L).squareNorm)
    }

    @Test
    fun isReal() {
        assertTrue(ONE.isReal)
    }

    @Test
    fun isUnreal() {
        assertFalse(I.isReal)
        assertFalse(J.isReal)
        assertFalse(K.isReal)
    }

    @Test
    fun multiplies() {
        assertEquals(ONE, ONE * ONE)
        assertEquals(-ONE, I * I)
        assertEquals(-ONE, J * J)
        assertEquals(-ONE, K * K)

        val excessOnes = Quarternion(RONE, RONE, RONE, RONE)
        assertEquals(
            Quarternion(-RTWO, RTWO, RTWO, RTWO), // A neat fact
            excessOnes * excessOnes
        )
    }

    @Test
    fun doesNotCommute() {
        val a = Quarternion(RONE, RTWO, Rational(3L), Rational(4L))
        val b = Quarternion(Rational(4L), Rational(3L), RTWO, RONE)

        assertNotEquals(a * b, b * a)
    }

    @Test
    fun divides() {
        assertEquals(ONE, ONE / ONE)
        assertEquals(-I, ONE / I)
        assertEquals(-J, ONE / J)
        assertEquals(-K, ONE / K)
        assertEquals(-K, I / J)
        assertEquals(-I, J / K)
        assertEquals(-J, K / I)
    }
}
