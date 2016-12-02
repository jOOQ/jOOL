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

import org.jooq.lambda.Sneaky;
import org.jooq.lambda.Unchecked;
import org.jooq.lambda.function.Consumer1;

import java.util.function.Consumer;

/**
 * A {@link Consumer} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedConsumer1<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t) throws Throwable;

    /**
     * @see {@link Sneaky#consumer1(CheckedConsumer1)}
     */
    static <T> Consumer1<T> sneaky(CheckedConsumer1<T> consumer) {
        return Sneaky.consumer1(consumer);
    }

    /**
     * @see {@link Unchecked#consumer1(CheckedConsumer1)}
     */
    static <T> Consumer1<T> unchecked(CheckedConsumer1<T> consumer) {
        return Unchecked.consumer1(consumer);
    }

    /**
     * @see {@link Unchecked#consumer1(CheckedConsumer1, Consumer)}
     */
    static <T> Consumer1<T> unchecked(CheckedConsumer1<T> consumer, Consumer<Throwable> handler) {
        return Unchecked.consumer1(consumer, handler);
    }
}
