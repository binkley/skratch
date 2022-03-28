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
        layers.forEach { it.valid() }
    }

    abstract fun new(): M

    fun <N : M> push(layer: N): N = layer.also {
        layers.push(layer).valid()
    }

    fun push(block: EditMap<K, V>.() -> Unit): M =
        push(new()).edit(block)

    fun pop(): M = layers.pop()

    /** Edits the top layer raising error if the result is invalid. */
    fun edit(block: EditMap<K, V>.() -> Unit): M {
        val whatIf = whatIf(peek().duplicate().edit(block))
        layers.clear()
        layers.addAll(whatIf.layers)
        return peek()
    }

    /** Duplicates this [MutableLayers], and replaces the top layer in it.  */
    fun whatIf(layer: M): MutableLayers<K, V, M> {
        val outer = this
        val whatIf = object : MutableLayers<K, V, M>(history) {
            override fun new(): M = outer.new()
        }
        whatIf.pop()
        whatIf.push(layer)
        return whatIf
    }

    /** Duplicates this [MutableLayers], and edits the top layer in it. */
    fun whatIf(block: EditMap<K, V>.() -> Unit) = whatIf(new().edit(block))

    private fun <N : M> N.valid(): N {
        entries.asSequence()
            .filter { (_, value) -> value is Value<V> }
            .forEach { (key, _) -> ruleForOrThrow<V>(key) }
        return self()
    }
}
