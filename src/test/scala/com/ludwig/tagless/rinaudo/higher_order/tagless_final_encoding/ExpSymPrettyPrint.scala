package com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding

object ExpSymPrettyPrint {
  // We know that the interpreted type for pretty-printing must be String. This is problematic, because
  // String is not parametric, and we need a type parameter to keep track of an expression’s normal
  // form. This is easily worked around with some relatively common type trickery:
  type Pretty[_] = String

  // With this, we can write our interpreter. Every expression keeps track of its normal form, so we can
  // only compose them when it makes sense, but it all ultimately evaluates to a String.
  given ExpSym[Pretty] with {
    def lit(i: Int): Pretty[Int] = i.toString

    def add(lhs: Pretty[Int], rhs: Pretty[Int]): Pretty[Int] = s"($lhs + $rhs)"

    def eq(lhs: Pretty[Int], rhs: Pretty[Int]): Pretty[Boolean] = s"($lhs = $rhs)"
  }
}
