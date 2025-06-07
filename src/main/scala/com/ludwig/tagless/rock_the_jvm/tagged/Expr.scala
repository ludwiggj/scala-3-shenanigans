package com.ludwig.tagless.rock_the_jvm.tagged

sealed trait Expr(val tag: String) // the tree

case class B(boolean: Boolean) extends Expr("bool")
case class Or(left: Expr, right: Expr) extends Expr("bool")
case class And(left: Expr, right: Expr) extends Expr("bool")
case class Not(expr: Expr) extends Expr("bool")
case class I(int: Int) extends Expr("int")
case class Sum(left: Expr, right: Expr) extends Expr("int")

object Expr {
  // This approach has the benefit of doing “type” checks (by checking the tag) and returning
  // the correct result for each expression. However, the type check still happens at runtime,
  // we still don’t have true type checking (by the wider return type) and we’re still doing
  // type casts. This still isn't type safe - it can crash at runtime
  def eval(expr: Expr): Boolean | Int = expr match {
    case And(left, right) =>
      assert(
        left.tag == "bool" && right.tag == "bool",
        s"attempting to evaluate And expression with improperly typed operands (${left.tag}, ${right.tag})"
      )
      eval(left).asInstanceOf[Boolean] && eval(right).asInstanceOf[Boolean]
    case B(boolean) =>
      boolean
    case Not(expr) =>
      assert(
        expr.tag == "bool",
        s"attempting to evaluate Not expression with improperly typed operand (${expr.tag})"
      )
      !eval(expr).asInstanceOf[Boolean]
    case Or(left, right) =>
      assert(
        left.tag == "bool" && right.tag == "bool",
        s"attempting to evaluate Or expression with improperly typed operands (${left.tag}, ${right.tag})"
      )
      eval(left).asInstanceOf[Boolean] || eval(right).asInstanceOf[Boolean]
    case I(i) => i
    case Sum(left, right) =>
      assert(
        left.tag == "int" && right.tag == "int",
        s"attempting to evaluate Sum expression with improperly typed operands (${left.tag}, ${right.tag})"
      )
      eval(left).asInstanceOf[Int] + eval(right).asInstanceOf[Int]
  }
}
