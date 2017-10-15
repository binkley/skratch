package hm.binkley.labs.skratch.collections

class Layers(private val list: MutableList<ValueMap> = ArrayList())
    : AbstractList<ValueMap>() {
    override val size: Int
        get() = list.size

    override fun get(index: Int) = list[index]

    operator fun get(key: String) = list.mapNotNull { it[key] }
}
