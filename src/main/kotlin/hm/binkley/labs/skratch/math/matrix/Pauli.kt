package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Complex.Companion.ONE
import hm.binkley.labs.skratch.math.matrix.Complex.Companion.ZERO
import hm.binkley.labs.skratch.math.matrix.Pauli.Companion.I
import hm.binkley.labs.skratch.math.matrix.Pauli.Companion.σ1
import hm.binkley.labs.skratch.math.matrix.Pauli.Companion.σ2
import hm.binkley.labs.skratch.math.matrix.Pauli.Companion.σ3

class Pauli private constructor(
        a: Complex, b: Complex, c: Complex, d: Complex)
    : Matrix2x2<Complex, Rational, Pauli>(a, b, c, d) {
    override fun elementCtor(n: Long) = Complex(n)

    override fun matrixCtor(a: Complex, b: Complex, c: Complex, d: Complex)
            = Pauli(a, b, c, d)

    companion object {
        val I = Pauli(ONE, ZERO, ZERO, ONE)
        val σ1 = Pauli(ZERO, ONE, ONE, ZERO)
        val σ2 = Pauli(ZERO, -Complex.I, Complex.I, ZERO)
        val σ3 = Pauli(ONE, ZERO, ZERO, -ONE)
    }
}

operator fun Complex.times(other: Pauli): Pauli {
    val p = GeneralMatrix2x2(this * other.a, this * other.b, this * other.c,
            this * other.d) { Complex(it) }
    return when {
        I.equivalent(p) -> I
        σ1.equivalent(p) -> σ1
        σ2.equivalent(p) -> σ2
        σ3.equivalent(p) -> σ3
        else -> throw IllegalArgumentException(
                "Not closed for Pauli matrices")
    }
}
