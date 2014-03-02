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
package org.jooq.unchecked;

import org.junit.Test;

import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;

/**
 * @author Lukas Eder
 */
public class CheckedUnaryOperatorTest {

    @Test
    public void testCheckedUnaryOperator() {
        UnaryOperator<Object> test = Unchecked.unaryOperator(
            t -> {
                throw new Exception("" + t);
            }
        );

        assertUnaryOperator(test, RuntimeException.class);
    }

    @Test
    public void testCheckedUnaryOperatorWithCustomHandler() {
        UnaryOperator<Object> test = Unchecked.unaryOperator(
            t -> {
                throw new Exception("" + t);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertUnaryOperator(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntUnaryOperator() {
        IntUnaryOperator test = Unchecked.intUnaryOperator(
            i -> {
                throw new Exception("" + i);
            }
        );

        assertIntUnaryOperator(test, RuntimeException.class);
    }

    @Test
    public void testCheckedIntUnaryOperatorWithCustomHandler() {
        IntUnaryOperator test = Unchecked.intUnaryOperator(
            i -> {
                throw new Exception("" + i);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertIntUnaryOperator(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongUnaryOperator() {
        LongUnaryOperator test = Unchecked.longUnaryOperator(
            l -> {
                throw new Exception("" + l);
            }
        );

        assertLongUnaryOperator(test, RuntimeException.class);
    }

    @Test
    public void testCheckedLongUnaryOperatorWithCustomHandler() {
        LongUnaryOperator test = Unchecked.longUnaryOperator(
            l -> {
                throw new Exception("" + l);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertLongUnaryOperator(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleUnaryOperator() {
        DoubleUnaryOperator test = Unchecked.doubleUnaryOperator(
            d -> {
                throw new Exception("" + d);
            }
        );

        assertDoubleUnaryOperator(test, RuntimeException.class);
    }

    @Test
    public void testCheckedDoubleUnaryOperatorWithCustomHandler() {
        DoubleUnaryOperator test = Unchecked.doubleUnaryOperator(
            d -> {
                throw new Exception("" + d);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertDoubleUnaryOperator(test, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertUnaryOperator(UnaryOperator<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("a", "b", "c").map(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertIntUnaryOperator(IntUnaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).map(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertLongUnaryOperator(LongUnaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0L);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).map(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertDoubleUnaryOperator(DoubleUnaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0.0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).map(test);
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
