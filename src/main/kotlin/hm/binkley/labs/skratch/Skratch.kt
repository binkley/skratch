package hm.binkley.labs.skratch

import hm.binkley.labs.skratch.Bar.Companion.bar

class Bar {
    infix fun foo(body: (b: Bar) -> Bar): Bar {
        println("foo: " + this)
        println("body: " + body)
        return body(this)
    }

    infix fun bob(body: (b: Bar) -> Bar): Bar {
        println("bob: " + this)
        println("body: " + body)
        return body(this)
    }

    companion object {
        val bar = Bar()
    }
}

infix fun Int.foo(b: Int) = this + b

fun main(args: Array<String>) {
    println(1 foo 2)

    bar foo {
        println("body: " + it)
        it
    } bob {
        println("body: " + it)
        it
    }
}
