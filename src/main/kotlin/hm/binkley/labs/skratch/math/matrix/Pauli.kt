package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Complex.Companion.I
import hm.binkley.labs.skratch.math.matrix.Complex.Companion.ONE
import hm.binkley.labs.skratch.math.matrix.Complex.Companion.ZERO

class Pauli private constructor(
        a: Complex, b: Complex, c: Complex, d: Complex)
    : Matrix2x2<Complex, Rational, Pauli>(a, b, c, d) {
    override fun elementCtor(n: Long) = Complex(n)

    override fun matrixCtor(a: Complex, b: Complex, c: Complex, d: Complex)
            = Pauli(a, b, c, d)

    companion object {
        val σ0 = Pauli(ONE, ZERO, ZERO, ONE)
        val σ1 = Pauli(ZERO, ONE, ONE, ZERO)
        val σ2 = Pauli(ZERO, -I, I, ZERO)
        val σ3 = Pauli(ONE, ZERO, ZERO, -ONE)
    }
}
