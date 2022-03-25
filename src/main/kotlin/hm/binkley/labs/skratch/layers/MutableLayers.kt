package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.enumy.Left
import hm.binkley.labs.skratch.layers.rules.LastRule

interface MutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    > : Layers<K, V, M> {
    // TODO: Raise exception if value added with no previous rule
    fun edit(block: EditMap<K, V>.() -> Unit): M = last().edit(block)

    fun new(): M

    fun <N : M> add(new: N): N
    fun add(block: EditMap<K, V>.() -> Unit): M = add(new()).edit(block)
}

@Suppress("LeakingThis")
abstract class AbstractMutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    firstLayer: M? = null,
    private val layers: MutableList<M> = mutableListOf(),
) : MutableLayers<K, V, M>, AbstractLayers<K, V, M>(layers) {
    init {
        if (null != firstLayer) add(firstLayer.validAsFirst())
        add { }
    }

    override fun <N : M> add(new: N): N = new.also { layers.add(new) }
}

private fun <
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    N : M,
    >
N.validAsFirst(): N {
    values.asSequence()
        .filterIsInstance<Value<*>>()
        .firstOrNull() ?: return this

    throw InvalidFirstLayerException(this)
}
