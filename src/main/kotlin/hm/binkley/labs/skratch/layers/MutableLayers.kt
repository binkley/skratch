package hm.binkley.labs.skratch.layers

import hm.binkley.util.MutableStack
import hm.binkley.util.Stack
import hm.binkley.util.mutableStackOf
import hm.binkley.util.toMutableStack
import hm.binkley.util.top
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstanceOrNull
import java.util.AbstractMap.SimpleImmutableEntry
import kotlin.collections.Map.Entry

fun interface NewLayer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    > {
    operator fun invoke(index: Int): M
}

// TODO: Messy relationships among "push", "edit", and "whatIf"
open class MutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    > private constructor(
    private val layers: MutableStack<M>,
    private val newLayer: NewLayer<K, V, M>,
) : AbstractMap<K, V>(), Layers<K, V, M> {
    constructor(
        initialRules: NewLayer<K, V, M>,
        newLayer: NewLayer<K, V, M>,
    ) : this(mutableStackOf(initialRules(0)), newLayer)

    /** @todo Only used in testing for pathological cases */
    constructor(
        layers: List<M>,
        newLayer: NewLayer<K, V, M>,
    ) : this(layers.toMutableStack(), newLayer)

    init {
        if (layers.isEmpty()) throw MissingFirstLayerException
        allKeys().forEach { ruleForOrThrow<V>(it) }
    }

    override val history: Stack<Layer<K, V, M>> get() = layers
    override val entries: Set<Entry<K, V>> get() = ValuedEntries()

    override fun <T : V> getAs(
        key: K,
        except: Collection<Layer<K, V, *>>,
    ): T? = whatIfNot(except).valueFor(key)

    override fun peek(): M = layers.top

    override fun whatIf(layer: M): MutableLayers<K, V, M> =
        validate { it.replaceLast(layer) }

    override fun whatIf(
        block: EditMap<K, V>.() -> Unit,
    ): MutableLayers<K, V, M> = whatIf(top.copy().edit(block))

    override fun whatIfNot(except: Collection<Layer<K, V, *>>):
        MutableLayers<K, V, M> = validate { it.removeAll(except.toSet()) }

    fun pop(): M =
        if (1 < layers.size) layers.pop()
        else throw MissingFirstLayerException

    fun <N : M> push(newLayer: NewLayer<K, V, M>): N {
        val valid = validate { it.push(newLayer(layers.size)) }.top
        return layers.push(valid).self()
    }

    /** Creates a [newLayer], edits it, and returns it. */
    fun push(block: EditMap<K, V>.() -> Unit): M =
        push { index -> newLayer(index).edit(block) }

    /** Edits the [top] layer, and returns it. */
    fun edit(block: EditMap<K, V>.() -> Unit): M {
        val valid = whatIf(block).top
        layers.replaceLast(valid)
        return valid
    }

    /**
     * Applies [block] to a shallow defensive copy of the layers, and
     * returns a new [MutableLayers] using the updates from [block].
     * Actual validation is in `init` for the new mutable layers.
     */
    private fun validate(
        block: (MutableStack<M>) -> Unit,
    ): MutableLayers<K, V, M> {
        val layersCopy = layers.toMutableStack()
        block(layersCopy)
        return MutableLayers(layersCopy, newLayer)
    }

    // The "allKeys()" fun is NOT the [keys] property
    private fun allKeys(): Set<K> = layers.flatMap { it.keys }.toSet()

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

    /** Filters non-null values so rules can hide keys. */
    private inner class ValuedEntries : AbstractSet<Entry<K, V>>() {
        override val size: Int get() = iterator().size()
        override fun iterator(): Iterator<Entry<K, V>> = NonNullValues()

        private inner class NonNullValues(
            private val kit: Iterator<K> = allKeys().iterator(),
        ) : AbstractIterator<Entry<K, V>>() {
            override fun computeNext() {
                while (kit.hasNext()) {
                    val key = kit.next()
                    val value = valueFor<V>(key) ?: continue
                    setNext(SimpleImmutableEntry(key, value))
                    return
                }
                done()
            }
        }
    }
}

/**
 * Swaps out the last element in the list, returning the previous last
 * element.
 */
private fun <T> MutableList<T>.replaceLast(element: T): T =
    set(lastIndex, element)

/**
 * Exhausts the iterator to find the element count.
 * The iterator is not reusable afterwards.
 */
private fun <T> Iterator<T>.size(): Int {
    var n = 0
    while (hasNext()) {
        next()
        ++n
    }
    return n
}
