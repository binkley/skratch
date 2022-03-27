package hm.binkley.util

interface Stack<out T> : List<T> {
    fun peek() = last()
}

fun <T> emptyStack(): Stack<T> = stackOf()
fun <T> stackOf(vararg elements: T): Stack<T> = mutableStackOf(*elements)
fun <T> Collection<T>.toStack(): Stack<T> = toMutableStack()

interface MutableStack<T> : Stack<T>, MutableList<T> {
    fun push(element: T): T = element.also { add(it) }
    fun pop(): T = removeLast()
}

open class ArrayMutableStack<T>(
    private val elements: List<T> = ArrayList(),
) : MutableStack<T>, MutableList<T> by elements.toMutableList() {
    constructor(elements: Collection<T>) : this(ArrayList(elements))
    constructor(initialCapacity: Int) : this(ArrayList(initialCapacity))

    // TODO: equals and hashCode
    override fun toString() = elements.toString()
}

fun <T> emptyMutableStack(): MutableStack<T> = mutableStackOf()
fun <T> mutableStackOf(vararg elements: T): MutableStack<T> =
    elements.toList().toMutableStack()

fun <T> Collection<T>.toMutableStack(): MutableStack<T> =
    ArrayMutableStack(this)

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
