package com.ludwig.tagless.rinaudo.higher_order.tagged_initial_encoding

enum Exp {
  case Lit(value: Int)
  case Add(lhs: Exp, rhs: Exp)
  case Eq(lhs: Exp, rhs: Exp)
}

case class TypeError(operation: String) extends RuntimeException {
  override def getMessage: String = operation
}

object Exp {
  import Exp.*

  // eval returns a union type, as evaluation expression may return an Int or a Boolean
  // `Int | Boolean` is a type tag, hence this is a tagged initial encoding
  def eval(exp: Exp): Int | Boolean = exp match {
    case Lit(value) => value

    case Add(lhs, rhs) => (eval(lhs), eval(rhs)) match {
      case (l: Int, r: Int) => l + r
      case _ => throw TypeError("Add")
    }

    case Eq(lhs, rhs) => (eval(lhs), eval(rhs)) match {
      case (l: Int, r: Int) => l == r
      case (l: Boolean, r: Boolean) => l == r
      case _ => throw TypeError("Eq")
    }
  }
}