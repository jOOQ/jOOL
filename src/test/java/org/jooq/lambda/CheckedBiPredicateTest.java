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
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import org.jooq.lambda.fi.util.function.CheckedBiPredicate;
import org.jooq.lambda.unchecked.UncheckedBiPredicate;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class CheckedBiPredicateTest {

    @Test
    public void testCheckedBiPredicate() {

        final CheckedBiPredicate<Object, Object> biPredicate = (t, u) -> {
            throw new Exception(t + ":" + u);
        };

        BiPredicate<Object, Object> test = Unchecked.biPredicate(biPredicate);
        BiPredicate<Object, Object> alias = UncheckedBiPredicate.unchecked(biPredicate);

        assertBiPredicate(test, UncheckedException.class);
        assertBiPredicate(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedBiPredicateWithCustomHandler() {

        final CheckedBiPredicate<Object, Object> biPredicate = (t, u) -> {
            throw new Exception(t + ":" + u);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        BiPredicate<Object, Object> test = Unchecked.biPredicate(biPredicate, handler);
        BiPredicate<Object, Object> alias = UncheckedBiPredicate.unchecked(biPredicate, handler);

        assertBiPredicate(test, IllegalStateException.class);
        assertBiPredicate(alias, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertBiPredicate(BiPredicate<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.test(null, null);
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
