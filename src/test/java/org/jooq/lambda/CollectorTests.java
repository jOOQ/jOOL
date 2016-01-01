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

import org.junit.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static org.jooq.lambda.Agg.median;
import static org.jooq.lambda.Agg.percentileDisc;
import static org.jooq.lambda.Agg.percentileDiscBy;
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
    public void testPercentileDiscWithInts() {

        // Values can be obtained from PostgreSQL, e.g. with this query:
        // SELECT percentile_disc(0.75) WITHIN GROUP (ORDER BY a)
        // FROM unnest(array[1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22]) t(a)

        // Min
        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentileDisc(0.0)));
        assertEquals(Optional.of(1), Stream.of(1).collect(percentileDisc(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2).collect(percentileDisc(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3).collect(percentileDisc(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4).collect(percentileDisc(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10).collect(percentileDisc(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9).collect(percentileDisc(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentileDisc(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentileDisc(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentileDisc(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentileDisc(0.0)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentileDisc(0.0)));

        // 0.25 percentile
        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentileDisc(0.25)));
        assertEquals(Optional.of(1), Stream.of(1).collect(percentileDisc(0.25)));
        assertEquals(Optional.of(1), Stream.of(1, 2).collect(percentileDisc(0.25)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3).collect(percentileDisc(0.25)));
        assertEquals(Optional.of(1), Stream.of(1, 2, 3, 4).collect(percentileDisc(0.25)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3, 4, 10).collect(percentileDisc(0.25)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3, 4, 10, 9).collect(percentileDisc(0.25)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentileDisc(0.25)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentileDisc(0.25)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentileDisc(0.25)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentileDisc(0.25)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentileDisc(0.25)));

        // Median
        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentileDisc(0.5)));
        assertEquals(Optional.of(1), Stream.of(1).collect(percentileDisc(0.5)));
        assertEquals(Optional.of(1), Stream.of(1, 2).collect(percentileDisc(0.5)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3).collect(percentileDisc(0.5)));
        assertEquals(Optional.of(2), Stream.of(1, 2, 3, 4).collect(percentileDisc(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10).collect(percentileDisc(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9).collect(percentileDisc(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentileDisc(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentileDisc(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentileDisc(0.5)));
        assertEquals(Optional.of(3), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentileDisc(0.5)));
        assertEquals(Optional.of(4), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentileDisc(0.5)));

        // 0.75 percentile
        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentileDisc(0.75)));
        assertEquals(Optional.of(1) , Stream.of(1).collect(percentileDisc(0.75)));
        assertEquals(Optional.of(2) , Stream.of(1, 2).collect(percentileDisc(0.75)));
        assertEquals(Optional.of(3) , Stream.of(1, 2, 3).collect(percentileDisc(0.75)));
        assertEquals(Optional.of(3) , Stream.of(1, 2, 3, 4).collect(percentileDisc(0.75)));
        assertEquals(Optional.of(4) , Stream.of(1, 2, 3, 4, 10).collect(percentileDisc(0.75)));
        assertEquals(Optional.of(9) , Stream.of(1, 2, 3, 4, 10, 9).collect(percentileDisc(0.75)));
        assertEquals(Optional.of(9) , Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentileDisc(0.75)));
        assertEquals(Optional.of(4) , Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentileDisc(0.75)));
        assertEquals(Optional.of(9) , Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentileDisc(0.75)));
        assertEquals(Optional.of(10), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentileDisc(0.75)));
        assertEquals(Optional.of(20), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentileDisc(0.75)));

        // Max
        assertEquals(Optional.empty(), Stream.<Integer> of().collect(percentileDisc(1.0)));
        assertEquals(Optional.of(1) , Stream.of(1).collect(percentileDisc(1.0)));
        assertEquals(Optional.of(2) , Stream.of(1, 2).collect(percentileDisc(1.0)));
        assertEquals(Optional.of(3) , Stream.of(1, 2, 3).collect(percentileDisc(1.0)));
        assertEquals(Optional.of(4) , Stream.of(1, 2, 3, 4).collect(percentileDisc(1.0)));
        assertEquals(Optional.of(10), Stream.of(1, 2, 3, 4, 10).collect(percentileDisc(1.0)));
        assertEquals(Optional.of(10), Stream.of(1, 2, 3, 4, 10, 9).collect(percentileDisc(1.0)));
        assertEquals(Optional.of(10), Stream.of(1, 2, 3, 4, 10, 9, 3).collect(percentileDisc(1.0)));
        assertEquals(Optional.of(10), Stream.of(1, 2, 3, 4, 10, 9, 3, 3).collect(percentileDisc(1.0)));
        assertEquals(Optional.of(20), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20).collect(percentileDisc(1.0)));
        assertEquals(Optional.of(21), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21).collect(percentileDisc(1.0)));
        assertEquals(Optional.of(22), Stream.of(1, 2, 3, 4, 10, 9, 3, 3, 20, 21, 22).collect(percentileDisc(1.0)));

        // Illegal args
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of(1).collect(percentileDisc(-1)));
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of(1).collect(percentileDisc(2)));
    }

    @Test
    public void testPercentileDiscWithStrings() {

        // Values can be obtained from PostgreSQL, e.g. with this query:
        // SELECT percentile_disc(0.75) WITHIN GROUP (ORDER BY a)
        // FROM unnest(array['a', 'b', 'c', 'd', 'j', 'i', 'c', 'c', 't', 'u', 'v']) t(a)

        // Min
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileDisc(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileDisc(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileDisc(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c").collect(percentileDisc(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d").collect(percentileDisc(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j").collect(percentileDisc(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileDisc(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileDisc(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileDisc(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileDisc(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileDisc(0.0)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileDisc(0.0)));

        // 0.25 percentile
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileDisc(0.25)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileDisc(0.25)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileDisc(0.25)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c").collect(percentileDisc(0.25)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d").collect(percentileDisc(0.25)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j").collect(percentileDisc(0.25)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileDisc(0.25)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileDisc(0.25)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileDisc(0.25)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileDisc(0.25)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileDisc(0.25)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileDisc(0.25)));

        // Median
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileDisc(0.5)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileDisc(0.5)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileDisc(0.5)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c").collect(percentileDisc(0.5)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d").collect(percentileDisc(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j").collect(percentileDisc(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileDisc(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileDisc(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileDisc(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileDisc(0.5)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileDisc(0.5)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileDisc(0.5)));

        // 0.75 percentile
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileDisc(0.75)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileDisc(0.75)));
        assertEquals(Optional.of("b"), Stream.of("a", "b").collect(percentileDisc(0.75)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c").collect(percentileDisc(0.75)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d").collect(percentileDisc(0.75)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j").collect(percentileDisc(0.75)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileDisc(0.75)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileDisc(0.75)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileDisc(0.75)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileDisc(0.75)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileDisc(0.75)));
        assertEquals(Optional.of("t"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileDisc(0.75)));

        // Max
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileDisc(1.0)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileDisc(1.0)));
        assertEquals(Optional.of("b"), Stream.of("a", "b").collect(percentileDisc(1.0)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c").collect(percentileDisc(1.0)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d").collect(percentileDisc(1.0)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j").collect(percentileDisc(1.0)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileDisc(1.0)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileDisc(1.0)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileDisc(1.0)));
        assertEquals(Optional.of("t"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileDisc(1.0)));
        assertEquals(Optional.of("u"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileDisc(1.0)));
        assertEquals(Optional.of("v"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileDisc(1.0)));

        // Illegal args
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of("a").collect(percentileDisc(-1)));
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of("a").collect(percentileDisc(2)));
    }

    @Test
    public void testPercentileDiscWithStringsAndFunction() {

        // Values can be obtained from PostgreSQL, e.g. with this query:
        // SELECT percentile_disc(0.75) WITHIN GROUP (ORDER BY a)
        // FROM unnest(array['a', 'b', 'c', 'd', 'j', 'i', 'c', 'c', 't', 'u', 'v']) t(a)

        // Min
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileDiscBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileDiscBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileDiscBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c").collect(percentileDiscBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d").collect(percentileDiscBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j").collect(percentileDiscBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileDiscBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileDiscBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileDiscBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileDiscBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileDiscBy(0.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileDiscBy(0.0, String::length)));

        // 0.25 percentile
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileDiscBy(0.25, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileDiscBy(0.25, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileDiscBy(0.25, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c").collect(percentileDiscBy(0.25, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b", "c", "d").collect(percentileDiscBy(0.25, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j").collect(percentileDiscBy(0.25, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileDiscBy(0.25, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileDiscBy(0.25, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileDiscBy(0.25, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileDiscBy(0.25, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileDiscBy(0.25, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileDiscBy(0.25, String::length)));

        // Median
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileDiscBy(0.5, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileDiscBy(0.5, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a", "b").collect(percentileDiscBy(0.5, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c").collect(percentileDiscBy(0.5, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b", "c", "d").collect(percentileDiscBy(0.5, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j").collect(percentileDiscBy(0.5, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileDiscBy(0.5, String::length)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileDiscBy(0.5, String::length)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileDiscBy(0.5, String::length)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileDiscBy(0.5, String::length)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileDiscBy(0.5, String::length)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileDiscBy(0.5, String::length)));

        // 0.75 percentile
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileDiscBy(0.75, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileDiscBy(0.75, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b").collect(percentileDiscBy(0.75, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c").collect(percentileDiscBy(0.75, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d").collect(percentileDiscBy(0.75, String::length)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d", "j").collect(percentileDiscBy(0.75, String::length)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileDiscBy(0.75, String::length)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileDiscBy(0.75, String::length)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileDiscBy(0.75, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileDiscBy(0.75, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileDiscBy(0.75, String::length)));
        assertEquals(Optional.of("t"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileDiscBy(0.75, String::length)));

        // Max
        assertEquals(Optional.empty(), Stream.<String> of().collect(percentileDiscBy(1.0, String::length)));
        assertEquals(Optional.of("a"), Stream.of("a").collect(percentileDiscBy(1.0, String::length)));
        assertEquals(Optional.of("b"), Stream.of("a", "b").collect(percentileDiscBy(1.0, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c").collect(percentileDiscBy(1.0, String::length)));
        assertEquals(Optional.of("d"), Stream.of("a", "b", "c", "d").collect(percentileDiscBy(1.0, String::length)));
        assertEquals(Optional.of("j"), Stream.of("a", "b", "c", "d", "j").collect(percentileDiscBy(1.0, String::length)));
        assertEquals(Optional.of("i"), Stream.of("a", "b", "c", "d", "j", "i").collect(percentileDiscBy(1.0, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c").collect(percentileDiscBy(1.0, String::length)));
        assertEquals(Optional.of("c"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c").collect(percentileDiscBy(1.0, String::length)));
        assertEquals(Optional.of("t"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t").collect(percentileDiscBy(1.0, String::length)));
        assertEquals(Optional.of("u"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u").collect(percentileDiscBy(1.0, String::length)));
        assertEquals(Optional.of("v"), Stream.of("a", "b", "c", "d", "j", "i", "c", "c", "t", "u", "v").collect(percentileDiscBy(1.0, String::length)));

        // Illegal args
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of("a").collect(percentileDiscBy(-1, String::length)));
        Utils.assertThrows(IllegalArgumentException.class, () -> Stream.of("a").collect(percentileDiscBy(2, String::length)));
    }
}