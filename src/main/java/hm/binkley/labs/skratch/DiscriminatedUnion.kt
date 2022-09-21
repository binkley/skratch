package hm.binkley.labs.skratch

import hm.binkley.labs.skratch.DiscriminatedUnion.Bob

sealed interface DiscriminatedUnion {
    fun doIt()

    @JvmInline
    value class Bob(val x: Int): DiscriminatedUnion {
        override fun doIt() {
            println("I AM A BOB: $this")
        }
    }
}

fun main() {
    println(Bob(3))
}
