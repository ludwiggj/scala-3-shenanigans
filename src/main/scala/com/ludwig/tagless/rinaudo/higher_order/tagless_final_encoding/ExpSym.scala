package com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding

trait ExpSym[F[_]] {
  def lit(i: Int): F[Int]
  def add(lhs: F[Int], rhs: F[Int]): F[Int]
  def eq(lhs: F[Int], rhs: F[Int]): F[Boolean]
}

// A little syntactic sugar to make this less unpleasant to work with:
object ExpSym {
  def lit[F[_]](i: Int)(using expSym: ExpSym[F]): F[Int] = expSym.lit(i)

  def add[F[_]](lhs: F[Int], rhs: F[Int])(using expSym: ExpSym[F]): F[Int] = expSym.add(lhs, rhs)

  def eq[F[_]](lhs: F[Int], rhs: F[Int])(using expSym: ExpSym[F]): F[Boolean] = expSym.eq(lhs, rhs)
}
