package hm.binkley.labs.skratch.circular

fun main() {
    println("${P.ZERO} -> ${P.valueOf(0)}")
    println("${Q.ZERO} -> ${Q.valueOf(0)}")
}

private interface Zeroable<T> {
    val ZERO: T
}

private abstract class XCompanionBase<T : XBase<T>>(
    @JvmField
    override val ZERO: T,
) : Zeroable<T> {
    abstract fun valueOf(x: Int): T

    protected fun construct(x: Int, ctor: (Int) -> T) =
        if (0 == x) ZERO else ctor(x)
}

private abstract class XBase<T : XBase<T>> protected constructor(
    val x: Int,
    val companion: XCompanionBase<T>,
) {
    override fun toString() = "${super.toString()}: $x"
}

private class P private constructor(x: Int) : XBase<P>(x, Companion) {
    companion object : XCompanionBase<P>(
        ZERO = P(0),
    ) {
        override fun valueOf(x: Int): P = construct(x) { P(it) }
    }
}

private class Q private constructor(x: Int) : XBase<Q>(x, Companion) {
    companion object : XCompanionBase<Q>(
        ZERO = Q(0),
    ) {
        override fun valueOf(x: Int): Q = construct(x) { Q(it) }
    }
}
