package com.ludwig.tagless

// https://rockthejvm.com/articles/tagless-final-in-scala
sealed trait Expr // the tree

case class B(boolean: Boolean) extends Expr
case class Or(left: Expr, right: Expr) extends Expr
case class And(left: Expr, right: Expr) extends Expr
case class Not(expr: Expr) extends Expr

object Expr {
  def eval(expr: Expr): Boolean = expr match {
    case And(left, right) => eval(left) && eval(right)
    case B(boolean) => boolean
    case Not(expr) => !eval(expr)
    case Or(left, right) => eval(left) || eval(right)
  }
}