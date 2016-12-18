/**
 * Copyright (c) 2014-2016, Data Geekery GmbH, contact@datageekery.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jooq.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import org.jooq.lambda.fi.util.concurrent.CheckedCallable;
import org.junit.Test;

/**
 * @author Zack Young
 */
public class CheckedCallableTest {

    @Test
    public void testCheckedCallable() {
        final CheckedCallable<Void> callable = () -> {
            throw new Exception("callable");
        };

        Callable<Void> r1 = Unchecked.callable(callable);
        Callable<Void> r2 = CheckedCallable.unchecked(callable);
        Callable<Void> r3 = Sneaky.callable(callable);
        Callable<Void> r4 = CheckedCallable.sneaky(callable);

        assertCallable(r1, UncheckedException.class);
        assertCallable(r2, UncheckedException.class);
        assertCallable(r3, Exception.class);
        assertCallable(r4, Exception.class);
    }

    @Test
    public void testCheckedCallableWithCustomHandler() {
        final CheckedCallable<Void> callable = () -> {
            throw new Exception("callable");
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        Callable<Void> test = Unchecked.callable(callable, handler);
        Callable<Void> alias = CheckedCallable.unchecked(callable, handler);

        assertCallable(test, IllegalStateException.class);
        assertCallable(alias, IllegalStateException.class);
    }

    private <E extends Exception> void assertCallable(Callable<Void> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.call();
            fail();
        }
        catch (Exception e) {
            assertException(type, e, "callable");
        }
    }

    private <E extends Exception> void assertException(Class<E> type, Exception e, String message) {
        assertEquals(type, e.getClass());

        // Sneaky
        if (e.getCause() == null) {
            assertEquals(message, e.getMessage());
        }

        // Unchecked
        else {
            assertEquals(Exception.class, e.getCause().getClass());
            assertEquals(message, e.getCause().getMessage());
        }
    }

    @Test
    public void testCheckedCallableRethrowAll() {
        Callable<Void> test = Unchecked.callable(
            () -> {
                throw new Throwable("callable");
            },
            Unchecked.RETHROW_ALL
        );

        try {
            test.call();
            fail();
        }
        catch (Throwable e) {
            assertEquals("callable", e.getMessage());
        }
    }
}
