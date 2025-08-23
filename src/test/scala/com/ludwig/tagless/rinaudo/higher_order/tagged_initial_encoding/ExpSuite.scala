package com.ludwig.tagless.rinaudo.higher_order.tagged_initial_encoding

import com.ludwig.tagless.rinaudo.higher_order.tagged_initial_encoding.Exp.*
import com.ludwig.tagless.rinaudo.higher_order.tagged_initial_encoding.TypeError

class ExpSuite extends munit.FunSuite {
  test("evaluate add expression") {
    val expr = Add(Lit(5), Add(Lit(1), Lit(-4)))
    assertEquals(eval(expr), 2)
  }
  
  test("evaluating add expression with mixed types generates type error") {
    interceptMessage[TypeError]("Add") {
      eval(Add(Eq(Lit(5), Lit(6)), Add(Lit(1), Lit(-4))))
    }
  }

  test("evaluate equals expression - equals") {
    assertEquals(eval(Eq(Lit(5), Lit(6))), false)
    assertEquals(eval(Eq(Lit(4), Lit(4))), true)
    assertEquals(eval(Eq(Eq(Lit(40), Lit(40)), Eq(Lit(14), Lit(14)))), true)
    assertEquals(eval(Eq(Eq(Lit(4), Lit(4)), Eq(Lit(4), Lit(1)))), false)
  }

  test("evaluating eq expression with mixed types generates type error") {
    interceptMessage[TypeError]("Eq") {
      eval(Eq(Lit(4), Eq(Lit(4), Lit(0))))
    }
  }
}
