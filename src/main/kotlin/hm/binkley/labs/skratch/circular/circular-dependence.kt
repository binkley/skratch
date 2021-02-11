package hm.binkley.labs.skratch.circular

fun main() {
    println(Q.T)
}

private abstract class XCompanion<T : X<T>>() {
    abstract val T: T
}

private abstract class X<T : X<T>>() {
    override fun toString() = "I am an X"
}

private class Q : X<Q>() {
    companion object : XCompanion<Q>() {
        override val T = Q()
    }
}
