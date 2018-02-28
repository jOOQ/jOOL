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

import java.util.function.Consumer;
import java.util.function.Predicate;
import org.jooq.lambda.Sneaky;
import org.jooq.lambda.Unchecked;

/**
 * A {@link Predicate} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedPredicate<T> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    boolean test(T t) throws Throwable;

    /**
     * @see {@link Sneaky#predicate(CheckedPredicate)}
     */
    static <T> Predicate<T> sneaky(CheckedPredicate<T> predicate) {
        return Sneaky.predicate(predicate);
    }

    /**
     * @see {@link Unchecked#predicate(CheckedPredicate)}
     */
    static <T> Predicate<T> unchecked(CheckedPredicate<T> predicate) {
        return Unchecked.predicate(predicate);
    }

    /**
     * @see {@link Unchecked#predicate(CheckedPredicate, Consumer)}
     */
    static <T> Predicate<T> unchecked(CheckedPredicate<T> function, Consumer<Throwable> handler) {
        return Unchecked.predicate(function, handler);
    }
}
