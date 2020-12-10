package hm.binkley.labs.skratch

import java.util.function.Function

private interface Branch<F, T> : Function<Branch<F, T>, Function<F, T>>

// See https://gist.github.com/aruld/3965968/
fun <F, T> Y() =
    Function<Function<Function<F, T>, Function<F, T>>, Function<F, T>> { it ->
        object : Branch<F, T> {
            override fun apply(x: Branch<F, T>) = it.apply {
                x.apply(x).apply(it)
            }
        }.apply(object : Branch<F, T> {
            override fun apply(x: Branch<F, T>) = it.apply {
                x.apply(x).apply(it)
            }
        })
    }
