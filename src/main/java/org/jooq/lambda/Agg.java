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

import org.jooq.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;
import static org.jooq.lambda.tuple.Tuple.tuple;

/**
 * A set of additional {@link Collector} implementations.
 * <p>
 * The class name isn't set in stone and will change.
 *
 * @author Lukas Eder
 */
public class Agg {

    /**
     * Get a {@link Collector} that calculates the <code>COUNT(*)</code> function.
     */
    public static <T> Collector<T, ?, Long> count() {
        return Collectors.counting();
    }

    /**
     * Get a {@link Collector} that calculates the <code>COUNT (DISTINCT *)</code> function.
     */
    public static <T> Collector<T, ?, Long> countDistinct() {
        return countDistinctBy(t -> t);
    }

    /**
     * Get a {@link Collector} that calculates the <code>COUNT (DISTINCT expr)</code> function.
     */
    public static <T, U> Collector<T, ?, Long> countDistinctBy(Function<? super T, ? extends U> function) {
        return Collector.of(
            () -> new HashSet<U>(),
            (s, v) -> s.add(function.apply(v)),
            (s1, s2) -> {
                s1.addAll(s2);
                return s1;
            },
            s -> (long) s.size()
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN()</code> function.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<T>> min() {
        return minBy(t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN()</code> function.
     */
    public static <T> Collector<T, ?, Optional<T>> min(Comparator<? super T> comparator) {
        return minBy(t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN(expr)</code> function.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<T>> minBy(Function<? super T, ? extends U> function) {
        return minBy(function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN(expr)</code> function.
     */
    public static <T, U> Collector<T, ?, Optional<T>> minBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return maxBy(function, comparator.reversed());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX()</code> function.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<T>> max() {
        return maxBy(t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX()</code> function.
     */
    public static <T> Collector<T, ?, Optional<T>> max(Comparator<? super T> comparator) {
        return maxBy(t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX(expr)</code> function.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<T>> maxBy(Function<? super T, ? extends U> function) {
        return maxBy(function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN(expr)</code> function.
     */
    public static <T, U> Collector<T, ?, Optional<T>> maxBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return Collector.of(
            () -> (Tuple2<T, U>[]) new Tuple2[] { tuple(null, null) },
            (a, t) -> {
                U u = function.apply(t);
                if (a[0].v2 == null || comparator.compare(a[0].v2, u) < 0)
                    a[0] = tuple(t, u);
            },
            (a1, a2) -> comparator.compare(a1[0].v2, a2[0].v2) < 0 ? a2 : a1,
            a -> Optional.ofNullable(a[0].v1)
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>MODE()</code> function.
     */
    public static <T> Collector<T, ?, Optional<T>> mode() {
        return Collector.of(
            () -> new LinkedHashMap<T, Long>(),
            (m, v) -> m.compute(v, (k1, v1) -> v1 == null ? 1L : v1 + 1L),
            (m1, m2) -> {
                m1.putAll(m2);
                return m1;
            },
            m -> Seq.seq(m).maxBy(t -> t.v2).map(t -> t.v1)
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>RANK()</code> function given natural ordering.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<Long>> rank(T value) {
        return rank(value, t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>RANK()</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<Long>> rank(T value, Comparator<? super T> comparator) {
        return rank(value, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>RANK()</code> function given natural ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<Long>> rank(U value, Function<? super T, ? extends U> function) {
        return rank(value, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>RANK()</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<Long>> rank(U value, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return Collector.of(
            () -> new ArrayList<U>(),
            (l, v) -> l.add(function.apply(v)),
            (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            },
            l -> {
                int size = l.size();

                if (size == 0)
                    return Optional.empty();

                // TODO: Find a faster implementation using binarySearch
                l.sort(comparator);
                for (int i = 0; i < size; i++)
                    if (comparator.compare(value, l.get(i)) <= 0)
                        return Optional.of((long) i);

                return Optional.of((long) size);
            }
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>DENSE_RANK()</code> function given natural ordering.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<Long>> denseRank(T value) {
        return denseRank(value, t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>DENSE_RANK()</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<Long>> denseRank(T value, Comparator<? super T> comparator) {
        return denseRank(value, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>DENSE_RANK()</code> function given natural ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<Long>> denseRank(U value, Function<? super T, ? extends U> function) {
        return denseRank(value, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>DENSE_RANK()</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<Long>> denseRank(U value, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return Collector.of(
            () -> new TreeSet<U>(comparator),
            (l, v) -> l.add(function.apply(v)),
            (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            },
            l -> {
                int size = l.size();

                if (size == 0)
                    return Optional.empty();

                // TODO: Find a faster implementation using binarySearch
                int i = -1;
                Iterator<U> it = l.iterator();
                while (it.hasNext() && i++ < l.size())
                    if (comparator.compare(value, it.next()) <= 0)
                        return Optional.of((long) i);

                return Optional.of((long) size);
            }
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENT_RANK()</code> function given natural ordering.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<Double>> percentRank(T value) {
        return percentRank(value, t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENT_RANK()</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<Double>> percentRank(T value, Comparator<? super T> comparator) {
        return percentRank(value, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>PERCENT_RANK()</code> function given natural ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<Double>> percentRank(U value, Function<? super T, ? extends U> function) {
        return percentRank(value, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>PERCENT_RANK()</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<Double>> percentRank(U value, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return Collector.of(
            () -> new ArrayList<U>(),
            (l, v) -> l.add(function.apply(v)),
            (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            },
            l -> {
                int size = l.size();

                if (size == 0)
                    return Optional.empty();

                // TODO: Find a faster implementation using binarySearch
                l.sort(comparator);
                for (int i = 0; i < size; i++)
                    if (comparator.compare(value, l.get(i)) <= 0)
                        return Optional.of((double) i / (double) size);

                return Optional.of(1.0);
            }
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>MEDIAN()</code> function given natural ordering.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<T>> median() {
        return percentileBy(0.5, t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MEDIAN()</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<T>> median(Comparator<? super T> comparator) {
        return percentileBy(0.5, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>MEDIAN()</code> function given natural ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<T>> medianBy(Function<? super T, ? extends U> function) {
        return percentileBy(0.5, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>MEDIAN()</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<T>> medianBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return percentileBy(0.5, function, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENTILE_DISC(percentile)</code> function given natural ordering.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<T>> percentile(double percentile) {
        return percentile(percentile, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<T>> percentile(double percentile, Comparator<? super T> comparator) {
        return percentileBy(percentile, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>PERCENTILE_DISC(percentile)</code> function given natural ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<T>> percentileBy(double percentile, Function<? super T, ? extends U> function) {
        return percentileBy(percentile, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<T>> percentileBy(double percentile, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        if (percentile < 0.0 || percentile > 1.0)
            throw new IllegalArgumentException("Percentile must be between 0.0 and 1.0");

        // At a later stage, we'll optimise this implementation in case that function is the identity function
        return Collector.of(
            () -> new ArrayList<Tuple2<T, U>>(),
            (l, v) -> l.add(tuple(v, function.apply(v))),
            (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            },
            l -> {
                int size = l.size();

                if (size == 0)
                    return Optional.empty();
                else if (size == 1)
                    return Optional.of(l.get(0).v1);

                l.sort(Comparator.comparing(t -> t.v2, comparator));

                if (percentile == 0.0)
                    return Optional.of(l.get(0).v1);
                else if (percentile == 1.0)
                    return Optional.of(l.get(size - 1).v1);

                // x.5 should be rounded down
                return Optional.of(l.get((int) -Math.round(-(size * percentile + 0.5)) - 1).v1);
            }
        );
    }
}
