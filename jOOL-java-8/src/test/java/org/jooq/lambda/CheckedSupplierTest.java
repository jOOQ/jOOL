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
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.jooq.lambda.fi.util.function.CheckedBooleanSupplier;
import org.jooq.lambda.fi.util.function.CheckedDoubleSupplier;
import org.jooq.lambda.fi.util.function.CheckedIntSupplier;
import org.jooq.lambda.fi.util.function.CheckedLongSupplier;
import org.jooq.lambda.fi.util.function.CheckedSupplier;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class CheckedSupplierTest {

    @Test
    public void testCheckedSupplier() {
        final CheckedSupplier<Object> supplier = () -> {
            throw new Exception("object");
        };

        Supplier<Object> s1 = Unchecked.supplier(supplier);
        Supplier<Object> s2 = CheckedSupplier.unchecked(supplier);
        Supplier<Object> s3 = Sneaky.supplier(supplier);
        Supplier<Object> s4 = CheckedSupplier.sneaky(supplier);

        assertSupplier(s1, UncheckedException.class);
        assertSupplier(s2, UncheckedException.class);
        assertSupplier(s3, Exception.class);
        assertSupplier(s4, Exception.class);
    }

    @Test
    public void testCheckedSupplierWithCustomHandler() {
        final CheckedSupplier<Object> supplier = () -> {
            throw new Exception("object");
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        Supplier<Object> test = Unchecked.supplier(supplier, handler);
        Supplier<Object> alias = CheckedSupplier.unchecked(supplier, handler);

        assertSupplier(test, IllegalStateException.class);
        assertSupplier(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntSupplier() {
        final CheckedIntSupplier intSupplier = () -> {
            throw new Exception("int");
        };

        IntSupplier s1 = Unchecked.intSupplier(intSupplier);
        IntSupplier s2 = CheckedIntSupplier.unchecked(intSupplier);
        IntSupplier s3 = Sneaky.intSupplier(intSupplier);
        IntSupplier s4 = CheckedIntSupplier.sneaky(intSupplier);

        assertIntSupplier(s1, UncheckedException.class);
        assertIntSupplier(s2, UncheckedException.class);
        assertIntSupplier(s3, Exception.class);
        assertIntSupplier(s4, Exception.class);
    }

    @Test
    public void testCheckedIntSupplierWithCustomHandler() {
        final CheckedIntSupplier intSupplier = () -> {
            throw new Exception("int");
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        IntSupplier test = Unchecked.intSupplier(intSupplier, handler);
        IntSupplier alias = CheckedIntSupplier.unchecked(intSupplier, handler);

        assertIntSupplier(test, IllegalStateException.class);
        assertIntSupplier(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongSupplier() {
        final CheckedLongSupplier longSupplier = () -> {
            throw new Exception("long");
        };

        LongSupplier s1 = Unchecked.longSupplier(longSupplier);
        LongSupplier s2 = CheckedLongSupplier.unchecked(longSupplier);
        LongSupplier s3 = Sneaky.longSupplier(longSupplier);
        LongSupplier s4 = CheckedLongSupplier.sneaky(longSupplier);

        assertLongSupplier(s1, UncheckedException.class);
        assertLongSupplier(s2, UncheckedException.class);
        assertLongSupplier(s3, Exception.class);
        assertLongSupplier(s4, Exception.class);
    }

    @Test
    public void testCheckedLongSupplierWithCustomHandler() {
        final CheckedLongSupplier longSupplier = () -> {
            throw new Exception("long");
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        LongSupplier test = Unchecked.longSupplier(longSupplier, handler);
        LongSupplier alias = CheckedLongSupplier.unchecked(longSupplier, handler);

        assertLongSupplier(test, IllegalStateException.class);
        assertLongSupplier(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleSupplier() {
        final CheckedDoubleSupplier doubleSupplier = () -> {
            throw new Exception("double");
        };

        DoubleSupplier s1 = Unchecked.doubleSupplier(doubleSupplier);
        DoubleSupplier s2 = CheckedDoubleSupplier.unchecked(doubleSupplier);
        DoubleSupplier s3 = Sneaky.doubleSupplier(doubleSupplier);
        DoubleSupplier s4 = CheckedDoubleSupplier.sneaky(doubleSupplier);

        assertDoubleSupplier(s1, UncheckedException.class);
        assertDoubleSupplier(s2, UncheckedException.class);
        assertDoubleSupplier(s3, Exception.class);
        assertDoubleSupplier(s4, Exception.class);
    }

    @Test
    public void testCheckedDoubleSupplierWithCustomHandler() {
        final CheckedDoubleSupplier doubleSupplier = () -> {
            throw new Exception("double");
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        DoubleSupplier test = Unchecked.doubleSupplier(doubleSupplier, handler);
        DoubleSupplier alias = CheckedDoubleSupplier.unchecked(doubleSupplier, handler);

        assertDoubleSupplier(test, IllegalStateException.class);
        assertDoubleSupplier(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedBooleanSupplier() {
        final CheckedBooleanSupplier booleanSupplier = () -> {
            throw new Exception("boolean");
        };

        BooleanSupplier s1 = Unchecked.booleanSupplier(booleanSupplier);
        BooleanSupplier s2 = CheckedBooleanSupplier.unchecked(booleanSupplier);
        BooleanSupplier s3 = Sneaky.booleanSupplier(booleanSupplier);
        BooleanSupplier s4 = CheckedBooleanSupplier.sneaky(booleanSupplier);

        assertBooleanSupplier(s1, UncheckedException.class);
        assertBooleanSupplier(s2, UncheckedException.class);
        assertBooleanSupplier(s3, Exception.class);
        assertBooleanSupplier(s4, Exception.class);
    }

    @Test
    public void testCheckedBooleanSupplierWithCustomHandler() {
        final CheckedBooleanSupplier booleanSupplier = () -> {
            throw new Exception("boolean");
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        BooleanSupplier test = Unchecked.booleanSupplier(booleanSupplier, handler);
        BooleanSupplier alias = CheckedBooleanSupplier.unchecked(booleanSupplier, handler);

        assertBooleanSupplier(test, IllegalStateException.class);
        assertBooleanSupplier(alias, IllegalStateException.class);
    }

    private <E extends Exception> void assertSupplier(Supplier<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.get();
            fail();
        }
        catch (Exception e) {
            assertException(type, e, "object");
        }

        try {
            Stream.generate(test).findFirst();
        }
        catch (Exception e) {
            assertException(type, e, "object");
        }
    }

    private <E extends Exception> void assertIntSupplier(IntSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsInt();
            fail();
        }
        catch (Exception e) {
            assertException(type, e, "int");
        }

        try {
            IntStream.generate(test).findFirst();
        }
        catch (Exception e) {
            assertException(type, e, "int");
        }
    }

    private <E extends Exception> void assertLongSupplier(LongSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsLong();
            fail();
        }
        catch (Exception e) {
            assertException(type, e, "long");
        }

        try {
            LongStream.generate(test).findFirst();
        }
        catch (Exception e) {
            assertException(type, e, "long");
        }
    }

    private <E extends Exception> void assertDoubleSupplier(DoubleSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsDouble();
            fail();
        }
        catch (Exception e) {
            assertException(type, e, "double");
        }

        try {
            DoubleStream.generate(test).findFirst();
        }
        catch (Exception e) {
            assertException(type, e, "double");
        }
    }

    private <E extends Exception> void assertBooleanSupplier(BooleanSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsBoolean();
            fail();
        }
        catch (Exception e) {
            assertException(type, e, "boolean");
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
