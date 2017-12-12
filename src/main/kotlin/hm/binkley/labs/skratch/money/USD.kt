package hm.binkley.labs.skratch.money

import java.math.BigDecimal

class USD(amount: BigDecimal) : Money<USD>("USD", amount) {
    constructor(amount: Int) : this(BigDecimal(amount))
    constructor(amount: Long) : this(BigDecimal(amount))
    constructor(amount: Double) : this(BigDecimal(amount))

    override fun with(amount: BigDecimal) = USD(amount)
}
