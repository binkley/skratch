package hm.binkley.labs.skratch.layers

import kotlin.collections.Map.Entry

interface Layers<
    K : Any,
    out V : Any,
    out L : Layer<K, V, L>,
    > :
    Map<K, V> {
    val history: List<L>

    fun last(): L = history.last()
}

interface MutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    > :
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

private class DefaultView<
    K : Any,
    out V : Any,
    out L : Layer<K, V, L>,
    >(
    private val layers: List<L>,
) : AbstractMap<K, V>() {
    override val entries: Set<Entry<K, V>>
        get() = layers.fold<L, MutableMap<K, V>>(mutableMapOf()) { merged, it ->
            merged.putAll(it); merged
        }.entries
}

abstract class AbstractLayers<
    K : Any,
    out V : Any,
    out L : Layer<K, V, L>,
    >(
    protected open val layers: List<L>,
) : Layers<K, V, L>, Map<K, V> by DefaultView(layers) {
    override val history: List<L> get() = layers

    override fun toString() = DefaultView(layers).toString()
}

abstract class AbstractMutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    override val layers: MutableList<M> = mutableListOf(),
) : MutableLayers<K, V, M>, AbstractLayers<K, V, M>(layers) {
    override fun <N : M> add(new: N): N = new.also { layers.add(new) }
}
