package com.ludwig.tagless.rinaudo

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

  // Adding Mult also results in compiler warnings on any pattern match on Exp
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

  def eval(exp: Exp): Int = exp match {
    case Lit(value) => value
    case Add(lhs, rhs) => eval(lhs) + eval(rhs)
  }
}
