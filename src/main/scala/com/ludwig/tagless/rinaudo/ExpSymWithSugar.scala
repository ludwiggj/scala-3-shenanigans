package com.ludwig.tagless.rinaudo

// Syntactic sugar

//trait ExpSym[A] {
//  def lit(i: Int): A
//  def add(lhs: A, rhs: A): A
//}

// expSym is now being treated as a type class, so now just call the methods directly on the type class
object ExpSymWithSugar {
  def lit[A](i: Int)(using expSym: ExpSym[A]): A = expSym.lit(i)
  def add[A](lhs: A, rhs: A)(using expSym: ExpSym[A]): A = expSym.add(lhs, rhs)
}