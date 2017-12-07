package hm.binkley.labs.skratch.math

data class Complex(val re: Ratio, val im: Ratio) : Rational<Complex> {
    constructor(re: Long, im: Ratio) : this(Ratio(re), im)
    constructor(re: Long, im: Long) : this(Ratio(re), Ratio(im))
    constructor(re: Ratio, im: Long) : this(re, Ratio(im))

    override fun unaryMinus() = Complex(-re, -im)

    override fun plus(that: Complex) = Complex(re + that.re, im + that.im)

    override fun minus(that: Complex) = Complex(re - that.re, im - that.re)

    override fun times(that: Complex)
            = Complex(re * that.re - im * that.im,
            re * that.im + im * that.re)

    operator fun times(that: Ratio) = Complex(re * that, im * that)

    override fun div(that: Complex): Complex {
        val den = that.re * that.re + that.im * that.im
        return Complex((re * that.re + im * that.im) / den,
                (im * that.re - re * that.im) / den)
    }

    operator fun div(that: Ratio) = Complex(re * that.inv, im * that.inv)

    override val inv: Complex
        get() = conj / (re * re + im * im)

    val conj by lazy { Complex(re, -im) }
}
