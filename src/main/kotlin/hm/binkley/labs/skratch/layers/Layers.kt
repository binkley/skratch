package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.DefaultMutableLayer.Companion.defaultMutableLayer
import hm.binkley.labs.skratch.layers.DefaultMutableLayers.Companion.defaultMutableLayers
import java.util.AbstractMap.SimpleEntry
import kotlin.collections.Map.Entry

fun main() {
    val c = defaultMutableLayers<String, Number>("C")
    c.edit {
        this["ALICE"] = latestOfRule(0)
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

    println(c)

    val d =
        DefaultMutableLayers<String, Number, DefaultMutableLayer<String, Number, *>>(
            name = "D") {
            DefaultMutableLayer()
        }
    d.edit {
        this["CAROL"] = constantRule(2)
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
    d.commitAndNext()
    d.edit {
        d.edit {
            this["CAROL"] = rule<Int>("Product[Int]") { _, values, _ ->
                values.fold(1) { a, b -> a * b }
            }
        }
    }

    println(d)

    println(d.whatIf(scenario = mapOf("CAROL" to (-1).toValue())))
}

interface Layers<K : Any, V : Any> : Map<K, V> {
    val name: String
    val history: List<Map<K, ValueOrRule<V>>>

    fun whatIf(
        name: String = "<WHAT-IF: ${this.name}>",
        scenario: Map<K, ValueOrRule<V>>,
    ): Map<K, V>
}

interface MutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>
    : Layers<K, V> {
    fun edit(block: EditMap<K, V>.() -> Unit)

    fun commitAndNext(): MutableLayer<K, V, M> // TODO: How to return M?
    fun <N : M> commitAndNext(nextMutableLayer: () -> N): N
}

open class DefaultMutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>(
    private val layers: MutableList<M> = mutableListOf(),
    override val name: String,
    private val defaultMutableLayer: () -> M,
) : MutableLayers<K, V, M>, AbstractMap<K, V>() {
    init {
        if (layers.isEmpty()) layers.add(defaultMutableLayer())
    }

    companion object {
        fun <K : Any, V : Any> defaultMutableLayers(
            name: String,
        ): MutableLayers<K, V, *> =
            DefaultMutableLayers<K, V, MutableLayer<K, V, *>>(name = name) {
                defaultMutableLayer<K, V>()
            }
    }

    override val entries: Set<Entry<K, V>> get() = ViewSet()
    override val history: List<Map<K, ValueOrRule<V>>> = layers

    override fun whatIf(
        name: String,
        scenario: Map<K, ValueOrRule<V>>,
    ): Map<K, V> {
        val whatIfLayer = defaultMutableLayer()
        whatIfLayer.edit { putAll(scenario) }
        val whatIfLayers = layers.toMutableList()
        whatIfLayers.add(whatIfLayer)

        return DefaultMutableLayers(
            whatIfLayers,
            name,
            defaultMutableLayer,
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

    override fun toString() = history.mapIndexed { index, layer ->
        "$index: $layer"
    }.joinToString("\n", "$name: ${super.toString()}\n")

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
