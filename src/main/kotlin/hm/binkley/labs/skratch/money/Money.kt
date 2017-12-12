package hm.binkley.labs.skratch.money

import java.math.BigDecimal
import java.math.RoundingMode.UNNECESSARY
import java.util.Objects

abstract class Money<M : Money<M>>(
        val currency: String, val amount: BigDecimal) {
    protected abstract fun with(amount: BigDecimal): M
    protected abstract fun valid(amount: BigDecimal): BigDecimal
    private fun make(amount: BigDecimal) = with(valid(amount))

    operator fun unaryMinus() = make(-amount)
    @Suppress("UNCHECKED_CAST")
    operator fun unaryPlus(): M = this as M

    operator fun plus(other: M) = make(amount + other.amount)
    operator fun minus(other: M) = make(amount - other.amount)

    operator fun times(other: Int) = make(amount * BigDecimal(other))
    operator fun times(other: Long) = make(amount * BigDecimal(other))

    operator fun div(other: Int): M = make(
            amount.divide(BigDecimal(other), amount.scale(), UNNECESSARY))

    operator fun div(other: Long): M = make(
            amount.divide(BigDecimal(other), amount.scale(), UNNECESSARY))

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Money<*>

        return currency == other.currency && amount == other.amount
    }

    final override fun hashCode() = Objects.hash(currency, amount)

    abstract override fun toString(): String
}

operator fun <M : Money<M>> Int.times(other: M) = other * this
operator fun <M : Money<M>> Long.times(other: M) = other * this
