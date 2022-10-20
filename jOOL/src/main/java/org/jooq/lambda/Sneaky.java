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

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.function.*;

/**
 * Improved interoperability between checked exceptions and Java 8.
 * <p>
 * Similar to {@link Unchecked}, except that {@link Unchecked#RETHROW_ALL} is
 * used as the default way to re-throw checked exceptions.
 *
 * @author Lukas Eder
 */
public final class Sneaky {

    /**
     * "sneaky-throw" a checked exception or throwable.
     */
    public static void throwChecked(Throwable t) {
        SeqUtils.sneakyThrow(t);
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
        return Unchecked.runnable(runnable, Unchecked.RETHROW_ALL);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.concurrent.Callable
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedCallable} in a {@link Callable}.
     * <p>
     * Example:
     * <pre><code>
     * Executors.newFixedThreadPool(1).submit(Unchecked.callable(() -&gt; {
     *     throw new Exception("Cannot execute this task");
     * })).get();
     * </code></pre>
     */
    public static <T> Callable<T> callable(CheckedCallable<T> callable) {
        return Unchecked.callable(callable, Unchecked.RETHROW_ALL);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.Comparator
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedComparator} in a {@link Comparator}.
     */
    public static <T> Comparator<T> comparator(CheckedComparator<T> comparator) {
        return Unchecked.comparator(comparator, Unchecked.RETHROW_ALL);
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
        return Unchecked.biConsumer(consumer, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedObjIntConsumer} in a {@link ObjIntConsumer}.
     */
    public static <T> ObjIntConsumer<T> objIntConsumer(CheckedObjIntConsumer<T> consumer) {
        return Unchecked.objIntConsumer(consumer, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedObjLongConsumer} in a {@link ObjLongConsumer}.
     */
    public static <T> ObjLongConsumer<T> objLongConsumer(CheckedObjLongConsumer<T> consumer) {
        return Unchecked.objLongConsumer(consumer, Unchecked.RETHROW_ALL);
    }
    /**
     * Wrap a {@link CheckedObjDoubleConsumer} in a {@link ObjDoubleConsumer}.
     */
    public static <T> ObjDoubleConsumer<T> objDoubleConsumer(CheckedObjDoubleConsumer<T> consumer) {
        return Unchecked.objDoubleConsumer(consumer, Unchecked.RETHROW_ALL);
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
        return Unchecked.biFunction(function, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedToIntBiFunction} in a {@link ToIntBiFunction}.
     */
    public static <T, U> ToIntBiFunction<T, U> toIntBiFunction(CheckedToIntBiFunction<T, U> function) {
        return Unchecked.toIntBiFunction(function, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedToLongBiFunction} in a {@link ToLongBiFunction}.
     */
    public static <T, U> ToLongBiFunction<T, U> toLongBiFunction(CheckedToLongBiFunction<T, U> function) {
        return Unchecked.toLongBiFunction(function, Unchecked.RETHROW_ALL);
    }

    /**
     * Wrap a {@link CheckedToDoubleBiFunction} in a {@link ToDoubleBiFunction}.
     */
    public static <T, U> ToDoubleBiFunction<T, U> toDoubleBiFunction(CheckedToDoubleBiFunction<T, U> function) {
        return Unchecked.toDoubleBiFunction(function, Unchecked.RETHROW_ALL);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for java.util.function.BiPredicates
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link org.jooq.lambda.fi.util.function.CheckedBiPredicate} in a {@link BiPredicate}.
     */
    public static <T, U> BiPredicate<T, U> biPredicate(CheckedBiPredicate<T, U> predicate) {
        return Unchecked.biPredicate(predicate, Unchecked.RETHROW_ALL);
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
        return Unchecked.binaryOperator(operator, Unchecked.RETHROW_ALL);
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
        return Unchecked.intBinaryOperator(operator, Unchecked.RETHROW_ALL);
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
        return Unchecked.longBinaryOperator(operator, Unchecked.RETHROW_ALL);
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
        return Unchecked.doubleBinaryOperator(operator, Unchecked.RETHROW_ALL);
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
        return Unchecked.consumer(consumer, Unchecked.RETHROW_ALL);
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
        return Unchecked.intConsumer(consumer, Unchecked.RETHROW_ALL);
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
        return Unchecked.longConsumer(consumer, Unchecked.RETHROW_ALL);
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
        return Unchecked.doubleConsumer(consumer, Unchecked.RETHROW_ALL);
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
        return Unchecked.function(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.toIntFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.toLongFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.toDoubleFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.intFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.intToLongFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.intToDoubleFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.longFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.longToIntFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.longToDoubleFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.doubleFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.doubleToIntFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.doubleToLongFunction(function, Unchecked.RETHROW_ALL);
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
        return Unchecked.predicate(predicate, Unchecked.RETHROW_ALL);
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
        return Unchecked.intPredicate(predicate, Unchecked.RETHROW_ALL);
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
        return Unchecked.longPredicate(predicate, Unchecked.RETHROW_ALL);
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
        return Unchecked.doublePredicate(predicate, Unchecked.RETHROW_ALL);
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
        return Unchecked.supplier(supplier, Unchecked.RETHROW_ALL);
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
        return Unchecked.intSupplier(supplier, Unchecked.RETHROW_ALL);
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
        return Unchecked.longSupplier(supplier, Unchecked.RETHROW_ALL);
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
        return Unchecked.doubleSupplier(supplier, Unchecked.RETHROW_ALL);
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
        return Unchecked.booleanSupplier(supplier, Unchecked.RETHROW_ALL);
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
        return Unchecked.unaryOperator(operator, Unchecked.RETHROW_ALL);
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
        return Unchecked.intUnaryOperator(operator, Unchecked.RETHROW_ALL);
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
        return Unchecked.longUnaryOperator(operator, Unchecked.RETHROW_ALL);
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
        return Unchecked.doubleUnaryOperator(operator, Unchecked.RETHROW_ALL);
    }

    /**
     * No instances
     */
    private Sneaky() {}
}