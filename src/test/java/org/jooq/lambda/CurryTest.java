/**
 * Copyright (c) 2014-2015, Data Geekery GmbH, contact@datageekery.com
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

import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.Assert.assertEquals;

import org.jooq.lambda.function.Function5;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;

import org.junit.Test;

public class CurryTest {

    @Test
    public void testFunction5to3() {
        Tuple2<Integer, Integer> first = tuple(4, 4);
        Tuple3<Integer, Integer, Integer> second = tuple(5, 3, 2);

        // Curry with the first two values, then apply with the remaining three
        int curriedResult = lift(this::fiveArgMethod).curry(first.v1, first.v2).apply(second);

        // Concat the two and three tuples and apply them together.
        int normalResult = lift(this::fiveArgMethod).apply(first.concat(second));

        assertEquals(curriedResult, normalResult);
    }

    private <A, B, C, D, E, F> Function5<A, B, C, D, E, F> lift(Function5<A, B, C, D, E, F> func) {
        return func;
    }

    private int fiveArgMethod(int a, int b, int c, int d, int e) {
        return a + b * c / d - e;
    }
}
