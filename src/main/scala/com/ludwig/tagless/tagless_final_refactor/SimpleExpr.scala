package com.ludwig.tagless.tagless_final_refactor

// Algebra is a typeclass that represents the ability to construct expressions,
// operands and operators, implemented in terms of an abstract type E 
sealed trait Algebra[E[_]] {
  def b(boolean: Boolean): E[Boolean]
  def not(boolean: E[Boolean]): E[Boolean]
  def or(left: E[Boolean], right: E[Boolean]): E[Boolean]
  def and(left: E[Boolean], right: E[Boolean]): E[Boolean]
  def i(int: Int): E[Int]
  def sum(left: E[Int], right: E[Int]): E[Int]
}

// This is an interpreter
case class SimpleExpr[A](value: A)

given simpleExprAlg: Algebra[SimpleExpr] with {
  override def b(boolean: Boolean): SimpleExpr[Boolean] = SimpleExpr(boolean)

  override def not(boolean: SimpleExpr[Boolean]): SimpleExpr[Boolean] = SimpleExpr(!boolean.value)

  override def or(left: SimpleExpr[Boolean], right: SimpleExpr[Boolean]): SimpleExpr[Boolean] =
    SimpleExpr(left.value || right.value)
    
  override def and(left: SimpleExpr[Boolean], right: SimpleExpr[Boolean]): SimpleExpr[Boolean] =
    SimpleExpr(left.value && right.value)

  override def i(int: Int): SimpleExpr[Int] = SimpleExpr(int)

  override def sum(left: SimpleExpr[Int], right: SimpleExpr[Int]) = SimpleExpr(left.value + right.value)
}
