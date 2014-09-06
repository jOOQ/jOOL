jOOλ
====

jOOλ is part of the jOOQ series (along with jOOQ, jOOX, jOOR, jOOU) providing some useful extensions to Java 8 lambdas. It contains these classes:

org.jooq.lambda.function
------------------------

Why only `Function` and `BiFunction`? We have also included support for `Function1` through `Function8`.

org.jooq.lambda.tuple
---------------------

Tuple support is essential in functional programming. A variety of things can be modelled as tuples, e.g. function argument lists. This is why we support type safe `Tuple1` through `Tuple8` types.

org.jooq.lambda.Seq
-------------------

The new Streams API was implemented with a strong focus on parallelisation. Many of the useful methods that we know from other functional languages (e.g Scala) are missing. This is why jOOλ knows a `Seq` interface that extends `Stream` and adds a variety of additional methods to, such as:

```java
// (1, 2, 3, 4, 5, 6)
Seq.of(1, 2, 3).concat(Seq.of(4, 5, 6));

// (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"));

// ("1:a", "2:b", "3:c")
Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (x, y) -> x + ":" + y);

// tuple((1, 2, 3), (a, b, c))
Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));

// (tuple("a", 0), tuple("b", 1), tuple("c", 2))
Seq.of("a", "b", "c").zipWithIndex();

// (3, 4, 5)
Seq.of(1, 2, 3, 4, 5).skipWhile(i -> i < 3);

// (3, 4, 5)
Seq.of(1, 2, 3, 4, 5).skipUntil(i -> i == 3);

// (1, 2)
Seq.of(1, 2, 3, 4, 5).limitWhile(i -> i < 3);

// (1, 2)
Seq.of(1, 2, 3, 4, 5).limitUntil(i -> i == 3);

// "abc"
Seq.of("a", "b", "c").foldLeft("", (u, t) -> t + u);

// "cba"
Seq.of("a", "b", "c").foldRight("", (t, u) -> t + u);

// tuple((1, 2, 3), (1, 2, 3))
Seq.of(1, 2, 3).duplicate();

// tuple((1, 3, 5), (2, 4, 6))
Seq.of(1, 2, 3, 4, 5, 6).partition(i -> i % 2 != 0)

// tuple((1, 2), (3, 4, 5))
Seq.of(1, 2, 3, 4, 5).splitAt(2);

// (2, 3)
Seq.of(1, 2, 3, 4, 5).slice(1, 3)
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

org.jooq.lambda.SQL
-------------------
JDBC should be as simple as this in Java 8:

```java
SQL.stream(stmt, Unchecked.function(r ->
    new SQLGoodies.Schema(
        r.getString("FIELD_1"),
        r.getBoolean("FIELD_2")
    )
))
.forEach(System.out::println);
```

(where SQLGoodies.Schema is just an ordinary POJO).

For more details and other options to write SQL in Java 8, visit this website here:

http://www.jooq.org/java-8-and-sql
