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

import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple9;
import org.junit.Test;

import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.jooq.lambda.Agg.*;
import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.Assert.assertEquals;

/**
 * @author Lukas Eder
 */
public class CollectorTests {

    @Test
    public void testMedian() {
        assertEquals(Optional.empty(), Stream.<Integer> of().collect(median()));
        assertEquals(Optional.of(1), Stream.of(1).collect(median()));
        assertEquals(Optional.of(1), Stream.of(1, 2).collect(median()));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3).collect(median()));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3, 4).collect(median()));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10).collect(median()));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9).collect(median()));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(median()));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(median()));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(median()));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(median()));
        assertEquals(Optional.of(4), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(median()));
    }

    @Test
    public void testPercentileWithInts() {

        // Values can be obtained from PostgreSQL, e.g. with this query:
        // SELECT percentile_(0.75) WITHIN GROUP (ORDER BY a)
        // FROM unnest(array[1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22]) t(a)

        // Min
        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentile(0.0)));
        assertEquals(Optional.of(1), Stream.of(1).collect(percentile(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2).collect(percentile(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3).collect(percentile(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4).collect(percentile(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10).collect(percentile(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9).collect(percentile(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentile(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentile(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentile(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentile(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentile(0.0)));

        // 0.25 percentile
        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentile(0.25)));
        assertEquals(Optional.of(1), Stream.of(1).collect(percentile(0.25)));
        assertEquals(Optional.of(1), Stream.of(1, 2).collect(percentile(0.25)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3).collect(percentile(0.25)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4).collect(percentile(0.25)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3, 4, 10).collect(percentile(0.25)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3, 4, 10, 9).collect(percentile(0.25)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentile(0.25)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentile(0.25)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentile(0.25)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentile(0.25)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentile(0.25)));

        // Median
        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentile(0.5)));
        assertEquals(Optional.of(1), Stream.of(1).collect(percentile(0.5)));
        assertEquals(Optional.of(1), Stream.of(1, 2).collect(percentile(0.5)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3).collect(percentile(0.5)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3, 4).collect(percentile(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10).collect(percentile(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9).collect(percentile(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentile(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentile(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentile(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentile(0.5)));
        assertEquals(Optional.of(4), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentile(0.5)));

        // 0.75 percentile
        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentile(0.75)));
        assertEquals(Optional.of(1) , Stream.of(1).collect(percentile(0.75)));
        assertEquals(Optional.of(2) , Stream.of(1, 2).collect(percentile(0.75)));
        assertEquals(Optional.of(3) , Stream.of(1, 2, 3).collect(percentile(0.75)));
        assertEquals(Optional.of(3) , Stream.of(1, 2, 3, 4).collect(percentile(0.75)));
        assertEquals(Optional.of(4) , Stream.of(1, 2, 3, 4, 10).collect(percentile(0.75)));
        assertEquals(Optional.of(9) , Stream.of(1, 2, 3, 4, 10, 9).collect(percentile(0.75)));
        assertEquals(Optional.of(9) , Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentile(0.75)));
        assertEquals(Optional.of(4) , Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentile(0.75)));
        assertEquals(Optional.of(9) , Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentile(0.75)));
        assertEquals(Optional.of(10), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentile(0.75)));
        assertEquals(Optional.of(20), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentile(0.75)));

        // Max
        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentile(1.0)));
        assertEquals(Optional.of(1) , Stream.of(1).collect(percentile(1.0)));
        assertEquals(Optional.of(2) , Stream.of(1, 2).collect(percentile(1.0)));
        assertEquals(Optional.of(3) , Stream.of(1, 2, 3).collect(percentile(1.0)));
        assertEquals(Optional.of(4) , Stream.of(1, 2, 3, 4).collect(percentile(1.0)));
        assertEquals(Optional.of(10), Stream.of(1, 2, 3, 4, 10).collect(percentile(1.0)));
        assertEquals(Optional.of(10), Stream.of(1, 2, 3, 4, 10, 9).collect(percentile(1.0)));
        assertEquals(Optional.of(10), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentile(1.0)));
        assertEquals(Optional.of(10), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentile(1.0)));
        assertEquals(Optional.of(20), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentile(1.0)));
        assertEquals(Optional.of(21), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentile(1.0)));
        assertEquals(Optional.of(22), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentile(1.0)));

        // Illegal args
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of(1).collect(percentile(-1)));
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of(1).collect(percentile(2)));
    }

    @Test
    public void testPercentileWithStrings() {

        // Values can be obtained from PostgreSQL, e.g. with this query:
        // SELECT percentile_(0.75) WITHIN GROUP (ORDER BY a)
        // FROM unnest(array['a', 'b', 'c', 'd', 'j', 'i', 'c', 'c', 't', 'u', 'v']) t(a)

        // Min
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentile(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentile(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentile(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c").collect(percentile(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d").collect(percentile(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j").collect(percentile(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentile(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentile(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentile(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentile(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentile(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentile(0.0)));

        // 0.25 percentile
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentile(0.25)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentile(0.25)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentile(0.25)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c").collect(percentile(0.25)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d").collect(percentile(0.25)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j").collect(percentile(0.25)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentile(0.25)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentile(0.25)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentile(0.25)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentile(0.25)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentile(0.25)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentile(0.25)));

        // Median
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentile(0.5)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentile(0.5)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentile(0.5)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c").collect(percentile(0.5)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d").collect(percentile(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j").collect(percentile(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentile(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentile(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentile(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentile(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentile(0.5)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentile(0.5)));

        // 0.75 percentile
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentile(0.75)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentile(0.75)));
        assertEquals(Optional.of("b"), Stream.of("a", "b").collect(percentile(0.75)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c").collect(percentile(0.75)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d").collect(percentile(0.75)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j").collect(percentile(0.75)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentile(0.75)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentile(0.75)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentile(0.75)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentile(0.75)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentile(0.75)));
        assertEquals(Optional.of("t"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentile(0.75)));

        // Max
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentile(1.0)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentile(1.0)));
        assertEquals(Optional.of("b"), Stream.of("a", "b").collect(percentile(1.0)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c").collect(percentile(1.0)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d").collect(percentile(1.0)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j").collect(percentile(1.0)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentile(1.0)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentile(1.0)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentile(1.0)));
        assertEquals(Optional.of("t"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentile(1.0)));
        assertEquals(Optional.of("u"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentile(1.0)));
        assertEquals(Optional.of("v"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentile(1.0)));

        // Illegal args
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of("a").collect(percentile(-1)));
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of("a").collect(percentile(2)));
    }

    @Test
    public void testPercentileWithStringsAndFunction() {

        // Values can be obtained from PostgreSQL, e.g. with this query:
        // SELECT percentile_(0.75) WITHIN GROUP (ORDER BY a)
        // FROM unnest(array['a', 'b', 'c', 'd', 'j', 'i', 'c', 'c', 't', 'u', 'v']) t(a)

        // Min
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c").collect(percentileBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d").collect(percentileBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j").collect(percentileBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileBy(0.0, String::length)));

        // 0.25 percentile
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileBy(0.25, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileBy(0.25, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileBy(0.25, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c").collect(percentileBy(0.25, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d").collect(percentileBy(0.25, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j").collect(percentileBy(0.25, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileBy(0.25, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileBy(0.25, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileBy(0.25, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileBy(0.25, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileBy(0.25, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileBy(0.25, String::length)));

        // Median
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileBy(0.5, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileBy(0.5, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileBy(0.5, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c").collect(percentileBy(0.5, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d").collect(percentileBy(0.5, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j").collect(percentileBy(0.5, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileBy(0.5, String::length)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileBy(0.5, String::length)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileBy(0.5, String::length)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileBy(0.5, String::length)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileBy(0.5, String::length)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileBy(0.5, String::length)));

        // 0.75 percentile
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileBy(0.75, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileBy(0.75, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b").collect(percentileBy(0.75, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c").collect(percentileBy(0.75, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d").collect(percentileBy(0.75, String::length)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j").collect(percentileBy(0.75, String::length)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileBy(0.75, String::length)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileBy(0.75, String::length)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileBy(0.75, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileBy(0.75, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileBy(0.75, String::length)));
        assertEquals(Optional.of("t"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileBy(0.75, String::length)));

        // Max
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileBy(1.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileBy(1.0, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b").collect(percentileBy(1.0, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c").collect(percentileBy(1.0, String::length)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d").collect(percentileBy(1.0, String::length)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j").collect(percentileBy(1.0, String::length)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileBy(1.0, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileBy(1.0, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileBy(1.0, String::length)));
        assertEquals(Optional.of("t"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileBy(1.0, String::length)));
        assertEquals(Optional.of("u"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileBy(1.0, String::length)));
        assertEquals(Optional.of("v"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileBy(1.0, String::length)));

        // Illegal args
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of("a").collect(percentileBy(-1, String::length)));
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of("a").collect(percentileBy(2, String::length)));
    }

    @Test
    public void testRank() {

        // Values can be obtained from PostgreSQL, e.g. with this query:
        // SELECT rank(20) WITHIN GROUP (ORDER BY a)
        // FROM unnest(array[1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22]) t(a)

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank(0)));
        assertEquals(Optional.of(0L), Stream.of(1).collect(rank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(rank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(rank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(rank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(rank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank(0)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank(1)));
        assertEquals(Optional.of(0L), Stream.of(1).collect(rank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(rank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(rank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(rank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(rank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank(1)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank(2)));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2).collect(rank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3).collect(rank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4).collect(rank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10).collect(rank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank(2)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank(3)));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3).collect(rank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4).collect(rank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10).collect(rank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank(3)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank(4)));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rank(4)));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(rank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4).collect(rank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10).collect(rank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank(4)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank(4)));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank(4)));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank(4)));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank(4)));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank(4)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank(5)));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rank(5)));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rank(5)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(rank(5)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(rank(5)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10).collect(rank(5)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank(5)));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank(5)));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank(5)));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank(5)));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank(5)));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank(5)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank(20)));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rank(20)));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rank(20)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(rank(20)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(rank(20)));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10).collect(rank(20)));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank(20)));
        assertEquals(Optional.of(7L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank(20)));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank(20)));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank(20)));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank(20)));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank(20)));
    }

    @Test
    public void testRankWithFunction() {
        String[] strings = Seq.rangeClosed('a', 'z').map(Object::toString).toArray(String[]::new);

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1).collect(rank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(rank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(rank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(rank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(rank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank("a", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1).collect(rank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(rank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(rank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(rank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(rank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank("b", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2).collect(rank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3).collect(rank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4).collect(rank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10).collect(rank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank("c", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank("d", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3).collect(rank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4).collect(rank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10).collect(rank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank("d", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank("e", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rank("e", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(rank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4).collect(rank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10).collect(rank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank("e", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank("e", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank("e", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank("e", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank("e", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank("e", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank("f", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rank("f", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rank("f", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(rank("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(rank("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10).collect(rank("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank("f", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank("f", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank("f", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank("f", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank("f", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank("f", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rank("u", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rank("u", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rank("u", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(rank("u", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(rank("u", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10).collect(rank("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9).collect(rank("u", i -> strings[i])));
        assertEquals(Optional.of(7L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rank("u", i -> strings[i])));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rank("u", i -> strings[i])));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rank("u", i -> strings[i])));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rank("u", i -> strings[i])));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rank("u", i -> strings[i])));
    }

    @Test
    public void testDenseRank() {

        // Values can be obtained from PostgreSQL, e.g. with this query:
        // SELECT dense_rank(20) WITHIN GROUP (ORDER BY a)
        // FROM unnest(array[1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22]) t(a)

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank(0)));
        assertEquals(Optional.of(0L), Stream.of(1).collect(denseRank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(denseRank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(denseRank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(denseRank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(denseRank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank(0)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank(0)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank(1)));
        assertEquals(Optional.of(0L), Stream.of(1).collect(denseRank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(denseRank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(denseRank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(denseRank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(denseRank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank(1)));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank(1)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank(2)));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2).collect(denseRank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3).collect(denseRank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4).collect(denseRank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10).collect(denseRank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank(2)));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank(2)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank(3)));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3).collect(denseRank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4).collect(denseRank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10).collect(denseRank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank(3)));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank(3)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank(4)));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRank(4)));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(denseRank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4).collect(denseRank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10).collect(denseRank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank(4)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank(4)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank(5)));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRank(5)));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRank(5)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(denseRank(5)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(denseRank(5)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10).collect(denseRank(5)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank(5)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank(5)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank(5)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank(5)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank(5)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank(5)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank(20)));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRank(20)));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRank(20)));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(denseRank(20)));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(denseRank(20)));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10).collect(denseRank(20)));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank(20)));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank(20)));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank(20)));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank(20)));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank(20)));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank(20)));
    }

    @Test
    public void testDenseRankWithFunction() {
        String[] strings = Seq.rangeClosed('a', 'z').map(Object::toString).toArray(String[]::new);

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1).collect(denseRank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(denseRank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(denseRank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(denseRank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(denseRank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank("a", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1).collect(denseRank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(denseRank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(denseRank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(denseRank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(denseRank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank("b", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2).collect(denseRank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3).collect(denseRank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4).collect(denseRank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10).collect(denseRank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank("c", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank("d", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3).collect(denseRank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4).collect(denseRank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10).collect(denseRank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank("d", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank("e", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRank("e", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(denseRank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4).collect(denseRank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10).collect(denseRank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank("e", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank("f", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRank("f", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRank("f", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(denseRank("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(denseRank("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10).collect(denseRank("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank("f", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRank("u", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRank("u", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRank("u", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(denseRank("u", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(denseRank("u", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10).collect(denseRank("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRank("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRank("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRank("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRank("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRank("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRank("u", i -> strings[i])));
    }
    
    @Test
    public void testPercentRank() {

        // Values can be obtained from PostgreSQ.0 / x, e.g. with this query:
        // SE.0 / xECT percent_rank(20) WITHIN GROUP (ORDER BY a)
        // FROM unnest(array[1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22]) t(a)

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentRank(0)));
        assertEquals(Optional.of(0.0), Stream.of(1).collect(percentRank(0)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2).collect(percentRank(0)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3).collect(percentRank(0)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4).collect(percentRank(0)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10).collect(percentRank(0)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9).collect(percentRank(0)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentRank(0)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentRank(0)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentRank(0)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentRank(0)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentRank(0)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentRank(1)));
        assertEquals(Optional.of(0.0), Stream.of(1).collect(percentRank(1)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2).collect(percentRank(1)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3).collect(percentRank(1)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4).collect(percentRank(1)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10).collect(percentRank(1)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9).collect(percentRank(1)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentRank(1)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentRank(1)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentRank(1)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentRank(1)));
        assertEquals(Optional.of(0.0), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentRank(1)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentRank(2)));
        assertEquals(Optional.of(1.0 / 1), Stream.of(1).collect(percentRank(2)));
        assertEquals(Optional.of(1.0 / 2), Stream.of(1, 2).collect(percentRank(2)));
        assertEquals(Optional.of(1.0 / 3), Stream.of(1, 2, 3).collect(percentRank(2)));
        assertEquals(Optional.of(1.0 / 4), Stream.of(1, 2, 3, 4).collect(percentRank(2)));
        assertEquals(Optional.of(1.0 / 5), Stream.of(1, 2, 3, 4, 10).collect(percentRank(2)));
        assertEquals(Optional.of(1.0 / 6), Stream.of(1, 2, 3, 4, 10, 9).collect(percentRank(2)));
        assertEquals(Optional.of(1.0 / 7), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentRank(2)));
        assertEquals(Optional.of(1.0 / 8), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentRank(2)));
        assertEquals(Optional.of(1.0 / 9), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentRank(2)));
        assertEquals(Optional.of(1.0 /10), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentRank(2)));
        assertEquals(Optional.of(1.0 /11), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentRank(2)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentRank(3)));
        assertEquals(Optional.of(1.0 / 1), Stream.of(1).collect(percentRank(3)));
        assertEquals(Optional.of(2.0 / 2), Stream.of(1, 2).collect(percentRank(3)));
        assertEquals(Optional.of(2.0 / 3), Stream.of(1, 2, 3).collect(percentRank(3)));
        assertEquals(Optional.of(2.0 / 4), Stream.of(1, 2, 3, 4).collect(percentRank(3)));
        assertEquals(Optional.of(2.0 / 5), Stream.of(1, 2, 3, 4, 10).collect(percentRank(3)));
        assertEquals(Optional.of(2.0 / 6), Stream.of(1, 2, 3, 4, 10, 9).collect(percentRank(3)));
        assertEquals(Optional.of(2.0 / 7), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentRank(3)));
        assertEquals(Optional.of(2.0 / 8), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentRank(3)));
        assertEquals(Optional.of(2.0 / 9), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentRank(3)));
        assertEquals(Optional.of(2.0 /10), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentRank(3)));
        assertEquals(Optional.of(2.0 /11), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentRank(3)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentRank(4)));
        assertEquals(Optional.of(1.0 / 1), Stream.of(1).collect(percentRank(4)));
        assertEquals(Optional.of(2.0 / 2), Stream.of(1, 2).collect(percentRank(4)));
        assertEquals(Optional.of(3.0 / 3), Stream.of(1, 2, 3).collect(percentRank(4)));
        assertEquals(Optional.of(3.0 / 4), Stream.of(1, 2, 3, 4).collect(percentRank(4)));
        assertEquals(Optional.of(3.0 / 5), Stream.of(1, 2, 3, 4, 10).collect(percentRank(4)));
        assertEquals(Optional.of(3.0 / 6), Stream.of(1, 2, 3, 4, 10, 9).collect(percentRank(4)));
        assertEquals(Optional.of(4.0 / 7), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentRank(4)));
        assertEquals(Optional.of(5.0 / 8), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentRank(4)));
        assertEquals(Optional.of(5.0 / 9), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentRank(4)));
        assertEquals(Optional.of(5.0 /10), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentRank(4)));
        assertEquals(Optional.of(5.0 /11), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentRank(4)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentRank(5)));
        assertEquals(Optional.of(1.0 / 1), Stream.of(1).collect(percentRank(5)));
        assertEquals(Optional.of(2.0 / 2), Stream.of(1, 2).collect(percentRank(5)));
        assertEquals(Optional.of(3.0 / 3), Stream.of(1, 2, 3).collect(percentRank(5)));
        assertEquals(Optional.of(4.0 / 4), Stream.of(1, 2, 3, 4).collect(percentRank(5)));
        assertEquals(Optional.of(4.0 / 5), Stream.of(1, 2, 3, 4, 10).collect(percentRank(5)));
        assertEquals(Optional.of(4.0 / 6), Stream.of(1, 2, 3, 4, 10, 9).collect(percentRank(5)));
        assertEquals(Optional.of(5.0 / 7), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentRank(5)));
        assertEquals(Optional.of(6.0 / 8), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentRank(5)));
        assertEquals(Optional.of(6.0 / 9), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentRank(5)));
        assertEquals(Optional.of(6.0 /10), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentRank(5)));
        assertEquals(Optional.of(6.0 /11), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentRank(5)));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentRank(20)));
        assertEquals(Optional.of(1.0 / 1), Stream.of(1).collect(percentRank(20)));
        assertEquals(Optional.of(2.0 / 2), Stream.of(1, 2).collect(percentRank(20)));
        assertEquals(Optional.of(3.0 / 3), Stream.of(1, 2, 3).collect(percentRank(20)));
        assertEquals(Optional.of(4.0 / 4), Stream.of(1, 2, 3, 4).collect(percentRank(20)));
        assertEquals(Optional.of(5.0 / 5), Stream.of(1, 2, 3, 4, 10).collect(percentRank(20)));
        assertEquals(Optional.of(6.0 / 6), Stream.of(1, 2, 3, 4, 10, 9).collect(percentRank(20)));
        assertEquals(Optional.of(7.0 / 7), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentRank(20)));
        assertEquals(Optional.of(8.0 / 8), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentRank(20)));
        assertEquals(Optional.of(8.0 / 9), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentRank(20)));
        assertEquals(Optional.of(8.0 /10), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentRank(20)));
        assertEquals(Optional.of(8.0 /11), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentRank(20)));
    }

    @Test
    public void testRanksCombined() {

        // javac cannot compile this with reasonable speeds, if the type needs to be inferred on the collect() call
        Collector<Integer, ?, Tuple9<
            Optional<Long>,
            Optional<Long>,
            Optional<Long>,
            Optional<Long>,
            Optional<Long>,
            Optional<Long>,
            Optional<Double>,
            Optional<Double>,
            Optional<Double>
        >> collectors =
        Tuple.collectors(
            rank(0),
            rank(10),
            rank(20),
            denseRank(0),
            denseRank(10),
            denseRank(20),
            percentRank(0),
            percentRank(10),
            percentRank(20)
        );

        assertEquals(
            tuple(
                Optional.of(0L),
                Optional.of(7L),
                Optional.of(8L),
                Optional.of(0L),
                Optional.of(5L),
                Optional.of(6L),
                Optional.of(0.0 / 11),
                Optional.of(7.0 / 11),
                Optional.of(8.0 / 11)
            ),
            Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(collectors)
        );
    }

    @Test
    public void testMinMax() {
        assertEquals(Optional.empty(), Seq.<Integer>of().collect(min()));
        assertEquals(Optional.empty(), Seq.<Integer>of().collect(max()));
        assertEquals(Optional.empty(), Seq.<Integer>of().collect(minBy(i -> -i)));
        assertEquals(Optional.empty(), Seq.<Integer>of().collect(maxBy(i -> -i)));

        assertEquals(Optional.of(1), Seq.of(1).collect(min()));
        assertEquals(Optional.of(1), Seq.of(1).collect(max()));
        assertEquals(Optional.of(1), Seq.of(1).collect(minBy(i -> -i)));
        assertEquals(Optional.of(1), Seq.of(1).collect(maxBy(i -> -i)));

        assertEquals(Optional.of(1), Seq.of(1, 2).collect(min()));
        assertEquals(Optional.of(2), Seq.of(1, 2).collect(max()));
        assertEquals(Optional.of(2), Seq.of(1, 2).collect(minBy(i -> -i)));
        assertEquals(Optional.of(1), Seq.of(1, 2).collect(maxBy(i -> -i)));

        assertEquals(Optional.of(1), Seq.of(1, 2, 3).collect(min()));
        assertEquals(Optional.of(3), Seq.of(1, 2, 3).collect(max()));
        assertEquals(Optional.of(3), Seq.of(1, 2, 3).collect(minBy(i -> -i)));
        assertEquals(Optional.of(1), Seq.of(1, 2, 3).collect(maxBy(i -> -i)));
    }

    @Test
    public void testAny() {
        assertEquals(Optional.empty(), Seq.<Integer>of().collect(anySomeOtherNameNeededHere()));
        assertEquals(Optional.of(1), Seq.of(1).collect(anySomeOtherNameNeededHere()));
        assertEquals(Optional.of(1), Seq.of(1, 2).collect(anySomeOtherNameNeededHere()));
    }
    
    @Test
    public void testAllAnyNone() {
        
        // jOOL API with explicit collectors
        // ---------------------------------
        assertEquals(true, Seq.<Integer>of().collect(allBy(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(0).collect(allBy(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(0, 3).collect(allBy(i -> i % 3 == 0)));
        assertEquals(false, Seq.of(0, 3, 4).collect(allBy(i -> i % 3 == 0)));
        assertEquals(false, Seq.of(0, 3, 4, 5).collect(allBy(i -> i % 3 == 0)));
        
        assertEquals(false, Seq.<Integer>of().collect(anyBy(i -> i % 3 == 0)));
        assertEquals(false, Seq.of(1, 2).collect(anyBy(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(1, 2, 3).collect(anyBy(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(1, 2, 3, 4).collect(anyBy(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(1, 2, 3, 4, 5).collect(anyBy(i -> i % 3 == 0)));
        
        assertEquals(true, Seq.<Integer>of().collect(noneBy(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(1).collect(noneBy(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(1, 2).collect(noneBy(i -> i % 3 == 0)));
        assertEquals(false, Seq.of(1, 2, 3, 4).collect(noneBy(i -> i % 3 == 0)));
        assertEquals(false, Seq.of(1, 2, 3, 4, 5).collect(noneBy(i -> i % 3 == 0)));
        
        
        // Stream API with implicit collectors
        // -----------------------------------
        assertEquals(true, Seq.<Integer>of().allMatch(i -> i % 3 == 0));
        assertEquals(true, Seq.of(0).allMatch(i -> i % 3 == 0));
        assertEquals(true, Seq.of(0, 3).allMatch(i -> i % 3 == 0));
        assertEquals(false, Seq.of(0, 3, 4).allMatch(i -> i % 3 == 0));
        assertEquals(false, Seq.of(0, 3, 4, 5).allMatch(i -> i % 3 == 0));
        
        assertEquals(false, Seq.<Integer>of().anyMatch(i -> i % 3 == 0));
        assertEquals(false, Seq.of(1, 2).anyMatch(i -> i % 3 == 0));
        assertEquals(true, Seq.of(1, 2, 3).anyMatch(i -> i % 3 == 0));
        assertEquals(true, Seq.of(1, 2, 3, 4).anyMatch(i -> i % 3 == 0));
        assertEquals(true, Seq.of(1, 2, 3, 4, 5).anyMatch(i -> i % 3 == 0));
        
        assertEquals(true, Seq.<Integer>of().noneMatch(i -> i % 3 == 0));
        assertEquals(true, Seq.of(1).noneMatch(i -> i % 3 == 0));
        assertEquals(true, Seq.of(1, 2).noneMatch(i -> i % 3 == 0));
        assertEquals(false, Seq.of(1, 2, 3, 4).noneMatch(i -> i % 3 == 0));
        assertEquals(false, Seq.of(1, 2, 3, 4, 5).noneMatch(i -> i % 3 == 0));
    }
}