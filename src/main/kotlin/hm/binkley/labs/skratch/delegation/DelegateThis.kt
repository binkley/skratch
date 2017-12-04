package hm.binkley.labs.skratch.delegation

interface Fooby {
    fun foo() = println("Fooby:foo")
    fun bar(): Unit
    fun baz(): Unit
}

class Bob : Fooby {
    override fun bar() {
        println("Bob:bar")
    }

    override fun baz() {
        println("Bob:Baz")
    }

}

class DelegateThis : Fooby by Bob() {
    override fun baz() {
        println("DelegateThis:baz")
    }
}

fun main(args: Array<String>) {
    val x = DelegateThis()
    x.foo()
    x.bar()
    x.baz()
}

