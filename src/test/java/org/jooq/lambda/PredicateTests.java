/**
 * Copyright (c), Data Geekery GmbH, contact@datageekery.com
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Predicate;

import org.jooq.lambda.function.Functions;

import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class PredicateTests {

    @Test
    public void testPredicates() {
        Predicate<Integer> even = i -> i % 2 == 0;
        Predicate<Integer> threes = i -> i % 3 == 0;

        assertTrue(even.test(0));
        assertFalse(even.test(1));

        assertFalse(Functions.not(even).test(0));
        assertTrue(Functions.not(even).test(1));

        assertTrue(Functions.and(even, threes).test(0));
        assertFalse(Functions.and(even, threes).test(1));
        assertFalse(Functions.and(even, threes).test(2));
        assertFalse(Functions.and(even, threes).test(3));
        assertFalse(Functions.and(even, threes).test(4));
        assertFalse(Functions.and(even, threes).test(5));
        assertTrue(Functions.and(even, threes).test(6));

        assertTrue(Functions.or(even, threes).test(0));
        assertFalse(Functions.or(even, threes).test(1));
        assertTrue(Functions.or(even, threes).test(2));
        assertTrue(Functions.or(even, threes).test(3));
        assertTrue(Functions.or(even, threes).test(4));
        assertFalse(Functions.or(even, threes).test(5));
        assertTrue(Functions.or(even, threes).test(6));
    }
}
