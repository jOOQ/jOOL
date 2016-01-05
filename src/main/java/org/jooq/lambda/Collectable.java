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

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple10;
import org.jooq.lambda.tuple.Tuple11;
import org.jooq.lambda.tuple.Tuple12;
import org.jooq.lambda.tuple.Tuple13;
import org.jooq.lambda.tuple.Tuple14;
import org.jooq.lambda.tuple.Tuple15;
import org.jooq.lambda.tuple.Tuple16;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.jooq.lambda.tuple.Tuple4;
import org.jooq.lambda.tuple.Tuple5;
import org.jooq.lambda.tuple.Tuple6;
import org.jooq.lambda.tuple.Tuple7;
import org.jooq.lambda.tuple.Tuple8;
import org.jooq.lambda.tuple.Tuple9;

/**
 * A common super type for types like {@link Seq} or {@link Window} delegating
 * common aggregation functions to {@link #collect(Collector)}
 *
 * @author Lukas Eder
 */
public interface Collectable<T> {

    // Collect overloads
    // -----------------

    /**
     * Collect this collectable.
     */
    <R, A> R collect(Collector<? super T, A, R> collector);

    // [jooq-tools] START [collect]

    /**
     * Collect this collectable into 2 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, A1, A2> Tuple2<R1, R2> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2
    ) {
        return collect(Tuple.collectors(collector1, collector2));
    }

    /**
     * Collect this collectable into 3 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, A1, A2, A3> Tuple3<R1, R2, R3> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3));
    }

    /**
     * Collect this collectable into 4 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, A1, A2, A3, A4> Tuple4<R1, R2, R3, R4> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4));
    }

    /**
     * Collect this collectable into 5 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, A1, A2, A3, A4, A5> Tuple5<R1, R2, R3, R4, R5> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5));
    }

    /**
     * Collect this collectable into 6 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, R6, A1, A2, A3, A4, A5, A6> Tuple6<R1, R2, R3, R4, R5, R6> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5,
        Collector<? super T, A6, R6> collector6
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5, collector6));
    }

    /**
     * Collect this collectable into 7 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, R6, R7, A1, A2, A3, A4, A5, A6, A7> Tuple7<R1, R2, R3, R4, R5, R6, R7> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5,
        Collector<? super T, A6, R6> collector6,
        Collector<? super T, A7, R7> collector7
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5, collector6, collector7));
    }

    /**
     * Collect this collectable into 8 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, R6, R7, R8, A1, A2, A3, A4, A5, A6, A7, A8> Tuple8<R1, R2, R3, R4, R5, R6, R7, R8> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5,
        Collector<? super T, A6, R6> collector6,
        Collector<? super T, A7, R7> collector7,
        Collector<? super T, A8, R8> collector8
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5, collector6, collector7, collector8));
    }

    /**
     * Collect this collectable into 9 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, R6, R7, R8, R9, A1, A2, A3, A4, A5, A6, A7, A8, A9> Tuple9<R1, R2, R3, R4, R5, R6, R7, R8, R9> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5,
        Collector<? super T, A6, R6> collector6,
        Collector<? super T, A7, R7> collector7,
        Collector<? super T, A8, R8> collector8,
        Collector<? super T, A9, R9> collector9
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5, collector6, collector7, collector8, collector9));
    }

    /**
     * Collect this collectable into 10 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10> Tuple10<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5,
        Collector<? super T, A6, R6> collector6,
        Collector<? super T, A7, R7> collector7,
        Collector<? super T, A8, R8> collector8,
        Collector<? super T, A9, R9> collector9,
        Collector<? super T, A10, R10> collector10
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5, collector6, collector7, collector8, collector9, collector10));
    }

    /**
     * Collect this collectable into 11 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11> Tuple11<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5,
        Collector<? super T, A6, R6> collector6,
        Collector<? super T, A7, R7> collector7,
        Collector<? super T, A8, R8> collector8,
        Collector<? super T, A9, R9> collector9,
        Collector<? super T, A10, R10> collector10,
        Collector<? super T, A11, R11> collector11
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5, collector6, collector7, collector8, collector9, collector10, collector11));
    }

    /**
     * Collect this collectable into 12 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12> Tuple12<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5,
        Collector<? super T, A6, R6> collector6,
        Collector<? super T, A7, R7> collector7,
        Collector<? super T, A8, R8> collector8,
        Collector<? super T, A9, R9> collector9,
        Collector<? super T, A10, R10> collector10,
        Collector<? super T, A11, R11> collector11,
        Collector<? super T, A12, R12> collector12
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5, collector6, collector7, collector8, collector9, collector10, collector11, collector12));
    }

    /**
     * Collect this collectable into 13 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13> Tuple13<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5,
        Collector<? super T, A6, R6> collector6,
        Collector<? super T, A7, R7> collector7,
        Collector<? super T, A8, R8> collector8,
        Collector<? super T, A9, R9> collector9,
        Collector<? super T, A10, R10> collector10,
        Collector<? super T, A11, R11> collector11,
        Collector<? super T, A12, R12> collector12,
        Collector<? super T, A13, R13> collector13
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5, collector6, collector7, collector8, collector9, collector10, collector11, collector12, collector13));
    }

    /**
     * Collect this collectable into 14 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14> Tuple14<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5,
        Collector<? super T, A6, R6> collector6,
        Collector<? super T, A7, R7> collector7,
        Collector<? super T, A8, R8> collector8,
        Collector<? super T, A9, R9> collector9,
        Collector<? super T, A10, R10> collector10,
        Collector<? super T, A11, R11> collector11,
        Collector<? super T, A12, R12> collector12,
        Collector<? super T, A13, R13> collector13,
        Collector<? super T, A14, R14> collector14
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5, collector6, collector7, collector8, collector9, collector10, collector11, collector12, collector13, collector14));
    }

    /**
     * Collect this collectable into 15 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15> Tuple15<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5,
        Collector<? super T, A6, R6> collector6,
        Collector<? super T, A7, R7> collector7,
        Collector<? super T, A8, R8> collector8,
        Collector<? super T, A9, R9> collector9,
        Collector<? super T, A10, R10> collector10,
        Collector<? super T, A11, R11> collector11,
        Collector<? super T, A12, R12> collector12,
        Collector<? super T, A13, R13> collector13,
        Collector<? super T, A14, R14> collector14,
        Collector<? super T, A15, R15> collector15
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5, collector6, collector7, collector8, collector9, collector10, collector11, collector12, collector13, collector14, collector15));
    }

    /**
     * Collect this collectable into 16 {@link Collector}s.
     */
    @Generated("This method was generated using jOOQ-tools")
    default <R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16> Tuple16<R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16> collect(
        Collector<? super T, A1, R1> collector1,
        Collector<? super T, A2, R2> collector2,
        Collector<? super T, A3, R3> collector3,
        Collector<? super T, A4, R4> collector4,
        Collector<? super T, A5, R5> collector5,
        Collector<? super T, A6, R6> collector6,
        Collector<? super T, A7, R7> collector7,
        Collector<? super T, A8, R8> collector8,
        Collector<? super T, A9, R9> collector9,
        Collector<? super T, A10, R10> collector10,
        Collector<? super T, A11, R11> collector11,
        Collector<? super T, A12, R12> collector12,
        Collector<? super T, A13, R13> collector13,
        Collector<? super T, A14, R14> collector14,
        Collector<? super T, A15, R15> collector15,
        Collector<? super T, A16, R16> collector16
    ) {
        return collect(Tuple.collectors(collector1, collector2, collector3, collector4, collector5, collector6, collector7, collector8, collector9, collector10, collector11, collector12, collector13, collector14, collector15, collector16));
    }

// [jooq-tools] END [collect]

