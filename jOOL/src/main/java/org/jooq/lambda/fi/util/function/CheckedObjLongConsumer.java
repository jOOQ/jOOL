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
import java.util.function.ObjLongConsumer;

/**
 * A {@link ObjLongConsumer} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedObjLongConsumer<T> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param value the second input argument
     */
    void accept(T t, long value) throws Throwable;

    /**
     * {@link Sneaky#objLongConsumer(CheckedObjLongConsumer)}
     */
    static <T> ObjLongConsumer<T> sneaky(CheckedObjLongConsumer<T> consumer) {
        return Sneaky.objLongConsumer(consumer);
    }

    /**
     * {@link Unchecked#objLongConsumer(CheckedObjLongConsumer)}
     */
    static <T> ObjLongConsumer<T> unchecked(CheckedObjLongConsumer<T> consumer) {
        return Unchecked.objLongConsumer(consumer);
    }

    /**
     * {@link Unchecked#objLongConsumer(CheckedObjLongConsumer, Consumer)}
     */
    static <T> ObjLongConsumer<T> unchecked(CheckedObjLongConsumer<T> consumer, Consumer<Throwable> handler) {
        return Unchecked.objLongConsumer(consumer, handler);
    }
}