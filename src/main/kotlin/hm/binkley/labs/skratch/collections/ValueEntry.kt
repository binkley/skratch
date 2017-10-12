package hm.binkley.labs.skratch.collections

import kotlin.collections.MutableMap.MutableEntry

data class ValueEntry(override val key: String, override val value: Value)
    : MutableEntry<String, Value> {
    override fun setValue(newValue: Value): Value {
        throw UnsupportedOperationException()
    }

    fun remove(layer: Int) = value.remove(layer, key)

    fun replaceWith(layer: Int, element: ValueEntry)
            = value.replaceWith(layer, key, element.value)

    fun add(layer: Int) = value.add(layer, key)
}
