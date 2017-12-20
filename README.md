# Skratch

Kotlin Scratch Code

## BDD

Two styles of BDD in Kotlin: strings and functions.

- Strings: passes BDD text as [strings](src/main/kotlin/hm/binkley/skratch/bdd/strings)
- Functions: passes BDD text as [function names](src/main/kotlin/hm/binkley/skratch/bdd/funcs)

## Matrix

Modelling the Pauli spin matrices.

## Money

Classic Ward Cunningham example.

### Rethink

Using subtypes for specific currencies is fraught with peril.  See:

- https://deque.blog/2017/08/17/a-study-of-4-money-class-designs-featuring-martin-fowler-kent-beck-and-ward-cunningham-implementations/
- https://deque.blog/2017/08/22/a-follow-up-of-the-study-of-4-money-class-designs-why-not-having-currencies-as-type-parameters/

## Building

Gradle was tried out, and found lacking (and painful).  Returned to dull yet
reliable Maven.

### Gotchas

* Java heap space failures when tests fail
* `Rational` should extends `Complex` (with 0 imaginary portion), but 
  `Complex` uses rational values for its portions.  How to untangle? 
