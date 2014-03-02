/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.unchecked;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.*;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;

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

        assertBiFunction(test, RuntimeException.class);
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

        assertToIntBiFunction(test, RuntimeException.class);
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

        assertToLongBiFunction(test, RuntimeException.class);
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

        assertToDoubleBiFunction(test, RuntimeException.class);
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
