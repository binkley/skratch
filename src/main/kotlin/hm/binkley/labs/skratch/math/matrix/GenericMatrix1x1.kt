package hm.binkley.labs.skratch.math.matrix

class GenericMatrix1x1<N : GeneralNumber<N, Rational>>(
    a: N,
    private val elementCtor: (n: Long) -> N,
) :
    Matrix1x1<N, Rational, GenericMatrix1x1<N>>(a) {
    override fun elementCtor(n: Long) = elementCtor.invoke(n)

    override fun matrixCtor(a: N) =
        GenericMatrix1x1(a, elementCtor)
}