    /**
     * Count the values in this collectable.
     */
    long count();

    /**
     * Count the distinct values in this collectable.
     */
    default long countDistinct() {
        return collect(Agg.countDistinct());
    }

    /**
     * Count the distinct values of a given expression in this collectable.
     */
    default <U> long countDistinctBy(Function<? super T, ? extends U> function) {
        return collect(Agg.countDistinctBy(function));
    }

    /**
     * Get the mode, i.e. the value that appears most often in the collectable.
     */
    default Optional<T> mode() {
        return collect(Agg.mode());
    }

    /**
     * Get the sum of the elements in this collectable.
     */
    default Optional<T> sum() {
        return collect(Agg.sum());
    }
    
    /**
     * Get the sum of the elements in this collectable.
     */
    default <U> Optional<U> sum(Function<? super T, ? extends U> function) {
        return collect(Collectors.mapping(function, Agg.sum()));
    }
    
    /**
     * Get the sum of the elements in this collectable as <code>int</code>.
     */
    default int sumInt(ToIntFunction<? super T> function) {
        return collect(Collectors.summingInt(function));
    }
    
    /**
     * Get the sum of the elements in this collectable as <code>long</code>.
     */
    default long sumLong(ToLongFunction<? super T> function) {
        return collect(Collectors.summingLong(function));
    }
  
