package hm.binkley.labs.skratch.math

abstract class Matrix2x2<M : Matrix2x2<M>>(
        private val a: Ratio, private val b: Ratio,
        private val c: Ratio, private val d: Ratio) {
    constructor(that: AnyMatrix2x2) : this(that[1, 1], that[1, 2],
            that[2, 1], that[2, 2])

    val rank = 2
    val det
        get() = a * d - b * c
    val trace
        get() = a + d
    abstract val transpose: M
    abstract val inv: M

    operator fun get(row: Int, col: Int) = when {
        row == 1 && col == 1 -> a
        row == 1 && col == 2 -> b
        row == 2 && col == 1 -> c
        row == 2 && col == 2 -> d
        else -> throw IndexOutOfBoundsException("$row, $col")
    }

    abstract operator fun times(that: M): M
    abstract operator fun div(that: M): M

    override fun toString() = if (I == this) "I" else "[$a $b / $c $d]"

    companion object {
        val I = AnyMatrix2x2(1, 0, 0, 1)
    }
}
