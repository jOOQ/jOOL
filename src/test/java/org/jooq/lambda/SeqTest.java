/**
 * Copyright (c) 2014-2016, Data Geekery GmbH, contact@datageekery.com
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
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.jooq.lambda.Agg.count;
import static org.jooq.lambda.Agg.max;
import static org.jooq.lambda.Agg.min;
import static org.jooq.lambda.Seq.seq;
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
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jooq.lambda.exception.TooManyElementsException;
import org.jooq.lambda.function.Function4;

import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.jooq.lambda.tuple.Tuple4;
import org.jooq.lambda.tuple.Tuple5;
import org.jooq.lambda.tuple.Tuple6;
import org.jooq.lambda.tuple.Tuple7;
import org.jooq.lambda.tuple.Tuple8;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Lukas Eder
 * @author Roman Tkalenko
 */
public class SeqTest {

    @Test
    public void testSeqArraySlice() throws Exception {
        Seq<Integer> seq = seq(new Integer[] { 1, 2, 3, 4, 5 }, 2, 4);
        assertEquals(asList(3, 4), seq.toList());
    }
    
    @Test
    public void testTransform() throws Exception {
        Function<Seq<Integer>, Seq<String>> toStringInt = i -> i.map(Objects::toString);
        Function<Seq<? extends Number>, Seq<String>> toStringNumber = i -> i.map(Objects::toString);
        Function<Seq<?>, Seq<String>> toStringAny = i -> i.map(Objects::toString);
        
        Seq<String> s1 = Seq.of(1, 2, 3).transform(toStringInt);
        assertEquals(asList("1", "2", "3"), s1.toList());
        
        Seq<String> s2 = Seq.of(1, 2, 3).transform(toStringNumber);
        assertEquals(asList("1", "2", "3"), s2.toList());
        
        Seq<String> s3 = Seq.of(1, 2, 3).transform(toStringAny);
        assertEquals(asList("1", "2", "3"), s3.toList());
    }
    
    @Test
    public void testGroupedIterator() throws Exception {
        Iterator<Tuple2<Integer, Seq<Integer>>> it1 =
        Seq.of(1, 2, 3, 4)
           .grouped(i -> i % 2)
           .iterator();

        assertTrue(it1.hasNext());
        Tuple2<Integer, Seq<Integer>> t11 = it1.next();
        assertTrue(it1.hasNext());
        assertEquals(1, (int) t11.v1);
        Iterator<Integer> it11 = t11.v2.iterator();
        assertTrue(it11.hasNext());
        assertEquals(1, (int) it11.next());
        assertTrue(it11.hasNext());

        assertTrue(it1.hasNext());
        Tuple2<Integer, Seq<Integer>> t12 = it1.next();
        assertFalse(it1.hasNext());
        assertEquals(0, (int) t12.v1);
        Iterator<Integer> it12 = t12.v2.iterator();
        assertTrue(it12.hasNext());
        assertEquals(2, (int) it12.next());
        assertTrue(it12.hasNext());

        assertEquals(3, (int) it11.next());
        assertEquals(4, (int) it12.next());

        assertFalse(it1.hasNext());
        assertFalse(it11.hasNext());
        assertFalse(it12.hasNext());

        assertThrows(NoSuchElementException.class, () -> it1.next());
        assertThrows(NoSuchElementException.class, () -> it11.next());
        assertThrows(NoSuchElementException.class, () -> it12.next());
    }

    @Test
    public void testGroupedIteratorSkipFirstGroup() throws Exception {
        Iterator<Tuple2<Integer, Seq<Integer>>> it1 =
        Seq.of(1, 2, 3, 4)
           .grouped(i -> i % 2)
           .iterator();

        assertTrue(it1.hasNext());
        Tuple2<Integer, Seq<Integer>> t11 = it1.next();

        assertTrue(it1.hasNext());
        Tuple2<Integer, Seq<Integer>> t12 = it1.next();
        assertEquals(0, (int) t12.v1);
        Iterator<Integer> it12 = t12.v2.iterator();
        assertTrue(it12.hasNext());
        assertEquals(2, (int) it12.next());
        assertTrue(it12.hasNext());
        assertEquals(4, (int) it12.next());
        assertFalse(it12.hasNext());
        assertThrows(NoSuchElementException.class, () -> it12.next());

        assertEquals(1, (int) t11.v1);
        Iterator<Integer> it11 = t11.v2.iterator();
        assertTrue(it11.hasNext());
        assertEquals(1, (int) it11.next());
        assertTrue(it11.hasNext());
        assertEquals(3, (int) it11.next());
        assertFalse(it11.hasNext());
        assertThrows(NoSuchElementException.class, () -> it11.next());

        assertFalse(it1.hasNext());
        assertThrows(NoSuchElementException.class, () -> it1.next());
    }

    @Test
    public void testGroupedWithNullClassifications() throws Exception {
        List<Tuple2<Integer, List<Integer>>> list =
        Seq.of(1, 2, 3, 4)
           .grouped(x -> x % 2 == 0 ? null : 1)
           .map(kv -> kv.map2(cls -> cls.toList())).toList();

        assertEquals(asList(
            Tuple.tuple(1, Arrays.asList(1, 3)),
            Tuple.tuple(null, Arrays.asList(2, 4))
        ), list);
    }

    @Test
    public void testGroupedToList() throws Exception {
        List<Tuple2<Integer, List<Integer>>> list =
        Seq.of(1, 2, 3, 4)
           .grouped(x -> x % 2)
           .map(kv -> kv.map2(cls -> cls.toList())).toList();

        assertEquals(asList(
            Tuple.tuple(1, Arrays.asList(1, 3)),
            Tuple.tuple(0, Arrays.asList(2, 4))
        ), list);
    }

    @Test
    public void testGroupedWithCollector() throws Exception {
        List<Tuple2<Integer, List<Integer>>> l1 =
        Seq.of(1, 2, 3, 4)
           .grouped(x -> x % 2, Collectors.toList())
           .toList();

        assertEquals(asList(
            Tuple.tuple(1, Arrays.asList(1, 3)),
            Tuple.tuple(0, Arrays.asList(2, 4))
        ), l1);


        List<Tuple2<Integer, String>> l2 =
        Seq.of(1, 2, 3, 4)
           .grouped(x -> x % 2, Collectors.mapping(Object::toString, Collectors.joining(", ")))
           .toList();

        assertEquals(asList(
            Tuple.tuple(1, "1, 3"),
            Tuple.tuple(0, "2, 4")
        ), l2);
    }

    @Test
    public void testGroupedSameBehaviorAsGroupBy() throws Exception {
        Random r = new Random(System.nanoTime());
        int runs = r.nextInt(127) + 1;
        for (int i = 0; i < runs; i++) {
            int mod = r.nextInt(125) + 2;
            List<Long> longs = r.longs().limit(1024).boxed().collect(toList());
            Map<Long, List<Long>> newMethod = Seq.seq(longs).grouped(x -> x % mod).toMap(t -> t.v1, t -> t.v2.toList());
            Map<Long, List<Long>> libMethod = longs.stream().collect(groupingBy(x -> x % mod));
            assertEquals(libMethod, newMethod);
        }
    }

    @Test
    public void testGroupedOnEmptySeq() throws Exception {
        Seq<int[]> seq = Seq.empty();
        assertEquals(Seq.empty().toList(), seq.grouped(xs -> xs.length).toList());
    }

    @Test
    public void testGroupedOnASingleElementSeq() throws Exception {
        int[] array = { 0 };
        Seq<int[]> seq = Seq.of(array);
        assertEquals(
                asList(tuple(1, asList(array))),
                seq.grouped(xs -> xs.length).map(t -> t.map2(s -> s.toList())).toList());
    }

    @Test
    public void testGroupedOnSeqOfEqualElements() throws Exception {
        List<String> strings = asList("seq", "seq", "seq", "seq", "seq");
        Seq<String> seq = Seq.seq(strings);
        assertEquals(
                asList(tuple('q', strings)),
                seq.grouped(xs -> xs.charAt(2)).map(t -> t.map2(s -> s.toList())).toList());
    }

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
        
        assertEquals(asList(1, 2, 3), Seq.of(1, 2, 3).concat(Optional.empty()).toList());
        assertEquals(asList(1, 2, 3, 4), Seq.of(1, 2, 3).concat(Optional.of(4)).toList());
        
        assertEquals(asList(1, 2, 3), Seq.of(1, 2, 3).prepend(Optional.empty()).toList());
        assertEquals(asList(0, 1, 2, 3), Seq.of(1, 2, 3).prepend(Optional.of(0)).toList());
        
