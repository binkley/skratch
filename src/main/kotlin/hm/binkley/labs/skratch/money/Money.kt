package hm.binkley.labs.skratch.money

import java.math.BigDecimal
import java.util.Objects

abstract class Money<M : Money<M>>(
        val currency: String, val amount: BigDecimal) {
    abstract fun with(amount: BigDecimal): M

    operator fun times(other: Int) = with(amount.times(BigDecimal(other)))
    operator fun times(other: Long) = with(amount.times(BigDecimal(other)))
    operator fun times(other: BigDecimal) = with(amount.times(other))

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Money<*>

        return currency == other.currency && amount == other.amount
    }

    final override fun hashCode() = Objects.hash(currency, amount)
}

operator fun <M : Money<M>> Int.times(other: M) = other * this
operator fun <M : Money<M>> Long.times(other: M) = other * this
operator fun <M : Money<M>> BigDecimal.times(other: M) = other * this
