/**
 * Copyright (c), Data Geekery GmbH, contact@datageekery.com
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
package org.jooq.lambda.fi.util.function;

import org.jooq.lambda.Sneaky;
import org.jooq.lambda.Unchecked;

import java.util.function.Consumer;
import java.util.function.LongToIntFunction;

/**
 * A {@link LongToIntFunction} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedLongToIntFunction {

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    int applyAsInt(long value) throws Throwable;

    /**
     * {@link Sneaky#longToIntFunction(CheckedLongToIntFunction)}
     */
    static LongToIntFunction sneaky(CheckedLongToIntFunction function) {
        return Sneaky.longToIntFunction(function);
    }

    /**
     * {@link Unchecked#longToIntFunction(CheckedLongToIntFunction)}
     */
    static LongToIntFunction unchecked(CheckedLongToIntFunction function) {
        return Unchecked.longToIntFunction(function);
    }

    /**
     * {@link Unchecked#longToIntFunction(CheckedLongToIntFunction, Consumer)}
     */
    static LongToIntFunction unchecked(CheckedLongToIntFunction function, Consumer<Throwable> handler) {
        return Unchecked.longToIntFunction(function, handler);
    }
}