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
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import org.jooq.lambda.fi.util.function.CheckedConsumer;
import org.jooq.lambda.fi.util.function.CheckedDoubleConsumer;
import org.jooq.lambda.fi.util.function.CheckedIntConsumer;
import org.jooq.lambda.fi.util.function.CheckedLongConsumer;
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

        Consumer<Object> c1 = Unchecked.consumer(consumer);
        Consumer<Object> c2 = CheckedConsumer.unchecked(consumer);
        Consumer<Object> c3 = Sneaky.consumer(consumer);
        Consumer<Object> c4 = CheckedConsumer.sneaky(consumer);

        assertConsumer(c1, UncheckedException.class);
        assertConsumer(c2, UncheckedException.class);
        assertConsumer(c3, Exception.class);
        assertConsumer(c4, Exception.class);
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
        Consumer<Object> alias = CheckedConsumer.unchecked(consumer, handler);

        assertConsumer(test, IllegalStateException.class);
        assertConsumer(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntConsumer() {
        final CheckedIntConsumer intConsumer = i -> {
            throw new Exception("" + i);
        };

        IntConsumer c1 = Unchecked.intConsumer(intConsumer);
        IntConsumer c2 = CheckedIntConsumer.unchecked(intConsumer);
        IntConsumer c3 = Sneaky.intConsumer(intConsumer);
        IntConsumer c4 = CheckedIntConsumer.sneaky(intConsumer);

        assertIntConsumer(c1, UncheckedException.class);
        assertIntConsumer(c2, UncheckedException.class);
        assertIntConsumer(c3, Exception.class);
        assertIntConsumer(c4, Exception.class);
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
        IntConsumer alias = CheckedIntConsumer.unchecked(intConsumer, handler);

        assertIntConsumer(test, IllegalStateException.class);
        assertIntConsumer(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongConsumer() {
        final CheckedLongConsumer longConsumer = l -> {
            throw new Exception("" + l);
        };

        LongConsumer c1 = Unchecked.longConsumer(longConsumer);
        LongConsumer c2 = CheckedLongConsumer.unchecked(longConsumer);
        LongConsumer c3 = Sneaky.longConsumer(longConsumer);
        LongConsumer c4 = CheckedLongConsumer.sneaky(longConsumer);

        assertLongConsumer(c1, UncheckedException.class);
        assertLongConsumer(c2, UncheckedException.class);
        assertLongConsumer(c3, Exception.class);
        assertLongConsumer(c4, Exception.class);
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
        LongConsumer alias = CheckedLongConsumer.unchecked(longConsumer, handler);

        assertLongConsumer(test, IllegalStateException.class);
        assertLongConsumer(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleConsumer() {
        final CheckedDoubleConsumer doubleConsumer = d -> {
            throw new Exception("" + d);
        };

        DoubleConsumer c1 = Unchecked.doubleConsumer(doubleConsumer);
        DoubleConsumer c2 = CheckedDoubleConsumer.unchecked(doubleConsumer);
        DoubleConsumer c3 = Sneaky.doubleConsumer(doubleConsumer);
        DoubleConsumer c4 = CheckedDoubleConsumer.sneaky(doubleConsumer);

        assertDoubleConsumer(c1, UncheckedException.class);
        assertDoubleConsumer(c2, UncheckedException.class);
        assertDoubleConsumer(c3, Exception.class);
        assertDoubleConsumer(c4, Exception.class);
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
        DoubleConsumer alias = CheckedDoubleConsumer.unchecked(doubleConsumer, handler);

        assertDoubleConsumer(test, IllegalStateException.class);
        assertDoubleConsumer(alias, IllegalStateException.class);
    }

    private <E extends Exception> void assertConsumer(Consumer<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Arrays.asList("a", "b", "c").stream().forEach(test);
        }
        catch (Exception e) {
            assertException(type, e, "a");
        }
    }

    private <E extends Exception> void assertIntConsumer(IntConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            Arrays.stream(new int[]{1, 2, 3}).forEach(test);
        } 
        catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertLongConsumer(LongConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0L);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            Arrays.stream(new long[]{1L, 2L, 3L}).forEach(test);
        } 
        catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertDoubleConsumer(DoubleConsumer test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(0.0);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0.0");
        }

        try {
            Arrays.stream(new double[]{1.0, 2.0, 3.0}).forEach(test);
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
