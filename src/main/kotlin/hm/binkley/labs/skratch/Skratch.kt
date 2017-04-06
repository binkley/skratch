package hm.binkley.labs.skratch

class X(private val a: String) : CharSequence by a {
    override fun toString() = a
}

interface Bob<T>
typealias IntBobby = (Int, Int) -> Bob<Int>

class Q : MutableMap<String, Any> by mutableMapOf() {
    operator fun <T> set(key: String, x: (a: Int, b: Int) -> Bob<T>) {
        this[key] = x as Any
    }
}

fun main(args: Array<String>) {
    println(::main)
    println(X("abc"))
    val q = Q()
    q["bob"] = { a, b ->
        object : Bob<Int> {
            override fun toString() = "Int Bob"
        }
    }
    q["mary"] = "mary"
    println("${q["mary"]}: ${(q["bob"] as IntBobby)(1, 2)}")
}
