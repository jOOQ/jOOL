/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.lambda;

import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static org.jooq.lambda.tuple.Tuple.tuple;

/**
 * A sequential, ordered {@link Stream} that adds all sorts of useful methods that work only because
 * it is sequential and ordered.
 *
 * @author Lukas Eder
 */
public interface Seq<T> extends Stream<T> {

    /**
     * The underlying {@link Stream} implementation.
     */
    Stream<T> stream();

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
        return Seq.concat(new Stream[]{this, other});
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
    default <U, R> Seq<R> zip(Seq<U> other, BiFunction<T, U, R> zipper) {
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
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (3, 4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipWhile(i -> i < 3)
     * </pre></code>
     *
     * @see #skipWhile(Stream, Predicate)
     */
    default Seq<T> skipWhile(Predicate<? super T> predicate) {
        return skipWhile(this, predicate);
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
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (1, 2)
     * Seq.of(1, 2, 3, 4, 5).limitWhile(i -> i < 3)
     * </pre></code>
     *
     * @see #limitWhile(Stream, Predicate)
     */
    default Seq<T> limitWhile(Predicate<? super T> predicate) {
        return limitWhile(this, predicate);
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
     * Returns a stream with a given value interspersed between any two values of this stream.
     * <p>
     * <code><pre>
     * // (1, 0, 2, 0, 3, 0, 4)
     * Seq.of(1, 2, 3, 4).intersperse(0)
     * </pre></code>
     *
     * @see #intersperse(Stream, T)
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
     * Collect a Stream into a Set.
     *
     * @see #toSet(Stream)
     */
    default Set<T> toSet() {
        return toSet(this);
    }

    /**
     * Collect a Stream into a Map.
     *
     * @see #toMap(Stream, Function, Function)
     */
    default <K, V> Map<K, V> toMap(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return toMap(this, keyMapper, valueMapper);
    }

    /**
     * Consume a stream and concatenate all elements using a separator.
     */
    default String toString(String separator) {
        return toString(this, separator);
    }

    /**
     * Get the maximum value by a function.
     */
    default <U extends Comparable<U>> Optional<T> minBy(Function<T, U> function) {
        return minBy(function, naturalOrder());
    }

    /**
     * Get the maximum value by a function.
     */
    default <U> Optional<T> minBy(Function<T, U> function, Comparator<? super U> comparator) {
        return map(t -> tuple(t, function.apply(t)))
              .min(comparing(Tuple2::v2, comparator))
              .map(t -> t.v1);
    }

    /**
     * Get the maximum value by a function.
     */
    default <U extends Comparable<U>> Optional<T> maxBy(Function<T, U> function) {
        return maxBy(function, naturalOrder());
    }

    /**
     * Get the maximum value by a function.
     */
    default <U> Optional<T> maxBy(Function<T, U> function, Comparator<? super U> comparator) {
        return map(t -> tuple(t, function.apply(t)))
              .max(comparing(Tuple2::v2, comparator))
              .map(t -> t.v1);
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
    static <T> Seq<T> of(T... values) {
        return seq(Stream.of(values));
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
     * Wrap a Stream into a Seq.
     */
    static <T> Seq<T> seq(Stream<T> stream) {
        if (stream instanceof Seq)
            return (Seq<T>) stream;

        return new SeqImpl<>(stream);
    }

    /**
     * Wrap an Iterable into a Seq.
     */
    static <T> Seq<T> seq(Iterable<T> iterable) {
        return seq(iterable.iterator());
    }

    /**
     * Wrap an Iterator into a Seq.
     */
    static <T> Seq<T> seq(Iterator<T> iterator) {
        return seq(StreamSupport.stream(spliteratorUnknownSize(iterator, ORDERED), false));
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
        final List<T> list = new ArrayList<T>();

        class Cycle implements Iterator<T> {
            boolean cycled;
            Iterator<T> it;

            Cycle(Iterator<T> it) {
                this.it = it;
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                if (!it.hasNext()) {
                    cycled = true;
                    it = list.iterator();
                }

                T next = it.next();

                if (!cycled) {
                    list.add(next);
                }

                return next;
            }
        }

        return seq(new Cycle(stream.iterator()));
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
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Stream<Tuple2<T1, T2>> stream, Function<T1, U1> leftUnzipper, Function<T2, U2> rightUnzipper) {
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
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Stream<Tuple2<T1, T2>> stream, Function<Tuple2<T1, T2>, Tuple2<U1, U2>> unzipper) {
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
    static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Stream<Tuple2<T1, T2>> stream, BiFunction<T1, T2, Tuple2<U1, U2>> unzipper) {
        return seq(stream)
              .map(t -> unzipper.apply(t.v1, t.v2))
              .duplicate()
              .map1(s -> s.map(u -> u.v1))
              .map2(s -> s.map(u -> u.v2));
    }

    /**
     * Zip two streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    static <T1, T2> Seq<Tuple2<T1, T2>> zip(Stream<T1> left, Stream<T2> right) {
        return zip(left, right, Tuple::tuple);
    }

    /**
     * Zip two streams into one using a {@link BiFunction} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    static <T1, T2, R> Seq<R> zip(Stream<T1> left, Stream<T2> right, BiFunction<T1, T2, R> zipper) {
        final Iterator<T1> it1 = left.iterator();
        final Iterator<T2> it2 = right.iterator();

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
     * Zip a Stream with a corresponding Stream of indexes.
     * <p>
     * <code><pre>
     * // (tuple("a", 0), tuple("b", 1), tuple("c", 2))
     * Seq.of("a", "b", "c").zipWithIndex()
     * </pre></code>
     */
    static <T> Seq<Tuple2<T, Long>> zipWithIndex(Stream<T> stream) {
        final Iterator<T> it = stream.iterator();

        class ZipWithIndex implements Iterator<Tuple2<T, Long>> {
            long index;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Tuple2<T, Long> next() {
                return tuple(it.next(), index++);
            }
        }

        return seq(new ZipWithIndex());
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
        return seq(stream).reverse().foldLeft(seed, (u, t) -> function.apply(t, u));
    }

    /**
     * Unfold a function into a stream.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5)
     * Seq.unfold(1, i -> i <= 6 ? Optional.of(tuple(i, i + 1)) : Optional.empty())
     * </pre></code>
     */
    static <T, U> Seq<T> unfold(U seed, Function<U, Optional<Tuple2<T, U>>> unfolder) {
        class Unfold implements Iterator<T> {
            U u;
            Optional<Tuple2<T, U>> unfolded;

            public Unfold(U u) {
                this.u = u;
            }

            void unfold() {
                if (unfolded == null)
                    unfolded = unfolder.apply(u);
            }

            @Override
            public boolean hasNext() {
                unfold();
                return unfolded.isPresent();
            }

            @Override
            public T next() {
                unfold();

                try {
                    return unfolded.get().v1;
                }
                finally {
                    u = unfolded.get().v2;
                    unfolded = null;
                }
            }
        }

        return seq(new Unfold(seed));
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
        List<T> list = toList(stream);
        Collections.reverse(list);
        return seq(list);
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
    static <T> Seq<T> concat(Stream<T>... streams) {
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

        @SuppressWarnings("unchecked")
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

                return gap.poll();
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
     * Collect a Stream into a Map.
     */
    static <T, K, V> Map<K, V> toMap(Stream<T> stream, Function<T, K> keyMapper, Function<T, V> valueMapper) {
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
     * Seq.of(1, 2, 3, 4, 5).skipWhile(i -> i < 3)
     * </pre></code>
     */
    static <T> Seq<T> skipWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return skipUntil(stream, predicate.negate());
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>false</code>.
     * <p>
     * <code><pre>
     * // (3, 4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipUntil(i -> i == 3)
     * </pre></code>
     */
    static <T> Seq<T> skipUntil(Stream<T> stream, Predicate<? super T> predicate) {
        final Iterator<T> it = stream.iterator();

        class SkipUntil implements Iterator<T> {
            T next = (T) SeqImpl.NULL;
            boolean test = false;

            void skip() {
                while (next == SeqImpl.NULL && it.hasNext()) {
                    next = it.next();

                    if (test || (test = predicate.test(next)))
                        break;
                    else
                        next = (T) SeqImpl.NULL;
                }
            }

            @Override
            public boolean hasNext() {
                skip();
                return next != SeqImpl.NULL;
            }

            @Override
            public T next() {
                if (next == SeqImpl.NULL)
                    throw new NoSuchElementException();

                try {
                    return next;
                }
                finally {
                    next = (T) SeqImpl.NULL;
                }
            }
        }

        return seq(new SkipUntil());
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
     * Seq.of(1, 2, 3, 4, 5).limitWhile(i -> i < 3)
     * </pre></code>
     */
    static <T> Seq<T> limitWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return limitUntil(stream, predicate.negate());
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (1, 2)
     * Seq.of(1, 2, 3, 4, 5).limitUntil(i -> i == 3)
     * </pre></code>
     */
    static <T> Seq<T> limitUntil(Stream<T> stream, Predicate<? super T> predicate) {
        final Iterator<T> it = stream.iterator();

        class LimitUntil implements Iterator<T> {
            T next = (T) SeqImpl.NULL;
            boolean test = false;

            void test() {
                if (!test && next == SeqImpl.NULL && it.hasNext()) {
                    next = it.next();

                    if (test = predicate.test(next))
                        next = (T) SeqImpl.NULL;
                }
            }

            @Override
            public boolean hasNext() {
                test();
                return next != SeqImpl.NULL;
            }

            @Override
            public T next() {
                if (next == SeqImpl.NULL)
                    throw new NoSuchElementException();

                try {
                    return next;
                }
                finally {
                    next = (T) SeqImpl.NULL;
                }
            }
        }

        return seq(new LimitUntil());
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
            .map((v1, v2) -> tuple(
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

    // These methods have no effect
    // ----------------------------

    @Override
    default Seq<T> sequential() {
        return this;
    }

    @Override
    default Seq<T> parallel() {
        return this;
    }

    @Override
    default Seq<T> unordered() {
        return this;
    }
}
