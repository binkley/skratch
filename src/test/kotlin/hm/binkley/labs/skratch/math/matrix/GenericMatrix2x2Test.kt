package hm.binkley.labs.skratch.math.matrix

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class GenericMatrix2x2Test {
    @Test
    fun equivalentIfTypesEquivalent() {
        val left = GenericMatrix2x2(
            Rational.ONE,
            Rational.ZERO,
            Rational.ZERO,
            Rational.ONE
        ) {
            Rational(it)
        }
        val right = GenericMatrix2x2(
            Complex.ONE,
            Complex.ZERO,
            Complex.ZERO,
            Complex.ONE
        ) {
            Complex(it)
        }

        assertTrue(left.equivalent(right))
    }
}
