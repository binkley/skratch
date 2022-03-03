package hm.binkley.labs.skratch.layers

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
