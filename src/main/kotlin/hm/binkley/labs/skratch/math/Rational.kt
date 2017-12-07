package hm.binkley.labs.skratch.math

interface Rational<R : Rational<R>> {
    @Suppress("UNCHECKED_CAST")
    operator fun unaryPlus(): R = this as R

    operator fun unaryMinus(): R
    operator fun plus(that: R): R
    operator fun minus(that: R): R
    operator fun times(that: R): R
    operator fun div(that: R): R
    val inv: R
}
