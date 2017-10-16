package hm.binkley.labs.skratch.collections

import hm.binkley.labs.skratch.collections.Value.DatabaseValue

class Layers(private val list: MutableList<ValueMap> = ArrayList())
    : AbstractList<ValueMap>() {
    override val size: Int
        get() = list.size

    override fun get(index: Int) = list[index]

    operator fun get(key: String) = list.
            mapNotNull { it[key] }.
            filter { it is DatabaseValue }.
            map { (it as DatabaseValue).value }
}
