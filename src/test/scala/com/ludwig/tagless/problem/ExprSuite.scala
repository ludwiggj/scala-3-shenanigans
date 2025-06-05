package com.ludwig.tagless.problem

import Expr.eval

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

  test("I") {
    assertEquals(eval(I(6)), 6)
  }

  test("Add integers") {
    assertEquals(eval(Sum(I(6), I(8))), 14)
  }

  test("Add integer and boolean - boom!") {
    intercept[java.lang.ClassCastException] {
      eval(Sum(B(true), I(8)))
    }
  }
}
