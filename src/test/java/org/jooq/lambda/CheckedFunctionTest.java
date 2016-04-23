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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.LongFunction;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.jooq.lambda.fi.util.function.CheckedDoubleFunction;
import org.jooq.lambda.fi.util.function.CheckedDoubleToIntFunction;
import org.jooq.lambda.fi.util.function.CheckedDoubleToLongFunction;
import org.jooq.lambda.fi.util.function.CheckedFunction;
import org.jooq.lambda.fi.util.function.CheckedIntFunction;
import org.jooq.lambda.fi.util.function.CheckedIntToDoubleFunction;
import org.jooq.lambda.fi.util.function.CheckedIntToLongFunction;
import org.jooq.lambda.fi.util.function.CheckedLongFunction;
import org.jooq.lambda.fi.util.function.CheckedLongToDoubleFunction;
import org.jooq.lambda.fi.util.function.CheckedLongToIntFunction;
import org.jooq.lambda.fi.util.function.CheckedToDoubleFunction;
import org.jooq.lambda.fi.util.function.CheckedToIntFunction;
import org.jooq.lambda.fi.util.function.CheckedToLongFunction;
import org.jooq.lambda.unchecked.UncheckedDoubleFunction;
import org.jooq.lambda.unchecked.UncheckedDoubleToIntFunction;
import org.jooq.lambda.unchecked.UncheckedDoubleToLongFunction;
import org.jooq.lambda.unchecked.UncheckedFunction;
import org.jooq.lambda.unchecked.UncheckedIntFunction;
import org.jooq.lambda.unchecked.UncheckedIntToDoubleFunction;
import org.jooq.lambda.unchecked.UncheckedIntToLongFunction;
import org.jooq.lambda.unchecked.UncheckedLongFunction;
import org.jooq.lambda.unchecked.UncheckedLongToDoubleFunction;
import org.jooq.lambda.unchecked.UncheckedLongToIntFunction;
import org.jooq.lambda.unchecked.UncheckedToDoubleFunction;
import org.jooq.lambda.unchecked.UncheckedToIntFunction;
import org.jooq.lambda.unchecked.UncheckedToLongFunction;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class CheckedFunctionTest {

    @Test
    public void testCheckedFunction() {

        final CheckedFunction<Object, Object> function = t -> {
            throw new Exception("" + t);
        };

        Function<Object, Object> test = Unchecked.function(function);
        Function<Object, Object> alias = UncheckedFunction.unchecked(function);

        assertFunction(test, UncheckedException.class);
        assertFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedFunctionWithCustomHandler() {

        final CheckedFunction<Object, Object> function = t -> {
            throw new Exception("" + t);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        Function<Object, Object> test = Unchecked.function(function, handler);
        Function<Object, Object> alias = UncheckedFunction.unchecked(function, handler);

        assertFunction(test, IllegalStateException.class);
        assertFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedToIntFunction() {

        final CheckedToIntFunction<Object> toIntFunction = t -> {
            throw new Exception("" + t);
        };

        ToIntFunction<Object> test = Unchecked.toIntFunction(toIntFunction);
        ToIntFunction<Object> alias = UncheckedToIntFunction.unchecked(toIntFunction);

        assertToIntFunction(test, UncheckedException.class);
        assertToIntFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedToIntFunctionWithCustomHandler() {

        final CheckedToIntFunction<Object> toIntFunction = t -> {
            throw new Exception("" + t);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        ToIntFunction<Object> test = Unchecked.toIntFunction(toIntFunction, handler);
        ToIntFunction<Object> alias = UncheckedToIntFunction.unchecked(toIntFunction, handler);

        assertToIntFunction(test, IllegalStateException.class);
        assertToIntFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedToLongFunction() {

        final CheckedToLongFunction<Object> toLongFunction = t -> {
            throw new Exception("" + t);
        };

        ToLongFunction<Object> test = Unchecked.toLongFunction(toLongFunction);
        ToLongFunction<Object> alias = UncheckedToLongFunction.unchecked(toLongFunction);

        assertToLongFunction(test, UncheckedException.class);
        assertToLongFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedToLongFunctionWithCustomHandler() {

        final CheckedToLongFunction<Object> toLongFunction = t -> {
            throw new Exception("" + t);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        ToLongFunction<Object> test = Unchecked.toLongFunction(toLongFunction, handler);
        ToLongFunction<Object> alias = UncheckedToLongFunction.unchecked(toLongFunction, handler);

        assertToLongFunction(test, IllegalStateException.class);
        assertToLongFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedToDoubleFunction() {

        final CheckedToDoubleFunction<Object> toDoubleFunction = t -> {
            throw new Exception("" + t);
        };

        ToDoubleFunction<Object> test = Unchecked.toDoubleFunction(toDoubleFunction);
        ToDoubleFunction<Object> alias = UncheckedToDoubleFunction.unchecked(toDoubleFunction);

        assertToDoubleFunction(test, UncheckedException.class);
        assertToDoubleFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedToDoubleunctionWithCustomHandler() {

        final CheckedToDoubleFunction<Object> toDoubleFunction = t -> {
            throw new Exception("" + t);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        ToDoubleFunction<Object> test = Unchecked.toDoubleFunction(toDoubleFunction, handler);
        ToDoubleFunction<Object> alias = UncheckedToDoubleFunction.unchecked(toDoubleFunction, handler);

        assertToDoubleFunction(test, IllegalStateException.class);
        assertToDoubleFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntFunction() {

        final CheckedIntFunction<Object> intFunction = i -> {
            throw new Exception("" + i);
        };

        IntFunction<Object> test = Unchecked.intFunction(intFunction);
        IntFunction<Object> alias = UncheckedIntFunction.unchecked(intFunction);

        assertIntFunction(test, UncheckedException.class);
        assertIntFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedIntFunctionWithCustomHandler() {

        final CheckedIntFunction<Object> intFunction = i -> {
            throw new Exception("" + i);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        IntFunction<Object> test = Unchecked.intFunction(intFunction, handler);
        IntFunction<Object> alias = UncheckedIntFunction.unchecked(intFunction, handler);

        assertIntFunction(test, IllegalStateException.class);
        assertIntFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntToLongFunction() {

        final CheckedIntToLongFunction intToLongFunction = i -> {
            throw new Exception("" + i);
        };

        IntToLongFunction test = Unchecked.intToLongFunction(intToLongFunction);
        IntToLongFunction alias = UncheckedIntToLongFunction.unchecked(intToLongFunction);

        assertIntToLongFunction(test, UncheckedException.class);
        assertIntToLongFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedIntToLongFunctionWithCustomHandler() {

        final CheckedIntToLongFunction intToLongFunction = i -> {
            throw new Exception("" + i);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        IntToLongFunction test = Unchecked.intToLongFunction(intToLongFunction, handler);
        IntToLongFunction alias = UncheckedIntToLongFunction.unchecked(intToLongFunction, handler);

        assertIntToLongFunction(test, IllegalStateException.class);
        assertIntToLongFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntToDoubleFunction() {

        final CheckedIntToDoubleFunction intToDoubleFunction = i -> {
            throw new Exception("" + i);
        };

        IntToDoubleFunction test = Unchecked.intToDoubleFunction(intToDoubleFunction);
        IntToDoubleFunction alias = UncheckedIntToDoubleFunction.unchecked(intToDoubleFunction);

        assertIntToDoubleFunction(test, UncheckedException.class);
        assertIntToDoubleFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedIntToDoubleFunctionWithCustomHandler() {

        final CheckedIntToDoubleFunction intToDoubleFunction = i -> {
            throw new Exception("" + i);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        IntToDoubleFunction test = Unchecked.intToDoubleFunction(intToDoubleFunction, handler);
        IntToDoubleFunction alias = UncheckedIntToDoubleFunction.unchecked(intToDoubleFunction, handler);

        assertIntToDoubleFunction(test, IllegalStateException.class);
        assertIntToDoubleFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongFunction() {

        final CheckedLongFunction<Object> longFunction = l -> {
            throw new Exception("" + l);
        };

        LongFunction<Object> test = Unchecked.longFunction(longFunction);
        LongFunction<Object> alias = UncheckedLongFunction.unchecked(longFunction);

        assertLongFunction(test, UncheckedException.class);
        assertLongFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedLongFunctionWithCustomHandler() {

        final CheckedLongFunction<Object> longFunction = l -> {
            throw new Exception("" + l);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        LongFunction<Object> test = Unchecked.longFunction(longFunction, handler);
        LongFunction<Object> alias = UncheckedLongFunction.unchecked(longFunction, handler);

        assertLongFunction(test, IllegalStateException.class);
        assertLongFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongToIntFunction() {

        final CheckedLongToIntFunction longToIntFunction = l -> {
            throw new Exception("" + l);
        };

        LongToIntFunction test = Unchecked.longToIntFunction(longToIntFunction);
        LongToIntFunction alias = UncheckedLongToIntFunction.unchecked(longToIntFunction);

        assertLongToIntFunction(test, UncheckedException.class);
        assertLongToIntFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedLongToIntFunctionWithCustomHandler() {

        final CheckedLongToIntFunction longToIntFunction = l -> {
            throw new Exception("" + l);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        LongToIntFunction test = Unchecked.longToIntFunction(longToIntFunction, handler);
        LongToIntFunction alias = UncheckedLongToIntFunction.unchecked(longToIntFunction, handler);

        assertLongToIntFunction(test, IllegalStateException.class);
        assertLongToIntFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongToDoubleFunction() {

        final CheckedLongToDoubleFunction longToDoubleFunction = l -> {
            throw new Exception("" + l);
        };

        LongToDoubleFunction test = Unchecked.longToDoubleFunction(longToDoubleFunction);
        LongToDoubleFunction alias = UncheckedLongToDoubleFunction.unchecked(longToDoubleFunction);

        assertLongToDoubleFunction(test, UncheckedException.class);
        assertLongToDoubleFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedLongToDoubleFunctionWithCustomHandler() {

        final CheckedLongToDoubleFunction longToDoubleFunction = l -> {
            throw new Exception("" + l);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        LongToDoubleFunction test = Unchecked.longToDoubleFunction(longToDoubleFunction, handler);
        LongToDoubleFunction alias = UncheckedLongToDoubleFunction.unchecked(longToDoubleFunction, handler);

        assertLongToDoubleFunction(test, IllegalStateException.class);
        assertLongToDoubleFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleFunction() {

        final CheckedDoubleFunction<Object> doubleFunction = d -> {
            throw new Exception("" + d);
        };

        DoubleFunction<Object> test = Unchecked.doubleFunction(doubleFunction);
        DoubleFunction<Object> alias = UncheckedDoubleFunction.unchecked(doubleFunction);

        assertDoubleFunction(test, UncheckedException.class);
        assertDoubleFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedDoubleFunctionWithCustomHandler() {

        final CheckedDoubleFunction<Object> doubleFunction = d -> {
            throw new Exception("" + d);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        DoubleFunction<Object> test = Unchecked.doubleFunction(doubleFunction, handler);
        DoubleFunction<Object> alias = UncheckedDoubleFunction.unchecked(doubleFunction, handler);

        assertDoubleFunction(test, IllegalStateException.class);
        assertDoubleFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleToIntFunction() {

        final CheckedDoubleToIntFunction doubleToIntFunction = d -> {
            throw new Exception("" + d);
        };

        DoubleToIntFunction test = Unchecked.doubleToIntFunction(doubleToIntFunction);
        DoubleToIntFunction alias = UncheckedDoubleToIntFunction.unchecked(doubleToIntFunction);

        assertDoubleToIntFunction(test, UncheckedException.class);
        assertDoubleToIntFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedDoubleToIntFunctionWithCustomHandler() {

        final CheckedDoubleToIntFunction doubleToIntFunction = d -> {
            throw new Exception("" + d);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        DoubleToIntFunction test = Unchecked.doubleToIntFunction(doubleToIntFunction, handler);
        DoubleToIntFunction alias = UncheckedDoubleToIntFunction.unchecked(doubleToIntFunction, handler);

        assertDoubleToIntFunction(test, IllegalStateException.class);
        assertDoubleToIntFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleToLongFunction() {

        final CheckedDoubleToLongFunction doubleToLongFunction = d -> {
            throw new Exception("" + d);
        };

        DoubleToLongFunction test = Unchecked.doubleToLongFunction(doubleToLongFunction);
        DoubleToLongFunction alias = UncheckedDoubleToLongFunction.unchecked(doubleToLongFunction);

        assertDoubleToLongFunction(test, UncheckedException.class);
        assertDoubleToLongFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedDoubleToLongFunctionWithCustomHandler() {

        final CheckedDoubleToLongFunction doubleToLongFunction = d -> {
            throw new Exception("" + d);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        DoubleToLongFunction test = Unchecked.doubleToLongFunction(doubleToLongFunction, handler);
        DoubleToLongFunction alias = UncheckedDoubleToLongFunction.unchecked(doubleToLongFunction, handler);

        assertDoubleToLongFunction(test, IllegalStateException.class);
        assertDoubleToLongFunction(alias, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertFunction(Function<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Map<Object, Object> map = new LinkedHashMap<>();
            map.computeIfAbsent("a", test);
        } catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertToIntFunction(ToIntFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToInt(test);
        } catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertToLongFunction(ToLongFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToLong(test);
        } catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertToDoubleFunction(ToDoubleFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToDouble(test);
        } catch (RuntimeException e) {
            assertException(type, e, "a");
        }
    }

    private <E extends RuntimeException> void assertIntFunction(IntFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToObj(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertIntToLongFunction(IntToLongFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToLong(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertIntToDoubleFunction(IntToDoubleFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToDouble(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertLongFunction(LongFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0L);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToObj(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertLongToIntFunction(LongToIntFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0L);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToInt(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertLongToDoubleFunction(LongToDoubleFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0L);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToDouble(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1");
        }
    }

    private <E extends RuntimeException> void assertDoubleFunction(DoubleFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0.0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToObj(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1.0");
        }
    }

    private <E extends RuntimeException> void assertDoubleToIntFunction(DoubleToIntFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0.0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToInt(test);
        } catch (RuntimeException e) {
            assertException(type, e, "1.0");
        }
    }

    private <E extends RuntimeException> void assertDoubleToLongFunction(DoubleToLongFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0.0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToLong(test);
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
