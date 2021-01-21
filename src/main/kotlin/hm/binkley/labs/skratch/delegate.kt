package hm.binkley.labs.skratch

fun main() {
    data class Foo(private val map: MutableMap<String, Any?>) :
        MutableMap<String, Any?> by map {
        val name: String by this
        val number: Int by this
    }

    val foo = Foo(mutableMapOf(
        "name" to "BOB",
        "number" to 42,
        "pi" to 3.14159,
    ))

    println("AGE -> ${foo.name}")
    println("NUMBER -> ${foo.number}")
    println("PI -> ${foo["pi"]}")
}
