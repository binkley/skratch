package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.DefaultMutableLayers.Companion.defaultMutableLayers

fun main() {
    println("== USING DEFAULT TYPES")

    val c = defaultMutableLayers<String, Number>("C")
    c.edit {
        this["ALICE"] = latestOfRule(0)
        this["BOB"] = rule<Double>("Sum[Int]") { _, values, _ ->
            values.sum()
        }
    }
    c.commitAndNext()
    c.edit {
        this["ALICE"] = Value(3)
    }
    val a = c.commitAndNext()
    a.edit {
        this["BOB"] = Value(4.0)
    }

    println(c)

    println()
    println("== USING CUSTOM TYPES")

    val d =
        DefaultMutableLayers<String, Number, DefaultMutableLayer<String, Number, *>>(
            "D"
        ) { DefaultMutableLayer() }
    d.edit {
        this["CAROL"] = constantRule(2)
    }

    class Bob : DefaultMutableLayer<String, Number, Bob>() {
        fun foo() = println("I AM FOCUTUS OF BOB")
    }

    val b = d.commitAndNext { Bob() }
    b.foo()

    println(d)

    println()
    println("== USING COMPLEX RULES")

    b.edit {
        this["CAROL"] = 17.toValue()
    }
    d.commitAndNext()
    d.edit {
        this["CAROL"] = 19.toValue()
    }
    d.commitAndNext()
    d.edit {
        this["CAROL"] = rule<Int>("Product[Int]") { _, values, _ ->
            values.fold(1) { a, b -> a * b }
        }
    }

    println(d)

    println()
    println("== WHAT-IF SCENARIOS")

    println(d.whatIf(scenario = mapOf("CAROL" to (-1).toValue())))

    println()
    println("== ORIGINAL WITHOUT WHAT-IF")

    d.edit {
        this["DAVE"] = rule<Int>("Halve[Int](other=CAROL)") { _, _, _ ->
            getOtherValueAs<Int>("CAROL") / 2
        }
    }

    println(d)

    println()
    println("== MORE COMPLEX RULES")

    d.edit {
        this["DAVE"] = rule<Int>("Count of non-DAVE keys") { _, _, view ->
            view.keys.size
        }
        this["EVE"] = constantRule(31)
    }

    println(d)
}
