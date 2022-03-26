package hm.binkley.labs.skratch.layers.util

interface Stack<out T> : List<T> {
    fun peek() = last()
}

open class ArrayStack<T>(
    private val elements: Collection<T> = emptyList(),
) : Stack<T>, List<T> by elements.toList()

fun <T> emptyStack(): Stack<T> = stackOf()
fun <T> stackOf(vararg elements: T): Stack<T> = ArrayStack(elements.toList())

fun <T> Collection<T>.toStack() = ArrayStack(this)
