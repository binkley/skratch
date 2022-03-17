package hm.binkley.labs.skratch.factories

import hm.binkley.labs.skratch.factories.Bar.Bars
import hm.binkley.labs.skratch.factories.Baz.Bazs
import hm.binkley.labs.skratch.factories.Foo.Foos
import hm.binkley.labs.skratch.factories.Ham.Hams
import hm.binkley.labs.skratch.factories.Spam.Spams

fun main() {
    val foo1 = 1.foo
    val foo2 = 2.foo
    val bar3 = 3.bar

    println("== FACTORIES")
    println(foo1)
    println(foo2)
    println(bar3)
    println(bar3 into Foos)

    val list1 = listOf(foo1, foo2, bar3)
    val sum1 = list1.fold(0.foo) { acc, it -> acc + it }
    println("SUM #1 -> $sum1 $list1")

    val bar2 = foo1 into Bars
    println(bar2)
    val list2 = listOf(foo1, foo2, bar2)
    val sum2 = list2.fold(0.foo) { acc, it -> acc + it }
    println("SUM #2 -> $sum2 $list2")

    val foo14 = 14.foo
    println(foo14)
    val wholes = foo14.into(Foos, Bazs, Bars)
    val sum3 = wholes.fold(0.foo) { acc, it -> acc + it }
    println("SUM #3 -> $sum3 $wholes")

    val foo14_2 = (29 over 2).foo
    println(foo14_2)
    val remainders = foo14_2.into(Foos, Bazs, Bars)
    val sum4 = remainders.fold(0.foo) { acc, it -> acc + it }
    println("SUM #4 -> $sum4 $remainders")

    val foo7 = 14.foo
    val spam1 = 2.spams
    val grok1 = 1.groks
    println(foo7)
    println(spam1)
    println(grok1)
    // Correctly does not compile -- Spams is in the wrong Kind
    // foo7 into Spams
    // foo7.into(Bars, Spams)
    // val x = foo7 + spam1
    // Correctly does not compile -- Groks is in the wrong System
    // foo7 into Groks
    // foo7.into(Bazs, Bars, Groks)
    // val x = foo7 + grok1

    println(Foos < Bars)
    // Correctly does not compile -- Groks is in the wrong System
    // println(Foos < Groks)
    println(1.foo < 2.foo)
    // Correctly does not compile -- Spams is in the wrong Kind
    // println(1.foo < 1.spams)
    println(1.foo < 1.bar)
    // Correctly does not compile -- Groks is in the wrong System
    // println(1.foo < 1.groks)

    val spam4 = 5.spams
    println(spam4.whatYaGot())
    val menuItems = spam4.into(Spams, Hams)
    println(menuItems)
    menuItems.forEach { println(it.whatYaGot()) }

    println(foo1 + (bar2 into Foos))
}
