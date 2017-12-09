package hm.binkley.labs.skratch.math.matrix

interface Multiplicative<T : Multiplicative<T>> {
    operator fun times(that: T): T
    operator fun div(that: T): T
    val inv: T

    fun isUnit(): Boolean
}
