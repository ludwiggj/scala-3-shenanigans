package com.ludwig.tagless.rinaudo.first_order.final_encoding

// Syntactic sugar
// expSym is now being treated as a type class, so now just call the methods directly on the type class
object Exp03SymWithSugar {
  def lit[A](i: Int)(using expSym: Exp02Sym[A]): A = expSym.lit(i)
  def add[A](lhs: A, rhs: A)(using expSym: Exp02Sym[A]): A = expSym.add(lhs, rhs)
}