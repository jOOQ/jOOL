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
package org.jooq.lambda.fi.lang;

import org.jooq.lambda.Sneaky;
import org.jooq.lambda.Unchecked;

import java.util.function.Consumer;

/**
 * A {@link Runnable} that allows for checked exceptions.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CheckedRunnable {

    /**
     * Run this runnable.
     */
    void run() throws Throwable;

    /**
     * {@link Sneaky#runnable(CheckedRunnable)}
     */
    static Runnable sneaky(CheckedRunnable runnable) {
        return Sneaky.runnable(runnable);
    }

    /**
     * {@link Unchecked#runnable(CheckedRunnable)}
     */
    static Runnable unchecked(CheckedRunnable runnable) {
        return Unchecked.runnable(runnable);
    }

    /**
     * {@link Unchecked#runnable(CheckedRunnable, Consumer)}
     */
    static Runnable unchecked(CheckedRunnable runnable, Consumer<Throwable> handler) {
        return Unchecked.runnable(runnable, handler);
    }
}