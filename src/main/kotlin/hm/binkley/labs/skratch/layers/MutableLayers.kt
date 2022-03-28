package hm.binkley.labs.skratch.layers

import hm.binkley.util.MutableStack
import hm.binkley.util.mutableStackOf
import hm.binkley.util.toMutableStack

// TODO: Pull up [Layers] implementation?
@Suppress("LeakingThis")
abstract class MutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    // TODO: Avoid pointer sharing with [Layers]
    private val layers: MutableStack<M>,
) : Layers<K, V, M>(layers) {
    constructor(initialRules: M) : this(mutableStackOf(initialRules))
    constructor(layers: List<M>) : this(layers.toMutableStack())

    init {
        valid()
    }

    abstract fun new(): M

    fun <N : M> push(layer: N): N {
        try {
            layers.push(layer)
            valid()
        } catch (e: LayersException) {
            pop()
            throw e
        }
        return layer
    }

    fun push(block: EditMap<K, V>.() -> Unit): M = push(new().edit(block))
    fun pop(): M = layers.pop()

    fun edit(block: EditMap<K, V>.() -> Unit): M {
        val whatIf = whatIf(block)
        layers.clear()
        layers.addAll(whatIf.layers)
        return peek()
    }

    /** Duplicates this [MutableLayers], and replaces the top layer in it.  */
    fun whatIf(layer: M): MutableLayers<K, V, M> {
        val whatIf = duplicate()
        whatIf.pop()
        whatIf.push(layer)
        return whatIf
    }

    /** Duplicates this [MutableLayers], and edits the top layer in it. */
    fun whatIf(block: EditMap<K, V>.() -> Unit) =
        whatIf(peek().duplicate().edit(block))

    protected open fun valid() {
        if (layers.isEmpty()) throw MissingFirstLayerException
        keys.forEach { ruleForOrThrow<V>(it) }
    }

    // NOT a clone
    private fun duplicate(): MutableLayers<K, V, M> {
        return object : MutableLayers<K, V, M>(history) {
            override fun new(): M = this@MutableLayers.new()
        }
    }
}
