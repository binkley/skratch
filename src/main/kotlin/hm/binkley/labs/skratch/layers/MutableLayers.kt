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
        if (layers.isEmpty()) throw MissingFirstLayerException
        keys.forEach { ruleForOrThrow<V>(it) }
    }

    abstract fun new(): M

    fun pop(): M =
        if (1 < layers.size) layers.pop()
        else throw MissingFirstLayerException

    fun <N : M> push(layer: N): N {
        val valid = validate { it.push(layer) }.peek()
        return layers.push(valid).self()
    }

    fun push(block: EditMap<K, V>.() -> Unit): M = push(new().edit(block))

    fun edit(block: EditMap<K, V>.() -> Unit): M {
        val valid = whatIf(block).peek()
        layers.replaceLast(valid)
        return valid
    }

    /** Duplicates this [MutableLayers], and replaces the top layer in it.  */
    fun whatIf(layer: M): MutableLayers<K, V, M> =
        validate { it.replaceLast(layer) }

    /** Duplicates this [MutableLayers], and edits the top layer in it. */
    fun whatIf(block: EditMap<K, V>.() -> Unit) =
        whatIf(peek().copy().edit(block))

    /**
     * Applies [block] to a shallow defensive copy returning an anonymous
     * [MutableLayers] of that copy.
     */
    private fun validate(
        block: (MutableStack<M>) -> Unit,
    ): MutableLayers<K, V, M> {
        val copy = layers.toMutableStack()
        block(copy)
        return object : MutableLayers<K, V, M>(copy) {
            override fun new(): M = this@MutableLayers.new()
        }
    }
}

private fun <T> MutableList<T>.replaceLast(element: T): T {
    val last = last()
    this[lastIndex] = element
    return last
}
