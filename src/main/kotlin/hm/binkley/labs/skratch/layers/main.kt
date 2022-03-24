package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.enumy.EnumyMutableLayer
import hm.binkley.labs.skratch.layers.enumy.EnumyMutableLayers
import hm.binkley.labs.skratch.layers.enumy.Left

fun main() {
    open class MyMutableLayer(
        map: MutableMap<String, Number> = mutableMapOf(),
    ) : AbstractMutableLayer<String, Number, MyMutableLayer>(map)

    val layers =
        object : AbstractMutableLayers<String, Number, MyMutableLayer>() {
            override fun new() = MyMutableLayer()

            fun doHickey(): MyMutableLayer =
                MyMutableLayer(mutableMapOf("HUM-HUM" to 2))
        }

    println("--- EMPTY")
    println("LAYERS -> $layers")
    println("HISTORY -> ${layers.history}")
    println("STRING -> ${layers["STRING"]}")
    println("--- ADD EMPTY")
    println(layers.add(MyMutableLayer()).self)
    println("LAYERS -> $layers")
    println("HISTORY -> ${layers.history}")
    println("STRING -> ${layers["STRING"]}")
    println("--- ADD NOT EMPTY")
    println(layers.add(MyMutableLayer(mutableMapOf("STRING" to 3))).self)
    println("LAYERS -> $layers")
    println("HISTORY -> ${layers.history}")
    println("STRING -> ${layers["STRING"]}")
    println("--- ADD CUSTOM")
    println(layers.add(layers.doHickey()).self)
    println("--- ADD VIA BLOCK")
    println(
        layers.add {
            this["BOB"] = 77
        }
    )
    println("LAYERS -> $layers")
    println("HISTORY -> ${layers.history}")
    println("STRING -> ${layers["STRING"]}")

    open class OhMyMutableLayer<M : OhMyMutableLayer<M>> :
        MyMutableLayer(mutableMapOf("message" to 17)) {
        open fun ohMy() = println("OH, MY, ${this["message"]}!")
    }

    class MyWordMutableLayer : OhMyMutableLayer<MyWordMutableLayer>() {
        init {
            edit {
                this["message"] = 31
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
    println("LAYERS -> $layers")
    println("HISTORY -> ${layers.history}")
    println("STRING -> ${layers["STRING"]}")

    val firstEnumyLayer = EnumyMutableLayer()
    val enumyLayers = EnumyMutableLayers(firstEnumyLayer)
    enumyLayers.edit {
        this[Left] = 7
    }
    enumyLayers.add(EnumyMutableLayer())
    enumyLayers.edit {
        this[Left] = 8
    }

    println("-- ENUM-Y KEYS")
    println("LAYERS -> $enumyLayers")
    println("HISTORY -> ${enumyLayers.history}")
    println("LEFT -> ${enumyLayers[Left]}")
}
