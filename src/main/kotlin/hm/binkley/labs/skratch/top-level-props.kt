package hm.binkley.labs.skratch

import java.util.Locale
import kotlin.reflect.KProperty

val a by APropDelegate()
var b: String = "abc"
    get() = field.uppercase(Locale.getDefault())

private class APropDelegate {
    operator fun getValue(
        thisRef: Nothing?,
        property: KProperty<*>
    ) = property
}

fun main() {
    println("A -> $a")
    println("B -> $b")
    b = "def"
    println("B -> $b")
}
