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
package org.jooq.lambda.tuple;

import java.util.Optional;

/**
 * A range is a special {@link Tuple2} with two times the same type.
 * <p>
 * Ranges can be (partially) unbounded if one or both of their bounds are <code>null</code>,
 * which corresponds to "infinity", if <code>T</code> is a type that doesn't already have an
 * infinity value, such as {@link Double#POSITIVE_INFINITY} or {@link Double#NEGATIVE_INFINITY}.
 * <p>
 * Ranges are inclusive on both bounds.
 *
 * @author Lukas Eder
 */
public class Range<T extends Comparable<T>> extends Tuple2<T, T> {

    private static final long serialVersionUID = 1L;

    public Range(T lowerInclusive, T upperInclusive) {
        super(r(lowerInclusive, upperInclusive));
    }

    public Range(Tuple2<T, T> tuple) {
        this(tuple.v1, tuple.v2);
    }

    private static <T extends Comparable<T>> Tuple2<T, T> r(T t1, T t2) {
        if (t1 != null && t2 != null)
            return t1.compareTo(t2) <= 0 ? new Tuple2<>(t1, t2) : new Tuple2<>(t2, t1);
        else
            return new Tuple2<>(t1, t2);
    }

    /**
     * Whether two ranges overlap.
     * <p>
     * <pre><code>
     * // true
     * range(1, 3).overlaps(range(2, 4))
     *
     * // false
     * range(1, 3).overlaps(range(5, 8))
     * </code></pre>
     *
     * @deprecated - Use {@link #overlaps(Range)} instead.
     */
    @Deprecated
    public boolean overlaps(Tuple2<T, T> other) {
        return overlaps(new Range<>(other));
    }

    /**
     * Whether two ranges overlap.
     * <p>
     * <pre><code>
     * // true
     * range(1, 3).overlaps(range(2, 4))
     *
     * // false
     * range(1, 3).overlaps(range(5, 8))
     * </code></pre>
     */
    public boolean overlaps(Range<T> other) {
        return (v1 == null
            ||  other.v2 == null
            ||  v1.compareTo(other.v2) <= 0)
            && (v2 == null
            ||  other.v1 == null
            ||  v2.compareTo(other.v1) >= 0);
    }

    /**
     * Whether two ranges overlap.
     * <p>
     * <pre><code>
     * // true
     * range(1, 3).overlaps(2, 4)
     *
     * // false
     * range(1, 3).overlaps(5, 8)
     * </code></pre>
     */
    public boolean overlaps(T lowerInclusive, T upperInclusive) {
        return overlaps(new Range<>(lowerInclusive, upperInclusive));
    }

    /**
     * The intersection of two ranges.
     * <p>
     * <pre><code>
     * // (2, 3)
     * range(1, 3).intersect(range(2, 4))
     *
     * // none
     * range(1, 3).intersect(range(5, 8))
     * </code></pre>
     * @deprecated - Use {@link #intersect(Range)} instead.
     */
    @Deprecated
    public Optional<Range<T>> intersect(Tuple2<T, T> other) {
        return intersect(new Range<>(other));
    }

    /**
     * The intersection of two ranges.
     * <p>
     * <pre><code>
     * // (2, 3)
     * range(1, 3).intersect(range(2, 4))
     *
     * // none
     * range(1, 3).intersect(range(5, 8))
     * </code></pre>
     */
    public Optional<Range<T>> intersect(Range<T> other) {
        if (overlaps(other))
            return Optional.of(new Range<>(
                v1 == null
                    ? other.v1
                    : other.v1 == null
                    ? v1
                    : v1.compareTo(other.v1) >= 0
                    ? v1
                    : other.v1,
                v2 == null
                    ? other.v2
                    : other.v2 == null
                    ? v2
                    : v2.compareTo(other.v2) <= 0
                    ? v2
                    : other.v2
            ));
        else
            return Optional.empty();
    }

    /**
     * The intersection of two ranges.
     * <p>
     * <pre><code>
     * // (2, 3)
     * range(1, 3).intersect(2, 4)
     *
     * // none
     * range(1, 3).intersect(5, 8)
     * </code></pre>
     */
    public Optional<Range<T>> intersect(T lowerInclusive, T upperInclusive) {
        return intersect(new Range<>(lowerInclusive, upperInclusive));
    }

    /**
     * Whether a value is contained in this range.
     * <p>
     * <pre><code>
     * // true
     * range(1, 3).contains(2)
     *
     * // false
     * range(1, 3).contains(4)
     * </code></pre>
     */
    public boolean contains(T t) {
        return t != null
            && (v1 == null || v1.compareTo(t) <= 0)
            && (v2 == null || v2.compareTo(t) >= 0);
    }

    /**
     * Whether a range is contained in this range.
     * <p>
     * <pre><code>
     * // true
     * range(1, 3).contains(range(2, 3))
     *
     * // false
     * range(1, 3).contains(range(2, 4))
     * </code></pre>
     */
    public boolean contains(Range<T> other) {
        return (other.v1 == v1 || contains(other.v1))
            && (other.v2 == v2 || contains(other.v2));
    }
}