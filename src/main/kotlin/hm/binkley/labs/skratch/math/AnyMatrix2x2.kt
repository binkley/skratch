package hm.binkley.labs.skratch.math

data class AnyMatrix2x2(
        val a: Ratio,
        val b: Ratio,
        val c: Ratio,
        val d: Ratio)
    : Matrix2x2<AnyMatrix2x2> {
    constructor(a: Long, b: Long, c: Long, d: Long)
            : this(Ratio(a),
            Ratio(b),
            Ratio(c),
            Ratio(d))

    operator fun times(that: AnyMatrix2x2) = AnyMatrix2x2(
            a * that.a + b * that.c,
            a * that.b + b * that.d,
            c * that.a + d * that.c,
            c * that.b + d * that.d)

    operator fun times(that: Ratio)
            = AnyMatrix2x2(a * that, b * that, c * that,
            d * that)

    operator fun div(that: AnyMatrix2x2) = this * that.inv

    operator fun get(row: Int, col: Int) = when {
        row == 0 && col == 0 -> a
        row == 0 && col == 1 -> b
        row == 1 && col == 0 -> c
        row == 1 && col == 1 -> d
        else -> throw IndexOutOfBoundsException("$row, $col")
    }

    override val det
        get() = a * d - b * c
    override val trace
        get() = a + d
    override val transpose by lazy { AnyMatrix2x2(a, c, b, d) }
    override val inv by lazy { AnyMatrix2x2(d, -b, -c, a) * det.inv }

    override fun toString() = if (I == this) "I" else "[$a $b / $c $d]"

    companion object {
        val I = AnyMatrix2x2(1, 0, 0, 1)
    }
}
