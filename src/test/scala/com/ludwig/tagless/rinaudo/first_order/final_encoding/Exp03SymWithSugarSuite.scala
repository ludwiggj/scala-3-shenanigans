package com.ludwig.tagless.rinaudo.first_order.final_encoding

import com.ludwig.tagless.rinaudo.first_order.final_encoding.Exp02Sym
import com.ludwig.tagless.rinaudo.first_order.final_encoding.Exp03SymWithSugar.*

class Exp03SymWithSugarSuite extends munit.FunSuite {

  // no longer pass sym in as a parameter to exp, so don't have to import sym.*
  def exp[A: Exp02Sym]: A = {
    add(lit(1), add(lit(-2), lit(4)))
  }

  test("print expression") {
    // Let’s do pretty-printing - declare a given
    given Exp02Sym[String] with {
      override def lit(i: Int): String = i.toString
      override def add(lhs: String, rhs: String): String = s"($lhs + $rhs)"
    }

    assertEquals(exp, "(1 + (-2 + 4))")
  }
  
  test("evaluate expression") {
    // Let’s evaluate!
    given Exp02Sym[Int] with {
      override def lit(i: Int): Int = i
      override def add(lhs: Int, rhs: Int): Int = lhs + rhs
    }

    assertEquals(exp, 3)
  }
}
