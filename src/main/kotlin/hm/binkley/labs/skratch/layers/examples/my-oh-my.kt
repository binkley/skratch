package hm.binkley.labs.skratch.layers.examples

import hm.binkley.labs.skratch.layers.MutableLayer
import hm.binkley.labs.skratch.layers.MutableLayers
import hm.binkley.labs.skratch.layers.ValueOrRule
import hm.binkley.labs.skratch.layers.rules.lastOrDefaultRule
import hm.binkley.labs.skratch.layers.self
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

    fun doHickey(): MyLayer =
        MyLayer(mapOf("HUM-HUM" to 2.toValue()))
}

open class MyLayer(
    map: Map<String, ValueOrRule<Number>> = emptyMap(),
) : MutableLayer<String, Number, MyLayer>(map) {
    override fun <N : MyLayer> copy(): N = MyLayer(toMap()).self()
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
