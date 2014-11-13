/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
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

import org.junit.Test;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

import static org.junit.Assert.*;

/**
 * @author Lukas Eder
 */
public class CheckedConsumerTest {

    @Test
    public void testCheckedConsumer() {
        Consumer<Object> test = Unchecked.consumer(
            o -> {
                throw new Exception("" + o);
            }
        );

        assertConsumer(test, UncheckedException.class);
    }

    @Test
    public void testCheckedConsumerWithCustomHandler() {
        Consumer<Object> test = Unchecked.consumer(
            o -> {
                throw new Exception("" + o);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertConsumer(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntConsumer() {
        IntConsumer test = Unchecked.intConsumer(
            i -> {
                throw new Exception("" + i);
            }
        );

        assertIntConsumer(test, UncheckedException.class);
    }

    @Test
    public void testCheckedIntConsumerWithCustomHandler() {
        IntConsumer test = Unchecked.intConsumer(
            l -> {
                throw new Exception("" + l);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertIntConsumer(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongConsumer() {
        LongConsumer test = Unchecked.longConsumer(
            l -> {
                throw new Exception("" + l);
            }
        );

        assertLongConsumer(test, UncheckedException.class);
    }

    @Test
    public void testCheckedLongConsumerWithCustomHandler() {
        LongConsumer test = Unchecked.longConsumer(
            l -> {
                throw new Exception("" + l);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertLongConsumer(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleConsumer() {
        DoubleConsumer test = Unchecked.doubleConsumer(
            d -> {
                throw new Exception("" + d);
            }
        );

        assertDoubleConsumer(test, UncheckedException.class);
    }

    @Test
    public void testCheckedDoubleConsumerWithCustomHandler() {
        DoubleConsumer test = Unchecked.doubleConsumer(
            d -> {
                throw new Exception("" + d);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertDoubleConsumer(test, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertConsumer(Consumer<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Arrays.asList("a", "b", "c").stream().forEach(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertIntConsumer(IntConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            Arrays.stream(new int[]{1, 2, 3}).forEach(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertLongConsumer(LongConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0L);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            Arrays.stream(new long[] { 1L, 2L, 3L }).forEach(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertDoubleConsumer(DoubleConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0.0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0.0");
        }

        try {
            Arrays.stream(new double[] { 1.0, 2.0, 3.0 }).forEach(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1.0");
        }
    }

    private <E extends RuntimeException> void assertException(Class<E> type, RuntimeException e, String message) {
        assertEquals(type, e.getClass());
        assertEquals(Exception.class, e.getCause().getClass());
        assertEquals(message, e.getCause().getMessage());
    }
}
