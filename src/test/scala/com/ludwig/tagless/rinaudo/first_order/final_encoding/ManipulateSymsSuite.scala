package com.ludwig.tagless.rinaudo.first_order.final_encoding

import com.ludwig.tagless.rinaudo.first_order.final_encoding.Exp03SymWithSugar.*
import com.ludwig.tagless.rinaudo.first_order.final_encoding.Exp04MultSym.*

class ManipulateSymsSuite extends munit.FunSuite {
  given Exp02Sym[Int] with {
    override def lit(i: Int): Int = i

    override def add(lhs: Int, rhs: Int): Int = lhs + rhs
  }

  given Exp04MultSym[Int] with {
    override def mult(lhs: Int, rhs: Int): Int = lhs * rhs
  }

  given Exp02Sym[String] with {
    override def lit(i: Int): String = i.toString

    override def add(lhs: String, rhs: String): String = s"($lhs + $rhs)"
  }

  given Exp04MultSym[String] with {
    override def mult(lhs: String, rhs: String): String = s"($lhs x $rhs)"
  }

  object Multiplier {
    def twoTimes[A: Exp02Sym : Exp04MultSym](expr: A): A = {
      mult(expr, lit(2))
    }

    def threeTimes[A: Exp02Sym : Exp04MultSym](expr: A): A = {
      mult(expr, lit(3))
    }
  }

  def doubleIt[A: Exp02Sym : Exp04MultSym](expr: A => A): A => A = (a: A) => mult(expr(a), lit(2))

  test("4 times 7") {
    val fourTimes = doubleIt(Multiplier.twoTimes[Int])
    assertEquals(fourTimes(7), 28)
  }

  test("6 times 6") {
    assertEquals(doubleIt[Int](Multiplier.threeTimes).apply(6), 36)
  }

  test("6 times 7") {
    // No given instance of type com.ludwig.tagless.rinaudo.final_encoding.ExpSym[Any] was found for a context parameter
    // of method twoTimes in object Doubler
    // val a = doubleIt(Doubler.twoTimes)

    def a[A: Exp02Sym : Exp04MultSym]: A => A = doubleIt(Multiplier.threeTimes)

    assertEquals(a.apply(7), 42)
  }

  test("polymorphic function fun") {
    // Note that ?=> is the ImplicitType operator. It defines implicit (or given) parameters in a function declaration
    val doubleItPoly: [A] => (A => A) => (Exp02Sym[A], Exp04MultSym[A]) ?=> A => A =
      [A] => (a: A => A) => (expSym: Exp02Sym[A], expMultSym: Exp04MultSym[A]) ?=> doubleIt(a)(expSym, expMultSym)

    def sixTimes[A: Exp02Sym : Exp04MultSym]: A => A = doubleItPoly(Multiplier.threeTimes)

    val fourTimes: [A] => () => (Exp02Sym[A], Exp04MultSym[A]) ?=> A => A =
      [A] => () => (expSym: Exp02Sym[A], expMultSym: Exp04MultSym[A]) ?=> doubleItPoly(Multiplier.twoTimes)

    assertEquals(sixTimes.apply("6"), "((6 x 3) x 2)")
    assertEquals(sixTimes.apply(6), 36)

    assertEquals(fourTimes[String]()("6"), "((6 x 2) x 2)")
    assertEquals(fourTimes[Int]()(6), 24)
  }
}
