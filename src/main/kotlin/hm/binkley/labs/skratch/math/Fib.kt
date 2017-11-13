package hm.binkley.labs.skratch.math

class Fib(val n: Int) {
    private val mat2x2: Mat2x2

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
                    mat2x2 *= fib1.inv()
            }
        }
        this.mat2x2 = mat2x2
    }

    fun det() = mat2x2.det()
    fun char() = mat2x2[0, 1]

    fun toMat2x2() = mat2x2

    override fun toString() = mat2x2.toString()

    companion object {
        private val fib1 = Mat2x2(0, 1, 1, 1)
        fun pow(n: Int) = fib1 pow n
    }
}
