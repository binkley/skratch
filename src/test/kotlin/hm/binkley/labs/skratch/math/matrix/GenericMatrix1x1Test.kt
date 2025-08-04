package hm.binkley.labs.skratch.math.matrix

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class GenericMatrix1x1Test {
    @Test
    fun equivalentIfTypesEquivalent() {
        val left =
            GenericMatrix1x1(Rational.ONE) {
                Rational(it)
            }
        val right =
            GenericMatrix1x1(Complex.ONE) {
                Complex(it)
            }

        assertTrue(left.equivalent(right))
    }
}
