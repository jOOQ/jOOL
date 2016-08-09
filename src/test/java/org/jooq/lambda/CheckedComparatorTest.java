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
import java.util.Comparator;
import java.util.function.Consumer;
import org.jooq.lambda.fi.util.CheckedComparator;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class CheckedComparatorTest {

    @Test
    public void testCheckedComparator() {

        final CheckedComparator<Object> comparator = (t1, t2) -> {
            throw new Exception(t1 + ":" + t2);
        };

        Comparator<Object> test = Unchecked.comparator(comparator);
        Comparator<Object> alias = CheckedComparator.unchecked(comparator);

        assertComparator(test, UncheckedException.class);
        assertComparator(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedComparatorWithCustomHandler() {

        final CheckedComparator<Object> comparator = (t1, t2) -> {
            throw new Exception(t1 + ":" + t2);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        Comparator<Object> test = Unchecked.comparator(comparator, handler);
        Comparator<Object> alias = CheckedComparator.unchecked(comparator, handler);

        assertComparator(test, IllegalStateException.class);
        assertComparator(alias, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertComparator(Comparator<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.compare(null, null);
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
