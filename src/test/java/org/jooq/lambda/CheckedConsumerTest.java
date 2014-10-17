/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
