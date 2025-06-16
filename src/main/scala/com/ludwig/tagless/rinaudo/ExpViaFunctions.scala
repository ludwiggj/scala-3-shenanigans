package com.ludwig.tagless.rinaudo

object ExpViaFunctions {
  def lit(value: Int) = value
  def add(lhs: Int, rhs: Int) = lhs + rhs

  // Adding the multiplication function does not break the existing functionality
  def mult(lhs: Int, rhs: Int) = lhs * rhs
}
