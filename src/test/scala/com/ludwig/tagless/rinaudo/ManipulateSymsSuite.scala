package com.ludwig.tagless.rinaudo

import com.ludwig.tagless.rinaudo.ExpMultSym.*
import com.ludwig.tagless.rinaudo.ExpSymWithSugar.*

class ManipulateSymsSuite extends munit.FunSuite {
  given ExpSym[Int] with {
    override def lit(i: Int): Int = i

    override def add(lhs: Int, rhs: Int): Int = lhs + rhs
  }

  given ExpMultSym[Int] with {
    override def mult(lhs: Int, rhs: Int): Int = lhs * rhs
  }

  given ExpSym[String] with {
    override def lit(i: Int): String = i.toString

    override def add(lhs: String, rhs: String): String = s"($lhs + $rhs)"
  }

  given ExpMultSym[String] with {
    override def mult(lhs: String, rhs: String): String = s"($lhs x $rhs)"
  }

  object Multiplier {
    def twoTimes[A: ExpSym : ExpMultSym](expr: A): A = {
      mult(expr, lit(2))
    }

    def threeTimes[A: ExpSym : ExpMultSym](expr: A): A = {
      mult(expr, lit(3))
    }
  }

  def doubleIt[A: ExpSym : ExpMultSym](expr: A => A): A => A = (a: A) => mult(expr(a), lit(2))

  test("4 times 7") {
    val fourTimes = doubleIt(Multiplier.twoTimes[Int])
    assertEquals(fourTimes(7), 28)
  }

  test("6 times 6") {
    assertEquals(doubleIt[Int](Multiplier.threeTimes).apply(6), 36)
  }

  test("6 times 7") {
    // No given instance of type com.ludwig.tagless.rinaudo.ExpSym[Any] was found for a context parameter
    // of method twoTimes in object Doubler
    // val a = doubleIt(Doubler.twoTimes)

    def a[A: ExpSym : ExpMultSym]: A => A = doubleIt(Multiplier.threeTimes)

    assertEquals(a.apply(7), 42)
  }

  test("polymorphic function fun") {
    // Note that ?=> is the ImplicitType operator. It defines implicit (or given) parameters in a function declaration
    val doubleItPoly: [A] => (A => A) => (ExpSym[A], ExpMultSym[A]) ?=> A => A =
      [A] => (a: A => A) => (expSym: ExpSym[A], expMultSym: ExpMultSym[A]) ?=> doubleIt(a)(expSym, expMultSym)

    def sixTimes[A: ExpSym : ExpMultSym]: A => A = doubleItPoly(Multiplier.threeTimes)

    val fourTimes: [A] => () => (ExpSym[A], ExpMultSym[A]) ?=> A => A =
      [A] => () => (expSym: ExpSym[A], expMultSym: ExpMultSym[A]) ?=> doubleItPoly(Multiplier.twoTimes)

    assertEquals(sixTimes.apply("6"), "((6 x 3) x 2)")
    assertEquals(sixTimes.apply(6), 36)

    assertEquals(fourTimes[String]()("6"), "((6 x 2) x 2)")
    assertEquals(fourTimes[Int]()(6), 24)
  }
}
