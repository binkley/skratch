package hm.binkley.labs.skratch.money

import java.math.BigDecimal

interface Currency {
    fun format(amount: BigDecimal): String

    /** Force implementations rather than the default JDK ones. */
    override fun toString(): String
}

enum class KnownCurrency : Currency {
    SGD {
        override fun format(amount: BigDecimal) = "SG\$$amount"
    },
    USD {
        override fun format(amount: BigDecimal) = "\$$amount"
    }
}
