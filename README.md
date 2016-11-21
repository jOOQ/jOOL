jOOλ
====

jOOλ is part of the jOOQ series (along with jOOQ, jOOX, jOOR, jOOU) providing some useful extensions to Java 8 lambdas. It contains these classes:

org.jooq.lambda.function
------------------------

Why only `Function` and `BiFunction`? We have also included support for `Function1` through `Function16`.

org.jooq.lambda.tuple
---------------------

Tuple support is essential in functional programming. A variety of things can be modelled as tuples, e.g. function argument lists. This is why we support type safe `Tuple1` through `Tuple16` types.

org.jooq.lambda.Seq
-------------------

The new Streams API was implemented to provide filter/map/reduce-like operations leveraging the new lambdas.
Many of the useful methods that we know from other functional languages (e.g Scala) are missing. This is why jOOλ knows
a `Seq` (short of _Sequential_) interface that extends `Stream` and adds a variety of additional methods to.

Please note that all `Seq`'s are **sequential and ordered streams**, so don't bother to call `parallel()` on it, it will
return the same `Seq`.

`Seq` adds a handful of useful methods, such as:

```java
// (1, 2, 3, 4, 5, 6)
Seq.of(1, 2, 3).concat(Seq.of(4, 5, 6));

// true
Seq.of(1, 2, 3, 4).contains(2);

// true
Seq.of(1, 2, 3, 4).containsAll(2, 3);

// true
Seq.of(1, 2, 3, 4).containsAny(2, 5);

// (tuple(1, "A"), tuple(1, "B"), tuple(2, "A"), tuple(2, "B"))
Seq.of(1, 2).crossJoin(Seq.of("A", "B"));

// (1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, ...)
Seq.of(1, 2, 3).cycle();

// tuple((1, 2, 3), (1, 2, 3))
Seq.of(1, 2, 3).duplicate();

// "!abc"
Seq.of("a", "b", "c").foldLeft("!", (u, t) -> u + t);

// "abc!"
Seq.of("a", "b", "c").foldRight("!", (t, u) -> t + u);

// { 1 = (1, 3), 0 = (2, 4) }
Seq.of(1, 2, 3, 4).groupBy(i -> i % 2);

// (tuple(1, (1, 3)), tuple(0, (2, 4)))
Seq.of(1, 2, 3, 4).grouped(i -> i % 2);

// (tuple(1, 1), tuple(2, 2))
Seq.of(1, 2, 4).innerJoin(Seq.of(1, 2, 3), (a, b) -> a == b);

// (1, 0, 2, 0, 3, 0, 4)
Seq.of(1, 2, 3, 4).intersperse(0);

// "123"
Seq.of(1, 2, 3).join();

// "1, 2, 3"
Seq.of(1, 2, 3).join(", ");

// "^1|2|3$"
Seq.of(1, 2, 3).join("|", "^", "$"); 

// (tuple(1, 1), tuple(2, 2), tuple(4, null))
Seq.of(1, 2, 4).leftOuterJoin(Seq.of(1, 2, 3), (a, b) -> a == b);

// (1, 2)
Seq.of(1, 2, 3, 4, 5).limitWhile(i -> i < 3);

// (1, 2)
Seq.of(1, 2, 3, 4, 5).limitUntil(i -> i == 3);

// (1, 2L)
Seq.of(new Object(), 1, "B", 2L).ofType(Number.class);

// (tuple(1, 1), tuple(2, 2), tuple(null, 3))
Seq.of(1, 2, 4).rightOuterJoin(Seq.of(1, 2, 3), (a, b) -> a == b);

// tuple((1, 3), (2, 4))
Seq.of(1, 2, 3, 4).partition(i -> i % 2 != 0);

// (1, 3, 4)
Seq.of(1, 2, 3, 4).remove(2);

// (1, 4)
Seq.of(1, 2, 3, 4).removeAll(2, 3, 5);

// (2, 3)
Seq.of(1, 2, 3, 4).retainAll(2, 3, 5);

// (4, 3, 2, 1)
Seq.of(1, 2, 3, 4).reverse();

// (3, 1, 4, 5, 2) for example
Seq.of(1, 2, 3, 4, 5).shuffle();

// (3, 4, 5)
Seq.of(1, 2, 3, 4, 5).skipWhile(i -> i < 3);

// (3, 4, 5)
Seq.of(1, 2, 3, 4, 5).skipUntil(i -> i == 3);

// (2, 3)
Seq.of(1, 2, 3, 4, 5).slice(1, 3)

// tuple((1, 2), (3, 4, 5))
Seq.of(1, 2, 3, 4, 5).splitAt(2);

// tuple(1, (2, 3, 4, 5))
Seq.of(1, 2, 3, 4, 5).splitAtHead();

// tuple((1, 2, 3), (a, b, c))
Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));

// (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"));

// ("1:a", "2:b", "3:c")
Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (x, y) -> x + ":" + y);

// (tuple("a", 0), tuple("b", 1), tuple("c", 2))
Seq.of("a", "b", "c").zipWithIndex();
```

org.jooq.lambda.Unchecked
-------------------------

Lambda expressions and checked exceptions are a major pain. Even before going live with Java 8, there are a lot of Stack Overflow questions related to the subject:

- http://stackoverflow.com/q/18198176/521799
- http://stackoverflow.com/q/19757300/521799
- http://stackoverflow.com/q/14039995/521799

The Unchecked class can be used to wrap common `@FunctionalInterfaces` in equivalent ones that are allowed to throw checked exceptions. E.g. this painful beast:

```java
Arrays.stream(dir.listFiles()).forEach(file -> {
    try {
        System.out.println(file.getCanonicalPath());
    }
    catch (IOException e) {
        throw new RuntimeException(e);
    }

    // Ouch, my fingers hurt! All this typing!
});
```

... will become this beauty:

```java
Arrays.stream(dir.listFiles()).forEach(
    Unchecked.consumer(file -> { System.out.println(file.getCanonicalPath()); })
);
```

... or if you fancy method references:

```java
Arrays.stream(dir.listFiles())
        .map(Unchecked.function(File::getCanonicalPath))
        .forEach(System.out::println);
```

Maven
-----
```
<dependency>
	<groupId>org.jooq</groupId>
	<artifactId>jool</artifactId>
	<version>0.9.12</version>
</dependency>
```
