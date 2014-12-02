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
        int result = 0;
        
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
