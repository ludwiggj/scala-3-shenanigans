package com.ludwig.tagless

import com.ludwig.tagless.Expr.eval

class ExprSuite extends munit.FunSuite {
  test("B") {
    assertEquals(eval(B(false)), false)
    assertEquals(eval(B(true)), true)
  }

  test("Not") {
    assertEquals(eval(Not(B(false))), true)
    assertEquals(eval(Not(B(true))), false)
  }

  test("And") {
    assertEquals(eval(And(B(false), B(false))), false)
    assertEquals(eval(And(B(false), B(true))), false)
    assertEquals(eval(And(B(true), B(false))), false)
    assertEquals(eval(And(B(true), B(true))), true)
  }

  test("Or") {
    assertEquals(eval(Or(B(false), B(false))), false)
    assertEquals(eval(Or(B(false), B(true))), true)
    assertEquals(eval(Or(B(true), B(false))), true)
    assertEquals(eval(Or(B(true), B(true))), true)
  }

  test("expression combination") {
    val expr = Not(Or(And(B(true), B(false)), B(true)))
    assertEquals(eval(expr), false)
  }
}
