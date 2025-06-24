package com.ludwig.tagless.rock_the_jvm.tagless_final

sealed trait Expr[A] {  // the tree
  val value: A          // the final value we care about
} 

case class B(boolean: Boolean) extends Expr[Boolean] {
  override val value: Boolean = boolean
}

case class Or(left: Expr[Boolean], right: Expr[Boolean]) extends Expr[Boolean] {
  override val value: Boolean = left.value || right.value
}

case class And(left: Expr[Boolean], right: Expr[Boolean]) extends Expr[Boolean] {
  override val value: Boolean = left.value && right.value
}

case class Not(expr: Expr[Boolean]) extends Expr[Boolean] {
  override val value: Boolean = !expr.value
}

case class I(int: Int) extends Expr[Int] {
  override val value: Int = int
}

case class Sum(left: Expr[Int], right: Expr[Int]) extends Expr[Int] {
  override val value: Int = left.value + right.value
}

object Expr {
  def eval[A](expr: Expr[A]): A = expr.value
}