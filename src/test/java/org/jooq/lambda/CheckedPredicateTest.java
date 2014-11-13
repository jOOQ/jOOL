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

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * @author Lukas Eder
 */
public class CheckedPredicateTest {

    @Test
    public void testCheckedPredicate() {
        Predicate<Object> test = Unchecked.predicate(
            t -> {
                throw new Exception("" + t);
            }
        );

        assertPredicate(test, UncheckedException.class);
    }

    @Test
    public void testCheckedPredicateWithCustomHandler() {
        Predicate<Object> test = Unchecked.predicate(
            t -> {
                throw new Exception("" + t);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertPredicate(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntPredicate() {
        IntPredicate test = Unchecked.intPredicate(
            i -> {
                throw new Exception("" + i);
            }
        );

        assertIntPredicate(test, UncheckedException.class);
    }

    @Test
    public void testCheckedIntPredicateWithCustomHandler() {
        IntPredicate test = Unchecked.intPredicate(
            i -> {
                throw new Exception("" + i);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertIntPredicate(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongPredicate() {
        LongPredicate test = Unchecked.longPredicate(
            l -> {
                throw new Exception("" + l);
            }
        );

        assertLongPredicate(test, UncheckedException.class);
    }

    @Test
    public void testCheckedLongPredicateWithCustomHandler() {
        LongPredicate test = Unchecked.longPredicate(
            l -> {
                throw new Exception("" + l);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertLongPredicate(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoublePredicate() {
        DoublePredicate test = Unchecked.doublePredicate(
            d -> {
                throw new Exception("" + d);
            }
        );

        assertDoublePredicate(test, UncheckedException.class);
    }

    @Test
    public void testCheckedDoublePredicateWithCustomHandler() {
        DoublePredicate test = Unchecked.doublePredicate(
            d -> {
                throw new Exception("" + d);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertDoublePredicate(test, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertPredicate(Predicate<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.test(null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("a", "b", "c").filter(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertIntPredicate(IntPredicate test, Class<E> type) {
        assertNotNull(test);
        try {
            test.test(0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).filter(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertLongPredicate(LongPredicate test, Class<E> type) {
        assertNotNull(test);
        try {
            test.test(0L);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).filter(test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertDoublePredicate(DoublePredicate test, Class<E> type) {
        assertNotNull(test);
        try {
            test.test(0.0);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).filter(test);
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
