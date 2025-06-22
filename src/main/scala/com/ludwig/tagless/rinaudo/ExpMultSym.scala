package com.ludwig.tagless.rinaudo

trait ExpMultSym[A] {
  def mult(lhs: A, rhs: A): A
}

object ExpMultSym {
  def mult[A](lhs: A, rhs: A)(using expMultSym: ExpMultSym[A]): A = expMultSym.mult(lhs, rhs)
}