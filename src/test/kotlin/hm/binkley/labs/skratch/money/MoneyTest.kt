package hm.binkley.labs.skratch.money

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.reflect.KClass

internal class MoneyTest {
    @Test
    fun `dollars and cents`() {
        assertThrows(ArithmeticException::class.java) {
            USD.valueOf("1.234")
        }
    }

    @Test
    fun `double your money`() {
        assertEquals(USD.valueOf(2), 2 * USD.valueOf(1))
    }

    @Test
    fun `convert nicely`() {
        val exchange = object : CurrencyExchange {
            @Suppress("UNCHECKED_CAST")
            override fun <M : Money<M>, O : Money<O>> exchange(
                money: M, to: KClass<O>,
            ) = when (to) {
                SGD::class -> SGD.valueOf("1.35") as O
                else -> fail("Unsupported exchange: $money -> $to")
            }
        }

        assertEquals(SGD.valueOf("1.35"),
            USD.valueOf(1) at exchange convertTo SGD::class)
    }

    @Test
    fun `nicely convert`() {
        val exchange = object : CurrencyExchange {
            @Suppress("UNCHECKED_CAST")
            override fun <M : Money<M>, O : Money<O>> exchange(
                money: M, to: KClass<O>,
            ) = when (to) {
                SGD::class -> SGD.valueOf("1.35") as O
                else -> fail("Unsupported exchange: $money -> $to")
            }
        }

        assertEquals(SGD.valueOf("1.35"),
            USD.valueOf(1) convertTo SGD::class at exchange)
    }

    @Test
    fun `no non-decimal dollars`() {
        assertThrows(ArithmeticException::class.java) {
            USD.valueOf(1) / 3
        }
    }

    @Test
    fun `custom currency`() {
        // Compiling *is* the test
        object : Currency {
            override fun format(amount: BigDecimal) = "MMG\$$amount"
            override fun toString() = "PP"
        }
    }
}
