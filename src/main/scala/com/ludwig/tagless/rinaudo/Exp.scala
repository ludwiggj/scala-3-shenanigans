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
}

object Interpreters {
  import Exp.*
  
  def print(exp: Exp): String = exp match {
    case Lit(value) => value.toString
    case Add(lhs, rhs) => s"(${print(lhs)} + ${print(rhs)})"
  }
  
  def eval(exp: Exp): Int = exp match {
    case Lit(value) => value
    case Add(lhs, rhs) => eval(lhs) + eval(rhs)
  }
}
