/**
 * Copyright (c) 2014-2016, Data Geekery GmbH, contact@datageekery.com
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

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static org.jooq.lambda.SeqUtils.sneakyThrow;
import static org.jooq.lambda.SeqUtils.transform;
import static org.jooq.lambda.tuple.Tuple.tuple;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Generated;

import org.jooq.lambda.function.Function10;
import org.jooq.lambda.function.Function11;
import org.jooq.lambda.function.Function12;
import org.jooq.lambda.function.Function13;
import org.jooq.lambda.function.Function14;
import org.jooq.lambda.function.Function15;
import org.jooq.lambda.function.Function16;
import org.jooq.lambda.function.Function3;
import org.jooq.lambda.function.Function4;
import org.jooq.lambda.function.Function5;
import org.jooq.lambda.function.Function6;
import org.jooq.lambda.function.Function7;
import org.jooq.lambda.function.Function8;
import org.jooq.lambda.function.Function9;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple10;
import org.jooq.lambda.tuple.Tuple11;
import org.jooq.lambda.tuple.Tuple12;
import org.jooq.lambda.tuple.Tuple13;
import org.jooq.lambda.tuple.Tuple14;
import org.jooq.lambda.tuple.Tuple15;
import org.jooq.lambda.tuple.Tuple16;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.jooq.lambda.tuple.Tuple4;
import org.jooq.lambda.tuple.Tuple5;
import org.jooq.lambda.tuple.Tuple6;
import org.jooq.lambda.tuple.Tuple7;
import org.jooq.lambda.tuple.Tuple8;
import org.jooq.lambda.tuple.Tuple9;


/**
 * A sequential, ordered {@link Stream} that adds all sorts of useful methods that work only because
 * it is sequential and ordered.
 *
 * @author Lukas Eder
 * @author Roman Tkalenko
 */
public interface Seq<T> extends Stream<T>, Iterable<T> {

    /**
     * The underlying {@link Stream} implementation.
     */
    Stream<T> stream();

    /**
     * Cross join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> crossJoin(Stream<U> other) {
        return Seq.crossJoin(this, other);
    }

    /**
     * Cross join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> crossJoin(Iterable<U> other) {
        return Seq.crossJoin(this, other);
    }

    /**
     * Cross join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> crossJoin(Seq<U> other) {
        return Seq.crossJoin(this, other);
    }

    /**
     * Inner join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, 1), tuple(2, 2))
     * Seq.of(1, 2, 3).innerJoin(Seq.of(1, 2), t -> Objects.equals(t.v1, t.v2))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> innerJoin(Stream<U> other, BiPredicate<? super T, ? super U> predicate) {
        return innerJoin(seq(other), predicate);
    }

    /**
     * Inner join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, 1), tuple(2, 2))
     * Seq.of(1, 2, 3).innerJoin(Seq.of(1, 2), t -> Objects.equals(t.v1, t.v2))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> innerJoin(Iterable<U> other, BiPredicate<? super T, ? super U> predicate) {
        return innerJoin(seq(other), predicate);
    }

    /**
     * Inner join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, 1), tuple(2, 2))
     * Seq.of(1, 2, 3).innerJoin(Seq.of(1, 2), t -> Objects.equals(t.v1, t.v2))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> innerJoin(Seq<U> other, BiPredicate<? super T, ? super U> predicate) {

        // This algorithm isn't lazy and has substantial complexity for large argument streams!
        List<U> list = other.toList();

        return flatMap(t -> seq(list)
                .filter(u -> predicate.test(t, u))
                .map(u -> tuple(t, u)));
    }

    /**
     * Left outer join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, 1), tuple(2, 2), tuple(3, null))
     * Seq.of(1, 2, 3).leftOuterJoin(Seq.of(1, 2), t -> Objects.equals(t.v1, t.v2))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> leftOuterJoin(Stream<U> other, BiPredicate<? super T, ? super U> predicate) {
        return leftOuterJoin(seq(other), predicate);
    }

    /**
     * Left outer join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, 1), tuple(2, 2), tuple(3, null))
     * Seq.of(1, 2, 3).leftOuterJoin(Seq.of(1, 2), t -> Objects.equals(t.v1, t.v2))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> leftOuterJoin(Iterable<U> other, BiPredicate<? super T, ? super U> predicate) {
        return leftOuterJoin(seq(other), predicate);
    }

    /**
     * Left outer join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, 1), tuple(2, 2), tuple(3, null))
     * Seq.of(1, 2, 3).leftOuterJoin(Seq.of(1, 2), t -> Objects.equals(t.v1, t.v2))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> leftOuterJoin(Seq<U> other, BiPredicate<? super T, ? super U> predicate) {

        // This algorithm isn't lazy and has substantial complexity for large argument streams!
        List<U> list = other.toList();

        return flatMap(t -> seq(list)
                .filter(u -> predicate.test(t, u))
                .onEmpty(null)
                .map(u -> tuple(t, u)));
    }

    /**
     * Right outer join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, 1), tuple(2, 2), tuple(null, 3))
     * Seq.of(1, 2).rightOuterJoin(Seq.of(1, 2, 3), t -> Objects.equals(t.v1, t.v2))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> rightOuterJoin(Stream<U> other, BiPredicate<? super T, ? super U> predicate) {
        return rightOuterJoin(seq(other), predicate);
    }

    /**
     * Right outer join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, 1), tuple(2, 2), tuple(null, 3))
     * Seq.of(1, 2).rightOuterJoin(Seq.of(1, 2, 3), t -> Objects.equals(t.v1, t.v2))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> rightOuterJoin(Iterable<U> other, BiPredicate<? super T, ? super U> predicate) {
        return rightOuterJoin(seq(other), predicate);
    }

    /**
     * Right outer join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, 1), tuple(2, 2), tuple(null, 3))
     * Seq.of(1, 2).rightOuterJoin(Seq.of(1, 2, 3), t -> Objects.equals(t.v1, t.v2))
     * </pre></code>
     */
    default <U> Seq<Tuple2<T, U>> rightOuterJoin(Seq<U> other, BiPredicate<? super T, ? super U> predicate) {
        return other
              .leftOuterJoin(this, (u, t) -> predicate.test(t, u))
              .map(t -> tuple(t.v2, t.v1));
    }

    /**
     * Produce this stream, or an alternative stream from the
     * <code>value</code>, in case this stream is empty.
     */
    default Seq<T> onEmpty(T value) {
        return onEmptyGet(() -> value);
    }

    /**
     * Produce this stream, or an alternative stream from the
     * <code>supplier</code>, in case this stream is empty.
     */
    default Seq<T> onEmptyGet(Supplier<T> supplier) {
        boolean[] first = { true };

        return transform(this, (delegate, action) -> {
            if (first[0]) {
                first[0] = false;

                if (!delegate.tryAdvance(action))
                    action.accept(supplier.get());

                return true;
            } else {
                return delegate.tryAdvance(action);
            }
        });
    }

