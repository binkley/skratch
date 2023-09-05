<a href="./LICENSE.md">
<img src="./images/public-domain.svg" alt="Public Domain"
align="right" width="20%" height="auto"/>
</a>

# Skratch

[![build](https://github.com/binkley/skratch/workflows/build/badge.svg)](https://github.com/binkley/skratch/actions)
[![issues](https://img.shields.io/github/issues/binkley/skratch.svg)](https://github.com/binkley/skratch/issues/)
[![pull requests](https://img.shields.io/github/issues-pr/binkley/skratch.svg)](https://github.com/binkley/skratch/pulls)
[![vulnerabilities](https://snyk.io/test/github/binkley/skratch/badge.svg)](https://snyk.io/test/github/binkley/skratch)
[![license](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](http://unlicense.org/)

Kotlin Scratch Code

## Caveat emptor

This is an scratch, experimental repository.  It might use force push.

## Modules

Drop use of JVM modules. Repeating the same thing 3 times is annoying:

- In the build dependencies
- In `module-info.java`
- In source code imports

## BDD

Two styles of BDD in Kotlin: strings and functions.

- Functions: passes BDD text
  as [function names](./src/main/kotlin/hm/binkley/labs/skratch/bdd/funcs)
- Strings: passes BDD text
  as [strings](./src/main/kotlin/hm/binkley/labs/skratch/bdd/strings)

## Matrix

Modelling the Pauli spin matrices.

## Money

Classic Ward Cunningham example.

### Rethink

Using subtypes for specific currencies is fraught with peril. See:

- https://deque.blog/2017/08/17/a-study-of-4-money-class-designs-featuring-martin-fowler-kent-beck-and-ward-cunningham-implementations/
- https://deque.blog/2017/08/22/a-follow-up-of-the-study-of-4-money-class-designs-why-not-having-currencies-as-type-parameters/

## Building

Gradle was tried out, and found lacking (and painful). Returned to dull yet
reliable Maven.

### Gotchas

* Remember to add the `--enable-preview` flag to the JVM, including when
  running from inside an IDE
* Java heap space failures when tests fail
* `Rational` should extends `Complex` (with 0 imaginary portion), but
  `Complex` uses rational values for its portions. How to untangle?
