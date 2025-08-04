package hm.binkley.labs.skratch.math.matrix
import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ONE as RONE
import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ZERO as RZERO

data class Complex(
    val re: Rational,
    val im: Rational
) : GeneralNumber<Complex, Rational> {
    constructor(re: Long) : this(Rational(re), RZERO)
    constructor(re: Rational) : this(re, RZERO)
    constructor(re: Long, im: Long) : this(Rational(re), Rational(im))
    constructor(re: Long, im: Rational) : this(Rational(re), im)
    constructor(re: Rational, im: Long) : this(re, Rational(im))

    override fun unaryMinus() = Complex(-re, -im)

    override fun plus(other: Complex) = Complex(re + other.re, im + other.im)

    override fun unaryDiv() = conj / squareNorm

    override fun times(other: Complex) =
        Complex(
            re * other.re - im * other.im,
            re * other.im + im * other.re
        )

    operator fun times(other: Rational) = Complex(re * other, im * other)

    override operator fun times(other: Long) = this * Rational(other)

    override operator fun div(other: Complex) = this * other.multInv

    operator fun div(other: Rational) = Complex(re / other, im / other)

    override operator fun div(other: Long) = this / Rational(other)

    override val conj get() = Complex(re, -im)
    override val squareNorm get() = re * re + im * im
    override val absoluteValue get() = squareNorm.root as Rational

    override fun isZero() = ZERO == this

    override fun isUnit() = ONE == this

    val isReal get() = im.isZero()
    val isImaginary get() = re.isZero()

    override fun equivalent(other: GeneralNumber<*, *>) =
        when (other) {
            is Complex -> this == other
            is Rational -> RZERO == im && re == other
            else -> TODO("BUG: This is a terrible approach")
        }

    override fun toString(): String {
        if (isReal) return "$re"

        val simpleI =
            when (im) {
                RONE -> "i"
                -RONE -> "-i"
                else -> "${im}i"
            }

        return if (isImaginary) {
            simpleI
        } else {
            "$re+$simpleI"
        }
    }

    companion object {
        val ZERO = Complex(RZERO, RZERO)
        val ONE = Complex(RONE, RZERO)
        val I = Complex(RZERO, RONE)
    }
}