    /**
     * Get the sum of the elements in this collectable as <code>double</code>.
     */
    default double sumDouble(ToDoubleFunction<? super T> function) {
        return collect(Collectors.summingDouble(function));
    }

    /**
     * Get the average of the elements in this collectable.
     */
    default Optional<T> avg() {
        return collect(Agg.avg());
    }
 
    /**
     * Get the average of the elements in this collectable.
     */
    default <U> Optional<U> avg(Function<? super T, ? extends U> function) {
        return collect(Collectors.mapping(function, Agg.avg()));
    }
    
    /**
     * Get the average of the elements in this collectable as <code>int</code>.
     */
    default double avgInt(ToIntFunction<? super T> function) {
        return collect(Collectors.averagingInt(function));
    }
    
    /**
     * Get the average of the elements in this collectable as <code>long</code>.
     */
    default double avgLong(ToLongFunction<? super T> function) {
        return collect(Collectors.averagingLong(function));
    }
  
    /**
     * Get the average of the elements in this collectable as <code>double</code>.
     */
    default double avgDouble(ToDoubleFunction<? super T> function) {
        return collect(Collectors.averagingDouble(function));
    }
  
    /**
     * Get the minimum value.
     * <p>
     * This makes the unsafe assumption that
     * <code>&lt;T extends Comparable&lt;? super T>></code>
     */
    default Optional<T> min() {
        return (Optional) collect(Agg.<Comparable>min());
    }
    
    /**
     * Get the minimum value by a function.
     */
    Optional<T> min(Comparator<? super T> comparator);
   
    /**
     * Get the minimum value by a function.
     */
    default <U extends Comparable<? super U>> Optional<U> min(Function<? super T, ? extends U> function) {
        return collect(Agg.min(function));
    }
   
    /**
     * Get the minimum value by a function.
     */
    default <U> Optional<U> min(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collect(Agg.min(function, comparator));
    }
 
    /**
     * Get the minimum value by a function.
     */
    default <U extends Comparable<? super U>> Optional<T> minBy(Function<? super T, ? extends U> function) {
        return collect(Agg.minBy(function));
    }

