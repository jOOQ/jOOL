/**
 * Copyright (c), Data Geekery GmbH, contact@datageekery.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jooq.lambda;


import org.jooq.lambda.fi.lang.CheckedRunnable;
import org.jooq.lambda.fi.util.CheckedComparator;
import org.jooq.lambda.fi.util.concurrent.CheckedCallable;
import org.jooq.lambda.fi.util.function.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.function.*;

/**
 * Improved interoperability between checked exceptions and Java 8.
 * <p>
 * Checked exceptions are one of Java's biggest flaws. Due to backwards-compatibility, we're inheriting all the checked
 * exception trouble back from JDK 1.0. This becomes even more obvious when using lambda expressions, most of which are
 * not allowed to throw checked exceptions.
 * <p>
 * This library tries to ease some pain and wraps / unwraps a variety of API elements from the JDK 8 to improve
 * interoperability with checked exceptions.
 *
 * @author Lukas Eder
 */
public final class Unchecked {

    /**
     * A {@link Consumer} that wraps any {@link Throwable} in a {@link RuntimeException}.
     */
    public static final Consumer<Throwable> THROWABLE_TO_RUNTIME_EXCEPTION = Unchecked::throwUnchecked;

    /**
     * A {@link Consumer} that rethrows all exceptions, including checked exceptions.
     */
    public static final Consumer<Throwable> RETHROW_ALL = SeqUtils::sneakyThrow;



    /**
     * "sneaky-throw" a checked exception or throwable.
     */
    public static void throwChecked(Throwable t) {
        SeqUtils.sneakyThrow(t);
    }

