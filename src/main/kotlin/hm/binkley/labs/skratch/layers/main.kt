package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.examples.EnumyKey.AbstractEnumyKey
import hm.binkley.labs.skratch.layers.examples.EnumyLayers
import hm.binkley.labs.skratch.layers.examples.LARGE
import hm.binkley.labs.skratch.layers.examples.LEFT
import hm.binkley.labs.skratch.layers.examples.Large
import hm.binkley.labs.skratch.layers.examples.Left
import hm.binkley.labs.skratch.layers.examples.MyLayer
import hm.binkley.labs.skratch.layers.examples.MyLayers
import hm.binkley.labs.skratch.layers.examples.MyWordLayer
import hm.binkley.labs.skratch.layers.examples.SMALL
import hm.binkley.labs.skratch.layers.examples.Small
import hm.binkley.labs.skratch.layers.rules.LastOrDefaultRule
import hm.binkley.labs.skratch.layers.rules.ceilRule
import hm.binkley.labs.skratch.layers.rules.constantRule
import hm.binkley.labs.skratch.layers.rules.floorRule
import hm.binkley.labs.skratch.layers.rules.lastOrDefaultRule
import kotlin.random.Random

fun main() {
    val layers = MyLayers()

    println("--- ADD NOT EMPTY")
    val lastOrNegativeOne = LastOrDefaultRule<String, Number, Number>(-1)
    println(
        layers.push { index ->
            MyLayer(index, mapOf("STRING" to lastOrNegativeOne))
        }
    )
    println(
        layers.push { index ->
            MyLayer(index, mapOf("STRING" to 3.toValue()))
        }
    )
    println("LAYERS -> ${layers.display()}")
    println("STRING -> ${layers["STRING"]}")
    println("--- ADD CUSTOM")
    println(layers.push(layers::doHickey))
    println("--- ADD VIA BLOCK")
    println(
        layers.push {
            this["BOB"] = lastOrDefaultRule(-1)
        }
    )
    println(
        layers.push {
            this["BOB"] = 77
        }
    )
    println("LAYERS -> ${layers.display()}")
    println("STRING -> ${layers["STRING"]}")

    // TODO: Can the <T> be avoided?
    val wordy = layers.push<MyWordLayer>(::MyWordLayer).apply {
        ohMy()
        myWord()
    }

    println("-- MORE EXTENDING")
    println("WORDY -> $wordy")
    println("LAYERS -> ${layers.display()}")
    println("STRING -> ${layers["STRING"]}")

    val enumyLayers = EnumyLayers()
    println("-- ENUM-Y INIT")
    println("LAYERS -> ${enumyLayers.display()}")

    enumyLayers.edit {
        LEFT = 7
    }
    enumyLayers.push {
        LEFT = 8
    }
    val fooKey = object : AbstractEnumyKey("FOO") {}
    enumyLayers.push {
        // These two are equivalent:
        this[fooKey] = rule<Float>("foo") { _, _, _ -> 1.1f }
        this[fooKey] = constantRule(1.1f)
    }

    println("-- ENUM-Y ADD LEFTS")
    println("LAYERS -> ${enumyLayers.display()}")
    println("LEFT -> ${enumyLayers[Left]}")
    val asInt: Int? = enumyLayers.getAs(Left)
    println("LEFT -> $asInt")

    println("-- ENUM-Y FOONESS")
    enumyLayers.edit {
        this[fooKey] = rule<Float>("random") { _, _, _ -> Random.nextFloat() }
    }
    println("FOO -> ${enumyLayers[fooKey]}")
    enumyLayers.edit {
        // Hide "FOO" key
        this[fooKey] = rule<Float>("random") { _, _, _ -> null }
    }
    println("KEYS -> ${enumyLayers.keys}")

    println("-- ENUM-Y UNDO")
    enumyLayers.push {
        LEFT = null
    }
    println("TOP -> ${enumyLayers.top}")
    println("LEFT -> ${enumyLayers[Left]}")
    enumyLayers.edit {
        LEFT = 9
    }
    println("TOP -> ${enumyLayers.top}")
    println("LEFT -> ${enumyLayers[Left]}")

    println("-- ENUM-Y DISPLAY")
    println(enumyLayers.display())

    println("-- ENUM-Y CEILINGS AND FLOORS")
    enumyLayers.push {
        this[Small] = ceilRule(7)
        this[Large] = floorRule(7)
    }

    enumyLayers.push {
        SMALL = 3
        LARGE = 3
    }
    println("LAYERS -> ${enumyLayers.display()}")
    enumyLayers.push {
        SMALL = 11
        LARGE = SMALL // prop delegate reads as well
    }
    println("LAYERS -> ${enumyLayers.display()}")

    /*
    println("-- ENUM-Y CONTAINER")
    // TODO: DOES NOT COMPILE -- EnumyStuff is not an EnumyLayer
    val container: EnumyStuff = enumyLayers.push(EnumyStuff::new)
    */
}

private fun Layers<*, *, *>.display() =
    history.joinToString("\n", "\$NAME:\n") { layer ->
        "* ${layer.index + 1} (${layer::class.simpleName}): $layer"
    }
