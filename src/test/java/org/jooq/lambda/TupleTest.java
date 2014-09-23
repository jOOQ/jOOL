/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.lambda;

import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.jooq.lambda.tuple.Tuple.range;
import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Lukas Eder
 */
public class TupleTest {

    @Test
    public void testEqualsHashCode() {
        Set<Tuple2<Integer, String>> set = new HashSet<>();

        set.add(tuple(1, "abc"));
        assertEquals(1, set.size());
        set.add(tuple(1, "abc"));
        assertEquals(1, set.size());
        set.add(tuple(null, null));
        assertEquals(2, set.size());
        set.add(tuple(null, null));
        assertEquals(2, set.size());
        set.add(tuple(1, null));
        assertEquals(3, set.size());
        set.add(tuple(1, null));
        assertEquals(3, set.size());
    }

    @Test
    public void testEqualsNull() {
        assertFalse(tuple(1).equals(null));
        assertFalse(tuple(1, 2).equals(null));
        assertFalse(tuple(1, 2, 3).equals(null));
    }

    @Test
    public void testToString() {
        assertEquals("(1, abc)", tuple(1, "abc").toString());
    }

    @Test
    public void testArrayAndList() {
        assertEquals(asList(1, "a", null), asList(tuple(1, "a", null).array()));
        assertEquals(asList(1, "a", null), tuple(1, "a", null).list());
    }

    @Test
    public void testSwap() {
        assertEquals(tuple(1, "a"), tuple("a", 1).swap());
        assertEquals(tuple(1, "a"), tuple(1, "a").swap().swap());
    }

    @Test
    public void testCompareTo() {
        Set<Tuple2<Integer, String>> set = new TreeSet<>();

        set.add(tuple(2, "a"));
        set.add(tuple(1, "b"));
        set.add(tuple(1, "a"));
        set.add(tuple(2, "a"));

        assertEquals(3, set.size());
        assertEquals(asList(tuple(1, "a"), tuple(1, "b"), tuple(2, "a")), new ArrayList<>(set));
    }

    @Test
    public void testCompareToWithNulls() {
        Set<Tuple2<Integer, String>> set = new TreeSet<>();

        set.add(tuple(2, "a"));
        set.add(tuple(1, "b"));
        set.add(tuple(1, null));
        set.add(tuple(null, "a"));
        set.add(tuple(null, "b"));
        set.add(tuple(null, null));

        assertEquals(6, set.size());
        assertEquals(asList(tuple(1, "b"), tuple(1, null), tuple(2, "a"), tuple(null, "a"), tuple(null, "b"), tuple(null, null)), new ArrayList<>(set));
    }

    @Test
    public void testIterable() {
        LinkedList<Object> list = new LinkedList<>(tuple(1, "b", null).list());
        for (Object o : tuple(1, "b", null)) {
            assertEquals(list.poll(), o);
        }
    }

    @Test
    public void testFunctions() {
        assertEquals("(1, b, null)", tuple(1, "b", null).map((v1, v2, v3) -> tuple(v1, v2, v3).toString()));
        assertEquals("1-b", tuple(1, "b", null).map((v1, v2, v3) -> v1 + "-" + v2));
    }

    @Test
    public void testMapN() {
        assertEquals(tuple(1, "a", 2, "b"), tuple(1, null, 2, null).map2(v -> "a").map4(v -> "b"));
    }

    @Test
    public void testOverlaps() {
        assertTrue(Tuple2.overlaps(tuple(1, 3), tuple(1, 3)));
        assertTrue(Tuple2.overlaps(tuple(1, 3), tuple(2, 3)));
        assertTrue(Tuple2.overlaps(tuple(1, 3), tuple(2, 4)));
        assertTrue(Tuple2.overlaps(tuple(1, 3), tuple(3, 4)));
        assertFalse(Tuple2.overlaps(tuple(1, 3), tuple(4, 5)));
        assertFalse(Tuple2.overlaps(tuple(1, 1), tuple(2, 2)));


        assertTrue(range(1, 3).overlaps(tuple(1, 3)));
        assertTrue(range(1, 3).overlaps(tuple(2, 3)));
        assertTrue(range(1, 3).overlaps(tuple(2, 4)));
        assertTrue(range(1, 3).overlaps(tuple(3, 4)));
        assertFalse(range(1, 3).overlaps(tuple(4, 5)));
        assertFalse(range(1, 1).overlaps(2, 2));
    }

    @Test
    public void testIntersect() {
        assertEquals(Optional.of(tuple(2, 3)), range(1, 3).intersect(range(2, 4)));
        assertEquals(Optional.of(tuple(2, 3)), range(3, 1).intersect(range(4, 2)));
        assertEquals(Optional.of(tuple(3, 3)), range(1, 3).intersect(3, 5));
        assertEquals(Optional.empty(), range(1, 3).intersect(range(4, 5)));
    }

    @Test
    public void testRange() {
        assertEquals(range(1, 3), range(3, 1));
    }
}
