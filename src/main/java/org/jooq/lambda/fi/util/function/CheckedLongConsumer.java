/**
 * Copyright (c) 2014-2016, Data Geekery GmbH, contact@datageekery.com
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

import java.util.function.Consumer;
import java.util.function.LongConsumer;
import org.jooq.lambda.Unchecked;

/**
 * A {@link LongConsumer} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedLongConsumer {

    /**
     * Performs this operation on the given argument.
     *
     * @param value the input argument
     */
    void accept(long value) throws Throwable;

    /**
     * Alias of {@link Unchecked#longConsumer(CheckedLongConsumer)} for static import.
     */
    static LongConsumer unchecked(CheckedLongConsumer consumer) {
        return Unchecked.longConsumer(consumer);
    }

    /**
     * Alias of {@link Unchecked#longConsumer(CheckedLongConsumer, Consumer)} for static import.
     */
    static LongConsumer unchecked(CheckedLongConsumer consumer, Consumer<Throwable> handler) {
        return Unchecked.longConsumer(consumer, handler);
    }
}
