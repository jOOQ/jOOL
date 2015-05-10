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

import static org.jooq.lambda.JOOL.lift;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jooq.lambda.function.Function1;
import org.jooq.lambda.function.Function5;

import org.junit.Test;

public class JOOLTest {

    @Test
    public void testLifters(){
        assertEquals(fiveArgMethod(1, 2, 3, 4, 5), calculate5(lift(this::fiveArgMethod)));

        assertEquals(oneArgMethod(2), caclulate1(lift(this::oneArgMethod)));

        assertFalse(testPredicate(lift(this::isNull)));

        //The following line compiles and works exactly like the above, BUT it breaks Eclipse. (Luna Release 2 - 4.4.2)
        //assertFalse(testPredicate(lift(JOOLTest::isStaticNull)));
    }

    private int fiveArgMethod(int a, int b, int c, int d, int e) {
        return a + b * c / d - e;
    }

    private int calculate5(Function5<Integer, Integer,Integer,Integer, Integer, Integer> func){
        return func.apply(1, 2, 3, 4, 5);
    }

    private int oneArgMethod(int a) {
        return a * 2;
    }

    private int caclulate1(Function1<Integer, Integer> func){
        return func.apply(2);
    }

    private boolean isNull(Object obj){
        return obj == null;
    }

    @SuppressWarnings("unused")
    private static boolean isStaticNull(Object obj){
        return obj == null;
    }

    private boolean testPredicate(Function1<Object, Boolean> func){
        return func.apply("");
    }
}
