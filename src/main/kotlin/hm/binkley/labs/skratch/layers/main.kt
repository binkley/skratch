package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.enumy.EnumyKey.AbstractEnumyKey
import hm.binkley.labs.skratch.layers.enumy.EnumyMutableLayers
import hm.binkley.labs.skratch.layers.enumy.Left
import hm.binkley.labs.skratch.layers.rules.LastOrDefaultRule

fun main() {
    open class MyMutableLayer(
        map: MutableMap<String, ValueOrRule<Number>> = mutableMapOf(),
    ) : MutableLayer<String, Number, MyMutableLayer>(map)

    val layers =
        object : MutableLayers<String, Number, MyMutableLayer>() {
            init {
                edit {
                    this["HUM-HUM"] = lastOrDefaultRule(-2)
                    this["message"] = lastOrDefaultRule(-3)
                }
                add { }
            }

            override fun new() = MyMutableLayer()

            fun doHickey(): MyMutableLayer =
                MyMutableLayer(mutableMapOf("HUM-HUM" to 2.toValue()))
        }

    println("--- ADD NOT EMPTY")
    val lastOrNegativeOne = LastOrDefaultRule<String, Number, Number>(-1)
    println(layers.add(MyMutableLayer(mutableMapOf("STRING" to lastOrNegativeOne))))
    println(layers.add(MyMutableLayer(mutableMapOf("STRING" to 3.toValue()))))
    println("HISTORY -> ${layers.history}")
    println("LAYERS -> $layers")
    println("STRING -> ${layers["STRING"]}")
    println("--- ADD CUSTOM")
    println(layers.add(layers.doHickey()))
    println("--- ADD VIA BLOCK")
    println(
        layers.add {
            this["BOB"] = lastOrDefaultRule(-1)
        }
    )
    println(
        layers.add {
            this["BOB"] = 77.toValue()
        }
    )
    println("HISTORY -> ${layers.history}")
    println("LAYERS -> $layers")
    println("STRING -> ${layers["STRING"]}")

    open class OhMyMutableLayer<M : OhMyMutableLayer<M>> :
        MyMutableLayer(mutableMapOf("message" to 17.toValue())) {
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

    val wordy = layers.add(MyWordMutableLayer()).apply {
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
    enumyLayers.add {
        this[Left] = 8.toValue()
    }
    val fooKey = object : AbstractEnumyKey("FOO") {}
    enumyLayers.add {
        this[fooKey] = rule<Float>("foo") { _, _, _ -> 1.1f }
    }

    println("-- ENUM-Y ADD LEFTS")
    println("HISTORY -> ${enumyLayers.history}")
    println("LAYERS -> $enumyLayers")
    println("LEFT -> ${enumyLayers[Left]}")
    val asInt: Int? = enumyLayers.getAs(Left)
    println("LEFT -> $asInt")

    println ("-- WHAT IF?")
    val whatIf = enumyLayers.whatIf {
        this[Left] = 17
    }
    println("INDEPENDENT? ${enumyLayers.history != whatIf.history}")
    println("ORIGINAL -> $enumyLayers")
    println("WHAT-IF? -> $whatIf")
    val diff = whatIf.history - enumyLayers.history.toSet()
    println("DIFF -> $diff")
}
