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
package org.jooq.lambda.function;

import org.jooq.lambda.Seq;

import java.util.function.Predicate;

/**
 * Grouping operations on Predicates.
 * @author Lukas Eder
 */
public final class Functions {
    private Functions () {}


    /**
     * Negate a predicate.
     */
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    /**
     * AND all predicates.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Predicate<T> and(Predicate<T>... predicates) {
        return Seq.of(predicates).reduce(t -> true, Predicate::and);
    }

    /**
     * OR all predicates.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Predicate<T> or(Predicate<T>... predicates) {
        return Seq.of(predicates).reduce(t -> false, Predicate::or);
    }
}