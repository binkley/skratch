package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.enumy.EnumyKey.AbstractEnumyKey
import hm.binkley.labs.skratch.layers.enumy.EnumyLayers
import hm.binkley.labs.skratch.layers.enumy.LARGE
import hm.binkley.labs.skratch.layers.enumy.LEFT
import hm.binkley.labs.skratch.layers.enumy.Large
import hm.binkley.labs.skratch.layers.enumy.Left
import hm.binkley.labs.skratch.layers.enumy.SMALL
import hm.binkley.labs.skratch.layers.enumy.Small
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
    println(layers.push(MyLayer(mapOf("STRING" to lastOrNegativeOne))))
    println(layers.push(MyLayer(mapOf("STRING" to 3.toValue()))))
    println("HISTORY -> ${layers.history}")
    println("LAYERS -> $layers")
    println("STRING -> ${layers["STRING"]}")
    println("--- ADD CUSTOM")
    println(layers.push(layers.doHickey()))
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
    println("HISTORY -> ${layers.history}")
    println("LAYERS -> $layers")
    println("STRING -> ${layers["STRING"]}")

    val wordy = layers.push(MyWordLayer()).apply {
        ohMy()
        myWord()
    }

    println("-- MORE EXTENDING")
    println("WORDY -> $wordy")
    println("HISTORY -> ${layers.history}")
    println("LAYERS -> $layers")
    println("STRING -> ${layers["STRING"]}")

    val enumyLayers = EnumyLayers()
    println("-- ENUM-Y INIT")
    println("HISTORY -> ${enumyLayers.history}")

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
    println("HISTORY -> ${enumyLayers.history}")
    println("LAYERS -> $enumyLayers")
    println("LEFT -> ${enumyLayers[Left]}")
    val asInt: Int? = enumyLayers.getAs(Left)
    println("LEFT -> $asInt")

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
    println(display(enumyLayers))

    println("-- ENUM-Y CEILINGS AND FLOORS")
    enumyLayers.push {
        this[Small] = ceilRule(7)
        this[Large] = floorRule(7)
    }

    enumyLayers.push {
        SMALL = 3
        LARGE = 3
    }
    println("HISTORY -> ${enumyLayers.history}")
    println("LAYERS -> $enumyLayers")
    enumyLayers.push {
        SMALL = 11
        LARGE = SMALL // prop delegate reads as well
    }
    println("HISTORY -> ${enumyLayers.history}")
    println("LAYERS -> $enumyLayers")
}

class MyLayers :
    MutableLayers<String, Number, MyLayer>(::MyLayer, MyLayer()) {
    init {
        // First layer via editing initial empty layer
        edit {
            this["HUM-HUM"] = lastOrDefaultRule(-2)
            this["message"] = lastOrDefaultRule(-3)
        }
        // Ensure edits afterwards do not overwrite pre-defined rules
        push { }
    }

    fun doHickey(): MyLayer =
        MyLayer(mapOf("HUM-HUM" to 2.toValue()))
}

open class MyLayer(
    map: Map<String, ValueOrRule<Number>> = emptyMap(),
) : MutableLayer<String, Number, MyLayer>(map) {
    override fun <N : MyLayer> copy(): N =
        MyLayer(toMap()).self()
}

open class OhMyLayer<M : OhMyLayer<M>> :
    MyLayer(mapOf("message" to 17.toValue())) {
    open fun ohMy() = println("OH, MY, ${this["message"]}!")
}

class MyWordLayer : OhMyLayer<MyWordLayer>() {
    init {
        edit {
            this["message"] = 31.toValue()
        }
    }

    fun myWord() = println("MY, WORD!")
}

private fun <K : Any, V : Any> display(layers: Layers<K, V, *>): String {
    return layers.history.mapIndexed { index, layer ->
        "${index + 1} (${layer::class.simpleName}): $layer"
    }.joinToString("\n", "\$NAME:\n")
}
