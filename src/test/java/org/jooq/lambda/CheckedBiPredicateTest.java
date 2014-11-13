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

import java.util.function.BiPredicate;

import static org.junit.Assert.*;

/**
 * @author Lukas Eder
 */
public class CheckedBiPredicateTest {

    @Test
    public void testCheckedBiPredicate() {
        BiPredicate<Object, Object> test = Unchecked.biPredicate(
            (t, u) -> {
                throw new Exception(t + ":" + u);
            }
        );

        assertBiPredicate(test, UncheckedException.class);
    }

    @Test
    public void testCheckedBiPredicateWithCustomHandler() {
        BiPredicate<Object, Object> test = Unchecked.biPredicate(
            (t, u) -> {
                throw new Exception(t + ":" + u);
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertBiPredicate(test, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertBiPredicate(BiPredicate<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.test(null, null);
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
