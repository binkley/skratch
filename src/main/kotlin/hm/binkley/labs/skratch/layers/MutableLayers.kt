package hm.binkley.labs.skratch.layers

interface MutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    > : Layers<K, V, M> {
    fun edit(block: EditMap<K, V>.() -> Unit) = last().edit(block)

    fun <N : M> add(new: N): N
    fun new(): M
    fun add(block: EditMap<K, V>.() -> Unit): M {
        val new = add(new())
        edit(block)
        return new
    }
}

abstract class AbstractMutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    private val layers: MutableList<M> = mutableListOf(),
) : MutableLayers<K, V, M>, AbstractLayers<K, V, M>(layers) {
    override fun <N : M> add(new: N): N = new.also { layers.add(new) }
}
