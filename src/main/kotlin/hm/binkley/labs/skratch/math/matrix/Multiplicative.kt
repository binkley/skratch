package hm.binkley.labs.skratch.math.matrix

interface Multiplicative<T : Multiplicative<T>> {
    operator fun times(that: T): T
    operator fun times(that: Long): T
    operator fun div(that: T): T
    operator fun div(that: Long): T
    val inv: T

    fun isUnit(): Boolean
}
