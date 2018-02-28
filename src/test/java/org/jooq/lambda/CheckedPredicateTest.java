/**
 * Copyright (c), Data Geekery GmbH, contact@datageekery.com
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
import java.util.function.Consumer;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.jooq.lambda.fi.util.function.CheckedDoublePredicate;
import org.jooq.lambda.fi.util.function.CheckedIntPredicate;
import org.jooq.lambda.fi.util.function.CheckedLongPredicate;
import org.jooq.lambda.fi.util.function.CheckedPredicate;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class CheckedPredicateTest {

    @Test
    public void testCheckedPredicate() {
        final CheckedPredicate<Object> predicate = t -> {
            throw new Exception("" + t);
        };

        Predicate<Object> p1 = Unchecked.predicate(predicate);
        Predicate<Object> p2 = CheckedPredicate.unchecked(predicate);
        Predicate<Object> p3 = Sneaky.predicate(predicate);
        Predicate<Object> p4 = CheckedPredicate.sneaky(predicate);

        assertPredicate(p1, UncheckedException.class);
        assertPredicate(p2, UncheckedException.class);
        assertPredicate(p3, Exception.class);
        assertPredicate(p4, Exception.class);
    }

    @Test
    public void testCheckedPredicateWithCustomHandler() {
        final CheckedPredicate<Object> predicate = t -> {
            throw new Exception("" + t);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        Predicate<Object> test = Unchecked.predicate(predicate, handler);
        Predicate<Object> alias = CheckedPredicate.unchecked(predicate, handler);

        assertPredicate(test, IllegalStateException.class);
        assertPredicate(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntPredicate() {
        final CheckedIntPredicate intPredicate = i -> {
            throw new Exception("" + i);
        };

        IntPredicate p1 = Unchecked.intPredicate(intPredicate);
        IntPredicate p2 = CheckedIntPredicate.unchecked(intPredicate);
        IntPredicate p3 = Sneaky.intPredicate(intPredicate);
        IntPredicate p4 = CheckedIntPredicate.sneaky(intPredicate);

        assertIntPredicate(p1, UncheckedException.class);
        assertIntPredicate(p2, UncheckedException.class);
        assertIntPredicate(p3, Exception.class);
        assertIntPredicate(p4, Exception.class);
    }

    @Test
    public void testCheckedIntPredicateWithCustomHandler() {
        final CheckedIntPredicate intPredicate = i -> {
            throw new Exception("" + i);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        IntPredicate test = Unchecked.intPredicate(intPredicate, handler);
        IntPredicate alias = CheckedIntPredicate.unchecked(intPredicate, handler);

        assertIntPredicate(test, IllegalStateException.class);
        assertIntPredicate(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongPredicate() {
        final CheckedLongPredicate longPredicate = l -> {
            throw new Exception("" + l);
        };

        LongPredicate p1 = Unchecked.longPredicate(longPredicate);
        LongPredicate p2 = CheckedLongPredicate.unchecked(longPredicate);
        LongPredicate p3 = Sneaky.longPredicate(longPredicate);
        LongPredicate p4 = CheckedLongPredicate.sneaky(longPredicate);

        assertLongPredicate(p1, UncheckedException.class);
        assertLongPredicate(p2, UncheckedException.class);
        assertLongPredicate(p3, Exception.class);
        assertLongPredicate(p4, Exception.class);
    }

    @Test
    public void testCheckedLongPredicateWithCustomHandler() {
        final CheckedLongPredicate longPredicate = l -> {
            throw new Exception("" + l);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        LongPredicate test = Unchecked.longPredicate(longPredicate, handler);
        LongPredicate alias = CheckedLongPredicate.unchecked(longPredicate, handler);

        assertLongPredicate(test, IllegalStateException.class);
        assertLongPredicate(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoublePredicate() {
        final CheckedDoublePredicate doublePredicate = d -> {
            throw new Exception("" + d);
        };

        DoublePredicate p1 = Unchecked.doublePredicate(doublePredicate);
        DoublePredicate p2 = CheckedDoublePredicate.unchecked(doublePredicate);
        DoublePredicate p3 = Sneaky.doublePredicate(doublePredicate);
        DoublePredicate p4 = CheckedDoublePredicate.sneaky(doublePredicate);

        assertDoublePredicate(p1, UncheckedException.class);
        assertDoublePredicate(p2, UncheckedException.class);
        assertDoublePredicate(p3, Exception.class);
        assertDoublePredicate(p4, Exception.class);
    }

    @Test
    public void testCheckedDoublePredicateWithCustomHandler() {
        final CheckedDoublePredicate doublePredicate = d -> {
            throw new Exception("" + d);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        DoublePredicate test = Unchecked.doublePredicate(doublePredicate, handler);
        DoublePredicate alias = CheckedDoublePredicate.unchecked(doublePredicate, handler);

        assertDoublePredicate(test, IllegalStateException.class);
        assertDoublePredicate(alias, IllegalStateException.class);
    }

    private <E extends Exception> void assertPredicate(Predicate<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.test(null);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("a", "b", "c").filter(test);
        } 
        catch (Exception e) {
            assertException(type, e, "a");
        }
    }

    private <E extends Exception> void assertIntPredicate(IntPredicate test, Class<E> type) {
        assertNotNull(test);
        try {
            test.test(0);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).filter(test);
        }
        catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertLongPredicate(LongPredicate test, Class<E> type) {
        assertNotNull(test);
        try {
            test.test(0L);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).filter(test);
        } 
        catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertDoublePredicate(DoublePredicate test, Class<E> type) {
        assertNotNull(test);
        try {
            test.test(0.0);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).filter(test);
        }
        catch (Exception e) {
            assertException(type, e, "1.0");
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
}
