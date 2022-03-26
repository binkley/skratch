package hm.binkley.labs.skratch.layers

// TODO: Pull up [Layers] implementation?
@Suppress("LeakingThis")
abstract class MutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    @Suppress("UNUSED_PARAMETER") token: Nothing?,
    private val layers: MutableList<M>,
) : Layers<K, V, M>(layers) {
    constructor() : this(null, mutableListOf()) {
        add { }
    }

    constructor(firstLayer: M) : this(null, mutableListOf()) {
        layers.add(firstLayer.validAsFirst())
    }

    // Defensive copy
    constructor(layers: List<M>) : this(null, layers.toMutableList())

    abstract fun new(): M

    // TODO: Raise exception if value added with no previous rule
    fun edit(block: EditMap<K, V>.() -> Unit): M = last().edit(block)

    fun <N : M> add(new: N): N = new.also { layers.add(new) }

    fun add(block: EditMap<K, V>.() -> Unit): M = add(new()).edit(block)

    fun whatIf(block: EditMap<K, V>.() -> Unit): MutableLayers<K, V, M> {
        val outer = this
        val whatIf = object : MutableLayers<K, V, M>(history) {
            override fun new(): M = outer.new()
        }
        whatIf.add(block)
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
