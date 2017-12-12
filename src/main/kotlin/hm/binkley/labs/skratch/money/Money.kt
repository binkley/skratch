package hm.binkley.labs.skratch.money

import java.math.BigDecimal

data class Money(val currency: String, val amount: BigDecimal) {
    constructor(currency: String, amount: Int)
            : this(currency, BigDecimal(amount))

    constructor(currency: String, amount: Long)
            : this(currency, BigDecimal(amount))

    constructor(currency: String, amount: Double)
            : this(currency, BigDecimal(amount))

    infix fun at(exchange: CurrencyExchange) = MoneyChanger(this, exchange)

    class MoneyChanger(private val money: Money,
            private val exchange: CurrencyExchange) {
        infix fun convertTo(to: String) = exchange.exchange(money, to)
    }
}
