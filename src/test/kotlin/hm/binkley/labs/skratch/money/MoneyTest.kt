package hm.binkley.labs.skratch.money

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.reflect.KClass

internal class MoneyTest {
    @Test
    fun `dollars and cents`() {
        shouldThrow<ArithmeticException> {
            USD.of("1.234")
        }
    }

    @Test
    fun `double your money`() =
        2 * USD.of(1) shouldBe USD.of(2)

    @Test
    fun `convert nicely`() {
        val exchange = object : CurrencyExchange {
            @Suppress("UNCHECKED_CAST")
            override fun <M : Money<M>, O : Money<O>> exchange(
                money: M, to: KClass<O>,
            ) = when (to) {
                SGD::class -> SGD.of("1.35") as O
                else -> fail("Unsupported exchange: $money -> $to")
            }
        }

        USD.of(1) at exchange convertTo SGD::class shouldBe SGD.of("1.35")
    }

    @Test
    fun `nicely convert`() {
        val exchange = object : CurrencyExchange {
            @Suppress("UNCHECKED_CAST")
            override fun <M : Money<M>, O : Money<O>> exchange(
                money: M, to: KClass<O>,
            ) = when (to) {
                SGD::class -> SGD.of("1.35") as O
                else -> fail("Unsupported exchange: $money -> $to")
            }
        }

        USD.of(1) convertTo SGD::class at exchange shouldBe SGD.of("1.35")
    }

    @Test
    fun `no non-decimal dollars`() {
        shouldThrow<ArithmeticException> {
            USD.of(1) / 3
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
