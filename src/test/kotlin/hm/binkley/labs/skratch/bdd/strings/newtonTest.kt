package hm.binkley.labs.skratch.bdd.strings

import hm.binkley.labs.skratch.bdd.Apple
import hm.binkley.labs.skratch.bdd.Newton
import hm.binkley.labs.skratch.bdd.strings.BDD.Companion.So
import hm.binkley.labs.skratch.bdd.strings.BDD.Companion.upon
import org.junit.jupiter.api.Test

class newtonTest {
    @Test
    fun shouldThink() {
        var apple: Apple? = null
        upon("an apple") {
            apple = Apple(Newton(thinking = false))
        }
        upon("it falls") {
            apple?.falls()
        }
        upon("Newton thinks") {
            assert(apple?.physicist?.thinking ?: false) {
                "Newton is sleeping"
            }
        }

        // TODO: Breaking these across lines confuses Kotlin compiler
        So GIVEN "an apple" WHEN "it falls" THEN "Newton thinks"
    }
}
