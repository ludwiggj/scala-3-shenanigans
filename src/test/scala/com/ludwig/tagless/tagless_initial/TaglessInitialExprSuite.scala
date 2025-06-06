package com.ludwig.tagless.tagless_initial

import Expr.eval

class TaglessInitialExprSuite extends munit.FunSuite {
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

  // This no longer compiles - yay for type safety!
//  test("Add integer and boolean - boom!") {
//    val expectedErrorMessge = "assertion failed: attempting to evaluate Sum expression with improperly typed operands (bool, int)"
//    interceptMessage[java.lang.AssertionError](expectedErrorMessge) {
//      eval(Sum(B(true), I(8)))
//    }
//  }
}
