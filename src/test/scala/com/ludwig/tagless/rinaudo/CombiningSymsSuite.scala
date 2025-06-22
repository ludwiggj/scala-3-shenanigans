package com.ludwig.tagless.rinaudo

import com.ludwig.tagless.rinaudo.ExpSymWithSugar.*
import com.ludwig.tagless.rinaudo.ExpMultSym.*

class CombiningSymsSuite extends munit.FunSuite {
  // This is where all the work pays off: we can now easily compose ExpMultSym and ExpSym.
  // All we need to do is ask to have an implicit interpreter for both DSLs in scope.

  // exp can call mult because there’s an implicit ExpMultSym in scope.
  // exp can call lit & add because there’s an implicit ExpSym in scope.
  // Everything either takes or returns As, which means all the types line up.
  def exp[A: ExpSym : ExpMultSym]: A = {
    mult(add(lit(1), add(lit(-2), lit(4))), lit(2))
  }

  test("print expression") {
    given ExpSym[String] with {
      override def lit(i: Int): String = i.toString

      override def add(lhs: String, rhs: String): String = s"($lhs + $rhs)"
    }

    given ExpMultSym[String] with {
      override def mult(lhs: String, rhs: String): String = s"($lhs x $rhs)"
    }

    assertEquals(exp, "((1 + (-2 + 4)) x 2)")
  }

  test("evaluate expression") {
    given ExpSym[Int] with {
      override def lit(i: Int): Int = i

      override def add(lhs: Int, rhs: Int): Int = lhs + rhs
    }

    given ExpMultSym[Int] with {
      override def mult(lhs: Int, rhs: Int): Int = lhs * rhs
    }

    assertEquals(exp, 6)
  }
}
