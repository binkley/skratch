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

infix fun Given.`an apple`(WHEN: When) = When()
infix fun When.`it falls`(THEN: Then) = Then(GIVEN)
infix fun Then.`Newton thinks`(QED: Qed) = BDD(GIVEN, WHEN)

inline fun whoami() = Throwable().stackTrace[1].methodName

data class BDD(val GIVEN: String, val WHEN: String, val THEN: String = whoami()) {
    companion object {
        val GIVEN = Given()
        val WHEN = When()
        val THEN = Then("")
        val QED = Qed()
    }

    class Given
    class When(val GIVEN: String = whoami())
    class Then(val GIVEN: String, val WHEN: String = whoami())
    class Qed
}
