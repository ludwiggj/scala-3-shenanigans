package com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding

import com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding.ExpSym.{add, lit}

class ExpSymSuite extends munit.FunSuite {
  def addExp[F[_]](using sym: ExpSym[F]): F[Int] = {
    add(lit(1), add(lit(-2), lit(4)))
  }

  def eqExp[F[_]](using sym: ExpSym[F]): F[Boolean] = {
    ExpSym.eq(lit(1), add(lit(-2), lit(4)))
  }

  test("print add expression") {
    import com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding.ExpSymPrettyPrint.given
    assertEquals(addExp, "(1 + (-2 + 4))")
  }
  
  test("evaluate add expression") {
    import com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding.Evaluate.ExpSymEvaluate.given
    assertEquals(addExp, 3)
  }

  test("evaluate eq expression") {
    import com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding.Evaluate.ExpSymEvaluate.given
    assertEquals(eqExp, false)
  }
}
