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
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;
import org.jooq.lambda.fi.util.function.CheckedBiFunction;
import org.jooq.lambda.fi.util.function.CheckedToDoubleBiFunction;
import org.jooq.lambda.fi.util.function.CheckedToIntBiFunction;
import org.jooq.lambda.fi.util.function.CheckedToLongBiFunction;
import org.jooq.lambda.unchecked.UncheckedBiFunction;
import org.jooq.lambda.unchecked.UncheckedToDoubleBiFunction;
import org.jooq.lambda.unchecked.UncheckedToIntBiFunction;
import org.jooq.lambda.unchecked.UncheckedToLongBiFunction;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class CheckedBiFunctionTest {

    @Test
    public void testCheckedBiFunction() {

        final CheckedBiFunction<Object, Object, Object> biFunction = (t, u) -> {
            throw new Exception(t + ":" + u);
        };

        BiFunction<Object, Object, Object> test = Unchecked.biFunction(biFunction);
        BiFunction<Object, Object, Object> alias = UncheckedBiFunction.unchecked(biFunction);

        assertBiFunction(test, UncheckedException.class);
        assertBiFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedBiFunctionWithCustomHandler() {

        final CheckedBiFunction<Object, Object, Object> biFunction = (t, u) -> {
            throw new Exception(t + ":" + u);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        BiFunction<Object, Object, Object> test = Unchecked.biFunction(biFunction, handler);
        BiFunction<Object, Object, Object> alias = UncheckedBiFunction.unchecked(biFunction, handler);

        assertBiFunction(test, IllegalStateException.class);
        assertBiFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedToIntBiFunction() {

        final CheckedToIntBiFunction<Object, Object> toIntBiFunction = (t, u) -> {
            throw new Exception(t + ":" + u);
        };

        ToIntBiFunction<Object, Object> test = Unchecked.toIntBiFunction(toIntBiFunction);
        ToIntBiFunction<Object, Object> alias = UncheckedToIntBiFunction.unchecked(toIntBiFunction);

        assertToIntBiFunction(test, UncheckedException.class);
        assertToIntBiFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedToIntBiFunctionWithCustomHandler() {

        final CheckedToIntBiFunction<Object, Object> toIntBiFunction = (t, u) -> {
            throw new Exception(t + ":" + u);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        ToIntBiFunction<Object, Object> test = Unchecked.toIntBiFunction(toIntBiFunction, handler);
        ToIntBiFunction<Object, Object> alias = UncheckedToIntBiFunction.unchecked(toIntBiFunction, handler);

        assertToIntBiFunction(test, IllegalStateException.class);
        assertToIntBiFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedToLongBiFunction() {

        final CheckedToLongBiFunction<Object, Object> toLongBiFunction = (t, u) -> {
            throw new Exception(t + ":" + u);
        };

        ToLongBiFunction<Object, Object> test = Unchecked.toLongBiFunction(toLongBiFunction);
        ToLongBiFunction<Object, Object> alias = UncheckedToLongBiFunction.unchecked(toLongBiFunction);

        assertToLongBiFunction(test, UncheckedException.class);
        assertToLongBiFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedToLongBiFunctionWithCustomHandler() {

        final CheckedToLongBiFunction<Object, Object> toLongBiFunction = (t, u) -> {
            throw new Exception(t + ":" + u);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        ToLongBiFunction<Object, Object> test = Unchecked.toLongBiFunction(toLongBiFunction, handler);
        ToLongBiFunction<Object, Object> alias = UncheckedToLongBiFunction.unchecked(toLongBiFunction, handler);

        assertToLongBiFunction(test, IllegalStateException.class);
        assertToLongBiFunction(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedToDoubleBiFunction() {

        final CheckedToDoubleBiFunction<Object, Object> toDoubleBiFunction = (t, u) -> {
            throw new Exception(t + ":" + u);
        };

        ToDoubleBiFunction<Object, Object> test = Unchecked.toDoubleBiFunction(toDoubleBiFunction);
        ToDoubleBiFunction<Object, Object> alias = UncheckedToDoubleBiFunction.unchecked(toDoubleBiFunction);

        assertToDoubleBiFunction(test, UncheckedException.class);
        assertToDoubleBiFunction(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedToDoubleBiFunctionWithCustomHandler() {

        final CheckedToDoubleBiFunction<Object, Object> toDoubleBiFunction = (t, u) -> {
            throw new Exception(t + ":" + u);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        ToDoubleBiFunction<Object, Object> test = Unchecked.toDoubleBiFunction(toDoubleBiFunction, handler);
        ToDoubleBiFunction<Object, Object> alias = UncheckedToDoubleBiFunction.unchecked(toDoubleBiFunction, handler);

        assertToDoubleBiFunction(test, IllegalStateException.class);
        assertToDoubleBiFunction(alias, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertBiFunction(BiFunction<Object, Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(null, null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }

        try {
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("a", "b");
            map.computeIfPresent("a", test);
        } catch (RuntimeException e) {
            assertException(type, e, "a:b");
        }
    }

    private <E extends RuntimeException> void assertToIntBiFunction(ToIntBiFunction<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(null, null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }
    }

    private <E extends RuntimeException> void assertToLongBiFunction(ToLongBiFunction<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(null, null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }
    }

    private <E extends RuntimeException> void assertToDoubleBiFunction(ToDoubleBiFunction<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(null, null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }
    }

    private <E extends RuntimeException> void assertException(Class<E> type, RuntimeException e, String message) {
        assertEquals(type, e.getClass());
        assertEquals(Exception.class, e.getCause().getClass());
        assertEquals(message, e.getCause().getMessage());
    }
}
