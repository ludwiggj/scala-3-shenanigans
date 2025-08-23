package com.ludwig.tagless.rinaudo.first_order.final_encoding

object Exp01ViaFunctions {
  def lit(value: Int) = value
  def add(lhs: Int, rhs: Int) = lhs + rhs

  // Adding the multiplication function does not break the existing functionality
  def mult(lhs: Int, rhs: Int) = lhs * rhs
}
