package hm.binkley.labs.skratch.factories

import hm.binkley.labs.skratch.factories.Animal.Pasture
import hm.binkley.labs.skratch.factories.Plant.Soil
import java.util.Objects.hash

fun main() {
    val p1 = 1.plant
    val p2 = 2.plant
    val a3 = 3.animal

    println("== FACTORIES")
    println("P1 -> $p1")
    println("P2 -> $p2")
    println("A3 -> $a3")

    val thingSum1 = listOf(p2, a3).fold(p1) { acc, it -> acc + it }
    println("THING SUM #1 -> $thingSum1 ${listOf(p1, p2, a3)}")

    val a2 = p1 into Pasture
    println("A2 -> $a2")
    val thingSum2 = listOf(p2, a2).fold(p1) { acc, it -> acc + it }
    println("THING SUM #2 -> $thingSum2 ${listOf(p1, p2, a2)}")
}

operator fun <F : Factory<F, T>, T : Thing<F, T>>
T.plus(other: Thing<*, *>): T = factory.new(number + other.number)

infix fun <G : Factory<G, U>, U : Thing<G, U>>
Thing<*, *>.into(other: G): U = other.new(number + 1)

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
    override fun toString() = "$factory=$number"
}

class Plant private constructor(
    number: Int,
) : Thing<Soil, Plant>(Soil, number) {
    companion object Soil : Factory<Soil, Plant>("plant") {
        override fun new(number: Int) = Plant(number)
    }
}

val Int.plant: Plant get() = Soil.new(this)

class Animal private constructor(
    number: Int,
) : Thing<Pasture, Animal>(Pasture, number) {
    companion object Pasture : Factory<Pasture, Animal>("animal") {
        override fun new(number: Int) = Animal(number)
    }
}

val Int.animal: Animal get() = Pasture.new(this)
