package hm.binkley.labs.skratch.jdk

import java.util.concurrent.ConcurrentNavigableMap
import java.util.concurrent.ConcurrentSkipListMap

private fun Int.toAlphaLowerCase() =
    ('a'.code.toShort() + (this - 1)).toChar()

fun main() {
    val x: ConcurrentNavigableMap<String, Int> =
        object : ConcurrentSkipListMap<String, Int>(
            (1..10).map {
                it.toAlphaLowerCase().toString() to it
            }.shuffled().toMap()
        ) {
            override fun put(key: String, value: Int): Int? {
                val old = super.put(key, value)
                println("PUT -> $key: $value -> $old")
                return old
            }
        }

    println("MAP -> $x")
    println("HEAD-MAP -> ${x.headMap("f")}")
    println("TAIL-MAP -> ${x.tailMap("f")}")
    println("SUB-MAP -> ${x.subMap("c", "i")}")
}
