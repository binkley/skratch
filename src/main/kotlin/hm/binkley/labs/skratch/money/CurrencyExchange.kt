package hm.binkley.labs.skratch.money

import hm.binkley.labs.skratch.money.CurrencyExchange.ChangedMoney
import hm.binkley.labs.skratch.money.CurrencyExchange.MoneyChanger
import kotlin.reflect.KClass

interface CurrencyExchange {
    fun <M : AbstractMoney<M>, O : AbstractMoney<O>> exchange(money: M, to: KClass<O>): O

    class MoneyChanger<M : AbstractMoney<M>>(
        private val money: M,
        private val exchange: CurrencyExchange,
    ) {
        infix fun <O : AbstractMoney<O>> convertTo(to: KClass<O>) = exchange.exchange(
            money, to)
    }

    class ChangedMoney<M : AbstractMoney<M>, O : AbstractMoney<O>>(
        private val money: M,
        private val to: KClass<O>,
    ) {
        infix fun at(exchange: CurrencyExchange) =
            exchange.exchange(money, to)
    }
}

infix fun <M : AbstractMoney<M>> M.at(exchange: CurrencyExchange) = MoneyChanger(this,
    exchange)

infix fun <M : AbstractMoney<M>, O : AbstractMoney<O>> M.convertTo(
    to: KClass<O>,
) = ChangedMoney(this, to)
