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

import static org.jooq.lambda.Agg.allMatch;
import static org.jooq.lambda.Agg.anyMatch;
import static org.jooq.lambda.Agg.denseRank;
import static org.jooq.lambda.Agg.denseRankBy;
import static org.jooq.lambda.Agg.max;
import static org.jooq.lambda.Agg.maxBy;
import static org.jooq.lambda.Agg.median;
import static org.jooq.lambda.Agg.min;
import static org.jooq.lambda.Agg.minBy;
import static org.jooq.lambda.Agg.noneMatch;
import static org.jooq.lambda.Agg.percentRank;
import static org.jooq.lambda.Agg.percentile;
import static org.jooq.lambda.Agg.percentileBy;
import static org.jooq.lambda.Agg.rank;
import static org.jooq.lambda.Agg.rankBy;
import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.Assert.assertEquals;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple9;
import org.junit.Test;

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
    public void testPercentileWithStringsAndFunctionWithDifferentValues() {

        // In the test testPercentileWithStringsAndFunction, the values (length) of the items are all the same,
        // The function used in this test will take the first character of each string to be compared.

        Function<String, Character> getFirstLetter = s -> s.length() == 0 ? 0 : s.charAt(0);

        // Min
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileBy(0.0, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileBy(0.0, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c").collect(percentileBy(0.0, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d").collect(percentileBy(0.0, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j").collect(percentileBy(0.0, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileBy(0.0, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileBy(0.0, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileBy(0.0, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileBy(0.0, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileBy(0.0, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileBy(0.0, getFirstLetter)));

        // 0.25 percentile
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileBy(0.25, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileBy(0.25, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c").collect(percentileBy(0.25, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d").collect(percentileBy(0.25, getFirstLetter)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j").collect(percentileBy(0.25, getFirstLetter)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileBy(0.25, getFirstLetter)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileBy(0.25, getFirstLetter)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileBy(0.25, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileBy(0.25, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileBy(0.25, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileBy(0.25, getFirstLetter)));

        // Median
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileBy(0.5, getFirstLetter)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileBy(0.5, getFirstLetter)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c").collect(percentileBy(0.5, getFirstLetter)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d").collect(percentileBy(0.5, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j").collect(percentileBy(0.5, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileBy(0.5, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileBy(0.5, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileBy(0.5, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileBy(0.5, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileBy(0.5, getFirstLetter)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileBy(0.5, getFirstLetter)));

        // 0.75 percentile
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileBy(0.75, getFirstLetter)));
        assertEquals(Optional.of("b"), Stream.of("a", "b").collect(percentileBy(0.75, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c").collect(percentileBy(0.75, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d").collect(percentileBy(0.75, getFirstLetter)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j").collect(percentileBy(0.75, getFirstLetter)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileBy(0.75, getFirstLetter)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileBy(0.75, getFirstLetter)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileBy(0.75, getFirstLetter)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileBy(0.75, getFirstLetter)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileBy(0.75, getFirstLetter)));
        assertEquals(Optional.of("t"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileBy(0.75, getFirstLetter)));

        // Max
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileBy(1.0, getFirstLetter)));
        assertEquals(Optional.of("b"), Stream.of("a", "b").collect(percentileBy(1.0, getFirstLetter)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c").collect(percentileBy(1.0, getFirstLetter)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d").collect(percentileBy(1.0, getFirstLetter)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j").collect(percentileBy(1.0, getFirstLetter)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileBy(1.0, getFirstLetter)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileBy(1.0, getFirstLetter)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileBy(1.0, getFirstLetter)));
        assertEquals(Optional.of("t"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileBy(1.0, getFirstLetter)));
        assertEquals(Optional.of("u"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileBy(1.0, getFirstLetter)));
        assertEquals(Optional.of("v"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileBy(1.0, getFirstLetter)));
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

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1).collect(rankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(rankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(rankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(rankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(rankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(rankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rankBy("a", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1).collect(rankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(rankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(rankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(rankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(rankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(rankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rankBy("b", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2).collect(rankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3).collect(rankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4).collect(rankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10).collect(rankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9).collect(rankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rankBy("c", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rankBy("d", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3).collect(rankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4).collect(rankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10).collect(rankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9).collect(rankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rankBy("d", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rankBy("e", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rankBy("e", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(rankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4).collect(rankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10).collect(rankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9).collect(rankBy("e", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rankBy("e", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rankBy("e", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rankBy("e", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rankBy("e", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rankBy("e", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rankBy("f", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rankBy("f", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rankBy("f", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(rankBy("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(rankBy("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10).collect(rankBy("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9).collect(rankBy("f", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rankBy("f", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rankBy("f", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rankBy("f", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rankBy("f", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rankBy("f", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(rankBy("u", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(rankBy("u", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(rankBy("u", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(rankBy("u", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(rankBy("u", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10).collect(rankBy("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9).collect(rankBy("u", i -> strings[i])));
        assertEquals(Optional.of(7L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(rankBy("u", i -> strings[i])));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(rankBy("u", i -> strings[i])));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(rankBy("u", i -> strings[i])));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(rankBy("u", i -> strings[i])));
        assertEquals(Optional.of(8L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(rankBy("u", i -> strings[i])));
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

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1).collect(denseRankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(denseRankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(denseRankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(denseRankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(denseRankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRankBy("a", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRankBy("a", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1).collect(denseRankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2).collect(denseRankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3).collect(denseRankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4).collect(denseRankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10).collect(denseRankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRankBy("b", i -> strings[i])));
        assertEquals(Optional.of(0L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRankBy("b", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2).collect(denseRankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3).collect(denseRankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4).collect(denseRankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10).collect(denseRankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRankBy("c", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRankBy("c", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRankBy("d", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3).collect(denseRankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4).collect(denseRankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10).collect(denseRankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRankBy("d", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRankBy("d", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRankBy("e", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRankBy("e", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(denseRankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4).collect(denseRankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10).collect(denseRankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRankBy("e", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRankBy("e", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRankBy("f", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRankBy("f", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRankBy("f", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(denseRankBy("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(denseRankBy("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10).collect(denseRankBy("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRankBy("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRankBy("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRankBy("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRankBy("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRankBy("f", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRankBy("f", i -> strings[i])));

        assertEquals(Optional.empty(), Stream.<Integer> of().collect(denseRankBy("u", i -> strings[i])));
        assertEquals(Optional.of(1L), Stream.of(1).collect(denseRankBy("u", i -> strings[i])));
        assertEquals(Optional.of(2L), Stream.of(1, 2).collect(denseRankBy("u", i -> strings[i])));
        assertEquals(Optional.of(3L), Stream.of(1, 2, 3).collect(denseRankBy("u", i -> strings[i])));
        assertEquals(Optional.of(4L), Stream.of(1, 2, 3, 4).collect(denseRankBy("u", i -> strings[i])));
        assertEquals(Optional.of(5L), Stream.of(1, 2, 3, 4, 10).collect(denseRankBy("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9).collect(denseRankBy("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(denseRankBy("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(denseRankBy("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(denseRankBy("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(denseRankBy("u", i -> strings[i])));
        assertEquals(Optional.of(6L), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(denseRankBy("u", i -> strings[i])));
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
    public void testAllAnyNone() {

        // jOOL API with explicit collectors
        // ---------------------------------
        assertEquals(true, Seq.<Integer>of().collect(allMatch(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(0).collect(allMatch(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(0, 3).collect(allMatch(i -> i % 3 == 0)));
        assertEquals(false, Seq.of(0, 3, 4).collect(allMatch(i -> i % 3 == 0)));
        assertEquals(false, Seq.of(0, 3, 4, 5).collect(allMatch(i -> i % 3 == 0)));

        assertEquals(false, Seq.<Integer>of().collect(anyMatch(i -> i % 3 == 0)));
        assertEquals(false, Seq.of(1, 2).collect(anyMatch(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(1, 2, 3).collect(anyMatch(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(1, 2, 3, 4).collect(anyMatch(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(1, 2, 3, 4, 5).collect(anyMatch(i -> i % 3 == 0)));

        assertEquals(true, Seq.<Integer>of().collect(noneMatch(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(1).collect(noneMatch(i -> i % 3 == 0)));
        assertEquals(true, Seq.of(1, 2).collect(noneMatch(i -> i % 3 == 0)));
        assertEquals(false, Seq.of(1, 2, 3, 4).collect(noneMatch(i -> i % 3 == 0)));
        assertEquals(false, Seq.of(1, 2, 3, 4, 5).collect(noneMatch(i -> i % 3 == 0)));


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

    @Test
    public void testCommonPrefix() {
        assertEquals("", Seq.<String>of().collect(Agg.commonPrefix()));
        assertEquals("", Seq.of("").collect(Agg.commonPrefix()));
        assertEquals("", Seq.of("", "A").collect(Agg.commonPrefix()));
        assertEquals("", Seq.of("", "AA", "AAB").collect(Agg.commonPrefix()));
        assertEquals("A", Seq.of("A").collect(Agg.commonPrefix()));
        assertEquals("A", Seq.of("A", "AA", "AAB").collect(Agg.commonPrefix()));
        assertEquals("AB", Seq.of("AB", "ABC", "ABCD", "ABD").collect(Agg.commonPrefix()));
        assertEquals("AB", Seq.of("ABC", "ABCD", "ABD").collect(Agg.commonPrefix()));
        assertEquals("AABB", Seq.of("AABBCC", "AABBDD", "AABBE").collect(Agg.commonPrefix()));
    }

    @Test
    public void testCommonSuffix() {
        assertEquals("", Seq.<String>of().collect(Agg.commonSuffix()));
        assertEquals("", Seq.of("").collect(Agg.commonSuffix()));
        assertEquals("", Seq.of("", "A").collect(Agg.commonSuffix()));
        assertEquals("", Seq.of("", "AA", "AAB").collect(Agg.commonSuffix()));
        assertEquals("A", Seq.of("A").collect(Agg.commonSuffix()));
        assertEquals("", Seq.of("A", "AA", "AAB").collect(Agg.commonSuffix()));
        assertEquals("", Seq.of("AB", "ABC", "ABCD", "ABD").collect(Agg.commonSuffix()));
        assertEquals("", Seq.of("ABC", "ABCD", "ABD").collect(Agg.commonSuffix()));
        assertEquals("", Seq.of("AABBCC", "AABBDD", "AABBE").collect(Agg.commonSuffix()));
        assertEquals("A", Seq.of("A", "AA", "BAA").collect(Agg.commonSuffix()));
        assertEquals("BA", Seq.of("BA", "CBA", "DCBA", "DBA").collect(Agg.commonSuffix()));
        assertEquals("BA", Seq.of("CBA", "DCBA", "DBA").collect(Agg.commonSuffix()));
        assertEquals("BBAA", Seq.of("CCBBAA", "DDBBAA", "EBBAA").collect(Agg.commonSuffix()));
    }

    @Test
    public void testFilter() {
        assertEquals(0L, (long) Seq.<Integer>of().collect(Agg.filter(t -> false, Agg.count())));
        assertEquals(0L, (long) Seq.of(1).collect(Agg.filter(t -> false, Agg.count())));
        assertEquals(0L, (long) Seq.of(1, 2).collect(Agg.filter(t -> false, Agg.count())));
        assertEquals(0L, (long) Seq.of(1, 2, 3).collect(Agg.filter(t -> false, Agg.count())));
        assertEquals(0L, (long) Seq.of(1, 2, 3, 4).collect(Agg.filter(t -> false, Agg.count())));

        assertEquals(0L, (long) Seq.<Integer>of().collect(Agg.filter(t -> t % 2 == 0, Agg.count())));
        assertEquals(0L, (long) Seq.of(1).collect(Agg.filter(t -> t % 2 == 0, Agg.count())));
        assertEquals(1L, (long) Seq.of(1, 2).collect(Agg.filter(t -> t % 2 == 0, Agg.count())));
        assertEquals(1L, (long) Seq.of(1, 2, 3).collect(Agg.filter(t -> t % 2 == 0, Agg.count())));
        assertEquals(2L, (long) Seq.of(1, 2, 3, 4).collect(Agg.filter(t -> t % 2 == 0, Agg.count())));
    }

    @Test
    public void testFirst() {
        assertEquals(Optional.empty(), Seq.<Integer>of().collect(Agg.first()));
        assertEquals(Optional.of(1), Seq.of(1).collect(Agg.first()));
        assertEquals(Optional.of(2), Seq.of(2, 3).collect(Agg.first()));
        assertEquals(Optional.of(4), Seq.of(4, null).collect(Agg.first()));
        assertEquals(Optional.empty(), Seq.of(null, 5, 6).collect(Agg.first()));
    }

    @Test
    public void testLast() {
        assertEquals(Optional.empty(), Seq.<Integer>of().collect(Agg.last()));
        assertEquals(Optional.of(1), Seq.of(1).collect(Agg.last()));
        assertEquals(Optional.of(3), Seq.of(2, 3).collect(Agg.last()));
        assertEquals(Optional.of(4), Seq.of(null, 4).collect(Agg.last()));
        assertEquals(Optional.empty(), Seq.of(5, 6, null).collect(Agg.last()));
    }

    @Test
    public void testTaking() {
        assertEquals(Seq.of().toList(), Seq.of(1, 2, 3).collect(Agg.taking(0)).toList());
        assertEquals(Seq.of().toList(), Seq.of(1, 2, 3).collect(Agg.taking(-1)).toList());
        assertEquals(Seq.of(1).toList(), Seq.of(1, 2, 3).collect(Agg.taking(1)).toList());
        assertEquals(Seq.of(1, 2, 3).toList(), Seq.of(1, 2, 3, 4, 5).collect(Agg.taking(3)).toList());
        assertEquals(Seq.of(1, 2, 3, 4, 5).toList(), Seq.of(1, 2, 3, 4, 5).collect(Agg.taking(6)).toList());
        assertEquals(Seq.of("a", "b").toList(), Seq.of("a", "b", "c", "d").collect(Agg.taking(2)).toList());
    }

    @Test
    public void testDropping() {
        assertEquals(Seq.of(1, 2, 3).toList(), Seq.of(1, 2, 3).collect(Agg.dropping(0)).toList());
        assertEquals(Seq.of(1, 2, 3).toList(), Seq.of(1, 2, 3).collect(Agg.dropping(-1)).toList());
        assertEquals(Seq.of(2, 3).toList(), Seq.of(1, 2, 3).collect(Agg.dropping(1)).toList());
        assertEquals(Seq.of(4, 5).toList(), Seq.of(1, 2, 3, 4, 5).collect(Agg.dropping(3)).toList());
        assertEquals(Seq.of().toList(), Seq.of(1, 2, 3, 4, 5).collect(Agg.dropping(6)).toList());
        assertEquals(Seq.of("c", "d").toList(), Seq.of("a", "b", "c", "d").collect(Agg.dropping(2)).toList());
    }
}
