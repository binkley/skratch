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
        except: Collection<Layer<K, V, *>> = emptyList(),
    ): T?

    /** Convenience for [getAs]. */
    fun <T : V> getAs(key: K, vararg except: Layer<K, V, *>): T? =
        getAs(key, except.asList())

    /**
     * Gets the top layer.
     * Implementers provide "peek()", and callers typically use [top].
     */
    fun peek(): L

    /** Presents a view as-if [layer] were the top layer. */
    fun whatIf(layer: L): Layers<K, V, L>

    /** Presents a view as-if [block] edited the top layer. */
    fun whatIf(block: EditMap<K, V>.() -> Unit): Layers<K, V, L>

    /** Presents a view as-if [except] were absent. */
    fun whatIfNot(except: Collection<Layer<K, V, *>>): Layers<K, V, L>

    /** Convenience for [whatIfNot]. */
    fun whatIfNot(vararg except: Layer<K, V, *>): Layers<K, V, L> =
        whatIfNot(except.asList())
}

/**
 * The top (current) layer for editing.
 * Convenience property for [Layers.peek].
 */
val <
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    >
Layers<K, V, L>.top get() = peek()

/** Finds the most recent layer containing [rule] assigned to [key]. */
fun <K : Any, V : Any>
Layers<K, V, *>.find(
    key: K,
    rule: Rule<K, V, *>,
): Layer<K, V, *> = history.asReversed().first { rule == it[key] }

/**
 * Gets the value of [key] in the layers as-if [rule] were not present.
 *
 * @todo Return `T?`.
 *       1) It should be valid for an earlier rule to return `null`
 *       2) It should work when there are no values for [key]
 */
fun <
    K : Any,
    V : Any,
    T : V,
    >
Layers<K, V, *>.getAsWithout(
    key: K,
    rule: Rule<K, V, *>,
): T = getAs(key, find(key, rule))!! // `!!` as [rule] must be present
