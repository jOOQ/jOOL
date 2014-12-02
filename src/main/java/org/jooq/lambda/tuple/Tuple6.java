/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
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
import java.util.Optional;

import org.jooq.lambda.function.Function1;
import org.jooq.lambda.function.Function6;

/**
 * A tuple of degree 6.
 *
 * @author Lukas Eder
 */
public class Tuple6<T1, T2, T3, T4, T5, T6> implements Tuple, Comparable<Tuple6<T1, T2, T3, T4, T5, T6>>, Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    
    public final T1 v1;
    public final T2 v2;
    public final T3 v3;
    public final T4 v4;
    public final T5 v5;
    public final T6 v6;
    
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
    
    public Tuple6(Tuple6<T1, T2, T3, T4, T5, T6> tuple) {
        this.v1 = tuple.v1;
        this.v2 = tuple.v2;
        this.v3 = tuple.v3;
        this.v4 = tuple.v4;
        this.v5 = tuple.v5;
        this.v6 = tuple.v6;
    }

    public Tuple6(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
    }
    
    /**
     * Apply this tuple as arguments to a function.
     */
    public final <R> R map(Function6<T1, T2, T3, T4, T5, T6, R> function) {
        return function.apply(this);
    }
    
    /**
     * Apply attribute 1 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U1> Tuple6<U1, T2, T3, T4, T5, T6> map1(Function1<T1, U1> function) {
        return Tuple.tuple(function.apply(v1), v2, v3, v4, v5, v6);
    }
    
    /**
     * Apply attribute 2 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U2> Tuple6<T1, U2, T3, T4, T5, T6> map2(Function1<T2, U2> function) {
        return Tuple.tuple(v1, function.apply(v2), v3, v4, v5, v6);
    }
    
    /**
     * Apply attribute 3 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U3> Tuple6<T1, T2, U3, T4, T5, T6> map3(Function1<T3, U3> function) {
        return Tuple.tuple(v1, v2, function.apply(v3), v4, v5, v6);
    }
    
    /**
     * Apply attribute 4 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U4> Tuple6<T1, T2, T3, U4, T5, T6> map4(Function1<T4, U4> function) {
        return Tuple.tuple(v1, v2, v3, function.apply(v4), v5, v6);
    }
    
    /**
     * Apply attribute 5 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U5> Tuple6<T1, T2, T3, T4, U5, T6> map5(Function1<T5, U5> function) {
        return Tuple.tuple(v1, v2, v3, v4, function.apply(v5), v6);
    }
    
    /**
     * Apply attribute 6 as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U6> Tuple6<T1, T2, T3, T4, T5, U6> map6(Function1<T6, U6> function) {
        return Tuple.tuple(v1, v2, v3, v4, v5, function.apply(v6));
    }
    
    @Override
    public final Object[] array() {
        return new Object[] { v1, v2, v3, v4, v5, v6 };
    }

    @Override
    public final List<?> list() {
        return Arrays.asList(array());
    }

    /**
     * The degree of this tuple: 6.
     */
    @Override
    public final int degree() {
        return 6;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<Object> iterator() {
        return (Iterator<Object>) list().iterator();
    }

    @Override
    public int compareTo(Tuple6<T1, T2, T3, T4, T5, T6> other) {
        int result = 0;
        
        result = Tuples.compare(v1, other.v1); if (result != 0) return result;
        result = Tuples.compare(v2, other.v2); if (result != 0) return result;
        result = Tuples.compare(v3, other.v3); if (result != 0) return result;
        result = Tuples.compare(v4, other.v4); if (result != 0) return result;
        result = Tuples.compare(v5, other.v5); if (result != 0) return result;
        result = Tuples.compare(v6, other.v6); if (result != 0) return result;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Tuple6))
            return false;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Tuple6<T1, T2, T3, T4, T5, T6> that = (Tuple6) o;
        
        if (!Objects.equals(v1, that.v1)) return false;
        if (!Objects.equals(v2, that.v2)) return false;
        if (!Objects.equals(v3, that.v3)) return false;
        if (!Objects.equals(v4, that.v4)) return false;
        if (!Objects.equals(v5, that.v5)) return false;
        if (!Objects.equals(v6, that.v6)) return false;

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
             + ")";
    }

    @Override
    public Tuple6<T1, T2, T3, T4, T5, T6> clone() {
        return new Tuple6<>(this);
    }
}
