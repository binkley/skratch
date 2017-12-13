# Skratch

Kotlin Scratch Code

## BDD

Two styles of BDD in Kotlin: strings and functions.

- Strings: passes BDD text as [strings](src/main/kotlin/hm/binkley/skratch/bdd/strings)
- Functions: passes BDD text as [function names](src/main/kotlin/hm/binkley/skratch/bdd/funcs)

## Matrix

Modelling the Pauli spin matrices.

## Building

Gradle was tried out, and found lacking (and painful).  Returned to dull yet
reliable Maven.

### Gotchas

* Java heap space failures when tests fail
* `Rational` should extends `Complex` (with 0 imaginary portion), but 
  `Complex` uses rational values for its portions.  How to untangle? 
