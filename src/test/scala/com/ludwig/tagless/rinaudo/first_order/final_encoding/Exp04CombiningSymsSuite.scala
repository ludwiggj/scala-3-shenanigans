package com.ludwig.tagless.rinaudo.first_order.final_encoding

import com.ludwig.tagless.rinaudo.first_order.final_encoding.Exp02Sym
import com.ludwig.tagless.rinaudo.first_order.final_encoding.Exp03SymWithSugar.*
import com.ludwig.tagless.rinaudo.first_order.final_encoding.Exp04MultSym.*

class Exp04CombiningSymsSuite extends munit.FunSuite {
  // This is where all the work pays off: we can now easily compose ExpMultSym and ExpSym.
  // All we need to do is ask to have an implicit interpreter for both DSLs in scope.

  // exp can call mult because there’s an implicit ExpMultSym in scope.
  // exp can call lit & add because there’s an implicit ExpSym in scope.
  // Everything either takes or returns As, which means all the types line up.
  def exp[A: Exp02Sym : Exp04MultSym]: A = {
    mult(add(lit(1), add(lit(-2), lit(4))), lit(2))
  }

  test("print expression") {
    given Exp02Sym[String] with {
      override def lit(i: Int): String = i.toString

      override def add(lhs: String, rhs: String): String = s"($lhs + $rhs)"
    }

    given Exp04MultSym[String] with {
      override def mult(lhs: String, rhs: String): String = s"($lhs x $rhs)"
    }

    assertEquals(exp, "((1 + (-2 + 4)) x 2)")
  }

  test("evaluate expression") {
    given Exp02Sym[Int] with {
      override def lit(i: Int): Int = i

      override def add(lhs: Int, rhs: Int): Int = lhs + rhs
    }

    given Exp04MultSym[Int] with {
      override def mult(lhs: Int, rhs: Int): Int = lhs * rhs
    }

    assertEquals(exp, 6)
  }
}