    /**
     * Get the minimum value by a function.
     */
    default <U> Optional<T> minBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collect(Agg.minBy(function, comparator));
    }
  
    /**
     * Get the maximum value.
     * <p>
     * This makes the unsafe assumption that
     * <code>&lt;T extends Comparable&lt;? super T>></code>
     */
    default Optional<T> max() {
        return (Optional) collect(Agg.<Comparable>max());
    }

    /**
     * Get the maximum value by a function.
     */
    Optional<T> max(Comparator<? super T> comparator);
   
    /**
     * Get the maximum value by a function.
     */
    default <U extends Comparable<? super U>> Optional<U> max(Function<? super T, ? extends U> function) {
        return collect(Agg.max(function));
    }
   
    /**
     * Get the maximum value by a function.
     */
    default <U> Optional<U> max(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collect(Agg.max(function, comparator));
    }

    /**
     * Get the maximum value by a function.
     */
    default <U extends Comparable<? super U>> Optional<T> maxBy(Function<? super T, ? extends U> function) {
        return collect(Agg.maxBy(function));
    }

    /**
     * Get the maximum value by a function.
     */
    default <U> Optional<T> maxBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collect(Agg.maxBy(function, comparator));
    }
  
    /**
     * Get the median value.
     * <p>
     * This makes the unsafe assumption that
     * <code>&lt;T extends Comparable&lt;? super T>></code>
     */
    default Optional<T> median() {
        return (Optional) collect(Agg.<Comparable>median());
    }

    /**
     * Get the median value.
     */
    default Optional<T> median(Comparator<? super T> comparator) {
        return collect(Agg.median(comparator));
    }

    /**
     * Get the median value by a function.
     */
    default <U extends Comparable<? super U>> Optional<T> medianBy(Function<? super T, ? extends U> function) {
        return collect(Agg.medianBy(function));
    }

    /**
     * Get the median value by a function.
     */
    default <U> Optional<T> medianBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collect(Agg.medianBy(function, comparator));
    }
  
    /**
     * Get the discrete percentile value.
     * <p>
     * This makes the unsafe assumption that
     * <code>&lt;T extends Comparable&lt;? super T>></code>
     */
    default Optional<T> percentile(double percentile) {
        return (Optional) collect(Agg.<Comparable>percentile(percentile));
    }

    /**
     * Get the discrete percentile value.
     */
    default Optional<T> percentile(double percentile, Comparator<? super T> comparator) {
        return collect(Agg.percentile(percentile, comparator));
    }

    /**
     * Get the discrete percentile value by a function.
     */
    default <U extends Comparable<? super U>> Optional<T> percentileBy(double percentile, Function<? super T, ? extends U> function) {
        return collect(Agg.percentileBy(percentile, function));
    }

    /**
     * Get the discrete percentile value by a function.
     */
    default <U> Optional<T> percentileBy(double percentile, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return collect(Agg.percentileBy(percentile, function, comparator));
    }
    
    /**
     * Whether all elements in the collectable match a given predicate.
     */ 
    boolean allMatch(Predicate<? super T> predicate);
    
    /**
     * Whether any element in the collectable matches a given predicate.
     */
    boolean anyMatch(Predicate<? super T> predicate);
    
    /**
     * Whether no element in the collectable matches a given predicate.
     */
    boolean noneMatch(Predicate<? super T> predicate);

    /**
     * Collect the collectable into an {@link ArrayList}.
     */
    default List<T> toList() {
        return collect(Collectors.toList());
    }
    
    /**
     * Collect the collectable into a {@link List}.
     */
    default <L extends List<T>> L toList(Supplier<L> factory) {
        return collect(Collectors.toCollection(factory));
    }
    
    /**
     * Collect the collectable into a {@link LinkedHashSet}.
     */
    default Set<T> toSet() {
        return toSet(LinkedHashSet::new);
    }
    
    /**
     * Collect the collectable into a {@link Set}.
     */
    default <S extends Set<T>> S toSet(Supplier<S> factory) {
        return collect(Collectors.toCollection(factory));
    } 
    
    /**
     * Collect the collectable into a {@link Collection}.
     */
    default <C extends Collection<T>> C toCollection(Supplier<C> factory) {
        return collect(Collectors.toCollection(factory));
    }

    /**
     * Collect the collectable into a {@link Map}.
     *
     * @see #toMap(Stream, Function, Function)
     */
    default <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return collect(Collectors.toMap(keyMapper, valueMapper));
    }
}
