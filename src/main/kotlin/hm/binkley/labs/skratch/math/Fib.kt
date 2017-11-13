package hm.binkley.labs.skratch.math

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

    fun toMat() = mat2

    override fun toString() = mat2.toString()

    companion object {
        private val fib1 = Mat2(0, 1, 1, 1)
        fun pow(n: Int) = fib1 pow n
    }
}
