package hm.binkley.labs.skratch.layers.util

import java.util.Collections.rotate

interface MutableStack<T> : Stack<T>, MutableList<T> {
    fun push(element: T) = add(element)
    fun pop() = removeLast()
}

open class ArrayMutableStack<T>(
    private val elements: Collection<T> = emptyList(),
) : MutableStack<T>, MutableList<T> by elements.toMutableList()

fun <T> emptyMutableStack(): MutableStack<T> = mutableStackOf()
fun <T> mutableStackOf(vararg elements: T): MutableStack<T> =
    ArrayMutableStack(elements.toList())

fun <T> Collection<T>.toMutableStack() = ArrayMutableStack(this)

fun <T> MutableStack<T>.duplicate() {
    push(peek())
}

fun <T> MutableStack<T>.rotate(n: Int = 3) {
    when {
        0 == n -> Unit
        0 > n -> push(removeAt(size + n))
        else -> add(size - n, pop())
    }
}

fun <T> MutableStack<T>.swap() = rotate(2)
