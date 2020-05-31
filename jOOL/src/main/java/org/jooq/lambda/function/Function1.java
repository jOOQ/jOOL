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

import java.util.function.Function;

import org.jooq.lambda.tuple.Tuple1;

/**
 * A function with 1 arguments.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface Function1<T1, R> extends Function<T1, R> {

    /**
     * Apply this function to the arguments.
     *
     * @param args The arguments as a tuple.
     */
    default R apply(Tuple1<? extends T1> args) {
        return apply(args.v1);
    }

    /**
     * Apply this function to the arguments.
     */
    @Override
    R apply(T1 v1);

    /**
     * Convert this function to a {@link java.util.function.Function}.
     */
    default Function<T1, R> toFunction() {
        return this::apply;
    }

    /**
     * Convert to this function from a {@link java.util.function.Function}.
     */
    static <T1, R> Function1<T1, R> from(Function<? super T1, ? extends R> function) {
        return function::apply;
    }

    /**
     * Partially apply this function to the arguments.
     */
    default Function0<R> applyPartially(T1 v1) {
        return () -> apply(v1);
    }

    /**
     * Partially apply this function to the arguments.
     */
    default Function0<R> applyPartially(Tuple1<? extends T1> args) {
        return () -> apply(args.v1);
    }

    /**
     * Partially apply this function to the arguments.
     *
     * @deprecated - Use {@link #applyPartially(Object)} instead.
     */
    @Deprecated
    default Function0<R> curry(T1 v1) {
        return () -> apply(v1);
    }

    /**
     * Partially apply this function to the arguments.
     *
     * @deprecated - Use {@link #applyPartially(Tuple1)} instead.
     */
    @Deprecated
    default Function0<R> curry(Tuple1<? extends T1> args) {
        return () -> apply(args.v1);
    }
	
	/**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @return          The composite function <code>Function1</code>
     */
    default <V1> Function1<V1, R> compose(Function1<V1, T1> before) {
        return (v1 -> apply(before.apply(v1)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @return          The composite function <code>Function2</code>
     */
    default <V1, V2> Function2<V1, V2, R> compose(Function2<V1, V2, T1> before) {
        return ((v1, v2) -> apply(before.apply(v1, v2)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @return          The composite function <code>Function3</code>
     */
    default <V1, V2, V3> Function3<V1, V2, V3, R> compose(Function3<V1, V2, V3, T1> before) {
        return ((v1, v2, v3) -> apply(before.apply(v1, v2, v3)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @return          The composite function <code>Function4</code>
     */
    default <V1, V2, V3, V4> Function4<V1, V2, V3, V4, R> compose(Function4<V1, V2, V3, V4, T1> before) {
        return ((v1, v2, v3, v4) -> apply(before.apply(v1, v2, v3, v4)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @return          The composite function <code>Function5</code>
     */
    default <V1, V2, V3, V4, V5> Function5<V1, V2, V3, V4, V5, R> compose(Function5<V1, V2, V3, V4, V5, T1> before) {
        return ((v1, v2, v3, v4, v5) -> apply(before.apply(v1, v2, v3, v4, v5)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @param <V6>      The 6th type parameter of <code>before</code>
     * @return          The composite function <code>Function6</code>
     */
    default <V1, V2, V3, V4, V5, V6> Function6<V1, V2, V3, V4, V5, V6, R> compose(Function6<V1, V2, V3, V4, V5, V6, T1> before) {
        return ((v1, v2, v3, v4, v5, v6) -> apply(before.apply(v1, v2, v3, v4, v5, v6)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @param <V6>      The 6th type parameter of <code>before</code>
     * @param <V7>      The 7th type parameter of <code>before</code>
     * @return          The composite function <code>Function7</code>
     */
    default <V1, V2, V3, V4, V5, V6, V7> Function7<V1, V2, V3, V4, V5, V6, V7, R> compose(Function7<V1, V2, V3, V4, V5, V6, V7, T1> before) {
        return ((v1, v2, v3, v4, v5, v6, v7) -> apply(before.apply(v1, v2, v3, v4, v5, v6, v7)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @param <V6>      The 6th type parameter of <code>before</code>
     * @param <V7>      The 7th type parameter of <code>before</code>
     * @param <V8>      The 8th type parameter of <code>before</code>
     * @return          The composite function <code>Function8</code>
     */
    default <V1, V2, V3, V4, V5, V6, V7, V8> Function8<V1, V2, V3, V4, V5, V6, V7, V8, R> compose(Function8<V1, V2, V3, V4, V5, V6, V7, V8, T1> before) {
        return ((v1, v2, v3, v4, v5, v6, v7, v8) -> apply(before.apply(v1, v2, v3, v4, v5, v6, v7, v8)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @param <V6>      The 6th type parameter of <code>before</code>
     * @param <V7>      The 7th type parameter of <code>before</code>
     * @param <V8>      The 8th type parameter of <code>before</code>
     * @param <V9>      The 9th type parameter of <code>before</code>
     * @return          The composite function <code>Function9</code>
     */
    default <V1, V2, V3, V4, V5, V6, V7, V8, V9> Function9<V1, V2, V3, V4, V5, V6, V7, V8, V9, R> compose(Function9<V1, V2, V3, V4, V5, V6, V7, V8, V9, T1> before) {
        return ((v1, v2, v3, v4, v5, v6, v7, v8, v9) -> apply(before.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @param <V6>      The 6th type parameter of <code>before</code>
     * @param <V7>      The 7th type parameter of <code>before</code>
     * @param <V8>      The 8th type parameter of <code>before</code>
     * @param <V9>      The 9th type parameter of <code>before</code>
     * @param <V10>     The 10th type parameter of <code>before</code>
     * @return          The composite function <code>Function10</code>
     */
    default <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> Function10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, R> compose(Function10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, T1> before) {
        return ((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10) -> apply(before.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @param <V6>      The 6th type parameter of <code>before</code>
     * @param <V7>      The 7th type parameter of <code>before</code>
     * @param <V8>      The 8th type parameter of <code>before</code>
     * @param <V9>      The 9th type parameter of <code>before</code>
     * @param <V10>     The 10th type parameter of <code>before</code>
     * @param <V11>     The 11th type parameter of <code>before</code>
     * @return          The composite function <code>Function11</code>
     */
    default <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> Function11<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, R> compose(Function11<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, T1> before) {
        return ((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11) -> apply(before.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @param <V6>      The 6th type parameter of <code>before</code>
     * @param <V7>      The 7th type parameter of <code>before</code>
     * @param <V8>      The 8th type parameter of <code>before</code>
     * @param <V9>      The 9th type parameter of <code>before</code>
     * @param <V10>     The 10th type parameter of <code>before</code>
     * @param <V11>     The 11th type parameter of <code>before</code>
     * @param <V12>     The 12th type parameter of <code>before</code>
     * @return          The composite function <code>Function12</code>
     */
    default <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> Function12<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, R> compose(Function12<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, T1> before) {
        return ((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12) -> apply(before.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @param <V6>      The 6th type parameter of <code>before</code>
     * @param <V7>      The 7th type parameter of <code>before</code>
     * @param <V8>      The 8th type parameter of <code>before</code>
     * @param <V9>      The 9th type parameter of <code>before</code>
     * @param <V10>     The 10th type parameter of <code>before</code>
     * @param <V11>     The 11th type parameter of <code>before</code>
     * @param <V12>     The 12th type parameter of <code>before</code>
     * @param <V13>     The 13th type parameter of <code>before</code>
     * @return          The composite function <code>Function13</code>
     */
    default <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> Function13<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, R> compose(Function13<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, T1> before) {
        return ((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13) -> apply(before.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @param <V6>      The 6th type parameter of <code>before</code>
     * @param <V7>      The 7th type parameter of <code>before</code>
     * @param <V8>      The 8th type parameter of <code>before</code>
     * @param <V9>      The 9th type parameter of <code>before</code>
     * @param <V10>     The 10th type parameter of <code>before</code>
     * @param <V11>     The 11th type parameter of <code>before</code>
     * @param <V12>     The 12th type parameter of <code>before</code>
     * @param <V13>     The 13th type parameter of <code>before</code>
     * @param <V14>     The 14th type parameter of <code>before</code>
     * @return          The composite function <code>Function14</code>
     */
    default <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> Function14<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, R> compose(Function14<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, T1> before) {
        return ((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14) -> apply(before.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @param <V6>      The 6th type parameter of <code>before</code>
     * @param <V7>      The 7th type parameter of <code>before</code>
     * @param <V8>      The 8th type parameter of <code>before</code>
     * @param <V9>      The 9th type parameter of <code>before</code>
     * @param <V10>     The 10th type parameter of <code>before</code>
     * @param <V11>     The 11th type parameter of <code>before</code>
     * @param <V12>     The 12th type parameter of <code>before</code>
     * @param <V13>     The 13th type parameter of <code>before</code>
     * @param <V14>     The 14th type parameter of <code>before</code>
     * @param <V15>     The 15th type parameter of <code>before</code>
     * @return          The composite function <code>Function15</code>
     */
    default <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> Function15<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, R> compose(Function15<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, T1> before) {
        return ((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15) -> apply(before.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>before</code> will be applied first
     * @param before    The function that will be applied first
     * @param <V1>      The 1st type parameter of <code>before</code>
     * @param <V2>      The 2nd type parameter of <code>before</code>
     * @param <V3>      The 3rd type parameter of <code>before</code>
     * @param <V4>      The 4th type parameter of <code>before</code>
     * @param <V5>      The 5th type parameter of <code>before</code>
     * @param <V6>      The 6th type parameter of <code>before</code>
     * @param <V7>      The 7th type parameter of <code>before</code>
     * @param <V8>      The 8th type parameter of <code>before</code>
     * @param <V9>      The 9th type parameter of <code>before</code>
     * @param <V10>     The 10th type parameter of <code>before</code>
     * @param <V11>     The 11th type parameter of <code>before</code>
     * @param <V12>     The 12th type parameter of <code>before</code>
     * @param <V13>     The 13th type parameter of <code>before</code>
     * @param <V14>     The 14th type parameter of <code>before</code>
     * @param <V15>     The 15th type parameter of <code>before</code>
     * @param <V16>     The 16th type parameter of <code>before</code>
     * @return          The composite function <code>Function16</code>
     */
    default <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16> Function16<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16, R> compose(Function16<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16, T1> before) {
        return ((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16) -> apply(before.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)));
    }

    /**
     * The method used to compose two functions while
     * the type parameter <code>after</code> will be applied last
     * @param after     The function that will be applied last
     * @param <V>       The result of this <code>Function1</code> that is applied first
     * @return          The composite function <code>Function1</code>
     */
    default <V> Function1<T1, V> andThen(Function1<R, V> after) {
        return (v -> after.apply(apply(v)));
    }
}
