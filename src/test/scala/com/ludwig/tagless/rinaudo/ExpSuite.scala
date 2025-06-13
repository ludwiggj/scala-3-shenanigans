package com.ludwig.tagless.rinaudo

import Exp.*
import Interpreters.*

class ExpSuite extends munit.FunSuite {
  test("print expression") {
    val expr = Add(Lit(5), Add(Lit(1), Lit(-4)))
    assertEquals(print(expr), "(5 + (1 + -4))")
  }
  
  test("evaluate expression") {
    val expr = Add(Lit(5), Add(Lit(1), Lit(-4)))
    assertEquals(eval(expr), 2)
  }

  // Fails with scala.MatchError: Mult(Lit(1),Lit(-4))
  test("evaluate multiplication expression - boom!") {
    intercept[scala.MatchError] {
      val expr = Add(Lit(5), Mult(Lit(1), Lit(-4)))
      assertEquals(eval(expr), 2)
    }
  }
}
