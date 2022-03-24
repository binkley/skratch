package hm.binkley.labs.skratch.layers

import kotlin.collections.Map.Entry

interface Layers<K : Any, out V : Any, out B : Layer<K, V, B>> :
    Map<K, V> {
    val history: List<B>

    fun last(): B = history.last()
}

interface MutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>> :
    Layers<K, V, M> {
    fun edit(block: EditMap<K, V>.() -> Unit) = last().edit(block)

    fun <N : M> add(new: N): N
    fun new(): M
    fun add(block: EditMap<K, V>.() -> Unit): M {
        val new = add(new())
        edit(block)
        return new
    }
}

private class DefaultView<K : Any, V : Any, B : Layer<K, V, B>>(
    private val layers: List<B>,
) : AbstractMap<K, V>() {
    override val entries: Set<Entry<K, V>>
        get() = layers.fold<B, MutableMap<K, V>>(mutableMapOf()) { merged, it ->
            merged.putAll(it); merged
        }.entries
}

abstract class AbstractLayers<K : Any, out V : Any, out B : Layer<K, V, B>>(
    protected open val layers: List<B>,
) : Layers<K, V, B>, Map<K, V> by DefaultView(layers) {
    override val history: List<B> get() = layers

    override fun toString() = DefaultView(layers).toString()
}

abstract class AbstractMutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>(
    override val layers: MutableList<M> = mutableListOf(),
) : MutableLayers<K, V, M>, AbstractLayers<K, V, M>(layers) {
    override fun <N : M> add(new: N): N = new.also { layers.add(new) }
}
