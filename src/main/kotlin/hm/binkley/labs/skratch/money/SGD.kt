package hm.binkley.labs.skratch.money

import java.math.BigDecimal

class SGD(amount: BigDecimal) : Money<SGD>("SGD", amount) {
    constructor(amount: Int) : this(BigDecimal(amount))
    constructor(amount: Long) : this(BigDecimal(amount))
    constructor(amount: Double) : this(BigDecimal(amount))

    override fun with(amount: BigDecimal) = SGD(amount)
}
