package hm.binkley.labs.skratch.money

import java.math.BigDecimal

class SGD private constructor(
    amount: BigDecimal
) : AbstractMoney<SGD>(KnownCurrency.SGD, amount.setScale(2)) {
    override fun with(amount: BigDecimal) = SGD(amount)

    companion object : MoneyFactory<SGD> {
        override fun of(amount: BigDecimal): SGD = SGD(amount)
    }
}
