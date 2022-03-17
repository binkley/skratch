package hm.binkley.labs.skratch.factories

operator fun <
    S : System<S>,
    K : Kind,
    U : Units<S, K, U, M>,
    M : Measure<S, K, U, M>,
    >
M.plus(other: Measure<S, K, *, *>): M =
    unit.new(quantity + (other into unit).quantity)
