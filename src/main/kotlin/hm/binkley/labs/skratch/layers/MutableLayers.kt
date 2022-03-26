package hm.binkley.labs.skratch.layers

// TODO: Pull up [Layers] implementation?
@Suppress("LeakingThis")
abstract class MutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    firstLayer: M? = null,
    // TODO: Defensive copy of [layers]
    private val layers: MutableList<M> = mutableListOf(),
) : Layers<K, V, M>(layers) {
    init {
        if (null == firstLayer) {
            if (layers.isEmpty()) add { }
        } else {
            layers.add(firstLayer)
        }
    }

    abstract fun new(): M

    // TODO: Raise exception if value added with no previous rule
    fun edit(block: EditMap<K, V>.() -> Unit): M = last().edit(block)

    fun <N : M> add(new: N): N = new.also { layers.add(new) }

    fun add(block: EditMap<K, V>.() -> Unit): M = add(new()).edit(block)

    fun whatIf(block: EditMap<K, V>.() -> Unit): MutableLayers<K, V, M> {
        val outer = this
        val whatIf = object : MutableLayers<K, V, M>() {
            override fun new(): M = outer.new()
        }
        history.forEach {
            whatIf.edit { putAll(it) }
            whatIf.add { }
        }
        whatIf.edit(block)
        return whatIf
    }
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
