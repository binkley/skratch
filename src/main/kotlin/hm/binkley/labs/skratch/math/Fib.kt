package hm.binkley.labs.skratch.math

import java.util.Objects

class Fib
private constructor(private val n: Int,
        a: Ratio, b: Ratio, c: Ratio, d: Ratio)
    : Matrix2x2<Ratio, Fib>(a, b, c, d) {
    val char
        get() = this[1, 2]
    override val transpose
        get() = this
    override val inv: Fib by lazy { fib(-n) }

    override operator fun times(that: Fib) = fib(n + that.n)
    override operator fun div(that: Fib) = fib(n - that.n)
    infix fun pow(that: Int) = fib(n * that)

    infix fun root(that: Int) = when {
        0 != n % that -> throw ArithmeticException()
        else -> fib(n / that)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Fib

        return n == other.n
    }

    override fun hashCode() = Objects.hash(n)

    companion object {
        fun fib(n: Int): Fib {
            val m = doIt(n)
            return Fib(n, m[1, 1], m[1, 2], m[2, 1], m[2, 2])
        }

        private val computeFib1 = AnyMatrix2x2(0, 1, 1, 1)

        private fun doIt(n: Int): AnyMatrix2x2 {
            var mat2x2 = computeFib1
            var k = n - 1
            when {
                0 == k -> Unit
                0 < k -> {
                    while (k-- > 0)
                        mat2x2 *= computeFib1
                }
                else -> {
                    while (k++ < 0)
                        mat2x2 *= computeFib1.inv
                }
            }
            return mat2x2
        }
    }
}
