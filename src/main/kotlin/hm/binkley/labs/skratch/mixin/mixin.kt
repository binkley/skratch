package hm.binkley.labs.skratch.mixin

import java.lang.annotation.Inherited
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.superclasses

fun main() {
    println("MIXINS")
    val rice = Rice()
    rice.attend()
    with(rice) {
        println("BOB -> $bob")
        println("NANCY -> $nancy")
    }

    // REFLECTION TIME

    println("SUPERCLASSES OF RICE:")
    rice::class.superclasses.forEach {
        println("- $it")
        KClass::class
            .memberProperties
            .filter { prop -> prop.name.startsWith("is") }
            .filter { prop -> prop.get(it) as Boolean }
            .map { prop -> prop.name }
            .forEach { prop -> println("  * $prop") }
    }
    println("MEMBERS OF RICE:")
    rice::class.memberProperties.forEach { uncast ->
        // TODO: This is weird -- `rice::class` is KClass<out Rice>.  The
        //  `out` makes it ineligible to use the "rice" instance as the
        //  parameter for `getDelegate`.  In fact, leaving `out` makes
        //  `getDelegate` look for `Nothing` as the parameter type
        @Suppress("UNCHECKED_CAST")
        val it = uncast as KProperty1<Rice, *>
        println("- $it")
        println("  - delegate: ${it.getDelegate(rice)}")
        println("  - annotations:")
        it.annotations.forEach {
            println("    * $it")
        }
    }
}

@Inherited
@Target(FIELD)
annotation class College(
    val name: String
)

abstract class University {
    fun attend() = Unit
}

class Rice :
    University(),
    Fooby by Fooby.Mixin()

interface Fooby {
    var bob: String
    var nancy: String

    class Mixin(
        @College("Baker")
        override var bob: String = "Hi, Nancy! It's Bob.",
        @College("Baker")
        override var nancy: String = "Hi, Bob! It's Nancy."
    ) : Fooby
}
