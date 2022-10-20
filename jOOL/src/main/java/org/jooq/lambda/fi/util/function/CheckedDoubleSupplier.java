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
import java.util.function.DoubleSupplier;

/**
 * A {@link DoubleSupplier} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedDoubleSupplier {

    /**
     * Gets a result.
     *
     * @return a result
     */
    double getAsDouble() throws Throwable;

    /**
     * {@link Sneaky#doubleSupplier(CheckedDoubleSupplier)}
     */
    static DoubleSupplier sneaky(CheckedDoubleSupplier supplier) {
        return Sneaky.doubleSupplier(supplier);
    }

    /**
     * {@link Unchecked#doubleSupplier(CheckedDoubleSupplier)}
     */
    static DoubleSupplier unchecked(CheckedDoubleSupplier supplier) {
        return Unchecked.doubleSupplier(supplier);
    }

    /**
     * {@link Unchecked#doubleSupplier(CheckedDoubleSupplier, Consumer)}
     */
    static DoubleSupplier unchecked(CheckedDoubleSupplier supplier, Consumer<Throwable> handler) {
        return Unchecked.doubleSupplier(supplier, handler);
    }
}