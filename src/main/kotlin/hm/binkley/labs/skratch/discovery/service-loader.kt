package hm.binkley.labs.skratch.discovery

import java.util.ServiceLoader

fun main() {
    val sl = ServiceLoader.load(LoadMe::class.java)
    sl.forEach { println(it) }
}
