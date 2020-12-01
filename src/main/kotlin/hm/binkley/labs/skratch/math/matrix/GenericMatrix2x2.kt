package hm.binkley.labs.skratch.math.matrix

class GenericMatrix2x2<N : GeneralNumber<N, Rational>>(
    a: N,
    b: N,
    c: N,
    d: N,
    private val elementCtor: (n: Long) -> N,
) :
    Matrix2x2<N, Rational, GenericMatrix2x2<N>>(a, b, c, d) {
    override fun elementCtor(n: Long) = elementCtor.invoke(n)

    override fun matrixCtor(a: N, b: N, c: N, d: N) =
        GenericMatrix2x2(a, b, c, d, elementCtor)
}
