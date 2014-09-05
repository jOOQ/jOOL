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
package org.jooq.lambda;

import org.jooq.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;
import static org.jooq.lambda.tuple.Tuple.tuple;

/**
 * All missing functionality in the JDK streams.
 *
 * @author Lukas Eder
 */
public final class Streams {

    /**
     * Zip two streams into one.
     *
     * @param left The left stream producing {@link Tuple2#v1} values.
     * @param right The right stream producing {@link Tuple2#v1} values.
     * @param <T1> The left data type
     * @param <T2> The right data type
     * @return The zipped stream.
     */
    public static <T1, T2> Stream<Tuple2<T1, T2>> zip(Stream<T1> left, Stream<T2> right) {
        return zip(left, right, (t1, t2) -> tuple(t1, t2));
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
    public static <T1, T2, R> Stream<R> zip(Stream<T1> left, Stream<T2> right, BiFunction<T1, T2, R> zipper) {
        final Iterator<T1> it1 = left.iterator();
        final Iterator<T2> it2 = right.iterator();

        return stream(
            spliteratorUnknownSize(
                new Iterator<R>() {
                    @Override
                    public boolean hasNext() {
                        return it1.hasNext() && it2.hasNext();
                    }

                    @Override
                    public R next() {
                        return zipper.apply(it1.next(), it2.next());
                    }
                },
                ORDERED
            ),
            false
        );
    }

    /**
     * Concatenate a number of streams
     */
    @SafeVarargs
    public static <T> Stream<T> concat(Stream<T>... streams) {
        if (streams == null || streams.length == 0)
            return Stream.empty();

        if (streams.length == 1)
            return streams[0];

        Stream<T> result = streams[0];
        for (int i = 1; i < streams.length; i++)
            result = Stream.concat(result, streams[i]);

        return result;
    }

    public static <T> Tuple2<Stream<T>, Stream<T>> duplicate(Stream<T> stream) {
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
            stream(spliteratorUnknownSize(new Duplicate(), ORDERED), false),
            stream(spliteratorUnknownSize(new Duplicate(), ORDERED), false)
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
    public static <T> Stream<T> slice(Stream<T> stream, long from, long to) {
        long f = Math.max(from, 0);
        long t = Math.max(to - f, 0);

        return stream.skip(f).limit(t);
    }

    /**
     * Returns a stream with n elements skipped.
     */
    public static <T> Stream<T> skip(Stream<T> stream, long elements) {
        return stream.skip(elements);
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>true</code>.
     */
    public static <T> Stream<T> skipWhile(Stream<T> stream, Predicate<T> predicate) {
        return skipUntil(stream, predicate.negate());
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>false</code>.
     */
    public static <T> Stream<T> skipUntil(Stream<T> stream, Predicate<T> predicate) {
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

        return stream(spliteratorUnknownSize(new SkipUntil(), ORDERED), false);
    }

    /**
     * Returns a stream limited to n elements.
     */
    public static <T> Stream<T> limit(Stream<T> stream, long elements) {
        return stream.limit(elements);
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>.
     */
    public static <T> Stream<T> limitWhile(Stream<T> stream, Predicate<T> predicate) {
        return limitUntil(stream, predicate.negate());
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>.
     */
    public static <T> Stream<T> limitUntil(Stream<T> stream, Predicate<T> predicate) {
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

        return stream(spliteratorUnknownSize(new LimitUntil(), ORDERED), false);
    }
}
