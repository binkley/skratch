package hm.binkley.labs.skratch.math

fun main(args: Array<String>) {
    for (n in 0..7)
        println(Fib(n))
}

class Fib(val n: Int) {
    val a: Ratio
    val b: Ratio
    val c: Ratio
    val d: Ratio

    init {
        val fib0 = Mat2(Ratio(0), Ratio(1), Ratio(1), Ratio(1))
        var mat2 = fib0
        var n = this.n
        while (n-- > 0)
            mat2 *= fib0
        a = mat2.a
        b = mat2.b
        c = mat2.c
        d = mat2.d
    }

    override fun toString() = "[$a $b / $c $d]"
}

data class Mat2(val a: Ratio, val b: Ratio, val c: Ratio, val d: Ratio) {
    operator fun times(that: Mat2): Mat2 {
        return Mat2(a * that.a + b * that.c,
                a * that.d + b * that.b,
                a * that.d + c * that.c,
                c * that.b + d * that.d)
    }
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

    operator fun plus(that: Ratio)
            = Ratio(n * that.d + that.n * d, d * that.d)

    operator fun times(that: Ratio) = Ratio(n * that.n, d * that.d)

    override fun toString() = if (1L == d) n.toString() else "$n/$d"

    companion object {
        fun gcm(a: Long, b: Long): Long = if (b == 0L) a else gcm(b, a % b)
    }
}
