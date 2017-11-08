package hm.binkley.labs.skratch.math

fun main(args: Array<String>) {
    for (n in -6..6) {
        val fib = Fib(n)
        println("f($n) = ${fib.char()}, F($n) = $fib, 1/F($n) = ${fib.asMatrix().inv()}, |F($n)| = ${fib.det()}")
    }

    println()

    val a = (-6..6).map(::Fib).map(Fib::char)
    val b = (-6..6).map(::Fib).map { -it.char() * it.det() }.reversed()

    println(a)
    println(b)

    println()

    val p = (-6..6).map(::Fib)
    val q = (-6..6).map(::Fib).map { it.asMatrix().inv() }.reversed()

    println(q)
    println(q)
    println("FIX ME - p == q ? ${p == q}")

    println()

    val fib1 = Mat2(0, 1, 1, 1)
    println("F(1)^-1 = ${fib1.inv()}")
    println("F(1)^-1 * F(1) = ${fib1.inv() * fib1}")
    println("F(1) * F(1)^-1 = ${fib1 * fib1.inv()}")

    println()

    for (n in -1..3)
        println("F(1)^$n = ${Fib.pow(n)}")
}

class Fib(val n: Int) {
    private val mat2: Mat2

    init {
        var mat2 = fib1
        var n = this.n - 1
        when {
            0 == n -> Unit
            0 < n -> {
                while (n-- > 0)
                    mat2 *= fib1
            }
            else -> {
                while (n++ < 0)
                    mat2 *= fib1.inv()
            }
        }
        this.mat2 = mat2
    }

    fun det() = mat2.det()
    fun char() = mat2[0, 1]

    fun asMatrix() = mat2

    override fun toString() = mat2.toString()

    companion object {
        private val fib1 = Mat2(0, 1, 1, 1)
        fun pow(n: Int) = fib1.pow(n)
    }
}
