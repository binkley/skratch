package hm.binkley.labs.skratch.bdd.strings

import hm.binkley.labs.skratch.bdd.Apple
import hm.binkley.labs.skratch.bdd.Newton
import hm.binkley.labs.skratch.bdd.strings.BDD.Companion.So
import hm.binkley.labs.skratch.bdd.strings.BDD.Companion.upon
import org.junit.jupiter.api.Test

class newtonTest {
    private lateinit var apple: Apple

    @Test
    fun shouldThink() {
        upon(an_apple) {
            apple = Apple(Newton(thinking = false))
        }
        upon(it_falls) {
            apple.falls()
        }
        upon(Newton_thinks) {
            assert(apple.physicist.thinking) {
                "Newton is sleeping"
            }
        }

        // TODO: Breaking these across lines confuses Kotlin compiler
        So GIVEN an_apple WHEN it_falls THEN Newton_thinks
    }

    companion object {
        private const val an_apple = "an apple"
        private const val it_falls = "it falls"
        private const val Newton_thinks = "Newton thinks"
    }
}
