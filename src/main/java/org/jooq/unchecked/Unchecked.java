/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.unchecked;


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
    public static final Consumer<Throwable> CHECKED_CONSUMER = t -> {
        throw new RuntimeException(t);
    };

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for BiConsumers
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedBiConsumer} in a {@link BiConsumer}.
     * <p>
     * Example:
     * <code><pre>
     * map.forEach(Unchecked.biConsumer((k, v) -> {
     *     if (k == null || v == null)
     *         throw new Exception("No nulls allowed in map");
     * }));
     * </pre></code>
     */
    public static <T, U> BiConsumer<T, U> biConsumer(CheckedBiConsumer<T, U> consumer) {
        return biConsumer(consumer, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedConsumer} in a {@link Consumer} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * map.forEach(Unchecked.biConsumer(
     *     (k, v) -> {
     *         if (k == null || v == null)
     *             throw new Exception("No nulls allowed in map");
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
     */
    public static <T, U> BiConsumer<T, U> biConsumer(CheckedBiConsumer<T, U> consumer, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                consumer.accept(t, u);
            }
            catch (Throwable e) {
                handler.accept(e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for BiFunctions
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedBiFunction} in a {@link BiFunction}.
     * <p>
     * Example:
     * <code><pre>
     * map.computeIfPresent("key", Unchecked.biFunction((k, v) -> {
     *     if (k == null || v == null)
     *         throw new Exception("No nulls allowed in map");
     *
     *     return 42;
     * }));
     * </pre></code>
     */
    public static <T, U, R> BiFunction<T, U, R> biFunction(CheckedBiFunction<T, U, R> function) {
        return biFunction(function, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedBiFunction} in a {@link BiFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * map.forEach(Unchecked.biFunction(
     *     (k, v) -> {
     *         if (k == null || v == null)
     *             throw new Exception("No nulls allowed in map");
     *
     *         return 42;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for BiPredicates
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedBiPredicate} in a {@link BiPredicate}.
     */
    public static <T, U> BiPredicate<T, U> biPredicate(CheckedBiPredicate<T, U> predicate) {
        return biPredicate(predicate, CHECKED_CONSUMER);
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
    // Wrappers for BinaryOperators
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedBinaryOperator} in a {@link BinaryOperator}.
     * <p>
     * Example:
     * <code><pre>
     * Stream.of("a", "b", "c").reduce(Unchecked.binaryOperator((s1, s2) -> {
     *     if (s2.length() > 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return s1 + s2;
     * }));
     * </pre></code>
     */
    public static <T> BinaryOperator<T> binaryOperator(CheckedBinaryOperator<T> operator) {
        return binaryOperator(operator, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedBinaryOperator} in a {@link BinaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * Stream.of("a", "b", "c").reduce(Unchecked.binaryOperator(
     *     (s1, s2) -> {
     *         if (s2.length() > 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return s1 + s2;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * <code><pre>
     * IntStream.of(1, 2, 3).reduce(Unchecked.intBinaryOperator((i1, i2) -> {
     *     if (i2 < 0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return i1 + i2;
     * }));
     * </pre></code>
     */
    public static IntBinaryOperator intBinaryOperator(CheckedIntBinaryOperator operator) {
        return intBinaryOperator(operator, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedIntBinaryOperator} in a {@link IntBinaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * IntStream.of(1, 2, 3).reduce(Unchecked.intBinaryOperator(
     *     (i1, i2) -> {
     *         if (i2 < 0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return i1 + i2;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * <code><pre>
     * LongStream.of(1L, 2L, 3L).reduce(Unchecked.longBinaryOperator((l1, l2) -> {
     *     if (l2 < 0L)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return l1 + l2;
     * }));
     * </pre></code>
     */
    public static LongBinaryOperator longBinaryOperator(CheckedLongBinaryOperator operator) {
        return longBinaryOperator(operator, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedLongBinaryOperator} in a {@link LongBinaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * LongStream.of(1L, 2L, 3L).reduce(Unchecked.longBinaryOperator(
     *     (l1, l2) -> {
     *         if (l2 < 0L)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return l1 + l2;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * <code><pre>
     * DoubleStream.of(1.0, 2.0, 3.0).reduce(Unchecked.doubleBinaryOperator((d1, d2) -> {
     *     if (d2 < 0.0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return d1 + d2;
     * }));
     * </pre></code>
     */
    public static DoubleBinaryOperator doubleBinaryOperator(CheckedDoubleBinaryOperator operator) {
        return doubleBinaryOperator(operator, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedDoubleBinaryOperator} in a {@link DoubleBinaryOperator} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * DoubleStream.of(1.0, 2.0, 3.0).reduce(Unchecked.doubleBinaryOperator(
     *     (d1, d2) -> {
     *         if (d2 < 0.0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return d1 + d2;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
    // Wrappers for Consumers
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedConsumer} in a {@link Consumer}.
     * <p>
     * Example:
     * <code><pre>
     * Arrays.asList("a", "b").stream().forEach(Unchecked.consumer(s -> {
     *     if (s.length() > 10)
     *         throw new Exception("Only short strings allowed");
     * }));
     * </pre></code>
     */
    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer) {
        return consumer(consumer, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedConsumer} in a {@link Consumer} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * Arrays.asList("a", "b").stream().forEach(Unchecked.consumer(
     *     s -> {
     *         if (s.length() > 10)
     *             throw new Exception("Only short strings allowed");
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
     */
    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer, Consumer<Throwable> handler) {
        return t -> {
            try {
                consumer.accept(t);
            }
            catch (Throwable e) {
                handler.accept(e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedIntConsumer} in a {@link IntConsumer}.
     * <p>
     * Example:
     * <code><pre>
     * Arrays.stream(new int[] { 1, 2 }).forEach(Unchecked.intConsumer(i -> {
     *     if (i < 0)
     *         throw new Exception("Only positive numbers allowed");
     * }));
     * </pre></code>
     */
    public static IntConsumer intConsumer(CheckedIntConsumer consumer) {
        return intConsumer(consumer, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedIntConsumer} in a {@link IntConsumer} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * Arrays.stream(new int[] { 1, 2 }).forEach(Unchecked.intConsumer(
     *     i -> {
     *         if (i < 0)
     *             throw new Exception("Only positive numbers allowed");
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
     */
    public static IntConsumer intConsumer(CheckedIntConsumer consumer, Consumer<Throwable> handler) {
        return i -> {
            try {
                consumer.accept(i);
            }
            catch (Throwable e) {
                handler.accept(e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedLongConsumer} in a {@link LongConsumer}.
     * <p>
     * Example:
     * <code><pre>
     * Arrays.stream(new long[] { 1L, 2L }).forEach(Unchecked.longConsumer(l -> {
     *     if (l < 0)
     *         throw new Exception("Only positive numbers allowed");
     * }));
     * </pre></code>
     */
    public static LongConsumer longConsumer(CheckedLongConsumer consumer) {
        return longConsumer(consumer, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedLongConsumer} in a {@link LongConsumer} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * Arrays.stream(new long[] { 1L, 2L }).forEach(Unchecked.longConsumer(
     *     l -> {
     *         if (l < 0)
     *             throw new Exception("Only positive numbers allowed");
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
     */
    public static LongConsumer longConsumer(CheckedLongConsumer consumer, Consumer<Throwable> handler) {
        return l -> {
            try {
                consumer.accept(l);
            }
            catch (Throwable e) {
                handler.accept(e);
            }
        };
    }

    /**
     * Wrap a {@link CheckedDoubleConsumer} in a {@link DoubleConsumer}.
     * <p>
     * Example:
     * <code><pre>
     * Arrays.stream(new double[] { 1.0, 2.0 }).forEach(Unchecked.doubleConsumer(d -> {
     *     if (d < 0.0)
     *         throw new Exception("Only positive numbers allowed");
     * }));
     * </pre></code>
     */
    public static DoubleConsumer doubleConsumer(CheckedDoubleConsumer consumer) {
        return doubleConsumer(consumer, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedDoubleConsumer} in a {@link DoubleConsumer} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * Arrays.stream(new double[] { 1.0, 2.0 }).forEach(Unchecked.doubleConsumer(
     *     d -> {
     *         if (d < 0.0)
     *             throw new Exception("Only positive numbers allowed");
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
     */
    public static DoubleConsumer doubleConsumer(CheckedDoubleConsumer consumer, Consumer<Throwable> handler) {
        return d -> {
            try {
                consumer.accept(d);
            }
            catch (Throwable e) {
                handler.accept(e);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for Functions
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedFunction} in a {@link Function}.
     * <p>
     * Example:
     * <code><pre>
     * map.computeIfAbsent("key", Unchecked.function(k -> {
     *     if (k.length() > 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return 42;
     * }));
     * </pre></code>
     */
    public static <T, R> Function<T, R> function(CheckedFunction<T, R> function) {
        return function(function, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedFunction} in a {@link Function} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * map.forEach(Unchecked.function(
     *     k -> {
     *         if (k.length() > 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return 42;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * Wrap a {@link CheckedIntFunction} in a {@link IntFunction}.
     * <p>
     * Example:
     * <code><pre>
     * IntStream.of(1, 2, 3).mapToObj(Unchecked.intFunction(i -> {
     *     if (i < 0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return "" + i;
     * });
     * </pre></code>
     */
    public static <R> IntFunction<R> intFunction(CheckedIntFunction<R> function) {
        return intFunction(function, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedIntFunction} in a {@link IntFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * IntStream.of(1, 2, 3).mapToObj(Unchecked.intFunction(
     *     i -> {
     *         if (i < 0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return "" + i;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * Wrap a {@link CheckedLongFunction} in a {@link LongFunction}.
     * <p>
     * Example:
     * <code><pre>
     * LongStream.of(1L, 2L, 3L).mapToObj(Unchecked.longFunction(l -> {
     *     if (l < 0L)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return "" + l;
     * });
     * </pre></code>
     */
    public static <R> LongFunction<R> longFunction(CheckedLongFunction<R> function) {
        return longFunction(function, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedLongFunction} in a {@link LongFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * LongStream.of(1L, 2L, 3L).mapToObj(Unchecked.longFunction(
     *     l -> {
     *         if (l < 0L)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return "" + l;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * Wrap a {@link CheckedDoubleFunction} in a {@link DoubleFunction}.
     * <p>
     * Example:
     * <code><pre>
     * DoubleStream.of(1.0, 2.0, 3.0).mapToObj(Unchecked.doubleFunction(d -> {
     *     if (d < 0.0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return "" + d;
     * });
     * </pre></code>
     */
    public static <R> DoubleFunction<R> doubleFunction(CheckedDoubleFunction<R> function) {
        return doubleFunction(function, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedDoubleFunction} in a {@link DoubleFunction} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * DoubleStream.of(1.0, 2.0, 3.0).mapToObj(Unchecked.doubleFunction(
     *     d -> {
     *         if (d < 0.0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return "" + d;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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

    // -----------------------------------------------------------------------------------------------------------------
    // Wrappers for Predicates
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedPredicate} in a {@link Predicate}.
     * <p>
     * Example:
     * <code><pre>
     * Stream.of("a", "b", "c").filter(Unchecked.predicate(s -> {
     *     if (s.length() > 10)
     *         throw new Exception("Only short strings allowed");
     *
     *     return true;
     * }));
     * </pre></code>
     */
    public static <T> Predicate<T> predicate(CheckedPredicate<T> predicate) {
        return predicate(predicate, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedPredicate} in a {@link Predicate} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * Stream.of("a", "b", "c").filter(Unchecked.predicate(
     *     s -> {
     *         if (s.length() > 10)
     *             throw new Exception("Only short strings allowed");
     *
     *         return true;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * <code><pre>
     * IntStream.of(1, 2, 3).filter(Unchecked.intPredicate(i -> {
     *     if (i < 0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return true;
     * }));
     * </pre></code>
     */
    public static IntPredicate intPredicate(CheckedIntPredicate predicate) {
        return intPredicate(predicate, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedPredicate} in a {@link IntPredicate} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * IntStream.of(1, 2, 3).filter(Unchecked.intPredicate(
     *     i -> {
     *         if (i < 0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return true;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * <code><pre>
     * LongStream.of(1L, 2L, 3L).filter(Unchecked.longPredicate(l -> {
     *     if (l < 0L)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return true;
     * }));
     * </pre></code>
     */
    public static LongPredicate longPredicate(CheckedLongPredicate predicate) {
        return longPredicate(predicate, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedLongPredicate} in a {@link LongPredicate} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * LongStream.of(1L, 2L, 3L).filter(Unchecked.longPredicate(
     *     l -> {
     *         if (l < 0L)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return true;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * <code><pre>
     * DoubleStream.of(1.0, 2.0, 3.0).filter(Unchecked.doublePredicate(d -> {
     *     if (d < 0.0)
     *         throw new Exception("Only positive numbers allowed");
     *
     *     return true;
     * }));
     * </pre></code>
     */
    public static DoublePredicate doublePredicate(CheckedDoublePredicate predicate) {
        return doublePredicate(predicate, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedDoublePredicate} in a {@link DoublePredicate} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * DoubleStream.of(1.0, 2.0, 3.0).filter(Unchecked.doublePredicate(
     *     d -> {
     *         if (d < 0.0)
     *             throw new Exception("Only positive numbers allowed");
     *
     *         return true;
     *     },
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
    // Wrappers for Suppliers
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Wrap a {@link CheckedSupplier} in a {@link Supplier}.
     * <p>
     * Example:
     * <code><pre>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.supplier(() -> rs.getObject(1)));
     * </pre></code>
     */
    public static <T> Supplier<T> supplier(CheckedSupplier<T> supplier) {
        return supplier(supplier, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedSupplier} in a {@link Supplier} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * ResultSet rs = statement.executeQuery();
     *
     * Stream.generate(Unchecked.supplier(
     *     () -> rs.getObject(1),
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * <code><pre>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.intSupplier(() -> rs.getInt(1)));
     * </pre></code>
     */
    public static IntSupplier intSupplier(CheckedIntSupplier supplier) {
        return intSupplier(supplier, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedIntSupplier} in a {@link IntSupplier} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * ResultSet rs = statement.executeQuery();
     *
     * Stream.generate(Unchecked.intSupplier(
     *     () -> rs.getInt(1),
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * <code><pre>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.longSupplier(() -> rs.getLong(1)));
     * </pre></code>
     */
    public static LongSupplier longSupplier(CheckedLongSupplier supplier) {
        return longSupplier(supplier, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedLongSupplier} in a {@link LongSupplier} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * ResultSet rs = statement.executeQuery();
     *
     * Stream.generate(Unchecked.longSupplier(
     *     () -> rs.getLong(1),
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * <code><pre>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.doubleSupplier(() -> rs.getDouble(1)));
     * </pre></code>
     */
    public static DoubleSupplier doubleSupplier(CheckedDoubleSupplier supplier) {
        return doubleSupplier(supplier, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedDoubleSupplier} in a {@link DoubleSupplier} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * ResultSet rs = statement.executeQuery();
     *
     * Stream.generate(Unchecked.doubleSupplier(
     *     () -> rs.getDouble(1),
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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
     * Wrap a {@link CheckedBooleanSupplier} in a {@link BooleanSupplier}.
     * <p>
     * Example:
     * <code><pre>
     * ResultSet rs = statement.executeQuery();
     * Stream.generate(Unchecked.booleanSupplier(() -> rs.getBoolean(1)));
     * </pre></code>
     */
    public static BooleanSupplier booleanSupplier(CheckedBooleanSupplier supplier) {
        return booleanSupplier(supplier, CHECKED_CONSUMER);
    }

    /**
     * Wrap a {@link CheckedBooleanSupplier} in a {@link BooleanSupplier} with a custom handler for checked exceptions.
     * <p>
     * Example:
     * <code><pre>
     * ResultSet rs = statement.executeQuery();
     *
     * Stream.generate(Unchecked.booleanSupplier(
     *     () -> rs.getBoolean(1),
     *     e -> {
     *         throw new IllegalStateException(e);
     *     }
     * ));
     * </pre></code>
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

    /**
     * No instances
     */
    private Unchecked() {}
}
