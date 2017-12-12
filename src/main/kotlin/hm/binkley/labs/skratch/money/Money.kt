package hm.binkley.labs.skratch.money

import java.math.BigDecimal

data class Money(val currency: String, val amount: BigDecimal) {
    constructor(currency: String, amount: Int)
            : this(currency, BigDecimal(amount))

    constructor(currency: String, amount: Long)
            : this(currency, BigDecimal(amount))

    constructor(currency: String, amount: Double)
            : this(currency, BigDecimal(amount))

    infix fun at(exchange: CurrencyExchange) = Exchange(this, exchange)

    class Exchange(val money: Money, val exchange: CurrencyExchange) {
        infix fun to(to: String) = exchange.exchange(money, to)
    }
}
