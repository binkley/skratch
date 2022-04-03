package hm.binkley.labs.skratch.layers.enumy

import hm.binkley.labs.skratch.layers.EditMap
import hm.binkley.labs.skratch.layers.EditMapDelegate
import hm.binkley.labs.skratch.layers.MutableLayer
import hm.binkley.labs.skratch.layers.MutableLayers
import hm.binkley.labs.skratch.layers.ValueOrRule
import hm.binkley.labs.skratch.layers.enumy.EnumyKey.AbstractEnumyKey
import hm.binkley.labs.skratch.layers.rules.lastOrNullRule
import hm.binkley.labs.skratch.layers.rules.lastRule
import hm.binkley.labs.skratch.layers.self

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

open class EnumyLayer(
    map: Map<EnumyKey, ValueOrRule<Number>> = emptyMap(),
) : MutableLayer<EnumyKey, Number, EnumyLayer>(map) {
    override fun <N : EnumyLayer> copy(): N = EnumyLayer(toMap()).self()
}

var EditMap<EnumyKey, Number>.LEFT: Number by EditMapDelegate { Left }
var EditMap<EnumyKey, Number>.SMALL: Number by EditMapDelegate { Small }
var EditMap<EnumyKey, Number>.LARGE: Number by EditMapDelegate { Large }

val initialRules = object : EnumyLayer() {
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

class EnumyLayers :
    MutableLayers<EnumyKey, Number, EnumyLayer>(::EnumyLayer, initialRules) {
    init { push { } } // Start with blank layer for edits
}
