package hm.binkley.labs.skratch.layers

interface Bases<K : Any, out V : Any, out B : Base<K, V, B>> :
    Map<K, V> {
    val history: List<B>

    fun last(): B = history.last()
}

interface MutableBases<K : Any, V : Any, M : MutableBase<K, V, M>> :
    Bases<K, V, M> {
    fun edit(block: EditMap<K, V>.() -> Unit) = last().edit(block)

    fun <N : M> add(new: N): N
    fun new(): M
    fun add(block: EditMap<K, V>.() -> Unit): M {
        val new = add(new())
        edit(block)
        return new
    }
}

private fun <K : Any, V : Any, B : Base<K, V, B>> defaultView(
    bases: List<B>,
): Map<K, V> = bases.fold(mutableMapOf()) { merged, it ->
    merged.putAll(it); merged
}

abstract class AbstractBases<K : Any, out V : Any, out B : Base<K, V, B>>(
    protected open val bases: List<B>,
) : Bases<K, V, B>, Map<K, V> by defaultView(bases) {
    override val history: List<B> get() = bases

    override fun toString() = defaultView(bases).toString()
}

abstract class AbstractMutableBases<K : Any, V : Any, M : MutableBase<K, V, M>>(
    override val bases: MutableList<M> = mutableListOf(),
) : MutableBases<K, V, M>, AbstractBases<K, V, M>(bases) {
    override fun <N : M> add(new: N): N = new.also { bases.add(new) }
}
