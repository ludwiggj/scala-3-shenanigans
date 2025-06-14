package com.ludwig.tagless.rinaudo

import ExpSymWithSugar.*

class ExpSymWithSugarSuite extends munit.FunSuite {
//  def exp[A](sym: ExpSym[A]): A = {
//    import sym.*
//    add(lit(1), add(lit(-2), lit(4)))
//  }

  // no longer pass sym in as a parameter to exp, so don't have to import sym.*
  def exp[A: ExpSym]: A = {
    add(lit(1), add(lit(-2), lit(4)))
  }

  test("print expression") {
    // Let’s do pretty-printing - declare a given
    given ExpSym[String] with {
      override def lit(i: Int): String = i.toString
      override def add(lhs: String, rhs: String): String = s"($lhs + $rhs)"
    }

    // this syntax simplifies as well
//    assertEquals(exp(print), "(1 + (-2 + 4))")
    assertEquals(exp, "(1 + (-2 + 4))")
  }
  
  test("evaluate expression") {
    // Let’s evaluate!
    given ExpSym[Int] with {
      override def lit(i: Int): Int = i
      override def add(lhs: Int, rhs: Int): Int = lhs + rhs
    }

    assertEquals(exp, 3)
  }
}