        assertEquals(asList(1, 2), Seq.concat(Optional.of(1), Optional.empty(), Optional.of(2), Optional.empty()).toList());
    }

    @Test
    public void testAppend() {
        assertEquals(asList(1, 2, 3, 4), Seq.of(1).append(Seq.of(2, 3, 4)).toList());
        assertEquals(asList(1, 2, 3, 4), Seq.of(1, 2).append(Seq.of(3, 4)).toList());
        assertEquals(asList(1, 2, 3, 4), Seq.of(1).append(2, 3, 4).toList());
        assertEquals(asList(1, 2, 3, 4), Seq.of(1, 2).append(3, 4).toList());
    }

    @Test
    public void testPrepend() {
        assertEquals(asList(1, 2, 3, 4), Seq.of(4).prepend(Seq.of(1, 2, 3)).toList());
        assertEquals(asList(1, 2, 3, 4), Seq.of(3, 4).prepend(Seq.of(1, 2)).toList());
        assertEquals(asList(1, 2, 3, 4), Seq.of(4).prepend(1, 2, 3).toList());
        assertEquals(asList(1, 2, 3, 4), Seq.of(3, 4).prepend(1, 2).toList());
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
        Seq<Integer> oneTwoThree = Seq.of(1, 2, 3);

        // [#118] toString() operations on a Stream are buffered
        assertEquals("123", oneTwoThree.toString());
        assertEquals("123", oneTwoThree.toString());

        assertEquals("1, 2, 3", oneTwoThree.toString(", "));
        assertEquals("1, 2, 3", oneTwoThree.toString(", "));

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
    public void testToIdentityMap() {
        LocalDate v1_0 = LocalDate.of(1996, 1, 23);
        LocalDate v1_1 = LocalDate.of(1997, 2, 19);
        LocalDate v1_2 = LocalDate.of(1998, 12, 8);
        Map<Integer, LocalDate> expected = new HashMap<>();
        expected.put(v1_0.getYear(), v1_0);
        expected.put(v1_1.getYear(), v1_1);
        expected.put(v1_2.getYear(), v1_2);

        assertEquals(expected, Seq.of(v1_0, v1_1, v1_2).toMap(LocalDate::getYear));
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
    public void testSkipWhileClosed() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(2, 3, 4, 5), s.get().skipWhileClosed(i -> false).toList());
        assertEquals(asList(4, 5), s.get().skipWhileClosed(i -> i % 3 != 0).toList());
        assertEquals(asList(4, 5), s.get().skipWhileClosed(i -> i < 3).toList());
        assertEquals(asList(5), s.get().skipWhileClosed(i -> i < 4).toList());
        assertEquals(asList(), s.get().skipWhileClosed(i -> true).toList());
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
    public void testSkipUntilWithSplitAtHead() {
        // See https://github.com/jOOQ/jOOL/issues/236
        Tuple2<Optional<Integer>, Seq<Integer>> split = 
            Seq.of(1, 2, 3, 4, 5, 6).skipUntil(i -> i == 3).splitAtHead();
        
        assertEquals(Optional.of(3), split.v1);
        assertEquals(Arrays.asList(4, 5, 6), split.v2.toList());
    }

    @Test
    public void testSkipUntilClosed() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(), s.get().skipUntilClosed(i -> false).toList());
        assertEquals(asList(4, 5), s.get().skipUntilClosed(i -> i % 3 == 0).toList());
        assertEquals(asList(4, 5), s.get().skipUntilClosed(i -> i == 3).toList());
        assertEquals(asList(5), s.get().skipUntilClosed(i -> i == 4).toList());
        assertEquals(asList(2, 3, 4, 5), s.get().skipUntilClosed(i -> true).toList());
    }

    @Test
    public void testSkipUntilClosedWithSplitAtHead() {
        // See https://github.com/jOOQ/jOOL/issues/236
        Tuple2<Optional<Integer>, Seq<Integer>> split =
            Seq.of(1, 2, 3, 4, 5, 6).skipUntilClosed(i -> i == 3).splitAtHead();
        
        assertEquals(Optional.of(4), split.v1);
        assertEquals(Arrays.asList(5, 6), split.v2.toList());
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
    public void testLimitWhileClosed() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(1), s.get().limitWhileClosed(i -> false).toList());
        assertEquals(asList(1, 2, 3), s.get().limitWhileClosed(i -> i % 3 != 0).toList());
        assertEquals(asList(1, 2, 3), s.get().limitWhileClosed(i -> i < 3).toList());
        assertEquals(asList(1, 2, 3, 4), s.get().limitWhileClosed(i -> i < 4).toList());
        assertEquals(asList(1, 2, 3, 4, 5), s.get().limitWhileClosed(i -> true).toList());
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
    public void testLimitUntilClosed() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 2, 3, 4, 5);

        assertEquals(asList(1, 2, 3, 4, 5), s.get().limitUntilClosed(i -> false).toList());
        assertEquals(asList(1, 2, 3), s.get().limitUntilClosed(i -> i % 3 == 0).toList());
        assertEquals(asList(1, 2, 3), s.get().limitUntilClosed(i -> i == 3).toList());
        assertEquals(asList(1, 2, 3, 4), s.get().limitUntilClosed(i -> i == 4).toList());
        assertEquals(asList(1), s.get().limitUntilClosed(i -> true).toList());
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
    public void testMinAllMaxAll() {
        Supplier<Seq<Integer>> s = () -> Seq.of(1, 1, 2, 3, 4, 5, 5, 6);

        assertEquals(asList(6)   , s.get().maxAll().toList());
        assertEquals(asList(3, 3), s.get().maxAll(t -> Math.abs(t - 4)).toList());
        assertEquals(asList(4, 4), s.get().maxAll(t -> Math.abs(t - 5)).toList());
        assertEquals(asList(1, 1), s.get().minAll().toList());
        assertEquals(asList(0)   , s.get().minAll(t -> Math.abs(t - 4)).toList());
        assertEquals(asList(0, 0), s.get().minAll(t -> Math.abs(t - 5)).toList());
    }

    @Test
    public void testMinAllByMaxAllBy() {
        Supplier<Seq<String>> s = () -> Seq.of("abc", "ab", "xy", "xyz");

        assertEquals(asList("abc", "xyz"), s.get().maxAllBy(String::length).toList());
        assertEquals(asList("ab" , "xy" ), s.get().minAllBy(String::length).toList());
    }

    @Test
    public void testMode() {
        assertEquals(1, (int) Seq.of(1, 1, 1, 2, 3).mode().get());
        assertEquals(2, (int) Seq.of(1, 2, 2, 2, 3, 3, 3, 4).mode().get());
        assertEquals(3, (int) Seq.of(1, 2, 2, 2, 3, 3, 3, 4).reverse().mode().get());
        assertEquals("B", Seq.of("A", "B", "B", "B", "C").mode().get());
        assertEquals(Optional.empty(), Seq.of().mode());
    }

    @Test
    public void testModeAll() {
        assertEquals(asList(1)   , Seq.of(1, 1, 1, 2, 3).modeAll().toList());
        assertEquals(asList(2, 3), Seq.of(1, 2, 2, 2, 3, 3, 3, 4).modeAll().toList());
        assertEquals(asList(3, 2), Seq.of(1, 2, 2, 2, 3, 3, 3, 4).reverse().modeAll().toList());
        assertEquals(asList("B") , Seq.of("A", "B", "B", "B", "C").modeAll().toList());
        assertEquals(asList()    , Seq.of().modeAll().toList());
    }

    @Test
    public void testModeBy() {
        assertEquals(1, (int) Seq.of(1, 2, 2, 3, 5).modeBy(i -> i % 2).get());
        assertEquals(5, (int) Seq.of(1, 2, 2, 3, 5).reverse().modeBy(i -> i % 2).get());
        assertEquals("abc", Seq.of("abc", "a", "", "xyz", "asdfasdf").modeBy(String::length).get());
        assertEquals(Optional.empty(), Seq.of().modeBy(i -> i));
    }

    @Test
    public void testModeAllBy() {
        assertEquals(asList(1, 3, 5), Seq.of(1, 2, 2, 3, 5).modeAllBy(i -> i % 2).toList());
        assertEquals(asList(5, 3, 1), Seq.of(1, 2, 2, 3, 5).reverse().modeAllBy(i -> i % 2).toList());
        assertEquals(asList("abc", "xyz"), Seq.of("abc", "a", "", "xyz", "asdfasdf").modeAllBy(String::length).toList());
        assertEquals(asList(), Seq.of().modeAllBy(i -> i).toList());
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

        assertEquals("!abc", s.get().foldLeft("!", String::concat));
        assertEquals("!abc", s.get().foldLeft("!", (u, t) -> u + t));
        assertEquals("-a-b-c", s.get().foldLeft(new StringBuilder(), (u, t) -> u.append("-").append(t)).toString());
        assertEquals(3, (int) s.get().foldLeft(0, (u, t) -> u + t.length()));

        assertEquals("abc!", s.get().foldRight("!", String::concat));
        assertEquals("abc!", s.get().foldRight("!", (t, u) -> t + u));
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
        
        Seq<Object> result = Seq.unfold(0, i -> Optional.of(tuple("a", i + 1)));
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
        assertEquals(asList(), Seq.empty().cycle().toList());
        assertEquals(asList(1, 2, 1, 2, 1, 2), Seq.of(1, 2).cycle().limit(6).toList());
        assertEquals(asList(1, 2, 3, 1, 2, 3), Seq.of(1, 2, 3).cycle().limit(6).toList());
    }
    
    @Test
    public void testCycleTimes() {
        assertEquals(asList(), Seq.empty().cycle(0).toList());
        assertEquals(asList(), Seq.empty().cycle(1).toList());
        assertEquals(asList(), Seq.empty().cycle(2).toList());
        
        assertEquals(asList(), Seq.of(1).cycle(0).toList());
        assertEquals(asList(1), Seq.of(1).cycle(1).toList());
        assertEquals(asList(1, 1), Seq.of(1).cycle(2).toList());
        assertEquals(asList(1, 1, 1), Seq.of(1).cycle(3).toList());

        assertEquals(asList(), Seq.of(1, 2).cycle(0).toList());
        assertEquals(asList(1, 2), Seq.of(1, 2).cycle(1).toList());
        assertEquals(asList(1, 2, 1, 2), Seq.of(1, 2).cycle(2).toList());
        assertEquals(asList(1, 2, 1, 2, 1, 2), Seq.of(1, 2).cycle(3).toList());
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
    public void testEnumeration() {
        List<Integer> list = asList(1, 2, 3);
        Vector<Integer> vector = new Vector<>();
        vector.addAll(list);

        assertEquals(list, Seq.seq(vector.elements()).toList());
    }

    @Test
    public void testIterate() {
        assertEquals(
            asList(2, 4, 16, 256, 65536),
            Seq.iterate(2, i -> i * i).limit(5).toList()
        );
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
    public void testGroupByAndMax() {
        Map<String, Optional<Integer>> map = 
        Seq.of(
            tuple("A", 1),
            tuple("B", 2),
            tuple("C", 3),
            tuple("A", 4),
            tuple("A", 5),
            tuple("C", 6))
           .groupBy(t -> t.v1, Agg.max(t -> t.v2));
        
        assertEquals(3, map.size());
        assertEquals(5, (int) map.get("A").get());
        assertEquals(2, (int) map.get("B").get());
        assertEquals(6, (int) map.get("C").get());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testJoin() {
        assertEquals("123", Seq.of(1, 2, 3).join());
        assertEquals("1, 2, 3", Seq.of(1, 2, 3).join(", "));
        assertEquals("^1|2|3$", Seq.of(1, 2, 3).join("|", "^", "$"));
    }
    
    @Test
    public void testFormat() {
        System.out.println(
            Seq.of(
                    tuple(12, (Double) null, new BigDecimal("0.12341234"), (String) null),
                    tuple(1, 1.1, null, "abc"),
                    tuple(12, 3.25, new BigDecimal("3134.256"), "xyz abc")
                )
               .format()
        );
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
    public void testOptionals() {
        assertEquals(asList(1), Seq.seq(Optional.of(1), Optional.empty()).toList());
        assertEquals(asList(1,2), Seq.seq(Optional.of(1), Optional.of(2)).toList());
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
        assertEquals("abc", Seq.seq(reader2).toString());
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
    public void testSortedComplexPipeline() {
        List<Integer> expected1 = asList(1, 1, 1, 2, 2, 2, 3, 3, 3);
        List<Integer> expected2 = asList(1, 2, 3, 1, 2, 3, 1, 2, 3);
        
        assertEquals(expected2, Seq.of(3, 2, 1).sorted().cycle(3).toList());
        assertEquals(expected1, Seq.of(1, 2, 3).cycle(3).sorted().toList());
        assertEquals(expected1, Seq.of(1, 2, 3).cycle(3).sorted().sorted().toList());
        assertEquals(expected1, Seq.of(1, 2, 3).sorted().cycle(3).sorted().toList());
        
        assertEquals(expected2, Seq.of(3, 2, 1).filter(x -> true).sorted().cycle(3).toList());
        assertEquals(expected1, Seq.of(1, 2, 3).filter(x -> true).cycle(3).sorted().toList());
        assertEquals(expected1, Seq.of(1, 2, 3).filter(x -> true).cycle(3).sorted().sorted().toList());
        assertEquals(expected1, Seq.of(1, 2, 3).filter(x -> true).sorted().cycle(3).sorted().toList());
        
        assertEquals(expected2, Seq.of(3, 2, 1).filter(x -> true).sorted(Comparator.naturalOrder()).cycle(3).toList());
        assertEquals(expected1, Seq.of(1, 2, 3).filter(x -> true).cycle(3).sorted(Comparator.naturalOrder()).toList());
        assertEquals(expected1, Seq.of(1, 2, 3).filter(x -> true).cycle(3).sorted(Comparator.naturalOrder()).sorted().toList());
        assertEquals(expected1, Seq.of(1, 2, 3).filter(x -> true).sorted(Comparator.naturalOrder()).cycle(3).sorted().toList());
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

    @Test
    public void testContainsAny() {
        assertFalse(Seq.of(1, 2, 3).containsAny());
        assertTrue(Seq.of(1, 2, 3).containsAny(1));
        assertTrue(Seq.of(1, 2, 3).containsAny(1, 2));
        assertTrue(Seq.of(1, 2, 3).containsAny(1, 2, 3));
        assertTrue(Seq.of(1, 2, 3).containsAny(1, 2, 3, 4));
        assertFalse(Seq.of(1, 2, 3).containsAny(4));
        assertFalse(Seq.of(1, 2, 3).containsAny((Integer) null));
    }

    @Test
    public void testRemove() {
        assertEquals(Arrays.asList(1, 3, 2, 4), Seq.of(1, 2, 3, 2, 4).remove(2).toList());
        assertEquals(Arrays.asList(1, 3, 4), Seq.of(1, 2, 3, 2, 4).remove(2).remove(2).toList());
        assertEquals(Arrays.asList(1, 3, 4), Seq.of(1, 2, 3, 2, 4).remove(2).remove(2).remove(2).toList());
        assertEquals(Arrays.asList(1, 2, 3, 2, 4), Seq.of(1, 2, 3, 2, 4).remove(5).toList());
        assertEquals(Arrays.asList(1, 2, 3, 2, 4), Seq.of(1, 2, 3, 2, 4).remove(null).toList());
    }

    @Test
    public void testRemoveAll() {
        assertEquals(Arrays.asList(1, 3, 4), Seq.of(1, 2, 3, 2, 4).removeAll(2).toList());
        assertEquals(Arrays.asList(1, 3, 4), Seq.of(1, 2, 3, 2, 4).removeAll(2).removeAll(2).toList());
        assertEquals(Arrays.asList(1, 2, 3, 2, 4), Seq.of(1, 2, 3, 2, 4).removeAll(5).toList());
        assertEquals(Arrays.asList(1, 4), Seq.of(1, 2, 3, 2, 4).removeAll(2, 3).toList());
        assertEquals(Arrays.asList(1, 2, 3, 2, 4), Seq.of(1, 2, 3, 2, 4).removeAll((Integer) null).toList());
    }

    @Test
    public void testRetainAll() {
        assertEquals(Arrays.asList(2, 3, 2), Seq.of(1, 2, 3, 2, 4).retainAll(2, 3).toList());
        assertEquals(Arrays.asList(2, 2), Seq.of(1, 2, 3, 2, 4).retainAll(2, 3).retainAll(2).toList());
        assertEquals(Arrays.asList(), Seq.of(1, 2, 3, 2, 4).retainAll(5).toList());
        assertEquals(Arrays.asList(), Seq.of(1, 2, 3, 2, 4).retainAll((Integer) null).toList());
    }

    @Test
    public void testRange() {
        assertEquals(Arrays.asList(), Seq.range((byte) 0, (byte) -1).toList());
        assertEquals(Arrays.asList(), Seq.range((byte) 0, (byte) 0).toList());
        assertEquals(Arrays.asList((byte) 0), Seq.range((byte) 0, (byte) 1).toList());
        assertEquals(Arrays.asList((byte) 0, (byte) 1), Seq.range((byte) 0, (byte) 2).toList());
        assertEquals(Arrays.asList((byte) 0, (byte) 2), Seq.range((byte) 0, (byte) 4, 2).toList());
        assertEquals(Arrays.asList((byte) 0, (byte) 2, (byte) 4), Seq.range((byte) 0, (byte) 5, 2).toList());

        assertEquals(Arrays.asList(), Seq.range((short) 0, (short) -1).toList());
        assertEquals(Arrays.asList(), Seq.range((short) 0, (short) 0).toList());
        assertEquals(Arrays.asList((short) 0), Seq.range((short) 0, (short) 1).toList());
        assertEquals(Arrays.asList((short) 0, (short) 1), Seq.range((short) 0, (short) 2).toList());
        assertEquals(Arrays.asList((short) 0, (short) 2), Seq.range((short) 0, (short) 4, 2).toList());
        assertEquals(Arrays.asList((short) 0, (short) 2, (short) 4), Seq.range((short) 0, (short) 5, 2).toList());

        assertEquals("", Seq.range('B', 'A').toString());
        assertEquals("", Seq.range('B', 'B').toString());
        assertEquals("A", Seq.range('A', 'B').toString());
        assertEquals("AB", Seq.range('A', 'C').toString());
        assertEquals("AC", Seq.range('A', 'E', 2).toString());
        assertEquals("ACE", Seq.range('A', 'F', 2).toString());

        assertEquals(Arrays.asList(), Seq.range(0, -1).toList());
        assertEquals(Arrays.asList(), Seq.range(0, 0).toList());
        assertEquals(Arrays.asList(0), Seq.range(0, 1).toList());
        assertEquals(Arrays.asList(0, 1), Seq.range(0, 2).toList());
        assertEquals(Arrays.asList(0, 2), Seq.range(0, 4, 2).toList());
        assertEquals(Arrays.asList(0, 2, 4), Seq.range(0, 5, 2).toList());

        assertEquals(Arrays.asList(), Seq.range(0L, -1L).toList());
        assertEquals(Arrays.asList(), Seq.range(0L, 0L).toList());
        assertEquals(Arrays.asList(0L), Seq.range(0L, 1L).toList());
        assertEquals(Arrays.asList(0L, 1L), Seq.range(0L, 2L).toList());
        assertEquals(Arrays.asList(0L, 2L), Seq.range(0L, 4L, 2).toList());
        assertEquals(Arrays.asList(0L, 2L, 4L), Seq.range(0L, 5L, 2).toList());

        assertEquals(
            Arrays.asList(),
            Seq.range(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(-1L)).toList());
        assertEquals(
            Arrays.asList(),
            Seq.range(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(0L)).toList());
        assertEquals(
            Arrays.asList(Instant.ofEpochSecond(0L)),
            Seq.range(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(1L)).toList());
        assertEquals(
            Arrays.asList(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(1L)),
            Seq.range(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(2L)).toList());
        assertEquals(
            Arrays.asList(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(2L)),
            Seq.range(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(4L), Duration.ofSeconds(2)).toList());
        assertEquals(
            Arrays.asList(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(2L), Instant.ofEpochSecond(4L)),
            Seq.range(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(5L), Duration.ofSeconds(2)).toList());
    }

    @Test
    public void testRangeClosed() {
        assertEquals(Arrays.asList(), Seq.rangeClosed((byte) 0, (byte) -1).toList());
        assertEquals(Arrays.asList((byte) 0), Seq.rangeClosed((byte) 0, (byte) 0).toList());
        assertEquals(Arrays.asList((byte) 0, (byte) 1), Seq.rangeClosed((byte) 0, (byte) 1).toList());
        assertEquals(Arrays.asList((byte) 0, (byte) 1, (byte) 2), Seq.rangeClosed((byte) 0, (byte) 2).toList());
        assertEquals(Arrays.asList((byte) 0, (byte) 2, (byte) 4), Seq.rangeClosed((byte) 0, (byte) 4, 2).toList());
        assertEquals(Arrays.asList((byte) 0, (byte) 2, (byte) 4), Seq.rangeClosed((byte) 0, (byte) 5, 2).toList());

        assertEquals(Arrays.asList(), Seq.rangeClosed((short) 0, (short) -1).toList());
        assertEquals(Arrays.asList((short) 0), Seq.rangeClosed((short) 0, (short) 0).toList());
        assertEquals(Arrays.asList((short) 0, (short) 1), Seq.rangeClosed((short) 0, (short) 1).toList());
        assertEquals(Arrays.asList((short) 0, (short) 1, (short) 2), Seq.rangeClosed((short) 0, (short) 2).toList());
        assertEquals(Arrays.asList((short) 0, (short) 2, (short) 4), Seq.rangeClosed((short) 0, (short) 4, 2).toList());
        assertEquals(Arrays.asList((short) 0, (short) 2, (short) 4), Seq.rangeClosed((short) 0, (short) 5, 2).toList());

        assertEquals("", Seq.rangeClosed('B', 'A').toString());
        assertEquals("B", Seq.rangeClosed('B', 'B').toString());
        assertEquals("AB", Seq.rangeClosed('A', 'B').toString());
        assertEquals("ABC", Seq.rangeClosed('A', 'C').toString());
        assertEquals("ACE", Seq.rangeClosed('A', 'E', 2).toString());
        assertEquals("ACE", Seq.rangeClosed('A', 'F', 2).toString());

        assertEquals(Arrays.asList(), Seq.rangeClosed(0, -1).toList());
        assertEquals(Arrays.asList(0), Seq.rangeClosed(0, 0).toList());
        assertEquals(Arrays.asList(0, 1), Seq.rangeClosed(0, 1).toList());
        assertEquals(Arrays.asList(0, 1, 2), Seq.rangeClosed(0, 2).toList());
        assertEquals(Arrays.asList(0, 2, 4), Seq.rangeClosed(0, 4, 2).toList());
        assertEquals(Arrays.asList(0, 2, 4), Seq.rangeClosed(0, 5, 2).toList());

        assertEquals(Arrays.asList(), Seq.rangeClosed(0L, -1L).toList());
        assertEquals(Arrays.asList(0L), Seq.rangeClosed(0L, 0L).toList());
        assertEquals(Arrays.asList(0L, 1L), Seq.rangeClosed(0L, 1L).toList());
        assertEquals(Arrays.asList(0L, 1L, 2L), Seq.rangeClosed(0L, 2L).toList());
        assertEquals(Arrays.asList(0L, 2L, 4L), Seq.rangeClosed(0L, 4L, 2).toList());
        assertEquals(Arrays.asList(0L, 2L, 4L), Seq.rangeClosed(0L, 5L, 2).toList());

        assertEquals(
            Arrays.asList(),
            Seq.rangeClosed(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(-1L)).toList());
        assertEquals(
            Arrays.asList(Instant.ofEpochSecond(0L)),
            Seq.rangeClosed(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(0L)).toList());
        assertEquals(
            Arrays.asList(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(1L)),
            Seq.rangeClosed(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(1L)).toList());
        assertEquals(
            Arrays.asList(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(1L), Instant.ofEpochSecond(2L)),
            Seq.rangeClosed(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(2L)).toList());
        assertEquals(
            Arrays.asList(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(2L), Instant.ofEpochSecond(4L)),
            Seq.rangeClosed(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(4L), Duration.ofSeconds(2)).toList());
        assertEquals(
            Arrays.asList(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(2L), Instant.ofEpochSecond(4L)),
            Seq.rangeClosed(Instant.ofEpochSecond(0L), Instant.ofEpochSecond(5L), Duration.ofSeconds(2)).toList());
    }

    @Test
    public void testGet() {
        assertEquals(Optional.empty(), Seq.of(1, 2, 3).get(-1));
        assertEquals(Optional.of(1), Seq.of(1, 2, 3).get(0));
        assertEquals(Optional.of(2), Seq.of(1, 2, 3).get(1));
        assertEquals(Optional.of(3), Seq.of(1, 2, 3).get(2));
        assertEquals(Optional.empty(), Seq.of(1, 2, 3).get(3));
    }
    
    @Test
    public void testFindSingle() {
        assertEquals(Optional.empty(), Seq.of().findSingle());
        assertEquals(Optional.empty(), Seq.of(1, 2, 3).filter(i -> i > 5).findSingle());
        assertEquals(Optional.of(1), Seq.of(1).findSingle());
        assertThrows(TooManyElementsException.class, () -> Seq.of(1, 2).findSingle());
    }
    
    @Test
    public void testFindFirst() {
        assertEquals(Optional.empty(), Seq.of(1, 2, 3).findFirst(t -> false));
        assertEquals(Optional.empty(), Seq.of(1, 2, 3).findFirst(t -> t > 3));
        assertEquals(Optional.of(3), Seq.of(1, 2, 3).findFirst(t -> t > 2));
        assertEquals(Optional.of(2), Seq.of(1, 2, 3).findFirst(t -> t > 1));
        assertEquals(Optional.of(1), Seq.of(1, 2, 3).findFirst(t -> t > 0));
    }
    
    @Test
    public void testFindLast() {
        assertEquals(Optional.empty(), Seq.empty().findLast());
        assertEquals(Optional.of(1), Seq.of(1).findLast());
        assertEquals(Optional.of(2), Seq.of(1, 2).findLast());
        assertEquals(Optional.of(3), Seq.of(1, 2, 3).findLast());
        
        assertEquals(Optional.empty(), Seq.of(1, 2, 3).findLast(t -> false));
        assertEquals(Optional.empty(), Seq.of(1, 2, 3).findLast(t -> t < 1));
        assertEquals(Optional.of(1), Seq.of(1, 2, 3).findFirst(t -> t < 2));
        assertEquals(Optional.of(2), Seq.of(1, 2, 3).findFirst(t -> t < 3));
        assertEquals(Optional.of(3), Seq.of(1, 2, 3).findFirst(t -> t < 4));
    }

    @Test
    public void testCount() {
        assertEquals(0L, Seq.of().count());
        assertEquals(0L, Seq.of().countDistinct());
        assertEquals(0L, Seq.<Integer>of().countDistinctBy(l -> l % 3));

        assertEquals(1L, Seq.of(1).count());
        assertEquals(1L, Seq.of(1).countDistinct());
        assertEquals(1L, Seq.of(1).countDistinctBy(l -> l % 3L));

        assertEquals(2L, Seq.of(1, 2).count());
        assertEquals(2L, Seq.of(1, 2).countDistinct());
        assertEquals(2L, Seq.of(1, 2).countDistinctBy(l -> l % 3L));

        assertEquals(3L, Seq.of(1, 2, 2).count());
        assertEquals(2L, Seq.of(1, 2, 2).countDistinct());
        assertEquals(2L, Seq.of(1, 2, 2).countDistinctBy(l -> l % 3L));

        assertEquals(4L, Seq.of(1, 2, 2, 4).count());
        assertEquals(3L, Seq.of(1, 2, 2, 4).countDistinct());
        assertEquals(2L, Seq.of(1, 2, 2, 4).countDistinctBy(l -> l % 3L));
    }
    
    @Test
    public void testCountWithPredicate() {
        Predicate<Integer> pi = i -> i % 2 == 0;
        Predicate<Long> pl = l -> l % 2 == 0;
        
        assertEquals(0L, Seq.<Integer>of().count(pi));
        assertEquals(0L, Seq.<Integer>of().countDistinct(pi));
        assertEquals(0L, Seq.<Integer>of().countDistinctBy(l -> l % 3, pi));

        assertEquals(0L, Seq.of(1).count(pi));
        assertEquals(0L, Seq.of(1).countDistinct(pi));
        assertEquals(0L, Seq.of(1).countDistinctBy(l -> l % 3L, pl));

        assertEquals(1L, Seq.of(1, 2).count(pi));
        assertEquals(1L, Seq.of(1, 2).countDistinct(pi));
        assertEquals(1L, Seq.of(1, 2).countDistinctBy(l -> l % 3L, pl));

        assertEquals(2L, Seq.of(1, 2, 2).count(pi));
        assertEquals(1L, Seq.of(1, 2, 2).countDistinct(pi));
        assertEquals(1L, Seq.of(1, 2, 2).countDistinctBy(l -> l % 3L, pl));

        assertEquals(3L, Seq.of(1, 2, 2, 4).count(pi));
        assertEquals(2L, Seq.of(1, 2, 2, 4).countDistinct(pi));
        assertEquals(1L, Seq.of(1, 2, 2, 4).countDistinctBy(l -> l % 3L, pl));
    }
    
    @Test
    public void testSum() {
        assertEquals(Optional.empty(), Seq.of().sum());
        
        assertEquals(Optional.of(1), Seq.of(1).sum());
        assertEquals(Optional.of(3), Seq.of(1, 2).sum());
        assertEquals(Optional.of(6), Seq.of(1, 2, 3).sum());
        
        assertEquals(Optional.of(1.0), Seq.of(1.0).sum());
        assertEquals(Optional.of(3.0), Seq.of(1.0, 2.0).sum());
        assertEquals(Optional.of(6.0), Seq.of(1.0, 2.0, 3.0).sum());
    }
    
    @Test
    public void testAvg() {
        assertEquals(Optional.empty(), Seq.of().avg());
        
        assertEquals(Optional.of(1), Seq.of(1).avg());
        assertEquals(Optional.of(1), Seq.of(1, 2).avg());
        assertEquals(Optional.of(2), Seq.of(1, 2, 3).avg());
        
        assertEquals(Optional.of(1.0), Seq.of(1.0).avg());
        assertEquals(Optional.of(1.5), Seq.of(1.0, 2.0).avg());
        assertEquals(Optional.of(2.0), Seq.of(1.0, 2.0, 3.0).avg());
    }
    
    @Test
    public void testBitAnd() {
        int value = 31 & 15 & 7 & 3;
        
        assertEquals(Optional.of(value), Seq.of(31, 15, 7, 3).bitAnd());
        assertEquals(Optional.of((long) value), Seq.of(31, 15, 7, 3).bitAnd(t -> (long) t));
        assertEquals(value, Seq.of(31, 15, 7, 3).bitAndInt(t -> t));
        assertEquals((long) value, Seq.of(31, 15, 7, 3).bitAndLong(t -> t));
    }
    
    @Test
    public void testBitOr() {
        int value = 1 | 2 | 4 | 8;
        
        assertEquals(Optional.of(value), Seq.of(1, 2, 4, 8).bitOr());
        assertEquals(Optional.of((long) value), Seq.of(1, 2, 4, 8).bitOr(t -> (long) t));
        assertEquals(value, Seq.of(1, 2, 4, 8).bitOrInt(t -> t));
        assertEquals((long) value, Seq.of(1, 2, 4, 8).bitOrLong(t -> t));
    }

    @Test
    public void testCollect() {
        assertEquals(
            tuple(0L, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
            Seq.<Integer>of().collect(count(), min(), min(i -> -i), max(), max(i -> -i))
        );

        assertEquals(
            tuple(1L, Optional.of(1), Optional.of(-1), Optional.of(1), Optional.of(-1)),
            Seq.of(1).collect(count(), min(), min(i -> -i), max(), max(i -> -i))
        );

        assertEquals(
            tuple(2L, Optional.of(1), Optional.of(-2), Optional.of(2), Optional.of(-1)),
            Seq.of(1, 2).collect(count(), min(), min(i -> -i), max(), max(i -> -i))
        );

        assertEquals(
            tuple(3L, Optional.of(1), Optional.of(-3), Optional.of(3), Optional.of(-1)),
            Seq.of(1, 2, 3).collect(count(), min(), min(i -> -i), max(), max(i -> -i))
        );

        assertEquals(
            tuple(4L, Optional.of(1), Optional.of(-4), Optional.of(4), Optional.of(-1)),
            Seq.of(1, 2, 3, 4).collect(count(), min(), min(i -> -i), max(), max(i -> -i))
        );
        
        assertEquals(
            asList("a", "b", "c"),
            Seq.of("a", "b", "c").collect(Collectors.toList())
        );
    }
    
    @Test
    public void testMergeTwoSeqs() {
        // See http://stackoverflow.com/q/16165942/521799
        Seq<Integer> s1 = Seq.of(1, 4, 9, 16);
        Seq<Integer> s2 = Seq.of(9, 7, 4, 9, 11);
        
        assertEquals(
            asList(1, 9, 4, 7, 9, 4, 16, 9),
            Seq.zip(s1, s2).flatMap(t -> Seq.of(t.v1, t.v2)).toList()
        );
    }
    
    @Test
    public void testSliding() {
        assertEquals(asList(), Seq.of().sliding(1).toList());
        assertEquals(asList(), Seq.of().sliding(2).toList());
        assertEquals(asList(), Seq.of().sliding(3).toList());
        assertEquals(asList(), Seq.of().sliding(4).toList());
        
        List<Seq<Integer>> s;
        
        // (1)
        s = Seq.of(1).sliding(1).toList();
        assertEquals(1, s.size());
        assertEquals(asList(1), s.get(0).toList());
        
        assertEquals(asList(), Seq.of(1).sliding(2).toList());
        assertEquals(asList(), Seq.of(1).sliding(3).toList());
        assertEquals(asList(), Seq.of(1).sliding(4).toList());
        
        // (1, 2)
        s = Seq.of(1, 2).sliding(1).toList();
        assertEquals(2, s.size());
        assertEquals(asList(1), s.get(0).toList());
        assertEquals(asList(2), s.get(1).toList());
        
        s = Seq.of(1, 2).sliding(2).toList();
        assertEquals(1, s.size());
        assertEquals(asList(1, 2), s.get(0).toList());
        
        assertEquals(asList(), Seq.of(1, 2).sliding(3).toList());
        assertEquals(asList(), Seq.of(1, 2).sliding(4).toList());
        
        // (1, 2, 3)
        s = Seq.of(1, 2, 3).sliding(1).toList();
        assertEquals(3, s.size());
        assertEquals(asList(1), s.get(0).toList());
        assertEquals(asList(2), s.get(1).toList());
        assertEquals(asList(3), s.get(2).toList());
        
        s = Seq.of(1, 2, 3).sliding(2).toList();
        assertEquals(2, s.size());
        assertEquals(asList(1, 2), s.get(0).toList());
        assertEquals(asList(2, 3), s.get(1).toList());
        
        s = Seq.of(1, 2, 3).sliding(3).toList();
        assertEquals(1, s.size());
        assertEquals(asList(1, 2, 3), s.get(0).toList());
        
        assertEquals(asList(), Seq.of(1, 2, 3).sliding(4).toList());
    }
    
    @Test
    public void testWindowSpecifications() {
        assertEquals(
            asList(
                tuple(0, 0, 0, 0, 4),
                tuple(1, 0, 1, 1, 2),
                tuple(2, 1, 0, 4, 0),
                tuple(3, 2, 2, 2, 3),
                tuple(4, 1, 1, 3, 1)
            ),
            Seq.of(1, 2, 4, 2, 3)
               .window(
                    Window.of(),
                    Window.of(i -> i % 2),
                    Window.of(i -> i < 3),
                    Window.of(naturalOrder()),
                    Window.of(reverseOrder())
                )
                .map(t -> tuple(
                    (int) t.v1.rowNumber(),
                    (int) t.v2.rowNumber(),
                    (int) t.v3.rowNumber(),
                    (int) t.v4.rowNumber(),
                    (int) t.v5.rowNumber()
                ))
                .toList()
        );
    }
    
    @Test
    public void testRunningTotal() {
        
        // Do the calculation from this blog post in Java
        // http://blog.jooq.org/2014/04/29/nosql-no-sql-how-to-calculate-running-totals/
        
        // | ID   | VALUE_DATE | AMOUNT |  BALANCE |
        // |------|------------|--------|----------|
        // | 9997 | 2014-03-18 |  99.17 | 19985.81 |
        // | 9981 | 2014-03-16 |  71.44 | 19886.64 |
        // | 9979 | 2014-03-16 | -94.60 | 19815.20 |
        // | 9977 | 2014-03-16 |  -6.96 | 19909.80 |
        // | 9971 | 2014-03-15 | -65.95 | 19916.76 |
        
        BigDecimal currentBalance = new BigDecimal("19985.81");
        
        assertEquals(
            asList(
                new BigDecimal("19985.81"),
                new BigDecimal("19886.64"),
                new BigDecimal("19815.20"),
                new BigDecimal("19909.80"),
                new BigDecimal("19916.76")
            ),
            Seq.of(
                    tuple(9997, "2014-03-18", new BigDecimal("99.17")),
                    tuple(9981, "2014-03-16", new BigDecimal("71.44")),
                    tuple(9979, "2014-03-16", new BigDecimal("-94.60")),
                    tuple(9977, "2014-03-16", new BigDecimal("-6.96")),
                    tuple(9971, "2014-03-15", new BigDecimal("-65.95")))
               .window(Comparator.comparing((Tuple3<Integer, String, BigDecimal> t) -> t.v1, reverseOrder()).thenComparing(t -> t.v2), Long.MIN_VALUE, -1)
               .map(w -> w.value().concat(
                    currentBalance.subtract(w.sum(t -> t.v3).orElse(BigDecimal.ZERO))
               ))
               .map(t -> t.v4)
               .toList()
        );
    }
    
    @Test
    public void testWindowFunctionRowNumber() {
        assertEquals(asList(0L, 1L, 2L, 3L, 4L), Seq.of(1, 2, 4, 2, 3).window().map(Window::rowNumber).toList());
        assertEquals(asList(0L, 1L, 4L, 2L, 3L), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::rowNumber).toList());
        assertEquals(asList(0L, 0L, 1L, 2L, 1L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(Window::rowNumber).toList());
        assertEquals(asList(0L, 0L, 2L, 1L, 1L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::rowNumber).toList());
    }
        
    @Test
    public void testWindowFunctionRank() {
        assertEquals(asList(0L, 1L, 4L, 1L, 3L), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::rank).toList());
        assertEquals(asList(0L, 0L, 2L, 0L, 1L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::rank).toList());
    }
      
    @Test
    public void testWindowFunctionDenseRank() {
        assertEquals(asList(0L, 1L, 3L, 1L, 2L), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::denseRank).toList());
        assertEquals(asList(0L, 0L, 1L, 0L, 1L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::denseRank).toList());
    }
    
    @Test
    public void testWindowFunctionPercentRank() {
        assertEquals(asList(0.0, 0.25, 1.0, 0.25, 0.75), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::percentRank).toList());
        assertEquals(asList(0.0, 0.0, 1.0, 0.0, 1.0), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::percentRank).toList());
    }
    
    @Test
    public void testWindowFunctionNtile() {
        assertEquals(asList(0L, 0L, 0L, 0L, 0L), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.ntile(1)).toList());
        assertEquals(asList(0L, 0L, 0L, 0L, 0L), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.ntile(1)).toList());
        assertEquals(asList(0L, 0L, 0L, 0L, 0L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.ntile(1)).toList());
        assertEquals(asList(0L, 0L, 0L, 0L, 0L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.ntile(1)).toList());
        
        assertEquals(asList(0L, 0L, 0L, 1L, 1L), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.ntile(2)).toList());
        assertEquals(asList(0L, 0L, 1L, 0L, 1L), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.ntile(2)).toList());
        assertEquals(asList(0L, 0L, 0L, 1L, 1L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.ntile(2)).toList());
        assertEquals(asList(0L, 0L, 1L, 0L, 1L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.ntile(2)).toList());
        
        assertEquals(asList(0L, 0L, 1L, 1L, 2L), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.ntile(3)).toList());
        assertEquals(asList(0L, 0L, 2L, 1L, 1L), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.ntile(3)).toList());
        assertEquals(asList(0L, 0L, 1L, 2L, 1L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.ntile(3)).toList());
        assertEquals(asList(0L, 0L, 2L, 1L, 1L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.ntile(3)).toList());
        
        assertEquals(asList(0L, 0L, 1L, 2L, 3L), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.ntile(4)).toList());
        assertEquals(asList(0L, 0L, 3L, 1L, 2L), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.ntile(4)).toList());
        assertEquals(asList(0L, 0L, 1L, 2L, 2L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.ntile(4)).toList());
        assertEquals(asList(0L, 0L, 2L, 1L, 2L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.ntile(4)).toList());
        
        assertEquals(asList(0L, 1L, 2L, 3L, 4L), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.ntile(5)).toList());
        assertEquals(asList(0L, 1L, 4L, 2L, 3L), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.ntile(5)).toList());
        assertEquals(asList(0L, 0L, 1L, 3L, 2L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.ntile(5)).toList());
        assertEquals(asList(0L, 0L, 3L, 1L, 2L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.ntile(5)).toList());
    }
    
    @Test
    public void testWindowFunctionLead() {
        assertEquals(optional(2, 4, 2, 3, null), Seq.of(1, 2, 4, 2, 3).window().map(Window::lead).toList());
        assertEquals(optional(3, 4, 2, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(Window::lead).toList());
        
        assertEquals(optional(2, 2, null, 3, 4), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::lead).toList());
        assertEquals(optional(3, 2, null, 4, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::lead).toList());
        
        
        assertEquals(optional(4, 2, 3, null, null), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.lead(2)).toList());
        assertEquals(optional(null, 2, null, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.lead(2)).toList());
        
        assertEquals(optional(2, 3, null, 4, null), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.lead(2)).toList());
        assertEquals(optional(null, 4, null, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.lead(2)).toList());
    }
    
    @Test
    public void testWindowFunctionLag() {
        assertEquals(optional(null, 1, 2, 4, 2), Seq.of(1, 2, 4, 2, 3).window().map(Window::lag).toList());
        assertEquals(optional(null, null, 2, 4, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(Window::lag).toList());
        
        assertEquals(optional(null, 1, 3, 2, 2), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::lag).toList());
        assertEquals(optional(null, null, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::lag).toList());
        
        
        assertEquals(optional(null, null, 1, 2, 4), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.lag(2)).toList());
        assertEquals(optional(null, null, null, 2, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.lag(2)).toList());
        
        assertEquals(optional(null, null, 2, 1, 2), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.lag(2)).toList());
        assertEquals(optional(null, null, 2, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.lag(2)).toList());
    }
    
    @Test
    public void testWindowFunctionFirstValue() {
        assertEquals(optional(1, 1, 1, 1, 1), Seq.of(1, 2, 4, 2, 3).window().map(Window::firstValue).toList());
        assertEquals(optional(1, 1, 2, 4, 2), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(Window::firstValue).toList());
        assertEquals(optional(null, 1, 1, 1, 2), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(Window::firstValue).toList());
        
        assertEquals(optional(1, 2, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(Window::firstValue).toList());
        assertEquals(optional(1, 2, 2, 4, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(Window::firstValue).toList());
        assertEquals(optional(null, null, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(Window::firstValue).toList());
        
        assertEquals(optional(1, 1, 1, 1, 1), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::firstValue).toList());
        assertEquals(optional(1, 1, 3, 2, 2), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(Window::firstValue).toList());
        assertEquals(optional(null, 1, 2, 1, 1), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(Window::firstValue).toList());
        
        assertEquals(optional(1, 2, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::firstValue).toList());
        assertEquals(optional(1, 2, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(Window::firstValue).toList());
        assertEquals(optional(null, null, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(Window::firstValue).toList());
    }
    
    @Test
    public void testWindowFunctionLastValue() {
        assertEquals(optional(3, 3, 3, 3, 3), Seq.of(1, 2, 4, 2, 3).window().map(Window::lastValue).toList());
        assertEquals(optional(2, 4, 2, 3, 3), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(Window::lastValue).toList());
        assertEquals(optional(null, 1, 2, 4, 2), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(Window::lastValue).toList());
        
        assertEquals(optional(3, 2, 2, 2, 3), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(Window::lastValue).toList());
        assertEquals(optional(3, 4, 2, 2, 3), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(Window::lastValue).toList());
        assertEquals(optional(null, null, 2, 4, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(Window::lastValue).toList());
        
        assertEquals(optional(1, 2, 4, 2, 3), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::lastValue).toList());
        assertEquals(optional(2, 2, 4, 3, 4), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(Window::lastValue).toList());
        assertEquals(optional(null, 1, 3, 2, 2), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(Window::lastValue).toList());
        
        assertEquals(optional(1, 2, 4, 2, 3), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::lastValue).toList());
        assertEquals(optional(3, 2, 4, 4, 3), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(Window::lastValue).toList());
        assertEquals(optional(null, null, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(Window::lastValue).toList());
    }
    
    @Test
    public void testWindowFunctionNthValue() {
        
        // N = 0
        assertEquals(optional(1, 1, 1, 1, 1), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.nthValue(0)).toList());
        assertEquals(optional(1, 1, 2, 4, 2), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(w -> w.nthValue(0)).toList());
        assertEquals(optional(null, 1, 1, 1, 2), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(w -> w.nthValue(0)).toList());
        
        assertEquals(optional(1, 2, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.nthValue(0)).toList());
        assertEquals(optional(1, 2, 2, 4, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(w -> w.nthValue(0)).toList());
        assertEquals(optional(null, null, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(w -> w.nthValue(0)).toList());
        
        assertEquals(optional(1, 1, 1, 1, 1), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.nthValue(0)).toList());
        assertEquals(optional(1, 1, 3, 2, 2), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(w -> w.nthValue(0)).toList());
        assertEquals(optional(null, 1, 2, 1, 1), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(w -> w.nthValue(0)).toList());
        
        assertEquals(optional(1, 2, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.nthValue(0)).toList());
        assertEquals(optional(1, 2, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(w -> w.nthValue(0)).toList());
        assertEquals(optional(null, null, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(w -> w.nthValue(0)).toList());
        

        // N = 2
        assertEquals(optional(4, 4, 4, 4, 4), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.nthValue(2)).toList());
        assertEquals(optional(null, 4, 2, 3, null), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(w -> w.nthValue(2)).toList());
        assertEquals(optional(null, null, null, 4, 2), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(w -> w.nthValue(2)).toList());
        
        assertEquals(optional(null, 2, 2, 2, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.nthValue(2)).toList());
        assertEquals(optional(null, null, 2, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(w -> w.nthValue(2)).toList());
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(w -> w.nthValue(2)).toList());
        
        assertEquals(optional(null, null, 2, 2, 2), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.nthValue(2)).toList());
        assertEquals(optional(null, 2, null, 3, 4), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(w -> w.nthValue(2)).toList());
        assertEquals(optional(null, null, 3, null, 2), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(w -> w.nthValue(2)).toList());
        
        assertEquals(optional(null, null, 4, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.nthValue(2)).toList());
        assertEquals(optional(null, null, null, 4, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(w -> w.nthValue(2)).toList());
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(w -> w.nthValue(2)).toList());
        
        
        // N = 3
        assertEquals(optional(2, 2, 2, 2, 2), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.nthValue(3)).toList());
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(w -> w.nthValue(3)).toList());
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(w -> w.nthValue(3)).toList());
        
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.nthValue(3)).toList());
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(w -> w.nthValue(3)).toList());
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(w -> w.nthValue(3)).toList());
        
        assertEquals(optional(null, null, 3, null, 3), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.nthValue(3)).toList());
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(w -> w.nthValue(3)).toList());
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(w -> w.nthValue(3)).toList());
        
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.nthValue(3)).toList());
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(w -> w.nthValue(3)).toList());
        assertEquals(optional(null, null, null, null, null), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(w -> w.nthValue(3)).toList());
    }
    
    @Test
    public void testWindowFunctionCount() {
        assertEquals(asList(5L, 5L, 5L, 5L, 5L), Seq.of(1, 2, 4, 2, 3).window().map(Window::count).toList());
        assertEquals(asList(2L, 3L, 3L, 3L, 2L), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(Window::count).toList());
        assertEquals(asList(0L, 1L, 2L, 3L, 3L), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(Window::count).toList());
        
        assertEquals(asList(2L, 3L, 3L, 3L, 2L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(Window::count).toList());
        assertEquals(asList(2L, 2L, 3L, 2L, 2L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(Window::count).toList());
        assertEquals(asList(0L, 0L, 1L, 2L, 1L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(Window::count).toList());
                
        assertEquals(asList(1L, 2L, 5L, 3L, 4L), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::count).toList());
        assertEquals(asList(2L, 3L, 2L, 3L, 3L), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(Window::count).toList());
        assertEquals(asList(0L, 1L, 3L, 2L, 3L), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(Window::count).toList());
        
        assertEquals(asList(1L, 1L, 3L, 2L, 2L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::count).toList());
        assertEquals(asList(2L, 2L, 2L, 3L, 2L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(Window::count).toList());
        assertEquals(asList(0L, 0L, 2L, 1L, 1L), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(Window::count).toList());
    }
    
    @Test
    public void testWindowFunctionSum() {
        assertEquals(optional(12, 12, 12, 12, 12), Seq.of(1, 2, 4, 2, 3).window().map(Window::sum).toList());
        assertEquals(optional(3, 7, 8, 9, 5), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(Window::sum).toList());
        assertEquals(optional(null, 1, 3, 7, 8), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(Window::sum).toList());
        
        assertEquals(optional(4, 8, 8, 8, 4), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(Window::sum).toList());
        assertEquals(optional(4, 6, 8, 6, 4), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(Window::sum).toList());
        assertEquals(optional(null, null, 2, 6, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(Window::sum).toList());
                
        assertEquals(optional(1, 3, 12, 5, 8), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::sum).toList());
        assertEquals(optional(3, 5, 7, 7, 9), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(Window::sum).toList());
        assertEquals(optional(null, 1, 7, 3, 5), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(Window::sum).toList());
        
        assertEquals(optional(1, 2, 8, 4, 4), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::sum).toList());
        assertEquals(optional(4, 4, 6, 8, 4), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(Window::sum).toList());
        assertEquals(optional(null, null, 4, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(Window::sum).toList());
    }
    
    @Test
    public void testWindowFunctionMax() {
        assertEquals(optional(4, 4, 4, 4, 4), Seq.of(1, 2, 4, 2, 3).window().map(Window::max).toList());
        assertEquals(optional(2, 4, 4, 4, 3), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(Window::max).toList());
        assertEquals(optional(null, 1, 2, 4, 4), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(Window::max).toList());
        
        assertEquals(optional(3, 4, 4, 4, 3), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(Window::max).toList());
        assertEquals(optional(3, 4, 4, 4, 3), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(Window::max).toList());
        assertEquals(optional(null, null, 2, 4, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(Window::max).toList());
                
        assertEquals(optional(1, 2, 4, 2, 3), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::max).toList());
        assertEquals(optional(2, 2, 4, 3, 4), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(Window::max).toList());
        assertEquals(optional(null, 1, 3, 2, 2), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(Window::max).toList());
        
        assertEquals(optional(1, 2, 4, 2, 3), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::max).toList());
        assertEquals(optional(3, 2, 4, 4, 3), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(Window::max).toList());
        assertEquals(optional(null, null, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(Window::max).toList());
    }
    
    @Test
    public void testWindowFunctionMin() {
        assertEquals(optional(1, 1, 1, 1, 1), Seq.of(1, 2, 4, 2, 3).window().map(Window::min).toList());
        assertEquals(optional(1, 1, 2, 2, 2), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(Window::min).toList());
        assertEquals(optional(null, 1, 1, 1, 2), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(Window::min).toList());
        
        assertEquals(optional(1, 2, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(Window::min).toList());
        assertEquals(optional(1, 2, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(Window::min).toList());
        assertEquals(optional(null, null, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(Window::min).toList());
                
        assertEquals(optional(1, 1, 1, 1, 1), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(Window::min).toList());
        assertEquals(optional(1, 1, 3, 2, 2), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(Window::min).toList());
        assertEquals(optional(null, 1, 2, 1, 1), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(Window::min).toList());
        
        assertEquals(optional(1, 2, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(Window::min).toList());
        assertEquals(optional(1, 2, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(Window::min).toList());
        assertEquals(optional(null, null, 2, 2, 1), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(Window::min).toList());
    }
    
    @Test
    public void testWindowFunctionAll() {
        assertEquals(asList(false, false, false, false, false), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.allMatch(i -> i < 4)).toList());
        assertEquals(asList(true, false, false, false, true), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(w -> w.allMatch(i -> i < 4)).toList());
        assertEquals(asList(true, true, true, false, false), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(w -> w.allMatch(i -> i < 4)).toList());

        assertEquals(asList(true, false, false, false, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.allMatch(i -> i < 4)).toList());
        assertEquals(asList(true, false, false, false, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(w -> w.allMatch(i -> i < 4)).toList());
        assertEquals(asList(true, true, true, false, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(w -> w.allMatch(i -> i < 4)).toList());

        assertEquals(asList(true, true, false, true, true), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.allMatch(i -> i < 4)).toList());
        assertEquals(asList(true, true, false, true, false), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(w -> w.allMatch(i -> i < 4)).toList());
        assertEquals(asList(true, true, true, true, true), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(w -> w.allMatch(i -> i < 4)).toList());

        assertEquals(asList(true, true, false, true, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.allMatch(i -> i < 4)).toList());
        assertEquals(asList(true, true, false, false, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(w -> w.allMatch(i -> i < 4)).toList());
        assertEquals(asList(true, true, true, true, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(w -> w.allMatch(i -> i < 4)).toList());
    }
        
    @Test
    public void testWindowFunctionAny() {
        assertEquals(asList(true, true, true, true, true), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.anyMatch(i -> i > 2)).toList());
        assertEquals(asList(false, true, true, true, true), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(w -> w.anyMatch(i -> i > 2)).toList());
        assertEquals(asList(false, false, false, true, true), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(w -> w.anyMatch(i -> i > 2)).toList());

        assertEquals(asList(true, true, true, true, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.anyMatch(i -> i > 2)).toList());
        assertEquals(asList(true, true, true, true, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(w -> w.anyMatch(i -> i > 2)).toList());
        assertEquals(asList(false, false, false, true, false), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(w -> w.anyMatch(i -> i > 2)).toList());

        assertEquals(asList(false, false, true, false, true), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.anyMatch(i -> i > 2)).toList());
        assertEquals(asList(false, false, true, true, true), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(w -> w.anyMatch(i -> i > 2)).toList());
        assertEquals(asList(false, false, true, false, false), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(w -> w.anyMatch(i -> i > 2)).toList());

        assertEquals(asList(false, false, true, false, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.anyMatch(i -> i > 2)).toList());
        assertEquals(asList(true, false, true, true, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(w -> w.anyMatch(i -> i > 2)).toList());
        assertEquals(asList(false, false, false, false, false), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(w -> w.anyMatch(i -> i > 2)).toList());
    }
           
    @Test
    public void testWindowFunctionNone() {
        assertEquals(asList(false, false, false, false, false), Seq.of(1, 2, 4, 2, 3).window().map(w -> w.noneMatch(i -> i > 2)).toList());
        assertEquals(asList(true, false, false, false, false), Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(w -> w.noneMatch(i -> i > 2)).toList());
        assertEquals(asList(true, true, true, false, false), Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(w -> w.noneMatch(i -> i > 2)).toList());

        assertEquals(asList(false, false, false, false, false), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.noneMatch(i -> i > 2)).toList());
        assertEquals(asList(false, false, false, false, false), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(w -> w.noneMatch(i -> i > 2)).toList());
        assertEquals(asList(true, true, true, false, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(w -> w.noneMatch(i -> i > 2)).toList());

        assertEquals(asList(true, true, false, true, false), Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.noneMatch(i -> i > 2)).toList());
        assertEquals(asList(true, true, false, false, false), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(w -> w.noneMatch(i -> i > 2)).toList());
        assertEquals(asList(true, true, false, true, true), Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(w -> w.noneMatch(i -> i > 2)).toList());

        assertEquals(asList(true, true, false, true, false), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.noneMatch(i -> i > 2)).toList());
        assertEquals(asList(false, true, false, false, false), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(w -> w.noneMatch(i -> i > 2)).toList());
        assertEquals(asList(true, true, true, true, true), Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(w -> w.noneMatch(i -> i > 2)).toList());
    }
          
    @Test
    public void testWindowFunctionCollect() {
        assertEquals(asList(
            asList(1, 2, 4, 2, 3), 
            asList(1, 2, 4, 2, 3), 
            asList(1, 2, 4, 2, 3), 
            asList(1, 2, 4, 2, 3), 
            asList(1, 2, 4, 2, 3)), 
            Seq.of(1, 2, 4, 2, 3).window().map(w -> w.collect(toList())).toList());
        
        assertEquals(asList(
            asList(1, 2), 
            asList(1, 2, 4), 
            asList(2, 4, 2), 
            asList(4, 2, 3), 
            asList(2, 3)), 
            Seq.of(1, 2, 4, 2, 3).window(-1, 1).map(w -> w.collect(toList())).toList());
        
        assertEquals(asList(
            asList(), 
            asList(1), 
            asList(1, 2), 
            asList(1, 2, 4), 
            asList(2, 4, 2)), 
            Seq.of(1, 2, 4, 2, 3).window(-3, -1).map(w -> w.collect(toList())).toList());

        
        assertEquals(asList(
            asList(1, 3), 
            asList(2, 4, 2), 
            asList(2, 4, 2), 
            asList(2, 4, 2), 
            asList(1, 3)),
            Seq.of(1, 2, 4, 2, 3).window(i -> i % 2).map(w -> w.collect(toList())).toList());
        
        assertEquals(asList(
            asList(1, 3), 
            asList(2, 4), 
            asList(2, 4, 2), 
            asList(4, 2), 
            asList(1, 3)),
            Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -1, 1).map(w -> w.collect(toList())).toList());
        
        assertEquals(asList(
            asList(), 
            asList(), 
            asList(2), 
            asList(2, 4), 
            asList(1)),
            Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, -3, -1).map(w -> w.collect(toList())).toList());

        
        assertEquals(asList(
            asList(1), 
            asList(1, 2), 
            asList(1, 2, 2, 3, 4), 
            asList(1, 2, 2), 
            asList(1, 2, 2, 3)),
            Seq.of(1, 2, 4, 2, 3).window(naturalOrder()).map(w -> w.collect(toList())).toList());
        
        assertEquals(asList(
            asList(1, 2), 
            asList(1, 2, 2), 
            asList(3, 4), 
            asList(2, 2, 3), 
            asList(2, 3, 4)),
            Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -1, 1).map(w -> w.collect(toList())).toList());
        
        assertEquals(asList(
            asList(), 
            asList(1), 
            asList(2, 2, 3), 
            asList(1, 2), 
            asList(1, 2, 2)),
            Seq.of(1, 2, 4, 2, 3).window(naturalOrder(), -3, -1).map(w -> w.collect(toList())).toList());

        
        assertEquals(asList(
            asList(1), 
            asList(2), 
            asList(2, 2, 4), 
            asList(2, 2), 
            asList(1, 3)),
            Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder()).map(w -> w.collect(toList())).toList());
        
        assertEquals(asList(
            asList(1, 3), 
            asList(2, 2), 
            asList(2, 4), 
            asList(2, 2, 4), 
            asList(1, 3)),
            Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -1, 1).map(w -> w.collect(toList())).toList());
        
        assertEquals(asList(
            asList(), 
            asList(), 
            asList(2, 2), 
            asList(2), 
            asList(1)),
            Seq.of(1, 2, 4, 2, 3).window(i -> i % 2, naturalOrder(), -3, -1).map(w -> w.collect(toList())).toList());
    }
    
    @SafeVarargs
    private final <T> List<Optional<T>> optional(T... list) {
        return Seq.of(list).map(Optional::ofNullable).toList();
    }
    
    @Test
    public void testSortedWithZipWithIndex() {
        assertEquals(
            asList(tuple("a", 0L), tuple("b", 1L), tuple("c", 2L)),
            Seq.of("c", "a", "b").sorted().zipWithIndex().toList()
        );

        assertEquals(
            asList(tuple("c", 0L), tuple("b", 1L), tuple("a", 2L)),
            Seq.of("c", "a", "b").sorted(reverseOrder()).zipWithIndex().toList()
        );
        
        assertEquals(
            asList(tuple("a", 1L), tuple("b", 2L), tuple("c", 0L)),
            Seq.of("c", "a", "b").zipWithIndex().sorted().toList()
        );
        
        assertEquals(
            asList(tuple("c", 0L), tuple("b", 2L), tuple("a", 1L)),
            Seq.of("c", "a", "b").zipWithIndex().sorted(reverseOrder()).toList()
        );
    }
    
    @Test
    public void testCloseStreamConcat() {
        AtomicBoolean closed1 = new AtomicBoolean();
        AtomicBoolean closed2 = new AtomicBoolean();
            
        Stream s1 = Stream.of(1, 2).onClose(() -> closed1.set(true));
        Stream s2 = Stream.of(3).onClose(() -> closed2.set(true));
        
        try (Stream s3 = Stream.concat(s1, s2)) {
            s3.collect(Collectors.toList());
        }
        
        assertTrue(closed1.get());
        assertTrue(closed2.get());
    }
    
    @Test
    public void testCloseOperationOnSingleSeq() {
        Consumer<Function<Seq<Integer>, Seq<?>>> test = f -> {
            AtomicBoolean closed1 = new AtomicBoolean();
            
            Seq<Integer> s1 = seq(Stream.of(1, 2).onClose(() -> closed1.set(true)));
            
            try (Seq<?> s2 = f.apply(s1)) {
                s2.limit(1000).collect(Collectors.toList());
            }

            assertTrue(closed1.get());
        };
        
        test.accept(s1 -> s1);
        test.accept(s1 -> s1.onEmpty(1));
        test.accept(s1 -> s1.remove(1));
        test.accept(s1 -> s1.removeAll(1, 2));
        test.accept(s1 -> s1.retainAll(1, 2));
        test.accept(s1 -> s1.cycle());
        test.accept(s1 -> s1.cycle(5));
        test.accept(s1 -> s1.distinct(i -> i));
        test.accept(s1 -> s1.zipWithIndex());
        test.accept(s1 -> s1.scanLeft(0, (a, b) -> a + b));
        test.accept(s1 -> s1.scanRight(0, (a, b) -> a + b));
        test.accept(s1 -> s1.reverse());
        test.accept(s1 -> s1.shuffle());
        test.accept(s1 -> s1.skipWhile(a -> true));
        test.accept(s1 -> s1.skipWhileClosed(a -> true));
        test.accept(s1 -> s1.skipUntil(a -> true));
        test.accept(s1 -> s1.skipUntilClosed(a -> true));
        test.accept(s1 -> s1.limitWhile(a -> true));
        test.accept(s1 -> s1.limitWhileClosed(a -> true));
        test.accept(s1 -> s1.limitUntil(a -> true));
        test.accept(s1 -> s1.limitUntilClosed(a -> true));
        test.accept(s1 -> s1.intersperse(0));
        test.accept(s1 -> s1.grouped(i -> i));
        test.accept(s1 -> s1.slice(0, 1));
        test.accept(s1 -> s1.sorted(i -> i));
        test.accept(s1 -> s1.ofType(Number.class));
        test.accept(s1 -> s1.cast(Number.class));
        test.accept(s1 -> s1.sliding(1));
        test.accept(s1 -> s1.window());
    }
    
    @Test
    public void testCloseOperationOnTwoSeqs() {
        Consumer<BiFunction<Seq<Integer>, Seq<Integer>, Seq<?>>> test = f -> {
            AtomicBoolean closed1 = new AtomicBoolean();
            AtomicBoolean closed2 = new AtomicBoolean();
            
            Seq<Integer> s1 = seq(Stream.of(1, 2).onClose(() -> closed1.set(true)));
            Seq<Integer> s2 = seq(Stream.of(3).onClose(() -> closed2.set(true)));
            
            try (Seq<?> s3 = f.apply(s1, s2)) {
                s3.limit(1000).collect(Collectors.toList());
            }

            assertTrue(closed1.get());
            assertTrue(closed2.get());
        };
        
        test.accept((s1, s2) -> s1.concat(s2));
        test.accept((s1, s2) -> s1.append(s2));
        test.accept((s1, s2) -> s1.prepend(s2));
        test.accept((s1, s2) -> s1.removeAll(s2));
        test.accept((s1, s2) -> s1.retainAll(s2));
        test.accept((s1, s2) -> s1.crossJoin(s2));
        test.accept((s1, s2) -> s1.innerJoin(s2, (a, b) -> true));
        test.accept((s1, s2) -> s1.leftOuterJoin(s2, (a, b) -> true));
        test.accept((s1, s2) -> s1.rightOuterJoin(s2, (a, b) -> true));
        test.accept((s1, s2) -> s1.zip(s2));
        test.accept((s1, s2) -> Seq.zipAll(s1, s2, 1, 2));
        test.accept((s1, s2) -> Seq.zipAll(s1, s2, 1, 2, (a, b) -> a + b));
    }
    
    @Test
    public void testCloseOperationOnFourSeqs() {
        Consumer<Function4<Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<?>>> test = f -> {
            AtomicBoolean closed1 = new AtomicBoolean();
            AtomicBoolean closed2 = new AtomicBoolean();
            AtomicBoolean closed3 = new AtomicBoolean();
            AtomicBoolean closed4 = new AtomicBoolean();

            Seq<Integer> s1 = seq(Stream.of(1, 2).onClose(() -> closed1.set(true)));
            Seq<Integer> s2 = seq(Stream.of(3).onClose(() -> closed2.set(true)));
            Seq<Integer> s3 = seq(Stream.of(1, 2).onClose(() -> closed3.set(true)));
            Seq<Integer> s4 = seq(Stream.of(3).onClose(() -> closed4.set(true)));

            try (Seq<?> s5 = f.apply(s1, s2, s3, s4)) {
                s5.limit(1000).collect(Collectors.toList());
            }
            
            assertTrue(closed1.get());
            assertTrue(closed2.get());
            assertTrue(closed3.get());
            assertTrue(closed4.get());
        };
        
        test.accept((s1, s2, s3, s4) -> Seq.crossJoin(s1, s2, s3, s4));
        test.accept((s1, s2, s3, s4) -> Seq.zip(s1, s2, s3, s4));
        test.accept((s1, s2, s3, s4) -> Seq.zipAll(s1, s2, s3, s4, 1, 2, 3, 4));
        test.accept((s1, s2, s3, s4) -> Seq.zipAll(s1, s2, s3, s4, 1, 2, 3, 4, (a, b, c, d) -> a + b + c + d));
    }

    @Test
    public void testTakeBehavesAsLimit() {
        assertTrue(Seq.range(1, 10).take(3).toList().equals(Seq.range(1, 10).limit(3).toList()));
    }

    @Test
    public void testDropBehavesAsSkip() {
        assertTrue(Seq.range(1, 10).drop(3).toList().equals(Seq.range(1, 10).skip(3).toList()));
    }

    @Test
    public void testZipAllWithSecondStreamLongerThanTheFirstOne() {
        final Seq<Integer> s1 = Seq.of(1,2,3);
        final Seq<Integer> s2 = Seq.of(1);

        final Seq<Tuple2<Integer, Integer>> expected = Seq.of(tuple(1,1),tuple(2,42),tuple(3,42));
        final Seq<Tuple2<Integer, Integer>> actual = Seq.zipAll(s1, s2, 0, 42);

        assertEquals(expected.toList(), actual.toList());
    }

    @Test
    public void testZipAllWithFirstStreamLongerThanTheSecondOne() {
        final Seq<Integer> s1 = Seq.of(1);
        final Seq<Integer> s2 = Seq.of(1, 2, 3);

        final Seq<Tuple2<Integer, Integer>> expected = Seq.of(tuple(1,1),tuple(0,2),tuple(0,3));
        final Seq<Tuple2<Integer, Integer>> actual = Seq.zipAll(s1, s2, 0, 42);

        assertEquals(expected.toList(), actual.toList());
    }

    @Test
    public void testZipAllWithSecondStreamLongerThanTheFirstOneAndCustomZipper() {
        final Seq<Integer> s1 = Seq.of(1,2,3);
        final Seq<Integer> s2 = Seq.of(1);

        final Seq<Integer> expected = Seq.of(2, 44, 45);
        final Seq<Integer> actual = Seq.zipAll(s1, s2, 0, 42, (l, r) -> l + r);

        assertEquals(expected.toList(), actual.toList());
    }

    @Test
    public void testZipAllWithFirstStreamLongerThanTheSecondOneAndCustomZipper() {
        final Seq<Integer> s1 = Seq.of(1);
        final Seq<Integer> s2 = Seq.of(1, 2, 3);

        final Seq<Integer> expected = Seq.of(2, 2, 3);
        final Seq<Integer> actual = Seq.zipAll(s1, s2, 0, 42, (l, r) -> l + r);

        assertEquals(expected.toList(), actual.toList());
    }
    
    @Test
    public void testCommonPrefix() {
        assertEquals("", Seq.of().commonPrefix());
        assertEquals("", Seq.of("").commonPrefix());
        assertEquals("", Seq.of("", "A").commonPrefix());
        assertEquals("", Seq.of("", "AA", "AAB").commonPrefix());
        assertEquals("A", Seq.of("A").commonPrefix());
        assertEquals("A", Seq.of("A", "AA", "AAB").commonPrefix());
        assertEquals("AB", Seq.of("AB", "ABC", "ABCD", "ABD").commonPrefix());
        assertEquals("AB", Seq.of("ABC", "ABCD", "ABD").commonPrefix());
        assertEquals("AABB", Seq.of("AABBCC", "AABBDD", "AABBE").commonPrefix());
    }
    
    @Test
    public void testCommonSuffix() {
        assertEquals("", Seq.of().commonSuffix());
        assertEquals("", Seq.of("").commonSuffix());
        assertEquals("", Seq.of("", "A").commonSuffix());
        assertEquals("", Seq.of("", "AA", "AAB").commonSuffix());
        assertEquals("A", Seq.of("A").commonSuffix());
        assertEquals("", Seq.of("A", "AA", "AAB").commonSuffix());
        assertEquals("", Seq.of("AB", "ABC", "ABCD", "ABD").commonSuffix());
        assertEquals("", Seq.of("ABC", "ABCD", "ABD").commonSuffix());
        assertEquals("", Seq.of("AABBCC", "AABBDD", "AABBE").commonSuffix());
        assertEquals("A", Seq.of("A", "AA", "BAA").commonSuffix());
        assertEquals("BA", Seq.of("BA", "CBA", "DCBA", "DBA").commonSuffix());
        assertEquals("BA", Seq.of("CBA", "DCBA", "DBA").commonSuffix());
        assertEquals("BBAA", Seq.of("CCBBAA", "DDBBAA", "EBBAA").commonSuffix());
    }
}
