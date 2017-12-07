package hm.binkley.labs.skratch.math.matrix

data class Complex(val re: Rational, val im: Rational) : Number<Complex> {
    constructor(re: Long) : this(Rational(re), Rational(0L))
    constructor(re: Rational) : this(re, Rational(0L))

    override fun unaryMinus() = Complex(-re, -im)
    override fun plus(that: Complex) = Complex(re + that.re, im + that.im)
    override fun minus(that: Complex) = this + -that

    override fun times(that: Complex) = Complex(
            re * that.re - im * that.im,
            re * that.im + im * that.re)

    operator fun times(that: Rational) = Complex(re * that, im * that)
    override operator fun times(that: Long) = this * Rational(that)
    override operator fun div(that: Complex) = this * that.inv
    operator fun div(that: Rational) = Complex(re / that, im / that)
    override operator fun div(that: Long) = this / Rational(that)

    override val inv: Complex
        get() = conj / (re * re + im * im)
    override val conj: Complex
        get() = Complex(re, -im)
    override val abs: Complex
        get() = TODO("Abs of a complex is not complex -- help!")

    override fun isZero() = re.isZero() && im.isZero()
    override fun isUnit() = re.isUnit() && im.isZero()

    override fun toString() = "%s+%si".format(re, im)
}
