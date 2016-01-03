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
package org.jooq.lambda.tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.jooq.lambda.function.Function1;
import org.jooq.lambda.function.Function9;

/**
 * A tuple of degree 9.
 *
 * @author Lukas Eder
 */
public class Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> implements Tuple, Comparable<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>>, Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    public final T1 v1;
    public final T2 v2;
    public final T3 v3;
    public final T4 v4;
    public final T5 v5;
    public final T6 v6;
    public final T7 v7;
    public final T8 v8;
    public final T9 v9;

    public T1 v1() {
        return v1;
    }

    public T2 v2() {
        return v2;
    }

    public T3 v3() {
        return v3;
    }

    public T4 v4() {
        return v4;
    }

    public T5 v5() {
        return v5;
    }

    public T6 v6() {
        return v6;
    }

    public T7 v7() {
        return v7;
    }

    public T8 v8() {
        return v8;
    }

    public T9 v9() {
        return v9;
    }

    public Tuple9(Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> tuple) {
        this.v1 = tuple.v1;
        this.v2 = tuple.v2;
        this.v3 = tuple.v3;
        this.v4 = tuple.v4;
        this.v5 = tuple.v5;
        this.v6 = tuple.v6;
        this.v7 = tuple.v7;
        this.v8 = tuple.v8;
        this.v9 = tuple.v9;
    }

    public Tuple9(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
        this.v7 = v7;
        this.v8 = v8;
        this.v9 = v9;
    }

    /**
     * Concatenate a value to this tuple.
     */
    public final <T10> Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> concat(T10 value) {
        return new Tuple10<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, value);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T10> Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> concat(Tuple1<T10> tuple) {
        return new Tuple10<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, tuple.v1);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T10, T11> Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> concat(Tuple2<T10, T11> tuple) {
        return new Tuple11<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, tuple.v1, tuple.v2);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T10, T11, T12> Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> concat(Tuple3<T10, T11, T12> tuple) {
        return new Tuple12<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, tuple.v1, tuple.v2, tuple.v3);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T10, T11, T12, T13> Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> concat(Tuple4<T10, T11, T12, T13> tuple) {
        return new Tuple13<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, tuple.v1, tuple.v2, tuple.v3, tuple.v4);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T10, T11, T12, T13, T14> Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> concat(Tuple5<T10, T11, T12, T13, T14> tuple) {
        return new Tuple14<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, tuple.v1, tuple.v2, tuple.v3, tuple.v4, tuple.v5);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T10, T11, T12, T13, T14, T15> Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> concat(Tuple6<T10, T11, T12, T13, T14, T15> tuple) {
        return new Tuple15<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, tuple.v1, tuple.v2, tuple.v3, tuple.v4, tuple.v5, tuple.v6);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T10, T11, T12, T13, T14, T15, T16> Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> concat(Tuple7<T10, T11, T12, T13, T14, T15, T16> tuple) {
        return new Tuple16<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, tuple.v1, tuple.v2, tuple.v3, tuple.v4, tuple.v5, tuple.v6, tuple.v7);
    }

    /**
     * Split this tuple into two tuples of degree 0 and 9.
     */
    public final Tuple2<Tuple0, Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> split0() {
        return new Tuple2<>(limit0(), skip0());
    }

    /**
     * Split this tuple into two tuples of degree 1 and 8.
     */
    public final Tuple2<Tuple1<T1>, Tuple8<T2, T3, T4, T5, T6, T7, T8, T9>> split1() {
        return new Tuple2<>(limit1(), skip1());
    }

    /**
     * Split this tuple into two tuples of degree 2 and 7.
     */
    public final Tuple2<Tuple2<T1, T2>, Tuple7<T3, T4, T5, T6, T7, T8, T9>> split2() {
        return new Tuple2<>(limit2(), skip2());
    }

    /**
     * Split this tuple into two tuples of degree 3 and 6.
     */
    public final Tuple2<Tuple3<T1, T2, T3>, Tuple6<T4, T5, T6, T7, T8, T9>> split3() {
        return new Tuple2<>(limit3(), skip3());
    }

    /**
     * Split this tuple into two tuples of degree 4 and 5.
     */
    public final Tuple2<Tuple4<T1, T2, T3, T4>, Tuple5<T5, T6, T7, T8, T9>> split4() {
        return new Tuple2<>(limit4(), skip4());
    }

    /**
     * Split this tuple into two tuples of degree 5 and 4.
     */
    public final Tuple2<Tuple5<T1, T2, T3, T4, T5>, Tuple4<T6, T7, T8, T9>> split5() {
        return new Tuple2<>(limit5(), skip5());
    }

    /**
     * Split this tuple into two tuples of degree 6 and 3.
     */
    public final Tuple2<Tuple6<T1, T2, T3, T4, T5, T6>, Tuple3<T7, T8, T9>> split6() {
        return new Tuple2<>(limit6(), skip6());
    }

    /**
     * Split this tuple into two tuples of degree 7 and 2.
     */
    public final Tuple2<Tuple7<T1, T2, T3, T4, T5, T6, T7>, Tuple2<T8, T9>> split7() {
        return new Tuple2<>(limit7(), skip7());
    }

    /**
     * Split this tuple into two tuples of degree 8 and 1.
     */
    public final Tuple2<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>, Tuple1<T9>> split8() {
        return new Tuple2<>(limit8(), skip8());
    }

    /**
     * Split this tuple into two tuples of degree 9 and 0.
     */
    public final Tuple2<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>, Tuple0> split9() {
        return new Tuple2<>(limit9(), skip9());
    }

    /**
     * Limit this tuple to degree 0.
     */
    public final Tuple0 limit0() {
        return new Tuple0();
    }

    /**
     * Limit this tuple to degree 1.
     */
    public final Tuple1<T1> limit1() {
        return new Tuple1<>(v1);
    }

    /**
     * Limit this tuple to degree 2.
     */
    public final Tuple2<T1, T2> limit2() {
        return new Tuple2<>(v1, v2);
    }

    /**
     * Limit this tuple to degree 3.
     */
    public final Tuple3<T1, T2, T3> limit3() {
        return new Tuple3<>(v1, v2, v3);
    }

    /**
     * Limit this tuple to degree 4.
     */
    public final Tuple4<T1, T2, T3, T4> limit4() {
        return new Tuple4<>(v1, v2, v3, v4);
    }

    /**
     * Limit this tuple to degree 5.
     */
    public final Tuple5<T1, T2, T3, T4, T5> limit5() {
        return new Tuple5<>(v1, v2, v3, v4, v5);
    }

    /**
     * Limit this tuple to degree 6.
     */
    public final Tuple6<T1, T2, T3, T4, T5, T6> limit6() {
        return new Tuple6<>(v1, v2, v3, v4, v5, v6);
    }

    /**
     * Limit this tuple to degree 7.
     */
    public final Tuple7<T1, T2, T3, T4, T5, T6, T7> limit7() {
        return new Tuple7<>(v1, v2, v3, v4, v5, v6, v7);
    }

    /**
     * Limit this tuple to degree 8.
     */
    public final Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> limit8() {
        return new Tuple8<>(v1, v2, v3, v4, v5, v6, v7, v8);
    }

    /**
     * Limit this tuple to degree 9.
     */
    public final Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> limit9() {
        return this;
    }

    /**
     * Skip 0 degrees from this tuple.
     */
    public final Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> skip0() {
        return this;
    }

    /**
     * Skip 1 degrees from this tuple.
     */
    public final Tuple8<T2, T3, T4, T5, T6, T7, T8, T9> skip1() {
        return new Tuple8<>(v2, v3, v4, v5, v6, v7, v8, v9);
    }

    /**
     * Skip 2 degrees from this tuple.
     */
    public final Tuple7<T3, T4, T5, T6, T7, T8, T9> skip2() {
        return new Tuple7<>(v3, v4, v5, v6, v7, v8, v9);
    }

    /**
     * Skip 3 degrees from this tuple.
     */
    public final Tuple6<T4, T5, T6, T7, T8, T9> skip3() {
        return new Tuple6<>(v4, v5, v6, v7, v8, v9);
    }

    /**
     * Skip 4 degrees from this tuple.
     */
    public final Tuple5<T5, T6, T7, T8, T9> skip4() {
        return new Tuple5<>(v5, v6, v7, v8, v9);
    }

    /**
     * Skip 5 degrees from this tuple.
     */
    public final Tuple4<T6, T7, T8, T9> skip5() {
        return new Tuple4<>(v6, v7, v8, v9);
    }

    /**
     * Skip 6 degrees from this tuple.
     */
    public final Tuple3<T7, T8, T9> skip6() {
        return new Tuple3<>(v7, v8, v9);
    }

    /**
     * Skip 7 degrees from this tuple.
     */
    public final Tuple2<T8, T9> skip7() {
        return new Tuple2<>(v8, v9);
    }

    /**
     * Skip 8 degrees from this tuple.
     */
    public final Tuple1<T9> skip8() {
        return new Tuple1<>(v9);
    }

    /**
     * Skip 9 degrees from this tuple.
     */
    public final Tuple0 skip9() {
        return new Tuple0();
    }

    /**
     * Apply this tuple as arguments to a function.
     */
    public final <R> R map(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> function) {
        return function.apply(this);
    }

    /**
     * Apply attribute 1 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U1> Tuple9<U1, T2, T3, T4, T5, T6, T7, T8, T9> map1(Function1<? super T1, ? extends U1> function) {
        return Tuple.tuple(function.apply(v1), v2, v3, v4, v5, v6, v7, v8, v9);
    }

    /**
     * Apply attribute 2 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U2> Tuple9<T1, U2, T3, T4, T5, T6, T7, T8, T9> map2(Function1<? super T2, ? extends U2> function) {
        return Tuple.tuple(v1, function.apply(v2), v3, v4, v5, v6, v7, v8, v9);
    }

    /**
     * Apply attribute 3 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U3> Tuple9<T1, T2, U3, T4, T5, T6, T7, T8, T9> map3(Function1<? super T3, ? extends U3> function) {
        return Tuple.tuple(v1, v2, function.apply(v3), v4, v5, v6, v7, v8, v9);
    }

    /**
     * Apply attribute 4 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U4> Tuple9<T1, T2, T3, U4, T5, T6, T7, T8, T9> map4(Function1<? super T4, ? extends U4> function) {
        return Tuple.tuple(v1, v2, v3, function.apply(v4), v5, v6, v7, v8, v9);
    }

    /**
     * Apply attribute 5 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U5> Tuple9<T1, T2, T3, T4, U5, T6, T7, T8, T9> map5(Function1<? super T5, ? extends U5> function) {
        return Tuple.tuple(v1, v2, v3, v4, function.apply(v5), v6, v7, v8, v9);
    }

    /**
     * Apply attribute 6 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U6> Tuple9<T1, T2, T3, T4, T5, U6, T7, T8, T9> map6(Function1<? super T6, ? extends U6> function) {
        return Tuple.tuple(v1, v2, v3, v4, v5, function.apply(v6), v7, v8, v9);
    }

    /**
     * Apply attribute 7 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U7> Tuple9<T1, T2, T3, T4, T5, T6, U7, T8, T9> map7(Function1<? super T7, ? extends U7> function) {
        return Tuple.tuple(v1, v2, v3, v4, v5, v6, function.apply(v7), v8, v9);
    }

    /**
     * Apply attribute 8 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U8> Tuple9<T1, T2, T3, T4, T5, T6, T7, U8, T9> map8(Function1<? super T8, ? extends U8> function) {
        return Tuple.tuple(v1, v2, v3, v4, v5, v6, v7, function.apply(v8), v9);
    }

    /**
     * Apply attribute 9 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U9> Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, U9> map9(Function1<? super T9, ? extends U9> function) {
        return Tuple.tuple(v1, v2, v3, v4, v5, v6, v7, v8, function.apply(v9));
    }

    @Override
    public final Object[] array() {
        return new Object[] { v1, v2, v3, v4, v5, v6, v7, v8, v9 };
    }

    @Override
    public final List<?> list() {
        return Arrays.asList(array());
    }

    /**
     * The degree of this tuple: 9.
     */
    @Override
    public final int degree() {
        return 9;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<Object> iterator() {
        return (Iterator<Object>) list().iterator();
    }

    /**
     * A comparator to order by element 1 ascendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> asc1() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v1, (Comparator) Comparator.naturalOrder());
    }

    /**
     * A comparator to order by element 2 ascendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> asc2() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v2, (Comparator) Comparator.naturalOrder());
    }

    /**
     * A comparator to order by element 3 ascendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> asc3() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v3, (Comparator) Comparator.naturalOrder());
    }

    /**
     * A comparator to order by element 4 ascendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> asc4() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v4, (Comparator) Comparator.naturalOrder());
    }

    /**
     * A comparator to order by element 5 ascendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> asc5() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v5, (Comparator) Comparator.naturalOrder());
    }

    /**
     * A comparator to order by element 6 ascendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> asc6() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v6, (Comparator) Comparator.naturalOrder());
    }

    /**
     * A comparator to order by element 7 ascendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> asc7() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v7, (Comparator) Comparator.naturalOrder());
    }

    /**
     * A comparator to order by element 8 ascendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> asc8() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v8, (Comparator) Comparator.naturalOrder());
    }

    /**
     * A comparator to order by element 9 ascendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> asc9() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v9, (Comparator) Comparator.naturalOrder());
    }

    /**
     * A comparator to order by element 1 descendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> desc1() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v1, (Comparator) Comparator.reverseOrder());
    }

    /**
     * A comparator to order by element 2 descendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> desc2() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v2, (Comparator) Comparator.reverseOrder());
    }

    /**
     * A comparator to order by element 3 descendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> desc3() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v3, (Comparator) Comparator.reverseOrder());
    }

    /**
     * A comparator to order by element 4 descendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> desc4() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v4, (Comparator) Comparator.reverseOrder());
    }

    /**
     * A comparator to order by element 5 descendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> desc5() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v5, (Comparator) Comparator.reverseOrder());
    }

    /**
     * A comparator to order by element 6 descendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> desc6() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v6, (Comparator) Comparator.reverseOrder());
    }

    /**
     * A comparator to order by element 7 descendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> desc7() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v7, (Comparator) Comparator.reverseOrder());
    }

    /**
     * A comparator to order by element 8 descendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> desc8() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v8, (Comparator) Comparator.reverseOrder());
    }

    /**
     * A comparator to order by element 9 descendingly.
     */
    public Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> desc9() {
        return Comparator.comparing((Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> t) -> t.v9, (Comparator) Comparator.reverseOrder());
    }

    @Override
    public int compareTo(Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> other) {
        int result = 0;

        result = Tuples.compare(v1, other.v1); if (result != 0) return result;
        result = Tuples.compare(v2, other.v2); if (result != 0) return result;
        result = Tuples.compare(v3, other.v3); if (result != 0) return result;
        result = Tuples.compare(v4, other.v4); if (result != 0) return result;
        result = Tuples.compare(v5, other.v5); if (result != 0) return result;
        result = Tuples.compare(v6, other.v6); if (result != 0) return result;
        result = Tuples.compare(v7, other.v7); if (result != 0) return result;
        result = Tuples.compare(v8, other.v8); if (result != 0) return result;
        result = Tuples.compare(v9, other.v9); if (result != 0) return result;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Tuple9))
            return false;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> that = (Tuple9) o;

        if (!Objects.equals(v1, that.v1)) return false;
        if (!Objects.equals(v2, that.v2)) return false;
        if (!Objects.equals(v3, that.v3)) return false;
        if (!Objects.equals(v4, that.v4)) return false;
        if (!Objects.equals(v5, that.v5)) return false;
        if (!Objects.equals(v6, that.v6)) return false;
        if (!Objects.equals(v7, that.v7)) return false;
        if (!Objects.equals(v8, that.v8)) return false;
        if (!Objects.equals(v9, that.v9)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
        result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
        result = prime * result + ((v3 == null) ? 0 : v3.hashCode());
        result = prime * result + ((v4 == null) ? 0 : v4.hashCode());
        result = prime * result + ((v5 == null) ? 0 : v5.hashCode());
        result = prime * result + ((v6 == null) ? 0 : v6.hashCode());
        result = prime * result + ((v7 == null) ? 0 : v7.hashCode());
        result = prime * result + ((v8 == null) ? 0 : v8.hashCode());
        result = prime * result + ((v9 == null) ? 0 : v9.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return "("
             +        v1
             + ", " + v2
             + ", " + v3
             + ", " + v4
             + ", " + v5
             + ", " + v6
             + ", " + v7
             + ", " + v8
             + ", " + v9
             + ")";
    }

    @Override
    public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> clone() {
        return new Tuple9<>(this);
    }
}
