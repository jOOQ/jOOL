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

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.jooq.lambda.Utils.assertThrows;
import static org.jooq.lambda.tuple.Tuple.collectors;
import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.jooq.lambda.tuple.Tuple4;
import org.jooq.lambda.tuple.Tuple5;
import org.jooq.lambda.tuple.Tuple6;
import org.jooq.lambda.tuple.Tuple7;
import org.jooq.lambda.tuple.Tuple8;

import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class SeqTest {

    @Test
    public void testZipEqualLength() {
        List<Tuple2<Integer, String>> list = Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c")).toList();

        assertEquals(3, list.size());
        assertEquals(1, (int) list.get(0).v1);
        assertEquals(2, (int) list.get(1).v1);
        assertEquals(3, (int) list.get(2).v1);
        assertEquals("a", list.get(0).v2);
        assertEquals("b", list.get(1).v2);
        assertEquals("c", list.get(2).v2);
    }

    @Test
    public void testZipDifferingLength() {
        List<Tuple2<Integer, String>> list = Seq.of(1, 2).zip(Seq.of("a", "b", "c", "d")).toList();

        assertEquals(2, list.size());
        assertEquals(1, (int) list.get(0).v1);
        assertEquals(2, (int) list.get(1).v1);
        assertEquals("a", list.get(0).v2);
        assertEquals("b", list.get(1).v2);
    }

    @Test
    public void testZipStaticEqualLength() {
       List<Tuple2<Integer, Long>> l1 =  Seq.zip(Seq.of(1, 2), Seq.of(11L, 12L)).toList();

       assertEquals(2, l1.size());
       assertEquals(1, (int) l1.get(0).v1);
       assertEquals(2, (int) l1.get(1).v1);
       assertEquals(11L, (long) l1.get(0).v2);
       assertEquals(12L, (long) l1.get(1).v2);


       List<Tuple3<Integer, Long, String>> l2 =  Seq.zip(Seq.of(1, 2), Seq.of(11L, 12L), Seq.of("a", "b")).toList();

       assertEquals(2, l2.size());
       assertEquals(1, (int) l2.get(0).v1);
       assertEquals(2, (int) l2.get(1).v1);
       assertEquals(11L, (long) l2.get(0).v2);
       assertEquals(12L, (long) l2.get(1).v2);
       assertEquals("a", l2.get(0).v3);
       assertEquals("b", l2.get(1).v3);


       List<Tuple4<Integer, Long, String, Integer>> l3 =  Seq.zip(
           Seq.of(1, 2),
           Seq.of(11L, 12L),
           Seq.of("a", "b"),
           Seq.of(1, 2)
       ).toList();

       assertEquals(2, l3.size());
       assertEquals(1, (int) l3.get(0).v1);
       assertEquals(2, (int) l3.get(1).v1);
       assertEquals(11L, (long) l3.get(0).v2);
       assertEquals(12L, (long) l3.get(1).v2);
       assertEquals("a", l3.get(0).v3);
       assertEquals("b", l3.get(1).v3);
       assertEquals(1, (int) l3.get(0).v4);
       assertEquals(2, (int) l3.get(1).v4);


       List<Tuple5<Integer, Long, String, Integer, Long>> l5 =  Seq.zip(
           Seq.of(1, 2),
           Seq.of(11L, 12L),
           Seq.of("a", "b"),
           Seq.of(1, 2),
           Seq.of(11L, 12L)
       ).toList();

       assertEquals(2, l5.size());
       assertEquals(1, (int) l5.get(0).v1);
       assertEquals(2, (int) l5.get(1).v1);
       assertEquals(11L, (long) l5.get(0).v2);
       assertEquals(12L, (long) l5.get(1).v2);
       assertEquals("a", l5.get(0).v3);
       assertEquals("b", l5.get(1).v3);
       assertEquals(1, (int) l5.get(0).v4);
       assertEquals(2, (int) l5.get(1).v4);
       assertEquals(11L, (long) l5.get(0).v5);
       assertEquals(12L, (long) l5.get(1).v5);


       List<Tuple6<Integer, Long, String, Integer, Long, String>> l6 =  Seq.zip(
           Seq.of(1, 2),
           Seq.of(11L, 12L),
           Seq.of("a", "b"),
           Seq.of(1, 2),
           Seq.of(11L, 12L),
           Seq.of("a", "b")
       ).toList();

       assertEquals(2, l6.size());
       assertEquals(1, (int) l6.get(0).v1);
       assertEquals(2, (int) l6.get(1).v1);
       assertEquals(11L, (long) l6.get(0).v2);
       assertEquals(12L, (long) l6.get(1).v2);
       assertEquals("a", l6.get(0).v3);
       assertEquals("b", l6.get(1).v3);
       assertEquals(1, (int) l6.get(0).v4);
       assertEquals(2, (int) l6.get(1).v4);
       assertEquals(11L, (long) l6.get(0).v5);
       assertEquals(12L, (long) l6.get(1).v5);
       assertEquals("a", l6.get(0).v6);
       assertEquals("b", l6.get(1).v6);


       List<Tuple7<Integer, Long, String, Integer, Long, String, Integer>> l7 =  Seq.zip(
           Seq.of(1, 2),
           Seq.of(11L, 12L),
           Seq.of("a", "b"),
           Seq.of(1, 2),
           Seq.of(11L, 12L),
           Seq.of("a", "b"),
           Seq.of(1, 2)
       ).toList();

       assertEquals(2, l7.size());
       assertEquals(1, (int) l7.get(0).v1);
       assertEquals(2, (int) l7.get(1).v1);
       assertEquals(11L, (long) l7.get(0).v2);
       assertEquals(12L, (long) l7.get(1).v2);
       assertEquals("a", l7.get(0).v3);
       assertEquals("b", l7.get(1).v3);
       assertEquals(1, (int) l7.get(0).v4);
       assertEquals(2, (int) l7.get(1).v4);
       assertEquals(11L, (long) l7.get(0).v5);
       assertEquals(12L, (long) l7.get(1).v5);
       assertEquals("a", l7.get(0).v6);
       assertEquals("b", l7.get(1).v6);
       assertEquals(1, (int) l7.get(0).v7);
       assertEquals(2, (int) l7.get(1).v7);


       List<Tuple8<Integer, Long, String, Integer, Long, String, Integer, Long>> l8 =  Seq.zip(
           Seq.of(1, 2),
           Seq.of(11L, 12L),
           Seq.of("a", "b"),
           Seq.of(1, 2),
           Seq.of(11L, 12L),
           Seq.of("a", "b"),
           Seq.of(1, 2),
           Seq.of(11L, 12L)
       ).toList();

       assertEquals(2, l8.size());
       assertEquals(1, (int) l8.get(0).v1);
       assertEquals(2, (int) l8.get(1).v1);
       assertEquals(11L, (long) l8.get(0).v2);
       assertEquals(12L, (long) l8.get(1).v2);
       assertEquals("a", l8.get(0).v3);
       assertEquals("b", l8.get(1).v3);
       assertEquals(1, (int) l8.get(0).v4);
       assertEquals(2, (int) l8.get(1).v4);
       assertEquals(11L, (long) l8.get(0).v5);
       assertEquals(12L, (long) l8.get(1).v5);
       assertEquals("a", l8.get(0).v6);
       assertEquals("b", l8.get(1).v6);
       assertEquals(1, (int) l8.get(0).v7);
       assertEquals(2, (int) l8.get(1).v7);
       assertEquals(11L, (long) l8.get(0).v8);
       assertEquals(12L, (long) l8.get(1).v8);
    }

    @Test
    public void testZipWithIndex() {
        assertEquals(asList(), Seq.of().zipWithIndex().toList());
        assertEquals(asList(tuple("a", 0L)), Seq.of("a").zipWithIndex().toList());
        assertEquals(asList(tuple("a", 0L), tuple("b", 1L)), Seq.of("a", "b").zipWithIndex().toList());
        assertEquals(asList(tuple("a", 0L), tuple("b", 1L), tuple("c", 2L)), Seq.of("a", "b", "c").zipWithIndex().toList());
    }

    @Test
    public void testDuplicate() {
        Supplier<Tuple2<Seq<Integer>, Seq<Integer>>> reset = () -> Seq.of(1, 2, 3).duplicate();
        Tuple2<Seq<Integer>, Seq<Integer>> duplicate;

        duplicate = reset.get();
        assertEquals(asList(1, 2, 3), duplicate.v1.toList());
        assertEquals(asList(1, 2, 3), duplicate.v2.toList());

        duplicate = reset.get();
        assertEquals(asList(1, 2, 3, 1, 2, 3), duplicate.v1.concat(duplicate.v2).toList());

        duplicate = reset.get();
        assertEquals(asList(tuple(1, 1), tuple(2, 2), tuple(3, 3)), duplicate.v1.zip(duplicate.v2).toList());
    }

    @Test
    public void testDuplicateWithFilter() {
        Supplier<Tuple2<Seq<Integer>, Seq<Integer>>> reset = () -> Seq.of(1, 2, 3, 4, 5).duplicate();
        Tuple2<Seq<Integer>, Seq<Integer>> duplicate;

        // Filter each seq individually
        duplicate = reset.get().map((s1, s2) -> tuple(s1.filter(i -> i % 2 == 0), s2.filter(i -> i % 2 != 0)));
        assertEquals(asList(2, 4), duplicate.v1.toList());
        assertEquals(asList(1, 3, 5), duplicate.v2.toList());

        duplicate = reset.get()
            .map1(s1 -> s1.filter(i -> i % 2 == 0))
            .map2(s2 -> s2.filter(i -> i % 2 != 0));
        assertEquals(asList(2, 4), duplicate.v1.toList());
        assertEquals(asList(1, 3, 5), duplicate.v2.toList());
    }

    @Test
    public void testDuplicateWithLimit() {
        Supplier<Tuple2<Seq<Integer>, Seq<Integer>>> reset = () -> Seq.of(1, 2, 3, 4, 5).duplicate();
        Tuple2<Seq<Integer>, Seq<Integer>> duplicate;

        // Consume v1 first
        duplicate = reset.get().map((s1, s2) -> tuple(s1.limit(2), s2.skip(2)));
        assertEquals(asList(1, 2), duplicate.v1.toList());
        assertEquals(asList(3, 4, 5), duplicate.v2.toList());

        // Consume v2 first
        duplicate = reset.get().map((s1, s2) -> tuple(s1.limit(2), s2.skip(2)));
        assertEquals(asList(3, 4, 5), duplicate.v2.toList());
        assertEquals(asList(1, 2), duplicate.v1.toList());
    }

    @Test
    public void testCrossJoin() {

        // {A} x {B}
        // ---------------------------------------------------------------------
        assertEquals(asList(),
            Seq.of().crossJoin(Seq.of()).toList());
        assertEquals(asList(),
            Seq.of().crossJoin(Seq.of(1)).toList());
        assertEquals(asList(),
            Seq.of().crossJoin(Seq.of(1, 2)).toList());

        assertEquals(asList(),
            Seq.of("A").crossJoin(Seq.of()).toList());
        assertEquals(asList(
            tuple("A", 1)),
            Seq.of("A").crossJoin(Seq.of(1)).toList());
        assertEquals(asList(
            tuple("A", 1),
            tuple("A", 2)),
            Seq.of("A").crossJoin(Seq.of(1, 2)).toList());

        assertEquals(asList(),
            Seq.of("A", "B").crossJoin(Seq.of()).toList());
        assertEquals(asList(
            tuple("A", 1),
            tuple("B", 1)),
            Seq.of("A", "B").crossJoin(Seq.of(1)).toList());
        assertEquals(asList(
            tuple("A", 1),
            tuple("A", 2),
            tuple("B", 1),
            tuple("B", 2)),
            Seq.of("A", "B").crossJoin(Seq.of(1, 2)).toList());

        assertEquals(asList(),
            Seq.of("A", "B", "C").crossJoin(Seq.of()).toList());
        assertEquals(asList(
            tuple("A", 1),
            tuple("B", 1),
            tuple("C", 1)),
            Seq.of("A", "B", "C").crossJoin(Seq.of(1)).toList());
        assertEquals(asList(
            tuple("A", 1),
            tuple("A", 2),
            tuple("B", 1),
            tuple("B", 2),
            tuple("C", 1),
            tuple("C", 2)),
            Seq.of("A", "B", "C").crossJoin(Seq.of(1, 2)).toList());


        // {A} x {B} x {C}
        // ---------------------------------------------------------------------
        assertEquals(asList(),
            Seq.crossJoin(Seq.of(), Seq.of(), Seq.of()).toList());
        assertEquals(asList(),
            Seq.crossJoin(Seq.of(), Seq.of(), Seq.of(1)).toList());
        assertEquals(asList(),
            Seq.crossJoin(Seq.of(), Seq.of(), Seq.of(1, 2)).toList());

        assertEquals(asList(),
            Seq.crossJoin(Seq.of(), Seq.of("A"), Seq.of()).toList());
        assertEquals(asList(),
            Seq.crossJoin(Seq.of(), Seq.of("A"), Seq.of(1)).toList());
        assertEquals(asList(),
            Seq.crossJoin(Seq.of(), Seq.of("A"), Seq.of(1, 2)).toList());

        assertEquals(asList(),
            Seq.crossJoin(Seq.of(), Seq.of("A", "B"), Seq.of()).toList());
        assertEquals(asList(),
            Seq.crossJoin(Seq.of(), Seq.of("A", "B"), Seq.of(1)).toList());
        assertEquals(asList(),
            Seq.crossJoin(Seq.of(), Seq.of("A", "B"), Seq.of(1, 2)).toList());



        assertEquals(asList(),
            Seq.crossJoin(Seq.of("X"), Seq.of(), Seq.of()).toList());
        assertEquals(asList(),
            Seq.crossJoin(Seq.of("X"), Seq.of(), Seq.of(1)).toList());
        assertEquals(asList(),
            Seq.crossJoin(Seq.of("X"), Seq.of(), Seq.of(1, 2)).toList());

        assertEquals(asList(),
            Seq.crossJoin(Seq.of("X"), Seq.of("A"), Seq.of()).toList());
        assertEquals(asList(
            tuple("X", "A", 1)),
            Seq.crossJoin(Seq.of("X"), Seq.of("A"), Seq.of(1)).toList());
        assertEquals(asList(
            tuple("X", "A", 1),
            tuple("X", "A", 2)),
            Seq.crossJoin(Seq.of("X"), Seq.of("A"), Seq.of(1, 2)).toList());

        assertEquals(asList(),
            Seq.crossJoin(Seq.of("X"), Seq.of("A", "B"), Seq.of()).toList());
        assertEquals(asList(
            tuple("X", "A", 1),
            tuple("X", "B", 1)),
            Seq.crossJoin(Seq.of("X"), Seq.of("A", "B"), Seq.of(1)).toList());
        assertEquals(asList(
            tuple("X", "A", 1),
            tuple("X", "A", 2),
            tuple("X", "B", 1),
            tuple("X", "B", 2)),
            Seq.crossJoin(Seq.of("X"), Seq.of("A", "B"), Seq.of(1, 2)).toList());



        assertEquals(asList(),
            Seq.crossJoin(Seq.of("X", "Y"), Seq.of(), Seq.of()).toList());
        assertEquals(asList(),
            Seq.crossJoin(Seq.of("X", "Y"), Seq.of(), Seq.of(1)).toList());
        assertEquals(asList(),
            Seq.crossJoin(Seq.of("X", "Y"), Seq.of(), Seq.of(1, 2)).toList());

        assertEquals(asList(),
            Seq.crossJoin(Seq.of("X", "Y"), Seq.of("A"), Seq.of()).toList());
        assertEquals(asList(
            tuple("X", "A", 1),
            tuple("Y", "A", 1)),
            Seq.crossJoin(Seq.of("X", "Y"), Seq.of("A"), Seq.of(1)).toList());
        assertEquals(asList(
            tuple("X", "A", 1),
            tuple("X", "A", 2),
            tuple("Y", "A", 1),
            tuple("Y", "A", 2)),
            Seq.crossJoin(Seq.of("X", "Y"), Seq.of("A"), Seq.of(1, 2)).toList());

        assertEquals(asList(),
            Seq.crossJoin(Seq.of("X", "Y"), Seq.of("A", "B"), Seq.of()).toList());
        assertEquals(asList(
            tuple("X", "A", 1),
            tuple("X", "B", 1),
            tuple("Y", "A", 1),
            tuple("Y", "B", 1)),
            Seq.crossJoin(Seq.of("X", "Y"), Seq.of("A", "B"), Seq.of(1)).toList());
        assertEquals(asList(
            tuple("X", "A", 1),
            tuple("X", "A", 2),
            tuple("X", "B", 1),
            tuple("X", "B", 2),
            tuple("Y", "A", 1),
            tuple("Y", "A", 2),
            tuple("Y", "B", 1),
            tuple("Y", "B", 2)),
            Seq.crossJoin(Seq.of("X", "Y"), Seq.of("A", "B"), Seq.of(1, 2)).toList());



        // {A} x {B} x {C} x {D}
        // ---------------------------------------------------------------------
        assertEquals(asList(
            tuple("(", "X", "A", 1),
            tuple("(", "X", "A", 2),
            tuple("(", "X", "B", 1),
            tuple("(", "X", "B", 2),
            tuple("(", "Y", "A", 1),
            tuple("(", "Y", "A", 2),
            tuple("(", "Y", "B", 1),
            tuple("(", "Y", "B", 2),
            tuple(")", "X", "A", 1),
            tuple(")", "X", "A", 2),
            tuple(")", "X", "B", 1),
            tuple(")", "X", "B", 2),
            tuple(")", "Y", "A", 1),
            tuple(")", "Y", "A", 2),
            tuple(")", "Y", "B", 1),
            tuple(")", "Y", "B", 2)),
            Seq.crossJoin(
                Seq.of("(", ")"),
                Seq.of("X", "Y"),
                Seq.of("A", "B"),
                Seq.of(1, 2)
            ).toList());
    }

    @Test
    public void testInnerJoin() {
        BiPredicate<Object, Object> TRUE = (t, u) -> true;

        assertEquals(asList(),
            Seq.of().innerJoin(Seq.of(), TRUE).toList());
        assertEquals(asList(),
            Seq.of().innerJoin(Seq.of(1), TRUE).toList());
        assertEquals(asList(),
            Seq.of().innerJoin(Seq.of(1, 2), TRUE).toList());

        assertEquals(asList(),
            Seq.<Object>of(1).innerJoin(Seq.of(), TRUE).toList());
        assertEquals(asList(),
            Seq.of(1).innerJoin(Seq.of(2), (t, u) -> t == u).toList());
        assertEquals(asList(
            tuple(1, 2)),
            Seq.of(1).innerJoin(Seq.of(2), (t, u) -> t * 2 == u).toList());
        assertEquals(asList(
            tuple(1, 1)),
            Seq.of(1).innerJoin(Seq.of(1, 2), (t, u) -> t == u).toList());
        assertEquals(asList(
            tuple(1, 2)),
            Seq.of(1).innerJoin(Seq.of(1, 2), (t, u) -> t * 2 == u).toList());
        assertEquals(asList(
            tuple(1, 1),
            tuple(1, 2)),
            Seq.<Object>of(1).innerJoin(Seq.of(1, 2), TRUE).toList());
    }

    @Test
    public void testLeftOuterJoin() {
        BiPredicate<Object, Object> TRUE = (t, u) -> true;

        assertEquals(asList(),
            Seq.of().leftOuterJoin(Seq.of(), TRUE).toList());
        assertEquals(asList(),
            Seq.of().leftOuterJoin(Seq.of(1), TRUE).toList());
        assertEquals(asList(),
            Seq.of().leftOuterJoin(Seq.of(1, 2), TRUE).toList());

        assertEquals(asList(
            tuple(1, null)),
            Seq.<Object>of(1).leftOuterJoin(Seq.of(), TRUE).toList());
        assertEquals(asList(
            tuple(1, null)),
            Seq.of(1).leftOuterJoin(Seq.of(2), (t, u) -> t == u).toList());
        assertEquals(asList(
            tuple(1, 2)),
            Seq.of(1).leftOuterJoin(Seq.of(2), (t, u) -> t * 2 == u).toList());
        assertEquals(asList(
            tuple(1, 1)),
            Seq.of(1).leftOuterJoin(Seq.of(1, 2), (t, u) -> t == u).toList());
        assertEquals(asList(
            tuple(1, 2)),
            Seq.of(1).leftOuterJoin(Seq.of(1, 2), (t, u) -> t * 2 == u).toList());
        assertEquals(asList(
            tuple(1, 1),
            tuple(1, 2)),
            Seq.<Object>of(1).leftOuterJoin(Seq.of(1, 2), TRUE).toList());
    }

    @Test
    public void testRightOuterJoin() {
        BiPredicate<Object, Object> TRUE = (t, u) -> true;

        assertEquals(asList(),
            Seq.of().rightOuterJoin(Seq.of(), TRUE).toList());
        assertEquals(asList(
            tuple(null, 1)),
            Seq.of().rightOuterJoin(Seq.of(1), TRUE).toList());
        assertEquals(asList(
            tuple(null, 1),
            tuple(null, 2)),
            Seq.of().rightOuterJoin(Seq.of(1, 2), TRUE).toList());

        assertEquals(asList(),
            Seq.<Object>of(1).rightOuterJoin(Seq.of(), TRUE).toList());
        assertEquals(asList(
            tuple(null, 2)),
            Seq.of(1).rightOuterJoin(Seq.of(2), (t, u) -> t == u).toList());
        assertEquals(asList(
            tuple(1, 2)),
            Seq.of(1).rightOuterJoin(Seq.of(2), (t, u) -> t * 2 == u).toList());
        assertEquals(asList(
            tuple(1, 1),
            tuple(null, 2)),
            Seq.of(1).rightOuterJoin(Seq.of(1, 2), (t, u) -> t == u).toList());
        assertEquals(asList(
            tuple(null, 1),
            tuple(1, 2)),
            Seq.of(1).rightOuterJoin(Seq.of(1, 2), (t, u) -> t * 2 == u).toList());
        assertEquals(asList(
            tuple(1, 1),
            tuple(1, 2)),
            Seq.<Object>of(1).rightOuterJoin(Seq.of(1, 2), TRUE).toList());
    }

    @Test
    public void testOnEmpty() throws X {
        assertEquals(asList(1), Seq.of().onEmpty(1).toList());
        assertEquals(asList(1), Seq.of().onEmptyGet(() -> 1).toList());
        assertThrows(X.class, () -> Seq.of().onEmptyThrow(() -> new X()).toList());

        assertEquals(asList(2), Seq.of(2).onEmpty(1).toList());
        assertEquals(asList(2), Seq.of(2).onEmptyGet(() -> 1).toList());
        assertEquals(asList(2), Seq.of(2).onEmptyThrow(() -> new X()).toList());

        assertEquals(asList(2, 3), Seq.of(2, 3).onEmpty(1).toList());
        assertEquals(asList(2, 3), Seq.of(2, 3).onEmptyGet(() -> 1).toList());
        assertEquals(asList(2, 3), Seq.of(2, 3).onEmptyThrow(() -> new X()).toList());
    }

    @SuppressWarnings("serial")
    class X extends Exception {}

    @Test
    public void testConcat() {
        assertEquals(asList(1, 2, 3, 4), Seq.of(1).concat(Seq.of(2, 3, 4)).toList());
        assertEquals(asList(1, 2, 3, 4), Seq.of(1, 2).concat(Seq.of(3, 4)).toList());
        assertEquals(asList(1, 2, 3, 4), Seq.of(1).concat(2, 3, 4).toList());
        assertEquals(asList(1, 2, 3, 4), Seq.of(1, 2).concat(3, 4).toList());
    }

    @Test
    public void testIntersperse() {
        assertEquals(asList(), Seq.of().intersperse(0).toList());
        assertEquals(asList(1), Seq.of(1).intersperse(0).toList());
        assertEquals(asList(1, 0, 2), Seq.of(1, 2).intersperse(0).toList());
        assertEquals(asList(1, 0, 2, 0, 3), Seq.of(1, 2, 3).intersperse(0).toList());
    }

    @Test
    public void testToString() {
        assertEquals("123", Seq.of(1, 2, 3).toString());
        assertEquals("1, 2, 3", Seq.of(1, 2, 3).toString(", "));
        assertEquals("1, null, 3", Seq.of(1, null, 3).toString(", "));
    }

    @Test
    public void testSlice() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertEquals(asList(3, 4, 5), s.get().slice(2, 5).toList());
        assertEquals(asList(4, 5, 6), s.get().slice(3, 6).toList());
        assertEquals(asList(), s.get().slice(4, 1).toList());
        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().slice(0, 6).toList());
        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().slice(-1, 6).toList());
        assertEquals(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), s.get().slice(-1, 12).toList());
    }

    @Test
    public void testGenerate() {
        assertEquals(10, Seq.generate().limit(10).toList().size());
        assertEquals(10, Seq.generate((Object) null).limit(10).toList().size());
        assertEquals(10, Seq.generate(() -> null).limit(10).toList().size());
        assertEquals(10, Seq.generate(1).limit(10).toList().size());
        assertEquals(Collections.nCopies(10, 1), Seq.generate(1).limit(10).toList());
    }

    @Test
    public void testToCollection() {
        assertEquals(asList(1, 2, 2, 3), Seq.of(1, 2, 2, 3).toCollection(LinkedList::new));
    }

    @Test
    public void testToList() {
        assertEquals(asList(1, 2, 2, 3), Seq.of(1, 2, 2, 3).toList());
    }

    @Test
    public void testToSet() {
        assertEquals(new HashSet<>(asList(1, 2, 3)), Seq.of(1, 2, 2, 3).toSet());
    }

    @Test
    public void testToMap() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("a", 1);
        expected.put("b", 2);
        expected.put("c", 3);

        assertEquals(expected, Seq.of(tuple("a", 1), tuple("b", 2), tuple("c", 3)).toMap(Tuple2::v1, Tuple2::v2));
        assertEquals(expected, Seq.toMap(Seq.of(tuple("a", 1), tuple("b", 2), tuple("c", 3))));
    }

    @Test
    public void testSkipWhile() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(1, 2, 3, 4, 5), s.get().skipWhile(i -> false).toList());
        assertEquals(asList(3, 4, 5), s.get().skipWhile(i -> i % 3 != 0).toList());
        assertEquals(asList(3, 4, 5), s.get().skipWhile(i -> i < 3).toList());
        assertEquals(asList(4, 5), s.get().skipWhile(i -> i < 4).toList());
        assertEquals(asList(), s.get().skipWhile(i -> true).toList());
    }

    @Test
    public void testSkipUntil() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(), s.get().skipUntil(i -> false).toList());
        assertEquals(asList(3, 4, 5), s.get().skipUntil(i -> i % 3 == 0).toList());
        assertEquals(asList(3, 4, 5), s.get().skipUntil(i -> i == 3).toList());
        assertEquals(asList(4, 5), s.get().skipUntil(i -> i == 4).toList());
        assertEquals(asList(1, 2, 3, 4, 5), s.get().skipUntil(i -> true).toList());
    }

    @Test
    public void testSkipUntilWithNulls() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, null, 3, 4, 5);

        assertEquals(asList(1, 2, null, 3, 4, 5), s.get().skipUntil(i -> true).toList());
    }

    @Test
    public void testLimitWhile() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(), s.get().limitWhile(i -> false).toList());
        assertEquals(asList(1, 2), s.get().limitWhile(i -> i % 3 != 0).toList());
        assertEquals(asList(1, 2), s.get().limitWhile(i -> i < 3).toList());
        assertEquals(asList(1, 2, 3), s.get().limitWhile(i -> i < 4).toList());
        assertEquals(asList(1, 2, 3, 4, 5), s.get().limitWhile(i -> true).toList());
    }

    @Test
    public void testLimitUntil() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(1, 2, 3, 4, 5), s.get().limitUntil(i -> false).toList());
        assertEquals(asList(1, 2), s.get().limitUntil(i -> i % 3 == 0).toList());
        assertEquals(asList(1, 2), s.get().limitUntil(i -> i == 3).toList());
        assertEquals(asList(1, 2, 3), s.get().limitUntil(i -> i == 4).toList());
        assertEquals(asList(), s.get().limitUntil(i -> true).toList());
    }

    @Test
    public void testLimitUntilWithNulls() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, null, 3, 4, 5);

        assertEquals(asList(1, 2, null, 3, 4, 5), s.get().limitUntil(i -> false).toList());
    }

    @Test
    public void testPartition() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5, 6);

        assertEquals(asList(1, 3, 5), s.get().partition(i -> i % 2 != 0).v1.toList());
        assertEquals(asList(2, 4, 6), s.get().partition(i -> i % 2 != 0).v2.toList());

        assertEquals(asList(2, 4, 6), s.get().partition(i -> i % 2 == 0).v1.toList());
        assertEquals(asList(1, 3, 5), s.get().partition(i -> i % 2 == 0).v2.toList());

        assertEquals(asList(1, 2, 3), s.get().partition(i -> i <= 3).v1.toList());
        assertEquals(asList(4, 5, 6), s.get().partition(i -> i <= 3).v2.toList());

        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().partition(i -> true).v1.toList());
        assertEquals(asList(), s.get().partition(i -> true).v2.toList());

        assertEquals(asList(), s.get().partition(i -> false).v1.toList());
        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().partition(i -> false).v2.toList());
    }

    @Test
    public void testSplitAt() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5, 6);

        assertEquals(asList(), s.get().splitAt(0).v1.toList());
        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().splitAt(0).v2.toList());

        assertEquals(asList(1), s.get().splitAt(1).v1.toList());
        assertEquals(asList(2, 3, 4, 5, 6), s.get().splitAt(1).v2.toList());

        assertEquals(asList(1, 2, 3), s.get().splitAt(3).v1.toList());
        assertEquals(asList(4, 5, 6), s.get().splitAt(3).v2.toList());

        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().splitAt(6).v1.toList());
        assertEquals(asList(), s.get().splitAt(6).v2.toList());

        assertEquals(asList(1, 2, 3, 4, 5, 6), s.get().splitAt(7).v1.toList());
        assertEquals(asList(), s.get().splitAt(7).v2.toList());
    }

    @Test
    public void testSplitAtHead() {
        assertEquals(Optional.empty(), Seq.of().splitAtHead().v1);
        assertEquals(asList(), Seq.of().splitAtHead().v2.toList());

        assertEquals(Optional.of(1), Seq.of(1).splitAtHead().v1);
        assertEquals(asList(), Seq.of(1).splitAtHead().v2.toList());

        assertEquals(Optional.of(1), Seq.of(1, 2).splitAtHead().v1);
        assertEquals(asList(2), Seq.of(1, 2).splitAtHead().v2.toList());

        assertEquals(Optional.of(1), Seq.of(1, 2, 3).splitAtHead().v1);
        assertEquals(Optional.of(2), Seq.of(1, 2, 3).splitAtHead().v2.splitAtHead().v1);
        assertEquals(Optional.of(3), Seq.of(1, 2, 3).splitAtHead().v2.splitAtHead().v2.splitAtHead().v1);
        assertEquals(asList(2, 3), Seq.of(1, 2, 3).splitAtHead().v2.toList());
        assertEquals(asList(3), Seq.of(1, 2, 3).splitAtHead().v2.splitAtHead().v2.toList());
        assertEquals(asList(), Seq.of(1, 2, 3).splitAtHead().v2.splitAtHead().v2.splitAtHead().v2.toList());
    }

    @Test
    public void testMinByMaxBy() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5, 6);

        assertEquals(1, (int) s.get().maxBy(t -> Math.abs(t - 5)).get());
        assertEquals(5, (int) s.get().minBy(t -> Math.abs(t - 5)).get());

        assertEquals(6, (int) s.get().maxBy(t -> "" + t).get());
        assertEquals(1, (int) s.get().minBy(t -> "" + t).get());
    }

    @Test
    public void testUnzip() {
        Supplier<Seq<Tuple2<Integer, String>>> s = () -> Seq.of(tuple(1, "a"), tuple(2, "b"), tuple(3, "c"));

        Tuple2<Seq<Integer>, Seq<String>> u1 = Seq.unzip(s.get());
        assertEquals(asList(1, 2, 3), u1.v1.toList());
        assertEquals(asList("a", "b", "c"), u1.v2.toList());

        Tuple2<Seq<Integer>, Seq<String>> u2 = Seq.unzip(s.get(), v1 -> -v1, v2 -> v2 + "!");
        assertEquals(asList(-1, -2, -3), u2.v1.toList());
        assertEquals(asList("a!", "b!", "c!"), u2.v2.toList());

        // Workaround for Eclipse bug: Explicit argument typing:
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=460517
        Tuple2<Seq<Integer>, Seq<String>> u3 = Seq.unzip(s.get(), (Tuple2<Integer, String> t) -> tuple(-t.v1, t.v2 + "!"));
        assertEquals(asList(-1, -2, -3), u3.v1.toList());
        assertEquals(asList("a!", "b!", "c!"), u3.v2.toList());

        // Workaround for Eclipse bug: Explicit argument typing:
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=460517
        Tuple2<Seq<Integer>, Seq<String>> u4 = Seq.unzip(s.get(), (Integer t1, String t2) -> tuple(-t1, t2 + "!"));
        assertEquals(asList(-1, -2, -3), u4.v1.toList());
        assertEquals(asList("a!", "b!", "c!"), u4.v2.toList());
    }

    @Test
    public void testUnzipWithLimits() {
        // Test contributed by John McClean on
        // https://github.com/jOOQ/jOOL/issues/103
        Supplier<Seq<Tuple2<Integer, String>>> s = () -> Seq.of(
            tuple(1, "a"),
            tuple(2, "b"),
            tuple(3, "c")
        );

        Tuple2<Seq<Integer>, Seq<String>> unzipped;

        // Consume v1 first
        unzipped = Seq.unzip(s.get()).map1(s1 -> s1.limit(2));
        assertEquals(Arrays.asList(1, 2), unzipped.v1.toList());
        assertEquals(Arrays.asList("a", "b", "c"), unzipped.v2.toList());

        // Consume v2 first
        unzipped = Seq.unzip(s.get()).map1(s1 -> s1.limit(2));
        assertEquals(Arrays.asList("a", "b", "c"), unzipped.v2.toList());
        assertEquals(Arrays.asList(1, 2), unzipped.v1.toList());
    }

    @Test
    public void testFold() {
        Supplier<Seq<String>> s = () -> Seq.of("a", "b", "c");

        assertEquals("abc", s.get().foldLeft("", String::concat));
        assertEquals("-a-b-c", s.get().foldLeft(new StringBuilder(), (u, t) -> u.append("-").append(t)).toString());
        assertEquals(3, (int) s.get().foldLeft(0, (u, t) -> u + t.length()));

        assertEquals("abc", s.get().foldRight("", String::concat));
        assertEquals("-c-b-a", s.get().foldRight(new StringBuilder(), (t, u) -> u.append("-").append(t)).toString());
        assertEquals(3, (int) s.get().foldRight(0, (t, u) -> u + t.length()));
    }

    @Test
    public void testScanLeft() {
        assertEquals(
            asList("", "a", "ab", "abc"),
            Seq.of("a", "b", "c").scanLeft("", String::concat).toList());

        assertEquals(
            asList(0, 1, 3, 6),
            Seq.of("a", "ab", "abc").scanLeft(0, (u, t) -> u + t.length()).toList());

        assertEquals(
            asList(0, 1, 3, 6),
            Seq.iterate(1, n -> n + 1).scanLeft(0, (u, t) -> u + t).limit(4).toList());
    }

    @Test
    public void testScanRight() {
        assertEquals(
            asList("", "c", "bc", "abc"),
            Seq.of("a", "b", "c").scanRight("", String::concat).toList());

        assertEquals(
            asList(0, 3, 5, 6),
            Seq.of("a", "ab", "abc").scanRight(0, (t, u) -> u + t.length()).toList());

        assertEquals(
            asList(0, 3, 5, 6),
            Seq.iterate(1, n -> n + 1).limit(3).scanRight(0, (t, u) -> u + t).toList());
    }

    @Test
    public void testUnfold() {
        // Workaround for Eclipse bug: Explicit argument typing:
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=460517
        assertEquals(
            asList(0, 1, 2, 3, 4),
            Seq.unfold(0, (Integer i) -> i < 5 ? Optional.of(tuple(i, i + 1)) : Optional.empty()).toList());

        // Workaround for Eclipse bug: Explicit argument typing:
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=460517
        assertEquals(
            asList('a', 'b', 'c', 'd', 'e'),
            Seq.unfold(0, (Integer i) -> i < 5 ? Optional.of(tuple((char) ('a' + i), i + 1)) : Optional.empty()).toList());
    }

    @Test
    public void testReverse() {
        assertEquals(asList(3, 2, 1), Seq.of(1, 2, 3).reverse().toList());
    }

    @Test
    public void testShuffle() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3);

        assertEquals(3, s.get().shuffle().toList().size());
        assertThat(s.get().shuffle().toList(), hasItems(1, 2, 3));

        assertEquals(asList(2, 3, 1), s.get().shuffle(new Random(1)).toList());
    }

    @Test
    public void testCycle() {
        assertEquals(asList(1, 2, 1, 2, 1, 2), Seq.of(1, 2).cycle().limit(6).toList());
        assertEquals(asList(1, 2, 3, 1, 2, 3), Seq.of(1, 2, 3).cycle().limit(6).toList());
    }

    @Test
    public void testDistinct() {
        assertEquals(asList(1, 2, 3), Seq.of(1, 1, 2, -2, 3).distinct(Math::abs).toList());
    }

    @Test
    public void testIterable() {
        List<Integer> list = Seq.of(1, 2, 3).toCollection(LinkedList::new);

        for (Integer i : Seq.of(1, 2, 3)) {
            assertEquals(list.remove(0), i);
        }
    }

    @Test
    public void testOfType() {
        assertEquals(asList(1, 2, 3), Seq.of(1, "a", 2, "b", 3, null).ofType(Integer.class).toList());
        assertEquals(asList(1, "a", 2, "b", 3), Seq.of(1, "a", 2, "b", 3, null).ofType(Serializable.class).toList());
    }

    @Test
    public void testCast() {
        Utils.assertThrows(
            ClassCastException.class,
            () -> Seq.of(1, "a", 2, "b", 3, null).cast(Integer.class).toList());
        assertEquals(asList(1, "a", 2, "b", 3, null), Seq.of(1, "a", 2, "b", 3, null).cast(Serializable.class).toList());
    }

    @Test
    public void testGroupBy() {
        Map<Integer, List<Integer>> map1 = Seq.of(1, 2, 3, 4).groupBy(i -> i % 2);
        assertEquals(asList(2, 4), map1.get(0));
        assertEquals(asList(1, 3), map1.get(1));
        assertEquals(2, map1.size());

        Map<Integer, List<Tuple2<Integer, Integer>>> map2 =
        Seq.of(tuple(1, 1), tuple(1, 2), tuple(1, 3), tuple(2, 1), tuple(2, 2))
           .groupBy(t -> t.v1);
        assertEquals(asList(tuple(1, 1), tuple(1, 2), tuple(1, 3)), map2.get(1));
        assertEquals(asList(tuple(2, 1), tuple(2, 2)), map2.get(2));

        Map<Integer, Long> map3 =
        Seq.of(tuple(1, 1), tuple(1, 2), tuple(1, 3), tuple(2, 1), tuple(2, 2))
           .groupBy(t -> t.v1, counting());
        assertEquals(3L, (long) map3.get(1));
        assertEquals(2L, (long) map3.get(2));

        Map<Integer, Tuple2<Long, String>> map4 =
        Seq.of(tuple(1, 1), tuple(1, 2), tuple(1, 3), tuple(2, 4), tuple(2, 5))
           .groupBy(t -> t.v1, collectors(counting(), mapping(t -> t.map2(Object::toString).v2, joining(", "))));
        assertEquals(3L, (long) map4.get(1).v1);
        assertEquals(2L, (long) map4.get(2).v1);
        assertEquals("1, 2, 3", map4.get(1).v2);
        assertEquals("4, 5", map4.get(2).v2);
    }

    @Test
    public void testJoin() {
        assertEquals("123", Seq.of(1, 2, 3).join());
        assertEquals("1, 2, 3", Seq.of(1, 2, 3).join(", "));
        assertEquals("^1|2|3$", Seq.of(1, 2, 3).join("|", "^", "$"));
    }

    @Test
    public void testMap() {
        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "c");

        assertEquals(
            asList(tuple(1, "a"), tuple(2, "b"), tuple(3, "c")),
            Seq.seq(map).toList()
        );
    }

    @Test
    public void testOptional() {
        assertEquals(asList(1), Seq.seq(Optional.of(1)).toList());
        assertEquals(asList(), Seq.seq(Optional.empty()).toList());
    }

    @Test
    public void testInputStream() {
        InputStream is = new ByteArrayInputStream(new byte[] { 0, 1, 2, 3 });
        assertEquals(asList((byte) 0, (byte) 1, (byte) 2, (byte) 3), Seq.seq(is).toList());
    }

    @Test
    public void testReader() {
        StringReader reader1 = new StringReader("abc");
        assertEquals(asList('a', 'b', 'c'), Seq.seq(reader1).toList());

        StringReader reader2 = new StringReader("abc");
        assertEquals("abc", Seq.seq(reader2).join());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(Seq.empty().isEmpty());
        assertFalse(Seq.empty().isNotEmpty());

        assertFalse(Seq.of(1).isEmpty());
        assertFalse(Seq.of(1, 2).isEmpty());
        assertTrue(Seq.of(1).isNotEmpty());
        assertTrue(Seq.of(1, 2).isNotEmpty());
    }

    @Test
    public void testSorted() {
        Seq<Tuple2<Integer, String>> t1 = Seq.of(tuple(2, "two"), tuple(1, "one"));
        List<Tuple2<Integer, String>> s1 = t1.sorted().toList();
        assertEquals(tuple(1, "one"), s1.get(0));
        assertEquals(tuple(2, "two"), s1.get(1));

        Seq<Tuple2<Integer, String>> t2 = Seq.of(tuple(2, "two"), tuple(1, "one"));
        List<Tuple2<Integer, String>> s2 = t2.sorted(comparing(t -> t.v1())).toList();
        assertEquals(tuple(1, "one"), s2.get(0));
        assertEquals(tuple(2, "two"), s2.get(1));

        Seq<Tuple2<Integer, String>> t3 = Seq.of(tuple(2, "two"), tuple(1, "one"));
        List<Tuple2<Integer, String>> s3 = t3.sorted(t -> t.v1()).toList();
        assertEquals(tuple(1, "one"), s3.get(0));
        assertEquals(tuple(2, "two"), s3.get(1));
    }

    @Test
    public void testContains() {
        assertTrue(Seq.of(1, 2, 3).contains(2));
        assertFalse(Seq.of(1, 2, 3).contains(4));
        assertFalse(Seq.of(1, 2, 3).contains(null));
    }

    @Test
    public void testContainsAll() {
        assertTrue(Seq.of(1, 2, 3).containsAll());
        assertTrue(Seq.of(1, 2, 3).containsAll(1));
        assertTrue(Seq.of(1, 2, 3).containsAll(1, 2));
        assertTrue(Seq.of(1, 2, 3).containsAll(1, 2, 3));
        assertFalse(Seq.of(1, 2, 3).containsAll(1, 2, 3, 4));
        assertFalse(Seq.of(1, 2, 3).containsAll(4));
        assertFalse(Seq.of(1, 2, 3).containsAll((Integer) null));
    }
}
