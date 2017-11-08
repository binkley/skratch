package hm.binkley.labs.skratch.math

fun main(args: Array<String>) {
    for (n in -7..7)
        println("F($n) = ${Fib(n)}")

    val fib0 = Mat2(Ratio(0), Ratio(1), Ratio(1), Ratio(1))
    println("|F(0)| = ${fib0.det()}")
    println("F(0)^-1 = ${fib0.inv()}")
    println("F(0)^-1 * F(0) = ${fib0.inv() * fib0}")
    println("F(0) * F(0)^-1 = ${fib0 * fib0.inv()}")
}

class Fib(val n: Int) {
    val a: Ratio
    val b: Ratio
    val c: Ratio
    val d: Ratio

    init {
        var mat2 = fib0
        var n = this.n
        when {
            0 == n -> Unit
            0 < n -> {
                while (n-- > 0)
                    mat2 *= fib0
            }
            else -> {
                while (n++ < 0)
                    mat2 *= fib0.inv()
            }
        }
        a = mat2.a
        b = mat2.b
        c = mat2.c
        d = mat2.d
    }

    override fun toString() = "[$a $b / $c $d]"

    companion object {
        private val fib0 = Mat2(Ratio(0), Ratio(1), Ratio(1), Ratio(1))
    }
}

data class Mat2(val a: Ratio, val b: Ratio, val c: Ratio, val d: Ratio) {
    operator fun times(that: Mat2) = Mat2(
            a * that.a + b * that.c,
            a * that.b + b * that.d,
            c * that.a + d * that.c,
            c * that.b + d * that.d)

    operator fun times(that: Ratio)
            = Mat2(a * that, b * that, c * that, d * that)

    fun det() = a * d - b * c

    fun inv() = Mat2(d, -b, -c, a) * det().inv()

    override fun toString() = "[$a $b / $c $d]"
}

class Ratio(n: Long, d: Long) {
    val n: Long
    val d: Long

    init {
        val gcm = gcm(n, d)
        this.n = n / gcm
        this.d = d / gcm
    }

    constructor(n: Long) : this(n, 1)

    operator fun unaryPlus() = this
    operator fun unaryMinus() = Ratio(-n, d)

    operator fun plus(that: Ratio)
            = Ratio(n * that.d + that.n * d, d * that.d)

    operator fun plus(that: Long) = this + Ratio(that)

    operator fun minus(that: Ratio) = this + -that
    operator fun minus(that: Long) = this + -that

    operator fun times(that: Ratio) = Ratio(n * that.n, d * that.d)
    operator fun times(that: Long) = this * Ratio(that)
    operator fun div(that: Ratio) = this * that.inv()
    operator fun div(that: Long) = this / Ratio(that)

    fun inv() = Ratio(d, n)

    override fun toString() = if (1L == d) n.toString() else "$n/$d"

    companion object {
        fun gcm(a: Long, b: Long): Long = if (b == 0L) a else gcm(b, a % b)
    }
}

operator fun Long.plus(that: Ratio) = Ratio(this) + that
operator fun Long.minus(that: Ratio) = Ratio(this) - that
operator fun Long.times(that: Ratio) = Ratio(this) * that
operator fun Long.div(that: Ratio) = Ratio(this) / that
