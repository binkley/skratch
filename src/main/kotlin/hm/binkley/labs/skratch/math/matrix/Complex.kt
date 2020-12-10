package hm.binkley.labs.skratch.math.matrix

data class Complex(
    val re: Rational,
    val im: Rational,
) :
    GeneralNumber<Complex, Rational> {
    constructor(re: Long) : this(Rational(re), Rational(0L))
    constructor(re: Rational) : this(re, Rational(0L))
    constructor(re: Long, im: Long) : this(Rational(re), Rational(im))
    constructor(re: Long, im: Rational) : this(Rational(re), im)
    constructor(re: Rational, im: Long) : this(re, Rational(im))

    override fun unaryMinus() = Complex(-re, -im)
    override fun plus(other: Complex) = Complex(re + other.re, im + other.im)
    override fun minus(other: Complex) = this + -other

    override fun times(other: Complex) = Complex(
        re * other.re - im * other.im,
        re * other.im + im * other.re)

    operator fun times(other: Rational) = Complex(re * other, im * other)
    override operator fun times(other: Long) = this * Rational(other)
    override operator fun div(other: Complex) =
        this * other.multInv

    operator fun div(other: Rational) = Complex(re / other, im / other)
    override operator fun div(other: Long) = this / Rational(other)

    override val multInv: Complex
        get() = conj / squareNorm
    override val conj: Complex
        get() = Complex(re, -im)
    override val absoluteValue: Rational
        get() = squareNorm.root as Rational
    override val squareNorm: Rational
        get() = re * re + im * im

    override fun isZero() = re.isZero() && im.isZero()
    override fun isUnit() = re.isUnit() && im.isZero()
    val isReal get() = im.isZero()
    val isImaginary get() = re.isZero()

    override fun equivalent(other: GeneralNumber<*, *>) = when (other) {
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
