package hm.binkley.labs.skratch.math

abstract class Matrix2x2<M : Matrix2x2<M>>(
        val a: Ratio, val b: Ratio, val c: Ratio, val d: Ratio) {
    constructor(that: AnyMatrix2x2) : this(that.a, that.b, that.c, that.d)

    val rank = 2
    val det
        get() = a * d - b * c
    val trace
        get() = a + d
    abstract val transpose: M
    abstract val inv: M

    override fun toString() = if (I == this) "I" else "[$a $b / $c $d]"

    companion object {
        val I = AnyMatrix2x2(1, 0, 0, 1)
    }
}
