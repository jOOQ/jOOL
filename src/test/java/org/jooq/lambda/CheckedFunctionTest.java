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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * @author Lukas Eder
 */
public class CheckedFunctionTest {

    @Test
    public void testCheckedFunction() {
        Function<Object, Object> test = Unchecked.function(
            t -> {
                throw new Exception("" + t);
            }
        );

        assertFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedFunctionWithCustomHandler() {
        Function<Object, Object> test = Unchecked.function(
            t -> {
                throw new Exception("" + t);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedToIntFunction() {
        ToIntFunction<Object> test = Unchecked.toIntFunction(
            t -> {
                throw new Exception("" + t);
            }
        );

        assertToIntFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedToIntFunctionWithCustomHandler() {
        ToIntFunction<Object> test = Unchecked.toIntFunction(
            t -> {
                throw new Exception("" + t);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertToIntFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedToLongFunction() {
        ToLongFunction<Object> test = Unchecked.toLongFunction(
            t -> {
                throw new Exception("" + t);
            }
        );

        assertToLongFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedToLongFunctionWithCustomHandler() {
        ToLongFunction<Object> test = Unchecked.toLongFunction(
            t -> {
                throw new Exception("" + t);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertToLongFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedToDoubleFunction() {
        ToDoubleFunction<Object> test = Unchecked.toDoubleFunction(
            t -> {
                throw new Exception("" + t);
            }
        );

        assertToDoubleFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedToDoubleunctionWithCustomHandler() {
        ToDoubleFunction<Object> test = Unchecked.toDoubleFunction(
            t -> {
                throw new Exception("" + t);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertToDoubleFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntFunction() {
        IntFunction<Object> test = Unchecked.intFunction(
            i -> {
                throw new Exception("" + i);
            }
        );

        assertIntFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedIntFunctionWithCustomHandler() {
        IntFunction<Object> test = Unchecked.intFunction(
            i -> {
                throw new Exception("" + i);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertIntFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntToLongFunction() {
        IntToLongFunction test = Unchecked.intToLongFunction(
            i -> {
                throw new Exception("" + i);
            }
        );

        assertIntToLongFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedIntToLongFunctionWithCustomHandler() {
        IntToLongFunction test = Unchecked.intToLongFunction(
            i -> {
                throw new Exception("" + i);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertIntToLongFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntToDoubleFunction() {
        IntToDoubleFunction test = Unchecked.intToDoubleFunction(
            i -> {
                throw new Exception("" + i);
            }
        );

        assertIntToDoubleFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedIntToDoubleFunctionWithCustomHandler() {
        IntToDoubleFunction test = Unchecked.intToDoubleFunction(
            i -> {
                throw new Exception("" + i);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertIntToDoubleFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongFunction() {
        LongFunction<Object> test = Unchecked.longFunction(
            l -> {
                throw new Exception("" + l);
            }
        );

        assertLongFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedLongFunctionWithCustomHandler() {
        LongFunction<Object> test = Unchecked.longFunction(
            l -> {
                throw new Exception("" + l);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertLongFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongToIntFunction() {
        LongToIntFunction test = Unchecked.longToIntFunction(
            l -> {
                throw new Exception("" + l);
            }
        );

        assertLongToIntFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedLongToIntFunctionWithCustomHandler() {
        LongToIntFunction test = Unchecked.longToIntFunction(
            l -> {
                throw new Exception("" + l);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertLongToIntFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongToDoubleFunction() {
        LongToDoubleFunction test = Unchecked.longToDoubleFunction(
            l -> {
                throw new Exception("" + l);
            }
        );

        assertLongToDoubleFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedLongToDoubleFunctionWithCustomHandler() {
        LongToDoubleFunction test = Unchecked.longToDoubleFunction(
            l -> {
                throw new Exception("" + l);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertLongToDoubleFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleFunction() {
        DoubleFunction<Object> test = Unchecked.doubleFunction(
            d -> {
                throw new Exception("" + d);
            }
        );

        assertDoubleFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedDoubleFunctionWithCustomHandler() {
        DoubleFunction<Object> test = Unchecked.doubleFunction(
            d -> {
                throw new Exception("" + d);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertDoubleFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleToIntFunction() {
        DoubleToIntFunction test = Unchecked.doubleToIntFunction(
            d -> {
                throw new Exception("" + d);
            }
        );

        assertDoubleToIntFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedDoubleToIntFunctionWithCustomHandler() {
        DoubleToIntFunction test = Unchecked.doubleToIntFunction(
            d -> {
                throw new Exception("" + d);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertDoubleToIntFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleToLongFunction() {
        DoubleToLongFunction test = Unchecked.doubleToLongFunction(
            d -> {
                throw new Exception("" + d);
            }
        );

        assertDoubleToLongFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedDoubleToLongFunctionWithCustomHandler() {
        DoubleToLongFunction test = Unchecked.doubleToLongFunction(
            d -> {
                throw new Exception("" + d);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertDoubleToLongFunction(test, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertFunction(Function<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Map<Object, Object> map = new LinkedHashMap<>();
            map.computeIfAbsent("a", test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertToIntFunction(ToIntFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToInt(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertToLongFunction(ToLongFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToLong(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertToDoubleFunction(ToDoubleFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToDouble(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertIntFunction(IntFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToObj(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertIntToLongFunction(IntToLongFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToLong(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertIntToDoubleFunction(IntToDoubleFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToDouble(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertLongFunction(LongFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0L);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToObj(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertLongToIntFunction(LongToIntFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0L);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToInt(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertLongToDoubleFunction(LongToDoubleFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0L);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToDouble(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertDoubleFunction(DoubleFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0.0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToObj(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1.0");
        }
    }

    private <E extends RuntimeException> void assertDoubleToIntFunction(DoubleToIntFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0.0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToInt(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1.0");
        }
    }

    private <E extends RuntimeException> void assertDoubleToLongFunction(DoubleToLongFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0.0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToLong(test);
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
