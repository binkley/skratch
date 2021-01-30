package hm.binkley.labs.skratch.generics

fun main() {
    val x: MutableList<MutableLayer<Q>> = mutableListOf(Q())
    val l = Layers<P, Q>(x)
    println(l)
    l.edit {
        this["a"] = 3
    }
    println(l)
    l.nextLayer { Q() }
    println(l)
}

private class P : Layer<P>()
private class Q : MutableLayer<Q>()
