package hm.binkley.labs.skratch.math.algebra

import java.util.Objects.hash

class Mod3Int private constructor(
    val value: Int
) : Ring<Mod3Int> {
    override val constants: Constants get() = Mod3Int

    override fun unaryMinus(): Mod3Int = valueOf(-value)
    override fun plus(addend: Mod3Int): Mod3Int =
        valueOf(value + addend.value)

    override fun times(factor: Mod3Int): Mod3Int =
        valueOf(value * factor.value)

    operator fun inc(): Mod3Int = valueOf(value + 1)
    operator fun dec(): Mod3Int = valueOf(value - 1)

    override fun equals(other: Any?): Boolean = this === other
    override fun hashCode(): Int = hash(this::class, value)
    override fun toString(): String = value.toString()

    companion object Constants : RingConstants<Mod3Int> {
        @JvmStatic
        fun valueOf(value: Int): Mod3Int = when (value.mod(3)) {
            0 -> ZERO
            1 -> ONE
            else -> TWO
        }

        override val ZERO: Mod3Int = Mod3Int(0)
        override val ONE: Mod3Int = Mod3Int(1)

        @JvmField
        val TWO: Mod3Int = Mod3Int(2)
    }
}
