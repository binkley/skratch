package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.DefaultMutableLayer.Companion.defaultMutableLayer
import hm.binkley.labs.skratch.layers.DefaultMutableLayers.Companion.defaultMutableLayers
import java.util.AbstractMap.SimpleEntry
import kotlin.collections.Map.Entry

fun main() {
    val c = defaultMutableLayers<String, Number>()
    c.edit {
        this["ALICE"] = rule<Int>("Latest of") { _, values, _ ->
            if (values.isEmpty()) 0 else values.last()
        }
        this["BOB"] = rule<Double>("Sum[Int]") { _, values, _ ->
            values.sum()
        }
    }
    c.commitAndNext()
    c.edit {
        this["ALICE"] = Value(3)
    }
    val a = c.commitAndNext()
    a.edit {
        this["BOB"] = Value(4.0)
    }
    println(c.history)
    println(c)

    val d =
        DefaultMutableLayers<String, Number, DefaultMutableLayer<String, Number, *>> {
            DefaultMutableLayer()
        }
    d.edit {
        this["CAROL"] = rule<Int>("Product[Int]") { _, values, _ ->
            values.fold(1) { a, b -> a * b }
        }
    }

    class Bob : DefaultMutableLayer<String, Number, Bob>() {
        fun foo() = println("I AM FOCUTUS OF BOB")
    }

    val b = d.commitAndNext { Bob() }
    b.foo()
    b.edit {
        this["CAROL"] = 17.toValue()
    }
    d.commitAndNext()
    d.edit {
        this["CAROL"] = 19.toValue()
    }

    println(d.history)
    println(d)

    println(d.whatIf(mapOf("CAROL" to (-1).toValue())))
}

sealed interface ValueOrRule<V : Any>
data class Value<V : Any>(val value: V) : ValueOrRule<V> {
    override fun toString() = "<Value>: $value"
}

fun <T : Any> T.toValue() = Value(this)

interface EditMap<K : Any, V : Any> : MutableMap<K, ValueOrRule<V>> {
    fun <T : V> rule(
        name: String,
        block: (K, List<T>, EditMap<K, V>) -> T,
    ): Rule<K, V, T> = object : Rule<K, V, T>(name) {
        override fun invoke(
            key: K,
            values: List<T>,
            editMap: EditMap<K, V>,
        ): T = block(key, values, editMap)
    }
}

abstract class Rule<K : Any, V : Any, T : V>(
    val name: String,
) : ValueOrRule<V>, (K, List<T>, EditMap<K, V>) -> T {
    override fun toString() = "<Rule>: $name"
}

interface Layer<K : Any, V : Any, L : Layer<K, V, L>>
    : Map<K, ValueOrRule<V>> {
    @Suppress("UNCHECKED_CAST")
    val self: L
        get() = this as L
}

interface MutableLayer<K : Any, V : Any, M : MutableLayer<K, V, M>>
    : Layer<K, V, M>,
    MutableMap<K, ValueOrRule<V>> {
    fun edit(block: EditMap<K, V>.() -> Unit)
}

open class DefaultMutableLayer<K : Any, V : Any, M : DefaultMutableLayer<K, V, M>>(
    private val map: MutableMap<K, ValueOrRule<V>> = mutableMapOf(),
) : MutableLayer<K, V, M>, MutableMap<K, ValueOrRule<V>> by map {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <K : Any, V : Any> defaultMutableLayer(): DefaultMutableLayer<K, V, *> =
            DefaultMutableLayer<K, V, DefaultMutableLayer<K, V, *>>()
    }

    override fun edit(block: EditMap<K, V>.() -> Unit) =
        LayerEditMap().block()

    override fun toString(): String = map.toString()

    private inner class LayerEditMap
        : EditMap<K, V>, MutableMap<K, ValueOrRule<V>> by map
}

interface Layers<K : Any, V : Any> : Map<K, V> {
    val history: List<Map<K, ValueOrRule<V>>>

    fun whatIf(scenario: Map<K, ValueOrRule<V>>): Map<K, V>
}

interface MutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>
    : Layers<K, V> {
    fun edit(block: EditMap<K, V>.() -> Unit)

    fun commitAndNext(): MutableLayer<K, V, M> // TODO: How to return M?
    fun <N : M> commitAndNext(nextMutableLayer: () -> N): N
}

open class DefaultMutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>(
    private val layers: MutableList<M> = mutableListOf(),
    private val defaultMutableLayer: () -> M,
) : MutableLayers<K, V, M>, AbstractMap<K, V>() {
    init {
        if (layers.isEmpty()) layers.add(defaultMutableLayer())
    }

    companion object {
        fun <K : Any, V : Any> defaultMutableLayers(): MutableLayers<K, V, *> =
            DefaultMutableLayers<K, V, MutableLayer<K, V, *>> {
                defaultMutableLayer<K, V>()
            }
    }

    override val history: List<Map<K, ValueOrRule<V>>> = layers

    override fun whatIf(scenario: Map<K, ValueOrRule<V>>): Map<K, V> {
        val whatIfLayer = defaultMutableLayer()
        whatIfLayer.edit { putAll(scenario) }
        val whatIfLayers = layers.toMutableList()
        whatIfLayers.add(whatIfLayer)

        return DefaultMutableLayers(
            whatIfLayers,
            defaultMutableLayer
        )
    }

    override fun edit(block: EditMap<K, V>.() -> Unit) =
        LayersEditMap().block()

    override fun commitAndNext(): M = commitAndNext(defaultMutableLayer)

    override fun <N : M> commitAndNext(nextMutableLayer: () -> N): N {
        val layer = nextMutableLayer()
        layers.add(layer)
        return layer
    }

    override val entries: Set<Entry<K, V>> get() = ViewSet()

    private inner class ViewIterator : Iterator<Entry<K, V>> {
        private val kit = allKeys().iterator()

        override fun hasNext(): Boolean = kit.hasNext()
        override fun next(): Entry<K, V> {
            val key = kit.next()
            return SimpleEntry(key, computeValue(key))
        }
    }

    private inner class ViewSet : AbstractSet<Entry<K, V>>() {
        override val size: Int get() = allKeys().size
        override fun iterator(): Iterator<Entry<K, V>> = ViewIterator()
    }

    private fun allKeys(): Set<K> = history.flatMap { it.keys }.toSet()
    private fun computeValue(key: K): V {
        val rule = currentRuleFor<V>(key)
        val values = currentValuesFor<V>(key)

        return rule(key, values, LayersEditMap())
    }

    private fun <T : V> currentRuleFor(key: K): Rule<K, V, T> =
        valuesOrRules(key).filterIsInstance<Rule<K, V, T>>().last()

    private fun <T : V> currentValuesFor(key: K): List<T> =
        valuesOrRules(key).filterIsInstance<Value<T>>().map { it.value }

    private fun valuesOrRules(key: K): List<ValueOrRule<V>> =
        layers.mapNotNull { it[key] }

    private inner class LayersEditMap
        : EditMap<K, V>, MutableMap<K, ValueOrRule<V>> by layers.last()
}
