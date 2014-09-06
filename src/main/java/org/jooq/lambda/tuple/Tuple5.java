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

package org.jooq.lambda.tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jooq.lambda.function.Function1;
import org.jooq.lambda.function.Function5;

/**
 * A tuple of degree 5.
 *
 * @author Lukas Eder
 */
public final class Tuple5<T1, T2, T3, T4, T5> implements Tuple, Comparable<Tuple5<T1, T2, T3, T4, T5>>, Serializable, Cloneable {
    
    public final T1 v1;
    public final T2 v2;
    public final T3 v3;
    public final T4 v4;
    public final T5 v5;

    public Tuple5(Tuple5<T1, T2, T3, T4, T5> tuple) {
        this.v1 = tuple.v1;
        this.v2 = tuple.v2;
        this.v3 = tuple.v3;
        this.v4 = tuple.v4;
        this.v5 = tuple.v5;
    }

    public Tuple5(T1 v1, T2 v2, T3 v3, T4 v4, T5 v5) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
    }
    
    /**
     * Apply this tuple as arguments to a function.
     */
    public <R> R map(Function5<T1, T2, T3, T4, T5, R> function) {
        return function.apply(this);
    }
    
    /**
     * Apply attribute 1 as argument to a function and return a new tuple with the substituted argument.
     */
    public <U1> Tuple5<U1, T2, T3, T4, T5> map1(Function1<T1, U1> function) {
        return Tuple.tuple(function.apply(v1), v2, v3, v4, v5);
    }
    
    /**
     * Apply attribute 2 as argument to a function and return a new tuple with the substituted argument.
     */
    public <U2> Tuple5<T1, U2, T3, T4, T5> map2(Function1<T2, U2> function) {
        return Tuple.tuple(v1, function.apply(v2), v3, v4, v5);
    }
    
    /**
     * Apply attribute 3 as argument to a function and return a new tuple with the substituted argument.
     */
    public <U3> Tuple5<T1, T2, U3, T4, T5> map3(Function1<T3, U3> function) {
        return Tuple.tuple(v1, v2, function.apply(v3), v4, v5);
    }
    
    /**
     * Apply attribute 4 as argument to a function and return a new tuple with the substituted argument.
     */
    public <U4> Tuple5<T1, T2, T3, U4, T5> map4(Function1<T4, U4> function) {
        return Tuple.tuple(v1, v2, v3, function.apply(v4), v5);
    }
    
    /**
     * Apply attribute 5 as argument to a function and return a new tuple with the substituted argument.
     */
    public <U5> Tuple5<T1, T2, T3, T4, U5> map5(Function1<T5, U5> function) {
        return Tuple.tuple(v1, v2, v3, v4, function.apply(v5));
    }
    
    @Override
    public Object[] array() {
        return new Object[] { v1, v2, v3, v4, v5 };
    }

    @Override
    public List<?> list() {
        return Arrays.asList(array());
    }

    /**
     * The degree of this tuple: 5.
     */
    @Override
    public int degree() {
        return 5;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<Object> iterator() {
        return (Iterator<Object>) list().iterator();
    }

    @Override
    public int compareTo(Tuple5<T1, T2, T3, T4, T5> other) {
        int result = 0;
        
        result = ((Comparable) v1).compareTo((Comparable) other.v1); if (result != 0) return result;
        result = ((Comparable) v2).compareTo((Comparable) other.v2); if (result != 0) return result;
        result = ((Comparable) v3).compareTo((Comparable) other.v3); if (result != 0) return result;
        result = ((Comparable) v4).compareTo((Comparable) other.v4); if (result != 0) return result;
        result = ((Comparable) v5).compareTo((Comparable) other.v5); if (result != 0) return result;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Tuple5<T1, T2, T3, T4, T5> that = (Tuple5) o;
        
        if (v1 != that.v1) {
            if (v1 == null ^ that.v1 == null)
                return false;

            if (!v1.equals(that.v1))
                return false;
        }
        
        if (v2 != that.v2) {
            if (v2 == null ^ that.v2 == null)
                return false;

            if (!v2.equals(that.v2))
                return false;
        }
        
        if (v3 != that.v3) {
            if (v3 == null ^ that.v3 == null)
                return false;

            if (!v3.equals(that.v3))
                return false;
        }
        
        if (v4 != that.v4) {
            if (v4 == null ^ that.v4 == null)
                return false;

            if (!v4.equals(that.v4))
                return false;
        }
        
        if (v5 != that.v5) {
            if (v5 == null ^ that.v5 == null)
                return false;

            if (!v5.equals(that.v5))
                return false;
        }
        
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
             + ")";
    }

    @Override
    public Tuple5<T1, T2, T3, T4, T5> clone() {
        return new Tuple5<>(this);
    }
}
