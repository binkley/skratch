package hm.binkley.labs.skratch.math

import java.util.Objects

class Fib(val n: Int) : Matrix2x2<Fib>(doIt(n)) {
    val char
        get() = b
    override val transpose
        get() = this
    override val inv: Fib by lazy { Fib(-n) }

    inline operator fun times(that: Fib) = Fib(n + that.n)
    inline operator fun div(that: Fib) = Fib(n - that.n)
    inline infix fun pow(that: Int) = Fib(n * that)

    infix fun root(that: Int) = when {
        0 != n % that -> throw ArithmeticException()
        else -> Fib(n / that)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Fib

        return n == other.n
    }

    override fun hashCode() = Objects.hash(n)

    companion object {
        private val computeFib1 = AnyMatrix2x2(0, 1, 1, 1)

        private fun doIt(n: Int): AnyMatrix2x2 {
            var mat2x2 = computeFib1
            var n = n - 1
            when {
                0 == n -> Unit
                0 < n -> {
                    while (n-- > 0)
                        mat2x2 *= computeFib1
                }
                else -> {
                    while (n++ < 0)
                        mat2x2 *= computeFib1.inv
                }
            }
            return mat2x2
        }
    }
}
