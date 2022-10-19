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
        USD.of(1) at exchange convertTo SGD::class shouldBe SGD.of("1.35")
    }

    @Test
    fun `nicely convert`() {
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
        USD.of(1) convertTo FunnyMoney::class at exchange shouldBe
            FunnyMoney.of("1101")
    }
}

private object Funny : Currency {
    override fun format(amount: BigDecimal) = "MMG\$$amount"
    override fun toString() = "PP"
}

private class FunnyMoney private constructor(amount: BigDecimal) :
    AbstractMoney<FunnyMoney>(Funny, amount.setScale(0)) {

    override fun with(amount: BigDecimal) = FunnyMoney(amount)

    companion object : MoneyFactory<FunnyMoney> {
        override fun of(amount: BigDecimal): FunnyMoney = FunnyMoney(amount)
    }
}

@Suppress("UNCHECKED_CAST")
private val exchange = object : CurrencyExchange {
    override fun <M : Money<M>, O : Money<O>> exchange(
        money: M,
        to: KClass<O>
    ) = when (to) {
        SGD::class -> SGD.of("1.35")
        FunnyMoney::class -> FunnyMoney.of(money.amount * 1101.toBigDecimal())
        else -> fail("Unsupported exchange: $money -> $to")
    } as O
}
