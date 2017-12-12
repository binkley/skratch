package hm.binkley.labs.skratch.money

import hm.binkley.labs.skratch.money.Money.Companion.one
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MoneyTest {
    private val oneUSDollar = one("USD")

    @Test
    fun doubleYourMoney() {
        assertEquals(Money("USD", 2), 2 * oneUSDollar)
    }

    @Test
    fun convertNicely() {
        val exchange = object : CurrencyExchange {
            override fun exchange(money: Money, to: String)
                    = if (to == "SGD") Money("SGD", 1.35)
            else throw UnsupportedOperationException()
        }

        assertEquals(Money("SGD", 1.35),
                oneUSDollar at exchange convertTo "SGD")
    }
}
