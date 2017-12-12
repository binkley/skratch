package hm.binkley.labs.skratch.money

import hm.binkley.labs.skratch.money.CurrencyExchange.MoneyChanger

interface CurrencyExchange {
    fun exchange(money: Money, to: String): Money

    class MoneyChanger(private val money: Money,
            private val exchange: CurrencyExchange) {
        infix fun convertTo(to: String) = exchange.exchange(money, to)
    }
}

infix fun Money.at(exchange: CurrencyExchange) = MoneyChanger(this, exchange)
