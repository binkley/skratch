package hm.binkley.labs.skratch

import kotlin.reflect.KProperty

val a by APropDelegate()
var b: String = "abc"
    get() = field.toUpperCase()

private class APropDelegate {
    operator fun getValue(thisRef: Nothing?, property: KProperty<*>) =
        property
}

fun main() {
    println("A -> $a")
    println("B -> $b")
    b = "def"
    println("B -> $b")
}
