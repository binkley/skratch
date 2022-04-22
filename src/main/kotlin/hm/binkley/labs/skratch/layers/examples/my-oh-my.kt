package hm.binkley.labs.skratch.layers.examples

import hm.binkley.labs.skratch.layers.MutableLayer
import hm.binkley.labs.skratch.layers.MutableLayers
import hm.binkley.labs.skratch.layers.ValueOrRule
import hm.binkley.labs.skratch.layers.rules.lastOrDefaultRule
import hm.binkley.labs.skratch.layers.toValue

class MyLayers :
    MutableLayers<String, Number, MyLayer>(::MyLayer, ::MyLayer) {
    init {
        // First layer via editing initial empty layer
        edit {
            this["HUM-HUM"] = lastOrDefaultRule(-2)
            this["message"] = lastOrDefaultRule(-3)
        }
        // Ensure edits afterwards do not overwrite pre-defined rules
        push { }
    }

    fun doHickey(index: Int): MyLayer =
        MyLayer(index, mapOf("HUM-HUM" to 2.toValue()))
}

open class MyLayer(
    index: Int,
    map: Map<String, ValueOrRule<Number>> = emptyMap(),
) : MutableLayer<String, Number, MyLayer>(index, map) {
    override fun copy() = MyLayer(index, this)
}

open class OhMyLayer<M : OhMyLayer<M>>(
    index: Int,
) :
    MyLayer(index, mapOf("message" to 17.toValue())) {
    open fun ohMy() = println("OH, MY, ${this["message"]}!")
}

class MyWordLayer(
    index: Int,
) : OhMyLayer<MyWordLayer>(index) {
    init {
        edit {
            this["message"] = 31.toValue()
        }
    }

    fun myWord() = println("MY, WORD!")
}
