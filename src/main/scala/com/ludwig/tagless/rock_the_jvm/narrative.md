# Tagless Final in Scala Quickly Explained

Taken from [this fine article](https://rockthejvm.com/articles/tagless-final-in-scala).

## The Expression Problem

Because we write FP, we think in terms of expressions. For pure FP to work properly in a strongly typed language like
Scala, we immediately get into the expression problem. Namely, if we’re given an expression computing a value, we would
like to be able to evaluate it and return the proper value of the right type for that expression.

Imagine that we want to write a program that is able to evaluate boolean expressions. We can encode the boolean operands
and operators as nodes in a binary tree. An expression is a tree consisting of operands (leaves) and operators
(branches), and we can evaluate the expression by traversing the tree and collapsing its result to a single value. For
instance, the expression

`true && false || true`

can be modeled as

`Or(And(Leaf(true), Leaf(false)), Leaf(true))`

considering we had data structures to represent each node in the tree. Naming Leaf as B (easier), we can immediately
build up a few case classes to represent our particular expression problem:

```scala
trait Expr // the "tree"

case class B(boolean: Boolean) extends Expr

case class Or(left: Expr, right: Expr) extends Expr

case class And(left: Expr, right: Expr) extends Expr

case class Not(expr: Expr) extends Expr
```

and an evaluation function would look like this:

```scala
def eval(expr: Expr): Boolean = expr match {
  case B(b) => b
  case Or(a, b) => eval(a) || eval(b)
  case And(a, b) => eval(a) && eval(b)
  case Not(e) => !eval(e)
}
```

Assume that we’d now like to enhance our evaluation capabilities to now include integers as well:

```scala
case class I(int: Int) extends Expr

case class Sum(left: Expr, right: Expr) extends Expr
```

but now evaluating an expression is not so straightforward, because not only do we have to add some additional cases in
our pattern match, but also lose type safety and type-cast everything. See the `eval` method
in the [Expr](./problem/Expr.scala) trait.

Also, some of those expressions can have the incorrect type e.g. `Or(I(1), B(true))` which will crash when
type-casting; see test `"Add integer and boolean - boom!"`
in [ProblemExprSuite](/src/test/scala/com/ludwig/tagless/rock_the_jvm/problem/ProblemExprSuite.scala) for details.

So the problem becomes: how do we return the right type for the right expression?

## First Solution: Tagging

The expression problem requires us to differentiate numerical expressions from boolean expressions without destroying
the code quality, losing type safety or using type casts everywhere.

One easy way of achieving this differentiation is by using some additional data inside each instance of Expr to be able
to tell whether we should be using type casts, and what type to expect:

```scala

trait Expr(val tag: String)

case class B(boolean: Boolean) extends Expr("bool")

// ...
case class I(int: Int) extends Expr("int")
// ...
```

See the `eval` method in the [Expr](./tagged/Expr.scala) trait for the updated implementation.

This approach has the benefit of doing "type" checks (by checking the tag) and returning the correct result for each
expression. However, the type check still happens at runtime, and it isn't true type checking (note the wider return
type). It's also still doing type casts, and it isn't type safe - it can crash at runtime. See the test
`"Add integer and boolean - boom!"`
in [TaggedExprSuite](/src/test/scala/com/ludwig/tagless/rock_the_jvm/tagged/TaggedExprSuite.scala) for details. At least
the error message is slightly nicer.

A slight improvement would be to move the “type” checks at the construction phase of each data structure e.g.

```scala
case class Or(left: Expr, right: Expr) extends Expr("bool") {
  assert(left.tag == "bool" || right.tag == "bool")
}
```

but this would still crash at runtime.

## Removing Tags

Why add tags and check them at runtime, when we have a strongly typed language that can do the type checks for us at
compile time? We can remove the tags and let the compiler do the type-checking automatically.

```scala
trait Expr[A]

case class B(boolean: Boolean) extends Expr[Boolean]

// ...
case class I(int: Int) extends Expr[Int]
// ...
```

The compiler can check the generic argument for correctness. For instance, we can easily build an expression such as
`Or(B(true), B(false))` but we can’t build an expression such as `Or(I(1), B(true))` or `I(false)` or `B(45)`.

See the `eval` method in the [Expr](./tagless_initial/Expr.scala) trait for the cleaner implementation. No more type
tags! This is a tagless solution, because we’ve removed tags. It’s called tagless initial, because we work with
intermediate data structures i.e. `Expr[A]`, not with the values we care about i.e. `B`, `Or`, `And` etc.

See also
the [TaglessInitialExprSuite](/src/test/scala/com/ludwig/tagless/rock_the_jvm/tagless_initial/TaglessInitialExprSuite.scala)
tests.

## Tagless Final

The next step is to represent these expressions in terms of the evaluated value we care about (the final value). This is
tagless final. We’ll represent our expression types a bit differently,

```scala
trait Expr[A] {
  val value: A // the final value we care about
}

def b(boolean: Boolean): Expr[Boolean] = new Expr[Boolean] {
  val value = boolean
}

def i(int: Int): Expr[Int] = new Expr[Int] {
  val value = int
}
// etc...
```

`Expr[A]` has the evaluated value directly in the instance, as a member. Each construction of another Expr of the right
type already has the final value embedded there. Therefore, the evaluation function is almost empty, because all we need
to do is just return the value embedded in the expression being passed as argument. See the `eval` method in
the [Expr](./tagless_final/Expr.scala) trait.

See also
the [TaglessFinalExprSuite](/src/test/scala/com/ludwig/tagless/rock_the_jvm/tagless_final/TaglessFinalExprSuite.scala)
tests.

This represents a simple refactoring of the code in tagless initial so that we can immediately work with the final
representation of our results.

## A "Tagless Final" Refactor

We can refactor the previous solution to use higher kinds. We can group all our functionalities, i.e. the ability to
construct expressions, operands and operators in a single type class, implemented in terms of an abstract type E:

```scala
trait Algebra[E[_]] {
  def b(boolean: Boolean): E[Boolean]

  def i(int: Int): E[Int]
  // ...
}
```

See the [SimpleExpr](./tagless_final_refactor/SimpleExpr.scala) trait.

We can imagine a concrete implementation:

```scala
case class SimpleExpr[A](value: A)

given simpleExprAlg: Algebra[SimpleExpr] with {
  override def b(boolean: Boolean) = SimpleExpr(boolean)

  override def i(int: Int) = SimpleExpr(int)

  override def or(left: SimpleExpr[Boolean], right: SimpleExpr[Boolean]) = SimpleExpr(left.value || right.value)
  // ...
}
```

This implementation is an interpreter. See
the [SimpleExprSuite](/src/test/scala/com/ludwig/tagless/rock_the_jvm/tagless_final_refactor/SimpleExprSuite.scala) for
examples of its usage.