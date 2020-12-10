package hm.binkley.labs.skratch.lambda

// TODO: Why does this break when it is private?
internal fun interface Onto<T, R> : (T) -> R
private typealias F<T> = Onto<T, T>

private fun interface FF<T> : Onto<F<T>, F<T>>
private fun interface Self<T> : Onto<Self<T>, T>

private fun <T> fixedPointGenerator(): Onto<FF<T>, F<T>> {
    val yCombinator: Self<Onto<FF<T>, F<T>>> =
        Self { a ->
            Onto { f ->
                Onto {
                    f(a(a)(f))(it)
                }
            }
        }
    return yCombinator(yCombinator)
}

private fun <T> recursiveF(higherOrderF: FF<T>) =
    fixedPointGenerator<T>()(higherOrderF)

// See https://gist.github.com/aruld/3965968/
fun main() {
    val higherOrderFact = FF { fact: F<Int> ->
        Onto {
            when (it) {
                0 -> 1
                else -> it * fact(it - 1)
            }
        }
    }
    val fact = recursiveF(higherOrderFact)

    (0..9).forEach {
        println("$it -> ${fact(it)}")
    }
}
