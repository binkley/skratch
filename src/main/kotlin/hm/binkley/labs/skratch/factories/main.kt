package hm.binkley.labs.skratch.factories

import hm.binkley.labs.skratch.factories.Animal.Pasture
import hm.binkley.labs.skratch.factories.Plant.Soil
import java.util.Objects.hash

fun main() {
    val p1 = Soil.new(0)
    val p2 = +p1
    val a3 = Pasture.new(2)

    println("== FACTORIES")
    println("P1 -> $p1")
    println("P2 -> $p2")
    println("A3 -> $a3")

    val numSum = listOf(p1, p2, a3).fold(0) { acc, it -> acc + it.number }
    println("NUM SUM -> $numSum")
    val thingSum = listOf(p2, a3).fold(p1) { acc, it -> acc + it }
    println("THING SUM -> $thingSum")
}

operator fun <F : Factory<F, T>, T : Thing<F, T>>
T.unaryPlus(): T = factory.new(number + 1)

operator fun <F : Factory<F, T>, T : Thing<F, T>>
T.plus(other: Thing<*, *>): T = factory.new(number + other.number)

abstract class Factory<F : Factory<F, T>, T : Thing<F, T>>(
    val name: String,
) {
    abstract fun new(number: Int): T
    override fun toString() = name
}

abstract class Thing<F : Factory<F, T>, T : Thing<F, T>>(
    val factory: F,
    val number: Int,
) {
    override fun equals(other: Any?) = this === other ||
            other is Thing<*, *> &&
            factory == other.factory &&
            number == other.number

    override fun hashCode() = hash(factory, number)
    override fun toString() = "$factory #$number"
}

class Plant private constructor(
    number: Int,
) : Thing<Soil, Plant>(Soil, number) {
    companion object Soil : Factory<Soil, Plant>("plant") {
        override fun new(number: Int) = Plant(number)
    }
}

class Animal private constructor(
    number: Int,
) : Thing<Pasture, Animal>(Pasture, number) {
    companion object Pasture : Factory<Pasture, Animal>("animal") {
        override fun new(number: Int) = Animal(number)
    }
}
