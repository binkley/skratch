package hm.binkley.labs.skratch.layers

import hm.binkley.util.MutableStack
import hm.binkley.util.Stack
import hm.binkley.util.mutableStackOf
import hm.binkley.util.toMutableStack
import hm.binkley.util.top
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstanceOrNull
import java.util.AbstractMap.SimpleImmutableEntry
import kotlin.collections.Map.Entry

// TODO: Messy relationships among "push", "edit", and "whatIf"
open class MutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    private val newLayer: () -> MutableLayer<K, V, M>,
    private val layers: MutableStack<M>,
) : AbstractMap<K, V>(), Layers<K, V, M> {
    constructor(
        newLayer: () -> MutableLayer<K, V, M>,
        initialRules: M,
    ) : this(newLayer, mutableStackOf(initialRules))
    constructor(
        newLayer: () -> MutableLayer<K, V, M>,
        layers: List<M>,
    ) : this(newLayer, layers.toMutableStack())

    init {
        if (layers.isEmpty()) throw MissingFirstLayerException
        keys().forEach { ruleForOrThrow<V>(it) }
    }

    override val history: Stack<Layer<K, V, M>> get() = layers
    override val entries: Set<Entry<K, V>> get() = RuledEntries()
    override fun <T : V> getAs(
        key: K,
        except: Collection<Layer<K, V, *>>
    ): T? = whatIfNot(except).valueFor(key)

    override fun peek(): M = layers.top

    override fun whatIf(layer: M): MutableLayers<K, V, M> =
        validate { it.replaceLast(layer) }

    override fun whatIf(
        block: EditMap<K, V>.() -> Unit,
    ): MutableLayers<K, V, M> = whatIf(copyOfTop().edit(block))

    override fun whatIfNot(except: Collection<Layer<K, V, *>>):
        MutableLayers<K, V, M> = validate { it.removeAll(except.toSet()) }

    fun pop(): M =
        if (1 < layers.size) layers.pop()
        else throw MissingFirstLayerException

    fun <N : M> push(layer: N): N {
        val valid = validate { it.push(layer) }.top
        return layers.push(valid).self()
    }

    fun push(block: EditMap<K, V>.() -> Unit): M =
        push(newLayer().edit(block))

    fun edit(block: EditMap<K, V>.() -> Unit): M {
        val valid = whatIf(block).top
        layers.replaceLast(valid)
        return valid
    }

    private fun copyOfTop() = top.copy()

    /**
     * Applies [block] to a shallow defensive copy of the layers, and
     * returns a new [MutableLayers] using the updates from [block].
     * As the init block validates the initial layers, the return is valid.
     */
    private fun validate(
        block: (MutableStack<M>) -> Unit,
    ): MutableLayers<K, V, M> {
        val copy = layers.toMutableStack()
        block(copy)
        return MutableLayers(newLayer, copy)
    }

    // The "keys()" fun is NOT the [keys] property
    private fun keys(): Set<K> =
        layers.asSequence().flatMap {
            it.keys
        }.toSet()

    private fun <T : V> valueFor(key: K): T? {
        val rule = ruleForOrThrow<T>(key)
        val values = valuesFor<T>(key)

        return rule(key, values, this)
    }

    private fun <T : V> ruleForOrThrow(key: K): Rule<K, V, T> =
        valuesOrRulesFor<T>(key).firstIsInstanceOrNull()
            ?: throw MissingRuleException(key)

    private fun <T : V> valuesFor(key: K): Sequence<T> =
        valuesOrRulesFor<T>(key).filterIsInstance<Value<T>>().map {
            it.value
        }

    private fun <T : V> valuesOrRulesFor(key: K): Sequence<ValueOrRule<T>> =
        layers.asReversed().asSequence().map {
            it[key]
        }.filterIsInstance<ValueOrRule<T>>()

    // Filters null present values so rules can hide keys
    private inner class RuledEntries : AbstractSet<Entry<K, V>>() {
        override val size: Int get() = keys().mapNotNull { valueFor(it) }.size

        override fun iterator(): Iterator<Entry<K, V>> =
            keys().asSequence().map {
                SimpleImmutableEntry<K, V>(it, valueFor(it))
            }.filter {
                null != it.value
            }.iterator()
    }
}

private fun <T> MutableList<T>.replaceLast(element: T): T =
    set(lastIndex, element)
