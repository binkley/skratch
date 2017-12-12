package hm.binkley.labs.skratch.money

interface CurrencyExchange {
    fun exchange(money: Money, to: String): Money
}
