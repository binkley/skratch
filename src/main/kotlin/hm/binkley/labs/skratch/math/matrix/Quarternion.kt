package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ONE as RONE
import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ZERO as RZERO

data class Quarternion(
    val re: Rational,
    val i: Rational,
    val j: Rational,
    val k: Rational,
) : GeneralNumber<Quarternion, Rational> {
    constructor(re: Rational) : this(re, RZERO, RZERO, RZERO)
    constructor(re: Long) : this(Rational(re), RZERO, RZERO, RZERO)
    constructor(re: Long, i: Long, j: Long, k: Long) :
            this(Rational(re), Rational(i), Rational(j), Rational(k))

    override fun unaryMinus() = Quarternion(-re, -i, -j, -k)
    override fun plus(other: Quarternion) = Quarternion(
        re + other.re, i + other.i, j + other.j, k + other.k
    )

    override fun unaryDiv() = conj / squareNorm

    /**
     * ```
     * q1 = a + bi + cj + dk
     * q2 = e + fi + gj + hk
     * ∴
     * q1*q2 = a*e - b*f - c*g - d*h
     *         + i (b*e + a*f + c*h - d*g)
     *         + j (a*g - b*h + c*e + d*f)
     *         + k (a*h + b*g - c*f + d*e)
     * ```
     */
    override fun times(other: Quarternion) = Quarternion(
        re * other.re - i * other.i - j * other.j - k * other.k,
        i * other.re + re * other.i + j * other.k - k * other.j,
        re * other.j - i * other.k + j * other.re + k * other.i,
        re * other.k + i * other.j - j * other.i + k * other.re
    )

    operator fun times(other: Rational) =
        Quarternion(re * other, i * other, j * other, k * other)

    override operator fun times(other: Long) = this * Rational(other)
    override operator fun div(other: Quarternion) = this * other.multInv

    operator fun div(other: Rational) =
        Quarternion(re / other, i / other, j / other, k / other)

    override operator fun div(other: Long) = this / Rational(other)

    override val conj get() = Quarternion(re, -i, -j, -k)
    override val squareNorm get() = re * re + i * i + j * j + k * k
    override val absoluteValue get() = squareNorm.root as Rational

    override fun isZero() = ZERO == this
    override fun isUnit() = ONE == this

    val isReal get() = i.isZero() && j.isZero() && k.isZero()

    override fun equivalent(other: GeneralNumber<*, *>) = when (other) {
        is Quarternion -> this == other
        is Rational -> RZERO == i && re == other
        else -> TODO("BUG: This is a terrible approach")
    }

    override fun toString() =
        if (isReal) "$re"
        else "$re+${i}i+${j}j+${k}k"

    companion object {
        val ZERO = Quarternion(RZERO, RZERO, RZERO, RZERO)
        val ONE = Quarternion(RONE, RZERO, RZERO, RZERO)
        val I = Quarternion(RZERO, RONE, RZERO, RZERO)
        val J = Quarternion(RZERO, RZERO, RONE, RZERO)
        val K = Quarternion(RZERO, RZERO, RZERO, RONE)
    }
}
