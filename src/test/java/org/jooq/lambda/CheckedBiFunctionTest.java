/**
 * Copyright (c) 2014-2016, Data Geekery GmbH, contact@datageekery.com
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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

import static org.junit.Assert.*;

/**
 * @author Lukas Eder
 */
public class CheckedBiFunctionTest {

    @Test
    public void testCheckedBiFunction() {
        BiFunction<Object, Object, Object> test = Unchecked.biFunction(
            (t, u) -> {
                throw new Exception(t + ":" + u);
            }
        );

        assertBiFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedBiFunctionWithCustomHandler() {
        BiFunction<Object, Object, Object> test = Unchecked.biFunction(
            (t, u) -> {
                throw new Exception(t + ":" + u);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertBiFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedToIntBiFunction() {
        ToIntBiFunction<Object, Object> test = Unchecked.toIntBiFunction(
            (t, u) -> {
                throw new Exception(t + ":" + u);
            }
        );

        assertToIntBiFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedToIntBiFunctionWithCustomHandler() {
        ToIntBiFunction<Object, Object> test = Unchecked.toIntBiFunction(
            (t, u) -> {
                throw new Exception(t + ":" + u);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertToIntBiFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedToLongBiFunction() {
        ToLongBiFunction<Object, Object> test = Unchecked.toLongBiFunction(
            (t, u) -> {
                throw new Exception(t + ":" + u);
            }
        );

        assertToLongBiFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedToLongBiFunctionWithCustomHandler() {
        ToLongBiFunction<Object, Object> test = Unchecked.toLongBiFunction(
            (t, u) -> {
                throw new Exception(t + ":" + u);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertToLongBiFunction(test, IllegalStateException.class);
    }

    @Test
    public void testCheckedToDoubleBiFunction() {
        ToDoubleBiFunction<Object, Object> test = Unchecked.toDoubleBiFunction(
            (t, u) -> {
                throw new Exception(t + ":" + u);
            }
        );

        assertToDoubleBiFunction(test, UncheckedException.class);
    }

    @Test
    public void testCheckedToDoubleBiFunctionWithCustomHandler() {
        ToDoubleBiFunction<Object, Object> test = Unchecked.toDoubleBiFunction(
            (t, u) -> {
                throw new Exception(t + ":" + u);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertToDoubleBiFunction(test, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertBiFunction(BiFunction<Object, Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.apply(null, null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }

        try {
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("a", "b");
            map.computeIfPresent("a", test);
        }
        catch (RuntimeException e) {
            assertException(type, e, "a:b");
        }
    }

    private <E extends RuntimeException> void assertToIntBiFunction(ToIntBiFunction<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsInt(null, null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }
    }

    private <E extends RuntimeException> void assertToLongBiFunction(ToLongBiFunction<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsLong(null, null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }
    }

    private <E extends RuntimeException> void assertToDoubleBiFunction(ToDoubleBiFunction<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.applyAsDouble(null, null);
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }
    }

    private <E extends RuntimeException> void assertException(Class<E> type, RuntimeException e, String message) {
        assertEquals(type, e.getClass());
        assertEquals(Exception.class, e.getCause().getClass());
        assertEquals(message, e.getCause().getMessage());
    }
}
