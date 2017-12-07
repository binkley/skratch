package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ZERO
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RationalTest {
    @Test
    fun throwWhenDividingByZero() {
        assertThrows(ArithmeticException::class.java) {
            Rational(1L, 0L)
        }
    }

    @Test
    fun zeroIsZero() {
        assertEquals(ZERO, Rational(0L, 3L))
    }

    @Test
    fun twoFourthsIsOneHalf() {
        assertEquals(Rational(1L, 2L), Rational(2L, 4L))
    }

    @Test
    fun negativeDenominatorMovedToNumerator() {
        assertEquals(Rational(-1L, 1L), Rational(1L, -1L))
    }

    @Test
    fun printAsFraction() {
        assertEquals("2/3", Rational(2L, 3L).toString())
    }
}
