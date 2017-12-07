package hm.binkley.labs.skratch.math

import java.util.Objects

abstract class Matrix2x2<R : Rational<R>, M : Matrix2x2<R, M>>(
        private val a: Ratio, private val b: Ratio,
        private val c: Ratio, private val d: Ratio) {
    val rank = 2
    open val det
        get() = a * d - b * c
    open val trace
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
    open operator fun div(that: M) = this * that.inv

    override fun toString() = if (I == this) "I" else "[$a $b / $c $d]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix2x2<*, *>

        return a == other.a && b == other.b && c == other.c && d == other.d
    }

    override fun hashCode() = Objects.hash(a, b, c, d)

    companion object {
        val I = GeneralMatrix2x2(1, 0, 0, 1)
    }
}