    /**
     * An Unchecked strategy to handle Throwable: wrap any {@link Throwable} in a {@link RuntimeException}
     */
    public static void throwUnchecked(Throwable t) throws UncheckedException, UncheckedIOException, RuntimeException, Error {
        if (t instanceof Error)
            throw (Error) t;

        if (t instanceof RuntimeException)
            throw (RuntimeException) t;

        if (t instanceof IOException)
            throw new UncheckedIOException((IOException) t);

        // [#230] Clients will not expect needing to handle this.
        if (t instanceof InterruptedException)
            Thread.currentThread().interrupt();

        throw new UncheckedException(t);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.lang.Runnable
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedRunnable} in a {@link Runnable}.
     * <p>
     * Example:
     * <pre><code>
     * new Thread(Unchecked.runnable(() -&gt; {
     *     throw new Exception("Cannot run this thread");
     * })).start();
     * </code></pre>
     */
    public static Runnable runnable(CheckedRunnable runnable) {
        return runnable(runnable, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedRunnable} in a {@link Runnable} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * new Thread(Unchecked.runnable(
     *     () -&gt; {
     *         throw new Exception("Cannot run this thread");
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * )).start();
     * </code></pre>
     */
    public static Runnable runnable(CheckedRunnable runnable, Consumer<Throwable> handler) {
        return () -> {
            try {
                runnable.run();
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.lang.Callable<T>
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedCallable}&lt;T&gt; in a {@link Callable}&lt;T&gt;.
     * <p>
     * Example:
     * <pre><code>
     * Executors.newFixedThreadPool(1).submit(Unchecked.callable(() -&gt; {
     *     throw new Exception("Cannot execute this task");
     * })).get();
     * </code></pre>
     */
    public static <T> Callable<T> callable(CheckedCallable<T> callable) {
        return callable(callable, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedCallable}&lt;T&gt; in a {@link Callable}&lt;T&gt; with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * Executors.newFixedThreadPool(1).submit(Unchecked.callable(
     *     () -&gt; {
     *         throw new Exception("Cannot execute this task");
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * )).get();
     * </code></pre>
     */
    public static <T> Callable<T> callable(CheckedCallable<T> callable, Consumer<Throwable> handler) {
        return () -> {
            try {
                return callable.call();
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.Comparator
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedComparator} in a {@link Comparator}.
     */
    public static <T> Comparator<T> comparator(CheckedComparator<T> comparator) {
        return comparator(comparator, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedComparator} in a {@link Comparator} with a custom handler for checked exceptions.
     */
    public static <T> Comparator<T> comparator(CheckedComparator<T> comparator, Consumer<Throwable> handler) {
        return (t1, t2) -> {
            try {
                return comparator.compare(t1, t2);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.function.BiConsumers
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link org.jooq.lambda.fi.util.function.CheckedBiConsumer} in a {@link BiConsumer}.
     * <p>
     * Example:
     * <pre><code>
     * map.forEach(Unchecked.biConsumer((k, v) -&gt; {
     *     if (k == null || v == null)
     *         throw new Exception("No nulls allowed in map");
     * }));
     * </code></pre>
     */
    public static <T, U> BiConsumer<T, U> biConsumer(CheckedBiConsumer<T, U> consumer) {
        return biConsumer(consumer, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedBiConsumer} in a {@link BiConsumer} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * map.forEach(Unchecked.biConsumer(
     *     (k, v) -&gt; {
     *         if (k == null || v == null)
     *             throw new Exception("No nulls allowed in map");
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T, U> BiConsumer<T, U> biConsumer(CheckedBiConsumer<T, U> consumer, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                consumer.accept(t, u);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedObjIntConsumer} in a {@link ObjIntConsumer}.
     */
    public static <T> ObjIntConsumer<T> objIntConsumer(CheckedObjIntConsumer<T> consumer) {
        return objIntConsumer(consumer, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedObjIntConsumer} in a {@link ObjIntConsumer} with a custom handler for checked exceptions.
     */
    public static <T> ObjIntConsumer<T> objIntConsumer(CheckedObjIntConsumer<T> consumer, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                consumer.accept(t, u);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedObjLongConsumer} in a {@link ObjLongConsumer}.
     */
    public static <T> ObjLongConsumer<T> objLongConsumer(CheckedObjLongConsumer<T> consumer) {
        return objLongConsumer(consumer, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedObjLongConsumer} in a {@link ObjLongConsumer} with a custom handler for checked exceptions.
     */
    public static <T> ObjLongConsumer<T> objLongConsumer(CheckedObjLongConsumer<T> consumer, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                consumer.accept(t, u);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedObjDoubleConsumer} in a {@link ObjDoubleConsumer}.
     */
    public static <T> ObjDoubleConsumer<T> objDoubleConsumer(CheckedObjDoubleConsumer<T> consumer) {
        return objDoubleConsumer(consumer, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedObjDoubleConsumer} in a {@link ObjDoubleConsumer} with a custom handler for checked exceptions.
     */
    public static <T> ObjDoubleConsumer<T> objDoubleConsumer(CheckedObjDoubleConsumer<T> consumer, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                consumer.accept(t, u);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.function.BiFunctions
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link org.jooq.lambda.fi.util.function.CheckedBiFunction} in a {@link BiFunction}.
     * <p>
     * Example:
     * <pre><code>
     * map.computeIfPresent("key", Unchecked.biFunction((k, v) -&gt; {
     *     if (k == null || v == null)
     *         throw new Exception("No nulls allowed in map");
     *
     *     return 42;
     * }));
     * </code></pre>
     */
    public static <T, U, R> BiFunction<T, U, R> biFunction(CheckedBiFunction<T, U, R> function) {
        return biFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedBiFunction} in a {@link BiFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * map.computeIfPresent("key", Unchecked.biFunction(
     *     (k, v) -&gt; {
     *         if (k == null || v == null)
     *             throw new Exception("No nulls allowed in map");
     *
     *         return 42;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T, U, R> BiFunction<T, U, R> biFunction(CheckedBiFunction<T, U, R> function, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                return function.apply(t, u);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedToIntBiFunction} in a {@link ToIntBiFunction}.
     */
    public static <T, U> ToIntBiFunction<T, U> toIntBiFunction(CheckedToIntBiFunction<T, U> function) {
        return toIntBiFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedToIntBiFunction} in a {@link ToIntBiFunction} with a custom handler for checked exceptions.
     */
    public static <T, U> ToIntBiFunction<T, U> toIntBiFunction(CheckedToIntBiFunction<T, U> function, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                return function.applyAsInt(t, u);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedToLongBiFunction} in a {@link ToLongBiFunction}.
     */
    public static <T, U> ToLongBiFunction<T, U> toLongBiFunction(CheckedToLongBiFunction<T, U> function) {
        return toLongBiFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedToLongBiFunction} in a {@link ToLongBiFunction} with a custom handler for checked exceptions.
     */
    public static <T, U> ToLongBiFunction<T, U> toLongBiFunction(CheckedToLongBiFunction<T, U> function, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                return function.applyAsLong(t, u);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedToDoubleBiFunction} in a {@link ToDoubleBiFunction}.
     */
    public static <T, U> ToDoubleBiFunction<T, U> toDoubleBiFunction(CheckedToDoubleBiFunction<T, U> function) {
        return toDoubleBiFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedToDoubleBiFunction} in a {@link ToDoubleBiFunction} with a custom handler for checked exceptions.
     */
    public static <T, U> ToDoubleBiFunction<T, U> toDoubleBiFunction(CheckedToDoubleBiFunction<T, U> function, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                return function.applyAsDouble(t, u);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.function.BiPredicates
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link org.jooq.lambda.fi.util.function.CheckedBiPredicate} in a {@link BiPredicate}.
     */
    public static <T, U> BiPredicate<T, U> biPredicate(CheckedBiPredicate<T, U> predicate) {
        return biPredicate(predicate, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedBiPredicate} in a {@link BiPredicate} with a custom handler for checked exceptions.
     */
    public static <T, U> BiPredicate<T, U> biPredicate(CheckedBiPredicate<T, U> predicate, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                return predicate.test(t, u);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.function.BinaryOperators
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link org.jooq.lambda.fi.util.function.CheckedBinaryOperator} in a {@link BinaryOperator}.
     * <p>
     * Example:
     * <pre><code>
     * Stream.of("a", "b", "c").reduce(Unchecked.binaryOperator((s1, s2) -&gt; {
     *     if (s2.length() &gt; 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return s1 + s2;
     * }));
     * </code></pre>
     */
    public static <T> BinaryOperator<T> binaryOperator(CheckedBinaryOperator<T> operator) {
        return binaryOperator(operator, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedBinaryOperator} in a {@link BinaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * Stream.of("a", "b", "c").reduce(Unchecked.binaryOperator(
     *     (s1, s2) -&gt; {
     *         if (s2.length() &gt; 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return s1 + s2;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T> BinaryOperator<T> binaryOperator(CheckedBinaryOperator<T> operator, Consumer<Throwable> handler) {
        return (t1, t2) -> {
            try {
                return operator.apply(t1, t2);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedIntBinaryOperator} in a {@link IntBinaryOperator}.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).reduce(Unchecked.intBinaryOperator((i1, i2) -&gt; {
     *     if (i2 &lt; 0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return i1 + i2;
     * }));
     * </code></pre>
     */
    public static IntBinaryOperator intBinaryOperator(CheckedIntBinaryOperator operator) {
        return intBinaryOperator(operator, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedIntBinaryOperator} in a {@link IntBinaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).reduce(Unchecked.intBinaryOperator(
     *     (i1, i2) -&gt; {
     *         if (i2 &lt; 0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return i1 + i2;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static  IntBinaryOperator intBinaryOperator(CheckedIntBinaryOperator operator, Consumer<Throwable> handler) {
        return (i1, i2) -> {
            try {
                return operator.applyAsInt(i1, i2);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedLongBinaryOperator} in a {@link LongBinaryOperator}.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).reduce(Unchecked.longBinaryOperator((l1, l2) -&gt; {
     *     if (l2 &lt; 0L)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return l1 + l2;
     * }));
     * </code></pre>
     */
    public static LongBinaryOperator longBinaryOperator(CheckedLongBinaryOperator operator) {
        return longBinaryOperator(operator, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedLongBinaryOperator} in a {@link LongBinaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).reduce(Unchecked.longBinaryOperator(
     *     (l1, l2) -&gt; {
     *         if (l2 &lt; 0L)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return l1 + l2;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static LongBinaryOperator longBinaryOperator(CheckedLongBinaryOperator operator, Consumer<Throwable> handler) {
        return (l1, l2) -> {
            try {
                return operator.applyAsLong(l1, l2);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedDoubleBinaryOperator} in a {@link DoubleBinaryOperator}.
     * <p>
     * Example:
     * <pre><code>
     * DoubleStream.of(1.0, 2.0, 3.0).reduce(Unchecked.doubleBinaryOperator((d1, d2) -&gt; {
     *     if (d2 &lt; 0.0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return d1 + d2;
     * }));
     * </code></pre>
     */
    public static DoubleBinaryOperator doubleBinaryOperator(CheckedDoubleBinaryOperator operator) {
        return doubleBinaryOperator(operator, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedDoubleBinaryOperator} in a {@link DoubleBinaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * DoubleStream.of(1.0, 2.0, 3.0).reduce(Unchecked.doubleBinaryOperator(
     *     (d1, d2) -&gt; {
     *         if (d2 &lt; 0.0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return d1 + d2;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static DoubleBinaryOperator doubleBinaryOperator(CheckedDoubleBinaryOperator operator, Consumer<Throwable> handler) {
        return (d1, d2) -> {
            try {
                return operator.applyAsDouble(d1, d2);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.function.Consumers
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedConsumer} in a {@link Consumer}.
     * <p>
     * Example:
     * <pre><code>
     * Arrays.asList("a", "b").stream().forEach(Unchecked.consumer(s -&gt; {
     *     if (s.length() &gt; 10)
     *         throw new Exception("Only short strings allowed");
     * }));
     * </code></pre>
     */
    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer) {
        return consumer(consumer, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedConsumer} in a {@link Consumer} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * Arrays.asList("a", "b").stream().forEach(Unchecked.consumer(
     *     s -&gt; {
     *         if (s.length() &gt; 10)
     *             throw new Exception("Only short strings allowed");
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer, Consumer<Throwable> handler) {
        return t -> {
            try {
                consumer.accept(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedIntConsumer} in a {@link IntConsumer}.
     * <p>
     * Example:
     * <pre><code>
     * Arrays.stream(new int[] { 1, 2 }).forEach(Unchecked.intConsumer(i -&gt; {
     *     if (i &lt; 0)
     *         throw new Exception("Only positive numbers allowed");
     * }));
     * </code></pre>
     */
    public static IntConsumer intConsumer(CheckedIntConsumer consumer) {
        return intConsumer(consumer, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedIntConsumer} in a {@link IntConsumer} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * Arrays.stream(new int[] { 1, 2 }).forEach(Unchecked.intConsumer(
     *     i -&gt; {
     *         if (i &lt; 0)
     *             throw new Exception("Only positive numbers allowed");
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static IntConsumer intConsumer(CheckedIntConsumer consumer, Consumer<Throwable> handler) {
        return i -> {
            try {
                consumer.accept(i);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedLongConsumer} in a {@link LongConsumer}.
     * <p>
     * Example:
     * <pre><code>
     * Arrays.stream(new long[] { 1L, 2L }).forEach(Unchecked.longConsumer(l -&gt; {
     *     if (l &lt; 0)
     *         throw new Exception("Only positive numbers allowed");
     * }));
     * </code></pre>
     */
    public static LongConsumer longConsumer(CheckedLongConsumer consumer) {
        return longConsumer(consumer, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedLongConsumer} in a {@link LongConsumer} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * Arrays.stream(new long[] { 1L, 2L }).forEach(Unchecked.longConsumer(
     *     l -&gt; {
     *         if (l &lt; 0)
     *             throw new Exception("Only positive numbers allowed");
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static LongConsumer longConsumer(CheckedLongConsumer consumer, Consumer<Throwable> handler) {
        return l -> {
            try {
                consumer.accept(l);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedDoubleConsumer} in a {@link DoubleConsumer}.
     * <p>
     * Example:
     * <pre><code>
     * Arrays.stream(new double[] { 1.0, 2.0 }).forEach(Unchecked.doubleConsumer(d -&gt; {
     *     if (d &lt; 0.0)
     *         throw new Exception("Only positive numbers allowed");
     * }));
     * </code></pre>
     */
    public static DoubleConsumer doubleConsumer(CheckedDoubleConsumer consumer) {
        return doubleConsumer(consumer, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedDoubleConsumer} in a {@link DoubleConsumer} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * Arrays.stream(new double[] { 1.0, 2.0 }).forEach(Unchecked.doubleConsumer(
     *     d -&gt; {
     *         if (d &lt; 0.0)
     *             throw new Exception("Only positive numbers allowed");
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static DoubleConsumer doubleConsumer(CheckedDoubleConsumer consumer, Consumer<Throwable> handler) {
        return d -> {
            try {
                consumer.accept(d);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.function.Functions
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedFunction} in a {@link Function}.
     * <p>
     * Example:
     * <pre><code>
     * map.computeIfAbsent("key", Unchecked.function(k -&gt; {
     *     if (k.length() &gt; 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return 42;
     * }));
     * </code></pre>
     */
    public static <T, R> Function<T, R> function(CheckedFunction<T, R> function) {
        return function(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedFunction} in a {@link Function} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * map.forEach(Unchecked.function(
     *     k -&gt; {
     *         if (k.length() &gt; 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return 42;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T, R> Function<T, R> function(CheckedFunction<T, R> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.apply(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedToIntFunction} in a {@link ToIntFunction}.
     * <p>
     * Example:
     * <pre><code>
     * map.computeIfAbsent("key", Unchecked.toIntFunction(k -&gt; {
     *     if (k.length() &gt; 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return 42;
     * }));
     * </code></pre>
     */
    public static <T> ToIntFunction<T> toIntFunction(CheckedToIntFunction<T> function) {
        return toIntFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedToIntFunction} in a {@link ToIntFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * map.forEach(Unchecked.toIntFunction(
     *     k -&gt; {
     *         if (k.length() &gt; 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return 42;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T> ToIntFunction<T> toIntFunction(CheckedToIntFunction<T> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.applyAsInt(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedToLongFunction} in a {@link ToLongFunction}.
     * <p>
     * Example:
     * <pre><code>
     * map.computeIfAbsent("key", Unchecked.toLongFunction(k -&gt; {
     *     if (k.length() &gt; 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return 42L;
     * }));
     * </code></pre>
     */
    public static <T> ToLongFunction<T> toLongFunction(CheckedToLongFunction<T> function) {
        return toLongFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedToLongFunction} in a {@link ToLongFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * map.forEach(Unchecked.toLongFunction(
     *     k -&gt; {
     *         if (k.length() &gt; 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return 42L;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T> ToLongFunction<T> toLongFunction(CheckedToLongFunction<T> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.applyAsLong(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedToDoubleFunction} in a {@link ToDoubleFunction}.
     * <p>
     * Example:
     * <pre><code>
     * map.computeIfAbsent("key", Unchecked.toDoubleFunction(k -&gt; {
     *     if (k.length() &gt; 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return 42.0;
     * }));
     * </code></pre>
     */
    public static <T> ToDoubleFunction<T> toDoubleFunction(CheckedToDoubleFunction<T> function) {
        return toDoubleFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedToDoubleFunction} in a {@link ToDoubleFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * map.forEach(Unchecked.toDoubleFunction(
     *     k -&gt; {
     *         if (k.length() &gt; 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return 42.0;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T> ToDoubleFunction<T> toDoubleFunction(CheckedToDoubleFunction<T> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.applyAsDouble(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedIntFunction} in a {@link IntFunction}.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).mapToObj(Unchecked.intFunction(i -&gt; {
     *     if (i &lt; 0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return "" + i;
     * });
     * </code></pre>
     */
    public static <R> IntFunction<R> intFunction(CheckedIntFunction<R> function) {
        return intFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedIntFunction} in a {@link IntFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).mapToObj(Unchecked.intFunction(
     *     i -&gt; {
     *         if (i &lt; 0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return "" + i;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <R> IntFunction<R> intFunction(CheckedIntFunction<R> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.apply(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedIntToLongFunction} in a {@link IntToLongFunction}.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).mapToLong(Unchecked.intToLongFunction(i -&gt; {
     *     if (i &lt; 0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return (long) i;
     * });
     * </code></pre>
     */
    public static IntToLongFunction intToLongFunction(CheckedIntToLongFunction function) {
        return intToLongFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedIntToLongFunction} in a {@link IntToLongFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).mapToLong(Unchecked.intToLongFunction(
     *     i -&gt; {
     *         if (i &lt; 0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return (long) i;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static IntToLongFunction intToLongFunction(CheckedIntToLongFunction function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.applyAsLong(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedIntToDoubleFunction} in a {@link IntToDoubleFunction}.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).mapToDouble(Unchecked.intToDoubleFunction(i -&gt; {
     *     if (i &lt; 0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return (double) i;
     * });
     * </code></pre>
     */
    public static IntToDoubleFunction intToDoubleFunction(CheckedIntToDoubleFunction function) {
        return intToDoubleFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedIntToDoubleFunction} in a {@link IntToDoubleFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).mapToDouble(Unchecked.intToDoubleFunction(
     *     i -&gt; {
     *         if (i &lt; 0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return (double) i;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static IntToDoubleFunction intToDoubleFunction(CheckedIntToDoubleFunction function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.applyAsDouble(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedLongFunction} in a {@link LongFunction}.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).mapToObj(Unchecked.longFunction(l -&gt; {
     *     if (l &lt; 0L)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return "" + l;
     * });
     * </code></pre>
     */
    public static <R> LongFunction<R> longFunction(CheckedLongFunction<R> function) {
        return longFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedLongFunction} in a {@link LongFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).mapToObj(Unchecked.longFunction(
     *     l -&gt; {
     *         if (l &lt; 0L)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return "" + l;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <R> LongFunction<R> longFunction(CheckedLongFunction<R> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.apply(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedLongToIntFunction} in a {@link LongToIntFunction}.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).mapToInt(Unchecked.longToIntFunction(l -&gt; {
     *     if (l &lt; 0L)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return (int) l;
     * });
     * </code></pre>
     */
    public static LongToIntFunction longToIntFunction(CheckedLongToIntFunction function) {
        return longToIntFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedLongToIntFunction} in a {@link LongToIntFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).mapToInt(Unchecked.longToIntFunction(
     *     l -&gt; {
     *         if (l &lt; 0L)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return (int) l;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static LongToIntFunction longToIntFunction(CheckedLongToIntFunction function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.applyAsInt(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedLongToDoubleFunction} in a {@link LongToDoubleFunction}.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).mapToInt(Unchecked.longToDoubleFunction(l -&gt; {
     *     if (l &lt; 0L)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return (double) l;
     * });
     * </code></pre>
     */
    public static LongToDoubleFunction longToDoubleFunction(CheckedLongToDoubleFunction function) {
        return longToDoubleFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedLongToDoubleFunction} in a {@link LongToDoubleFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).mapToInt(Unchecked.longToDoubleFunction(
     *     l -&gt; {
     *         if (l &lt; 0L)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return (double) l;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static LongToDoubleFunction longToDoubleFunction(CheckedLongToDoubleFunction function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.applyAsDouble(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedDoubleFunction} in a {@link DoubleFunction}.
     * <p>
     * Example:
     * <pre><code>
     * DoubleStream.of(1.0, 2.0, 3.0).mapToObj(Unchecked.doubleFunction(d -&gt; {
     *     if (d &lt; 0.0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return "" + d;
     * });
     * </code></pre>
     */
    public static <R> DoubleFunction<R> doubleFunction(CheckedDoubleFunction<R> function) {
        return doubleFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedDoubleFunction} in a {@link DoubleFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * DoubleStream.of(1.0, 2.0, 3.0).mapToObj(Unchecked.doubleFunction(
     *     d -&gt; {
     *         if (d &lt; 0.0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return "" + d;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <R> DoubleFunction<R> doubleFunction(CheckedDoubleFunction<R> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.apply(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedDoubleToIntFunction} in a {@link DoubleToIntFunction}.
     * <p>
     * Example:
     * <pre><code>
     * DoubleStream.of(1.0, 2.0, 3.0).mapToInt(Unchecked.doubleToIntFunction(d -&gt; {
     *     if (d &lt; 0.0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return (int) d;
     * });
     * </code></pre>
     */
    public static DoubleToIntFunction doubleToIntFunction(CheckedDoubleToIntFunction function) {
        return doubleToIntFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedDoubleToIntFunction} in a {@link DoubleToIntFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * DoubleStream.of(1.0, 2.0, 3.0).mapToInt(Unchecked.doubleToIntFunction(
     *     d -&gt; {
     *         if (d &lt; 0.0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return (int) d;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static DoubleToIntFunction doubleToIntFunction(CheckedDoubleToIntFunction function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.applyAsInt(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedDoubleToLongFunction} in a {@link DoubleToLongFunction}.
     * <p>
     * Example:
     * <pre><code>
     * DoubleStream.of(1.0, 2.0, 3.0).mapToLong(Unchecked.doubleToLongFunction(d -&gt; {
     *     if (d &lt; 0.0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return (long) d;
     * });
     * </code></pre>
     */
    public static DoubleToLongFunction doubleToLongFunction(CheckedDoubleToLongFunction function) {
        return doubleToLongFunction(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedDoubleToLongFunction} in a {@link DoubleToLongFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * DoubleStream.of(1.0, 2.0, 3.0).mapToLong(Unchecked.doubleToLongFunction(
     *     d -&gt; {
     *         if (d &lt; 0.0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return (long) d;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static DoubleToLongFunction doubleToLongFunction(CheckedDoubleToLongFunction function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.applyAsLong(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.function.Predicates
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedPredicate} in a {@link Predicate}.
     * <p>
     * Example:
     * <pre><code>
     * Stream.of("a", "b", "c").filter(Unchecked.predicate(s -&gt; {
     *     if (s.length() &gt; 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return true;
     * }));
     * </code></pre>
     */
    public static <T> Predicate<T> predicate(CheckedPredicate<T> predicate) {
        return predicate(predicate, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedPredicate} in a {@link Predicate} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * Stream.of("a", "b", "c").filter(Unchecked.predicate(
     *     s -&gt; {
     *         if (s.length() &gt; 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return true;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T> Predicate<T> predicate(CheckedPredicate<T> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.test(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedPredicate} in a {@link IntPredicate}.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).filter(Unchecked.intPredicate(i -&gt; {
     *     if (i &lt; 0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return true;
     * }));
     * </code></pre>
     */
    public static IntPredicate intPredicate(CheckedIntPredicate predicate) {
        return intPredicate(predicate, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedPredicate} in a {@link IntPredicate} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).filter(Unchecked.intPredicate(
     *     i -&gt; {
     *         if (i &lt; 0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return true;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static IntPredicate intPredicate(CheckedIntPredicate function, Consumer<Throwable> handler) {
        return i -> {
            try {
                return function.test(i);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedLongPredicate} in a {@link LongPredicate}.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).filter(Unchecked.longPredicate(l -&gt; {
     *     if (l &lt; 0L)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return true;
     * }));
     * </code></pre>
     */
    public static LongPredicate longPredicate(CheckedLongPredicate predicate) {
        return longPredicate(predicate, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedLongPredicate} in a {@link LongPredicate} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).filter(Unchecked.longPredicate(
     *     l -&gt; {
     *         if (l &lt; 0L)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return true;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static LongPredicate longPredicate(CheckedLongPredicate function, Consumer<Throwable> handler) {
        return l -> {
            try {
                return function.test(l);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedDoublePredicate} in a {@link DoublePredicate}.
     * <p>
     * Example:
     * <pre><code>
     * DoubleStream.of(1.0, 2.0, 3.0).filter(Unchecked.doublePredicate(d -&gt; {
     *     if (d &lt; 0.0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return true;
     * }));
     * </code></pre>
     */
    public static DoublePredicate doublePredicate(CheckedDoublePredicate predicate) {
        return doublePredicate(predicate, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedDoublePredicate} in a {@link DoublePredicate} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * DoubleStream.of(1.0, 2.0, 3.0).filter(Unchecked.doublePredicate(
     *     d -&gt; {
     *         if (d &lt; 0.0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return true;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static DoublePredicate doublePredicate(CheckedDoublePredicate function, Consumer<Throwable> handler) {
        return d -> {
            try {
                return function.test(d);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.function.Suppliers
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedSupplier} in a {@link Supplier}.
     * <p>
     * Example:
     * <pre><code>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.supplier(() -&gt; rs.getObject(1)));
     * </code></pre>
     */
    public static <T> Supplier<T> supplier(CheckedSupplier<T> supplier) {
        return supplier(supplier, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedSupplier} in a {@link Supplier} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * ResultSet rs = statement.executeQuery();
     *
     * Stream.generate(Unchecked.supplier(
     *     () -&gt; rs.getObject(1),
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T> Supplier<T> supplier(CheckedSupplier<T> supplier, Consumer<Throwable> handler) {
        return () -> {
            try {
                return supplier.get();
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedIntSupplier} in a {@link IntSupplier}.
     * <p>
     * Example:
     * <pre><code>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.intSupplier(() -&gt; rs.getInt(1)));
     * </code></pre>
     */
    public static IntSupplier intSupplier(CheckedIntSupplier supplier) {
        return intSupplier(supplier, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedIntSupplier} in a {@link IntSupplier} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * ResultSet rs = statement.executeQuery();
     *
     * Stream.generate(Unchecked.intSupplier(
     *     () -&gt; rs.getInt(1),
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static IntSupplier intSupplier(CheckedIntSupplier supplier, Consumer<Throwable> handler) {
        return () -> {
            try {
                return supplier.getAsInt();
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedLongSupplier} in a {@link LongSupplier}.
     * <p>
     * Example:
     * <pre><code>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.longSupplier(() -&gt; rs.getLong(1)));
     * </code></pre>
     */
    public static LongSupplier longSupplier(CheckedLongSupplier supplier) {
        return longSupplier(supplier, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedLongSupplier} in a {@link LongSupplier} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * ResultSet rs = statement.executeQuery();
     *
     * Stream.generate(Unchecked.longSupplier(
     *     () -&gt; rs.getLong(1),
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static LongSupplier longSupplier(CheckedLongSupplier supplier, Consumer<Throwable> handler) {
        return () -> {
            try {
                return supplier.getAsLong();
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedDoubleSupplier} in a {@link DoubleSupplier}.
     * <p>
     * Example:
     * <pre><code>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.doubleSupplier(() -&gt; rs.getDouble(1)));
     * </code></pre>
     */
    public static DoubleSupplier doubleSupplier(CheckedDoubleSupplier supplier) {
        return doubleSupplier(supplier, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedDoubleSupplier} in a {@link DoubleSupplier} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * ResultSet rs = statement.executeQuery();
     *
     * Stream.generate(Unchecked.doubleSupplier(
     *     () -&gt; rs.getDouble(1),
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static DoubleSupplier doubleSupplier(CheckedDoubleSupplier supplier, Consumer<Throwable> handler) {
        return () -> {
            try {
                return supplier.getAsDouble();
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link org.jooq.lambda.fi.util.function.CheckedBooleanSupplier} in a {@link BooleanSupplier}.
     * <p>
     * Example:
     * <pre><code>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.booleanSupplier(() -&gt; rs.getBoolean(1)));
     * </code></pre>
     */
    public static BooleanSupplier booleanSupplier(CheckedBooleanSupplier supplier) {
        return booleanSupplier(supplier, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedBooleanSupplier} in a {@link BooleanSupplier} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * ResultSet rs = statement.executeQuery();
     *
     * Stream.generate(Unchecked.booleanSupplier(
     *     () -&gt; rs.getBoolean(1),
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static BooleanSupplier booleanSupplier(CheckedBooleanSupplier supplier, Consumer<Throwable> handler) {
        return () -> {
            try {
                return supplier.getAsBoolean();
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.function.UnaryOperators
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedUnaryOperator} in a {@link UnaryOperator}.
     * <p>
     * Example:
     * <pre><code>
     * Stream.of("a", "b", "c").map(Unchecked.unaryOperator(s -&gt; {
     *     if (s.length() &gt; 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return s;
     * }));
     * </code></pre>
     */
    public static <T> UnaryOperator<T> unaryOperator(CheckedUnaryOperator<T> operator) {
        return unaryOperator(operator, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedUnaryOperator} in a {@link UnaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * Stream.of("a", "b", "c").map(Unchecked.unaryOperator(
     *     s -&gt; {
     *         if (s.length() &gt; 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return s;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static <T> UnaryOperator<T> unaryOperator(CheckedUnaryOperator<T> operator, Consumer<Throwable> handler) {
        return t -> {
            try {
                return operator.apply(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedIntUnaryOperator} in a {@link IntUnaryOperator}.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).map(Unchecked.intUnaryOperator(i -&gt; {
     *     if (i &lt; 0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return i;
     * }));
     * </code></pre>
     */
    public static IntUnaryOperator intUnaryOperator(CheckedIntUnaryOperator operator) {
        return intUnaryOperator(operator, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedIntUnaryOperator} in a {@link IntUnaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * IntStream.of(1, 2, 3).map(Unchecked.intUnaryOperator(
     *     i -&gt; {
     *         if (i &lt; 0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return i;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static IntUnaryOperator intUnaryOperator(CheckedIntUnaryOperator operator, Consumer<Throwable> handler) {
        return t -> {
            try {
                return operator.applyAsInt(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedLongUnaryOperator} in a {@link LongUnaryOperator}.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).map(Unchecked.longUnaryOperator(l -&gt; {
     *     if (l &lt; 0L)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return l;
     * }));
     * </code></pre>
     */
    public static LongUnaryOperator longUnaryOperator(CheckedLongUnaryOperator operator) {
        return longUnaryOperator(operator, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedLongUnaryOperator} in a {@link LongUnaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1L, 2L, 3L).map(Unchecked.longUnaryOperator(
     *     l -&gt; {
     *         if (l &lt; 0L)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return l;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static LongUnaryOperator longUnaryOperator(CheckedLongUnaryOperator operator, Consumer<Throwable> handler) {
        return t -> {
            try {
                return operator.applyAsLong(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedDoubleUnaryOperator} in a {@link DoubleUnaryOperator}.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1.0, 2.0, 3.0).map(Unchecked.doubleUnaryOperator(d -&gt; {
     *     if (d &lt; 0.0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return d;
     * }));
     * </code></pre>
     */
    public static DoubleUnaryOperator doubleUnaryOperator(CheckedDoubleUnaryOperator operator) {
        return doubleUnaryOperator(operator, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    /**
     * Wrap a {@link CheckedDoubleUnaryOperator} in a {@link DoubleUnaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <pre><code>
     * LongStream.of(1.0, 2.0, 3.0).map(Unchecked.doubleUnaryOperator(
     *     d -&gt; {
     *         if (d &lt; 0.0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return d;
     *     },
     *     e -&gt; {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </code></pre>
     */
    public static DoubleUnaryOperator doubleUnaryOperator(CheckedDoubleUnaryOperator operator, Consumer<Throwable> handler) {
        return t -> {
            try {
                return operator.applyAsDouble(t);
            }
            catch (Throwable e) {
                handler.accept(e);

                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    /**
     * No instances
     */
    private Unchecked() {}
}