package hm.binkley.labs.skratch.math

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ComplexTest {
    @Test
    fun shouldConjugate() {
        assertEquals(Complex(1, -1), Complex(1, 1).conj)
    }

    @Test
    fun shouldDivide() {
        assertEquals(Complex(1, 0), Complex(1, 1) / Complex(1, 1))
    }
}
