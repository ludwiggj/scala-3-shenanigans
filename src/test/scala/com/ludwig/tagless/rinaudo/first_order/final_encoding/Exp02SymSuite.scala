package com.ludwig.tagless.rinaudo.first_order.final_encoding

import com.ludwig.tagless.rinaudo.first_order.final_encoding.Exp02Sym

class Exp02SymSuite extends munit.FunSuite {
  // exp is written as a method rather than a function because it’s polymorphic, and support for
  // polymorphic functions is either absent (Scala 2) or saddled with a rather unfortunate syntax
  // (Scala 3).
  def exp[A](sym: Exp02Sym[A]): A = {
    import sym.*
    add(lit(1), add(lit(-2), lit(4)))
  }
  
  test("print expression") {
    // Let’s do pretty-printing.
    val print = new Exp02Sym[String] {
      override def lit(i: Int): String = i.toString
      override def add(lhs: String, rhs: String): String = s"($lhs + $rhs)"
    }

    assertEquals(exp(print), "(1 + (-2 + 4))")
  }
  
  test("evaluate expression") {
    // Let’s evaluate!
    val evaluate = new Exp02Sym[Int] {
      override def lit(i: Int): Int = i
      override def add(lhs: Int, rhs: Int): Int = lhs + rhs
    }

    assertEquals(exp(evaluate), 3)
  }
}
