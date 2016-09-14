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

        Function<Object, Object> f1 = Unchecked.function(function);
        Function<Object, Object> f2 = CheckedFunction.unchecked(function);
        Function<Object, Object> f3 = Sneaky.function(function);
        Function<Object, Object> f4 = CheckedFunction.sneaky(function);

        assertFunction(f1, UncheckedException.class);
        assertFunction(f2, UncheckedException.class);
        assertFunction(f3, Exception.class);
        assertFunction(f4, Exception.class);
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
        Function<Object, Object> alias = CheckedFunction.unchecked(function, handler);

        assertFunction(test, IllegalStateException.class);
        assertFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedToIntFunction() {
        final CheckedToIntFunction<Object> toIntFunction = t -> {
            throw new Exception("" + t);
        };

        ToIntFunction<Object> f1 = Unchecked.toIntFunction(toIntFunction);
        ToIntFunction<Object> f2 = CheckedToIntFunction.unchecked(toIntFunction);
        ToIntFunction<Object> f3 = Sneaky.toIntFunction(toIntFunction);
        ToIntFunction<Object> f4 = CheckedToIntFunction.sneaky(toIntFunction);

        assertToIntFunction(f1, UncheckedException.class);
        assertToIntFunction(f2, UncheckedException.class);
        assertToIntFunction(f3, Exception.class);
        assertToIntFunction(f4, Exception.class);
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
        ToIntFunction<Object> alias = CheckedToIntFunction.unchecked(toIntFunction, handler);

        assertToIntFunction(test, IllegalStateException.class);
        assertToIntFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedToLongFunction() {
        final CheckedToLongFunction<Object> toLongFunction = t -> {
            throw new Exception("" + t);
        };

        ToLongFunction<Object> f1 = Unchecked.toLongFunction(toLongFunction);
        ToLongFunction<Object> f2 = CheckedToLongFunction.unchecked(toLongFunction);
        ToLongFunction<Object> f3 = Sneaky.toLongFunction(toLongFunction);
        ToLongFunction<Object> f4 = CheckedToLongFunction.sneaky(toLongFunction);

        assertToLongFunction(f1, UncheckedException.class);
        assertToLongFunction(f2, UncheckedException.class);
        assertToLongFunction(f3, Exception.class);
        assertToLongFunction(f4, Exception.class);
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
        ToLongFunction<Object> alias = CheckedToLongFunction.unchecked(toLongFunction, handler);

        assertToLongFunction(test, IllegalStateException.class);
        assertToLongFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedToDoubleFunction() {
        final CheckedToDoubleFunction<Object> toDoubleFunction = t -> {
            throw new Exception("" + t);
        };

        ToDoubleFunction<Object> f1 = Unchecked.toDoubleFunction(toDoubleFunction);
        ToDoubleFunction<Object> f2 = CheckedToDoubleFunction.unchecked(toDoubleFunction);
        ToDoubleFunction<Object> f3 = Sneaky.toDoubleFunction(toDoubleFunction);
        ToDoubleFunction<Object> f4 = CheckedToDoubleFunction.sneaky(toDoubleFunction);

        assertToDoubleFunction(f1, UncheckedException.class);
        assertToDoubleFunction(f2, UncheckedException.class);
        assertToDoubleFunction(f3, Exception.class);
        assertToDoubleFunction(f4, Exception.class);
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
        ToDoubleFunction<Object> alias = CheckedToDoubleFunction.unchecked(toDoubleFunction, handler);

        assertToDoubleFunction(test, IllegalStateException.class);
        assertToDoubleFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntFunction() {
        final CheckedIntFunction<Object> intFunction = i -> {
            throw new Exception("" + i);
        };

        IntFunction<Object> f1 = Unchecked.intFunction(intFunction);
        IntFunction<Object> f2 = CheckedIntFunction.unchecked(intFunction);
        IntFunction<Object> f3 = Sneaky.intFunction(intFunction);
        IntFunction<Object> f4 = CheckedIntFunction.sneaky(intFunction);

        assertIntFunction(f1, UncheckedException.class);
        assertIntFunction(f2, UncheckedException.class);
        assertIntFunction(f3, Exception.class);
        assertIntFunction(f4, Exception.class);
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
        IntFunction<Object> alias = CheckedIntFunction.unchecked(intFunction, handler);

        assertIntFunction(test, IllegalStateException.class);
        assertIntFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntToLongFunction() {
        final CheckedIntToLongFunction intToLongFunction = i -> {
            throw new Exception("" + i);
        };

        IntToLongFunction f1 = Unchecked.intToLongFunction(intToLongFunction);
        IntToLongFunction f2 = CheckedIntToLongFunction.unchecked(intToLongFunction);
        IntToLongFunction f3 = Sneaky.intToLongFunction(intToLongFunction);
        IntToLongFunction f4 = CheckedIntToLongFunction.sneaky(intToLongFunction);

        assertIntToLongFunction(f1, UncheckedException.class);
        assertIntToLongFunction(f2, UncheckedException.class);
        assertIntToLongFunction(f3, Exception.class);
        assertIntToLongFunction(f4, Exception.class);
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
        IntToLongFunction alias = CheckedIntToLongFunction.unchecked(intToLongFunction, handler);

        assertIntToLongFunction(test, IllegalStateException.class);
        assertIntToLongFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedIntToDoubleFunction() {
        final CheckedIntToDoubleFunction intToDoubleFunction = i -> {
            throw new Exception("" + i);
        };

        IntToDoubleFunction f1 = Unchecked.intToDoubleFunction(intToDoubleFunction);
        IntToDoubleFunction f2 = CheckedIntToDoubleFunction.unchecked(intToDoubleFunction);
        IntToDoubleFunction f3 = Sneaky.intToDoubleFunction(intToDoubleFunction);
        IntToDoubleFunction f4 = CheckedIntToDoubleFunction.sneaky(intToDoubleFunction);

        assertIntToDoubleFunction(f1, UncheckedException.class);
        assertIntToDoubleFunction(f2, UncheckedException.class);
        assertIntToDoubleFunction(f3, Exception.class);
        assertIntToDoubleFunction(f4, Exception.class);
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
        IntToDoubleFunction alias = CheckedIntToDoubleFunction.unchecked(intToDoubleFunction, handler);

        assertIntToDoubleFunction(test, IllegalStateException.class);
        assertIntToDoubleFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongFunction() {
        final CheckedLongFunction<Object> longFunction = l -> {
            throw new Exception("" + l);
        };

        LongFunction<Object> f1 = Unchecked.longFunction(longFunction);
        LongFunction<Object> f2 = CheckedLongFunction.unchecked(longFunction);
        LongFunction<Object> f3 = Sneaky.longFunction(longFunction);
        LongFunction<Object> f4 = CheckedLongFunction.sneaky(longFunction);

        assertLongFunction(f1, UncheckedException.class);
        assertLongFunction(f2, UncheckedException.class);
        assertLongFunction(f3, Exception.class);
        assertLongFunction(f4, Exception.class);
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
        LongFunction<Object> alias = CheckedLongFunction.unchecked(longFunction, handler);

        assertLongFunction(test, IllegalStateException.class);
        assertLongFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongToIntFunction() {
        final CheckedLongToIntFunction longToIntFunction = l -> {
            throw new Exception("" + l);
        };

        LongToIntFunction f1 = Unchecked.longToIntFunction(longToIntFunction);
        LongToIntFunction f2 = CheckedLongToIntFunction.unchecked(longToIntFunction);
        LongToIntFunction f3 = Sneaky.longToIntFunction(longToIntFunction);
        LongToIntFunction f4 = CheckedLongToIntFunction.sneaky(longToIntFunction);

        assertLongToIntFunction(f1, UncheckedException.class);
        assertLongToIntFunction(f2, UncheckedException.class);
        assertLongToIntFunction(f3, Exception.class);
        assertLongToIntFunction(f4, Exception.class);
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
        LongToIntFunction alias = CheckedLongToIntFunction.unchecked(longToIntFunction, handler);

        assertLongToIntFunction(test, IllegalStateException.class);
        assertLongToIntFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedLongToDoubleFunction() {
        final CheckedLongToDoubleFunction longToDoubleFunction = l -> {
            throw new Exception("" + l);
        };

        LongToDoubleFunction f1 = Unchecked.longToDoubleFunction(longToDoubleFunction);
        LongToDoubleFunction f2 = CheckedLongToDoubleFunction.unchecked(longToDoubleFunction);
        LongToDoubleFunction f3 = Sneaky.longToDoubleFunction(longToDoubleFunction);
        LongToDoubleFunction f4 = CheckedLongToDoubleFunction.sneaky(longToDoubleFunction);

        assertLongToDoubleFunction(f1, UncheckedException.class);
        assertLongToDoubleFunction(f2, UncheckedException.class);
        assertLongToDoubleFunction(f3, Exception.class);
        assertLongToDoubleFunction(f4, Exception.class);
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
        LongToDoubleFunction alias = CheckedLongToDoubleFunction.unchecked(longToDoubleFunction, handler);

        assertLongToDoubleFunction(test, IllegalStateException.class);
        assertLongToDoubleFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleFunction() {
        final CheckedDoubleFunction<Object> doubleFunction = d -> {
            throw new Exception("" + d);
        };

        DoubleFunction<Object> f1 = Unchecked.doubleFunction(doubleFunction);
        DoubleFunction<Object> f2 = CheckedDoubleFunction.unchecked(doubleFunction);
        DoubleFunction<Object> f3 = Sneaky.doubleFunction(doubleFunction);
        DoubleFunction<Object> f4 = CheckedDoubleFunction.sneaky(doubleFunction);

        assertDoubleFunction(f1, UncheckedException.class);
        assertDoubleFunction(f2, UncheckedException.class);
        assertDoubleFunction(f3, Exception.class);
        assertDoubleFunction(f4, Exception.class);
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
        DoubleFunction<Object> alias = CheckedDoubleFunction.unchecked(doubleFunction, handler);

        assertDoubleFunction(test, IllegalStateException.class);
        assertDoubleFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleToIntFunction() {
        final CheckedDoubleToIntFunction doubleToIntFunction = d -> {
            throw new Exception("" + d);
        };

        DoubleToIntFunction f1 = Unchecked.doubleToIntFunction(doubleToIntFunction);
        DoubleToIntFunction f2 = CheckedDoubleToIntFunction.unchecked(doubleToIntFunction);
        DoubleToIntFunction f3 = Sneaky.doubleToIntFunction(doubleToIntFunction);
        DoubleToIntFunction f4 = CheckedDoubleToIntFunction.sneaky(doubleToIntFunction);

        assertDoubleToIntFunction(f1, UncheckedException.class);
        assertDoubleToIntFunction(f2, UncheckedException.class);
        assertDoubleToIntFunction(f3, Exception.class);
        assertDoubleToIntFunction(f4, Exception.class);
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
        DoubleToIntFunction alias = CheckedDoubleToIntFunction.unchecked(doubleToIntFunction, handler);

        assertDoubleToIntFunction(test, IllegalStateException.class);
        assertDoubleToIntFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedDoubleToLongFunction() {
        final CheckedDoubleToLongFunction doubleToLongFunction = d -> {
            throw new Exception("" + d);
        };

        DoubleToLongFunction f1 = Unchecked.doubleToLongFunction(doubleToLongFunction);
        DoubleToLongFunction f2 = CheckedDoubleToLongFunction.unchecked(doubleToLongFunction);
        DoubleToLongFunction f3 = Sneaky.doubleToLongFunction(doubleToLongFunction);
        DoubleToLongFunction f4 = CheckedDoubleToLongFunction.sneaky(doubleToLongFunction);

        assertDoubleToLongFunction(f1, UncheckedException.class);
        assertDoubleToLongFunction(f2, UncheckedException.class);
        assertDoubleToLongFunction(f3, Exception.class);
        assertDoubleToLongFunction(f4, Exception.class);
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
        DoubleToLongFunction alias = CheckedDoubleToLongFunction.unchecked(doubleToLongFunction, handler);

        assertDoubleToLongFunction(test, IllegalStateException.class);
        assertDoubleToLongFunction(alias, IllegalStateException.class);
    }

    private <E extends Exception> void assertFunction(Function<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(null);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Map<Object, Object> map = new LinkedHashMap<>();
            map.computeIfAbsent("a", test);
        } 
        catch (Exception e) {
            assertException(type, e, "a");
        }
    }

    private <E extends Exception> void assertToIntFunction(ToIntFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(null);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToInt(test);
        }
        catch (Exception e) {
            assertException(type, e, "a");
        }
    }

    private <E extends Exception> void assertToLongFunction(ToLongFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(null);
            fail();
        }
        catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToLong(test);
        } 
        catch (Exception e) {
            assertException(type, e, "a");
        }
    }

    private <E extends Exception> void assertToDoubleFunction(ToDoubleFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(null);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "null");
        }

        try {
            Stream.of("1", "2", "3").mapToDouble(test);
        } 
        catch (Exception e) {
            assertException(type, e, "a");
        }
    }

    private <E extends Exception> void assertIntFunction(IntFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0);
            fail();
        }
        catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToObj(test);
        } 
        catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertIntToLongFunction(IntToLongFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToLong(test);
        } 
        catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertIntToDoubleFunction(IntToDoubleFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            IntStream.of(1, 2, 3).mapToDouble(test);
        } 
        catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertLongFunction(LongFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0L);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToObj(test);
        } 
        catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertLongToIntFunction(LongToIntFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0L);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToInt(test);
        } 
        catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertLongToDoubleFunction(LongToDoubleFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(0L);
            fail();
        }
        catch (Exception e) {
            assertException(type, e, "0");
        }

        try {
            LongStream.of(1L, 2L, 3L).mapToDouble(test);
        } 
        catch (Exception e) {
            assertException(type, e, "1");
        }
    }

    private <E extends Exception> void assertDoubleFunction(DoubleFunction<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(0.0);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToObj(test);
        }
        catch (Exception e) {
            assertException(type, e, "1.0");
        }
    }

    private <E extends Exception> void assertDoubleToIntFunction(DoubleToIntFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(0.0);
            fail();
        } 
        catch (Exception e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToInt(test);
        } 
        catch (Exception e) {
            assertException(type, e, "1.0");
        }
    }

    private <E extends Exception> void assertDoubleToLongFunction(DoubleToLongFunction test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(0.0);
            fail();
        }
        catch (Exception e) {
            assertException(type, e, "0.0");
        }

        try {
            DoubleStream.of(1.0, 2.0, 3.0).mapToLong(test);
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
