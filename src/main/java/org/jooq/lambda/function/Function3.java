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

import org.jooq.lambda.tuple.Tuple3;

/**
 * A function with 3 arguments
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface Function3<T1, T2, T3, R> {

    /**
     * Apply this function to the arguments.
     */
    default R apply(Tuple3<T1, T2, T3> args) {
        return apply(args.v1, args.v2, args.v3);
    }

    /**
     * Apply this function to the arguments.
     */
    R apply(T1 v1, T2 v2, T3 v3);
}
