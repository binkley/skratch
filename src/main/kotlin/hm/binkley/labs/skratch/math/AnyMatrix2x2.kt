package hm.binkley.labs.skratch.math

class AnyMatrix2x2(a: Ratio, b: Ratio, c: Ratio, d: Ratio)
    : Matrix2x2<Ratio, AnyMatrix2x2>(a, b, c, d) {
    constructor(a: Long, b: Long, c: Long, d: Long)
            : this(Ratio(a), Ratio(b), Ratio(c), Ratio(d))

    constructor(that: Matrix2x2<Ratio, *>) : this(that[1, 1], that[1, 2],
            that[2, 1], that[2, 2])

    override operator fun times(that: AnyMatrix2x2) = AnyMatrix2x2(
            this[1, 1] * that[1, 1] + this[1, 2] * that[2, 1],
            this[1, 1] * that[1, 2] + this[1, 2] * that[2, 2],
            this[2, 1] * that[1, 1] + this[2, 2] * that[2, 1],
            this[2, 1] * that[1, 2] + this[2, 2] * that[2, 2])

    operator fun times(that: Ratio)
            = AnyMatrix2x2(this[1, 1] * that, this[1, 2] * that,
            this[2, 1] * that, this[2, 2] * that)

    override val transpose by lazy { AnyMatrix2x2(a, c, b, d) }
    override val inv by lazy { AnyMatrix2x2(d, -b, -c, a) * det.inv }
}
