package hm.binkley.labs.skratch.circular

fun main() {
    println("${P.ZERO} -> ${P.valueOf(0)}")
    println("${Q.ZERO} -> ${Q.valueOf(0)}")
    val x = P.valueOf(1)
    println("$x -> ${-x}")
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
) {
    protected abstract val companion: XCompanionBase<T>

    operator fun unaryMinus() = companion.valueOf(-x)

    override fun toString() = "${super.toString()}: $x"
}

private class P private constructor(x: Int) : XBase<P>(x) {
    override val companion get() = P

    companion object : XCompanionBase<P>(
        ZERO = P(0),
    ) {
        @JvmStatic
        override fun valueOf(x: Int): P = construct(x) { P(it) }
    }
}

private class Q private constructor(x: Int) : XBase<Q>(x) {
    override val companion get() = Companion

    companion object : XCompanionBase<Q>(
        ZERO = Q(0),
    ) {
        @JvmStatic
        override fun valueOf(x: Int): Q = construct(x) { Q(it) }
    }
}
