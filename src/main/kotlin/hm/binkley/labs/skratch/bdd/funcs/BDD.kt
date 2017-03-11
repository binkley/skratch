package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.GIVEN
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.QED
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.THEN
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.WHEN
import hm.binkley.labs.skratch.bdd.funcs.BDD.Given
import hm.binkley.labs.skratch.bdd.funcs.BDD.Qed
import hm.binkley.labs.skratch.bdd.funcs.BDD.Then
import hm.binkley.labs.skratch.bdd.funcs.BDD.When

fun main(args: Array<String>) {
    println(GIVEN `an apple`
            WHEN `it falls`
            THEN `Newton thinks`
            QED)
}

infix fun Given.`an apple`(WHEN: When) = When("an apple")
infix fun When.`it falls`(THEN: Then) = Then(GIVEN, "it falls")
infix fun Then.`Newton thinks`(QED: Qed) = BDD(GIVEN, WHEN, "Newton thinks")

data class BDD(val GIVEN: String, val WHEN: String, val THEN: String) {
    companion object {
        val GIVEN = Given()
        val WHEN = When("")
        val THEN = Then("", "")
        val QED = Qed()
    }

    class Given
    data class When(val GIVEN: String)
    data class Then(val GIVEN: String, val WHEN: String)
    class Qed
}
