package com.ludwig.tagless.rinaudo

// The core intuition of final encoding is that instead of using ADTs, we use functions to encode expressions.
// One way to think about this is that adding elements to an existing DSL can be seen as taking two DSLs, the
// old one and a new one with all the syntax we want to add, and compose them to form a third, richer one.
// When looking at things that way, it’s clear that ADTs aren’t going to be a great tool: they do not compose.
// Functions, on the other hand, famously do.
object ExpViaFunctions {
  def lit(value: Int) = value
  def add(lhs: Int, rhs: Int) = lhs + rhs

  // Adding the multiplication function does not break the existing functionality
  def mult(lhs: Int, rhs: Int) = lhs * rhs

  // This is known as a final encoding. One of its particularities is that we’re working directly with
  // interpreted values: look at lit, mult, … they’re all taking and returning Int. This makes things
  // easy, but is also a major flaw in the encoding.

  // The issue with this naive final encoding is that it works fine. but only if you’re not interested
  // in writing multiple interpreters. Since our functions immediately evaluate the corresponding
  // expression, we have a single interpreter: the evaluator.
  // We’re trying to solve the Expression Problem, which means writing multiple interpreters without
  // breaking anything, yet our encoding cannot write more than one, breaking changes or not.
  // We need to modify this approach in order to write multiple interpreters.
}
