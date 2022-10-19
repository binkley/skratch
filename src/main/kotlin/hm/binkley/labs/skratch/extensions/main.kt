package hm.binkley.labs.skratch.extensions

import hm.binkley.labs.skratch.extensions.Y.Companion.average
import hm.binkley.labs.skratch.extensions.Y.Companion.sum

fun main() {
    println(Z.ZERO)
    println(Y.ZERO)
    println(Y.ONE)
    println("AVG -> ${listOf(Y.ZERO, Y.ONE).average()}")
    println("SUM -> ${listOf(Y.ZERO, Y.ONE).sum()}")
}

abstract class CBase<T : XBase<T>> {
    abstract val ZERO: T
    abstract val ONE: T

    abstract fun valueOf(v: Int): T

    fun Iterable<T>.average() = sum() / count()

    fun Iterable<T>.sum() = sumOf { it }

    fun <E> Iterable<E>.sumOf(selector: (E) -> T) =
        fold(ZERO) { acc, element -> acc + selector(element) }
}

abstract class XBase<T : XBase<T>>(
    val companion: CBase<T>,
    val v: Int
) {
    operator fun plus(other: T): T = companion.valueOf(v + other.v)
    operator fun div(other: Int): T = companion.valueOf(v / other)

    override fun toString() = "${javaClass.simpleName}($v)"
}

class Y private constructor(v: Int) : XBase<Y>(Y, v) {
    companion object : CBase<Y>() {
        override val ZERO = 0.y
        override val ONE = 1.y

        override fun valueOf(v: Int) = Y(v)
    }
}

val Int.y: Y get() = Y.valueOf(this)

class Z private constructor(v: Int) : XBase<Z>(Z, v) {
    companion object : CBase<Z>() {
        override val ZERO = 0.z
        override val ONE = 1.z

        override fun valueOf(v: Int) = Z(v)
    }
}

val Int.z: Z get() = Z.valueOf(this)
