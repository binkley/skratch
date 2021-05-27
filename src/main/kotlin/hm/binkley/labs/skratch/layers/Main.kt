package hm.binkley.labs.skratch.layers

fun main() {
    val c = DefaultMutableLayers.defaultMutableLayers<String, Number>("C")
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
    b.edit {
        this["CAROL"] = 17.toValue()
    }
    d.commitAndNext()
    d.edit {
        this["CAROL"] = 19.toValue()
    }
    d.commitAndNext()
    d.edit {
        d.edit {
            this["CAROL"] = rule<Int>("Product[Int]") { _, values, _ ->
                values.fold(1) { a, b -> a * b }
            }
        }
    }

    println(d)

    println(d.whatIf(scenario = mapOf("CAROL" to (-1).toValue())))
}
