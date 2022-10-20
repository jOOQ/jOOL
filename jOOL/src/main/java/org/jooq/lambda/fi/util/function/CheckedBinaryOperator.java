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

import java.util.function.BinaryOperator;
import java.util.function.Consumer;

/**
 * A {@link BinaryOperator} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedBinaryOperator<T> extends CheckedBiFunction<T, T, T> {

    /**
     * {@link Sneaky#binaryOperator(CheckedBinaryOperator)}
     */
    static <T> BinaryOperator<T> sneaky(CheckedBinaryOperator<T> operator) {
        return Sneaky.binaryOperator(operator);
    }

    /**
     * {@link Unchecked#binaryOperator(CheckedBinaryOperator)}
     */
    static <T> BinaryOperator<T> unchecked(CheckedBinaryOperator<T> operator) {
        return Unchecked.binaryOperator(operator);
    }

    /**
     * {@link Unchecked#binaryOperator(CheckedBinaryOperator, Consumer)}
     */
    static <T> BinaryOperator<T> unchecked(CheckedBinaryOperator<T> operator, Consumer<Throwable> handler) {
        return Unchecked.binaryOperator(operator, handler);
    }
}