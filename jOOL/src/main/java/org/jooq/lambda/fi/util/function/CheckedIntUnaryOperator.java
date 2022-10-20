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
import java.util.function.IntUnaryOperator;

/**
 * A {@link IntUnaryOperator} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedIntUnaryOperator {

    /**
     * Applies this operator to the given operand.
     *
     * @param operand the operand
     * @return the operator result
     */
    int applyAsInt(int operand) throws Throwable;

    /**
     * {@link Sneaky#intUnaryOperator(CheckedIntUnaryOperator)}
     */
    static IntUnaryOperator sneaky(CheckedIntUnaryOperator operator) {
        return Sneaky.intUnaryOperator(operator);
    }

    /**
     * {@link Unchecked#intUnaryOperator(CheckedIntUnaryOperator)}
     */
    static IntUnaryOperator unchecked(CheckedIntUnaryOperator operator) {
        return Unchecked.intUnaryOperator(operator);
    }

    /**
     * {@link Unchecked#intUnaryOperator(CheckedIntUnaryOperator, Consumer)}
     */
    static IntUnaryOperator unchecked(CheckedIntUnaryOperator operator, Consumer<Throwable> handler) {
        return Unchecked.intUnaryOperator(operator, handler);
    }
}