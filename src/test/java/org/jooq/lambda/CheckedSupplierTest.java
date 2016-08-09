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

        Supplier<Object> test = Unchecked.supplier(supplier);
        Supplier<Object> alias = CheckedSupplier.unchecked(supplier);

        assertSupplier(test, UncheckedException.class);
        assertSupplier(alias, UncheckedException.class);
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

        IntSupplier test = Unchecked.intSupplier(intSupplier);
        IntSupplier alias = CheckedIntSupplier.unchecked(intSupplier);

        assertIntSupplier(test, UncheckedException.class);
        assertIntSupplier(alias, UncheckedException.class);
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

        LongSupplier test = Unchecked.longSupplier(longSupplier);
        LongSupplier alias = CheckedLongSupplier.unchecked(longSupplier);

        assertLongSupplier(test, UncheckedException.class);
        assertLongSupplier(alias, UncheckedException.class);
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

        DoubleSupplier test = Unchecked.doubleSupplier(doubleSupplier);
        DoubleSupplier alias = CheckedDoubleSupplier.unchecked(doubleSupplier);

        assertDoubleSupplier(test, UncheckedException.class);
        assertDoubleSupplier(alias, UncheckedException.class);
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

        BooleanSupplier test = Unchecked.booleanSupplier(booleanSupplier);
        BooleanSupplier alias = CheckedBooleanSupplier.unchecked(booleanSupplier);

        assertBooleanSupplier(test, UncheckedException.class);
        assertBooleanSupplier(alias, UncheckedException.class);
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

    private <E extends RuntimeException> void assertSupplier(Supplier<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.get();
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "object");
        }

        try {
            Stream.generate(test).findFirst();
        } catch (RuntimeException e) {
            assertException(type, e, "object");
        }
    }

    private <E extends RuntimeException> void assertIntSupplier(IntSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsInt();
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "int");
        }

        try {
            IntStream.generate(test).findFirst();
        } catch (RuntimeException e) {
            assertException(type, e, "int");
        }
    }

    private <E extends RuntimeException> void assertLongSupplier(LongSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsLong();
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "long");
        }

        try {
            LongStream.generate(test).findFirst();
        } catch (RuntimeException e) {
            assertException(type, e, "long");
        }
    }

    private <E extends RuntimeException> void assertDoubleSupplier(DoubleSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsDouble();
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "double");
        }

        try {
            DoubleStream.generate(test).findFirst();
        } catch (RuntimeException e) {
            assertException(type, e, "double");
        }
    }

    private <E extends RuntimeException> void assertBooleanSupplier(BooleanSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsBoolean();
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "boolean");
        }
    }

    private <E extends RuntimeException> void assertException(Class<E> type, RuntimeException e, String message) {
        assertEquals(type, e.getClass());
        assertEquals(Exception.class, e.getCause().getClass());
        assertEquals(message, e.getCause().getMessage());
    }
}
