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

import org.jooq.lambda.function.Consumer5;

import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.Assert.assertEquals;

import org.jooq.lambda.function.Function5;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;

import org.junit.Test;

public class PartialApplicationTest {

    @Test
    public void testFunction5to3() {
        Tuple2<Integer, Integer> t1 = tuple(4, 4);
        Tuple3<Integer, Integer, Integer> t2 = tuple(5, 3, 2);

        // Concat the two and three tuples and apply them together.
        int normal1 = lift(this::fiveArgMethod).apply(t1.concat(t2));

        // Apply partially the first two values, then apply the remaining three
        int partiallyAppliedExplicitExplicit = lift(this::fiveArgMethod).applyPartially(t1.v1, t1.v2).apply(t2.v1, t2.v2, t2.v3);
        int partiallyAppliedExplicitTuple = lift(this::fiveArgMethod).applyPartially(t1.v1, t1.v2).apply(t2);
        int partiallyAppliedTupleExplicit = lift(this::fiveArgMethod).applyPartially(t1).apply(t2.v1, t2.v2, t2.v3);
        int partiallyAppliedTupleTuple = lift(this::fiveArgMethod).applyPartially(t1).apply(t2);

        assertEquals(normal1, partiallyAppliedExplicitExplicit);
        assertEquals(normal1, partiallyAppliedExplicitTuple);
        assertEquals(normal1, partiallyAppliedTupleExplicit);
        assertEquals(normal1, partiallyAppliedTupleTuple);
    }

    private <A, B, C, D, E, F> Function5<A, B, C, D, E, F> lift(Function5<A, B, C, D, E, F> func) {
        return func;
    }

    private int fiveArgMethod(int a, int b, int c, int d, int e) {
        return a + b * c / d - e;
    }

    int result;
    
    private <A, B, C, D, E> Consumer5<A, B, C, D, E> lift(Consumer5<A, B, C, D, E> func) {
        return func;
    }

    private void fiveArgConsumer(int a, int b, int c, int d, int e) {
        result = a + b * c / d - e;
    }
    
    @Test
    public void testConsumer5to3() {
        Tuple2<Integer, Integer> t1 = tuple(4, 4);
        Tuple3<Integer, Integer, Integer> t2 = tuple(5, 3, 2);

        // Concat the two and three tuples and apply them together.
        lift(this::fiveArgConsumer).accept(t1.concat(t2));
        int normal1 = result;

        // Accept partially the first two values, then accept the remaining three
        lift(this::fiveArgConsumer).acceptPartially(t1.v1, t1.v2).accept(t2.v1, t2.v2, t2.v3);
        int partiallyAppliedExplicitExplicit = result;
        lift(this::fiveArgConsumer).acceptPartially(t1.v1, t1.v2).accept(t2);
        int partiallyAppliedExplicitTuple = result;
        lift(this::fiveArgConsumer).acceptPartially(t1).accept(t2.v1, t2.v2, t2.v3);
        int partiallyAppliedTupleExplicit = result;
        lift(this::fiveArgConsumer).acceptPartially(t1).accept(t2);
        int partiallyAppliedTupleTuple = result;

        assertEquals(normal1, partiallyAppliedExplicitExplicit);
        assertEquals(normal1, partiallyAppliedExplicitTuple);
        assertEquals(normal1, partiallyAppliedTupleExplicit);
        assertEquals(normal1, partiallyAppliedTupleTuple);
    }

}
