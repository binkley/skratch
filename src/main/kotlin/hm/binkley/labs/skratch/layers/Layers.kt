package hm.binkley.labs.skratch.layers

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

private fun <K : Any, V : Any, B : Layer<K, V, B>>
List<B>.defaultView(): Map<K, V> = fold(mutableMapOf()) { merged, it ->
    merged.putAll(it); merged
}

abstract class AbstractLayers<K : Any, out V : Any, out B : Layer<K, V, B>>(
    protected open val layers: List<B>,
) : Layers<K, V, B>, Map<K, V> by layers.defaultView() {
    override val history: List<B> get() = layers

    override fun toString() = layers.defaultView().toString()
}

abstract class AbstractMutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>(
    override val layers: MutableList<M> = mutableListOf(),
) : MutableLayers<K, V, M>, AbstractLayers<K, V, M>(layers) {
    override fun <N : M> add(new: N): N = new.also { layers.add(new) }
}
