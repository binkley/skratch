package hm.binkley.labs.skratch.math

data class Mat2x2(val a: Ratio, val b: Ratio, val c: Ratio, val d: Ratio) {
    constructor(a: Long, b: Long, c: Long, d: Long)
            : this(Ratio(a),
            Ratio(b),
            Ratio(c),
            Ratio(d))

    operator fun times(that: Mat2x2) = Mat2x2(
            a * that.a + b * that.c,
            a * that.b + b * that.d,
            c * that.a + d * that.c,
            c * that.b + d * that.d)

    operator fun times(that: Ratio)
            = Mat2x2(a * that, b * that, c * that,
            d * that)

    operator fun div(that: Mat2x2) = this * that.inv()

    operator fun get(row: Int, col: Int) = when {
        row == 0 && col == 0 -> a
        row == 0 && col == 1 -> b
        row == 1 && col == 0 -> c
        row == 1 && col == 1 -> d
        else -> throw IndexOutOfBoundsException("$row, $col")
    }

    fun det() = a * d - b * c

    fun trace() = a + d

    fun inv() = Mat2x2(d, -b, -c, a) * det().inv()

    inline infix fun pow(n: Int) = Fib(n)

    override fun toString() = if (I == this) "I" else "[$a $b / $c $d]"

    companion object {
        val I = Mat2x2(1, 0, 0, 1)
    }
}
