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
import java.util.function.ToIntBiFunction;

/**
 * A {@link ToIntBiFunction} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedToIntBiFunction<T, U> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    int applyAsInt(T t, U u) throws Throwable;

    /**
     * {@link Sneaky#toIntBiFunction(CheckedToIntBiFunction)}
     */
    static <T, U> ToIntBiFunction<T, U> sneaky(CheckedToIntBiFunction<T, U> function) {
        return Sneaky.toIntBiFunction(function);
    }

    /**
     * {@link Unchecked#toIntBiFunction(CheckedToIntBiFunction)}
     */
    static <T, U> ToIntBiFunction<T, U> unchecked(CheckedToIntBiFunction<T, U> function) {
        return Unchecked.toIntBiFunction(function);
    }

    /**
     * {@link Unchecked#toIntBiFunction(CheckedToIntBiFunction, Consumer)}
     */
    static <T, U> ToIntBiFunction<T, U> unchecked(CheckedToIntBiFunction<T, U> function, Consumer<Throwable> handler) {
        return Unchecked.toIntBiFunction(function, handler);
    }
}