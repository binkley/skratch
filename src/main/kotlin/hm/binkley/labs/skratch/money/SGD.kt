package hm.binkley.labs.skratch.money

import java.math.BigDecimal

class SGD(amount: BigDecimal) : Money<SGD>("SGD", amount.setScale(2)) {
    constructor(amount: Int) : this(BigDecimal(amount))
    constructor(amount: Long) : this(BigDecimal(amount))
    constructor(amount: String) : this(BigDecimal(amount))

    override fun valid(amount: BigDecimal) = when (amount.scale()) {
        2 -> amount
        else -> throw IllegalArgumentException(
                "$currency has exactly 2 decimal places")
    }

    override fun with(amount: BigDecimal) = SGD(amount)

    override fun toString() = "$currency$amount"
}
