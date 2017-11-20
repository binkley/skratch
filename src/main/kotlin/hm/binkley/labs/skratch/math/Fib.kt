package hm.binkley.labs.skratch.math

import java.util.Objects

class Fib(val n: Int) : Matrix2x2 {
    private val mat2x2: AnyMatrix2x2

    init {
        var mat2x2 = fib1
        var n = this.n - 1
        when {
            0 == n -> Unit
            0 < n -> {
                while (n-- > 0)
                    mat2x2 *= fib1
            }
            else -> {
                while (n++ < 0)
                    mat2x2 *= fib1.inv
            }
        }
        this.mat2x2 = mat2x2
    }

    override val rank = 2
    val char
        get() = mat2x2[0, 1]
    override val det
        get() = mat2x2.det
    override val trace
        get() = mat2x2.trace
    override val transpose
        get() = this
    override val inv
        get() = mat2x2.inv

    operator fun div(that: Fib) = Fib(n - that.n)

    inline infix fun pow(that: Int) = Fib(n * that)

    fun toMat2x2() = mat2x2

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Fib

        return n == other.n
    }

    override fun hashCode() = Objects.hash(n)

    override fun toString() = mat2x2.toString()

    companion object {
        private val fib1 = AnyMatrix2x2(0, 1, 1, 1)
        fun pow(n: Int) = Fib(n)
    }
}
