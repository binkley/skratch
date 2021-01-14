@file:Suppress("NonAsciiCharacters")

package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Complex.Companion.ONE
import hm.binkley.labs.skratch.math.matrix.Complex.Companion.ZERO
import hm.binkley.labs.skratch.math.matrix.Pauli.Companion.pauli

sealed class Pauli(
    val symbol: String,
    a: Complex,
    b: Complex,
    c: Complex,
    d: Complex,
) :
    Matrix2x2<Complex, Rational, Pauli>(a, b, c, d) {
    override fun elementCtor(n: Long) = Complex(n)

    override fun matrixCtor(
        a: Complex, b: Complex, c: Complex, d: Complex,
    ) = pauli(GenericMatrix2x2(a, b, c, d, { Complex(it) }))

    override fun toString(): String = "($symbol)${super.toString()}"

    private class I : Pauli("I", ONE, ZERO, ZERO, ONE)
    private class nI : Pauli("-I", -ONE, ZERO, ZERO, -ONE)
    private class iI : Pauli("iI", Complex.I, ZERO, ZERO, Complex.I)
    private class niI : Pauli("-iI", -Complex.I, ZERO, ZERO, -Complex.I)

    private class σ1 : Pauli("σ1", ZERO, ONE, ONE, ZERO)
    private class nσ1 : Pauli("-σ1", ZERO, -ONE, -ONE, ZERO)
    private class iσ1 : Pauli("iσ1", ZERO, Complex.I, Complex.I, ZERO)
    private class niσ1 : Pauli("-iσ1", ZERO, -Complex.I, -Complex.I, ZERO)

    private class σ2 : Pauli("σ2", ZERO, -Complex.I, Complex.I, ZERO)
    private class nσ2 : Pauli("-σ2", ZERO, Complex.I, -Complex.I, ZERO)
    private class iσ2 : Pauli("iσ2", ZERO, ONE, -ONE, ZERO)
    private class niσ2 : Pauli("-σ2", ZERO, -ONE, ONE, ZERO)

    private class σ3 : Pauli("σ3", ONE, ZERO, ZERO, -ONE)
    private class nσ3 : Pauli("-σ3", -ONE, ZERO, ZERO, ONE)
    private class iσ3 : Pauli("iσ3", Complex.I, ZERO, ZERO, -Complex.I)
    private class niσ3 : Pauli("-iσ3", -Complex.I, ZERO, ZERO, Complex.I)

    companion object {
        val I: Pauli = I()
        val nI: Pauli = nI()
        val iI: Pauli = iI()
        val niI: Pauli = niI()

        val σ1: Pauli = σ1()
        val nσ1: Pauli = nσ1()
        val iσ1: Pauli = iσ1()
        val niσ1: Pauli = niσ1()

        val σ2: Pauli = σ2()
        val nσ2: Pauli = nσ2()
        val iσ2: Pauli = iσ2()
        val niσ2: Pauli = niσ2()

        val σ3: Pauli = σ3()
        val nσ3: Pauli = nσ3()
        val iσ3: Pauli = iσ3()
        val niσ3: Pauli = niσ3()

        val group = listOf(
            I, nI, iI, niI,
            σ1, nσ1, iσ1, niσ1,
            σ2, nσ2, iσ2, niσ2,
            σ3, nσ3, iσ3, niσ3)

        fun pauli(p: Matrix2x2<Complex, Rational, *>) =
            group.find { it.equivalent(p) } ?: TODO(
                "BUG: How to downgrade type when multiplying Pauli by non-{one,zero,i}? $p")
    }
}

operator fun Complex.times(other: Pauli): Pauli {
    val p = GenericMatrix2x2(
        this * other.a,
        this * other.b,
        this * other.c,
        this * other.d) {
        Complex(it)
    }

    return pauli(p)
}
