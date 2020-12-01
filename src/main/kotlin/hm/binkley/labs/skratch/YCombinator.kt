package hm.binkley.labs.skratch

import java.util.function.Function

object YCombinator {
    private interface Branch<F, T> : Function<Branch<F, T>, Function<F, T>>

    fun <F, T> y() = Function<Function<Function<F, T>, Function<F, T>>, Function<F, T>> { it ->
        object : Branch<F, T> {
            override fun apply(x: Branch<F, T>) = it.apply(
                    Function { x.apply(x).apply(it) })
        }.apply(object : Branch<F, T> {
            override fun apply(x: Branch<F, T>) = it.apply(
                    Function { x.apply(x).apply(it) })
        })
    }
}
