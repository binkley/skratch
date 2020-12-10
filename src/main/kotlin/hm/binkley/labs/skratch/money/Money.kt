package hm.binkley.labs.skratch.money

import java.math.BigDecimal
import java.math.RoundingMode.UNNECESSARY
import java.util.Objects.hash

abstract class Money<M : Money<M>>(
    val currency: String, val amount: BigDecimal,
) {
    protected abstract fun with(amount: BigDecimal): M

    operator fun unaryMinus() = with(-amount)

    @Suppress("UNCHECKED_CAST")
    operator fun unaryPlus(): M = this as M

    operator fun plus(other: M) = with(amount + other.amount)
    operator fun minus(other: M) = with(amount - other.amount)

    operator fun times(other: Int) = this * other.toLong()
    operator fun times(other: Long) = this * BigDecimal(other)
    operator fun times(other: BigDecimal) = with(amount * other)

    // TODO: This should use Rationals; ie, non-decimal currencies
    operator fun div(other: Int): M = this / other.toLong()
    operator fun div(other: Long): M = this / BigDecimal(other)
    operator fun div(other: BigDecimal): M = with(
        amount.divide(other, amount.scale(), UNNECESSARY))

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Money<*>

        return currency == other.currency && amount == other.amount
    }

    final override fun hashCode() = hash(currency, amount)

    abstract override fun toString(): String
}

operator fun <M : Money<M>> Int.times(other: M) = other * this
operator fun <M : Money<M>> Long.times(other: M) = other * this
operator fun <M : Money<M>> BigDecimal.times(other: M) = other * this
