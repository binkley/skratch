package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Pauli.Companion.I
import hm.binkley.labs.skratch.math.matrix.Pauli.Companion.σ1
import hm.binkley.labs.skratch.math.matrix.Pauli.Companion.σ2
import hm.binkley.labs.skratch.math.matrix.Pauli.Companion.σ3
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PauliTest {
    @Test
    fun squaresToOne_σ1() {
        assertEquals(I, σ1 * σ1)
    }

    @Test
    fun squaresToOne_σ2() {
        assertEquals(I, σ2 * σ2)
    }

    @Test
    fun squaresToOne_σ3() {
        assertEquals(I, σ3 * σ3)
    }

    @Test
    fun squaresToOne_all() {
        assertEquals(I, -Complex.I * σ1 * σ2 * σ3)
    }
}
