# An Introduction to Tagless Final

Taken from [this fine article](https://nrinaudo.github.io/articles/tagless_final.html).

## Problem statement

Let's model a very simple DSL that allows us to express:

- integer literals.
- addition of two integers e.g `1 + (2 + 4)`.

We must also be able to provide multiple interpreters. We’ll focus on:

- pretty-printing: take an expression and make it "human-readable".
- evaluation: compute the result of an expression.

## Initial Encoding

See the [initial_encoding/Exp enum](first_order/initial_encoding/Exp.scala). This encodes the DSL as a straightforward ADT, and is
known as an initial encoding. See also
the [related tests](../../../../../../test/scala/com/ludwig/tagless/rinaudo/first_order/initial_encoding/ExpSuite.scala).

### The Expression Problem

This encoding does have one flaw, however: adding new elements to our DSL breaks existing interpreters. See the `Mult`
method added to [initial_encoding/Exp enum](first_order/initial_encoding/Exp.scala). Existing interpreters do not know about Mult
and cannot possibly handle it - it didn’t exist when they were written. Every single one of them will break one way or
another when presented with expressions containing multiplications - see the test
`evaluate multiplication expression - boom!`
in [ExpSuite](../../../../../../test/scala/com/ludwig/tagless/rinaudo/first_order/initial_encoding/ExpSuite.scala) for an example.

This is known as the Expression Problem: finding a statically checked encoding for a DSL that allows us to add both
syntax (such as multiplication) and interpreters (such as pretty-printing) without breaking anything.

## Final Encoding

### Modelling With Functions

The core intuition of final encoding is that instead of using ADTs, we use functions to encode expressions. One way to
think about this is that adding elements to an existing DSL can be seen as taking two DSLs, the old one and a new one
with all the syntax we want to add, and compose them to form a third, richer one. When looking at things that way, it’s
clear that ADTs aren’t going to be a great tool: they do not compose. Functions, on the other hand, famously do.

See [final_encoding/Exp01ViaFunctions](./first_order/final_encoding/Exp01ViaFunctions.scala), which represents the DSL
as functions. The first DSL is modelled via the add and lit functions. The second DSL that represents multiplication can
be added as the mult function, and they work together nicely.

See [Exp01ViaFunctionsSuite](../../../../../../test/scala/com/ludwig/tagless/rinaudo/first_order/final_encoding/Exp01ViaFunctionsSuite.scala)
for the related tests.

This is known as a final encoding. One of its particularities is that we’re working directly with interpreted values:
look at lit, mult, … they’re all taking and returning Int. This makes things easy, but is also a major flaw in the
encoding.

### Supporting Multiple Interpreters

The issue with this naive final encoding is that it works fine. but only if you’re not interested in writing multiple
interpreters. Since our functions immediately evaluate the corresponding expression, we have a single interpreter: the
evaluator. This is why the test method `"print expression"` was commented out
in [Exp01ViaFunctionsSuite](../../../../../../test/scala/com/ludwig/tagless/rinaudo/first_order/final_encoding/Exp01ViaFunctionsSuite.scala),
as the assertEquals line does not compile:

```
assertEquals(expr, "(5 + (1 + -4))")

Can't compare these two types:
First type: Int
Second type: String
```

We’re trying to solve the Expression Problem, which means writing multiple interpreters without breaking anything, yet
our encoding cannot write more than one, breaking changes or not. We need to modify this approach in order to write
multiple interpreters.

The intuition, here, is that since we’re working with functions, the only way to tell them how to interpret some data is
to pass an interpreter as a parameter. See [final_encoding/Exp02Sym](./first_order/final_encoding/Exp02Sym.scala). Sym
stands for symantic i.e. syntax and semantic: the type describes the syntax of our language, values of that type its
semantics.

To see an example of an expression using this syntax
see [Exp02SymSuite](../../../../../../test/scala/com/ludwig/tagless/rinaudo/first_order/final_encoding/Exp02SymSuite.scala).
We now have an encoding for our DSL and an expression of it, all we need to confirm that it works is an actual
interpreter.

### Syntactic Sugar

`Exp02Sym` can be tidied up by:

- declaring helper functions for lit and add, so that we no longer need to import sym.*.
- making ExpSym implicit, to avoid having to pass it explicitly.

See [final_encoding/Exp03SymWithSugar](./first_order/final_encoding/Exp03SymWithSugar.scala) for the resulting code, and
also [Exp03SymWithSugarSuite](../../../../../../test/scala/com/ludwig/tagless/rinaudo/first_order/final_encoding/Exp03SymWithSugarSuite.scala).

### The Expression Problem

[final_encoding/Exp03SymWithSugar](./first_order/final_encoding/Exp03SymWithSugar.scala) is a less naive final encoding
that allows us to support multiple interpreters. This was a lot of work to achieve exactly the same thing as the initial
encoding, but the pay-off is if it solves the Expression Problem. Let’s try to add multiplication to our final-encoded
DSL. Earlier it was mentioned that this can be seen as composing two distinct DSLs. This is exactly what we’re going to
do, by making multiplication its own dedicated DSL.

See [final_encoding/Exp04MultSym](./first_order/final_encoding/Exp04MultSym.scala)
and [Exp04CombiningSymsSuite](../../../../../../test/scala/com/ludwig/tagless/rinaudo/first_order/final_encoding/Exp04CombiningSymsSuite.scala)

So we’ve added syntax to our DSL without breaking any pre-existing code.

We can also add interpreters without breaking anything, since that’s merely a matter of writing new given instances of
an `Exp02Sym` and `Exp04MultSym` for the appropriate type A.

### Manipulating values of our DSL

There’s a major flaw in our implementation though: it does not allow us to manipulate expressions of our DSL. All we can
do is interpret them - we cannot, for example, pass them to other functions, or return them from functions. This is
because we’ve declared them as methods, which are not first-class citizens.

In theory that shouldn’t be much of an issue, because the compiler can mostly turn methods into functions, and those are
first-class citizen. In practice, unfortunately, it won’t quite work out because our methods are polymorphic.

Scala 2 does not support polymorphic functions. You could keep working with methods, which drastically reduces the
usefulness of a final encoding; expressions not being values means you cannot, say, parse them from text files. Or you
could write a lot of scaffolding to simulate polymorphic functions.

Things are a little better in Scala 3, because it does support polymorphic functions. I encourage you to play with this
yourself, and maybe, I don’t know, to think about ways of encoding an expression in JSON and back to an in-memory
representation. It won’t be pleasant, but it will be enlightening.

NOTE: The above section would be much clearer if it also had examples. Regardless, I've had a play with polymorphic
functions,
see [ManipulateSymsSuite](../../../../../../test/scala/com/ludwig/tagless/rinaudo/first_order/final_encoding/ManipulateSymsSuite.scala).
Note that this repo uses Scala 3.

See
also [Scala 3: Type Lambdas, Polymorphic Function Types, and Dependent Function Types](https://medium.com/scala-3/scala-3-type-lambdas-polymorphic-function-types-and-dependent-function-types-2a6eabef896d).

## Higher-Order Languages

We’ve talked about the distinction between initial and final encodings. We still need to tackle the tagless part. For
this, we must consider a higher-order language: one where evaluating an expression might yield different types.

For that, we’ll take our existing DSL, and add the ability to compare two numbers for equality. For example:
`(1 + 2) = 3` is `true`.

### Tagged Initial Encoding

As we’ve seen, an initial encoding is simply a confusing name for using an ADT. Here’s what an ADT of that new DSL might
look like:

```scala
enum Exp:
  case Lit(value: Int)
  case Add(lhs: Exp, rhs: Exp)
  case Eq(lhs: Exp, rhs: Exp)
```

So far so good, we can represent expressions of our DSL as values. We can also fairly easily write a pretty-printer for
them:

```scala
def print(exp: Exp): String = exp match
  case Lit(value) => value.toString
  case Add(lhs, rhs) => s"(${print(lhs)} + ${print(rhs)})"
  case Eq(lhs, rhs) => s"(${print(lhs)} = ${print(rhs)})"
```

Evaluation, on the other hand, is problematic. An expression might yield an integer (Add and Lit) or a boolean (Eq). And
second, how do we implement Add, since we have no guarantee that both operands yield integers?

We could use Any and runtime type checks, but resorting to runtime type checks by definition means that we’re not
checking it statically, a requirement for a solution to the Expression Problem. We can use union types.

See [tagged_initial_encoding/Exp](higher_order/tagged_initial_encoding/Exp.scala)
and [ExpSuite](../../../../../../test/scala/com/ludwig/tagless/rinaudo/higher_order/tagged_initial_encoding/ExpSuite.scala).
The tagged part comes from the fact that `Int | Boolean`, a type tag, is used to keep track of what we’re working with.

It’s not a very pleasant encoding. There's exceptions, awkward pattern matches… the stuff of nightmares. This is a
symptom of a deeper problem that we’ve been ignoring, consciously or not: our ADT allows us to write ill-typed
exceptions, such as `(1 = 2) + 3`. The solution to that will be a tagless initial encoding.

### Tagless Initial Encoding

The problem we must solve is that, when Add-ing two expressions, we have no way of knowing if they’ll evaluate to
integers. To work around this, we’ll need to keep track of the type of an expression’s normal form: what evaluating it
will yield. The simplest way of achieving this is to make it a type parameter.

See [tagless_initial_encoding/Exp.scala](higher_order/tagless_initial_encoding/Exp.scala)
and [ExpSuite](../../../../../../test/scala/com/ludwig/tagless/rinaudo/higher_order/tagless_initial_encoding/ExpSuite.scala).

Pay special attention to Add: it’s now impossible to create such an expression with non-numerical operands. We’ve made
it so that our values must be well typed, and we can safely evaluate them without jumping through all the type-checking
hoops that the tagged initial encoding required; see implementation of eval method.

Exp is now a Generalised Algebraic Data Type. And, really, that’s what tagless initial means: GADT. This encoding is
terse, statically checked, interpreters are simple natural recursions, but it does not solve the Expression Problem. As
we’ve seen earlier, initial encodings, tagged or not, do not.

### Tagless Final Encoding

If you think about it a little, you should see that this suffers from the same problem we encountered earlier: we cannot
write a well-type evaluator, and will need to do some runtime type analysis.

We'll skip the tagged final encoding as we’ve seen that tagged encodings allowed us to represent illegal values, and
therefore this solution is inherently flawed.

Instead, let’s consider what tagless would mean for a final encoding. We need to somehow keep track of two things: the
expression’s normal form, and the interpreted type.

A naive implementation of this would be to consider that since we’re tracking two types, we need two type parameters -
something like `ExpSym[N, A]` (where N stands for normal form). But we’d immediately hit a dead end: how would you
implement add?

```scala
def add(lhs: ???, rhs: ???): ???
```

What could we put in the `???` bits? We need it to be both:

* the interpreted type, because this is what’s passed to add in a final encoding.
* the expression’s normal form, to confirm that both operands are numeric.

What we want, really, is to parameterise the interpreted type by the expression’s normal form:

```scala
def add(lhs: F[Int], rhs: F[Int]): F[Int]
```

Here, Int is the expression’s normal form, and F the interpreted type. Now that we’ve decided to represent an
interpreted type as a type parameterised on an expression’s normal form, let’s write ExpSym accordingly:

```scala
trait ExpSym[F[_]]:
  def lit(i: Int): F[Int]
  def add(lhs: F[Int], rhs: F[Int]): F[Int]
  def eq(lhs: F[Int], rhs: F[Int]): F[Boolean]
```

Note how it’s now impossible for add to take things that do not evaluate to a number. Our expressions are back to being
well-typed.

See [tagless_final_encoding/ExpSym.scala](higher_order/tagless_final_encoding/ExpSym.scala) for the full implementation.

This is a bit abstract, so let’s write some concrete symantics to try and wrap our heads around it;
see [ExpSymPrettyPrint](../../../../../../test/scala/com/ludwig/tagless/rinaudo/higher_order/tagless_final_encoding/ExpSymPrettyPrint.scala)
and
see [ExpSymEvaluate](../../../../../../test/scala/com/ludwig/tagless/rinaudo/higher_order/tagless_final_encoding/Evaluate.scala).

See also
the [related tests](../../../../../../test/scala/com/ludwig/tagless/rinaudo/higher_order/tagless_final_encoding/ExpSymSuite.scala).

### The Expression Problem

Does the above solve the Expression Problem for higher-order languages?

Remember that the expression problem is solved if we can find a statically checked encoding for a DSL that allows us to
add both syntax (such as multiplication) and interpreters (such as pretty-printing) without breaking anything.

We’ll investigate by adding support for multiplications, as
a [new symantic](higher_order/tagless_final_encoding/MultSym.scala).

Writing an evaluator for that
is [not very hard at all](../../../../../../test/scala/com/ludwig/tagless/rinaudo/higher_order/tagless_final_encoding/Evaluate.scala).
And
finally, [here’s an expression that uses both addition and multiplication, without any need to recompile anything](../../../../../../test/scala/com/ludwig/tagless/rinaudo/higher_order/tagless_final_encoding/MultSymSuite.scala)

We have, finally, found an encoding for our DSL that solved the Expression Problem. Not a nice encoding, mind, nor even
a very convenient one, but one that unarguably does everything we set out to achieve.