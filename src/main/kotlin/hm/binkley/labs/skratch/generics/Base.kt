package hm.binkley.labs.skratch.generics

interface Base<K : Any, out V : Any, out B : Base<K, V, B>> : Map<K, V> {
    @Suppress("UNCHECKED_CAST")
    val self: B
        get() = this as B
}

interface EditMap<K : Any, V : Any> : MutableMap<K, V>

interface MutableBase<K : Any, V : Any, M : MutableBase<K, V, M>>
    : Base<K, V, M> {
    fun edit(block: EditMap<K, V>.() -> Unit): M
}

abstract class AbstractBase<K : Any, out V : Any, out B : AbstractBase<K, V, B>>(
    private val map: Map<K, V>,
) : Base<K, V, B>, Map<K, V> by map {
    override fun toString() = map.toString()
}

abstract class AbstractMutableBase<K : Any, V : Any, M : AbstractMutableBase<K, V, M>>(
    private val map: MutableMap<K, V>,
) : MutableBase<K, V, M>, AbstractBase<K, V, M>(map) {
    override fun edit(block: EditMap<K, V>.() -> Unit): M {
        DefaultEditMap().block()
        return self
    }

    private inner class DefaultEditMap
        : EditMap<K, V>, MutableMap<K, V> by map
}
