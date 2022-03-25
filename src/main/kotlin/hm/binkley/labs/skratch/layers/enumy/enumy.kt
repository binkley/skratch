package hm.binkley.labs.skratch.layers.enumy

import hm.binkley.labs.skratch.layers.AbstractMutableLayer
import hm.binkley.labs.skratch.layers.AbstractMutableLayers
import hm.binkley.labs.skratch.layers.ValueOrRule
import hm.binkley.labs.skratch.layers.enumy.EnumyKey.AbstractEnumyKey
import hm.binkley.labs.skratch.layers.rules.LastOrNullRule
import hm.binkley.labs.skratch.layers.rules.LastRule

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
) : AbstractMutableLayer<EnumyKey, Number, EnumyMutableLayer>(map)

val firstLayer = object : EnumyMutableLayer() {
    init {
        edit {
            this[Left] = LastRule<EnumyKey, Number, Number>()
            this[Right] = LastOrNullRule<EnumyKey, Number, Number>()
        }
    }
}

class EnumyMutableLayers :
    AbstractMutableLayers<EnumyKey, Number, EnumyMutableLayer>(firstLayer) {
    override fun new() = EnumyMutableLayer()
}
