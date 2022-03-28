package hm.binkley.util

interface Stack<out T> : List<T> {
    /**
     * Peeks at top element.
     *
     * This is logically equivalent to:
     * ```
     * val top = pop()
     * push(top)
     * return top
     * ```
     * but does not mutate the stack.
     */
    fun peek() = last()
}

fun <T> emptyStack(): Stack<T> = stackOf()
fun <T> stackOf(vararg elements: T): Stack<T> = mutableStackOf(*elements)

/**
 * Returns a new [Stack] filled with all elements of this collection.
 *
 * This is a _shallow_ copy.
 */
fun <T> Collection<T>.toStack(): Stack<T> = toMutableStack()

interface MutableStack<T> : Stack<T>, MutableList<T> {
    /**
     * Pops the top element from the stack.
     *
     * @return the previously top element
     * @throws NoSuchElementException if the stack is empty
     */
    fun pop(): T = removeLast()

    /**
     * Pushes [element] to the top of the stack.
     *
     * @return [element]
     */
    fun push(element: T): T {
        add(element)
        return element
    }
}

open class ArrayMutableStack<T> private constructor(
    private val elements: MutableList<T> = ArrayList(),
) : MutableStack<T>, MutableList<T> by elements {
    constructor(elements: Collection<T>) : this(ArrayList(elements))
    constructor(initialCapacity: Int) : this(ArrayList(initialCapacity))

    // TODO: equals and hashCode
    override fun toString() = elements.toString()

    companion object {
        // TODO: Wrapper for [List]->[Stack]

        /** Returns a [Stack] that wraps the list. */
        fun <T> MutableList<T>.asStack(): Stack<T> =
            ArrayMutableStack(this)

        /** Returns a [MutableStack] that wraps the list. */
        fun <T> MutableList<T>.asMutableStack(): Stack<T> =
            ArrayMutableStack(this)
    }
}

fun <T> emptyMutableStack(): MutableStack<T> = mutableStackOf()
fun <T> mutableStackOf(vararg elements: T): MutableStack<T> =
    elements.toList().toMutableStack()

/** Returns a [MutableStack] filled this collection. */
fun <T> Collection<T>.toMutableStack(): MutableStack<T> =
    ArrayMutableStack(this)

/** Duplicates (re-pushes) the top element. */
fun <T> MutableStack<T>.duplicate() {
    push(peek())
}

/**
 * Rotates the top [n] elements "clockwise".
 * Use negative [n] to counter-rotate.
 *
 * Contrast with [java.util.Collections.rotate] which acts as a
 * mathematical "mod" operator: negative [n] "loop around".
 * Other languages (such as Forth) provide two functions: one for clockwise
 * and another for counter-clockwise rotations.
 *
 * Examples given a stack, `[1, 2, 3, 4]`:
 * 1. `rotate()` mutates the stack to `[1, 4, 2, 3]`.
 * 2. `rotate(4)` mutates the stack to `[4, 1, 2, 3]`.
 * 3. `rotate(-3)` mutates the stack to `[1, 3, 4, 2]`.
 *
 * @param n number of elements to rotate, default 3
 */
fun <T> MutableStack<T>.rotate(n: Int = 3) {
    when {
        0 == n -> Unit
        0 > n -> push(removeAt(size + n))
        else -> add(size - n, pop())
    }
}

/**
 * Swaps (rotates) the top 2 elements.
 *
 * @see [rotate]
 */
fun <T> MutableStack<T>.swap() = rotate(2)
