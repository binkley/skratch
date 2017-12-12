package hm.binkley.labs.skratch.money

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class MoneyTest {
    @Test
    fun convertNicely() {
        val oneUSDollar = Money("USD", BigDecimal(1))
        val exchange = object : CurrencyExchange {
            override fun exchange(money: Money, to: String)
                    = if (to == "SGD") Money("SGD", BigDecimal(1.35))
            else throw UnsupportedOperationException()
        }

        assertEquals(Money("SGD", BigDecimal(1.35)),
                oneUSDollar at exchange to "SGD")
    }
}
