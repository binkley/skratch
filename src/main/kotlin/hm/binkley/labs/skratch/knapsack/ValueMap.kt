package hm.binkley.labs.skratch.knapsack

import hm.binkley.labs.skratch.knapsack.Value.Nonce
import hm.binkley.labs.skratch.knapsack.Value.RuleValue
import kotlin.collections.MutableMap.MutableEntry

class ValueMap(
        private val database: Database,
        val layer: Int,
        private val set: ValueSet = ValueSet(layer))
    : AbstractMutableMap<String, Value>() {
    override val entries: MutableSet<MutableEntry<String, Value>>
        get() = set as MutableSet<MutableEntry<String, Value>>

    override fun put(key: String, value: Value): Value? {
        val previous = set.find { it.key == key }
        set.add(ValueEntry(key, value))
        return previous?.value
    }

    operator fun set(key: String, nonce: Nothing?)
            = put(key, Nonce)

    operator fun set(key: String, value: String)
            = put(key, database.value(value))

    operator fun <T> set(key: String, rule: Rule<T>)
            = put(key, RuleValue(rule))
}
