package hm.binkley.labs.skratch.puzzlers

fun printSign(i: Int) {
    if (i < 0) {
        "negative"
    } else if (i > 0) {
        "positive"
    } else {
        "zero"
    }.let { println(it) }
}

fun main(args: Array<String>) {
    printSign(-1)
    printSign(0)
    printSign(1)
}
