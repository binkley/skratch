package hm.binkley.labs.skratch.factories

import hm.binkley.labs.skratch.factories.Bar.Bars
import hm.binkley.labs.skratch.factories.Baz.Bazs
import hm.binkley.labs.skratch.factories.Foo.Foos
import hm.binkley.labs.skratch.factories.Grok.Groks
import hm.binkley.labs.skratch.factories.Ham.Hams
import hm.binkley.labs.skratch.factories.Spam.Spams

object Meta : System<Meta>("Meta")

class Foo private constructor(
    value: BigRational,
) : Measure<Meta, Length, Foos, Foo>(Foos, value) {
    companion object Foos : Units<Meta, Length, Foos, Foo>(
        Meta, "foo", 1.rat
    ) {
        override fun new(value: BigRational) = Foo(value)
    }
}

val BigRational.foo: Foo get() = Foos.new(this)
val Int.foo: Foo get() = rat.foo

class Bar private constructor(
    value: BigRational,
) : Measure<Meta, Length, Bars, Bar>(Bars, value) {
    companion object Bars : Units<Meta, Length, Bars, Bar>(
        Meta, "bar", 3.rat
    ) {
        override fun new(value: BigRational) = Bar(value)
    }
}

val BigRational.bar: Bar get() = Bars.new(this)
val Int.bar: Bar get() = rat.bar

class Baz private constructor(
    value: BigRational,
) : Measure<Meta, Length, Bazs, Baz>(Bazs, value) {
    companion object Bazs : Units<Meta, Length, Bazs, Baz>(
        Meta, "baz", 9.rat
    ) {
        override fun new(value: BigRational) = Baz(value)
    }
}

val BigRational.baz: Baz get() = Bazs.new(this)
val Int.baz: Baz get() = rat.baz

abstract class MetaWeights<
    U : MetaWeights<U, M>,
    M : MetaWeight<U, M>,
    >(
    name: String,
    basis: BigRational,
) : Units<Meta, Weight, U, M>(Meta, name, basis)

abstract class MetaWeight<
    U : MetaWeights<U, M>,
    M : MetaWeight<U, M>,
    >(
    unit: U,
    quantity: BigRational,
) : Measure<Meta, Weight, U, M>(unit, quantity)

fun Measure<Meta, Weight, *, *>.whatYaGot() = "spam, ${unit.name}, and spam"

class Spam private constructor(
    value: BigRational,
) : MetaWeight<Spams, Spam>(Spams, value) {
    companion object Spams : MetaWeights<Spams, Spam>(
        "spam", 1.rat
    ) {
        override fun new(value: BigRational) = Spam(value)
    }
}

val BigRational.spams: Spam get() = Spams.new(this)
val Int.spams: Spam get() = rat.spams

class Ham private constructor(
    value: BigRational,
) : MetaWeight<Hams, Ham>(Hams, value) {
    companion object Hams : MetaWeights<Hams, Ham>(
        "ham", 2.rat
    ) {
        override fun new(value: BigRational) = Ham(value)
    }
}

val BigRational.hams: Ham get() = Hams.new(this)
val Int.hams: Ham get() = rat.hams

object Martian : System<Martian>("Martian")
class Grok private constructor(
    value: BigRational,
) : Measure<Martian, Length, Groks, Grok>(Groks, value) {
    companion object Groks :
        Units<Martian, Length, Groks, Grok>(
            Martian, "grok", 9.rat
        ) {
        override fun new(value: BigRational) = Grok(value)
    }
}

val BigRational.groks: Grok get() = Groks.new(this)
val Int.groks: Grok get() = rat.groks
