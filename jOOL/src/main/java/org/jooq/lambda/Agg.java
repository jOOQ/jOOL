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

import org.jooq.lambda.function.Function2;
import org.jooq.lambda.function.Function3;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;
import static org.jooq.lambda.tuple.Tuple.collectors;
import static org.jooq.lambda.tuple.Tuple.tuple;

/**
 * A set of additional {@link Collector} implementations.
 * <p>
 * The class name isn't set in stone and will change.
 *
 * @author Lukas Eder
 * @author Jichen Lu
 */
public class Agg {
    private Agg () {}


    /**
     * Get a {@link Collector} that filters data passed to downstream collector.
     */
    public static <T, A, R> Collector<T, ?, R> filter(Predicate<? super T> predicate, Collector<T, A, R> downstream) {
        return Collector.of(
            downstream.supplier(),
            (c, t) -> {
                if (predicate.test(t))
                    downstream.accumulator().accept(c, t);
            },
            downstream.combiner(),
            downstream.finisher()
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>FIRST</code> function.
     * <p>
     * Note that unlike in (Oracle) SQL, where the <code>FIRST</code> function
     * is an ordered set aggregate function that produces a set of results, this
     * collector just produces the first value in the order of stream traversal.
     * For matching behaviour to Oracle's [ aggregate function ] KEEP
     * (DENSE_RANK FIRST ORDER BY ... ), use {@link #maxAll(Comparator)} instead.
     */
    public static <T> Collector<T, ?, Optional<T>> first() {
        return reducing((v1, v2) -> v1);
    }

    /**
     * Get a {@link Collector} that calculates the <code>LAST</code> function.
     * <p>
     * Note that unlike in (Oracle) SQL, where the <code>FIRST</code> function
     * is an ordered set aggregate function that produces a set of results, this
     * collector just produces the first value in the order of stream traversal.
     * For matching behaviour to Oracle's [ aggregate function ] KEEP
     * (DENSE_RANK LAST ORDER BY ... ), use {@link #minAll(Comparator)} instead.
     */
    public static <T> Collector<T, ?, Optional<T>> last() {
        return reducing((v1, v2) -> v2);
    }

    /**
     * Get a {@link Collector} that takes the first <code>n</code> elements
     * from a collection.
     */
    public static <T> Collector<T, ?, Seq<T>> taking(long n) {
        return Collector.of(
            () -> new ArrayList<T>(),
            (l, v) -> {
                if (l.size() < n)
                    l.add(v);
            },
            (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            },
            Seq::seq
        );
    }

    /**
     * Get a {@link Collector} that skip the first <code>n</code> elements of a
     * collection.
     */
    public static <T> Collector<T, ?, Seq<T>> dropping(long n) {
        class Accumulator {
            List<T> list = new ArrayList<>();
            long index;
        }

        return Collector.of(
            Accumulator::new,
            (a, v) -> {
                if (a.index >= n)
                    a.list.add(v);
                else
                    a.index++;
            },
            (a1, a2) -> {
                a1.list.addAll(a2.list);
                a1.index += a2.index;
                return a1;
            },
            a -> Seq.seq(a.list)
        );
    }


    /**
     * Get a {@link Collector} that calculates the <code>COUNT(*)</code>
     * function.
     */
    public static <T> Collector<T, ?, Long> count() {
        return counting();
    }

    /**
     * Get a {@link Collector} that calculates the
     * <code>COUNT (DISTINCT *)</code> function.
     */
    public static <T> Collector<T, ?, Long> countDistinct() {
        return countDistinctBy(t -> t);
    }

    /**
     * Get a {@link Collector} that calculates the
     * <code>COUNT (DISTINCT expr)</code> function.
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
     * Get a {@link Collector} that calculates the <code>SUM()</code> for any
     * type of {@link Number}.
     */
    public static <T> Collector<T, ?, Optional<T>> sum() {
        return sum(t -> t);
    }

    /**
     * Get a {@link Collector} that calculates the <code>SUM()</code> for any
     * type of {@link Number}.
     */
    @SuppressWarnings({ "unchecked" })
    public static <T, U> Collector<T, ?, Optional<U>> sum(Function<? super T, ? extends U> function) {
        return Collector.of(() -> (Sum<U>[]) new Sum[1],
            (s, v) -> {
                if (s[0] == null)
                    s[0] = Sum.create(function.apply(v));
                else
                    s[0].add(function.apply(v));
            },
            (s1, s2) -> {
                s1[0].add(s2[0]);
                return s1;
            },
            s -> s[0] == null ? Optional.empty() : Optional.of(s[0].result())
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>AVG()</code> for any
     * type of {@link Number}.
     */
    public static <T> Collector<T, ?, Optional<T>> avg() {
        return avg(t -> t);
    }

    /**
     * Get a {@link Collector} that calculates the <code>AVG()</code> for any
     * type of {@link Number}.
     */
    @SuppressWarnings({ "unchecked" })
    public static <T, U> Collector<T, ?, Optional<U>> avg(Function<? super T, ? extends U> function) {
        return Collector.of(
            () -> (Sum<U>[]) new Sum[1],
            (s, v) -> {
                if (s[0] == null)
                    s[0] = Sum.create(function.apply(v));
                else
                    s[0].add(function.apply(v));
            },
            (s1, s2) -> {
                s1[0].add(s2[0]);
                return s1;
            },
            s -> s[0] == null ? Optional.empty() : Optional.of(s[0].avg())
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
     * Get a {@link Collector} that calculates the <code>MIN()</code> function.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<U>> min(Function<? super T, ? extends U> function) {
        return min(function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN()</code> function.
     */
    public static <T, U> Collector<T, ?, Optional<U>> min(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collectingAndThen(minBy(function, comparator), t -> t.map(function));
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN()</code> function.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<T>> minBy(Function<? super T, ? extends U> function) {
        return minBy(function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN()</code> function.
     */
    public static <T, U> Collector<T, ?, Optional<T>> minBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return maxBy(function, comparator.reversed());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN()</code> function, producing multiple results.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Seq<T>> minAll() {
        return minAllBy(t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN()</code> function, producing multiple results.
     */
    public static <T> Collector<T, ?, Seq<T>> minAll(Comparator<? super T> comparator) {
        return minAllBy(t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN()</code> function, producing multiple results.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Seq<U>> minAll(Function<? super T, ? extends U> function) {
        return minAll(function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN()</code> function, producing multiple results.
     */
    public static <T, U> Collector<T, ?, Seq<U>> minAll(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collectingAndThen(minAllBy(function, comparator), t -> t.map(function));
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN()</code> function, producing multiple results.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Seq<T>> minAllBy(Function<? super T, ? extends U> function) {
        return minAllBy(function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MIN()</code> function, producing multiple results.
     */
    public static <T, U> Collector<T, ?, Seq<T>> minAllBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return maxAllBy(function, comparator.reversed());
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
     * Get a {@link Collector} that calculates the <code>MAX()</code> function.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<U>> max(Function<? super T, ? extends U> function) {
        return max(function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX()</code> function.
     */
    public static <T, U> Collector<T, ?, Optional<U>> max(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collectingAndThen(maxBy(function, comparator), t -> t.map(function));
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX()</code> function.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<T>> maxBy(Function<? super T, ? extends U> function) {
        return maxBy(function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX()</code> function.
     */
    public static <T, U> Collector<T, ?, Optional<T>> maxBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        class Accumulator {
            T t;
            U u;
        }

        return Collector.of(
            Accumulator::new,
            (a, t) -> {
                U u = function.apply(t);

                if (a.u == null || comparator.compare(a.u, u) < 0) {
                    a.t = t;
                    a.u = u;
                }
            },
            (a1, a2) ->
                  a1.u == null
                ? a2
                : a2.u == null
                ? a1
                : comparator.compare(a1.u, a2.u) < 0
                ? a2
                : a1,
            a -> Optional.ofNullable(a.t)
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX()</code> function, producing multiple results.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Seq<T>> maxAll() {
        return maxAllBy(t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX()</code> function, producing multiple results.
     */
    public static <T> Collector<T, ?, Seq<T>> maxAll(Comparator<? super T> comparator) {
        return maxAllBy(t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX()</code> function, producing multiple results.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Seq<U>> maxAll(Function<? super T, ? extends U> function) {
        return maxAll(function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX()</code> function, producing multiple results.
     */
    public static <T, U> Collector<T, ?, Seq<U>> maxAll(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collectingAndThen(maxAllBy(function, comparator), t -> t.map(function));
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX()</code> function, producing multiple results.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Seq<T>> maxAllBy(Function<? super T, ? extends U> function) {
        return maxAllBy(function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>MAX()</code> function, producing multiple results.
     */
    public static <T, U> Collector<T, ?, Seq<T>> maxAllBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        class Accumulator {
            List<T> t = new ArrayList<>();
            U u;
            void set(T t, U u) {
                this.t.clear();
                this.t.add(t);
                this.u = u;
            }
        }

        return Collector.of(
            Accumulator::new,
            (a, t) -> {
                U u = function.apply(t);
                if (a.u == null) {
                    a.set(t, u);
                }
                else {
                    int compare = comparator.compare(a.u, u);

                    if (compare < 0)
                        a.set(t, u);
                    else if (compare == 0)
                        a.t.add(t);
                }
            },
            (a1, a2) -> {
                if (a1.u == null)
                    return a2;
                if (a2.u == null)
                    return a1;

                int compare = comparator.compare(a1.u, a2.u);

                if (compare < 0)
                    return a1;
                else if (compare > 0)
                    return a2;

                a1.t.addAll(a2.t);
                return a1;
            },
            a -> Seq.seq(a.t)
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>ALL()</code> function.
     */
    public static Collector<Boolean, ?, Boolean> allMatch() {
        return allMatch(t -> t);
    }

    /**
     * Get a {@link Collector} that calculates the <code>ALL()</code> function.
     */
    public static <T> Collector<T, ?, Boolean> allMatch(Predicate<? super T> predicate) {
        return Collector.of(
            () -> new Boolean[1],
            (a, t) -> {
                if (a[0] == null)
                    a[0] = predicate.test(t);
                else
                    a[0] = a[0] && predicate.test(t);
            },
            (a1, a2) -> {
                a1[0] = a1[0] && a2[0];
                return a1;
            },
            a -> a[0] == null || a[0]
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>ANY()</code> function.
     */
    public static Collector<Boolean, ?, Boolean> anyMatch() {
        return anyMatch(t -> t);
    }

    /**
     * Get a {@link Collector} that calculates the <code>ANY()</code> function.
     */
    public static <T> Collector<T, ?, Boolean> anyMatch(Predicate<? super T> predicate) {
        return collectingAndThen(noneMatch(predicate), t -> !t);
    }

    /**
     * Get a {@link Collector} that calculates the <code>NONE()</code> function.
     */
    public static Collector<Boolean, ?, Boolean> noneMatch() {
        return noneMatch(t -> t);
    }

    /**
     * Get a {@link Collector} that calculates the <code>NONE()</code> function.
     */
    public static <T> Collector<T, ?, Boolean> noneMatch(Predicate<? super T> predicate) {
        return allMatch(predicate.negate());
    }

    /**
     * Get a {@link Collector} that calculates <code>BIT_AND()</code> for any
     * type of {@link Number}.
     */
    public static <T> Collector<T, ?, Optional<T>> bitAnd() {
        return bitAnd(t -> t);
    }

    /**
     * Get a {@link Collector} that calculates the <code>BIT_AND()</code> for any
     * type of {@link Number}.
     */
    @SuppressWarnings({ "unchecked" })
    public static <T, U> Collector<T, ?, Optional<U>> bitAnd(Function<? super T, ? extends U> function) {
        return Collector.of(() -> (Sum<U>[]) new Sum[1],
            (s, v) -> {
                if (s[0] == null)
                    s[0] = Sum.create(function.apply(v));
                else
                    s[0].and(function.apply(v));
            },
            (s1, s2) -> {
                s1[0].and(s2[0]);
                return s1;
            },
            s -> s[0] == null ? Optional.empty() : Optional.of(s[0].result())
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>BIT_AND()</code> for any
     * type of {@link Number}.
     */
    public static <T> Collector<T, ?, Integer> bitAndInt(ToIntFunction<? super T> function) {
        return Collector.of(() -> new int[] { Integer.MAX_VALUE },
            (s, v) -> s[0] = s[0] & function.applyAsInt(v),
            (s1, s2) -> {
                s1[0] = s1[0] & s2[0];
                return s1;
            },
            s -> s[0]
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>BIT_AND()</code> for any
     * type of {@link Number}.
     */
    public static <T> Collector<T, ?, Long> bitAndLong(ToLongFunction<? super T> function) {
        return Collector.of(() -> new long[] { Long.MAX_VALUE },
            (s, v) -> s[0] = s[0] & function.applyAsLong(v),
            (s1, s2) -> {
                s1[0] = s1[0] & s2[0];
                return s1;
            },
            s -> s[0]
        );
    }

    /**
     * Get a {@link Collector} that calculates <code>BIT_OR()</code> for any
     * type of {@link Number}.
     */
    public static <T> Collector<T, ?, Optional<T>> bitOr() {
        return bitOr(t -> t);
    }

    /**
     * Get a {@link Collector} that calculates the <code>BIT_OR()</code> for any
     * type of {@link Number}.
     */
    @SuppressWarnings({ "unchecked" })
    public static <T, U> Collector<T, ?, Optional<U>> bitOr(Function<? super T, ? extends U> function) {
        return Collector.of(() -> (Sum<U>[]) new Sum[1],
            (s, v) -> {
                if (s[0] == null)
                    s[0] = Sum.create(function.apply(v));
                else
                    s[0].or(function.apply(v));
            },
            (s1, s2) -> {
                s1[0].or(s2[0]);
                return s1;
            },
            s -> s[0] == null ? Optional.empty() : Optional.of(s[0].result())
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>BIT_OR()</code> for any
     * type of {@link Number}.
     */
    public static <T> Collector<T, ?, Integer> bitOrInt(ToIntFunction<? super T> function) {
        return Collector.of(() -> new int[1],
            (s, v) -> s[0] = s[0] | function.applyAsInt(v),
            (s1, s2) -> {
                s1[0] = s1[0] | s2[0];
                return s1;
            },
            s -> s[0]
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>BIT_OR()</code> for any
     * type of {@link Number}.
     */
    public static <T> Collector<T, ?, Long> bitOrLong(ToLongFunction<? super T> function) {
        return Collector.of(() -> new long[1],
            (s, v) -> s[0] = s[0] | function.applyAsLong(v),
            (s1, s2) -> {
                s1[0] = s1[0] | s2[0];
                return s1;
            },
            s -> s[0]
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>MODE()</code> function.
     */
    public static <T> Collector<T, ?, Optional<T>> mode() {
        return mode0(seq -> seq.maxBy(t -> t.v2).map(t -> t.v1));
    }

    /**
     * Get a {@link Collector} that calculates the <code>MODE()</code> function.
     */
    public static <T> Collector<T, ?, Seq<T>> modeAll() {
        return mode0(seq -> seq.maxAllBy(t -> t.v2).map(t -> t.v1));
    }

    private static <T, X> Collector<T, ?, X> mode0(Function<? super Seq<Tuple2<T, Long>>, ? extends X> transformer) {
        return Collector.of(
            () -> new LinkedHashMap<T, Long>(),
            (m, v) -> m.compute(v, (k1, v1) -> v1 == null ? 1L : v1 + 1L),
            (m1, m2) -> {
                m1.putAll(m2);
                return m1;
            },
            m -> Seq.seq(m).transform(transformer)
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>MODE()</code> function.
     */
    public static <T, U> Collector<T, ?, Optional<T>> modeBy(Function<? super T, ? extends U> function) {
        return collectingAndThen(modeAllBy(function), Stream::findFirst);
    }

    /**
     * Get a {@link Collector} that calculates the <code>MODE()</code> function.
     */
    public static <T, U> Collector<T, ?, Seq<T>> modeAllBy(Function<? super T, ? extends U> function) {
        return Collector.of(
            () -> new LinkedHashMap<U, List<T>>(),
            (m, t) -> m.compute(function.apply(t), (k, l) -> {
                List<T> result = l != null ? l : new ArrayList<>();
                result.add(t);
                return result;
            }),
            (m1, m2) -> {
                for (Entry<U, List<T>> e : m2.entrySet()) {
                    List<T> l = m1.get(e.getKey());

                    if (l == null)
                        m1.put(e.getKey(), e.getValue());
                    else
                        l.addAll(e.getValue());
                }

                return m1;
            },
            m -> Seq.seq(m).maxAllBy(t -> t.v2.size()).flatMap(t -> Seq.seq(t.v2))
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>RANK()</code> function given natural ordering.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<Long>> rank(T value) {
        return rankBy(value, t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>RANK()</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<Long>> rank(T value, Comparator<? super T> comparator) {
        return rankBy(value, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>RANK()</code> function given natural ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<Long>> rankBy(U value, Function<? super T, ? extends U> function) {
        return rankBy(value, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>RANK()</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<Long>> rankBy(U value, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return Collector.of(
            () -> new long[] { -1L },
            (l, v) -> {
                if (l[0] == -1L)
                    l[0] = 0L;

                if (comparator.compare(value, function.apply(v)) > 0)
                    l[0] = l[0] + 1L;
            },
            (l1, l2) -> {
                l1[0] = (l1[0] == -1 ? 0L : l1[0]) +
                        (l2[0] == -1 ? 0L : l2[0]);

                return l1;
            },
            l -> l[0] == -1 ? Optional.empty() : Optional.of(l[0])
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>DENSE_RANK()</code> function given natural ordering.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<Long>> denseRank(T value) {
        return denseRankBy(value, t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>DENSE_RANK()</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<Long>> denseRank(T value, Comparator<? super T> comparator) {
        return denseRankBy(value, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>DENSE_RANK()</code> function given natural ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<Long>> denseRankBy(U value, Function<? super T, ? extends U> function) {
        return denseRankBy(value, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>DENSE_RANK()</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<Long>> denseRankBy(U value, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return Collector.of(
            () -> (TreeSet<U>[]) new TreeSet[1],
            (l, v) -> {
                if (l[0] == null)
                    l[0] = new TreeSet<>(comparator);

                U u = function.apply(v);
                if (comparator.compare(value, u) > 0)
                    l[0].add(u);
            },
            (l1, l2) -> {
                if (l2[0] == null)
                    return l1;
                else if (l1[0] == null)
                    return l2;

                l1[0].addAll(l2[0]);
                return l1;
            },
            l -> l[0] == null ? Optional.empty() : Optional.of((long) l[0].size())
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENT_RANK()</code> function given natural ordering.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<Double>> percentRank(T value) {
        return percentRankBy(value, t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENT_RANK()</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<Double>> percentRank(T value, Comparator<? super T> comparator) {
        return percentRankBy(value, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>PERCENT_RANK()</code> function given natural ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<Double>> percentRankBy(U value, Function<? super T, ? extends U> function) {
        return percentRankBy(value, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>PERCENT_RANK()</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<Double>> percentRankBy(U value, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collectingAndThen(
            collectors(rankBy(value, function, comparator), count()),
            t -> t.map((rank, count) -> rank.map(r -> (double) r / count))
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>MEDIAN()</code> function given natural ordering.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Optional<T>> median() {
        return percentile(0.5);
    }

    /**
     * Get a {@link Collector} that calculates the <code>MEDIAN()</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<T>> median(Comparator<? super T> comparator) {
        return percentile(0.5, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the <code>MEDIAN()</code> function given a specific ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<U>> median(Function<? super T, ? extends U> function) {
        return percentile(0.5, function);
    }

    /**
     * Get a {@link Collector} that calculates the <code>MEDIAN()</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<U>> median(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return percentile(0.5, function, comparator);
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
        return percentile(percentile, t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering.
     */
    public static <T> Collector<T, ?, Optional<T>> percentile(double percentile, Comparator<? super T> comparator) {
        return percentile(percentile, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Optional<U>> percentile(double percentile, Function<? super T, ? extends U> function) {
        return percentile(percentile, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering.
     */
    public static <T, U> Collector<T, ?, Optional<U>> percentile(double percentile, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collectingAndThen(percentileBy(percentile, function, comparator), t -> t.map(function));
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

        if (percentile == 0.0)
            return minBy(function, comparator);
        else if (percentile == 1.0)
            return collectingAndThen(maxAllBy(function, comparator), Seq::findLast);
        else
            return percentileCollector(
                function,
                comparator,
                Optional::empty,
                Optional::of,
                l -> Optional.of(l.get(percentileIndex(percentile, l.size())).v1)
            );
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering, producing multiple results.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Seq<T>> percentileAll(double percentile) {
        return percentileAllBy(percentile, t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering, producing multiple results.
     */
    public static <T> Collector<T, ?, Seq<T>> percentileAll(double percentile, Comparator<? super T> comparator) {
        return percentileAllBy(percentile, t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering, producing multiple results.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Seq<T>> percentileAllBy(double percentile, Function<? super T, ? extends U> function) {
        return percentileAllBy(percentile, function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering, producing multiple results.
     */
    public static <T, U> Collector<T, ?, Seq<T>> percentileAllBy(double percentile, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        if (percentile < 0.0 || percentile > 1.0)
            throw new IllegalArgumentException("Percentile must be between 0.0 and 1.0");

        if (percentile == 0.0)
            return minAllBy(function, comparator);
        else if (percentile == 1.0)
            return maxAllBy(function, comparator);
        else
            return percentileCollector(
                function,
                comparator,
                Seq::empty,
                Seq::of,
                l -> {
                    int left = 0;
                    int right = percentileIndex(percentile, l.size());
                    int mid;

                    U value = l.get(right).v2;

                    // Search the first value equal to the value at the position of PERCENTILE by binary search
                    while (left < right) {
                        mid = left + (right - left) / 2;

                        if (comparator.compare(l.get(mid).v2, value) < 0)
                            left = mid + 1;
                        else
                            right = mid;
                    }

                    // We could use Seq.seq(l).skip(left).filter(<comparison>) to prevent the additional allocation
                    // but it is very likely that l is quite large and result is quite small. So let's not keep an
                    // unnecessary reference to a possibly large list.
                    List<T> result = new ArrayList<>();

                    for (; left < l.size() && comparator.compare(l.get(left).v2, value) == 0; left++)
                        result.add(l.get(left).v1);

                    return Seq.seq(result);
                }
            );
    }

    private static <T, U, R> Collector<T, List<Tuple2<T, U>>, R> percentileCollector(
        Function<? super T, ? extends U> function,
        Comparator<? super U> comparator,
        Supplier<R> onEmpty,
        Function<T, R> onSingle,
        Function<List<Tuple2<T, U>>, R> finisher
    ) {
        return Collector.of(
            ArrayList::new,
            (l, v) -> l.add(tuple(v, function.apply(v))),
            (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            },
            l -> {
                int size = l.size();

                if (size == 0)
                    return onEmpty.get();
                else if (size == 1)
                    return onSingle.apply(l.get(0).v1);

                l.sort(comparing(t -> t.v2, comparator));
                return finisher.apply(l);
            }
        );
    }

    private static int percentileIndex(double percentile, int size) {

        // x.5 should be rounded down
        return (int) -Math.round(-(size * percentile + 0.5)) - 1;
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering, producing multiple results.
     */
    public static <T extends Comparable<? super T>> Collector<T, ?, Seq<T>> medianAll() {
        return medianAllBy(t -> t, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering, producing multiple results.
     */
    public static <T> Collector<T, ?, Seq<T>> medianAll(Comparator<? super T> comparator) {
        return medianAllBy(t -> t, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>PERCENTILE_DISC(percentile)</code> function given a specific ordering, producing multiple results.
     */
    public static <T, U extends Comparable<? super U>> Collector<T, ?, Seq<T>> medianAllBy(Function<? super T, ? extends U> function) {
        return medianAllBy(function, naturalOrder());
    }

    /**
     * Get a {@link Collector} that calculates the derived <code>MEDIAN()</code> function given natural ordering, producing multiple results.
     */
    public static <T, U> Collector<T, ?, Seq<T>> medianAllBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return percentileAllBy(0.5, function, comparator);
    }

    /**
     * Get a {@link Collector} that calculates the <code>STDDEV_POP()</code> function.
     */
    public static Collector<Double, ?, Optional<Double>> stddevDouble() {
        return stddevDouble(t -> t);
    }

    /**
     * Get a {@link Collector} that calculates the <code>STDDEV_POP()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> stddevDouble(ToDoubleFunction<? super T> function) {
        return collectingAndThen(toList(), l -> l.isEmpty() ? Optional.empty() : Optional.of(Math.sqrt(variance0(l, function))));
    }

    /**
     * Get a {@link Collector} that calculates the <code>VAR_POP()</code> function.
     */
    public static Collector<Double, ?, Optional<Double>> varianceDouble() {
        return varianceDouble(t -> t);
    }

    /**
     * Get a {@link Collector} that calculates the <code>VAR_POP()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> varianceDouble(ToDoubleFunction<? super T> function) {
        return collectingAndThen(toList(), l -> l.isEmpty() ? Optional.empty() : Optional.of(variance0(l, function)));
    }

    private static <T> double variance0(List<T> l, ToDoubleFunction<? super T> function) {
        double average = avg0(l, function);
        return avg0(l, t -> Math.pow(function.applyAsDouble(t) - average, 2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>COVAR_POP()</code> function.
     */
    public static Collector<Tuple2<Double, Double>, ?, Optional<Double>> covarianceDouble() {
        return filter(t -> t.v1 != null && t.v2 != null, covarianceDouble(t -> t.v1, t -> t.v2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>COVAR_POP()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> covarianceDouble(ToDoubleFunction<? super T> functionX, ToDoubleFunction<? super T> functionY) {
        return collectingAndThen(toList(), covarianceFinisher(functionX, functionY, Agg::avg0));
    }

    private static <T> Function<List<T>, Optional<Double>> covarianceFinisher(
        ToDoubleFunction<? super T> functionX,
        ToDoubleFunction<? super T> functionY,
        BiFunction<List<T>, ToDoubleFunction<? super T>, Double> sumOrAvg
    ) {
        return l -> {
            if (l.isEmpty())
                return Optional.empty();
            else if (l.size() == 1)
                return Optional.of(0.0);

            double avgX = avg0(l, functionX);
            double avgY = avg0(l, functionY);
            return Optional.of(sumOrAvg.apply(l, t -> (functionX.applyAsDouble(t) - avgX) * (functionY.applyAsDouble(t) - avgY)));
        };
    }

    /**
     * Get a {@link Collector} that calculates the <code>CORR()</code> function.
     */
    public static Collector<Tuple2<Double, Double>, ?, Optional<Double>> correlationDouble() {
        return filter(t -> t.v1 != null && t.v2 != null, correlationDouble(t -> t.v1, t -> t.v2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>CORR()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> correlationDouble(ToDoubleFunction<? super T> functionX, ToDoubleFunction<? super T> functionY) {
        return collectingAndThen(
            collectors(
                covarianceDouble(functionX, functionY),
                stddevDouble(functionX),
                stddevDouble(functionY)
            ),
            t -> mapAll(t, (v1, v2, v3) -> v2 == 0.0 || v3 == 0.0 ? null : v1 / (v2 * v3))
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_COUNT()</code> function.
     */
    public static  Collector<Tuple2<Double, Double>, ?, Long> regrCount() {
        return filter(t -> t.v1 != null && t.v2 != null, regrCount(t -> t.v1, t -> t.v2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_COUNT()</code> function.
     */
    public static <T> Collector<T, ?, Long> regrCount(ToDoubleFunction<? super T> functionX, ToDoubleFunction<? super T> functionY) {
        return count();
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_AVGX()</code> function.
     */
    public static  Collector<Tuple2<Double, Double>, ?, Optional<Double>> regrAvgXDouble() {
        return filter(t -> t.v1 != null && t.v2 != null, regrAvgXDouble(t -> t.v1, t -> t.v2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_AVGX()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> regrAvgXDouble(ToDoubleFunction<? super T> functionX, ToDoubleFunction<? super T> functionY) {
        return collectingAndThen(
            collectors(summingDouble(functionY), count()),
            t -> t.v2 == 0 ? Optional.empty() : Optional.of(t.v1 / t.v2)
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_AVGY()</code> function.
     */
    public static  Collector<Tuple2<Double, Double>, ?, Optional<Double>> regrAvgYDouble() {
        return filter(t -> t.v1 != null && t.v2 != null, regrAvgYDouble(t -> t.v1, t -> t.v2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_AVGY()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> regrAvgYDouble(ToDoubleFunction<? super T> functionX, ToDoubleFunction<? super T> functionY) {
        return collectingAndThen(
            collectors(summingDouble(functionX), count()),
            t -> t.v2 == 0 ? Optional.empty() : Optional.of(t.v1 / t.v2)
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_SXX()</code> function.
     */
    public static  Collector<Tuple2<Double, Double>, ?, Optional<Double>> regrSxxDouble() {
        return filter(t -> t.v1 != null && t.v2 != null, regrSxxDouble(t -> t.v1, t -> t.v2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_SXX()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> regrSxxDouble(ToDoubleFunction<? super T> functionX, ToDoubleFunction<? super T> functionY) {
        return collectingAndThen(
            collectors(
                regrCount(functionX, functionY),
                varianceDouble(functionY)
            ),
            t -> t.v2.map(v2 -> t.v1 * v2)
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_SXY()</code> function.
     */
    public static  Collector<Tuple2<Double,Double>, ?, Optional<Double>> regrSxyDouble() {
        return filter(t -> t.v1 != null && t.v2 != null, regrSxyDouble(t -> t.v1, t -> t.v2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_SXY()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> regrSxyDouble(ToDoubleFunction<? super T> functionX, ToDoubleFunction<? super T> functionY) {
        // REGR_SXY() is like COVAR_POP(), but using SUM() instead of AVG() at the end
        return collectingAndThen(toList(), covarianceFinisher(functionX, functionY, Agg::sum0));
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_SYY()</code> function.
     */
    public static  Collector<Tuple2<Double,Double>, ?, Optional<Double>> regrSyyDouble() {
        return filter(t -> t.v1 != null && t.v2 != null, regrSyyDouble(t -> t.v1, t -> t.v2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_SYY()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> regrSyyDouble(ToDoubleFunction<? super T> functionX, ToDoubleFunction<? super T> functionY) {
        return collectingAndThen(
            collectors(
                regrCount(functionX, functionY),
                varianceDouble(functionX)
            ),
            t -> t.v2.map(v2 -> t.v1 * v2)
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_SLOPE()</code> function.
     */
    public static Collector<Tuple2<Double, Double>, ?, Optional<Double>> regrSlopeDouble() {
        return filter(t -> t.v1 != null && t.v2 != null, regrSlopeDouble(t -> t.v1, t -> t.v2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_SLOPE()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> regrSlopeDouble(ToDoubleFunction<? super T> functionX, ToDoubleFunction<? super T> functionY) {
        return collectingAndThen(
            collectors(
                covarianceDouble(functionX, functionY),
                varianceDouble(functionY)
            ),
            t -> mapAll(t, (v1, v2) -> v2 == 0.0 ? null : v1 / v2)
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_INTERCEPT()</code> function.
     */
    public static Collector<Tuple2<Double, Double>, ?, Optional<Double>> regrInterceptDouble() {
        return filter(t -> t.v1 != null && t.v2 != null, regrInterceptDouble(t -> t.v1, t -> t.v2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>REGR_INTERCEPT()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> regrInterceptDouble(ToDoubleFunction<? super T> functionX, ToDoubleFunction<? super T> functionY) {
        return collectingAndThen(
            collectors(
                regrAvgYDouble(functionX, functionY),
                regrSlopeDouble(functionX, functionY),
                regrAvgXDouble(functionX, functionY)
            ),
            t -> mapAll(t, (v1, v2, v3) -> v1 - v2 * v3)
        );
    }

    /**
     * Get a {@link Collector} that calculates the <code>regrR2Double()</code> function.
     */
    public static Collector<Tuple2<Double,Double>, ?, Optional<Double>> regrR2Double() {
        return filter(t -> t.v1 != null && t.v2 != null, regrR2Double(t -> t.v1, t -> t.v2));
    }

    /**
     * Get a {@link Collector} that calculates the <code>regrR2Double()</code> function.
     */
    public static <T> Collector<T, ?, Optional<Double>> regrR2Double(ToDoubleFunction<? super T> functionX, ToDoubleFunction<? super T> functionY) {
        return collectingAndThen(
            collectors(
                varianceDouble(functionY),
                varianceDouble(functionX),
                correlationDouble(functionX, functionY)
            ),
            t -> mapAll(t, (v1, v2, v3) -> v1 == 0.0 ? null : v2 == 0.0 ? 1.0 : Math.pow(v3, 2))
        );
    }

    private static <T> double sum0(List<T> list, ToDoubleFunction<? super T> function) {
        double result = 0.0;

        for (T t : list)
            result += function.applyAsDouble(t);

        return result;
    }

    private static <T> double avg0(List<T> list, ToDoubleFunction<? super T> function) {
        return sum0(list, function) / list.size();
    }

    /**
    * Get a {@link Collector} that calculates the common prefix of a set of strings.
    */
    public static Collector<CharSequence, ?, String> commonPrefix() {
        return collectingAndThen(
            reducing((CharSequence s1, CharSequence s2) -> {
                if (s1 == null || s2 == null)
                    return "";

                int l = Math.min(s1.length(), s2.length());
                int i;
                for (i = 0; i < l && s1.charAt(i) == s2.charAt(i); i++);

                return s1.subSequence(0, i);
            }),
            s -> s.map(Objects::toString).orElse("")
        );
    }

    /**
     * Get a {@link Collector} that calculates the common suffix of a set of strings.
     */
    public static Collector<CharSequence, ?, String> commonSuffix() {
        return collectingAndThen(
            reducing((CharSequence s1, CharSequence s2) -> {
                if (s1 == null || s2 == null)
                    return "";

                int l1 = s1.length();
                int l2 = s2.length();
                int l = Math.min(l1, l2);
                int i;
                for (i = 0; i < l && s1.charAt(l1 - i - 1) == s2.charAt(l2 - i - 1); i++);

                return s1.subSequence(l1 - i, l1);
            }),
            s -> s.map(Objects::toString).orElse("")
        );
    }

    private static <T1, T2, R> Optional<R> mapAll(Tuple2<Optional<T1>, Optional<T2>> tuple, Function2<T1, T2, R> function) {
        return tuple.v1.flatMap(v1 -> tuple.v2.map(v2 -> function.apply(v1, v2)));
    }

    private static <T1, T2, T3, R> Optional<R> mapAll(Tuple3<Optional<T1>, Optional<T2>, Optional<T3>> tuple, Function3<T1, T2, T3, R> function) {
        return tuple.v1.flatMap(v1 -> tuple.v2.flatMap(v2 -> tuple.v3.map(v3 -> function.apply(v1, v2, v3))));
    }
}