package hm.binkley.labs.skratch.money

import java.math.BigDecimal

class SGD(amount: BigDecimal) :
    AbstractMoney<SGD>(KnownCurrencies.SGD, amount.setScale(2)) {
    constructor(amount: Int) : this(BigDecimal(amount))
    constructor(amount: Long) : this(BigDecimal(amount))
    constructor(amount: String) : this(BigDecimal(amount))

    override fun with(amount: BigDecimal) = SGD(amount)
}
