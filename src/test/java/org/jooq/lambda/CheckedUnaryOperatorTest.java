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
import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.jooq.lambda.fi.util.function.CheckedDoubleUnaryOperator;
import org.jooq.lambda.fi.util.function.CheckedIntUnaryOperator;
import org.jooq.lambda.fi.util.function.CheckedLongUnaryOperator;
import org.jooq.lambda.fi.util.function.CheckedUnaryOperator;
import org.jooq.lambda.unchecked.UncheckedDoubleUnaryOperator;
import org.jooq.lambda.unchecked.UncheckedIntUnaryOperator;
import org.jooq.lambda.unchecked.UncheckedLongUnaryOperator;
import org.jooq.lambda.unchecked.UncheckedUnaryOperator;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class CheckedUnaryOperatorTest {

    @Test
    public void testCheckedUnaryOperator() {

        final CheckedUnaryOperator<Object> unaryOperator = t -> {
            throw new Exception("" + t);
        };

        UnaryOperator<Object> test = Unchecked.unaryOperator(unaryOperator);
        UnaryOperator<Object> alias = UncheckedUnaryOperator.unchecked(unaryOperator);

        assertUnaryOperator(test, UncheckedException.class);
        assertUnaryOperator(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedUnaryOperatorWithCustomHandler() {

        final CheckedUnaryOperator<Object> unaryOperator = t -> {
            throw new Exception("" + t);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        UnaryOperator<Object> test = Unchecked.unaryOperator(unaryOperator, handler);
        UnaryOperator<Object> alias = UncheckedUnaryOperator.unchecked(unaryOperator, handler);

        assertUnaryOperator(test, IllegalStateException.class);
        assertUnaryOperator(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntUnaryOperator() {

        final CheckedIntUnaryOperator intUnaryOperator = i -> {
            throw new Exception("" + i);
        };

        IntUnaryOperator test = Unchecked.intUnaryOperator(intUnaryOperator);
        IntUnaryOperator alias = UncheckedIntUnaryOperator.unchecked(intUnaryOperator);

        assertIntUnaryOperator(test, UncheckedException.class);
        assertIntUnaryOperator(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedIntUnaryOperatorWithCustomHandler() {

        final CheckedIntUnaryOperator intUnaryOperator = i -> {
            throw new Exception("" + i);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        IntUnaryOperator test = Unchecked.intUnaryOperator(intUnaryOperator, handler);
        IntUnaryOperator alias = UncheckedIntUnaryOperator.unchecked(intUnaryOperator, handler);

        assertIntUnaryOperator(test, IllegalStateException.class);
        assertIntUnaryOperator(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongUnaryOperator() {

        final CheckedLongUnaryOperator longUnaryOperator = l -> {
            throw new Exception("" + l);
        };

        LongUnaryOperator test = Unchecked.longUnaryOperator(longUnaryOperator);
        LongUnaryOperator alias = UncheckedLongUnaryOperator.unchecked(longUnaryOperator);

        assertLongUnaryOperator(test, UncheckedException.class);
        assertLongUnaryOperator(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedLongUnaryOperatorWithCustomHandler() {

        final CheckedLongUnaryOperator longUnaryOperator = l -> {
            throw new Exception("" + l);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        LongUnaryOperator test = Unchecked.longUnaryOperator(longUnaryOperator, handler);
        LongUnaryOperator alias = UncheckedLongUnaryOperator.unchecked(longUnaryOperator, handler);

        assertLongUnaryOperator(test, IllegalStateException.class);
        assertLongUnaryOperator(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleUnaryOperator() {

        final CheckedDoubleUnaryOperator doubleUnaryOperator = d -> {
            throw new Exception("" + d);
        };

        DoubleUnaryOperator test = Unchecked.doubleUnaryOperator(doubleUnaryOperator);
        DoubleUnaryOperator alias = UncheckedDoubleUnaryOperator.unchecked(doubleUnaryOperator);

        assertDoubleUnaryOperator(test, UncheckedException.class);
        assertDoubleUnaryOperator(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedDoubleUnaryOperatorWithCustomHandler() {

        final CheckedDoubleUnaryOperator doubleUnaryOperator = d -> {
            throw new Exception("" + d);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        DoubleUnaryOperator test = Unchecked.doubleUnaryOperator(doubleUnaryOperator, handler);
        DoubleUnaryOperator alias = UncheckedDoubleUnaryOperator.unchecked(doubleUnaryOperator, handler);

        assertDoubleUnaryOperator(test, IllegalStateException.class);
        assertDoubleUnaryOperator(alias, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertUnaryOperator(UnaryOperator<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("a", "b", "c").map(test);
        } catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertIntUnaryOperator(IntUnaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).map(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertLongUnaryOperator(LongUnaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0L);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).map(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertDoubleUnaryOperator(DoubleUnaryOperator test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0.0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).map(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1.0");
        }
    }

    private <E extends RuntimeException> void assertException(Class<E> type, RuntimeException e, String message) {
        assertEquals(type, e.getClass());
        assertEquals(Exception.class, e.getCause().getClass());
        assertEquals(message, e.getCause().getMessage());
    }
}
