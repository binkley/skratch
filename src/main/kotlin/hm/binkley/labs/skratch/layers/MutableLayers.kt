package hm.binkley.labs.skratch.layers

import hm.binkley.util.MutableStack
import hm.binkley.util.emptyMutableStack
import hm.binkley.util.mutableStackOf
import hm.binkley.util.toMutableStack

// TODO: Pull up [Layers] implementation?
@Suppress("LeakingThis")
abstract class MutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    @Suppress("UNUSED_PARAMETER") token: Nothing?,
    // TODO: Defensive copy of [layers]
    // TODO: Avoid pointer sharing with [Layers]
    private val layers: MutableStack<M>,
) : Layers<K, V, M>(layers) {
    constructor() : this(null, emptyMutableStack()) {
        push { }
    }

    constructor(firstLayer: M) : this(null, mutableStackOf(firstLayer))

    // Defensive copy
    constructor(layers: List<M>) : this(null, layers.toMutableStack())

    init {
        // TODO: Require a first layer
        layers.firstOrNull()?.validAsFirst()
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

    fun <N : M> push(new: N): N = new.also { layers.push(new) }
    fun push(block: EditMap<K, V>.() -> Unit): M = push(new()).edit(block)
    fun pop(): M = layers.pop()

    fun whatIf(block: EditMap<K, V>.() -> Unit): MutableLayers<K, V, M> {
        val outer = this
        val whatIf = object : MutableLayers<K, V, M>(history) {
            override fun new(): M = outer.new()
        }
        whatIf.push(block)
        return whatIf
    }

    private fun <N : M> N.validAsLayer(): N {
        entries.asSequence()
            .filter { (_, value) -> value is Value<V> }
            .forEach { (key, _) -> ruleForOrThrow<V>(key) }
        return self()
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
