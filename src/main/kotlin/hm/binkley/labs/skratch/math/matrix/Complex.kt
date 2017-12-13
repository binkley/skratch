package hm.binkley.labs.skratch.math.matrix

data class Complex(val re: Rational, val im: Rational)
    : Number<Complex, Rational> {
    constructor(re: Long) : this(Rational(re), Rational(0L))
    constructor(re: Rational) : this(re, Rational(0L))
    constructor(re: Long, im: Long) : this(Rational(re), Rational(im))

    override fun unaryMinus() = Complex(-re, -im)
    override fun plus(other: Complex) = Complex(re + other.re, im + other.im)
    override fun minus(other: Complex) = this + -other

    override fun times(other: Complex) = Complex(
            re * other.re - im * other.im,
            re * other.im + im * other.re)

    operator fun times(other: Rational) = Complex(re * other, im * other)
    override operator fun times(other: Long) = this * Rational(other)
    override operator fun div(other: Complex) = this * other.inv
    operator fun div(other: Rational) = Complex(re / other, im / other)
    override operator fun div(other: Long) = this / Rational(other)

    override val inv: Complex
        get() = conj / sqnorm
    override val conj: Complex
        get() = Complex(re, -im)
    override val abs: Rational
        get() = sqnorm.root
    override val sqnorm: Rational
        get() = re * re + im * im

    override fun isZero() = re.isZero() && im.isZero()
    override fun isUnit() = re.isUnit() && im.isZero()

    override fun equivalent(other: Number<*, *>) = when (other) {
        is Complex -> re == other.re && im == other.im
        is Rational -> Rational.ZERO == im && re.n == other.n && re.d == other.d
        else -> TODO("BUG: This is a terrible approach")
    }

    override fun toString() = "$re+${im}i"

    companion object {
        val ZERO = Complex(0L)
        val ONE = Complex(1L)
        val I = Complex(0L, 1L)
    }
}
