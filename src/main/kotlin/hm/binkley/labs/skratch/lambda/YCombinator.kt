package hm.binkley.labs.skratch.lambda

// TODO: Why does this break when it is private?
internal fun interface GF<T, R> : (T) -> R
private typealias F<T> = GF<T, T>

private fun interface FF<T> : (F<T>) -> F<T>
private fun interface Self<T> : (Self<T>) -> T

private fun <T> fixedPointGenerator(): GF<FF<T>, F<T>> {
    val yCombinator: Self<GF<FF<T>, F<T>>> =
        Self { a ->
            GF { f ->
                GF {
                    f(a(a)(f))(it)
                }
            }
        }
    return yCombinator(yCombinator)
}

private fun <T> recursiveF(higherOrderF: FF<T>): GF<T, T> =
    fixedPointGenerator<T>()(higherOrderF)

// See https://gist.github.com/aruld/3965968/
fun main() {
    val higherOrderFactorial = FF { fac: F<Int> ->
        GF {
            if (it == 0) 1 else it * fac(it - 1)
        }
    }
    val factorial = recursiveF(higherOrderFactorial)

    (0..11).forEach {
        println("$it -> ${factorial(it)}")
    }
}
