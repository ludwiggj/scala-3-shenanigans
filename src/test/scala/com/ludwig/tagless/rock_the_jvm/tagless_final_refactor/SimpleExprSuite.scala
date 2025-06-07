package com.ludwig.tagless.rock_the_jvm.tagless_final_refactor

import com.ludwig.tagless.rock_the_jvm.tagless_final_refactor.{Algebra, SimpleExpr}

class SimpleExprSuite extends munit.FunSuite {
  import simpleExprAlg.*

  test("B") {
    assertEquals(b(false).value, false)
    assertEquals(b(true).value, true)
  }

  test("Not") {
    assertEquals(not(b(false)).value, true)
    assertEquals(not(b(true)).value, false)
  }

  test("And") {
    assertEquals(and(b(false), b(false)).value, false)
    assertEquals(and(b(false), b(true)).value, false)
    assertEquals(and(b(true), b(false)).value, false)
    assertEquals(and(b(true), b(true)).value, true)
  }

  test("Or") {
    assertEquals(or(b(false), b(false)).value, false)
    assertEquals(or(b(false), b(true)).value, true)
    assertEquals(or(b(true), b(false)).value, true)
    assertEquals(or(b(true), b(true)).value, true)
  }

  test("expression combination") {
    val expr = not(or(and(b(true), b(false)), b(true)))
    assertEquals(expr.value, false)
  }

  test("I") {
    assertEquals(i(6).value, 6)
  }

  test("Add integers") {
    assertEquals(sum(i(6), i(8)).value, 14)
  }

  test("Boolean program") {
    def program[E[_]](using alg: Algebra[E]): E[Boolean] = {
      import alg.*
      or(and(b(true), b(false)), b(true))
    }

    assertEquals(program[SimpleExpr].value, true)
  }

  test("Integer program") {
    def program[E[_]](using alg: Algebra[E]): E[Int] = {
      import alg.*
      sum(sum(i(0), i(5)), i(-3))
    }

    assertEquals(program[SimpleExpr].value, 2)
  }

  // This no longer compiles - yay for type safety!
//    test("Add integer and boolean - boom!") {
//      val expectedErrorMessge = "assertion failed: attempting to evaluate Sum expression with improperly typed operands (bool, int)"
//      interceptMessage[java.lang.AssertionError](expectedErrorMessge) {
//        sum(b(true), i(8)).value
//      }
//    }
}
