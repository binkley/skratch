package hm.binkley.labs.skratch.generics

fun main() {
    val l = Layers.new<P, Q>(::Q)
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
