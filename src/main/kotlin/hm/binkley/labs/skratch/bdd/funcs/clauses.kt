@file:Suppress("FunctionName", "UNUSED_PARAMETER")

package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.Apple
import hm.binkley.labs.skratch.bdd.Newton
import hm.binkley.labs.skratch.bdd.funcs.QED.Given
import hm.binkley.labs.skratch.bdd.funcs.QED.Scenario
import hm.binkley.labs.skratch.bdd.funcs.QED.Then
import hm.binkley.labs.skratch.bdd.funcs.QED.When

internal lateinit var apple: Apple

// The scenario extension functions need to be:
// 1. Non-local functions
// 2. Infix
// This lets the framework handle G-W-T correctly, and spells nicely

internal infix fun Scenario.`A revolution begins`(GIVEN: Given) = act { }
internal infix fun Scenario.`A revolution is missed`(GIVEN: Given) = act { }

internal infix fun Given.`an apple`(WHEN: When) = act {
    apple = Apple(Newton(thinking = false))
}

internal infix fun When.`it falls`(THEN: Then) = act {
    apple.falls()
}

internal infix fun Then.`Newton thinks`(QED: QED) = act {
    assert(apple.physicist.thinking) {
        "@|bold,red Newton is still sleeping|@".asAnsi()
    }
}

internal infix fun Then.`Newton sleeps`(QED: QED) = act {
    assert(!apple.physicist.thinking) {
        "@|bold,red Newton is still thinking|@".asAnsi()
    }
}
