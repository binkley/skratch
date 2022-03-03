package hm.binkley.labs.skratch.money

import java.math.BigDecimal
import java.math.RoundingMode.UNNECESSARY
import java.util.Objects.hash

interface Money<M : Money<M>> {
    val currency: Currency
    val amount: BigDecimal

    /**
     * Creates a new `Money` of the same [currency] with the given [amount].
     */
    fun with(amount: BigDecimal): M

    /** Force implementations to define equality. */
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

    /**
     * Canonical implementation is `currency.format(amount)`, but one
     * cannot override `Any.toString()` in an interface.  Force
     * implementations to define stringiness.
     */
    override fun toString(): String
}

interface MoneyFactory<M : Money<M>> {
    fun of(amount: Int): M = of(amount.toLong())
    fun of(amount: Long): M = of(amount.toBigDecimal())
    fun of(amount: String): M = of(amount.toBigDecimal())
    fun of(amount: BigDecimal): M
}

/** @todo How to ensure each [KnownCurrency] has a matching money? */
abstract class AbstractMoney<M : AbstractMoney<M>>(
    override val currency: Currency,
    override val amount: BigDecimal,
) : Money<M> {
    final override fun equals(other: Any?): Boolean = this === other ||
        other is Money<*> &&
        currency == other.currency &&
        amount == other.amount

    final override fun hashCode() = hash(currency, amount)
    final override fun toString() = currency.format(amount)
}

operator fun <M : Money<M>> M.unaryPlus(): M = this
operator fun <M : Money<M>> M.unaryMinus() = with(-amount)

operator fun <M : Money<M>> M.plus(other: M) = with(amount + other.amount)
operator fun <M : Money<M>> M.minus(other: M) = with(amount - other.amount)

operator fun <M : Money<M>> M.times(other: Int) = this * other.toLong()
operator fun <M : Money<M>> M.times(other: Long) = this * other.toBigDecimal()
operator fun <M : Money<M>> M.times(other: BigDecimal) = with(amount * other)
operator fun <M : Money<M>> Int.times(other: M) = other * this
operator fun <M : Money<M>> Long.times(other: M) = other * this
operator fun <M : Money<M>> BigDecimal.times(other: M) = other * this

// TODO: This should use BigRationals; ie, non-decimal currencies
operator fun <M : Money<M>> M.div(other: Int): M = this / other.toLong()
operator fun <M : Money<M>> M.div(other: Long): M =
    this / other.toBigDecimal()

operator fun <M : Money<M>> M.div(other: BigDecimal): M = with(
    amount.divide(other, amount.scale(), UNNECESSARY)
)
