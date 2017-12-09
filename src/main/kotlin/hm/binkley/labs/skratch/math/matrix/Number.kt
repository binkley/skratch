package hm.binkley.labs.skratch.math.matrix

interface Number<T, Norm>
    : Additative<T>, Multiplicative<T>, Normative<T, Norm>
        where T : Number<T, Norm>, Norm : Number<Norm, Norm> {
    val conj: T
}
