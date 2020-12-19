package hm.binkley.labs.skratch.guard

// See:
// https://medium.com/swlh/swifts-guard-statement-for-kotlin-967ba580443f
// https://gist.github.com/Aidanvii7/5c80a6008a934a95b579a307db8e19bb

class GuardedException(val guardBroken: () -> Any) : Exception()

fun <V> V?.guard(
    guardedBlock: V.() -> Boolean?,
    alternativeBlock: () -> Any = {},
) =
    if (this != null && guardedBlock(this) == true) this
    else throw GuardedException(alternativeBlock)

fun main() {
    var username = "bob".guard(String::isNotEmpty) {
        println("username is blank")
    }
    println(username)

    username = null.guard(String::isNotEmpty) {
        println("username is blank")
    }
}
