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
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.jooq.lambda.fi.util.function.CheckedBinaryOperator;
import org.jooq.lambda.fi.util.function.CheckedDoubleBinaryOperator;
import org.jooq.lambda.fi.util.function.CheckedIntBinaryOperator;
import org.jooq.lambda.fi.util.function.CheckedLongBinaryOperator;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class CheckedBinaryOperatorTest {

    @Test
    public void testCheckedBinaryOperator() {

        final CheckedBinaryOperator<Object> binaryOperator = (t1, t2) -> {
            throw new Exception(t1 + ":" + t2);
        };

        BinaryOperator<Object> test = Unchecked.binaryOperator(binaryOperator);
        BinaryOperator<Object> alias = CheckedBinaryOperator.unchecked(binaryOperator);

        assertBinaryOperator(test, UncheckedException.class);
        assertBinaryOperator(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedBinaryOperatorWithCustomHandler() {

        final CheckedBinaryOperator<Object> binaryOperator = (t1, t2) -> {
            throw new Exception(t1 + ":" + t2);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        BinaryOperator<Object> test = Unchecked.binaryOperator(binaryOperator, handler);
        BinaryOperator<Object> alias = CheckedBinaryOperator.unchecked(binaryOperator, handler);

        assertBinaryOperator(test, IllegalStateException.class);
        assertBinaryOperator(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntBinaryOperator() {

        final CheckedIntBinaryOperator intBinaryOperator = (i1, i2) -> {
            throw new Exception(i1 + ":" + i2);
        };

        IntBinaryOperator test = Unchecked.intBinaryOperator(intBinaryOperator);
        IntBinaryOperator alias = CheckedIntBinaryOperator.unchecked(intBinaryOperator);

        assertIntBinaryOperator(test, UncheckedException.class);
        assertIntBinaryOperator(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedIntBinaryOperatorWithCustomHandler() {

        final CheckedIntBinaryOperator intBinaryOperator = (i1, i2) -> {
            throw new Exception(i1 + ":" + i2);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        IntBinaryOperator test = Unchecked.intBinaryOperator(intBinaryOperator, handler);
        IntBinaryOperator alias = CheckedIntBinaryOperator.unchecked(intBinaryOperator, handler);

        assertIntBinaryOperator(test, IllegalStateException.class);
        assertIntBinaryOperator(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongBinaryOperator() {

        final CheckedLongBinaryOperator longBinaryOperator = (l1, l2) -> {
            throw new Exception(l1 + ":" + l2);
        };

        LongBinaryOperator test = Unchecked.longBinaryOperator(longBinaryOperator);
        LongBinaryOperator alias = CheckedLongBinaryOperator.unchecked(longBinaryOperator);

        assertLongBinaryOperator(test, UncheckedException.class);
        assertLongBinaryOperator(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedLongBinaryOperatorWithCustomHandler() {

        final CheckedLongBinaryOperator longBinaryOperator = (l1, l2) -> {
            throw new Exception(l1 + ":" + l2);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        LongBinaryOperator test = Unchecked.longBinaryOperator(longBinaryOperator, handler);
        LongBinaryOperator alias = CheckedLongBinaryOperator.unchecked(longBinaryOperator, handler);

        assertLongBinaryOperator(test, IllegalStateException.class);
        assertLongBinaryOperator(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleBinaryOperator() {

        final CheckedDoubleBinaryOperator doubleBinaryOperator = (d1, d2) -> {
            throw new Exception(d1 + ":" + d2);
        };

        DoubleBinaryOperator test = Unchecked.doubleBinaryOperator(doubleBinaryOperator);
        DoubleBinaryOperator alias = CheckedDoubleBinaryOperator.unchecked(doubleBinaryOperator);

        assertDoubleBinaryOperator(test, UncheckedException.class);
        assertDoubleBinaryOperator(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedDoubleBinaryOperatorWithCustomHandler() {

        final CheckedDoubleBinaryOperator doubleBinaryOperator = (d1, d2) -> {
            throw new Exception(d1 + ":" + d2);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        DoubleBinaryOperator test = Unchecked.doubleBinaryOperator(doubleBinaryOperator, handler);
        DoubleBinaryOperator alias = CheckedDoubleBinaryOperator.unchecked(doubleBinaryOperator, handler);

        assertDoubleBinaryOperator(test, IllegalStateException.class);
        assertDoubleBinaryOperator(alias, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertBinaryOperator(BinaryOperator<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(null, null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }

        try {
            Stream.of((Object) "a", "b", "c").reduce(test);
        } catch (RuntimeException e) {
            assertException(type, e, "a:b");
        }
    }

    private <E extends RuntimeException> void assertIntBinaryOperator(IntBinaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0, 0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0:0");
        }

        try {
            IntStream.of(1, 2, 3).reduce(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1:2");
        }
    }

    private <E extends RuntimeException> void assertLongBinaryOperator(LongBinaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0L, 0L);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0:0");
        }

        try {
            LongStream.of(1L, 2L, 3L).reduce(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1:2");
        }
    }

    private <E extends RuntimeException> void assertDoubleBinaryOperator(DoubleBinaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0.0, 0.0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0.0:0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).reduce(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1.0:2.0");
        }
    }

    private <E extends RuntimeException> void assertException(Class<E> type, RuntimeException e, String message) {
        assertEquals(type, e.getClass());
        assertEquals(Exception.class, e.getCause().getClass());
        assertEquals(message, e.getCause().getMessage());
    }
}
