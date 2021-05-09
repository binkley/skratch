@file:Suppress("FunctionName")

package hm.binkley.labs.skratch

import hm.binkley.labs.skratch.Q.Companion.la
import hm.binkley.labs.skratch.puzzlers.main

typealias F = (Int, Int) -> String

class X(private val a: String) : CharSequence by a {
    override fun toString() = a
}

class Q : MutableMap<String, Any> by mutableMapOf() {
    operator fun <T> set(key: String, x: (a: Int, b: Int) -> T) {
        this[key] = x as Any
    }

    companion object {
        fun la(s: String) = { _: Int, _: Int -> s }
    }
}

class S(private val s: String) {
    override fun toString() = s
}

fun main() {
    open class Named<in T>(val name: String, check: (T) -> Boolean) :
            (T) -> Boolean by check {
        override fun toString() = name
    }

    fun `do the three things`() {
        println("Breakfast, lunch, dinner")
    }

    fun `maybe three`(check: (Int) -> Boolean) {
        if (check(3)) `do the three things`()
        else println("$check says do not pass go")
    }

    `maybe three`(Named("Is it three?") { i -> 3 == i })
    `maybe three`(Named("Is it four?") { i -> 4 == i })

    println(::main)
    println(X("abc"))
    val q = Q()
    q["bob"] = { a, b -> "$a + $b = ${a + b}" }
    q["mary"] = S("Mary")
    q["howard"] = la("Fooby-do!")

    @Suppress("UNCHECKED_CAST")
    for ((k, v) in q)
        println("$k -> ${
            if (v is Function<*>) (v as F)(1, 2) else v
        }")

    println()
    println("== FAKE CTOR")
    val tripled = A(3) { 3 * it }
    println("TRIPLED -> $tripled")

    println()
    val j = J()
    val k = K()
    k.kkk(j) { this.hashCode() }

    println()
    println("== JAVA INTEROP")
    println(Point(1, 2))
}

data class A<T>(val list: List<T>) {
    // Equivalent secondary ctor to the fake ctor:
    // constructor(size: Int, init: (Int) -> T) : this(List(size, init))
}

fun <T> A(size: Int, init: (Int) -> T): A<T> {
    val list = ArrayList<T>(size)
    repeat(size) { list.add(init(it)) }
    return A(list)
}

class J
class K

fun K.kkk(j: J, f: J.() -> Int): Unit = println("$this -> $j -> ${f(j)}")
