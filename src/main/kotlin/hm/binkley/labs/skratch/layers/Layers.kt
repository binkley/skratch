package hm.binkley.labs.skratch.layers

import hm.binkley.util.Stack

interface Layers<
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    > : Map<K, V> {
    /** A read-only view of editing history. */
    val history: Stack<Layer<K, V, L>>

    fun <T : V> getAs(key: K): T?
    fun peek(): L

    /** Duplicates these layers replacing the top layer. */
    fun whatIf(layer: L): Layers<K, V, L>

    /** Duplicates these layers editing the top layer. */
    fun whatIf(block: EditMap<K, V>.() -> Unit): Layers<K, V, L>
}
