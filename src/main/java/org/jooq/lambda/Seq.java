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
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static org.jooq.lambda.tuple.Tuple.tuple;
import static java.util.stream.StreamSupport.stream;

/**
 * A wrapper for a {@link Stream} that adds all sorts of useful methods that work only on sequential 
 *
 * @author Lukas Eder
 */
public interface Seq<T> extends Stream<T> {

    Stream<T> stream();

    default <U> Seq<Tuple2<T, U>> zip(Seq<U> other) {
        return zip(this, other);
    }

    default <U, R> Seq<R> zip(Seq<U> other, BiFunction<T, U, R> zipper) {
        return zip(this, other, zipper);
    }

    default Seq<Tuple2<T, Long>> zipWithIndex() {
        return zipWithIndex(this);
    }

    default Seq<T> skipWhile(Predicate<? super T> predicate) {
        return skipWhile(this, predicate);
    }

    default Seq<T> skipUntil(Predicate<? super T> predicate) {
        return skipUntil(this, predicate);
    }

    default Seq<T> limitWhile(Predicate<? super T> predicate) {
        return limitWhile(this, predicate);
    }

    default Seq<T> limitUntil(Predicate<? super T> predicate) {
        return limitUntil(this, predicate);
    }

    default Tuple2<Seq<T>, Seq<T>> duplicate() {
        return duplicate(this);
    }

    default Seq<T> slice(long from, long to) {
        return slice(this, from, to);
    }

    default List<T> toList() {
        return toList(this);
    }

    default Set<T> toSet() {
        return toSet(this);
    }

    default String toString(String separator) {
        return toString(this, separator);
    }

    static <T> Seq<T> of(T value) {
        return seq(Stream.of(value));
    }

    static <T> Seq<T> of(T... values) {
        return seq(Stream.of(values));
    }

    static <T> Seq<T> empty() {
        return seq(Stream.empty());
    }

    static<T> Seq<T> iterate(final T seed, final UnaryOperator<T> f) {
        return seq(Stream.iterate(seed, f));
    }

    static<T> Seq<T> generate(Supplier<T> s) {
        return seq(Stream.generate(s));
    }

    static <T> Seq<T> seq(Stream<T> stream) {
        if (stream instanceof Seq)
            return (Seq<T>) stream;

        return new SeqImpl<>(stream);
    }

    /**
     * Zip two streams into one.
     *
     * @param left The left stream producing {@link org.jooq.lambda.tuple.Tuple2#v1} values.
     * @param right The right stream producing {@link org.jooq.lambda.tuple.Tuple2#v1} values.
     * @return The zipped stream.
     */
    public static <T1, T2> Seq<Tuple2<T1, T2>> zip(Stream<T1> left, Stream<T2> right) {
        return zip(left, right, Tuple::tuple);
    }

    /**
     * Zip two streams into one using a {@link BiFunction} to produce resulting values.
     *
     * @param left The left stream producing the first argument to the zipper.
     * @param right The right stream producing the second argument to the zipper.
     * @param zipper The function producing the output values.
     * @param <T1> The left data type.
     * @param <T2> The right data type.
     * @param <R> The result data type.
     * @return The zipped stream.
     */
    public static <T1, T2, R> Seq<R> zip(Stream<T1> left, Stream<T2> right, BiFunction<T1, T2, R> zipper) {
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

        return seq(StreamSupport.stream(spliteratorUnknownSize(new Zip(), ORDERED), false));
    }

    public static <T> Seq<Tuple2<T, Long>> zipWithIndex(Stream<T> stream) {
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

        return seq(StreamSupport.stream(spliteratorUnknownSize(new ZipWithIndex(), ORDERED), false));
    }

    /**
     * Concatenate a number of streams
     */
    @SafeVarargs
    public static <T> Seq<T> concat(Stream<T>... streams) {
        if (streams == null || streams.length == 0)
            return Seq.empty();

        if (streams.length == 1)
            return seq(streams[0]);

        Stream<T> result = streams[0];
        for (int i = 1; i < streams.length; i++)
            result = Stream.concat(result, streams[i]);

        return seq(result);
    }

