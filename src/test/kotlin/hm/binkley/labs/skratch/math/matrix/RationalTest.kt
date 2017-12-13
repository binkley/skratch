package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ONE
import hm.binkley.labs.skratch.math.matrix.Rational.Companion.TWO
import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ZERO
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RationalTest {
    @Test
    fun twoFourthsIsOneHalf() {
        assertEquals(Rational(1L, 2L), Rational(2L, 4L))
    }

    @Test
    fun negativeNumeratorStaysAsIs() {
        assertEquals(Rational(-1L, 2L), Rational(-1L, 2L))
    }

    @Test
    fun negativeDenominatorMovedToNumerator() {
        assertEquals(Rational(-1L, 2L), Rational(1L, -2L))
    }

    @Test
    fun negativeDenominatorAndNumeratorCancel() {
        assertEquals(ONE, Rational(-1L, -1L))
    }

    @Test
    fun printAsFraction() {
        assertEquals("2/3", Rational(2L, 3L).toString())
    }

    @Test
    fun multiplication() {
        assertEquals(Rational(4L, 20L), Rational(2L, 4L) * Rational(2L, 5L))
    }

    @Test
    fun throwWhenDividingByZero() {
        assertThrows(ArithmeticException::class.java) {
            Rational(1L, 0L)
        }
    }

    @Test
    fun absoluteValue() {
        assertEquals(Rational(1L, 2L), Rational(-1L, 2L).abs)
    }

    @Test
    fun zeroIsZero() {
        assertEquals(ZERO, Rational(0L, 3L))
    }

    @Test
    fun equalTo() {
        assertTrue(Rational(2L, 5L) == Rational(2L, 5L))
    }

    @Test
    fun lessThan() {
        assertTrue(Rational(2L, 5L) < Rational(2L, 4L))
    }

    @Test
    fun greaterThan() {
        assertTrue(Rational(2L, 4L) > Rational(2L, 5L))
    }

    @Test
    fun rootOfFourIsTwo() {
        assertEquals(TWO, Rational(4L).root)
    }

    /** See https://en.wikipedia.org/wiki/Square_root_of_2 */
    @Test
    fun rootOfTwoIsApproximate() {
        assertEquals(Rational(665_857L, 470_832L), Rational(2L).root)
    }

    @Test
    fun rootOfOneFourthIsOneHalf() {
        assertEquals(Rational(1, 2), Rational(1, 4).root)
    }
}
