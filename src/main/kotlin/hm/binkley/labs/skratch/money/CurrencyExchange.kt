package hm.binkley.labs.skratch.money

import hm.binkley.labs.skratch.money.CurrencyExchange.ChangedMoney
import hm.binkley.labs.skratch.money.CurrencyExchange.MoneyChanger
import kotlin.reflect.KClass

interface CurrencyExchange {
    fun <M : Money<M>, O : Money<O>> exchange(money: M, to: KClass<O>): O

    class MoneyChanger<M : Money<M>>(
        private val money: M,
        private val exchange: CurrencyExchange
    ) {
        infix fun <O : Money<O>> convertTo(to: KClass<O>) =
            exchange.exchange(money, to)
    }

    class ChangedMoney<M : Money<M>, O : Money<O>>(
        private val money: M,
        private val to: KClass<O>
    ) {
        infix fun at(exchange: CurrencyExchange) =
            exchange.exchange(money, to)
    }
}

infix fun <M : Money<M>> M.at(exchange: CurrencyExchange) =
    MoneyChanger(this, exchange)

infix fun <M : Money<M>, O : Money<O>> M.convertTo(to: KClass<O>) =
    ChangedMoney(this, to)
