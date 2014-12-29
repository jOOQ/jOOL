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

import java.util.function.BiFunction;
import org.jooq.lambda.tuple.Tuple2;

/**
 * A function with 2 arguments
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface Function2<T1, T2, R> extends BiFunction<T1, T2, R> {

    /**
     * Apply this function to the arguments.
     */
    default R apply(Tuple2<T1, T2> args) {
        return apply(args.v1, args.v2);
    }

    /**
     * Apply this function to the arguments.
     */
    @Override
    R apply(T1 v1, T2 v2);

    /**
     * Convert this function to a {@link java.util.function.BiFunction}
     */
    default BiFunction<T1, T2, R> toBiFunction() {
        return this::apply;
    }

    /**
     * Convert to this function to a {@link java.util.function.BiFunction}
     */
    static <T1, T2, R> Function2<T1, T2, R> from(BiFunction<T1, T2, R> function) {
        return function::apply;
    }
}
