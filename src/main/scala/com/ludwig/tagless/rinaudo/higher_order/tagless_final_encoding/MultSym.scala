package com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding

trait MultSym[F[_]] {
  def mult(lhs: F[Int], rhs: F[Int]): F[Int]
}

// A little syntactic sugar to make this less unpleasant to work with:
object MultSym {
  def mult[F[_]](lhs: F[Int], rhs: F[Int])(using multSym: MultSym[F]): F[Int] = multSym.mult(lhs, rhs)
}
