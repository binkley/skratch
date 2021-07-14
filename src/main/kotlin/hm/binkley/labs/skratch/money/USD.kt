package hm.binkley.labs.skratch.money

import java.math.BigDecimal

class USD private constructor(amount: BigDecimal) :
    AbstractMoney<USD>(KnownCurrencies.USD, amount.setScale(2)) {

    override fun with(amount: BigDecimal) = USD(amount)

    companion object : MoneyFactory<USD> {
        override fun valueOf(amount: BigDecimal): USD = USD(amount)
    }
}
