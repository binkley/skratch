package hm.binkley.labs.skratch.factories

import hm.binkley.labs.skratch.factories.Bar.Bars
import hm.binkley.labs.skratch.factories.Baz.Bazs
import hm.binkley.labs.skratch.factories.Foo.Foos
import hm.binkley.labs.skratch.factories.Grok.Groks

object Meta : System<Meta>("Meta")
class Foo private constructor(
    value: BigRational,
) : Measure<Meta, Foos, Foo>(Foos, value) {
    companion object Foos : Units<Meta, Foos, Foo>(Meta, "foo", 1.rat) {
        override fun new(value: BigRational) = Foo(value)
    }
}

val BigRational.foo: Foo get() = Foos.new(this)
val Int.foo: Foo get() = rat.foo

class Bar private constructor(
    value: BigRational,
) : Measure<Meta, Bars, Bar>(Bars, value) {
    companion object Bars : Units<Meta, Bars, Bar>(Meta, "bar", 3.rat) {
        override fun new(value: BigRational) = Bar(value)
    }
}

val BigRational.bar: Bar get() = Bars.new(this)
val Int.bar: Bar get() = rat.bar

class Baz private constructor(
    value: BigRational,
) : Measure<Meta, Bazs, Baz>(Bazs, value) {
    companion object Bazs : Units<Meta, Bazs, Baz>(Meta, "baz", 9.rat) {
        override fun new(value: BigRational) = Baz(value)
    }
}

val BigRational.baz: Baz get() = Bazs.new(this)
val Int.baz: Baz get() = rat.baz

object Martian : System<Martian>("Martian")
class Grok private constructor(
    value: BigRational,
) : Measure<Martian, Groks, Grok>(Groks, value) {
    companion object Groks :
        Units<Martian, Groks, Grok>(Martian, "grok", 9.rat) {
        override fun new(value: BigRational) = Grok(value)
    }
}
