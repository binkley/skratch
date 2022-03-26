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

fun <T> MutableStack<T>.rotate(n: Int = 3) =
    if (0 > n) rotate(subList(size + n, size).asReversed(), -1 + n)
    else rotate(subList(size - n, size), 1 - n)

fun <T> MutableStack<T>.swap() = rotate(2)
