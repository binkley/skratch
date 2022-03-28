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
        validOrThrow()
    }

    abstract fun new(): M

    fun pop(): M {
        val whatIf = duplicate()
        whatIf.layers.pop()
        whatIf.validOrThrow()
        return layers.pop()
    }

    fun <N : M> push(layer: N): N {
        val whatIf = whatIf { }
        whatIf.layers.push(layer)
        whatIf.validOrThrow()
        return layers.push(layer).self()
    }

    fun push(block: EditMap<K, V>.() -> Unit): M = push(new().edit(block))

    fun edit(block: EditMap<K, V>.() -> Unit): M {
        val whatIf = whatIf(block)
        layers.clear()
        layers.addAll(whatIf.layers)
        return peek()
    }

    /** Duplicates this [MutableLayers], and replaces the top layer in it.  */
    fun whatIf(layer: M): MutableLayers<K, V, M> {
        val whatIf = duplicate()
        whatIf.layers.replaceLast(layer)
        whatIf.validOrThrow()
        return whatIf
    }

    /** Duplicates this [MutableLayers], and edits the top layer in it. */
    fun whatIf(block: EditMap<K, V>.() -> Unit) =
        whatIf(peek().duplicate().edit(block))

    private fun validOrThrow() {
        if (layers.isEmpty()) throw MissingFirstLayerException
        keys.forEach { ruleForOrThrow<V>(it) }
    }

    // NOT a clone
    private fun duplicate(): MutableLayers<K, V, M> =
        object : MutableLayers<K, V, M>(history) {
            override fun new(): M = this@MutableLayers.new()
        }
}

private fun <T> MutableList<T>.replaceLast(element: T): T {
    val last = last()
    this[lastIndex] = element
    return last
}
