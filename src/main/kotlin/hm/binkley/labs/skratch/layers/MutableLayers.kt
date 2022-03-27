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
        layers.forEach { it.validAsLayer() }
    }

    abstract fun new(): M

    fun edit(block: EditMap<K, V>.() -> Unit): M {
        val rollback = peek().duplicate()
        try {
            return peek().edit(block).validAsLayer()
        } catch (e: MissingRuleException) {
            pop() // Drop the invalid edits
            push(rollback)
            throw e
        }
    }

    fun <N : M> push(layer: N): N = layer.also { layers.push(layer) }

    fun push(block: EditMap<K, V>.() -> Unit): M =
        push(new()).edit(block).validAsLayer()

    fun pop(): M = layers.pop()

    fun whatIf(layer: M): MutableLayers<K, V, M> {
        val outer = this
        val whatIf = object : MutableLayers<K, V, M>(history) {
            override fun new(): M = outer.new()
        }
        whatIf.push(layer).validAsLayer()
        return whatIf
    }

    fun whatIf(block: EditMap<K, V>.() -> Unit) = whatIf(new().edit(block))

    private fun <N : M> N.validAsLayer(): N {
        entries.asSequence()
            .filter { (_, value) -> value is Value<V> }
            .forEach { (key, _) -> ruleForOrThrow<V>(key) }
        return self()
    }
}
