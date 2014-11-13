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

import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * @author Lukas Eder
 */
public class CheckedSupplierTest {

    @Test
    public void testCheckedSupplier() {
        Supplier<Object> test = Unchecked.supplier(
            () -> {
                throw new Exception("object");
            }
        );

        assertSupplier(test, UncheckedException.class);
    }

    @Test
    public void testCheckedSupplierWithCustomHandler() {
        Supplier<Object> test = Unchecked.supplier(
            () -> {
                throw new Exception("object");
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertSupplier(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntSupplier() {
        IntSupplier test = Unchecked.intSupplier(
            () -> {
                throw new Exception("int");
            }
        );

        assertIntSupplier(test, UncheckedException.class);
    }

    @Test
    public void testCheckedIntSupplierWithCustomHandler() {
        IntSupplier test = Unchecked.intSupplier(
            () -> {
                throw new Exception("int");
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertIntSupplier(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongSupplier() {
        LongSupplier test = Unchecked.longSupplier(
            () -> {
                throw new Exception("long");
            }
        );

        assertLongSupplier(test, UncheckedException.class);
    }

    @Test
    public void testCheckedLongSupplierWithCustomHandler() {
        LongSupplier test = Unchecked.longSupplier(
            () -> {
                throw new Exception("long");
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertLongSupplier(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleSupplier() {
        DoubleSupplier test = Unchecked.doubleSupplier(
            () -> {
                throw new Exception("double");
            }
        );

        assertDoubleSupplier(test, UncheckedException.class);
    }

    @Test
    public void testCheckedDoubleSupplierWithCustomHandler() {
        DoubleSupplier test = Unchecked.doubleSupplier(
            () -> {
                throw new Exception("double");
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertDoubleSupplier(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedBooleanSupplier() {
        BooleanSupplier test = Unchecked.booleanSupplier(
            () -> {
                throw new Exception("boolean");
            }
        );

        assertBooleanSupplier(test, UncheckedException.class);
    }

    @Test
    public void testCheckedBooleanSupplierWithCustomHandler() {
        BooleanSupplier test = Unchecked.booleanSupplier(
            () -> {
                throw new Exception("boolean");
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertBooleanSupplier(test, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertSupplier(Supplier<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.get();
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "object");
        }

        try {
            Stream.generate(test).findFirst();
        }
        catch (RuntimeException e) {
            assertException(type, e, "object");
        }
    }

    private <E extends RuntimeException> void assertIntSupplier(IntSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsInt();
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "int");
        }

        try {
            IntStream.generate(test).findFirst();
        }
        catch (RuntimeException e) {
            assertException(type, e, "int");
        }
    }

    private <E extends RuntimeException> void assertLongSupplier(LongSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsLong();
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "long");
        }

        try {
            LongStream.generate(test).findFirst();
        }
        catch (RuntimeException e) {
            assertException(type, e, "long");
        }
    }

    private <E extends RuntimeException> void assertDoubleSupplier(DoubleSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsDouble();
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "double");
        }

        try {
            DoubleStream.generate(test).findFirst();
        }
        catch (RuntimeException e) {
            assertException(type, e, "double");
        }
    }

    private <E extends RuntimeException> void assertBooleanSupplier(BooleanSupplier test, Class<E> type) {
        assertNotNull(test);
        try {
            test.getAsBoolean();
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "boolean");
        }
    }

    private <E extends RuntimeException> void assertException(Class<E> type, RuntimeException e, String message) {
        assertEquals(type, e.getClass());
        assertEquals(Exception.class, e.getCause().getClass());
        assertEquals(message, e.getCause().getMessage());
    }
}
