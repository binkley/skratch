package hm.binkley.labs.skratch

import hm.binkley.labs.skratch.BDD.Companion.So
import hm.binkley.labs.skratch.Bar.Companion.bar

class Bar {
    infix fun foo(body: (b: Bar) -> Bar): Bar {
        println("foo: " + this)
        println("body: " + body)
        return body(this)
    }

    infix fun foo(b: Bar): Bar {
        println("foo: " + this)
        println("b: " + b)
        return b
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

data class BDD constructor(val GIVEN: String, val WHEN: String, val THEN: String) {
    companion object {
        val So = So()
    }

    class So {
        infix fun GIVEN(GIVEN: String): Given = Given(GIVEN)
        data class Given(private val GIVEN: String) {
            infix fun WHEN(WHEN: String): When = When(GIVEN, WHEN)
            data class When(private val GIVEN: String, private val WHEN: String) {
                infix fun THEN(THEN: String): BDD = BDD(GIVEN, WHEN, THEN)
            }
        }
    }
}

infix fun Int.foo(b: Int) = this + b

fun main(args: Array<String>) {
    println(1 foo 2)

    bar foo bar foo {
        println("body: " + it)
        it
    } bob {
        println("body: " + it)
        it
    }

    println(So GIVEN "an apple" WHEN "it falls" THEN "Newton thinks")
}
