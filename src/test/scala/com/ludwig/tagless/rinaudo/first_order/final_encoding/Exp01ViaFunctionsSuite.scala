package com.ludwig.tagless.rinaudo.first_order.final_encoding

import com.ludwig.tagless.rinaudo.first_order.final_encoding.Exp01ViaFunctions.*

class Exp01ViaFunctionsSuite extends munit.FunSuite {
  test("evaluate expression") {
    val expr = add(lit(5), add(lit(1), lit(-4)))
    assertEquals(expr, 2)
  }

  // Fails with scala.MatchError: Mult(Lit(1),Lit(-4))
  test("evaluate multiplication expression") {
    val expr = add(lit(5), mult(lit(1), lit(-4)))
    assertEquals(expr, 1)
  }

//  test("print expression") {
//    val expr = add(lit(5), add(lit(1), lit(-4)))
//    assertEquals(expr, "(5 + (1 + -4))")
//  }
}
