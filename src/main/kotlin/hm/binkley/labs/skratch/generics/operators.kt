package hm.binkley.labs.skratch.generics

operator fun <
    S : System<S>,
    K : Kind,
    U : Units<S, K, U, M>,
    M : Measure<S, K, U, M>
    >
    M.plus(other: Measure<S, K, *, *>): M =
    unit.new(quantity + (other into unit).quantity)

operator fun <
    S : System<S>,
    K : Kind,
    V : Units<S, K, V, N>,
    N : Measure<S, K, V, N>
    >
    Measure<S, K, *, *>.div(other: V): N = into(other)
