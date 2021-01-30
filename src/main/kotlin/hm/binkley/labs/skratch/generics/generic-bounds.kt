package hm.binkley.labs.skratch.generics

fun main() {
    val x: MutableList<Q> = mutableListOf(Q())
    val l = Layers<P, Q>(x)
    println(l)
    val ll = l.edit {
        this["a"] = 3
    }
    println(ll)
    l.nextLayer { Q() }
    println(l)
}

private class P : Layer<P>()
private class Q : MutableLayer<Q>()
