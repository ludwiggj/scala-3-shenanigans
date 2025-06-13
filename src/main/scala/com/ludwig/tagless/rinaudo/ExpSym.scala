package com.ludwig.tagless.rinaudo

// The intuition, here, is that since we’re working with functions, the only way to tell them how
// to interpret some data is to pass an interpreter as a parameter.

// Sym stands for symantic i.e. syntax and semantic: the type describes the syntax of our language,
// values of that type its semantics.

// The return values of the trait methods are the result of interpreting an expression. Therefore
// they should be of whatever type the interpreter returns i.e. the "interpreted type". This needs
// to be parametric: a pretty-printer evaluates to a String and an evaluator to an Int.

// For the type of the method arguments, remember that we’re working on a final encoding: one in
// which we’re manipulating the interpreted value rather than an intermediate representation. So,
// add will take the result of interpreting the nested expressions - and that is A as well.
trait ExpSym[A] {
  def lit(i: Int): A
  def add(lhs: A, rhs: A): A
}