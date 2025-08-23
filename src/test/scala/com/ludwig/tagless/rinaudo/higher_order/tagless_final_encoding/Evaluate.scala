package com.ludwig.tagless.rinaudo.higher_order.tagless_final_encoding

object Evaluate {
  type Eval[A] = A

  object ExpSymEvaluate {
    given ExpSym[Eval] with {
      def lit(i: Int): Eval[Int] = i

      def add(lhs: Eval[Int], rhs: Eval[Int]): Eval[Int] = lhs + rhs

      def eq(lhs: Eval[Int], rhs: Eval[Int]): Eval[Boolean] = lhs == rhs
    }
  }

  object MultSymEvaluate {
    given MultSym[Eval] with {
      def mult(lhs: Eval[Int], rhs: Eval[Int]): Eval[Int] = lhs * rhs
    }
  }
}
