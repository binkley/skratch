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

fun main(args: Array<String>) {
    open class Named<in T>(val name: String, check: (T) -> Boolean)
        : (T) -> Boolean by check {
        override fun toString() = name
    }

    fun do_the_three_thing() {
        println("Breakfast, lunch, dinner")
    }

    fun maybe_three(check: (Int) -> Boolean) {
        if (check(3)) do_the_three_thing()
        else println("$check says do not pass go")
    }

    maybe_three(Named("Is it three?") { i -> 3 == i })
    maybe_three(Named("Is it four?") { i -> 4 == i })

    println(::main)
    println(X("abc"))
    val q = Q()
    q["bob"] = { a, b -> "$a + $b = ${a + b}" }
    q["mary"] = S("Mary")
    q["howard"] = la("Fooby-do!")

    for ((k, v) in q)
        println("$k -> ${when (v) {
            is Function<*> -> (v as F)(1, 2)
            else -> v
        }}")
}
