package hm.binkley.labs.skratch.layers

import hm.binkley.util.Stack

interface Layers<
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    > : Map<K, V> {
    /** A read-only view of editing history in oldest-to-newest order. */
    val history: Stack<Layer<K, V, L>>

    /**
     * Gets the present value of [key] as type [T], possibly `null` if a
     * rule hides [key].
     * This is distinct from [Map.get].
     *
     * @param key the key of the value to get
     * @param except default no layers to exclude (empty list)
     *
     * @throws MissingRuleException if there is no rule for [key]
     */
    fun <T : V> getAs(
        key: K,
        except: Collection<Layer<K, V, *>> = emptyList()
    ): T?

    /** Convenience for [getAs]. */
    fun <T : V> getAs(key: K, vararg except: Layer<K, V, *>): T? =
        getAs(key, except.asList())

    /** Gets the top layer. */
    fun peek(): L

    /** Presents a view of the layers as-if [layer] were the top layer. */
    fun whatIf(layer: L): Layers<K, V, L>

    /** Presents a view of the layers as-if [block] edited the top layer. */
    fun whatIf(block: EditMap<K, V>.() -> Unit): Layers<K, V, L>

    /** Presents a view of the layers as-if [except] were absent. */
    fun whatIfNot(except: Collection<Layer<K, V, *>>): Layers<K, V, L>

    /** Convenience for [whatIfNot]. */
    fun whatIfNot(vararg except: Layer<K, V, *>): Layers<K, V, L> =
        whatIfNot(except.asList())
}

/** Convenience property for [Layers.peek]. */
val <
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    > Layers<K, V, L>.top
    get() = peek()
