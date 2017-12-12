package hm.binkley.labs.skratch.money

import java.math.BigDecimal

data class Money(val currency: String, val amount: BigDecimal) {
    constructor(currency: String, amount: Int)
            : this(currency, BigDecimal(amount))

    constructor(currency: String, amount: Long)
            : this(currency, BigDecimal(amount))

    constructor(currency: String, amount: Double)
            : this(currency, BigDecimal(amount))

    operator fun times(other: Int)
            = Money(currency, amount.times(BigDecimal(other)))

    operator fun times(other: Long)
            = Money(currency, amount.times(BigDecimal(other)))

    operator fun times(other: BigDecimal)
            = Money(currency, amount.times(other))

    companion object {
        fun one(currency: String) = Money(currency, 1)
    }
}

operator fun Int.times(other: Money) = other * this
operator fun Long.times(other: Money) = other * this
operator fun BigDecimal.times(other: Money) = other * this
