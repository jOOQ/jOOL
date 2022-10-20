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
import java.util.function.ToDoubleFunction;

/**
 * A {@link ToDoubleFunction} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedToDoubleFunction<T> {

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    double applyAsDouble(T value) throws Throwable;

    /**
     * {@link Sneaky#toDoubleFunction(CheckedToDoubleFunction)}
     */
    static <T> ToDoubleFunction<T> sneaky(CheckedToDoubleFunction<T> function) {
        return Sneaky.toDoubleFunction(function);
    }

    /**
     * {@link Unchecked#toDoubleFunction(CheckedToDoubleFunction)}
     */
    static <T> ToDoubleFunction<T> unchecked(CheckedToDoubleFunction<T> function) {
        return Unchecked.toDoubleFunction(function);
    }

    /**
     * {@link Unchecked#toDoubleFunction(CheckedToDoubleFunction, Consumer)}
     */
    static <T> ToDoubleFunction<T> unchecked(CheckedToDoubleFunction<T> function, Consumer<Throwable> handler) {
        return Unchecked.toDoubleFunction(function, handler);
    }
}