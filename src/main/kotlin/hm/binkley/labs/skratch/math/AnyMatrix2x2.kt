package hm.binkley.labs.skratch.math

class AnyMatrix2x2(a: Ratio, b: Ratio, c: Ratio, d: Ratio)
    : Matrix2x2<AnyMatrix2x2>(a, b, c, d) {
    constructor(a: Long, b: Long, c: Long, d: Long)
            : this(Ratio(a), Ratio(b), Ratio(c), Ratio(d))

    constructor(that: Matrix2x2<*>) : this(that.a, that.b, that.c, that.d)

    override operator fun times(that: AnyMatrix2x2) = AnyMatrix2x2(
            a * that.a + b * that.c,
            a * that.b + b * that.d,
            c * that.a + d * that.c,
            c * that.b + d * that.d)

    operator fun times(that: Ratio)
            = AnyMatrix2x2(a * that, b * that, c * that,
            d * that)

    override operator fun div(that: AnyMatrix2x2) = this * that.inv

    override val transpose by lazy { AnyMatrix2x2(a, c, b, d) }
    override val inv by lazy { AnyMatrix2x2(d, -b, -c, a) * det.inv }
}
