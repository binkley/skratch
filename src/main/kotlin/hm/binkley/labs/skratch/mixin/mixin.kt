package hm.binkley.labs.skratch.mixin

fun main() {
    println("MIXINS")
    val rice = Rice()
    rice.attend()
    with(rice) {
        println("BOB -> $bob")
        println("NANCY -> $nancy")
    }
}

abstract class University {
    fun attend() = Unit
}

class Rice : University(), Fooby by Fooby.Mixin()

interface Fooby {
    var bob: String
    var nancy: String

    class Mixin(
        override var bob: String = "Hi, Nancy! It's Bob.",
        override var nancy: String = "Hi, Bob! It's Nancy.",
    ) : Fooby
}
