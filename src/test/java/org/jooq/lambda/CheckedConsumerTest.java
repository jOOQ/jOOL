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
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import org.jooq.lambda.fi.util.function.CheckedConsumer;
import org.jooq.lambda.fi.util.function.CheckedDoubleConsumer;
import org.jooq.lambda.fi.util.function.CheckedIntConsumer;
import org.jooq.lambda.fi.util.function.CheckedLongConsumer;
import org.jooq.lambda.unchecked.UncheckedConsumer;
import org.jooq.lambda.unchecked.UncheckedDoubleConsumer;
import org.jooq.lambda.unchecked.UncheckedIntConsumer;
import org.jooq.lambda.unchecked.UncheckedLongConsumer;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class CheckedConsumerTest {

    @Test
    public void testCheckedConsumer() {

        final CheckedConsumer<Object> consumer = o -> {
            throw new Exception("" + o);
        };

        Consumer<Object> test = Unchecked.consumer(consumer);
        Consumer<Object> alias = UncheckedConsumer.unchecked(consumer);

        assertConsumer(test, UncheckedException.class);
        assertConsumer(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedConsumerWithCustomHandler() {

        final CheckedConsumer<Object> consumer = o -> {
            throw new Exception("" + o);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        Consumer<Object> test = Unchecked.consumer(consumer, handler);
        Consumer<Object> alias = UncheckedConsumer.unchecked(consumer, handler);

        assertConsumer(test, IllegalStateException.class);
        assertConsumer(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntConsumer() {

        final CheckedIntConsumer intConsumer = i -> {
            throw new Exception("" + i);
        };

        IntConsumer test = Unchecked.intConsumer(intConsumer);
        IntConsumer alias = UncheckedIntConsumer.unchecked(intConsumer);

        assertIntConsumer(test, UncheckedException.class);
        assertIntConsumer(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedIntConsumerWithCustomHandler() {

        final CheckedIntConsumer intConsumer = l -> {
            throw new Exception("" + l);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        IntConsumer test = Unchecked.intConsumer(intConsumer, handler);
        IntConsumer alias = UncheckedIntConsumer.unchecked(intConsumer, handler);

        assertIntConsumer(test, IllegalStateException.class);
        assertIntConsumer(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongConsumer() {

        final CheckedLongConsumer longConsumer = l -> {
            throw new Exception("" + l);
        };

        LongConsumer test = Unchecked.longConsumer(longConsumer);
        LongConsumer alias = UncheckedLongConsumer.unchecked(longConsumer);

        assertLongConsumer(test, UncheckedException.class);
        assertLongConsumer(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedLongConsumerWithCustomHandler() {

        final CheckedLongConsumer longConsumer = l -> {
            throw new Exception("" + l);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        LongConsumer test = Unchecked.longConsumer(longConsumer, handler);
        LongConsumer alias = UncheckedLongConsumer.unchecked(longConsumer, handler);

        assertLongConsumer(test, IllegalStateException.class);
        assertLongConsumer(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleConsumer() {

        final CheckedDoubleConsumer doubleConsumer = d -> {
            throw new Exception("" + d);
        };

        DoubleConsumer test = Unchecked.doubleConsumer(doubleConsumer);
        DoubleConsumer alias = UncheckedDoubleConsumer.unchecked(doubleConsumer);

        assertDoubleConsumer(test, UncheckedException.class);
        assertDoubleConsumer(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedDoubleConsumerWithCustomHandler() {

        final CheckedDoubleConsumer doubleConsumer = d -> {
            throw new Exception("" + d);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        DoubleConsumer test = Unchecked.doubleConsumer(doubleConsumer, handler);
        DoubleConsumer alias = UncheckedDoubleConsumer.unchecked(doubleConsumer, handler);

        assertDoubleConsumer(test, IllegalStateException.class);
        assertDoubleConsumer(alias, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertConsumer(Consumer<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Arrays.asList("a", "b", "c").stream().forEach(test);
        } catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertIntConsumer(IntConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            Arrays.stream(new int[]{1, 2, 3}).forEach(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertLongConsumer(LongConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0L);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            Arrays.stream(new long[]{1L, 2L, 3L}).forEach(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertDoubleConsumer(DoubleConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0.0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0.0");
        }

        try {
            Arrays.stream(new double[]{1.0, 2.0, 3.0}).forEach(test);
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
