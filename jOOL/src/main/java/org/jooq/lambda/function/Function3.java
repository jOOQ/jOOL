/**
 * Copyright (c) Data Geekery GmbH, contact@datageekery.com
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
package org.jooq.lambda.function;


import org.jooq.lambda.tuple.Tuple1;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;

/**
 * A function with 3 arguments.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface Function3<T1, T2, T3, R> {

    /**
     * Apply this function to the arguments.
     *
     * @param args The arguments as a tuple.
     */
    default R apply(Tuple3<? extends T1, ? extends T2, ? extends T3> args) {
        return apply(args.v1, args.v2, args.v3);
    }

    /**
     * Apply this function to the arguments.
     */
    R apply(T1 v1, T2 v2, T3 v3);

    /**
     * Partially apply this function to the arguments.
     */
    default Function2<T2, T3, R> applyPartially(T1 v1) {
        return (v2, v3) -> apply(v1, v2, v3);
    }

    /**
     * Partially apply this function to the arguments.
     */
    default Function1<T3, R> applyPartially(T1 v1, T2 v2) {
        return (v3) -> apply(v1, v2, v3);
    }

    /**
     * Partially apply this function to the arguments.
     */
    default Function0<R> applyPartially(T1 v1, T2 v2, T3 v3) {
        return () -> apply(v1, v2, v3);
    }

    /**
     * Partially apply this function to the arguments.
     */
    default Function2<T2, T3, R> applyPartially(Tuple1<? extends T1> args) {
        return (v2, v3) -> apply(args.v1, v2, v3);
    }

    /**
     * Partially apply this function to the arguments.
     */
    default Function1<T3, R> applyPartially(Tuple2<? extends T1, ? extends T2> args) {
        return (v3) -> apply(args.v1, args.v2, v3);
    }

    /**
     * Partially apply this function to the arguments.
     */
    default Function0<R> applyPartially(Tuple3<? extends T1, ? extends T2, ? extends T3> args) {
        return () -> apply(args.v1, args.v2, args.v3);
    }

    /**
     * Partially apply this function to the arguments.
     *
     * @deprecated - Use {@link #applyPartially(Object)} instead.
     */
    @Deprecated
    default Function2<T2, T3, R> curry(T1 v1) {
        return (v2, v3) -> apply(v1, v2, v3);
    }

    /**
     * Partially apply this function to the arguments.
     *
     * @deprecated - Use {@link #applyPartially(Object, Object)} instead.
     */
    @Deprecated
    default Function1<T3, R> curry(T1 v1, T2 v2) {
        return (v3) -> apply(v1, v2, v3);
    }

    /**
     * Partially apply this function to the arguments.
     *
     * @deprecated - Use {@link #applyPartially(Object, Object, Object)} instead.
     */
    @Deprecated
    default Function0<R> curry(T1 v1, T2 v2, T3 v3) {
        return () -> apply(v1, v2, v3);
    }

    /**
     * Partially apply this function to the arguments.
     *
     * @deprecated - Use {@link #applyPartially(Tuple1)} instead.
     */
    @Deprecated
    default Function2<T2, T3, R> curry(Tuple1<? extends T1> args) {
        return (v2, v3) -> apply(args.v1, v2, v3);
    }

    /**
     * Partially apply this function to the arguments.
     *
     * @deprecated - Use {@link #applyPartially(Tuple2)} instead.
     */
    @Deprecated
    default Function1<T3, R> curry(Tuple2<? extends T1, ? extends T2> args) {
        return (v3) -> apply(args.v1, args.v2, v3);
    }

    /**
     * Partially apply this function to the arguments.
     *
     * @deprecated - Use {@link #applyPartially(Tuple3)} instead.
     */
    @Deprecated
    default Function0<R> curry(Tuple3<? extends T1, ? extends T2, ? extends T3> args) {
        return () -> apply(args.v1, args.v2, args.v3);
    }
	
	/**
     * The method used to compose two functions while
     * the type parameter <code>after</code> will be applied last
     * @param after     The function that will be applied last
     * @param <V>       The result of this <code>Function3</code> that is applied first
     * @return          The composite function <code>Function3</code>
     */
    default <V> Function3<T1, T2, T3, V> andThen(Function1<R, V> after) {
        return ((t1, t2, t3) -> after.apply(apply(t1, t2, t3)));
    }
}