    public static <T> Tuple2<Seq<T>, Seq<T>> duplicate(Stream<T> stream) {
        final LinkedList<T> gap = new LinkedList<>();
        final Iterator<T> it = stream.iterator();

        @SuppressWarnings("unchecked")
        final Iterator<T>[] ahead = new Iterator[] { null };

        class Duplicate implements Iterator<T> {
            @Override
            public boolean hasNext() {
                synchronized (it) {
                    if (ahead[0] == null || ahead[0] == this)
                        return it.hasNext();

                    return !gap.isEmpty();
                }
            }

            @Override
            public T next() {
                synchronized (it) {
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
        }

        return tuple(
            seq(StreamSupport.stream(spliteratorUnknownSize(new Duplicate(), ORDERED), false)),
            seq(StreamSupport.stream(spliteratorUnknownSize(new Duplicate(), ORDERED), false))
        );
    }

    /**
     * Consume a stream and concatenate all elements.
     */
    public static String toString(Stream<?> stream) {
        return toString(stream, "");
    }

    /**
     * Consume a stream and concatenate all elements using a separator.
     */
    public static String toString(Stream<?> stream, String separator) {
        return stream.map(Objects::toString).collect(Collectors.joining(separator));
    }

    /**
     * Collect a Stream into a List.
     */
    public static <T> List<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }

    /**
     * Collect a Stream into a Set.
     */
    public static <T> Set<T> toSet(Stream<T> stream) {
        return stream.collect(Collectors.toSet());
    }

    /**
     * Returns a limited interval from a given Stream.
     *
     * @param stream The input Stream
     * @param from The first element to consider from the Stream.
     * @param to The first element not to consider from the Stream.
     * @param <T> The stream element type
     * @return The limited interval Stream
     */
    public static <T> Seq<T> slice(Stream<T> stream, long from, long to) {
        long f = Math.max(from, 0);
        long t = Math.max(to - f, 0);

        return seq(stream.skip(f).limit(t));
    }

    /**
     * Returns a stream with n elements skipped.
     */
    public static <T> Seq<T> skip(Stream<T> stream, long elements) {
        return seq(stream.skip(elements));
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>true</code>.
     */
    public static <T> Seq<T> skipWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return skipUntil(stream, predicate.negate());
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>false</code>.
     */
    public static <T> Seq<T> skipUntil(Stream<T> stream, Predicate<? super T> predicate) {
        final Iterator<T> it = stream.iterator();

        class SkipUntil implements Iterator<T> {
            T next;
            boolean test = false;

            void skip() {
                while (next == null && it.hasNext()) {
                    next = it.next();

                    if (test || (test = predicate.test(next)))
                        break;
                    else
                        next = null;
                }
            }

            @Override
            public boolean hasNext() {
                skip();
                return next != null;
            }

            @Override
            public T next() {
                if (next == null)
                    throw new NoSuchElementException();

                try {
                    return next;
                }
                finally {
                    next = null;
                }
            }
        }

        return seq(StreamSupport.stream(spliteratorUnknownSize(new SkipUntil(), ORDERED), false));
    }

    /**
     * Returns a stream limited to n elements.
     */
    public static <T> Seq<T> limit(Stream<T> stream, long elements) {
        return seq(stream.limit(elements));
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>.
     */
    public static <T> Seq<T> limitWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return limitUntil(stream, predicate.negate());
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>.
     */
    public static <T> Seq<T> limitUntil(Stream<T> stream, Predicate<? super T> predicate) {
        final Iterator<T> it = stream.iterator();

        class LimitUntil implements Iterator<T> {
            T next;
            boolean test = false;

            void test() {
                if (!test && next == null && it.hasNext()) {
                    next = it.next();

                    if (test = predicate.test(next))
                        next = null;
                }
            }

            @Override
            public boolean hasNext() {
                test();
                return next != null;
            }

            @Override
            public T next() {
                if (next == null)
                    throw new NoSuchElementException();

                try {
                    return next;
                }
                finally {
                    next = null;
                }
            }
        }

        return seq(StreamSupport.stream(spliteratorUnknownSize(new LimitUntil(), ORDERED), false));
    }
}
