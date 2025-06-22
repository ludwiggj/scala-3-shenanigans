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

See the [Exp enum](./Exp.scala). This encodes the DSL as a straightforward ADT, and is known as an initial encoding. See
also the [related tests](/src/test/scala/com/ludwig/tagless/rinaudo/ExpSuite.scala).

### The Expression Problem

This encoding does have one flaw, however: adding new elements to our DSL breaks existing interpreters. See the `Mult`
method added to [Exp enum](./Exp.scala). Existing interpreters do not know about Mult and cannot possibly handle it - it
didn’t exist when they were written. Every single one of them will break one way or another when presented with
expressions containing multiplications - see the test `evaluate multiplication expression - boom!`
in [ExpSuite](/src/test/scala/com/ludwig/tagless/rinaudo/ExpSuite.scala) for an example.

This is known as the Expression Problem: finding a statically checked encoding for a DSL that allows us to add both
syntax (such as multiplication) and interpreters (such as pretty-printing) without breaking anything.

## Final Encoding

### Modelling With Functions

The core intuition of final encoding is that instead of using ADTs, we use functions to encode expressions. One way to
think about this is that adding elements to an existing DSL can be seen as taking two DSLs, the old one and a new one
with all the syntax we want to add, and compose them to form a third, richer one. When looking at things that way, it’s
clear that ADTs aren’t going to be a great tool: they do not compose. Functions, on the other hand, famously do.

See [ExpViaFunctions](./ExpViaFunctions.scala), which represents the DSL as functions. The first DSL is modelled via the
add and lit functions. The second DSL that represents multiplication can be added as the mult function, and they work
together nicely.

See [ExpViaFunctionsSuite](/src/test/scala/com/ludwig/tagless/rinaudo/ExpViaFunctionsSuite.scala) for the related
tests.

This is known as a final encoding. One of its particularities is that we’re working directly with interpreted values:
look at lit, mult, … they’re all taking and returning Int. This makes things easy, but is also a major flaw in the
encoding.

### Supporting Multiple Interpreters

The issue with this naive final encoding is that it works fine. but only if you’re not interested in writing multiple
interpreters. Since our functions immediately evaluate the corresponding expression, we have a single interpreter: the
evaluator. This is why the test method `"print expression"` was commented out
in [ExpViaFunctionsSuite](/src/test/scala/com/ludwig/tagless/rinaudo/ExpViaFunctionsSuite.scala), as the assertEquals
line does not compile:

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
to pass an interpreter as a parameter. See [ExpSym](./ExpSym.scala). Sym stands for symantic i.e. syntax and semantic:
the type describes the syntax of our language, values of that type its semantics.

To see an example of an expression using this syntax
see [ExpSymSuite](/src/test/scala/com/ludwig/tagless/rinaudo/ExpSymSuite.scala). We now have an encoding for our DSL and
an expression of it, all we need to confirm that it works is an actual interpreter.

### Syntactic Sugar

`ExpSym` can be tidied up by:

- declaring helper functions for lit and add, so that we no longer need to import sym.*.
- making ExpSym implicit, to avoid having to pass it explicitly.

See [ExpSymWithSugar](./ExpSymWithSugar.scala) for the resulting code, and
also [ExpSymWithSugarSuite](/src/test/scala/com/ludwig/tagless/rinaudo/ExpSymWithSugarSuite.scala).

### The Expression Problem

[ExpSymWithSugar](./ExpSymWithSugar.scala) is a less naive final encoding that allows us to support multiple
interpreters. This was a lot of work to achieve exactly the same thing as the initial encoding, but the pay-off is if it
solves the Expression Problem. Let’s try to add multiplication to our final-encoded DSL. Earlier it was mentioned that
this can be seen as composing two distinct DSLs. This is exactly what we’re going to do, by making multiplication its
own dedicated DSL.

See [ExpMultSym](./ExpMultSym.scala)
and [CombiningSymsSuite](/src/test/scala/com/ludwig/tagless/rinaudo/CombiningSymsSuite.scala)

So we’ve added syntax to our DSL without breaking any pre-existing code.

We can also add interpreters without breaking anything, since that’s merely a matter of writing new given instances of
an ExpSym and ExpMultSym for the appropriate type A.