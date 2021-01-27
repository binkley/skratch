package hm.binkley.labs.skratch

import java.util.AbstractMap.SimpleEntry
import kotlin.collections.Map.Entry

fun main() {
    val layer = object : AbstractMap<String, Any>() {
        private val top = mutableListOf<Map<String, Ding<Any>>>()
        override val entries: Set<Entry<String, Any>>
            get() = top.flatMap {
                it.keys
            }.distinct().map {
                SimpleEntry(it, x(it, top))
            }.toSet()

        init {
            top.add(mapOf(
                "a" to object : RuleDing<String> {
                    override fun invoke(
                        key: String,
                        values: List<String>,
                        layers: Layers,
                    ) = key.toUpperCase()

                    override fun toString() = "UPCASE"
                },
                "b" to object : RuleDing<Int> {
                    override fun invoke(
                        key: String,
                        values: List<Int>,
                        layers: Layers,
                    ) = 2 * values.first()

                    override fun toString() = "DOUBLE MOST RECENT"
                }
            ))
            top.add(mapOf(
                "a" to "A".ding(),
                "b" to 3.ding(),
            ))
            top.add(mapOf(
                "b" to 4.ding(),
            ))
        }
    }

    println("LAYER -> $layer")
}

@Suppress("UNCHECKED_CAST")
private fun x(key: String, top: List<Map<String, Ding<*>>>): Any {
    var rule: RuleDing<Any>? = null
    val values = ArrayList<Any>(top.size)

    for (layer in top.asReversed()) {
        val ding = layer[key] ?: continue
        when (ding) {
            is RuleDing<*> -> if (null == rule) rule = (ding as RuleDing<Any>)
            else -> values.add((ding as ValueDing<Any>).value)
        }
    }
    if (null == rule) error("No rule for key: $key")
    else return rule(key, values, SimpleLayers())
}

// TODO: How many times am I going to play with this?

interface Layer : Map<String, Any>
interface MutableLayer : Layer, MutableMap<String, Any>

interface Layers : Map<String, Any> {
    val layers: List<Layer>
}

interface Ding<out T>

data class ValueDing<out T>(
    val value: T,
) : Ding<T>

fun <T> T.ding() = ValueDing(this)

interface RuleDing</* out */ T> : Ding<T>,
        (String, List<T>, Layers) -> T

class SimpleLayers : Layers, AbstractMap<String, Any>() {
    private val _layers = mutableListOf<Map<String, Ding<*>>>()

    // TODO: BORKEN
    @Suppress("UNCHECKED_CAST")
    override val layers: List<Layer>
        get() = _layers as List<Layer>

    override val entries: Set<Map.Entry<String, Any>>
        get() = layers.flatMap { layer ->
            layer.keys
        }.distinct().map { key ->
            SimpleEntry(key, valueFor(key))
        }.toSet()

    private fun valueFor(key: String): Any {
        val rule = ruleFor<Any>(key)
        val values = valuesFor<Any>(key)
        return rule(key, values, this)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> ruleFor(key: String) = (layers.map { layer ->
        layer[key]
    }.find { ding ->
        ding is RuleDing<*>
    } ?: error("No rule for key: $key")) as ((String, List<T>, Layers) -> T)

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> valuesFor(key: String) = layers.map { layer ->
        layer[key]
    }.filterIsInstance<ValueDing<*>>().map { entry ->
        entry.value as T
    }
}
