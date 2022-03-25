package hm.binkley.labs.skratch.layers

@Suppress("LeakingThis")
abstract class MutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    firstLayer: M? = null,
    private val layers: MutableList<M> = mutableListOf(),
) : Layers<K, V, M>(layers) {
    init {
        if (null != firstLayer) add(firstLayer.validAsFirst())
        add { }
    }

    abstract fun new(): M

    // TODO: Raise exception if value added with no previous rule
    fun edit(block: EditMap<K, V>.() -> Unit): M = last().edit(block)

    fun <N : M> add(new: N): N = new.also { layers.add(new) }

    fun add(block: EditMap<K, V>.() -> Unit): M = add(new()).edit(block)
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
