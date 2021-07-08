package hm.binkley.labs.skratch.generics

interface Bases<K : Any, out V : Any, out B : Base<K, V, B>>
    : Map<K, V> {
    val history: List<B>

    fun last(): B = history.last()
}

interface MutableBases<K : Any, V : Any, M : MutableBase<K, V, M>>
    : Bases<K, V, M> {
    fun mutate(block: EditMap<K, V>.() -> Unit) = last().mutate(block)

    fun <N : M> add(new: N): N
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

fun main() {
    class MyMutableBase(
        map: MutableMap<String, Number> = mutableMapOf(),
    ) : AbstractMutableBase<String, Number, MyMutableBase>(map)

    val bases =
        object : AbstractMutableBases<String, Number, MyMutableBase>() {}

    println(bases)
    println(bases.history)
    println(bases.add(MyMutableBase()).self)
    println(bases)
    println(bases.history)
    println(bases.add(MyMutableBase(mutableMapOf("STRING" to 3))).self)
    println(bases)
    println(bases.history)
}
