package hm.binkley.labs.skratch.money

import java.math.BigDecimal

data class Money(val currency: String, val amount: BigDecimal) {
    infix fun at(exchange: CurrencyExchange) = Exchange(this, exchange)

    class Exchange(val money: Money, val exchange: CurrencyExchange) {
        infix fun to(to: String) = exchange.exchange(money, to)
    }
}
