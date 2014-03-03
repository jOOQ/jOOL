jOOλ
====

jOOλ is part of the jOOQ series (along with jOOQ, jOOX, jOOR, jOOU) providing some useful extensions to Java 8 lambdas. It contains these classes:

org.jooq.lambda.Unchecked
-------------------------

Lambda expressions and checked exceptions are a major pain. Even before going live with Java 8, there are a lot of Stack Overflow questions related to the subject:

- http://stackoverflow.com/q/18198176/521799
- http://stackoverflow.com/q/19757300/521799
- http://stackoverflow.com/q/14039995/521799

The Unchecked class can be used to wrap common `@FunctionalIntefaces` in equivalent ones that are allowed to throw checked exceptions. E.g. this painful beast:

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

(where SQLGoodies.Schema is just an ordinary POJO)