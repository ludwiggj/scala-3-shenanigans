package com.ludwig.tagless.rinaudo.higher_order.tagless_initial_encoding

enum Exp[A] {
  case Lit(value: Int) extends Exp[Int]
  case Add(lhs: Exp[Int], rhs: Exp[Int]) extends Exp[Int]
  case Eq(lhs: Exp[Int], rhs: Exp[Int]) extends Exp[Boolean]
}

object Exp {
  import Exp.*

  def eval[A](exp: Exp[A]): A = exp match {
    case Lit(value)    => value
    case Add(lhs, rhs) => eval(lhs) + eval(rhs)
    case Eq(lhs, rhs)  => eval(lhs) == eval(rhs)
  }
}