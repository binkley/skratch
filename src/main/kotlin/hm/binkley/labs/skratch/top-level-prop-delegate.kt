package hm.binkley.labs.skratch

import kotlin.reflect.KProperty

val a by APropDelegate()

private class APropDelegate {
    operator fun getValue(thisRef: Nothing?, property: KProperty<*>) =
        property
}

fun main() {
    println("A -> $a")
}
