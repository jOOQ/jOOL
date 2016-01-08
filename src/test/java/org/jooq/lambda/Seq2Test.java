package org.jooq.lambda;

import org.jooq.lambda.tuple.Tuple2;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.Assert.assertEquals;

/**
 * @author Kirill Korgov (kirill@korgov.ru)
 *         08.01.2016 23:32
 */
public class Seq2Test {

    @Test
    public void testFoldLeft() throws Exception {
        final Supplier<Seq2<String, Long>> s = () -> Seq2.zip(Seq.of("a", "b", "c"), Seq.of(1L, 2L, 3L));

        assertEquals("abc", s.get().foldLeft("", (u, t1, t2) -> u.concat(t1)));
        assertEquals("-a-b-c", s.get().foldLeft(new StringBuilder(), (u, t1, t2) -> u.append("-").append(t1)).toString());
        assertEquals(3, (int) s.get().foldLeft(0, (u, t1, t2) -> u + t1.length()));
    }

    @Test
    public void testFilter() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("a", "b", "c", "d"), Seq.of(1L, 2L, 3L, 4L));
        final List<String> evenLetters = s.filter((letter, number) -> number % 2 == 0).v1s().toList();
        assertEquals(Arrays.asList("b", "d"), evenLetters);
    }

    @Test
    public void testFilter1() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("a", "b", "c", "d"), Seq.of(1L, 2L, 3L, 4L));
        final List<Long> filteredNums = s.filter1(letter -> Arrays.asList("a", "d").contains(letter)).v2s().toList();
        assertEquals(Arrays.asList(1L, 4L), filteredNums);
    }

    @Test
    public void testFilter2() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("a", "b", "c", "d"), Seq.of(1L, 2L, 3L, 4L));
        final List<String> oddLetters = s.filter2(number -> number % 2 != 0).v1s().toList();
        assertEquals(Arrays.asList("a", "c"), oddLetters);
    }

    @Test
    public void testMap() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("a", "b", "c", "d"), Seq.of(1L, 2L, 3L, 4L));
        final List<String> letterNumStrings = s.map((letter, number) -> letter + number).toList();
        assertEquals(Arrays.asList("a1", "b2", "c3", "d4"), letterNumStrings);
    }

    @Test
    public void testMap1() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("abc", "ab", "a"), Seq.of(1L, 2L, 3L));
        final List<Tuple2<Integer, Long>> lengthToNum = s.map1(String::length).toList();
        assertEquals(Arrays.asList(tuple(3, 1L), tuple(2, 2L), tuple(1, 3L)), lengthToNum);
    }

    @Test
    public void testMap2() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("abc", "ab", "a"), Seq.of(1L, 2L, 3L));
        final List<Tuple2<String, Long>> strTo100Num = s.map2(number -> number + 100L).toList();
        assertEquals(Arrays.asList(tuple("abc", 101L), tuple("ab", 102L), tuple("a", 103L)), strTo100Num);
    }

    @Test
    public void testMapToT2() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("abc", "ab", "a"), Seq.of(1L, 2L, 3L));
        final List<Tuple2<Long, String>> numToStr = s.mapToT2((str, number) -> tuple(number + 100, str)).toList();
        assertEquals(Arrays.asList(tuple(101L, "abc"), tuple(102L, "ab"), tuple(103L, "a")), numToStr);
    }

    @Test
    public void testKeys() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("abc", "ab", "a"), Seq.of(1L, 2L, 3L));
        final List<String> strs = s.keys().toList();
        assertEquals(Arrays.asList("abc", "ab", "a"), strs);
    }

    @Test
    public void testValues() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("abc", "ab", "a"), Seq.of(1L, 2L, 3L));
        final List<Long> nums = s.values().toList();
        assertEquals(Arrays.asList(1L, 2L, 3L), nums);
    }

    @Test
    public void testV1s() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("abc", "ab", "a"), Seq.of(1L, 2L, 3L));
        final List<String> strs = s.v1s().toList();
        assertEquals(Arrays.asList("abc", "ab", "a"), strs);
    }

    @Test
    public void testV2s() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("abc", "ab", "a"), Seq.of(1L, 2L, 3L));
        final List<Long> nums = s.v2s().toList();
        assertEquals(Arrays.asList(1L, 2L, 3L), nums);
    }

    @Test
    public void testToMap() throws Exception {
        final Seq2<String, Long> s = Seq2.zip(Seq.of("abc", "ab", "a"), Seq.of(1L, 2L, 3L));
        final HashMap<String, Long> expectedMap = new HashMap<String, Long>() {{
            put("abc", 1L);
            put("ab", 2L);
            put("a", 3L);
        }};
        assertEquals(expectedMap, s.toMap());
    }
}