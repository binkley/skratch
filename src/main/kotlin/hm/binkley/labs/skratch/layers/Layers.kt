package hm.binkley.labs.skratch.layers

import hm.binkley.util.Stack

interface Layers<
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    > : Map<K, V> {
    /** A read-only view of editing history. */
    val history: Stack<Layer<K, V, L>>

    /**
     * Gets the present value of [key] as type [T], possibly `null` if a
     * rule hides [key].
     * This is distinct from [Map.get].
     *
     * @throws MissingRuleException if there is no rule for [key]
     */
    fun <T : V> getAs(key: K): T?

    /** Gets the top layer. */
    fun peek(): L

    /** Presents a view of the layers as-if [layer] were the top layer. */
    fun whatIf(layer: L): Layers<K, V, L>

    /** Presents a view of the layers as-if [block] edited the top layer. */
    fun whatIf(block: EditMap<K, V>.() -> Unit): Layers<K, V, L>
}
