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
package org.jooq.lambda;

import org.jooq.lambda.fi.lang.CheckedRunnable;
import org.junit.Assert;

import java.util.function.Consumer;

/**
 * @author Lukas Eder
 */
public class Utils {

    /**
     * Assert a Throwable type
     */
    public static void assertThrows(Class<?> throwable, CheckedRunnable runnable) {
        assertThrows(throwable, runnable, t -> {});
    }

    /**
     * Assert a Throwable type and implement more assertions in a consumer
     */
    public static void assertThrows(Class<?> throwable, CheckedRunnable runnable, Consumer<Throwable> exceptionConsumer) {
        boolean fail = false;
        try {
            runnable.run();
            fail = true;
        }
        catch (Throwable t) {
            if (!throwable.isInstance(t))
                throw new AssertionError("Bad exception type", t);

            exceptionConsumer.accept(t);
        }

        if (fail)
            Assert.fail("No exception was thrown");
    }

    public static void ignoreThrows(CheckedRunnable runnable) {
        try {
            runnable.run();
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
