package hm.binkley.labs.skratch.knapsack

import java.util.Objects
import kotlin.collections.MutableMap.MutableEntry

class ValueEntry(override val key: String, override val value: Value)
    : MutableEntry<String, Value> {
    override fun setValue(newValue: Value): Value {
        throw UnsupportedOperationException()
    }

    fun remove(layer: Int) = value.remove(layer, key)

    fun replaceWith(layer: Int, element: ValueEntry) = value.replaceWith(
            layer, key, element.value)

    fun add(layer: Int) = value.add(layer, key)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ValueEntry

        return key == other.key && value == other.value
    }

    override fun hashCode() = Objects.hash(key, value)
}
