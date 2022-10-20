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
package org.jooq.lambda.fi.util;

import org.jooq.lambda.Sneaky;
import org.jooq.lambda.Unchecked;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * A {@link Comparator} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedComparator<T> {

    /**
     * Compares its two arguments for order.
     */
    int compare(T o1, T o2) throws Throwable;

    /**
     * {@link Sneaky#comparator(CheckedComparator)}
     */
    static <T> Comparator<T> sneaky(CheckedComparator<T> comparator) {
        return Sneaky.comparator(comparator);
    }

    /**
     * {@link Unchecked#comparator(CheckedComparator)}
     */
    static <T> Comparator<T> unchecked(CheckedComparator<T> comparator) {
        return Unchecked.comparator(comparator);
    }

    /**
     * {@link Unchecked#comparator(CheckedComparator, Consumer)}
     */
    static <T> Comparator<T> unchecked(CheckedComparator<T> comparator, Consumer<Throwable> handler) {
        return Unchecked.comparator(comparator, handler);
    }
}