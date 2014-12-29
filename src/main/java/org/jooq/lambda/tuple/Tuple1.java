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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.jooq.lambda.function.Function1;

/**
 * A tuple of degree 1.
 *
 * @author Lukas Eder
 */
public class Tuple1<T1> implements Tuple, Comparable<Tuple1<T1>>, Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    public final T1 v1;

    public T1 v1() {
        return v1;
    }

    public Tuple1(Tuple1<T1> tuple) {
        this.v1 = tuple.v1;
    }

    public Tuple1(T1 v1) {
        this.v1 = v1;
    }                    

    /**
     * Concatenate a value to this tuple.
     */
    public final <T2> Tuple2<T1, T2> concat(T2 value) {
        return new Tuple2<>(v1, value);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T2> Tuple2<T1, T2> concat(Tuple1<T2> tuple) {
        return new Tuple2<>(v1, tuple.v1);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T2, T3> Tuple3<T1, T2, T3> concat(Tuple2<T2, T3> tuple) {
        return new Tuple3<>(v1, tuple.v1, tuple.v2);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T2, T3, T4> Tuple4<T1, T2, T3, T4> concat(Tuple3<T2, T3, T4> tuple) {
        return new Tuple4<>(v1, tuple.v1, tuple.v2, tuple.v3);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple4<T2, T3, T4, T5> tuple) {
        return new Tuple5<>(v1, tuple.v1, tuple.v2, tuple.v3, tuple.v4);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(Tuple5<T2, T3, T4, T5, T6> tuple) {
        return new Tuple6<>(v1, tuple.v1, tuple.v2, tuple.v3, tuple.v4, tuple.v5);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple6<T2, T3, T4, T5, T6, T7> tuple) {
        return new Tuple7<>(v1, tuple.v1, tuple.v2, tuple.v3, tuple.v4, tuple.v5, tuple.v6);
    }

    /**
     * Concatenate a tuple to this tuple.
     */
    public final <T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(Tuple7<T2, T3, T4, T5, T6, T7, T8> tuple) {
        return new Tuple8<>(v1, tuple.v1, tuple.v2, tuple.v3, tuple.v4, tuple.v5, tuple.v6, tuple.v7);
    }

    /**
     * Apply this tuple as arguments to a function.
     */
    public final <R> R map(Function1<T1, R> function) {
        return function.apply(this);
    }

    /**
     * Apply attribute 1 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U1> Tuple1<U1> map1(Function1<T1, U1> function) {
        return Tuple.tuple(function.apply(v1));
    }

    @Override
    public final Object[] array() {
        return new Object[] { v1 };
    }

    @Override
    public final List<?> list() {
        return Arrays.asList(array());
    }

    /**
     * The degree of this tuple: 1.
     */
    @Override
    public final int degree() {
        return 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<Object> iterator() {
        return (Iterator<Object>) list().iterator();
    }

    @Override
    public int compareTo(Tuple1<T1> other) {
        int result;

        result = Tuples.compare(v1, other.v1); if (result != 0) return result;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Tuple1))
            return false;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Tuple1<T1> that = (Tuple1) o;

        if (!Objects.equals(v1, that.v1)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((v1 == null) ? 0 : v1.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return "("
             +        v1
             + ")";
    }

    @Override
    public Tuple1<T1> clone() {
        return new Tuple1<>(this);
    }
}
