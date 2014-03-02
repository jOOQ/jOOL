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
public class CheckedBinaryOperatorTest {

    @Test
    public void testCheckedBinaryOperator() {
        BinaryOperator<Object> test = Unchecked.binaryOperator(
            (t1, t2) -> {
                throw new Exception(t1 + ":" + t2);
            }
        );

        assertBinaryOperator(test, RuntimeException.class);
    }

    @Test
    public void testCheckedBinaryOperatorWithCustomHandler() {
        BinaryOperator<Object> test = Unchecked.binaryOperator(
            (t1, t2) -> {
                throw new Exception(t1 + ":" + t2);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertBinaryOperator(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntBinaryOperator() {
        IntBinaryOperator test = Unchecked.intBinaryOperator(
            (i1, i2) -> {
                throw new Exception(i1 + ":" + i2);
            }
        );

        assertIntBinaryOperator(test, RuntimeException.class);
    }

    @Test
    public void testCheckedIntBinaryOperatorWithCustomHandler() {
        IntBinaryOperator test = Unchecked.intBinaryOperator(
            (i1, i2) -> {
                throw new Exception(i1 + ":" + i2);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertIntBinaryOperator(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongBinaryOperator() {
        LongBinaryOperator test = Unchecked.longBinaryOperator(
            (l1, l2) -> {
                throw new Exception(l1 + ":" + l2);
            }
        );

        assertLongBinaryOperator(test, RuntimeException.class);
    }

    @Test
    public void testCheckedLongBinaryOperatorWithCustomHandler() {
        LongBinaryOperator test = Unchecked.longBinaryOperator(
            (l1, l2) -> {
                throw new Exception(l1 + ":" + l2);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertLongBinaryOperator(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleBinaryOperator() {
        DoubleBinaryOperator test = Unchecked.doubleBinaryOperator(
            (d1, d2) -> {
                throw new Exception(d1 + ":" + d2);
            }
        );

        assertDoubleBinaryOperator(test, RuntimeException.class);
    }

    @Test
    public void testCheckedDoubleBinaryOperatorWithCustomHandler() {
        DoubleBinaryOperator test = Unchecked.doubleBinaryOperator(
            (d1, d2) -> {
                throw new Exception(d1 + ":" + d2);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertDoubleBinaryOperator(test, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertBinaryOperator(BinaryOperator<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(null, null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }

        try {
            Stream.of((Object) "a", "b", "c").reduce(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "a:b");
        }
    }

    private <E extends RuntimeException> void assertIntBinaryOperator(IntBinaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0, 0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0:0");
        }

        try {
            IntStream.of(1, 2, 3).reduce(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1:2");
        }
    }

    private <E extends RuntimeException> void assertLongBinaryOperator(LongBinaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0L, 0L);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0:0");
        }

        try {
            LongStream.of(1L, 2L, 3L).reduce(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1:2");
        }
    }

    private <E extends RuntimeException> void assertDoubleBinaryOperator(DoubleBinaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0.0, 0.0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0.0:0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).reduce(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1.0:2.0");
        }
    }

    private <E extends RuntimeException> void assertException(Class<E> type, RuntimeException e, String message) {
        assertEquals(type, e.getClass());
        assertEquals(Exception.class, e.getCause().getClass());
        assertEquals(message, e.getCause().getMessage());
    }
}
