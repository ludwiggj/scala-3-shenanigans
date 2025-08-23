package com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding

import com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding.ExpSym.{add, lit}
import com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding.MultSym.mult

class MultSymSuite extends munit.FunSuite {
  def multExp[F[_]](using expSym: ExpSym[F], multSym: MultSym[F]): F[Int] = {
    mult(lit(-3), add(lit(-2), lit(4)))
  }
  
  test("evaluate expression") {
    import Evaluate.ExpSymEvaluate.given
    import Evaluate.MultSymEvaluate.given
    assertEquals(multExp, -6)
  }
}
