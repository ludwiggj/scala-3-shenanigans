package com.ludwig.tagless.rinaudo.first_order.final_encoding

trait Exp04MultSym[A] {
  def mult(lhs: A, rhs: A): A
}

object Exp04MultSym {
  def mult[A](lhs: A, rhs: A)(using expMultSym: Exp04MultSym[A]): A = expMultSym.mult(lhs, rhs)
}