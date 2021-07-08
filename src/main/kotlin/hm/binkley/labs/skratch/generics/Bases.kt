package hm.binkley.labs.skratch.generics

interface Bases<K : Any, out V : Any, out B : Base<K, V, B>>
    : Map<K, V> {
    val history: List<B>

    fun last(): B = history.last()
}

interface MutableBases<K : Any, V : Any, M : MutableBase<K, V, M>>
    : Bases<K, V, M> {
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

fun main() {
    open class MyMutableBase(
        map: MutableMap<String, Number> = mutableMapOf(),
    ) : AbstractMutableBase<String, Number, MyMutableBase>(map)

    val bases =
        object : AbstractMutableBases<String, Number, MyMutableBase>() {
            override fun new() = MyMutableBase()

            fun doHickey(): MyMutableBase =
                MyMutableBase(mutableMapOf("HUM-HUM" to 2))
        }

    println("--- EMPTY")
    println("BASES -> $bases")
    println("HISTORY -> ${bases.history}")
    println("STRING -> ${bases["STRING"]}")
    println("--- ADD EMPTY")
    println(bases.add(MyMutableBase()).self)
    println("BASES -> $bases")
    println("HISTORY -> ${bases.history}")
    println("STRING -> ${bases["STRING"]}")
    println("--- ADD NOT EMPTY")
    println(bases.add(MyMutableBase(mutableMapOf("STRING" to 3))).self)
    println("BASES -> $bases")
    println("HISTORY -> ${bases.history}")
    println("STRING -> ${bases["STRING"]}")
    println("--- ADD CUSTOM")
    println(bases.add(bases.doHickey()).self)
    println("--- ADD VIA BLOCK")
    println(bases.add {
        this["BOB"] = 77
    })
    println("BASES -> $bases")
    println("HISTORY -> ${bases.history}")
    println("STRING -> ${bases["STRING"]}")

    open class OhMyMutableBase<M : OhMyMutableBase<M>>
        : MyMutableBase(mutableMapOf("message" to 17)) {
        open fun ohMy() = println("OH, MY, ${this["message"]}!")
    }

    class MyWordMutableBase : OhMyMutableBase<MyWordMutableBase>() {
        init {
            edit {
                this["message"] = 31
            }
        }

        fun myWord() = println("MY, WORD!")
    }

    val wordy = bases.add(MyWordMutableBase()).apply {
        ohMy()
        myWord()
    }

    println("-- MORE EXTENDING")
    println("WORDY -> $wordy")
    println("BASES -> $bases")
    println("HISTORY -> ${bases.history}")
    println("STRING -> ${bases["STRING"]}")
}