    /**
     * Produce this stream, or an alternative stream from the
     * <code>supplier</code>, in case this stream is empty.
     */
    default <X extends Throwable> Seq<T> onEmptyThrow(Supplier<X> supplier) {
        boolean[] first = { true };

        return transform(this, (delegate, action) -> {
            if (first[0]) {
                first[0] = false;

                if (!delegate.tryAdvance(action))
                    sneakyThrow(supplier.get());

                return true;
            } else {
                return delegate.tryAdvance(action);
            }
        });
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).concat(Seq.of(4, 5, 6))
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    default Seq<T> concat(Stream<T> other) {
        return concat(seq(other));
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).concat(Seq.of(4, 5, 6))
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    default Seq<T> concat(Iterable<T> other) {
        return concat(seq(other));
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).concat(Seq.of(4, 5, 6))
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    @SuppressWarnings({ "unchecked" })
    default Seq<T> concat(Seq<T> other) {
        return Seq.concat(new Seq[]{this, other});
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4)
     * Seq.of(1, 2, 3).concat(4)
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    default Seq<T> concat(T other) {
        return concat(Seq.of(other));
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).concat(4, 5, 6)
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    @SuppressWarnings({ "unchecked" })
    default Seq<T> concat(T... other) {
        return concat(Seq.of(other));
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).append(Seq.of(4, 5, 6))
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    default Seq<T> append(Stream<T> other) {
        return concat(other);
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).append(Seq.of(4, 5, 6))
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    default Seq<T> append(Iterable<T> other) {
        return concat(other);
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).append(Seq.of(4, 5, 6))
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    @SuppressWarnings({ "unchecked" })
    default Seq<T> append(Seq<T> other) {
        return concat(other);
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4)
     * Seq.of(1, 2, 3).append(4)
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    default Seq<T> append(T other) {
        return concat(other);
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).append(4, 5, 6)
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    @SuppressWarnings({ "unchecked" })
    default Seq<T> append(T... other) {
        return concat(other);
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(4, 5, 6).prepend(Seq.of(1, 2, 3))
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    default Seq<T> prepend(Stream<T> other) {
        return seq(other).concat(this);
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(4, 5, 6).prepend(Seq.of(1, 2, 3))
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    default Seq<T> prepend(Iterable<T> other) {
        return seq(other).concat(this);
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(4, 5, 6).prepend(Seq.of(1, 2, 3))
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    @SuppressWarnings({ "unchecked" })
    default Seq<T> prepend(Seq<T> other) {
        return other.concat(this);
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4)
     * Seq.of(2, 3, 4).prepend(1)
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    default Seq<T> prepend(T other) {
        return Seq.of(other).concat(this);
    }

    /**
     * Concatenate two streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(4, 5, 6).prepend(Seq.of(1, 2, 3))
     * </pre></code>
     *
     * @see #concat(Stream[])
     */
    @SuppressWarnings({ "unchecked" })
    default Seq<T> prepend(T... other) {
        return Seq.of(other).concat(this);
    }

    /**
     * Check whether this stream contains a given value.
     * <p>
     * <code><pre>
     * // true
     * Seq.of(1, 2, 3).contains(2)
     * </pre><code>
     */
    default boolean contains(T other) {
        return anyMatch(Predicate.isEqual(other));
    }

    /**
     * Check whether this stream contains all given values.
     * <p>
     * <code><pre>
     * // true
     * Seq.of(1, 2, 3).containsAll(2, 3)
     * </pre><code>
     */
    default boolean containsAll(T... other) {
        return containsAll(of(other));
    }

    /**
     * Check whether this stream contains all given values.
     * <p>
     * <code><pre>
     * // true
     * Seq.of(1, 2, 3).containsAll(2, 3)
     * </pre><code>
     */
    default boolean containsAll(Stream<T> other) {
        return containsAll(seq(other));
    }

    /**
     * Check whether this stream contains all given values.
     * <p>
     * <code><pre>
     * // true
     * Seq.of(1, 2, 3).containsAll(2, 3)
     * </pre><code>
     */
    default boolean containsAll(Iterable<T> other) {
        return containsAll(seq(other));
    }

    /**
     * Check whether this stream contains all given values.
     * <p>
     * <code><pre>
     * // true
     * Seq.of(1, 2, 3).containsAll(2, 3)
     * </pre><code>
     */
    default boolean containsAll(Seq<T> other) {
        Set<T> set = other.toSet(HashSet::new);
        return set.isEmpty() ? true : filter(t -> set.remove(t)).anyMatch(t -> set.isEmpty());
    }

    /**
     * Check whether this stream contains any of the given values.
     * <p>
     * <code><pre>
     * // true
     * Seq.of(1, 2, 3).containsAny(2, 4)
     * </pre><code>
     */
    default boolean containsAny(T... other) {
        return containsAny(of(other));
    }

    /**
     * Check whether this stream contains any of the given values.
     * <p>
     * <code><pre>
     * // true
     * Seq.of(1, 2, 3).containsAny(2, 4)
     * </pre><code>
     */
    default boolean containsAny(Stream<T> other) {
        return containsAny(seq(other));
    }

    /**
     * Check whether this stream contains any of the given values.
     * <p>
     * <code><pre>
     * // true
     * Seq.of(1, 2, 3).containsAny(2, 4)
     * </pre><code>
     */
    default boolean containsAny(Iterable<T> other) {
        return containsAny(seq(other));
    }

    /**
     * Check whether this stream contains any of the given values.
     * <p>
     * <code><pre>
     * // true
     * Seq.of(1, 2, 3).containsAny(2, 4)
     * </pre><code>
     */
    default boolean containsAny(Seq<T> other) {
        Set<T> set = other.toSet(HashSet::new);
        return set.isEmpty() ? false : anyMatch(set::contains);
    }

    /**
     * Get a single element from the stream at a given index
     */
    default Optional<T> get(long index) {
        if (index < 0L)
            return Optional.empty();
        else if (index == 0L)
            return findFirst();
        else
            return skip(index).findFirst();
    }

    /**
     * Return a new stream where the first occurrence of the argument is removed.
     * <p>
     * <code><pre>
     * // 1, 3, 2, 4
     * Seq.of(1, 2, 3, 2, 4).remove(2)
     * </pre><code>
     */
    default Seq<T> remove(T other) {
        boolean[] removed = new boolean[1];
        return filter(t -> removed[0] || !(removed[0] = Objects.equals(t, other)));
    }

    /**
     * Return a new stream where all occurrences of the arguments are removed.
     * <p>
     * <code><pre>
     * // 1, 4
     * Seq.of(1, 2, 3, 2, 4).removeAll(2, 3)
     * </pre><code>
     */
    default Seq<T> removeAll(T... other) {
        return removeAll(of(other));
    }

    /**
     * Return a new stream where all occurrences of the arguments are removed.
     * <p>
     * <code><pre>
     * // 1, 4
     * Seq.of(1, 2, 3, 2, 4).removeAll(2, 3)
     * </pre><code>
     */
    default Seq<T> removeAll(Stream<T> other) {
        return removeAll(seq(other));
    }

    /**
     * Return a new stream where all occurrences of the arguments are removed.
     * <p>
     * <code><pre>
     * // 1, 4
     * Seq.of(1, 2, 3, 2, 4).removeAll(2, 3)
     * </pre><code>
     */
    default Seq<T> removeAll(Iterable<T> other) {
        return removeAll(seq(other));
    }

    /**
     * Return a new stream where all occurrences of the arguments are removed.
     * <p>
     * <code><pre>
     * // 1, 4
     * Seq.of(1, 2, 3, 2, 4).removeAll(2, 3)
     * </pre><code>
     */
    default Seq<T> removeAll(Seq<T> other) {
        Set<T> set = other.toSet(HashSet::new);
        return set.isEmpty() ? this : filter(t -> !set.contains(t));
    }

    /**
     * Return a new stream where only occurrences of the arguments are retained.
     * <p>
     * <code><pre>
     * // 2, 3, 2
     * Seq.of(1, 2, 3, 2, 4).retainAll(2, 3)
     * </pre><code>
     */
    default Seq<T> retainAll(T... other) {
        return retainAll(of(other));
    }

    /**
     * Return a new stream where only occurrences of the arguments are retained.
     * <p>
     * <code><pre>
     * // 2, 3, 2
     * Seq.of(1, 2, 3, 2, 4).retainAll(2, 3)
     * </pre><code>
     */
    default Seq<T> retainAll(Stream<T> other) {
        return retainAll(seq(other));
    }

    /**
     * Return a new stream where only occurrences of the arguments are retained.
     * <p>
     * <code><pre>
     * // 2, 3, 2
     * Seq.of(1, 2, 3, 2, 4).retainAll(2, 3)
     * </pre><code>
     */
    default Seq<T> retainAll(Iterable<T> other) {
        return retainAll(seq(other));
    }

    /**
     * Return a new stream where only occurrences of the arguments are retained.
     * <p>
     * <code><pre>
     * // 2, 3, 2
     * Seq.of(1, 2, 3, 2, 4).retainAll(2, 3)
     * </pre><code>
     */
    default Seq<T> retainAll(Seq<T> other) {
        Set<T> set = other.toSet(HashSet::new);
        return set.isEmpty() ? empty() : filter(t -> set.contains(t));
    }

    /**
     * Repeat a stream infinitely.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 1, 2, 3, ...)
     * Seq.of(1, 2, 3).cycle();
     * </pre></code>
     *
     * @see #cycle(Stream)
     */
    default Seq<T> cycle() {
        return cycle(this);
    }

    /**
     * Get a stream of distinct keys.
     * <p>
     * <code><pre>
     * // (1, 2, 3)
     * Seq.of(1, 1, 2, -2, 3).distinct(Math::abs)
     * </pre></code>
     */
    default <U> Seq<T> distinct(Function<? super T, ? extends U> keyExtractor) {
        final Map<U, String> seen = new ConcurrentHashMap<>();
        return filter(t -> seen.put(keyExtractor.apply(t), "") == null);
    }

    /**
     * Zip two streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     *
     * @see #zip(Stream, Stream)
     */
    default <U> Seq<Tuple2<T, U>> zip(Stream<U> other) {
        return zip(seq(other));
    }

    /**
     * Zip two streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     *
     * @see #zip(Stream, Stream)
     */
    default <U> Seq<Tuple2<T, U>> zip(Iterable<U> other) {
        return zip(seq(other));
    }

    /**
     * Zip two streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     *
     * @see #zip(Stream, Stream)
     */
    default <U> Seq<Tuple2<T, U>> zip(Seq<U> other) {
        return zip(this, other);
    }

    /**
     * Zip two streams into one using a {@link BiFunction} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     *
     * @see #zip(Seq, BiFunction)
     */
    default <U, R> Seq<R> zip(Stream<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        return zip(seq(other), zipper);
    }

    /**
     * Zip two streams into one using a {@link BiFunction} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     *
     * @see #zip(Seq, BiFunction)
     */
    default <U, R> Seq<R> zip(Iterable<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        return zip(seq(other), zipper);
    }

    /**
     * Zip two streams into one using a {@link BiFunction} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     *
     * @see #zip(Seq, BiFunction)
     */
    default <U, R> Seq<R> zip(Seq<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        return zip(this, other, zipper);
    }

    /**
     * Zip a Stream with a corresponding Stream of indexes.
     * <p>
     * <code><pre>
     * // (tuple("a", 0), tuple("b", 1), tuple("c", 2))
     * Seq.of("a", "b", "c").zipWithIndex()
     * </pre></code>
     *
     * @see #zipWithIndex(Stream)
     */
    default Seq<Tuple2<T, Long>> zipWithIndex() {
        return zipWithIndex(this);
    }

    /**
     * Fold a Stream to the left.
     * <p>
     * <code><pre>
     * // "abc"
     * Seq.of("a", "b", "c").foldLeft("", (u, t) -> u + t)
     * </pre></code>
     */
    default <U> U foldLeft(U seed, BiFunction<U, ? super T, U> function) {
        return foldLeft(this, seed, function);
    }

    /**
     * Fold a Stream to the right.
     * <p>
     * <code><pre>
     * // "cba"
     * Seq.of("a", "b", "c").foldRight("", (t, u) -> u + t)
     * </pre></code>
     */
    default <U> U foldRight(U seed, BiFunction<? super T, U, U> function) {
        return foldRight(this, seed, function);
    }

    /**
     * Scan a stream to the left.
     * <p>
     * <code><pre>
     * // ("", "a", "ab", "abc")
     * Seq.of("a", "b", "c").scanLeft("", (u, t) -> u + t)
     * </pre></code>
     */
    default <U> Seq<U> scanLeft(U seed, BiFunction<U, ? super T, U> function) {
        return scanLeft(this, seed, function);
    }

    /**
     * Scan a stream to the right.
     * <p>
     * <code><pre>
     * // ("", "c", "cb", "cba")
     * Seq.of("a", "b", "c").scanRight("", (t, u) -> u + t)
     * </pre></code>
     */
    default <U> Seq<U> scanRight(U seed, BiFunction<? super T, U, U> function) {
        return scanRight(this, seed, function);
    }

    /**
     * Reverse a stream.
     * <p>
     * <code><pre>
     * // (3, 2, 1)
     * Seq.of(1, 2, 3).reverse()
     * </pre></code>
     */
    default Seq<T> reverse() {
        return reverse(this);
    }

    /**
     * Shuffle a stream
     * <p>
     * <code><pre>
     * // e.g. (2, 3, 1)
     * Seq.of(1, 2, 3).shuffle()
     * </pre></code>
     */
    default Seq<T> shuffle() {
        return shuffle(this);
    }

    /**
     * Shuffle a stream using specified source of randomness
     * <p>
     * <code><pre>
     * // e.g. (2, 3, 1)
     * Seq.of(1, 2, 3).shuffle(new Random())
     * </pre></code>
     */
    default Seq<T> shuffle(Random random) {
        return shuffle(this, random);
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (3, 4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipWhile(i -> i &lt; 3)
     * </pre></code>
     *
     * @see #skipWhile(Stream, Predicate)
     */
    default Seq<T> skipWhile(Predicate<? super T> predicate) {
        return skipWhile(this, predicate);
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>true</code>
     * plus the first element for which it evaluates to false.
     * <p>
     * <code><pre>
     * // (4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipWhileClosed(i -> i &lt; 3)
     * </pre></code>
     *
     * @see #skipWhileClosed(Stream, Predicate)
     */
    default Seq<T> skipWhileClosed(Predicate<? super T> predicate) {
        return skipWhileClosed(this, predicate);
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>false</code>.
     * <p>
     * <code><pre>
     * // (3, 4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipUntil(i -> i == 3)
     * </pre></code>
     *
     * @see #skipUntil(Stream, Predicate)
     */
    default Seq<T> skipUntil(Predicate<? super T> predicate) {
        return skipUntil(this, predicate);
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>false</code>
     * plus the first element for which it evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipUntilClosed(i -> i == 3)
     * </pre></code>
     *
     * @see #skipUntilClosed(Stream, Predicate)
     */
    default Seq<T> skipUntilClosed(Predicate<? super T> predicate) {
        return skipUntilClosed(this, predicate);
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (1, 2)
     * Seq.of(1, 2, 3, 4, 5).limitWhile(i -> i &lt; 3)
     * </pre></code>
     *
     * @see #limitWhile(Stream, Predicate)
     */
    default Seq<T> limitWhile(Predicate<? super T> predicate) {
        return limitWhile(this, predicate);
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>
     * plus the first element for which it evaluates to <code>false</code>.
     * <p>
     * <code><pre>
     * // (1, 2, 3)
     * Seq.of(1, 2, 3, 4, 5).limitWhileClosed(i -> i &lt; 3)
     * </pre></code>
     *
     * @see #limitWhileClosed(Stream, Predicate)
     */
    default Seq<T> limitWhileClosed(Predicate<? super T> predicate) {
        return limitWhileClosed(this, predicate);
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>false</code>.
     * <p>
     * <code><pre>
     * // (1, 2)
     * Seq.of(1, 2, 3, 4, 5).limitUntil(i -> i == 3)
     * </pre></code>
     *
     * @see #limitUntil(Stream, Predicate)
     */
    default Seq<T> limitUntil(Predicate<? super T> predicate) {
        return limitUntil(this, predicate);
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>false</code>
     * plus the first element for which it evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (1, 2, 3)
     * Seq.of(1, 2, 3, 4, 5).limitUntilClosed(i -> i == 3)
     * </pre></code>
     *
     * @see #limitUntilClosed(Stream, Predicate)
     */
    default Seq<T> limitUntilClosed(Predicate<? super T> predicate) {
        return limitUntilClosed(this, predicate);
    }

    /**
     * Returns a stream with a given value interspersed between any two values of this stream.
     * <p>
     * <code><pre>
     * // (1, 0, 2, 0, 3, 0, 4)
     * Seq.of(1, 2, 3, 4).intersperse(0)
     * </pre></code>
     *
     * @see #intersperse(Stream, Object)
     */
    default Seq<T> intersperse(T value) {
        return intersperse(this, value);
    }

    /**
     * Duplicate a Streams into two equivalent Streams.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (1, 2, 3))
     * Seq.of(1, 2, 3).duplicate()
     * </pre></code>
     *
     * @see #duplicate(Stream)
     */
    default Tuple2<Seq<T>, Seq<T>> duplicate() {
        return duplicate(this);
    }

    /**
     * Classify this stream's elements according to a given classifier function.
     * <p>
     * <code><pre>
     * // Seq(tuple(1, Seq(1, 3, 5)), tuple(0, Seq(2, 4, 6)))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2)
     * // Seq(tuple(true, Seq(1, 3, 5)), tuple(false, Seq(2, 4, 6)))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2 != 0)
     * </pre></code>
     *
     * This is a non-terminal analog of {@link #groupBy(Function)})
     * @see #groupBy(Function)
     * @see #partition(Predicate)
     */
    default <K> Seq<Tuple2<K, Seq<T>>> grouped(Function<? super T, ? extends K> classifier) {
        return grouped(this, classifier);
    }

    /**
     * Classify this stream's elements according to a given classifier function
     * and collect each class's elements using a collector.
     * <p>
     * <code><pre>
     * // Seq(tuple(1, 9), tuple(0, 12))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2, Collectors.summingInt(i -> i))
     * // Seq(tuple(true, 9), tuple(false, 12))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2 != 0, Collectors.summingInt(i -> i))
     * </pre></code> This is a non-terminal analog of
     * {@link #groupBy(Function, Collector)})
     *
     * @see #groupBy(Function, Collector)
     */
    default <K, A, D> Seq<Tuple2<K, D>> grouped(Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream) {
        return grouped(this, classifier, downstream);
    }

    /**
     * Partition a stream into two given a predicate.
     * <p>
     * <code><pre>
     * // tuple((1, 3, 5), (2, 4, 6))
     * Seq.of(1, 2, 3, 4, 5, 6).partition(i -> i % 2 != 0)
     * </pre></code>
     *
     * @see #partition(Stream, Predicate)
     */
    default Tuple2<Seq<T>, Seq<T>> partition(Predicate<? super T> predicate) {
        return partition(this, predicate);
    }

    /**
     * Split a stream at a given position.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (4, 5, 6))
     * Seq.of(1, 2, 3, 4, 5, 6).splitAt(3)
     * </pre></code>
     *
     * @see #splitAt(Stream, long)
     */
    default Tuple2<Seq<T>, Seq<T>> splitAt(long position) {
        return splitAt(this, position);
    }

    /**
     * Split a stream at the head.
     * <p>
     * <code><pre>
     * // tuple(1, (2, 3, 4, 5, 6))
     * Seq.of(1, 2, 3, 4, 5, 6).splitHead(3)
     * </pre></code>
     *
     * @see #splitAt(Stream, long)
     */
    default Tuple2<Optional<T>, Seq<T>> splitAtHead() {
        return splitAtHead(this);
    }

    /**
     * Returns a limited interval from a given Stream.
     * <p>
     * <code><pre>
     * // (4, 5)
     * Seq.of(1, 2, 3, 4, 5, 6).slice(3, 5)
     * </pre></code>
     *
     * @see #slice(Stream, long, long)
     */
    default Seq<T> slice(long from, long to) {
        return slice(this, from, to);
    }

    /**
     * Collect a Stream into a Collection.
     *
     * @see #toCollection(Stream, Supplier)
     */
    default <C extends Collection<T>> C toCollection(Supplier<C> collectionFactory) {
        return toCollection(this, collectionFactory);
    }

    /**
     * Collect a Stream into a List.
     *
     * @see #toList(Stream)
     */
    default List<T> toList() {
        return toList(this);
    }

    /**
     * Collect a Stream into a List.
     *
     * @see #toList(Stream)
     */
    default <L extends List<T>> L toList(Supplier<L> listFactory) {
        return toCollection(listFactory);
    }

    /**
     * Collect a Stream into a Set.
     *
     * @see #toSet(Stream)
     */
    default Set<T> toSet() {
        return toSet(this);
    }

    /**
     * Collect a Stream into a Set.
     *
     * @see #toSet(Stream)
     */
    default <S extends Set<T>> S toSet(Supplier<S> setFactory) {
        return toCollection(setFactory);
    }

    /**
     * Collect a Stream into a Map.
     *
     * @see #toMap(Stream, Function, Function)
     */
    default <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return toMap(this, keyMapper, valueMapper);
    }

    /**
     * Consume a stream and concatenate aTll elements using a separator.
     */
    default String toString(String separator) {
        return toString(this, separator);
    }

    /**
     * Get the mode, i.e. the value that appears most often in the stream.
     */
    default Optional<T> mode() {
        return collect(Agg.mode());
    }

    /**
     * Get the minimum value by a function.
     */
    default <U extends Comparable<? super U>> Optional<T> minBy(Function<? super T, ? extends U> function) {
        return minBy(function, naturalOrder());
    }

    /**
     * Get the minimum value by a function.
     */
    default <U> Optional<T> minBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return map(t -> tuple(t, function.apply(t)))
              .min(comparing(Tuple2::v2, comparator))
              .map(t -> t.v1);
    }

    /**
     * Get the maximum value by a function.
     */
    default <U extends Comparable<? super U>> Optional<T> maxBy(Function<? super T, ? extends U> function) {
        return maxBy(function, naturalOrder());
    }

    /**
     * Get the maximum value by a function.
     */
    default <U> Optional<T> maxBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return map(t -> tuple(t, function.apply(t)))
              .max(comparing(Tuple2::v2, comparator))
              .map(t -> t.v1);
    }

    /**
     * Get the median value.
     */
    default Optional<T> median(Comparator<? super T> comparator) {
        return collect(Agg.median(comparator));
    }

    /**
     * Get the median value by a function.
     */
    default <U extends Comparable<? super U>> Optional<T> medianBy(Function<? super T, ? extends U> function) {
        return collect(Agg.medianBy(function));
    }

    /**
     * Get the median value by a function.
     */
    default <U> Optional<T> medianBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collect(Agg.medianBy(function, comparator));
    }

    /**
     * Get the discrete percentile value.
     */
    default Optional<T> percentile(double percentile, Comparator<? super T> comparator) {
        return collect(Agg.percentile(percentile, comparator));
    }

    /**
     * Get the discrete percentile value by a function.
     */
    default <U extends Comparable<? super U>> Optional<T> percentileBy(double percentile, Function<? super T, ? extends U> function) {
        return collect(Agg.percentileBy(percentile, function));
    }

    /**
     * Get the discrete percentile value by a function.
     */
    default <U> Optional<T> percentileBy(double percentile, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collect(Agg.percentileBy(percentile, function, comparator));
    }

    /**
     * Check if the sequence has any elements
     */
    default boolean isEmpty() {
        return !findAny().isPresent();
    }

    /**
     * Check if the sequence has no elements
     */
    default boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * Sort by the results of function.
     */
    default <U extends Comparable<? super U>> Seq<T> sorted(Function<? super T, ? extends U> function) {
        return sorted(comparing(function));
    }

    // Methods taken from LINQ
    // -----------------------

    /**
     * Keep only those elements in a stream that are of a given type.
     * <p>
     * <code><pre>
     * // (1, 2, 3)
     * Seq.of(1, "a", 2, "b", 3).ofType(Integer.class)
     * </pre></code>
     *
     * @see #ofType(Stream, Class)
     */
    default <U> Seq<U> ofType(Class<U> type) {
        return ofType(this, type);
    }

    /**
     * Cast all elements in a stream to a given type, possibly throwing a {@link ClassCastException}.
     * <p>
     * <code><pre>
     * // ClassCastException
     * Seq.of(1, "a", 2, "b", 3).cast(Integer.class)
     * </pre></code>
     *
     * @see #cast(Stream, Class)
     */
    default <U> Seq<U> cast(Class<U> type) {
        return cast(this, type);
    }

    // Shortcuts to Collectors
    // -----------------------

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function)} collector.
     */
    default <K> Map<K, List<T>> groupBy(Function<? super T, ? extends K> classifier) {
        return collect(Collectors.groupingBy(classifier));
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function, Collector)} collector.
     */
    default <K, A, D> Map<K, D> groupBy(Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream) {
        return collect(Collectors.groupingBy(classifier, downstream));
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function, Supplier, Collector)} collector.
     */
    default <K, D, A, M extends Map<K, D>> M groupBy(Function<? super T, ? extends K> classifier, Supplier<M> mapFactory, Collector<? super T, A, D> downstream) {
        return collect(Collectors.groupingBy(classifier, mapFactory, downstream));
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining()}
     * collector.
     */
    default String join() {
        return map(Objects::toString).collect(Collectors.joining());
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining(CharSequence)}
     * collector.
     */
    default String join(CharSequence delimiter) {
        return map(Objects::toString).collect(Collectors.joining(delimiter));
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining(CharSequence, CharSequence, CharSequence)}
     * collector.
     */
    default String join(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return map(Objects::toString).collect(Collectors.joining(delimiter, prefix, suffix));
    }

    /**
     * @see Stream#of(Object)
     */
    static <T> Seq<T> of(T value) {
        return seq(Stream.of(value));
    }

    /**
     * @see Stream#of(Object[])
     */
    @SafeVarargs
    static <T> Seq<T> of(T... values) {
        return seq(Stream.of(values));
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     */
    static Seq<Byte> range(byte from, byte to) {
        return range(from, to, (byte) 1);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     * @param step The increase between two values
     */
    static Seq<Byte> range(byte from, byte to, int step) {
        return to <= from ? empty() : iterate(from, t -> Byte.valueOf((byte) (t + step))).limitWhile(t -> t < to);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     */
    static Seq<Short> range(short from, short to) {
        return range(from, to, (short) 1);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     * @param step The increase between two values
     */
    static Seq<Short> range(short from, short to, int step) {
        return to <= from ? empty() : iterate(from, t -> Short.valueOf((short) (t + step))).limitWhile(t -> t < to);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     */
    static Seq<Character> range(char from, char to) {
        return range(from, to, (short) 1);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     * @param step The increase between two values
     */
    static Seq<Character> range(char from, char to, int step) {
        return to <= from ? empty() : iterate(from, t -> Character.valueOf((char) (t + step))).limitWhile(t -> t < to);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     */
    static Seq<Integer> range(int from, int to) {
        return range(from, to, 1);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     * @param step The increase between two values
     */
    static Seq<Integer> range(int from, int to, int step) {
        return to <= from ? empty() : iterate(from, t -> Integer.valueOf(t + step)).limitWhile(t -> t < to);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     */
    static Seq<Long> range(long from, long to) {
        return range(from, to, 1L);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     * @param step The increase between two values
     */
    static Seq<Long> range(long from, long to, long step) {
        return to <= from ? empty() : iterate(from, t -> Long.valueOf(t + step)).limitWhile(t -> t < to);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     */
    static Seq<Instant> range(Instant from, Instant to) {
        return range(from, to, Duration.ofSeconds(1));
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     * @param step The increase between two values
     */
    static Seq<Instant> range(Instant from, Instant to, Duration step) {
        return to.compareTo(from) <= 0 ? empty() : iterate(from, t -> t.plus(step)).limitWhile(t -> t.compareTo(to) < 0);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (inclusive)
     */
    static Seq<Byte> rangeClosed(byte from, byte to) {
        return rangeClosed(from, to, (byte) 1);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (inclusive)
     * @param step The increase between two values
     */
    static Seq<Byte> rangeClosed(byte from, byte to, int step) {
        return to < from ? empty() : iterate(from, t -> Byte.valueOf((byte) (t + step))).limitWhile(t -> t <= to);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (inclusive)
     */
    static Seq<Short> rangeClosed(short from, short to) {
        return rangeClosed(from, to, (short) 1);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (inclusive)
     * @param step The increase between two values
     */
    static Seq<Short> rangeClosed(short from, short to, int step) {
        return to < from ? empty() : iterate(from, t -> Short.valueOf((short) (t + step))).limitWhile(t -> t <= to);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (inclusive)
     */
    static Seq<Character> rangeClosed(char from, char to) {
        return rangeClosed(from, to, 1);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (inclusive)
     * @param step The increase between two values
     */
    static Seq<Character> rangeClosed(char from, char to, int step) {
        return to < from ? empty() : iterate(from, t -> Character.valueOf((char) (t + step))).limitWhile(t -> t <= to);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (inclusive)
     */
    static Seq<Integer> rangeClosed(int from, int to) {
        return rangeClosed(from, to, 1);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (inclusive)
     * @param step The increase between two values
     */
    static Seq<Integer> rangeClosed(int from, int to, int step) {
        return to < from ? empty() : iterate(from, t -> Integer.valueOf(t + step)).limitWhile(t -> t <= to);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (inclusive)
     */
    static Seq<Long> rangeClosed(long from, long to) {
        return rangeClosed(from, to, 1L);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (inclusive)
     * @param step The increase between two values
     */
    static Seq<Long> rangeClosed(long from, long to, long step) {
        return to < from ? empty() : iterate(from, t -> Long.valueOf(t + step)).limitWhile(t -> t <= to);
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     */
    static Seq<Instant> rangeClosed(Instant from, Instant to) {
        return rangeClosed(from, to, Duration.ofSeconds(1));
    }

    /**
     * The range between two values.
     *
     * @param from The lower bound (inclusive)
     * @param to The upper bound (exclusive)
     * @param step The increase between two values
     */
    static Seq<Instant> rangeClosed(Instant from, Instant to, Duration step) {
        return to.compareTo(from) < 0 ? empty() : iterate(from, t -> t.plus(step)).limitWhile(t -> t.compareTo(to) <= 0);
    }

    /**
     * @see Stream#empty()
     */
    static <T> Seq<T> empty() {
        return seq(Stream.empty());
    }

    /**
     * @see Stream#iterate(Object, UnaryOperator)
     */
    static <T> Seq<T> iterate(final T seed, final UnaryOperator<T> f) {
        return seq(Stream.iterate(seed, f));
    }

    /**
     * @see Stream#generate(Supplier)
     */
    static Seq<Void> generate() {
        return generate(() -> null);
    }

    /**
     * @see Stream#generate(Supplier)
     */
    static <T> Seq<T> generate(T value) {
        return generate(() -> value);
    }

    /**
     * @see Stream#generate(Supplier)
     */
    static <T> Seq<T> generate(Supplier<T> s) {
        return seq(Stream.generate(s));
    }

    /**
     * Wrap a <code>Stream</code> into a <code>Seq</code>.
     */
    static <T> Seq<T> seq(Stream<T> stream) {
        if (stream instanceof Seq)
            return (Seq<T>) stream;

        return new SeqImpl<>(stream);
    }

    /**
     * Wrap a <code>Stream</code> into a <code>Seq</code>.
     */
    static <T> Seq<T> seq(Seq<T> stream) {
        return stream;
    }

    /**
     * Wrap a <code>IntStream</code> into a <code>Seq</code>.
     */
    static Seq<Integer> seq(IntStream stream) {
        return new SeqImpl<>(stream.boxed());
    }

    /**
     * Wrap a <code>IntStream</code> into a <code>Seq</code>.
     */
    static Seq<Long> seq(LongStream stream) {
        return new SeqImpl<>(stream.boxed());
    }

    /**
     * Wrap a <code>IntStream</code> into a <code>Seq</code>.
     */
    static Seq<Double> seq(DoubleStream stream) {
        return new SeqImpl<>(stream.boxed());
    }

    /**
     * Wrap an <code>Iterable</code> into a <code>Seq</code>.
     */
    static <T> Seq<T> seq(Iterable<T> iterable) {
        return seq(iterable.iterator());
    }

    /**
     * Wrap an <code>Iterator</code> into a <code>Seq</code>.
     */
    static <T> Seq<T> seq(Iterator<T> iterator) {
        return seq(spliteratorUnknownSize(iterator, ORDERED));
    }

    /**
     * Wrap a <code>Spliterator</code> into a <code>Seq</code>.
     */
    static <T> Seq<T> seq(Spliterator<T> spliterator) {
        return seq(StreamSupport.stream(spliterator, false));
    }

    /**
     * Wrap a <code>Map</code> into a <code>Seq</code>.
     */
    static <K, V> Seq<Tuple2<K, V>> seq(Map<K, V> map) {
        return seq(map.entrySet()).map(e -> tuple(e.getKey(), e.getValue()));
    }

    /**
     * Wrap an <code>Optional</code> into a <code>Seq</code>.
     */
    static <T> Seq<T> seq(Optional<T> optional) {
        return optional.map(Seq::of).orElseGet(Seq::empty);
    }

    /**
     * Wrap an <code>InputStream</code> into a <code>Seq</code>.
     * <p>
     * Client code must close the <code>InputStream</code>. All
     * {@link IOException}'s thrown be the <code>InputStream</code> are wrapped
     * by {@link UncheckedIOException}'s.
     */
    static Seq<Byte> seq(InputStream is) {
        FunctionalSpliterator<Byte> spliterator = consumer -> {
            try {
                int value = is.read();

                if (value != -1)
                    consumer.accept((byte) value);

                return value != -1;
            }
            catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };

        return seq(spliterator).onClose(Unchecked.runnable(is::close));
    }

    /**
     * Wrap a <code>Reader</code> into a <code>Seq</code>.
     * <p>
     * Client code must close the <code>Reader</code>. All
     * {@link IOException}'s thrown be the <code>InputStream</code> are wrapped
     * by {@link UncheckedIOException}'s.
     */
    static Seq<Character> seq(Reader reader) {
        FunctionalSpliterator<Character> spliterator = consumer -> {
            try {
                int value = reader.read();

                if (value != -1)
                    consumer.accept((char) value);

                return value != -1;
            }
            catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };

        return seq(spliterator).onClose(Unchecked.runnable(reader::close));
    }

    /**
     * Repeat a stream infinitely.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 1, 2, 3, ...)
     * Seq.of(1, 2, 3).cycle();
     * </pre></code>
     */
    static <T> Seq<T> cycle(Stream<T> stream) {
        return cycle(seq(stream));
    }

    /**
     * Repeat a stream infinitely.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 1, 2, 3, ...)
     * Seq.of(1, 2, 3).cycle();
     * </pre></code>
     */
    static <T> Seq<T> cycle(Iterable<T> iterable) {
        return cycle(seq(iterable));
    }

    /**
     * Repeat a stream infinitely.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 1, 2, 3, ...)
     * Seq.of(1, 2, 3).cycle();
     * </pre></code>
     */
    static <T> Seq<T> cycle(Seq<T> stream) {
        List<T> list = new ArrayList<>();
        Spliterator<T>[] sp = new Spliterator[1];

        return transform(stream, (delegate, action) -> {
            if (sp[0] == null) {
                if (delegate.tryAdvance(t -> {
                    list.add(t);
                    action.accept(t);
                }))
                    return true;
                else
                    sp[0] = list.spliterator();
            }

            if (!sp[0].tryAdvance(action)) {
                sp[0] = list.spliterator();

                if (!sp[0].tryAdvance(action))
                    return false;
            }

            return true;
        });
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(Stream<Tuple2<T1, T2>> stream) {
        return unzip(seq(stream));
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Stream<Tuple2<T1, T2>> stream, Function<T1, U1> leftUnzipper, Function<T2, U2> rightUnzipper) {
        return unzip(seq(stream), leftUnzipper, rightUnzipper);
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Stream<Tuple2<T1, T2>> stream, Function<Tuple2<T1, T2>, Tuple2<U1, U2>> unzipper) {
        return unzip(seq(stream), unzipper);
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Stream<Tuple2<T1, T2>> stream, BiFunction<T1, T2, Tuple2<U1, U2>> unzipper) {
        return unzip(seq(stream), unzipper);
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(Iterable<Tuple2<T1, T2>> iterable) {
        return unzip(seq(iterable));
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Iterable<Tuple2<T1, T2>> iterable, Function<T1, U1> leftUnzipper, Function<T2, U2> rightUnzipper) {
        return unzip(seq(iterable), leftUnzipper, rightUnzipper);
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Iterable<Tuple2<T1, T2>> iterable, Function<Tuple2<T1, T2>, Tuple2<U1, U2>> unzipper) {
        return unzip(seq(iterable), unzipper);
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Iterable<Tuple2<T1, T2>> iterable, BiFunction<T1, T2, Tuple2<U1, U2>> unzipper) {
        return unzip(seq(iterable), unzipper);
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(Seq<Tuple2<T1, T2>> stream) {
        return unzip(stream, t -> t);
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Seq<Tuple2<T1, T2>> stream, Function<T1, U1> leftUnzipper, Function<T2, U2> rightUnzipper) {
        return unzip(stream, t -> tuple(leftUnzipper.apply(t.v1), rightUnzipper.apply(t.v2)));
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Seq<Tuple2<T1, T2>> stream, Function<Tuple2<T1, T2>, Tuple2<U1, U2>> unzipper) {
        return unzip(stream, (t1, t2) -> unzipper.apply(tuple(t1, t2)));
    }

    /**
     * Unzip one Stream into two.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (a, b, c))
     * Seq.unzip(Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")));
     * </pre></code>
     */
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Seq<Tuple2<T1, T2>> stream, BiFunction<T1, T2, Tuple2<U1, U2>> unzipper) {
        return stream
              .map(t -> unzipper.apply(t.v1, t.v2))
              .duplicate()
              .map1(s -> s.map(u -> u.v1))
              .map2(s -> s.map(u -> u.v2));
    }

 // [jooq-tools] START [zip-static]

    /**
     * Zip 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2> Seq<Tuple2<T1, T2>> zip(Stream<T1> s1, Stream<T2> s2) {
        return zip(seq(s1), seq(s2));
    }

    /**
     * Zip 3 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3> Seq<Tuple3<T1, T2, T3>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3) {
        return zip(seq(s1), seq(s2), seq(s3));
    }

    /**
     * Zip 4 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4> Seq<Tuple4<T1, T2, T3, T4>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4));
    }

    /**
     * Zip 5 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5> Seq<Tuple5<T1, T2, T3, T4, T5>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5));
    }

    /**
     * Zip 6 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6> Seq<Tuple6<T1, T2, T3, T4, T5, T6>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6));
    }

    /**
     * Zip 7 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7> Seq<Tuple7<T1, T2, T3, T4, T5, T6, T7>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7));
    }

    /**
     * Zip 8 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8> Seq<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8));
    }

    /**
     * Zip 9 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Seq<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9));
    }

    /**
     * Zip 10 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Seq<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10));
    }

    /**
     * Zip 11 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Seq<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11));
    }

    /**
     * Zip 12 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Seq<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12));
    }

    /**
     * Zip 13 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Seq<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13));
    }

    /**
     * Zip 14 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Seq<Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13, Stream<T14> s14) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13), seq(s14));
    }

    /**
     * Zip 15 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Seq<Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13, Stream<T14> s14, Stream<T15> s15) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13), seq(s14), seq(s15));
    }

    /**
     * Zip 16 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Seq<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13, Stream<T14> s14, Stream<T15> s15, Stream<T16> s16) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13), seq(s14), seq(s15), seq(s16));
    }

    /**
     * Zip 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2> Seq<Tuple2<T1, T2>> zip(Iterable<T1> i1, Iterable<T2> i2) {
        return zip(seq(i1), seq(i2));
    }

    /**
     * Zip 3 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3> Seq<Tuple3<T1, T2, T3>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3) {
        return zip(seq(i1), seq(i2), seq(i3));
    }

    /**
     * Zip 4 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4> Seq<Tuple4<T1, T2, T3, T4>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4));
    }

    /**
     * Zip 5 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5> Seq<Tuple5<T1, T2, T3, T4, T5>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5));
    }

    /**
     * Zip 6 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6> Seq<Tuple6<T1, T2, T3, T4, T5, T6>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6));
    }

    /**
     * Zip 7 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7> Seq<Tuple7<T1, T2, T3, T4, T5, T6, T7>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7));
    }

    /**
     * Zip 8 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8> Seq<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8));
    }

    /**
     * Zip 9 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Seq<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9));
    }

    /**
     * Zip 10 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Seq<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10));
    }

    /**
     * Zip 11 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Seq<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11));
    }

    /**
     * Zip 12 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Seq<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12));
    }

    /**
     * Zip 13 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Seq<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13));
    }

    /**
     * Zip 14 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Seq<Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13, Iterable<T14> i14) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13), seq(i14));
    }

    /**
     * Zip 15 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Seq<Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13, Iterable<T14> i14, Iterable<T15> i15) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13), seq(i14), seq(i15));
    }

    /**
     * Zip 16 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Seq<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13, Iterable<T14> i14, Iterable<T15> i15, Iterable<T16> i16) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13), seq(i14), seq(i15), seq(i16));
    }

    /**
     * Zip 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2> Seq<Tuple2<T1, T2>> zip(Seq<T1> s1, Seq<T2> s2) {
        return zip(s1, s2, (t1, t2) -> tuple(t1, t2));
    }

    /**
     * Zip 3 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3> Seq<Tuple3<T1, T2, T3>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3) {
        return zip(s1, s2, s3, (t1, t2, t3) -> tuple(t1, t2, t3));
    }

    /**
     * Zip 4 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4> Seq<Tuple4<T1, T2, T3, T4>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4) {
        return zip(s1, s2, s3, s4, (t1, t2, t3, t4) -> tuple(t1, t2, t3, t4));
    }

    /**
     * Zip 5 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5> Seq<Tuple5<T1, T2, T3, T4, T5>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5) {
        return zip(s1, s2, s3, s4, s5, (t1, t2, t3, t4, t5) -> tuple(t1, t2, t3, t4, t5));
    }

    /**
     * Zip 6 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6> Seq<Tuple6<T1, T2, T3, T4, T5, T6>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6) {
        return zip(s1, s2, s3, s4, s5, s6, (t1, t2, t3, t4, t5, t6) -> tuple(t1, t2, t3, t4, t5, t6));
    }

    /**
     * Zip 7 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7> Seq<Tuple7<T1, T2, T3, T4, T5, T6, T7>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7) {
        return zip(s1, s2, s3, s4, s5, s6, s7, (t1, t2, t3, t4, t5, t6, t7) -> tuple(t1, t2, t3, t4, t5, t6, t7));
    }

    /**
     * Zip 8 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8> Seq<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8) {
        return zip(s1, s2, s3, s4, s5, s6, s7, s8, (t1, t2, t3, t4, t5, t6, t7, t8) -> tuple(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    /**
     * Zip 9 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Seq<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9) {
        return zip(s1, s2, s3, s4, s5, s6, s7, s8, s9, (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> tuple(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    /**
     * Zip 10 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Seq<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10) {
        return zip(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> tuple(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    /**
     * Zip 11 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Seq<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11) {
        return zip(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> tuple(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    /**
     * Zip 12 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Seq<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12) {
        return zip(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> tuple(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    /**
     * Zip 13 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Seq<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13) {
        return zip(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> tuple(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    /**
     * Zip 14 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Seq<Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13, Seq<T14> s14) {
        return zip(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) -> tuple(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    /**
     * Zip 15 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Seq<Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13, Seq<T14> s14, Seq<T15> s15) {
        return zip(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) -> tuple(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    /**
     * Zip 16 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Seq<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13, Seq<T14> s14, Seq<T15> s15, Seq<T16> s16) {
        return zip(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> tuple(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    /**
     * Zip 2 streams into one using a {@link BiFunction} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), zipper);
    }

    /**
     * Zip 3 streams into one using a {@link Function3} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), zipper);
    }

    /**
     * Zip 4 streams into one using a {@link Function4} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), zipper);
    }

    /**
     * Zip 5 streams into one using a {@link Function5} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), zipper);
    }

    /**
     * Zip 6 streams into one using a {@link Function6} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), zipper);
    }

    /**
     * Zip 7 streams into one using a {@link Function7} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), zipper);
    }

    /**
     * Zip 8 streams into one using a {@link Function8} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), zipper);
    }

    /**
     * Zip 9 streams into one using a {@link Function9} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), zipper);
    }

    /**
     * Zip 10 streams into one using a {@link Function10} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Function10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), zipper);
    }

    /**
     * Zip 11 streams into one using a {@link Function11} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Function11<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), zipper);
    }

    /**
     * Zip 12 streams into one using a {@link Function12} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Function12<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), zipper);
    }

    /**
     * Zip 13 streams into one using a {@link Function13} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13, Function13<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13), zipper);
    }

    /**
     * Zip 14 streams into one using a {@link Function14} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13, Stream<T14> s14, Function14<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13), seq(s14), zipper);
    }

    /**
     * Zip 15 streams into one using a {@link Function15} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13, Stream<T14> s14, Stream<T15> s15, Function15<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13), seq(s14), seq(s15), zipper);
    }

    /**
     * Zip 16 streams into one using a {@link Function16} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> Seq<R> zip(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13, Stream<T14> s14, Stream<T15> s15, Stream<T16> s16, Function16<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? extends R> zipper) {
        return zip(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13), seq(s14), seq(s15), seq(s16), zipper);
    }

    /**
     * Zip 2 streams into one using a {@link BiFunction} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), zipper);
    }

    /**
     * Zip 3 streams into one using a {@link Function3} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), zipper);
    }

    /**
     * Zip 4 streams into one using a {@link Function4} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), zipper);
    }

    /**
     * Zip 5 streams into one using a {@link Function5} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), zipper);
    }

    /**
     * Zip 6 streams into one using a {@link Function6} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), zipper);
    }

    /**
     * Zip 7 streams into one using a {@link Function7} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), zipper);
    }

    /**
     * Zip 8 streams into one using a {@link Function8} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), zipper);
    }

    /**
     * Zip 9 streams into one using a {@link Function9} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), zipper);
    }

    /**
     * Zip 10 streams into one using a {@link Function10} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Function10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), zipper);
    }

    /**
     * Zip 11 streams into one using a {@link Function11} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Function11<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), zipper);
    }

    /**
     * Zip 12 streams into one using a {@link Function12} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Function12<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), zipper);
    }

    /**
     * Zip 13 streams into one using a {@link Function13} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13, Function13<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13), zipper);
    }

    /**
     * Zip 14 streams into one using a {@link Function14} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13, Iterable<T14> i14, Function14<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13), seq(i14), zipper);
    }

    /**
     * Zip 15 streams into one using a {@link Function15} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13, Iterable<T14> i14, Iterable<T15> i15, Function15<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13), seq(i14), seq(i15), zipper);
    }

    /**
     * Zip 16 streams into one using a {@link Function16} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> Seq<R> zip(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13, Iterable<T14> i14, Iterable<T15> i15, Iterable<T16> i16, Function16<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? extends R> zipper) {
        return zip(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13), seq(i14), seq(i15), seq(i16), zipper);
    }

    /**
     * Zip 2 streams into one using a {@link BiFunction} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 3 streams into one using a {@link Function3} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 4 streams into one using a {@link Function4} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 5 streams into one using a {@link Function5} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 6 streams into one using a {@link Function6} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();
        final Iterator<T6> it6 = s6.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext() && it6.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next(), it6.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 7 streams into one using a {@link Function7} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();
        final Iterator<T6> it6 = s6.iterator();
        final Iterator<T7> it7 = s7.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext() && it6.hasNext() && it7.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next(), it6.next(), it7.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 8 streams into one using a {@link Function8} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();
        final Iterator<T6> it6 = s6.iterator();
        final Iterator<T7> it7 = s7.iterator();
        final Iterator<T8> it8 = s8.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext() && it6.hasNext() && it7.hasNext() && it8.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next(), it6.next(), it7.next(), it8.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 9 streams into one using a {@link Function9} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();
        final Iterator<T6> it6 = s6.iterator();
        final Iterator<T7> it7 = s7.iterator();
        final Iterator<T8> it8 = s8.iterator();
        final Iterator<T9> it9 = s9.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext() && it6.hasNext() && it7.hasNext() && it8.hasNext() && it9.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next(), it6.next(), it7.next(), it8.next(), it9.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 10 streams into one using a {@link Function10} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Function10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();
        final Iterator<T6> it6 = s6.iterator();
        final Iterator<T7> it7 = s7.iterator();
        final Iterator<T8> it8 = s8.iterator();
        final Iterator<T9> it9 = s9.iterator();
        final Iterator<T10> it10 = s10.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext() && it6.hasNext() && it7.hasNext() && it8.hasNext() && it9.hasNext() && it10.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next(), it6.next(), it7.next(), it8.next(), it9.next(), it10.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 11 streams into one using a {@link Function11} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Function11<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();
        final Iterator<T6> it6 = s6.iterator();
        final Iterator<T7> it7 = s7.iterator();
        final Iterator<T8> it8 = s8.iterator();
        final Iterator<T9> it9 = s9.iterator();
        final Iterator<T10> it10 = s10.iterator();
        final Iterator<T11> it11 = s11.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext() && it6.hasNext() && it7.hasNext() && it8.hasNext() && it9.hasNext() && it10.hasNext() && it11.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next(), it6.next(), it7.next(), it8.next(), it9.next(), it10.next(), it11.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 12 streams into one using a {@link Function12} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Function12<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();
        final Iterator<T6> it6 = s6.iterator();
        final Iterator<T7> it7 = s7.iterator();
        final Iterator<T8> it8 = s8.iterator();
        final Iterator<T9> it9 = s9.iterator();
        final Iterator<T10> it10 = s10.iterator();
        final Iterator<T11> it11 = s11.iterator();
        final Iterator<T12> it12 = s12.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext() && it6.hasNext() && it7.hasNext() && it8.hasNext() && it9.hasNext() && it10.hasNext() && it11.hasNext() && it12.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next(), it6.next(), it7.next(), it8.next(), it9.next(), it10.next(), it11.next(), it12.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 13 streams into one using a {@link Function13} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13, Function13<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();
        final Iterator<T6> it6 = s6.iterator();
        final Iterator<T7> it7 = s7.iterator();
        final Iterator<T8> it8 = s8.iterator();
        final Iterator<T9> it9 = s9.iterator();
        final Iterator<T10> it10 = s10.iterator();
        final Iterator<T11> it11 = s11.iterator();
        final Iterator<T12> it12 = s12.iterator();
        final Iterator<T13> it13 = s13.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext() && it6.hasNext() && it7.hasNext() && it8.hasNext() && it9.hasNext() && it10.hasNext() && it11.hasNext() && it12.hasNext() && it13.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next(), it6.next(), it7.next(), it8.next(), it9.next(), it10.next(), it11.next(), it12.next(), it13.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 14 streams into one using a {@link Function14} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13, Seq<T14> s14, Function14<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();
        final Iterator<T6> it6 = s6.iterator();
        final Iterator<T7> it7 = s7.iterator();
        final Iterator<T8> it8 = s8.iterator();
        final Iterator<T9> it9 = s9.iterator();
        final Iterator<T10> it10 = s10.iterator();
        final Iterator<T11> it11 = s11.iterator();
        final Iterator<T12> it12 = s12.iterator();
        final Iterator<T13> it13 = s13.iterator();
        final Iterator<T14> it14 = s14.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext() && it6.hasNext() && it7.hasNext() && it8.hasNext() && it9.hasNext() && it10.hasNext() && it11.hasNext() && it12.hasNext() && it13.hasNext() && it14.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next(), it6.next(), it7.next(), it8.next(), it9.next(), it10.next(), it11.next(), it12.next(), it13.next(), it14.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 15 streams into one using a {@link Function15} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13, Seq<T14> s14, Seq<T15> s15, Function15<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();
        final Iterator<T6> it6 = s6.iterator();
        final Iterator<T7> it7 = s7.iterator();
        final Iterator<T8> it8 = s8.iterator();
        final Iterator<T9> it9 = s9.iterator();
        final Iterator<T10> it10 = s10.iterator();
        final Iterator<T11> it11 = s11.iterator();
        final Iterator<T12> it12 = s12.iterator();
        final Iterator<T13> it13 = s13.iterator();
        final Iterator<T14> it14 = s14.iterator();
        final Iterator<T15> it15 = s15.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext() && it6.hasNext() && it7.hasNext() && it8.hasNext() && it9.hasNext() && it10.hasNext() && it11.hasNext() && it12.hasNext() && it13.hasNext() && it14.hasNext() && it15.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next(), it6.next(), it7.next(), it8.next(), it9.next(), it10.next(), it11.next(), it12.next(), it13.next(), it14.next(), it15.next());
            }
        }

        return seq(new Zip());
    }

    /**
     * Zip 16 streams into one using a {@link Function16} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> Seq<R> zip(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13, Seq<T14> s14, Seq<T15> s15, Seq<T16> s16, Function16<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? extends R> zipper) {
        final Iterator<T1> it1 = s1.iterator();
        final Iterator<T2> it2 = s2.iterator();
        final Iterator<T3> it3 = s3.iterator();
        final Iterator<T4> it4 = s4.iterator();
        final Iterator<T5> it5 = s5.iterator();
        final Iterator<T6> it6 = s6.iterator();
        final Iterator<T7> it7 = s7.iterator();
        final Iterator<T8> it8 = s8.iterator();
        final Iterator<T9> it9 = s9.iterator();
        final Iterator<T10> it10 = s10.iterator();
        final Iterator<T11> it11 = s11.iterator();
        final Iterator<T12> it12 = s12.iterator();
        final Iterator<T13> it13 = s13.iterator();
        final Iterator<T14> it14 = s14.iterator();
        final Iterator<T15> it15 = s15.iterator();
        final Iterator<T16> it16 = s16.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext() && it3.hasNext() && it4.hasNext() && it5.hasNext() && it6.hasNext() && it7.hasNext() && it8.hasNext() && it9.hasNext() && it10.hasNext() && it11.hasNext() && it12.hasNext() && it13.hasNext() && it14.hasNext() && it15.hasNext() && it16.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next(), it3.next(), it4.next(), it5.next(), it6.next(), it7.next(), it8.next(), it9.next(), it10.next(), it11.next(), it12.next(), it13.next(), it14.next(), it15.next(), it16.next());
            }
        }

        return seq(new Zip());
    }

// [jooq-tools] END [zip-static]

    /**
     * Zip a Stream with a corresponding Stream of indexes.
     * <p>
     * <code><pre>
     * // (tuple("a", 0), tuple("b", 1), tuple("c", 2))
     * Seq.of("a", "b", "c").zipWithIndex()
     * </pre></code>
     */
    static <T> Seq<Tuple2<T, Long>> zipWithIndex(Stream<T> stream) {
        return zipWithIndex(seq(stream));
    }

    /**
     * Zip a Stream with a corresponding Stream of indexes.
     * <p>
     * <code><pre>
     * // (tuple("a", 0), tuple("b", 1), tuple("c", 2))
     * Seq.of("a", "b", "c").zipWithIndex()
     * </pre></code>
     */
    static <T> Seq<Tuple2<T, Long>> zipWithIndex(Iterable<T> iterable) {
        return zipWithIndex(seq(iterable));
    }

    /**
     * Zip a Stream with a corresponding Stream of indexes.
     * <p>
     * <code><pre>
     * // (tuple("a", 0), tuple("b", 1), tuple("c", 2))
     * Seq.of("a", "b", "c").zipWithIndex()
     * </pre></code>
     */
    static <T> Seq<Tuple2<T, Long>> zipWithIndex(Seq<T> stream) {
        long[] index = { -1L };

        return transform(stream, (delegate, action) ->
                        delegate.tryAdvance(t ->
                                        action.accept(tuple(t, index[0] = index[0] + 1))
                        )
        );
    }

    /**
     * Fold a stream to the left.
     * <p>
     * <code><pre>
     * // "abc"
     * Seq.of("a", "b", "c").foldLeft("", (u, t) -> u + t)
     * </pre></code>
     */
    static <T, U> U foldLeft(Stream<T> stream, U seed, BiFunction<U, ? super T, U> function) {
        return foldLeft(seq(stream), seed, function);
    }

    /**
     * Fold a stream to the left.
     * <p>
     * <code><pre>
     * // "abc"
     * Seq.of("a", "b", "c").foldLeft("", (u, t) -> u + t)
     * </pre></code>
     */
    static <T, U> U foldLeft(Iterable<T> iterable, U seed, BiFunction<U, ? super T, U> function) {
        return foldLeft(seq(iterable), seed, function);
    }

    /**
     * Fold a stream to the left.
     * <p>
     * <code><pre>
     * // "abc"
     * Seq.of("a", "b", "c").foldLeft("", (u, t) -> u + t)
     * </pre></code>
     */
    static <T, U> U foldLeft(Seq<T> stream, U seed, BiFunction<U, ? super T, U> function) {
        final Iterator<T> it = stream.iterator();
        U result = seed;

        while (it.hasNext())
            result = function.apply(result, it.next());

        return result;
    }

    /**
     * Fold a stream to the right.
     * <p>
     * <code><pre>
     * // "cba"
     * Seq.of("a", "b", "c").foldRight("", (t, u) -> u + t)
     * </pre></code>
     */
    static <T, U> U foldRight(Stream<T> stream, U seed, BiFunction<? super T, U, U> function) {
        return foldRight(seq(stream), seed, function);
    }

    /**
     * Fold a stream to the right.
     * <p>
     * <code><pre>
     * // "cba"
     * Seq.of("a", "b", "c").foldRight("", (t, u) -> u + t)
     * </pre></code>
     */
    static <T, U> U foldRight(Iterable<T> iterable, U seed, BiFunction<? super T, U, U> function) {
        return foldRight(seq(iterable), seed, function);
    }

    /**
     * Fold a stream to the right.
     * <p>
     * <code><pre>
     * // "cba"
     * Seq.of("a", "b", "c").foldRight("", (t, u) -> u + t)
     * </pre></code>
     */
    static <T, U> U foldRight(Seq<T> stream, U seed, BiFunction<? super T, U, U> function) {
        return stream.reverse().foldLeft(seed, (u, t) -> function.apply(t, u));
    }

    /**
     * Scan a stream to the left.
     * <p>
     * <code><pre>
     * // ("", "a", "ab", "abc")
     * Seq.of("a", "b", "c").scanLeft("", (u, t) -> u + t)
     * </pre></code>
     */
    static <T, U> Seq<U> scanLeft(Stream<T> stream, U seed, BiFunction<U, ? super T, U> function) {
        return scanLeft(seq(stream), seed, function);
    }

    /**
     * Scan a stream to the left.
     * <p>
     * <code><pre>
     * // ("", "a", "ab", "abc")
     * Seq.of("a", "b", "c").scanLeft("", (u, t) -> u + t)
     * </pre></code>
     */
    static <T, U> Seq<U> scanLeft(Iterable<T> iterable, U seed, BiFunction<U, ? super T, U> function) {
        return scanLeft(seq(iterable), seed, function);
    }

    /**
     * Scan a stream to the left.
     * <p>
     * <code><pre>
     * // ("", "a", "ab", "abc")
     * Seq.of("a", "b", "c").scanLeft("", (u, t) -> u + t)
     * </pre></code>
     */
    static <T, U> Seq<U> scanLeft(Seq<T> stream, U seed, BiFunction<U, ? super T, U> function) {
        U[] value = (U[]) new Object[] { seed };

        return Seq.of(seed).concat(transform(stream, (delegate, action) ->
            delegate.tryAdvance(t ->
                action.accept(value[0] = function.apply(value[0], t))
            )
        ));
    }

    /**
     * Scan a stream to the right.
     * <p>
     * <code><pre>
     * // ("", "c", "cb", "cba")
     * Seq.of("a", "b", "c").scanRight("", (t, u) -> u + t)
     * </pre></code>
     */
    static <T, U> Seq<U> scanRight(Stream<T> stream, U seed, BiFunction<? super T, U, U> function) {
        return scanRight(seq(stream), seed, function);
    }

    /**
     * Scan a stream to the right.
     * <p>
     * <code><pre>
     * // ("", "c", "cb", "cba")
     * Seq.of("a", "b", "c").scanRight("", (t, u) -> u + t)
     * </pre></code>
     */
    static <T, U> Seq<U> scanRight(Iterable<T> iterable, U seed, BiFunction<? super T, U, U> function) {
        return scanRight(seq(iterable), seed, function);
    }

    /**
     * Scan a stream to the right.
     * <p>
     * <code><pre>
     * // ("", "c", "cb", "cba")
     * Seq.of("a", "b", "c").scanRight("", (t, u) -> u + t)
     * </pre></code>
     */
    static <T, U> Seq<U> scanRight(Seq<T> stream, U seed, BiFunction<? super T, U, U> function) {
        return stream.reverse().scanLeft(seed, (u, t) -> function.apply(t, u));
    }

    /**
     * Unfold a function into a stream.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5)
     * Seq.unfold(1, i -> i &lt;= 6 ? Optional.of(tuple(i, i + 1)) : Optional.empty())
     * </pre></code>
     */
    static <T, U> Seq<T> unfold(U seed, Function<U, Optional<Tuple2<T, U>>> unfolder) {
        Tuple2<T, U>[] unfolded = new Tuple2[] { tuple((T) null, seed) };

        return seq((FunctionalSpliterator<T>) action -> {
            Optional<Tuple2<T, U>> result = unfolder.apply(unfolded[0].v2);

            if (result.isPresent())
                action.accept((unfolded[0] = result.get()).v1);

            return result.isPresent();
        });
    }

    /**
     * Reverse a stream.
     * <p>
     * <code><pre>
     * // (3, 2, 1)
     * Seq.of(1, 2, 3).reverse()
     * </pre></code>
     */
    static <T> Seq<T> reverse(Stream<T> stream) {
        return reverse(seq(stream));
    }

    /**
     * Reverse a stream.
     * <p>
     * <code><pre>
     * // (3, 2, 1)
     * Seq.of(1, 2, 3).reverse()
     * </pre></code>
     */
    static <T> Seq<T> reverse(Iterable<T> iterable) {
        return reverse(seq(iterable));
    }

    /**
     * Reverse a stream.
     * <p>
     * <code><pre>
     * // (3, 2, 1)
     * Seq.of(1, 2, 3).reverse()
     * </pre></code>
     */
    static <T> Seq<T> reverse(Seq<T> stream) {
        List<T> list = toList(stream);
        Collections.reverse(list);
        return seq(list);
    }

    /**
     * Shuffle a stream
     * <p>
     * <code><pre>
     * // e.g. (2, 3, 1)
     * Seq.of(1, 2, 3).shuffle()
     * </pre></code>
     */
    static <T> Seq<T> shuffle(Stream<T> stream) {
        return shuffle(seq(stream));
    }

    /**
     * Shuffle a stream
     * <p>
     * <code><pre>
     * // e.g. (2, 3, 1)
     * Seq.of(1, 2, 3).shuffle()
     * </pre></code>
     */
    static <T> Seq<T> shuffle(Iterable<T> iterable) {
        return shuffle(seq(iterable));
    }

    /**
     * Shuffle a stream
     * <p>
     * <code><pre>
     * // e.g. (2, 3, 1)
     * Seq.of(1, 2, 3).shuffle()
     * </pre></code>
     */
    static <T> Seq<T> shuffle(Seq<T> stream) {
        List<T> list = toList(stream);
        Collections.shuffle(list);
        return seq(list);
    }

    /**
     * Shuffle a stream using specified source of randomness
     * <p>
     * <code><pre>
     * // e.g. (2, 3, 1)
     * Seq.of(1, 2, 3).shuffle(new Random())
     * </pre></code>
     */
    static <T> Seq<T> shuffle(Stream<T> stream, Random random) {
        return shuffle(seq(stream), random);
    }

    /**
     * Shuffle a stream using specified source of randomness
     * <p>
     * <code><pre>
     * // e.g. (2, 3, 1)
     * Seq.of(1, 2, 3).shuffle(new Random())
     * </pre></code>
     */
    static <T> Seq<T> shuffle(Iterable<T> iterable, Random random) {
        return shuffle(seq(iterable), random);
    }

    /**
     * Shuffle a stream using specified source of randomness
     * <p>
     * <code><pre>
     * // e.g. (2, 3, 1)
     * Seq.of(1, 2, 3).shuffle(new Random())
     * </pre></code>
     */
    static <T> Seq<T> shuffle(Seq<T> stream, Random random) {
        List<T> list = toList(stream);
        Collections.shuffle(list, random);
        return seq(list);
    }

    // [jooq-tools] START [crossjoin-static]

    /**
     * Cross join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2> Seq<Tuple2<T1, T2>> crossJoin(Stream<T1> s1, Stream<T2> s2) {
        return crossJoin(seq(s1), seq(s2));
    }

    /**
     * Cross join 3 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3> Seq<Tuple3<T1, T2, T3>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3) {
        return crossJoin(seq(s1), seq(s2), seq(s3));
    }

    /**
     * Cross join 4 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4> Seq<Tuple4<T1, T2, T3, T4>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4));
    }

    /**
     * Cross join 5 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5> Seq<Tuple5<T1, T2, T3, T4, T5>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5));
    }

    /**
     * Cross join 6 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6> Seq<Tuple6<T1, T2, T3, T4, T5, T6>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6));
    }

    /**
     * Cross join 7 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7> Seq<Tuple7<T1, T2, T3, T4, T5, T6, T7>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7));
    }

    /**
     * Cross join 8 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8> Seq<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8));
    }

    /**
     * Cross join 9 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Seq<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9));
    }

    /**
     * Cross join 10 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Seq<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10));
    }

    /**
     * Cross join 11 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Seq<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11));
    }

    /**
     * Cross join 12 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Seq<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12));
    }

    /**
     * Cross join 13 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Seq<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13));
    }

    /**
     * Cross join 14 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Seq<Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13, Stream<T14> s14) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13), seq(s14));
    }

    /**
     * Cross join 15 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Seq<Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13, Stream<T14> s14, Stream<T15> s15) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13), seq(s14), seq(s15));
    }

    /**
     * Cross join 16 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Seq<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> crossJoin(Stream<T1> s1, Stream<T2> s2, Stream<T3> s3, Stream<T4> s4, Stream<T5> s5, Stream<T6> s6, Stream<T7> s7, Stream<T8> s8, Stream<T9> s9, Stream<T10> s10, Stream<T11> s11, Stream<T12> s12, Stream<T13> s13, Stream<T14> s14, Stream<T15> s15, Stream<T16> s16) {
        return crossJoin(seq(s1), seq(s2), seq(s3), seq(s4), seq(s5), seq(s6), seq(s7), seq(s8), seq(s9), seq(s10), seq(s11), seq(s12), seq(s13), seq(s14), seq(s15), seq(s16));
    }

    /**
     * Cross join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2> Seq<Tuple2<T1, T2>> crossJoin(Iterable<T1> i1, Iterable<T2> i2) {
        return crossJoin(seq(i1), seq(i2));
    }

    /**
     * Cross join 3 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3> Seq<Tuple3<T1, T2, T3>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3) {
        return crossJoin(seq(i1), seq(i2), seq(i3));
    }

    /**
     * Cross join 4 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4> Seq<Tuple4<T1, T2, T3, T4>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4));
    }

    /**
     * Cross join 5 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5> Seq<Tuple5<T1, T2, T3, T4, T5>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5));
    }

    /**
     * Cross join 6 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6> Seq<Tuple6<T1, T2, T3, T4, T5, T6>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6));
    }

    /**
     * Cross join 7 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7> Seq<Tuple7<T1, T2, T3, T4, T5, T6, T7>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7));
    }

    /**
     * Cross join 8 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8> Seq<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8));
    }

    /**
     * Cross join 9 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Seq<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9));
    }

    /**
     * Cross join 10 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Seq<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10));
    }

    /**
     * Cross join 11 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Seq<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11));
    }

    /**
     * Cross join 12 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Seq<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12));
    }

    /**
     * Cross join 13 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Seq<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13));
    }

    /**
     * Cross join 14 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Seq<Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13, Iterable<T14> i14) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13), seq(i14));
    }

    /**
     * Cross join 15 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Seq<Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13, Iterable<T14> i14, Iterable<T15> i15) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13), seq(i14), seq(i15));
    }

    /**
     * Cross join 16 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Seq<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> crossJoin(Iterable<T1> i1, Iterable<T2> i2, Iterable<T3> i3, Iterable<T4> i4, Iterable<T5> i5, Iterable<T6> i6, Iterable<T7> i7, Iterable<T8> i8, Iterable<T9> i9, Iterable<T10> i10, Iterable<T11> i11, Iterable<T12> i12, Iterable<T13> i13, Iterable<T14> i14, Iterable<T15> i15, Iterable<T16> i16) {
        return crossJoin(seq(i1), seq(i2), seq(i3), seq(i4), seq(i5), seq(i6), seq(i7), seq(i8), seq(i9), seq(i10), seq(i11), seq(i12), seq(i13), seq(i14), seq(i15), seq(i16));
    }

    /**
     * Cross join 2 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2> Seq<Tuple2<T1, T2>> crossJoin(Seq<T1> s1, Seq<T2> s2) {
        List<T2> list = s2.toList();
        return seq(s1).flatMap(v1 -> seq(list).map(v2 -> tuple(v1, v2)));
    }

    /**
     * Cross join 3 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3> Seq<Tuple3<T1, T2, T3>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3) {
        List<Tuple2<T2, T3>> list = crossJoin(s2, s3).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2)));
    }

    /**
     * Cross join 4 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4> Seq<Tuple4<T1, T2, T3, T4>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4) {
        List<Tuple3<T2, T3, T4>> list = crossJoin(s2, s3, s4).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3)));
    }

    /**
     * Cross join 5 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5> Seq<Tuple5<T1, T2, T3, T4, T5>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5) {
        List<Tuple4<T2, T3, T4, T5>> list = crossJoin(s2, s3, s4, s5).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4)));
    }

    /**
     * Cross join 6 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6> Seq<Tuple6<T1, T2, T3, T4, T5, T6>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6) {
        List<Tuple5<T2, T3, T4, T5, T6>> list = crossJoin(s2, s3, s4, s5, s6).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4, t.v5)));
    }

    /**
     * Cross join 7 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7> Seq<Tuple7<T1, T2, T3, T4, T5, T6, T7>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7) {
        List<Tuple6<T2, T3, T4, T5, T6, T7>> list = crossJoin(s2, s3, s4, s5, s6, s7).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4, t.v5, t.v6)));
    }

    /**
     * Cross join 8 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8> Seq<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8) {
        List<Tuple7<T2, T3, T4, T5, T6, T7, T8>> list = crossJoin(s2, s3, s4, s5, s6, s7, s8).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4, t.v5, t.v6, t.v7)));
    }

    /**
     * Cross join 9 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Seq<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9) {
        List<Tuple8<T2, T3, T4, T5, T6, T7, T8, T9>> list = crossJoin(s2, s3, s4, s5, s6, s7, s8, s9).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4, t.v5, t.v6, t.v7, t.v8)));
    }

    /**
     * Cross join 10 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Seq<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10) {
        List<Tuple9<T2, T3, T4, T5, T6, T7, T8, T9, T10>> list = crossJoin(s2, s3, s4, s5, s6, s7, s8, s9, s10).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4, t.v5, t.v6, t.v7, t.v8, t.v9)));
    }

    /**
     * Cross join 11 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Seq<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11) {
        List<Tuple10<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> list = crossJoin(s2, s3, s4, s5, s6, s7, s8, s9, s10, s11).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4, t.v5, t.v6, t.v7, t.v8, t.v9, t.v10)));
    }

    /**
     * Cross join 12 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Seq<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12) {
        List<Tuple11<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> list = crossJoin(s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4, t.v5, t.v6, t.v7, t.v8, t.v9, t.v10, t.v11)));
    }

    /**
     * Cross join 13 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Seq<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13) {
        List<Tuple12<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> list = crossJoin(s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4, t.v5, t.v6, t.v7, t.v8, t.v9, t.v10, t.v11, t.v12)));
    }

    /**
     * Cross join 14 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Seq<Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13, Seq<T14> s14) {
        List<Tuple13<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> list = crossJoin(s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4, t.v5, t.v6, t.v7, t.v8, t.v9, t.v10, t.v11, t.v12, t.v13)));
    }

    /**
     * Cross join 15 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Seq<Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13, Seq<T14> s14, Seq<T15> s15) {
        List<Tuple14<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> list = crossJoin(s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4, t.v5, t.v6, t.v7, t.v8, t.v9, t.v10, t.v11, t.v12, t.v13, t.v14)));
    }

    /**
     * Cross join 16 streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
     * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Seq<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> crossJoin(Seq<T1> s1, Seq<T2> s2, Seq<T3> s3, Seq<T4> s4, Seq<T5> s5, Seq<T6> s6, Seq<T7> s7, Seq<T8> s8, Seq<T9> s9, Seq<T10> s10, Seq<T11> s11, Seq<T12> s12, Seq<T13> s13, Seq<T14> s14, Seq<T15> s15, Seq<T16> s16) {
        List<Tuple15<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> list = crossJoin(s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16).toList();
        return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, t.v1, t.v2, t.v3, t.v4, t.v5, t.v6, t.v7, t.v8, t.v9, t.v10, t.v11, t.v12, t.v13, t.v14, t.v15)));
    }

// [jooq-tools] END [crossjoin-static]

    /**
     * Concatenate a number of streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).concat(Seq.of(4, 5, 6))
     * </pre></code>
     */
    @SafeVarargs
    static <T> Seq<T> concat(Stream<T>... streams) {
        return concat(SeqUtils.seqs(streams));
    }

    /**
     * Concatenate a number of streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).concat(Seq.of(4, 5, 6))
     * </pre></code>
     */
    @SafeVarargs
    static <T> Seq<T> concat(Iterable<T>... iterables) {
        return concat(SeqUtils.seqs(iterables));
    }

    /**
     * Concatenate a number of streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).concat(Seq.of(4, 5, 6))
     * </pre></code>
     */
    @SafeVarargs
    static <T> Seq<T> concat(Seq<T>... streams) {
        if (streams == null || streams.length == 0)
            return Seq.empty();

        if (streams.length == 1)
            return seq(streams[0]);

        Stream<T> result = streams[0];
        for (int i = 1; i < streams.length; i++)
            result = Stream.concat(result, streams[i]);

        return seq(result);
    }

    /**
     * Duplicate a Streams into two equivalent Streams.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (1, 2, 3))
     * Seq.of(1, 2, 3).duplicate()
     * </pre></code>
     */
    static <T> Tuple2<Seq<T>, Seq<T>> duplicate(Stream<T> stream) {
        final LinkedList<T> gap = new LinkedList<>();
        final Iterator<T> it = stream.iterator();

        @SuppressWarnings({"unchecked"})
        final Iterator<T>[] ahead = new Iterator[] { null };

        class Duplicate implements Iterator<T> {
            @Override
            public boolean hasNext() {
                if (ahead[0] == null || ahead[0] == this)
                    return it.hasNext();

                return !gap.isEmpty();
            }

            @Override
            public T next() {
                if (ahead[0] == null)
                    ahead[0] = this;

                if (ahead[0] == this) {
                    T value = it.next();
                    gap.offer(value);
                    return value;
                }
                else {
                    T value = gap.poll();

                    if (gap.isEmpty())
                        ahead[0] = null;

                    return value;
                }
            }
        }

        return tuple(seq(new Duplicate()), seq(new Duplicate()));
    }

    /**
     * Consume a stream and concatenate all elements.
     */
    static String toString(Stream<?> stream) {
        return toString(stream, "");
    }

    /**
     * Consume a stream and concatenate all elements using a separator.
     */
    static String toString(Stream<?> stream, String separator) {
        return stream.map(Objects::toString).collect(Collectors.joining(separator));
    }

    /**
     * Collect a Stream into a List.
     */
    static <T, C extends Collection<T>> C toCollection(Stream<T> stream, Supplier<C> collectionFactory) {
        return stream.collect(Collectors.toCollection(collectionFactory));
    }

    /**
     * Collect a Stream into a List.
     */
    static <T> List<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }

    /**
     * Collect a Stream into a Set.
     */
    static <T> Set<T> toSet(Stream<T> stream) {
        return stream.collect(Collectors.toSet());
    }

    /**
     * Collect a Stream of {@link Tuple2} into a Map.
     */
    static <T, K, V> Map<K, V> toMap(Stream<Tuple2<K, V>> stream) {
        return stream.collect(Collectors.toMap(Tuple2::v1, Tuple2::v2));
    }

    /**
     * Collect a Stream into a Map.
     */
    static <T, K, V> Map<K, V> toMap(Stream<T> stream, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return stream.collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * Returns a limited interval from a given Stream.
     * <p>
     * <code><pre>
     * // (4, 5)
     * Seq.of(1, 2, 3, 4, 5, 6).slice(3, 5)
     * </pre></code>
     */
    static <T> Seq<T> slice(Stream<T> stream, long from, long to) {
        long f = Math.max(from, 0);
        long t = Math.max(to - f, 0);

        return seq(stream.skip(f).limit(t));
    }

    /**
     * Returns a stream with n elements skipped.
     * <p>
     * <code><pre>
     * // (4, 5, 6)
     * Seq.of(1, 2, 3, 4, 5, 6).skip(3)
     * </pre></code>
     */
    static <T> Seq<T> skip(Stream<T> stream, long elements) {
        return seq(stream.skip(elements));
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (3, 4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipWhile(i -> i &lt; 3)
     * </pre></code>
     */
    static <T> Seq<T> skipWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return skipUntil(stream, predicate.negate());
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>true</code>
     * plus the first element for which it evaluates to <code>false</code>.
     * <p>
     * <code><pre>
     * // (4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipWhileClosed(i -> i &lt; 3)
     * </pre></code>
     */
    static <T> Seq<T> skipWhileClosed(Stream<T> stream, Predicate<? super T> predicate) {
        return skipUntilClosed(stream, predicate.negate());
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>false</code>.
     * <p>
     * <code><pre>
     * // (3, 4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipUntil(i -> i == 3)
     * </pre></code>
     */
    @SuppressWarnings("unchecked")
    static <T> Seq<T> skipUntil(Stream<T> stream, Predicate<? super T> predicate) {
        boolean[] test = { false };

        return transform(stream, (delegate, action) -> !test[0]
            ?   delegate.tryAdvance(t -> {
                    if (test[0] = predicate.test(t))
                        action.accept(t);
                })
            :   delegate.tryAdvance(action)
        );
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>false</code>
     * plus the first element for which it evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipUntilClosed(i -> i == 3)
     * </pre></code>
     */
    @SuppressWarnings("unchecked")
    static <T> Seq<T> skipUntilClosed(Stream<T> stream, Predicate<? super T> predicate) {
        boolean[] test = { false };

        return transform(stream, (delegate, action) -> !test[0]
            ? delegate.tryAdvance(t -> test[0] = predicate.test(t))
            : delegate.tryAdvance(action)
        );
    }

    /**
     * Returns a stream limited to n elements.
     * <p>
     * <code><pre>
     * // (1, 2, 3)
     * Seq.of(1, 2, 3, 4, 5, 6).limit(3)
     * </pre></code>
     */
    static <T> Seq<T> limit(Stream<T> stream, long elements) {
        return seq(stream.limit(elements));
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (1, 2)
     * Seq.of(1, 2, 3, 4, 5).limitWhile(i -> i &lt; 3)
     * </pre></code>
     */
    static <T> Seq<T> limitWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return limitUntil(stream, predicate.negate());
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>
     * plus the first element for which it evaluates to <code>false</code>.
     * <p>
     * <code><pre>
     * // (1, 2, 3)
     * Seq.of(1, 2, 3, 4, 5).limitWhileClosed(i -> i &lt; 3)
     * </pre></code>
     */
    static <T> Seq<T> limitWhileClosed(Stream<T> stream, Predicate<? super T> predicate) {
        return limitUntilClosed(stream, predicate.negate());
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>false</code>.
     * <p>
     * <code><pre>
     * // (1, 2)
     * Seq.of(1, 2, 3, 4, 5).limitUntil(i -> i == 3)
     * </pre></code>
     */
    @SuppressWarnings("unchecked")
    static <T> Seq<T> limitUntil(Stream<T> stream, Predicate<? super T> predicate) {
        boolean[] test = { false };

        return transform(stream, (delegate, action) ->
            delegate.tryAdvance(t -> {
                if (!(test[0] = predicate.test(t)))
                    action.accept(t);
            }) && !test[0]
        );
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>false</code>
     * plus the first element for which it evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (1, 2, 3)
     * Seq.of(1, 2, 3, 4, 5).limitUntilClosed(i -> i == 3)
     * </pre></code>
     */
    @SuppressWarnings("unchecked")
    static <T> Seq<T> limitUntilClosed(Stream<T> stream, Predicate<? super T> predicate) {
        boolean[] test = { false };

        return transform(stream, (delegate, action) ->
            !test[0] && delegate.tryAdvance(t -> {
                test[0] = predicate.test(t);
                action.accept(t);
            })
        );
    }

    /**
     * Returns a stream with a given value interspersed between any two values of this stream.
     * <p>
     * <code><pre>
     * // (1, 0, 2, 0, 3, 0, 4)
     * Seq.of(1, 2, 3, 4).intersperse(0)
     * </pre></code>
     */
    static <T> Seq<T> intersperse(Stream<T> stream, T value) {
        return seq(stream.flatMap(t -> Stream.of(value, t)).skip(1));
    }

    /**
     * Classify this stream's elements according to a given classifier function
     * <p>
     * <code><pre>
     * // Seq(tuple(1, Seq(1, 3, 5)), tuple(0, Seq(2, 4, 6)))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2 )
     * // Seq(tuple(true, Seq(1, 3, 5)), tuple(false, Seq(2, 4, 6)))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2 != 0)
     * </pre></code>
     *
     * This is a non-terminal analog of {@link #groupBy(Stream, Function)})
     * @see #groupBy(Function)
     * @see #partition(Predicate)
     */
    public static <K, T> Seq<Tuple2<K, Seq<T>>> grouped(Stream<T> stream, Function<? super T, ? extends K> classifier) {
        return grouped(seq(stream), classifier);
    }

    /**
     * Classify this stream's elements according to a given classifier function
     * <p>
     * <code><pre>
     * // Seq(tuple(1, Seq(1, 3, 5)), tuple(0, Seq(2, 4, 6)))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2 )
     * // Seq(tuple(true, Seq(1, 3, 5)), tuple(false, Seq(2, 4, 6)))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2 != 0)
     * </pre></code>
     *
     * This is a non-terminal analog of {@link #groupBy(Stream, Function)})
     * @see #groupBy(Function)
     * @see #partition(Predicate)
     */
    public static <K, T> Seq<Tuple2<K, Seq<T>>> grouped(Iterable<T> iterable, Function<? super T, ? extends K> classifier) {
        return grouped(seq(iterable), classifier);
    }

    /**
     * Classify this stream's elements according to a given classifier function
     * <p>
     * <code><pre>
     * // Seq(tuple(1, Seq(1, 3, 5)), tuple(0, Seq(2, 4, 6)))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2 )
     * // Seq(tuple(true, Seq(1, 3, 5)), tuple(false, Seq(2, 4, 6)))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2 != 0)
     * </pre></code>
     *
     * This is a non-terminal analog of {@link #groupBy(Stream, Function)})
     * @see #groupBy(Function)
     * @see #partition(Predicate)
     */
    public static <K, T> Seq<Tuple2<K, Seq<T>>> grouped(Seq<T> seq, Function<? super T, ? extends K> classifier) {
        final Iterator<T> it = seq.iterator();

        class ClassifyingIterator implements Iterator<Tuple2<K, Seq<T>>> {
            final Map<K, Queue<T>> buffers = new LinkedHashMap<>();
            final Queue<K> keys = new LinkedList<>();

            class Classification implements Iterator<T> {
                final K key;
                Queue<T> buffer;

                Classification(K key) {
                    this.key = key;
                }

                void fetchClassification() {
                    if (buffer == null)
                        buffer = buffers.get(key);

                    while (buffer.isEmpty() && it.hasNext())
                        fetchNextNewKey();
                }

                @Override
                public boolean hasNext() {
                    fetchClassification();
                    return !buffer.isEmpty();
                }

                @Override
                public T next() {
                    return buffer.poll();
                }
            }

            void fetchClassifying() {
                while (it.hasNext() && fetchNextNewKey());
            }

            boolean fetchNextNewKey() {
                T next = it.next();
                K nextK = classifier.apply(next);

                Queue<T> buffer = buffers.get(nextK);

                try {
                    if (buffer == null) {
                        buffer = new ArrayDeque<>();
                        buffers.put(nextK, buffer);
                        keys.add(nextK);
                        return true;
                    }
                }
                finally {
                    buffer.offer(next);
                }

                return false;
            }

            @Override
            public boolean hasNext() {
                fetchClassifying();
                return !keys.isEmpty();
            }

            @Override
            public Tuple2<K, Seq<T>> next() {
                K nextK = keys.poll();
                return tuple(nextK, seq(new Classification(nextK)));
            }
        }

        return seq(new ClassifyingIterator());
    }

    /**
     * Classify this stream's elements according to a given classifier function
     * and collect each class's elements using a collector.
     * <p>
     * <code><pre>
     * // Seq(tuple(1, 9), tuple(0, 12))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2, Collectors.summingInt(i -> i))
     * // Seq(tuple(true, 9), tuple(false, 12))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2 != 0, Collectors.summingInt(i -> i))
     * </pre></code> This is a non-terminal analog of
     * {@link #groupBy(Function, Collector)})
     *
     * @see #groupBy(Function, Collector)
     */
    public static <K, T, A, D> Seq<Tuple2<K, D>> grouped(Stream<T> stream, Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream) {
        return grouped(seq(stream), classifier, downstream);
    }

    /**
     * Classify this stream's elements according to a given classifier function
     * and collect each class's elements using a collector.
     * <p>
     * <code><pre>
     * // Seq(tuple(1, 9), tuple(0, 12))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2, Collectors.summingInt(i -> i))
     * // Seq(tuple(true, 9), tuple(false, 12))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2 != 0, Collectors.summingInt(i -> i))
     * </pre></code> This is a non-terminal analog of
     * {@link #groupBy(Function, Collector)})
     *
     * @see #groupBy(Function, Collector)
     */
    public static <K, T, A, D> Seq<Tuple2<K, D>> grouped(Iterable<T> iterable, Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream) {
        return grouped(seq(iterable), classifier, downstream);
    }

    /**
     * Classify this stream's elements according to a given classifier function
     * and collect each class's elements using a collector.
     * <p>
     * <code><pre>
     * // Seq(tuple(1, 9), tuple(0, 12))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2, Collectors.summingInt(i -> i))
     * // Seq(tuple(true, 9), tuple(false, 12))
     * Seq.of(1, 2, 3, 4, 5, 6).grouped(i -> i % 2 != 0, Collectors.summingInt(i -> i))
     * </pre></code> This is a non-terminal analog of
     * {@link #groupBy(Function, Collector)})
     *
     * @see #groupBy(Function, Collector)
     */
    public static <K, T, A, D> Seq<Tuple2<K, D>> grouped(Seq<T> seq, Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream) {
        return grouped(seq, classifier).map(t -> tuple(t.v1, t.v2.collect(downstream)));
    }

    /**
     * Partition a stream into two given a predicate.
     * <p>
     * <code><pre>
     * // tuple((1, 3, 5), (2, 4, 6))
     * Seq.of(1, 2, 3, 4, 5, 6).partition(i -> i % 2 != 0)
     * </pre></code>
     */
    static <T> Tuple2<Seq<T>, Seq<T>> partition(Stream<T> stream, Predicate<? super T> predicate) {
        final Iterator<T> it = stream.iterator();
        final LinkedList<T> buffer1 = new LinkedList<>();
        final LinkedList<T> buffer2 = new LinkedList<>();

        class Partition implements Iterator<T> {

            final boolean b;

            Partition(boolean b) {
                this.b = b;
            }

            void fetch() {
                while (buffer(b).isEmpty() && it.hasNext()) {
                    T next = it.next();
                    buffer(predicate.test(next)).offer(next);
                }
            }

            LinkedList<T> buffer(boolean test) {
                return test ? buffer1 : buffer2;
            }

            @Override
            public boolean hasNext() {
                fetch();
                return !buffer(b).isEmpty();
            }

            @Override
            public T next() {
                return buffer(b).poll();
            }
        }

        return tuple(seq(new Partition(true)), seq(new Partition(false)));
    }

    /**
     * Split a stream at a given position.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (4, 5, 6))
     * Seq.of(1, 2, 3, 4, 5, 6).splitAt(3)
     * </pre></code>
     */
    static <T> Tuple2<Seq<T>, Seq<T>> splitAt(Stream<T> stream, long position) {
        return seq(stream)
            .zipWithIndex()
            .partition(t -> t.v2 < position)
            // Explicit type parameters to work around this Eclipse compiler bug:
            // https://bugs.eclipse.org/bugs/show_bug.cgi?id=455945
            .map((v1, v2) -> Tuple.<Seq<T>, Seq<T>>tuple(
                v1.map(t -> t.v1),
                v2.map(t -> t.v1)
            ));
    }

    /**
     * Split a stream at the head.
     * <p>
     * <code><pre>
     * // tuple(1, (2, 3, 4, 5, 6))
     * Seq.of(1, 2, 3, 4, 5, 6).splitHead(3)
     * </pre></code>
     */
    static <T> Tuple2<Optional<T>, Seq<T>> splitAtHead(Stream<T> stream) {
        Iterator<T> it = stream.iterator();
        return tuple(it.hasNext() ? Optional.of(it.next()) : Optional.empty(), seq(it));
    }

    // Methods taken from LINQ
    // -----------------------

    /**
     * Keep only those elements in a stream that are of a given type.
     * <p>
     * <code><pre>
     * // (1, 2, 3)
     * Seq.of(1, "a", 2, "b", 3).ofType(Integer.class)
     * </pre></code>
     */
    @SuppressWarnings("unchecked")
    static <T, U> Seq<U> ofType(Stream<T> stream, Class<U> type) {
        return seq(stream).filter(type::isInstance).map(t -> (U) t);
    }

    /**
     * Cast all elements in a stream to a given type, possibly throwing a {@link ClassCastException}.
     * <p>
     * <code><pre>
     * // ClassCastException
     * Seq.of(1, "a", 2, "b", 3).cast(Integer.class)
     * </pre></code>
     */
    static <T, U> Seq<U> cast(Stream<T> stream, Class<U> type) {
        return seq(stream).map(type::cast);
    }

    // Shortcuts to Collectors
    // -----------------------

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function)} collector.
     */
    static <T, K> Map<K, List<T>> groupBy(Stream<T> stream, Function<? super T, ? extends K> classifier) {
        return seq(stream).groupBy(classifier);
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function, Collector)} collector.
     */
    static <T, K, A, D> Map<K, D> groupBy(Stream<T> stream, Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream) {
        return seq(stream).groupBy(classifier, downstream);
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function, Supplier, Collector)} collector.
     */
    static <T, K, D, A, M extends Map<K, D>> M groupBy(Stream<T> stream, Function<? super T, ? extends K> classifier, Supplier<M> mapFactory, Collector<? super T, A, D> downstream) {
        return seq(stream).groupBy(classifier, mapFactory, downstream);
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining()}
     * collector.
     */
    static String join(Stream<?> stream) {
        return seq(stream).join();
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining(CharSequence)}
     * collector.
     */
    static String join(Stream<?> stream, CharSequence delimiter) {
        return seq(stream).join(delimiter);
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining(CharSequence, CharSequence, CharSequence)}
     * collector.
     */
    static String join(Stream<?> stream, CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return seq(stream).join(delimiter, prefix, suffix);
    }

    // Covariant overriding of Stream return types
    // -------------------------------------------

    @Override
    Seq<T> filter(Predicate<? super T> predicate);

    @Override
    <R> Seq<R> map(Function<? super T, ? extends R> mapper);

    @Override
    IntStream mapToInt(ToIntFunction<? super T> mapper);

    @Override
    LongStream mapToLong(ToLongFunction<? super T> mapper);

    @Override
    DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper);

    @Override
    <R> Seq<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);

    @Override
    IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper);

    @Override
    LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper);

    @Override
    DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper);

    @Override
    Seq<T> distinct();

    @Override
    Seq<T> sorted();

    @Override
    Seq<T> sorted(Comparator<? super T> comparator);

    @Override
    Seq<T> peek(Consumer<? super T> action);

    @Override
    Seq<T> limit(long maxSize);

    @Override
    Seq<T> skip(long n);

    @Override
    Seq<T> onClose(Runnable closeHandler);

    @Override
    void close();

    @Override
    long count();

    // These methods have no effect
    // ----------------------------

    /**
     * Returns this stream. All Seq streams are sequential, hence the name.
     *
     * @return this stream unmodified
     */
    @Override
    default Seq<T> sequential() {
        return this;
    }

    /**
     * Seq streams are always sequential and, as such, doesn't support
     * parallelization.
     *
     * @return this sequential stream unmodified
     * @see <a href="https://github.com/jOOQ/jOOL/issues/130">jOOL Issue #130</a>
     */
    @Override
    default Seq<T> parallel() {
        return this;
    }

    /**
     * Returns this stream. All Seq streams are ordered so this method has
     * no effect.
     *
     * @return this stream unmodified
     */
    @Override
    default Seq<T> unordered() {
        return this;
    }

    @Override
    default Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    // Debugging tools
    // ---------------

    /**
     * Print contents of this stream to {@link System#out}.
     */
    default void printOut() {
        print(System.out);
    }

    /**
     * Print contents of this stream to {@link System#err}.
     */
    default void printErr() {
        print(System.err);
    }

    /**
     * Print contents of this stream to the argument writer.
     */
    default void print(PrintWriter writer) {
        forEach(writer::println);
    }

    /**
     * Print contents of this stream to the argument stream.
     */
    default void print(PrintStream stream) {
        forEach(stream::println);
    }
}
