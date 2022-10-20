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
import java.util.function.LongUnaryOperator;

/**
 * A {@link LongUnaryOperator} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedLongUnaryOperator {

    /**
     * Applies this operator to the given operand.
     *
     * @param operand the operand
     * @return the operator result
     */
    long applyAsLong(long operand) throws Throwable;

    /**
     * {@link Sneaky#longUnaryOperator(CheckedLongUnaryOperator)}
     */
    static LongUnaryOperator sneaky(CheckedLongUnaryOperator operator) {
        return Sneaky.longUnaryOperator(operator);
    }

    /**
     * {@link Unchecked#longUnaryOperator(CheckedLongUnaryOperator)}
     */
    static LongUnaryOperator unchecked(CheckedLongUnaryOperator operator) {
        return Unchecked.longUnaryOperator(operator);
    }

    /**
     * {@link Unchecked#longUnaryOperator(CheckedLongUnaryOperator, Consumer)}
     */
    static LongUnaryOperator unchecked(CheckedLongUnaryOperator operator, Consumer<Throwable> handler) {
        return Unchecked.longUnaryOperator(operator, handler);
    }
}