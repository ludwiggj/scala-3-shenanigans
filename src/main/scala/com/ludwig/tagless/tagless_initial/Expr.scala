package com.ludwig.tagless.tagless_initial

// https://rockthejvm.com/articles/tagless-final-in-scala

// The tags in the previous solution essentially added type information to the runtime.
// So let's remove the tags and let the compiler do the type-checking automatically.

// This is a tagless solution, because we’ve removed tags. It’s called tagless initial,
// because we work with intermediate data structures, not with the values we care about.
sealed trait Expr[A] // the tree

case class B(boolean: Boolean) extends Expr[Boolean]

case class Or(left: Expr[Boolean], right: Expr[Boolean]) extends Expr[Boolean]

case class And(left: Expr[Boolean], right: Expr[Boolean]) extends Expr[Boolean]

case class Not(expr: Expr[Boolean]) extends Expr[Boolean]

case class I(int: Int) extends Expr[Int]

case class Sum(left: Expr[Int], right: Expr[Int]) extends Expr[Int]

object Expr {
  def eval[A](expr: Expr[A]): A = expr match {
    case And(left, right) => eval(left) && eval(right)
    case B(boolean) => boolean
    case Not(expr) => !eval(expr)
    case Or(left, right) => eval(left) || eval(right)
    case I(i) => i
    case Sum(left, right) => eval(left) + eval(right)
  }
}