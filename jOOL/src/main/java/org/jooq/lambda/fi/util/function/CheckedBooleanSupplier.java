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

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * A {@link BooleanSupplier} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedBooleanSupplier {

    /**
     * Gets a result.
     *
     * @return a result
     */
    boolean getAsBoolean() throws Throwable;

    /**
     * {@link Sneaky#booleanSupplier(CheckedBooleanSupplier)}
     */
    static BooleanSupplier sneaky(CheckedBooleanSupplier supplier) {
        return Sneaky.booleanSupplier(supplier);
    }

    /**
     * {@link Unchecked#booleanSupplier(CheckedBooleanSupplier)}
     */
    static BooleanSupplier unchecked(CheckedBooleanSupplier supplier) {
        return Unchecked.booleanSupplier(supplier);
    }

    /**
     * {@link Unchecked#booleanSupplier(CheckedBooleanSupplier, Consumer)}
     */
    static BooleanSupplier unchecked(CheckedBooleanSupplier supplier, Consumer<Throwable> handler) {
        return Unchecked.booleanSupplier(supplier, handler);
    }
}