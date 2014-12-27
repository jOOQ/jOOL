/**
 * Copyright (c) 2014-2015, Data Geekery GmbH, contact@datageekery.com
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

import java.util.function.DoubleUnaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

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

        assertUnaryOperator(test, UncheckedException.class);
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

        assertIntUnaryOperator(test, UncheckedException.class);
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

        assertLongUnaryOperator(test, UncheckedException.class);
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

        assertDoubleUnaryOperator(test, UncheckedException.class);
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
