package hm.binkley.labs.skratch.layers.enumy

import hm.binkley.labs.skratch.layers.MutableLayer
import hm.binkley.labs.skratch.layers.MutableLayers
import hm.binkley.labs.skratch.layers.ValueOrRule
import hm.binkley.labs.skratch.layers.enumy.EnumyKey.AbstractEnumyKey
import hm.binkley.labs.skratch.layers.rules.lastOrNullRule
import hm.binkley.labs.skratch.layers.rules.lastRule

interface EnumyKey {
    val name: String

    abstract class AbstractEnumyKey(override val name: String) : EnumyKey {
        override fun toString() = name
    }
}

sealed class Handedness(name: String) : AbstractEnumyKey(name)
object Left : Handedness("Left")
object Right : Handedness("Right")

sealed class TeeShirtSize : EnumyKey {
    override val name get() = this::class.simpleName!!
    override fun toString() = "T-shirt:$name"
}

object Small : TeeShirtSize()
object Medium : TeeShirtSize()
object Large : TeeShirtSize()

open class EnumyMutableLayer(
    map: MutableMap<EnumyKey, ValueOrRule<Number>> = mutableMapOf(),
) : MutableLayer<EnumyKey, Number, EnumyMutableLayer>(map)

val firstLayer = object : EnumyMutableLayer() {
    init {
        edit {
            this[Left] = lastRule()
            this[Right] = lastOrNullRule()
            this[Small] = lastOrNullRule()
            this[Medium] = lastOrNullRule()
            this[Large] = lastOrNullRule()
        }
    }
}

class EnumyMutableLayers :
    MutableLayers<EnumyKey, Number, EnumyMutableLayer>(firstLayer) {
    init {
        push { }
    }

    override fun new() = EnumyMutableLayer()
}
