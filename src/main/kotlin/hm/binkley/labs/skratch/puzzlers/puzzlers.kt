package hm.binkley.labs.skratch.puzzlers

fun printSign(i: Int) = when {
    i < 0 -> "negative"
    i > 0 -> "positive"
    else -> "zero"
}.let { println(it) }

fun main() {
    printSign(-1)
    printSign(0)
    printSign(1)
}
