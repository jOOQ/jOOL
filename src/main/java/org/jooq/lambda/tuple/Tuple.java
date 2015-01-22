/**
 * Copyright (c) 2014-2015, Data Geekery GmbH, contact@datageekery.com
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
package org.jooq.lambda.tuple;

import java.util.List;
import java.util.stream.Collector;

/**
 * A tuple.
 *
 * @author Lukas Eder
 */
public interface Tuple extends Iterable<Object> {

    /**
     * Construct a tuple of degree 1.
     */
    static <T1> Tuple1<T1> tuple(T1 v1) {
        return new Tuple1<>(v1);
    }

    /**
     * Construct a tuple of degree 2.
     */
    static <T1, T2> Tuple2<T1, T2> tuple(T1 v1, T2 v2) {
        return new Tuple2<>(v1, v2);
    }

    /**
     * Construct a tuple of degree 3.
     */
    static <T1, T2, T3> Tuple3<T1, T2, T3> tuple(T1 v1, T2 v2, T3 v3) {
        return new Tuple3<>(v1, v2, v3);
    }

    /**
     * Construct a tuple of degree 4.
     */
    static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> tuple(T1 v1, T2 v2, T3 v3, T4 v4) {
        return new Tuple4<>(v1, v2, v3, v4);
    }

