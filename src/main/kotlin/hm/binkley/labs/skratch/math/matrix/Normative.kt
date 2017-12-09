package hm.binkley.labs.skratch.math.matrix

interface Normative<T, Norm>
        where T : Normative<T, Norm>, Norm : Normative<Norm, Norm> {
    val abs: Norm
    val sqnorm: Norm
}
