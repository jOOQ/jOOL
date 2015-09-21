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
     * Construct a tuple of degree 0.
     */
    static Tuple0 tuple() {
        return new Tuple0();
    }

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
     * Construct a tuple of degree 9.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9) {
        return new Tuple9<>(v1, v2, v3, v4, v5, v6, v7, v8, v9);
    }

    /**
     * Construct a tuple of degree 10.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10) {
        return new Tuple10<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
    }

    /**
     * Construct a tuple of degree 11.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11) {
        return new Tuple11<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11);
    }

    /**
     * Construct a tuple of degree 12.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12) {
        return new Tuple12<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12);
    }

    /**
     * Construct a tuple of degree 13.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13) {
        return new Tuple13<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13);
    }

    /**
     * Construct a tuple of degree 14.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14) {
        return new Tuple14<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14);
    }

    /**
     * Construct a tuple of degree 15.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15) {
        return new Tuple15<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15);
    }

    /**
     * Construct a tuple of degree 16.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> tuple(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9, T10 v10, T11 v11, T12 v12, T13 v13, T14 v14, T15 v15, T16 v16) {
        return new Tuple16<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16);
    }

    /**
     * Construct a tuple collector of degree 1.
     */
    static <T, A1, D1> Collector<T, Tuple1<A1>, Tuple1<D1>> collectors(
        Collector<T, A1, D1> collector1
    ) {
        return Collector.<T, Tuple1<A1>, Tuple1<D1>>of(
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
        return Collector.<T, Tuple2<A1, A2>, Tuple2<D1, D2>>of(
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
        return Collector.<T, Tuple3<A1, A2, A3>, Tuple3<D1, D2, D3>>of(
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
        return Collector.<T, Tuple4<A1, A2, A3, A4>, Tuple4<D1, D2, D3, D4>>of(
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
        return Collector.<T, Tuple5<A1, A2, A3, A4, A5>, Tuple5<D1, D2, D3, D4, D5>>of(
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
        return Collector.<T, Tuple6<A1, A2, A3, A4, A5, A6>, Tuple6<D1, D2, D3, D4, D5, D6>>of(
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
        return Collector.<T, Tuple7<A1, A2, A3, A4, A5, A6, A7>, Tuple7<D1, D2, D3, D4, D5, D6, D7>>of(
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
        return Collector.<T, Tuple8<A1, A2, A3, A4, A5, A6, A7, A8>, Tuple8<D1, D2, D3, D4, D5, D6, D7, D8>>of(
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
     * Construct a tuple collector of degree 9.
     */
    static <T, A1, A2, A3, A4, A5, A6, A7, A8, A9, D1, D2, D3, D4, D5, D6, D7, D8, D9> Collector<T, Tuple9<A1, A2, A3, A4, A5, A6, A7, A8, A9>, Tuple9<D1, D2, D3, D4, D5, D6, D7, D8, D9>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
      , Collector<T, A6, D6> collector6
      , Collector<T, A7, D7> collector7
      , Collector<T, A8, D8> collector8
      , Collector<T, A9, D9> collector9
    ) {
        return Collector.<T, Tuple9<A1, A2, A3, A4, A5, A6, A7, A8, A9>, Tuple9<D1, D2, D3, D4, D5, D6, D7, D8, D9>>of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
              , collector6.supplier().get()
              , collector7.supplier().get()
              , collector8.supplier().get()
              , collector9.supplier().get()
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
                collector9.accumulator().accept(a.v9, t);
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
              , collector9.combiner().apply(a1.v9, a2.v9)
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
              , collector9.finisher().apply(a.v9)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 10.
     */
    static <T, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10> Collector<T, Tuple10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10>, Tuple10<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
      , Collector<T, A6, D6> collector6
      , Collector<T, A7, D7> collector7
      , Collector<T, A8, D8> collector8
      , Collector<T, A9, D9> collector9
      , Collector<T, A10, D10> collector10
    ) {
        return Collector.<T, Tuple10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10>, Tuple10<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10>>of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
              , collector6.supplier().get()
              , collector7.supplier().get()
              , collector8.supplier().get()
              , collector9.supplier().get()
              , collector10.supplier().get()
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
                collector9.accumulator().accept(a.v9, t);
                collector10.accumulator().accept(a.v10, t);
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
              , collector9.combiner().apply(a1.v9, a2.v9)
              , collector10.combiner().apply(a1.v10, a2.v10)
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
              , collector9.finisher().apply(a.v9)
              , collector10.finisher().apply(a.v10)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 11.
     */
    static <T, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11> Collector<T, Tuple11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11>, Tuple11<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
      , Collector<T, A6, D6> collector6
      , Collector<T, A7, D7> collector7
      , Collector<T, A8, D8> collector8
      , Collector<T, A9, D9> collector9
      , Collector<T, A10, D10> collector10
      , Collector<T, A11, D11> collector11
    ) {
        return Collector.<T, Tuple11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11>, Tuple11<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11>>of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
              , collector6.supplier().get()
              , collector7.supplier().get()
              , collector8.supplier().get()
              , collector9.supplier().get()
              , collector10.supplier().get()
              , collector11.supplier().get()
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
                collector9.accumulator().accept(a.v9, t);
                collector10.accumulator().accept(a.v10, t);
                collector11.accumulator().accept(a.v11, t);
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
              , collector9.combiner().apply(a1.v9, a2.v9)
              , collector10.combiner().apply(a1.v10, a2.v10)
              , collector11.combiner().apply(a1.v11, a2.v11)
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
              , collector9.finisher().apply(a.v9)
              , collector10.finisher().apply(a.v10)
              , collector11.finisher().apply(a.v11)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 12.
     */
    static <T, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12> Collector<T, Tuple12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12>, Tuple12<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
      , Collector<T, A6, D6> collector6
      , Collector<T, A7, D7> collector7
      , Collector<T, A8, D8> collector8
      , Collector<T, A9, D9> collector9
      , Collector<T, A10, D10> collector10
      , Collector<T, A11, D11> collector11
      , Collector<T, A12, D12> collector12
    ) {
        return Collector.<T, Tuple12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12>, Tuple12<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12>>of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
              , collector6.supplier().get()
              , collector7.supplier().get()
              , collector8.supplier().get()
              , collector9.supplier().get()
              , collector10.supplier().get()
              , collector11.supplier().get()
              , collector12.supplier().get()
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
                collector9.accumulator().accept(a.v9, t);
                collector10.accumulator().accept(a.v10, t);
                collector11.accumulator().accept(a.v11, t);
                collector12.accumulator().accept(a.v12, t);
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
              , collector9.combiner().apply(a1.v9, a2.v9)
              , collector10.combiner().apply(a1.v10, a2.v10)
              , collector11.combiner().apply(a1.v11, a2.v11)
              , collector12.combiner().apply(a1.v12, a2.v12)
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
              , collector9.finisher().apply(a.v9)
              , collector10.finisher().apply(a.v10)
              , collector11.finisher().apply(a.v11)
              , collector12.finisher().apply(a.v12)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 13.
     */
    static <T, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13> Collector<T, Tuple13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13>, Tuple13<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
      , Collector<T, A6, D6> collector6
      , Collector<T, A7, D7> collector7
      , Collector<T, A8, D8> collector8
      , Collector<T, A9, D9> collector9
      , Collector<T, A10, D10> collector10
      , Collector<T, A11, D11> collector11
      , Collector<T, A12, D12> collector12
      , Collector<T, A13, D13> collector13
    ) {
        return Collector.<T, Tuple13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13>, Tuple13<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13>>of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
              , collector6.supplier().get()
              , collector7.supplier().get()
              , collector8.supplier().get()
              , collector9.supplier().get()
              , collector10.supplier().get()
              , collector11.supplier().get()
              , collector12.supplier().get()
              , collector13.supplier().get()
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
                collector9.accumulator().accept(a.v9, t);
                collector10.accumulator().accept(a.v10, t);
                collector11.accumulator().accept(a.v11, t);
                collector12.accumulator().accept(a.v12, t);
                collector13.accumulator().accept(a.v13, t);
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
              , collector9.combiner().apply(a1.v9, a2.v9)
              , collector10.combiner().apply(a1.v10, a2.v10)
              , collector11.combiner().apply(a1.v11, a2.v11)
              , collector12.combiner().apply(a1.v12, a2.v12)
              , collector13.combiner().apply(a1.v13, a2.v13)
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
              , collector9.finisher().apply(a.v9)
              , collector10.finisher().apply(a.v10)
              , collector11.finisher().apply(a.v11)
              , collector12.finisher().apply(a.v12)
              , collector13.finisher().apply(a.v13)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 14.
     */
    static <T, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14> Collector<T, Tuple14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14>, Tuple14<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
      , Collector<T, A6, D6> collector6
      , Collector<T, A7, D7> collector7
      , Collector<T, A8, D8> collector8
      , Collector<T, A9, D9> collector9
      , Collector<T, A10, D10> collector10
      , Collector<T, A11, D11> collector11
      , Collector<T, A12, D12> collector12
      , Collector<T, A13, D13> collector13
      , Collector<T, A14, D14> collector14
    ) {
        return Collector.<T, Tuple14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14>, Tuple14<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14>>of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
              , collector6.supplier().get()
              , collector7.supplier().get()
              , collector8.supplier().get()
              , collector9.supplier().get()
              , collector10.supplier().get()
              , collector11.supplier().get()
              , collector12.supplier().get()
              , collector13.supplier().get()
              , collector14.supplier().get()
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
                collector9.accumulator().accept(a.v9, t);
                collector10.accumulator().accept(a.v10, t);
                collector11.accumulator().accept(a.v11, t);
                collector12.accumulator().accept(a.v12, t);
                collector13.accumulator().accept(a.v13, t);
                collector14.accumulator().accept(a.v14, t);
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
              , collector9.combiner().apply(a1.v9, a2.v9)
              , collector10.combiner().apply(a1.v10, a2.v10)
              , collector11.combiner().apply(a1.v11, a2.v11)
              , collector12.combiner().apply(a1.v12, a2.v12)
              , collector13.combiner().apply(a1.v13, a2.v13)
              , collector14.combiner().apply(a1.v14, a2.v14)
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
              , collector9.finisher().apply(a.v9)
              , collector10.finisher().apply(a.v10)
              , collector11.finisher().apply(a.v11)
              , collector12.finisher().apply(a.v12)
              , collector13.finisher().apply(a.v13)
              , collector14.finisher().apply(a.v14)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 15.
     */
    static <T, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14, D15> Collector<T, Tuple15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15>, Tuple15<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14, D15>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
      , Collector<T, A6, D6> collector6
      , Collector<T, A7, D7> collector7
      , Collector<T, A8, D8> collector8
      , Collector<T, A9, D9> collector9
      , Collector<T, A10, D10> collector10
      , Collector<T, A11, D11> collector11
      , Collector<T, A12, D12> collector12
      , Collector<T, A13, D13> collector13
      , Collector<T, A14, D14> collector14
      , Collector<T, A15, D15> collector15
    ) {
        return Collector.<T, Tuple15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15>, Tuple15<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14, D15>>of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
              , collector6.supplier().get()
              , collector7.supplier().get()
              , collector8.supplier().get()
              , collector9.supplier().get()
              , collector10.supplier().get()
              , collector11.supplier().get()
              , collector12.supplier().get()
              , collector13.supplier().get()
              , collector14.supplier().get()
              , collector15.supplier().get()
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
                collector9.accumulator().accept(a.v9, t);
                collector10.accumulator().accept(a.v10, t);
                collector11.accumulator().accept(a.v11, t);
                collector12.accumulator().accept(a.v12, t);
                collector13.accumulator().accept(a.v13, t);
                collector14.accumulator().accept(a.v14, t);
                collector15.accumulator().accept(a.v15, t);
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
              , collector9.combiner().apply(a1.v9, a2.v9)
              , collector10.combiner().apply(a1.v10, a2.v10)
              , collector11.combiner().apply(a1.v11, a2.v11)
              , collector12.combiner().apply(a1.v12, a2.v12)
              , collector13.combiner().apply(a1.v13, a2.v13)
              , collector14.combiner().apply(a1.v14, a2.v14)
              , collector15.combiner().apply(a1.v15, a2.v15)
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
              , collector9.finisher().apply(a.v9)
              , collector10.finisher().apply(a.v10)
              , collector11.finisher().apply(a.v11)
              , collector12.finisher().apply(a.v12)
              , collector13.finisher().apply(a.v13)
              , collector14.finisher().apply(a.v14)
              , collector15.finisher().apply(a.v15)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 16.
     */
    static <T, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14, D15, D16> Collector<T, Tuple16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16>, Tuple16<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14, D15, D16>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
      , Collector<T, A3, D3> collector3
      , Collector<T, A4, D4> collector4
      , Collector<T, A5, D5> collector5
      , Collector<T, A6, D6> collector6
      , Collector<T, A7, D7> collector7
      , Collector<T, A8, D8> collector8
      , Collector<T, A9, D9> collector9
      , Collector<T, A10, D10> collector10
      , Collector<T, A11, D11> collector11
      , Collector<T, A12, D12> collector12
      , Collector<T, A13, D13> collector13
      , Collector<T, A14, D14> collector14
      , Collector<T, A15, D15> collector15
      , Collector<T, A16, D16> collector16
    ) {
        return Collector.<T, Tuple16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16>, Tuple16<D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14, D15, D16>>of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
              , collector3.supplier().get()
              , collector4.supplier().get()
              , collector5.supplier().get()
              , collector6.supplier().get()
              , collector7.supplier().get()
              , collector8.supplier().get()
              , collector9.supplier().get()
              , collector10.supplier().get()
              , collector11.supplier().get()
              , collector12.supplier().get()
              , collector13.supplier().get()
              , collector14.supplier().get()
              , collector15.supplier().get()
              , collector16.supplier().get()
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
                collector9.accumulator().accept(a.v9, t);
                collector10.accumulator().accept(a.v10, t);
                collector11.accumulator().accept(a.v11, t);
                collector12.accumulator().accept(a.v12, t);
                collector13.accumulator().accept(a.v13, t);
                collector14.accumulator().accept(a.v14, t);
                collector15.accumulator().accept(a.v15, t);
                collector16.accumulator().accept(a.v16, t);
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
              , collector9.combiner().apply(a1.v9, a2.v9)
              , collector10.combiner().apply(a1.v10, a2.v10)
              , collector11.combiner().apply(a1.v11, a2.v11)
              , collector12.combiner().apply(a1.v12, a2.v12)
              , collector13.combiner().apply(a1.v13, a2.v13)
              , collector14.combiner().apply(a1.v14, a2.v14)
              , collector15.combiner().apply(a1.v15, a2.v15)
              , collector16.combiner().apply(a1.v16, a2.v16)
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
              , collector9.finisher().apply(a.v9)
              , collector10.finisher().apply(a.v10)
              , collector11.finisher().apply(a.v11)
              , collector12.finisher().apply(a.v12)
              , collector13.finisher().apply(a.v13)
              , collector14.finisher().apply(a.v14)
              , collector15.finisher().apply(a.v15)
              , collector16.finisher().apply(a.v16)
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
