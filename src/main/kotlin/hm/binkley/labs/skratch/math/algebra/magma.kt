package hm.binkley.labs.skratch.math.algebra

interface MagmaConstants<T : Magma<T>>

/**
 * @todo There is no proper way to express this in Kotlin.  For example, a
 *       [Field] implements the four classic arithmetic operations, each
 *       function having a separate name.  However, each of those binary
 *       operations implements a Magma, so would collide function names in
 *       the overriding.
 */
interface Magma<T : Magma<T>> {
    val constants: MagmaConstants<T>
}
