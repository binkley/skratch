package hm.binkley.labs.skratch.layers.examples

import hm.binkley.labs.skratch.layers.EditMap
import hm.binkley.labs.skratch.layers.EditMapDelegate
import hm.binkley.labs.skratch.layers.MutableLayer
import hm.binkley.labs.skratch.layers.MutableLayers
import hm.binkley.labs.skratch.layers.NewLayer
import hm.binkley.labs.skratch.layers.ValueOrRule
import hm.binkley.labs.skratch.layers.container.AbstractContainer
import hm.binkley.labs.skratch.layers.examples.EnumyKey.AbstractEnumyKey
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

open class EnumyLayer(
    index: Int,
    map: Map<EnumyKey, ValueOrRule<Number>> = emptyMap(),
) : MutableLayer<EnumyKey, Number, EnumyLayer>(index, map) {
    override fun copy(): EnumyLayer = EnumyLayer(index, this)
}

/**
 * Shorthand for `this[Left]` in an edit block.  Assigning `null` deletes
 * the key.
 */
var EditMap<EnumyKey, Number>.LEFT: Number? by EditMapDelegate { Left }

/** Shorthand for `this[Small]` in an edit block. */
var EditMap<EnumyKey, Number>.SMALL: Number by EditMapDelegate { Small }

/** Shorthand for `this[Large]` in an edit block. */
var EditMap<EnumyKey, Number>.LARGE: Number by EditMapDelegate { Large }

fun initialRules(index: Int): EnumyLayer {
    val x = EnumyLayer(index)
    x.edit {
        this[Left] = lastRule()
        this[Right] = lastOrNullRule()
        this[Small] = lastOrNullRule()
        this[Medium] = lastOrNullRule()
        this[Large] = lastOrNullRule()
    }
    return x
}

class EnumyLayers :
    MutableLayers<EnumyKey, Number, EnumyLayer>(
        ::initialRules,
        ::EnumyLayer
    ) {
    init {
        push { }
    } // Start with blank layer for edits
}

/** @todo Implement [AbstractContainer] */
open class EnumyContainer(
    index: Int,
    map: Map<EnumyKey, ValueOrRule<Number>> = emptyMap(),
    private val contents: List<EnumyLayer> = emptyList(),
) : EnumyLayer(index, map) {
    override fun copy() = EnumyContainer(index, this, contents)

    operator fun plus(layer: EnumyLayer):
            NewLayer<EnumyKey, Number, EnumyLayer> {
        require(layer !in contents) { "Already in contents: $layer" }
        return NewLayer { index ->
            EnumyContainer(
                index,
                this,
                contents + layer
            )
        }
    }

    operator fun minus(layer: EnumyLayer):
            NewLayer<EnumyKey, Number, EnumyLayer> {
        require(layer in contents) { "Not in contents: $layer" }
        return NewLayer { index ->
            EnumyContainer(
                index,
                this,
                contents - layer
            )
        }
    }

    override fun toString() = "${super.toString()}: $contents"
}
