package com.ludwig.tagless.rinaudo

// https://nrinaudo.github.io/articles/tagless_final.html

// We’re trying to model a very simple Domain Specific Language that allows us to express:
// - integer literals.
// - addition of two integers e.g. 1 + (2 + 4).
//
// We must also be able to provide multiple interpreters:
// - pretty-printing: take an expression and make it human readable.
// - evaluation: compute the result of an expression.

// naive implementation

// Encoding our DSL as a straightforward ADT is known as an initial encoding
enum Exp {
  case Lit(value: Int)
  case Add(lhs: Exp, rhs: Exp)
  // adding new elements e.g. multiplication to our DSL breaks existing interpreters
  case Mult(lhs: Exp, rhs: Exp)
}

object Interpreters {

  import Exp.*

  // So compiler issues following on any pattern match
  // -- [E029] Pattern Match Exhaustivity Warning:
  // [warn] 26 |  def print(exp: Exp): String = exp match {
  // [warn]    |                                ^^^
  // [warn]    |match may not be exhaustive.
  // [warn]    |
  // [warn]    |It would fail on pattern case: com.ludwig.tagless.rinaudo.Exp.Mult(_, _)
  // [warn]    |
  def print(exp: Exp): String = exp match {
    case Lit(value) => value.toString
    case Add(lhs, rhs) => s"(${print(lhs)} + ${print(rhs)})"
  }

  // Existing interpreters do not know about Mult and cannot possibly handle it - it didn’t exist when they
  // were written. Every single one of them will break one way or another when presented with expressions
  // containing multiplications (see new test).

  // This is known as the Expression Problem: finding a statically checked encoding for a DSL that allows
  // us to add both syntax (such as multiplication) and interpreters (such as pretty-printing) without
  // breaking anything.
  def eval(exp: Exp): Int = exp match {
    case Lit(value) => value
    case Add(lhs, rhs) => eval(lhs) + eval(rhs)
  }
}
