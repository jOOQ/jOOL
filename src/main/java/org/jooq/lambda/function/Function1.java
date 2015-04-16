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
package org.jooq.lambda.function;

import java.util.function.Function;

import org.jooq.lambda.tuple.Tuple1;

/**
 * A function with 1 arguments
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface Function1<T1, R> extends Function<T1, R> {

    /**
     * Apply this function to the arguments.
     */
    default R apply(Tuple1<T1> args) {
        return apply(args.v1);
    }

    /**
     * Apply this function to the arguments.
     */
    @Override
    R apply(T1 v1);

    /**
     * Convert this function to a {@link java.util.function.Function}
     */
    default Function<T1, R> toFunction() {
        return this::apply;
    }

    /**
     * Convert to this function from a {@link java.util.function.Function}
     */
    static <T1, R> Function1<T1, R> from(Function<T1, R> function) {
        return function::apply;
    }

    /**
     * Partially apply this function to the arguments.
     */
    default Function0<R> curry(T1 v1){
        return () -> apply(v1);
    }
}
