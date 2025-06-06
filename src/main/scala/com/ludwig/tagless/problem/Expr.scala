package com.ludwig.tagless.problem

// https://rockthejvm.com/articles/tagless-final-in-scala
sealed trait Expr // the tree

case class B(boolean: Boolean) extends Expr
case class Or(left: Expr, right: Expr) extends Expr
case class And(left: Expr, right: Expr) extends Expr
case class Not(expr: Expr) extends Expr
case class I(int: Int) extends Expr
case class Sum(left: Expr, right: Expr) extends Expr

object Expr {
  // This is now pretty ugly, and we lose type safety
  def eval(expr: Expr): Boolean | Int = expr match {
    case And(left, right) => eval(left).asInstanceOf[Boolean] && eval(right).asInstanceOf[Boolean]
    case B(boolean) => boolean
    case Not(expr) => !eval(expr).asInstanceOf[Boolean]
    case Or(left, right) => eval(left).asInstanceOf[Boolean] || eval(right).asInstanceOf[Boolean]
    case I(i) => i
    case Sum(left, right) => eval(left).asInstanceOf[Int] + eval(right).asInstanceOf[Int]
  }
}