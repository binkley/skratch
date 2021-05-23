package hm.binkley.labs.skratch.lambda

// TODO: Why does this break when it is private?
internal fun interface Onto<T, R> : (T) -> R
private typealias F<T> = Onto<T, T>

private fun interface FF<T> : Onto<F<T>, F<T>>
private fun interface Self<T> : Onto<Self<T>, T>

private fun <T> fixedPointGenerator(): Onto<FF<T>, F<T>> {
    val yCombinator: Self<Onto<FF<T>, F<T>>> =
        Self { y ->
            Onto { f ->
                Onto {
                    f(y(y)(f))(it)
                }
            }
        }
    return yCombinator(yCombinator)
}

private fun <T> higherOrderFor(original: (t: T, f: F<T>) -> T): FF<T> =
    FF { f ->
        Onto {
            original(it, f)
        }
    }

private fun <T> recursiveFor(higherOrderF: FF<T>) =
    fixedPointGenerator<T>()(higherOrderF)

private fun <T> recursiveFor(original: (t: T, f: F<T>) -> T): F<T> =
    recursiveFor(higherOrderFor(original))

// See https://gist.github.com/aruld/3965968/
fun main() {
    val factorial = recursiveFor<Int> { it, factorial ->
        when (it) {
            0 -> 1
            else -> it * factorial(it - 1)
        }
    }

    (0..9).forEach {
        println("$it -> ${factorial(it)}")
    }
}
