package hm.binkley.labs.skratch.layers.enumy

import hm.binkley.labs.skratch.layers.AbstractMutableLayer
import hm.binkley.labs.skratch.layers.AbstractMutableLayers
import hm.binkley.labs.skratch.layers.ValueOrRule
import hm.binkley.labs.skratch.layers.enumy.Key.AbstractKey

interface Key {
    val name: String

    abstract class AbstractKey(override val name: String) : Key {
        override fun toString() = name
    }
}

sealed class Handedness(name: String) : AbstractKey(name)
object Left : Handedness("Left")
object Right : Handedness("Right")

sealed class TeeShirtSize : Key {
    override val name get() = this::class.simpleName!!
    override fun toString() = "T-shirt:$name"
}

object Small : TeeShirtSize()
object Medium : TeeShirtSize()
object Large : TeeShirtSize()

open class EnumyMutableLayer(
    map: MutableMap<Key, ValueOrRule<Number>> = mutableMapOf(),
) : AbstractMutableLayer<Key, Number, EnumyMutableLayer>(map)

class EnumyMutableLayers(firstLayer: EnumyMutableLayer) :
    AbstractMutableLayers<Key, Number, EnumyMutableLayer>(
        mutableListOf(firstLayer)
    ) {
    override fun new() = EnumyMutableLayer()
}
