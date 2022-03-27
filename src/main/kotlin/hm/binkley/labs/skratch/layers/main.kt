package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.enumy.EnumyKey.AbstractEnumyKey
import hm.binkley.labs.skratch.layers.enumy.EnumyMutableLayers
import hm.binkley.labs.skratch.layers.enumy.Left
import hm.binkley.labs.skratch.layers.rules.LastOrDefaultRule
import hm.binkley.labs.skratch.layers.rules.lastOrDefaultRule

fun main() {
    val layers = MyMutableLayers()

    println("--- ADD NOT EMPTY")
    val lastOrNegativeOne = LastOrDefaultRule<String, Number, Number>(-1)
    println(layers.push(MyMutableLayer(mutableMapOf("STRING" to lastOrNegativeOne))))
    println(layers.push(MyMutableLayer(mutableMapOf("STRING" to 3.toValue()))))
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
            this["BOB"] = 77.toValue()
        }
    )
    println("HISTORY -> ${layers.history}")
    println("LAYERS -> $layers")
    println("STRING -> ${layers["STRING"]}")

    val wordy = layers.push(MyWordMutableLayer()).apply {
        ohMy()
        myWord()
    }

    println("-- MORE EXTENDING")
    println("WORDY -> $wordy")
    println("HISTORY -> ${layers.history}")
    println("LAYERS -> $layers")
    println("STRING -> ${layers["STRING"]}")

    val enumyLayers = EnumyMutableLayers()
    println("-- ENUM-Y INIT")
    println("HISTORY -> ${enumyLayers.history}")

    enumyLayers.edit {
        this[Left] = 7.toValue()
    }
    enumyLayers.push {
        this[Left] = 8.toValue()
    }
    val fooKey = object : AbstractEnumyKey("FOO") {}
    enumyLayers.push {
        this[fooKey] = rule<Float>("foo") { _, _, _ -> 1.1f }
    }

    println("-- ENUM-Y ADD LEFTS")
    println("HISTORY -> ${enumyLayers.history}")
    println("LAYERS -> $enumyLayers")
    println("LEFT -> ${enumyLayers[Left]}")
    val asInt: Int? = enumyLayers.getAs(Left)
    println("LEFT -> $asInt")

    println("-- WHAT IF?")
    val whatIf = enumyLayers.whatIf {
        this[Left] = 17
    }
    println("INDEPENDENT? ${enumyLayers.history != whatIf.history}")
    println("ORIGINAL -> $enumyLayers")
    println("WHAT-IF? -> $whatIf")
    val diff = whatIf.history - enumyLayers.history.toSet()
    println("DIFF -> $diff")

    println("-- RULE WHAT HIDES A KEY")
    val withNullRule = enumyLayers.whatIf {
        this[Left] = rule<Int>("hidden") { _, _, _ -> null }
    }
    println("NULL RULE? -> $withNullRule")
    println("ORIGINAL -> $enumyLayers")

    println("-- POP LAST EDIT")
    println("LAYERS -> $enumyLayers")
    println("POPPED -> ${enumyLayers.pop()}")
    println("LAYERS -> $enumyLayers")

    println("-- BAD EDIT")
    val barKey = object : AbstractEnumyKey("BAR") {}
    try {
        enumyLayers.edit {
            this[barKey] = -10101 // No rule defined for "BAR"
        }
        throw IllegalStateException("SHOULD NOT REACH HERE")
    } catch (e: MissingRuleException) {
        println(e)
    }
    println("AFTER ROLLBACK -> $enumyLayers")

    println("-- DEFENSIVE COPY")
    val map: Map<String, ValueOrRule<Number>> = mapOf("a" to 3.toValue())
    val layer = MyMutableLayer(map)
    layer.edit { clear() }
    println("MAP -> $map")
    println("LAYER -> $layer")
}

class MyMutableLayers : MutableLayers<String, Number, MyMutableLayer>() {
    init {
        edit {
            this["HUM-HUM"] = lastOrDefaultRule(-2)
            this["message"] = lastOrDefaultRule(-3)
        }
        push { }
    }

    override fun new() = MyMutableLayer()

    fun doHickey(): MyMutableLayer =
        MyMutableLayer(mutableMapOf("HUM-HUM" to 2.toValue()))
}

open class MyMutableLayer(
    map: Map<String, ValueOrRule<Number>> = emptyMap(),
) : MutableLayer<String, Number, MyMutableLayer>(map) {
    override fun <N : MyMutableLayer> duplicate(): N =
        MyMutableLayer(toMutableMap()).self()
}

open class OhMyMutableLayer<M : OhMyMutableLayer<M>> :
    MyMutableLayer(mapOf("message" to 17.toValue())) {
    open fun ohMy() = println("OH, MY, ${this["message"]}!")
}

class MyWordMutableLayer : OhMyMutableLayer<MyWordMutableLayer>() {
    init {
        edit {
            this["message"] = 31.toValue()
        }
    }

    fun myWord() = println("MY, WORD!")
}