    /**
     * Construct a tuple of degree 5.
     */
    static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5) {
        return new Tuple5<>(v1, v2, v3, v4, v5);
    }

    /**
     * Construct a tuple of degree 6.
     */
    static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6) {
        return new Tuple6<>(v1, v2, v3, v4, v5, v6);
    }

    /**
     * Construct a tuple of degree 7.
     */
    static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7) {
        return new Tuple7<>(v1, v2, v3, v4, v5, v6, v7);
    }

    /**
     * Construct a tuple of degree 8.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8) {
        return new Tuple8<>(v1, v2, v3, v4, v5, v6, v7, v8);
    }

    /**
     * Construct a tuple collector of degree 1.
     */
    static <T, A1, D1> Collector<T, Tuple1<A1>, Tuple1<D1>> collectors(
        Collector<T, A1, D1> collector1
    ) {
        return Collector.of(
            () -> tuple(
                collector1.supplier().get()
            ),
            (a, t) -> {
                collector1.accumulator().accept(a.v1, t);
            },
            (a1, a2) -> tuple(
                collector1.combiner().apply(a1.v1, a2.v1)
            ),
            a -> tuple(
                collector1.finisher().apply(a.v1)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 2.
     */
    static <T, A1, A2, D1, D2> Collector<T, Tuple2<A1, A2>, Tuple2<D1, D2>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
    ) {
        return Collector.of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
            ),
            (a, t) -> {
                collector1.accumulator().accept(a.v1, t);
                collector2.accumulator().accept(a.v2, t);
            },
            (a1, a2) -> tuple(
                collector1.combiner().apply(a1.v1, a2.v1)
              , collector2.combiner().apply(a1.v2, a2.v2)
            ),
            a -> tuple(
                collector1.finisher().apply(a.v1)
              , collector2.finisher().apply(a.v2)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 3.
     */
    static <T, A1, A2, A3, D1, D2, D3> Collector<T, Tuple3<A1, A2, A3>, Tuple3<D1, D2, D3>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
    ) {
        return Collector.of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
            ),
            (a, t) -> {
                collector1.accumulator().accept(a.v1, t);
                collector2.accumulator().accept(a.v2, t);
                collector3.accumulator().accept(a.v3, t);
            },
            (a1, a2) -> tuple(
                collector1.combiner().apply(a1.v1, a2.v1)
              , collector2.combiner().apply(a1.v2, a2.v2)
              , collector3.combiner().apply(a1.v3, a2.v3)
            ),
            a -> tuple(
                collector1.finisher().apply(a.v1)
              , collector2.finisher().apply(a.v2)
              , collector3.finisher().apply(a.v3)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 4.
     */
    static <T, A1, A2, A3, A4, D1, D2, D3, D4> Collector<T, Tuple4<A1, A2, A3, A4>, Tuple4<D1, D2, D3, D4>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
    ) {
        return Collector.of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
            ),
            (a, t) -> {
                collector1.accumulator().accept(a.v1, t);
                collector2.accumulator().accept(a.v2, t);
                collector3.accumulator().accept(a.v3, t);
                collector4.accumulator().accept(a.v4, t);
            },
            (a1, a2) -> tuple(
                collector1.combiner().apply(a1.v1, a2.v1)
              , collector2.combiner().apply(a1.v2, a2.v2)
              , collector3.combiner().apply(a1.v3, a2.v3)
              , collector4.combiner().apply(a1.v4, a2.v4)
            ),
            a -> tuple(
                collector1.finisher().apply(a.v1)
              , collector2.finisher().apply(a.v2)
              , collector3.finisher().apply(a.v3)
              , collector4.finisher().apply(a.v4)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 5.
     */
    static <T, A1, A2, A3, A4, A5, D1, D2, D3, D4, D5> Collector<T, Tuple5<A1, A2, A3, A4, A5>, Tuple5<D1, D2, D3, D4, D5>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
    ) {
        return Collector.of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
            ),
            (a, t) -> {
                collector1.accumulator().accept(a.v1, t);
                collector2.accumulator().accept(a.v2, t);
                collector3.accumulator().accept(a.v3, t);
                collector4.accumulator().accept(a.v4, t);
                collector5.accumulator().accept(a.v5, t);
            },
            (a1, a2) -> tuple(
                collector1.combiner().apply(a1.v1, a2.v1)
              , collector2.combiner().apply(a1.v2, a2.v2)
              , collector3.combiner().apply(a1.v3, a2.v3)
              , collector4.combiner().apply(a1.v4, a2.v4)
              , collector5.combiner().apply(a1.v5, a2.v5)
            ),
            a -> tuple(
                collector1.finisher().apply(a.v1)
              , collector2.finisher().apply(a.v2)
              , collector3.finisher().apply(a.v3)
              , collector4.finisher().apply(a.v4)
              , collector5.finisher().apply(a.v5)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 6.
     */
    static <T, A1, A2, A3, A4, A5, A6, D1, D2, D3, D4, D5, D6> Collector<T, Tuple6<A1, A2, A3, A4, A5, A6>, Tuple6<D1, D2, D3, D4, D5, D6>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
      , Collector<T, A6, D6> collector6
    ) {
        return Collector.of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
              , collector6.supplier().get()
            ),
            (a, t) -> {
                collector1.accumulator().accept(a.v1, t);
                collector2.accumulator().accept(a.v2, t);
                collector3.accumulator().accept(a.v3, t);
                collector4.accumulator().accept(a.v4, t);
                collector5.accumulator().accept(a.v5, t);
                collector6.accumulator().accept(a.v6, t);
            },
            (a1, a2) -> tuple(
                collector1.combiner().apply(a1.v1, a2.v1)
              , collector2.combiner().apply(a1.v2, a2.v2)
              , collector3.combiner().apply(a1.v3, a2.v3)
              , collector4.combiner().apply(a1.v4, a2.v4)
              , collector5.combiner().apply(a1.v5, a2.v5)
              , collector6.combiner().apply(a1.v6, a2.v6)
            ),
            a -> tuple(
                collector1.finisher().apply(a.v1)
              , collector2.finisher().apply(a.v2)
              , collector3.finisher().apply(a.v3)
              , collector4.finisher().apply(a.v4)
              , collector5.finisher().apply(a.v5)
              , collector6.finisher().apply(a.v6)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 7.
     */
    static <T, A1, A2, A3, A4, A5, A6, A7, D1, D2, D3, D4, D5, D6, D7> Collector<T, Tuple7<A1, A2, A3, A4, A5, A6, A7>, Tuple7<D1, D2, D3, D4, D5, D6, D7>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
      , Collector<T, A6, D6> collector6
      , Collector<T, A7, D7> collector7
    ) {
        return Collector.of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
              , collector6.supplier().get()
              , collector7.supplier().get()
            ),
            (a, t) -> {
                collector1.accumulator().accept(a.v1, t);
                collector2.accumulator().accept(a.v2, t);
                collector3.accumulator().accept(a.v3, t);
                collector4.accumulator().accept(a.v4, t);
                collector5.accumulator().accept(a.v5, t);
                collector6.accumulator().accept(a.v6, t);
                collector7.accumulator().accept(a.v7, t);
            },
            (a1, a2) -> tuple(
                collector1.combiner().apply(a1.v1, a2.v1)
              , collector2.combiner().apply(a1.v2, a2.v2)
              , collector3.combiner().apply(a1.v3, a2.v3)
              , collector4.combiner().apply(a1.v4, a2.v4)
              , collector5.combiner().apply(a1.v5, a2.v5)
              , collector6.combiner().apply(a1.v6, a2.v6)
              , collector7.combiner().apply(a1.v7, a2.v7)
            ),
            a -> tuple(
                collector1.finisher().apply(a.v1)
              , collector2.finisher().apply(a.v2)
              , collector3.finisher().apply(a.v3)
              , collector4.finisher().apply(a.v4)
              , collector5.finisher().apply(a.v5)
              , collector6.finisher().apply(a.v6)
              , collector7.finisher().apply(a.v7)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 8.
     */
    static <T, A1, A2, A3, A4, A5, A6, A7, A8, D1, D2, D3, D4, D5, D6, D7, D8> Collector<T, Tuple8<A1, A2, A3, A4, A5, A6, A7, A8>, Tuple8<D1, D2, D3, D4, D5, D6, D7, D8>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
      , Collector<T, A6, D6> collector6
      , Collector<T, A7, D7> collector7
      , Collector<T, A8, D8> collector8
    ) {
        return Collector.of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
              , collector6.supplier().get()
              , collector7.supplier().get()
              , collector8.supplier().get()
            ),
            (a, t) -> {
                collector1.accumulator().accept(a.v1, t);
                collector2.accumulator().accept(a.v2, t);
                collector3.accumulator().accept(a.v3, t);
                collector4.accumulator().accept(a.v4, t);
                collector5.accumulator().accept(a.v5, t);
                collector6.accumulator().accept(a.v6, t);
                collector7.accumulator().accept(a.v7, t);
                collector8.accumulator().accept(a.v8, t);
            },
            (a1, a2) -> tuple(
                collector1.combiner().apply(a1.v1, a2.v1)
              , collector2.combiner().apply(a1.v2, a2.v2)
              , collector3.combiner().apply(a1.v3, a2.v3)
              , collector4.combiner().apply(a1.v4, a2.v4)
              , collector5.combiner().apply(a1.v5, a2.v5)
              , collector6.combiner().apply(a1.v6, a2.v6)
              , collector7.combiner().apply(a1.v7, a2.v7)
              , collector8.combiner().apply(a1.v8, a2.v8)
            ),
            a -> tuple(
                collector1.finisher().apply(a.v1)
              , collector2.finisher().apply(a.v2)
              , collector3.finisher().apply(a.v3)
              , collector4.finisher().apply(a.v4)
              , collector5.finisher().apply(a.v5)
              , collector6.finisher().apply(a.v6)
              , collector7.finisher().apply(a.v7)
              , collector8.finisher().apply(a.v8)
            )
        );
    }

    /**
     * Create a new range.
     */
    static <T extends Comparable<T>> Range<T> range(T t1, T t2) {
        return new Range<>(t1, t2);
    }

    /**
     * Get an array representation of this tuple.
     */
    Object[] array();

    /**
     * Get a list representation of this tuple.
     */
    List<?> list();

    /**
     * The degree of this tuple.
     */
    int degree();
}
