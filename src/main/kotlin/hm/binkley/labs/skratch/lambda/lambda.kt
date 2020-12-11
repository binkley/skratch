package hm.binkley.labs.skratch.lambda

interface Psyche

class Lambda {
    fun bob() = 3
}

class Beta : Psyche {
    fun bob() = 4
}

inline infix fun <T : Psyche, reified R> T.doit(block: T.() -> R): R {
    print("Psyche! ")
    return block(this)
}

inline infix fun <T, R> T.doit(block: T.() -> R) = block(this)

inline fun <reified T> lamb(): T =
    T::class.java.getDeclaredConstructor().newInstance()

fun main() {
    val lamb: Lambda = lamb()
    val sheep: Beta = lamb()

    println(lamb doit { bob() })
    println(sheep doit { bob() })
}
