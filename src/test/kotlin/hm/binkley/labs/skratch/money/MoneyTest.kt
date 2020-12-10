package hm.binkley.labs.skratch.money

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.reflect.KClass

internal class MoneyTest {
    @Test
    fun dollarsAndCents() {
        assertThrows(ArithmeticException::class.java) {
            USD(BigDecimal("1.234"))
        }
    }

    @Test
    fun doubleYourMoney() {
        assertEquals(USD(2), 2 * USD(1))
    }

    @Test
    fun convertNicely() {
        val exchange = object : CurrencyExchange {
            @Suppress("UNCHECKED_CAST")
            override fun <M : Money<M>, O : Money<O>> exchange(
                money: M, to: KClass<O>,
            ) = when (to) {
                SGD::class -> SGD(BigDecimal("1.35")) as O
                else -> fail("Unsupported exchange: $money -> $to")
            }
        }

        assertEquals(SGD("1.35"), USD(1) at exchange convertTo SGD::class)
    }

    @Test
    fun nicelyConvert() {
        val exchange = object : CurrencyExchange {
            @Suppress("UNCHECKED_CAST")
            override fun <M : Money<M>, O : Money<O>> exchange(
                money: M, to: KClass<O>,
            ) = when (to) {
                SGD::class -> SGD(BigDecimal("1.35")) as O
                else -> fail("Unsupported exchange: $money -> $to")
            }
        }

        assertEquals(SGD("1.35"), USD(1) convertTo SGD::class at exchange)
    }

    @Test
    fun noFractionalMoney() {
        assertThrows(ArithmeticException::class.java) {
            USD(1) / 3
        }
    }
}
