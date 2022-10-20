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
import java.util.function.ObjIntConsumer;

/**
 * A {@link ObjIntConsumer} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedObjIntConsumer<T> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param value the second input argument
     */
    void accept(T t, int value) throws Throwable;

    /**
     * {@link Sneaky#objIntConsumer(CheckedObjIntConsumer)}
     */
    static <T> ObjIntConsumer<T> sneaky(CheckedObjIntConsumer<T> consumer) {
        return Sneaky.objIntConsumer(consumer);
    }

    /**
     * {@link Unchecked#objIntConsumer(CheckedObjIntConsumer)}
     */
    static <T> ObjIntConsumer<T> unchecked(CheckedObjIntConsumer<T> consumer) {
        return Unchecked.objIntConsumer(consumer);
    }

    /**
     * {@link Unchecked#objIntConsumer(CheckedObjIntConsumer, Consumer)}
     */
    static <T> ObjIntConsumer<T> unchecked(CheckedObjIntConsumer<T> consumer, Consumer<Throwable> handler) {
        return Unchecked.objIntConsumer(consumer, handler);
    }
}