package hm.binkley.labs.skratch.guard

// See:
// https://medium.com/swlh/swifts-guard-statement-for-kotlin-967ba580443f
// https://gist.github.com/Aidanvii7/5c80a6008a934a95b579a307db8e19bb

data class GuardedException(val guardBroken: Any) : Exception()

fun <V> V?.guard(
    guardedBlock: V.() -> Boolean,
    alternativeBlock: (V?) -> Any = {},
): V =
    if (this != null && guardedBlock(this)) this
    else throw GuardedException(alternativeBlock(this))

fun main() = try {
    var username = "bob".guard(String::isNotEmpty) {
        println("username is blank")
    }
    println(username)

    val s: String? = null
    username = s.guard(String::isNotEmpty) {
        println("username is blank")
    }
    println(username)
} catch (e: GuardedException) {
    e.printStackTrace()
}
