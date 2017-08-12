package hm.binkley.labs.skratch.math

import java.lang.Math as nativeMath

inline infix fun Int.pow(that: Int) = nativeMath.pow(this.toDouble(),
        that.toDouble()).toInt()

fun main(args: Array<String>) {
    println(3 pow 4)
}
