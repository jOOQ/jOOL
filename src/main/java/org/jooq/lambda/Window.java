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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A window containing the data for its partition, to perform
 * calculations upon.
 *
 * @author Lukas Eder
 */
public interface Window<T> {
        
    /**
     * The value of the current row in the window.
     */
    T value();
    
    /**
     * Stream all elements in the window.
     */
    Seq<T> window();
    
    /**
     * The row number of the current row within the partition.
     */
    long rowNumber();
    
    /**
     * The rank of the current row within the partition.
     */
    long rank();
    
    /**
     * The dense rank of the current row within the partition.
     */
    long denseRank();
    
    /**
     * The precent rank of the current row within the partition.
     */
    double percentRank();
    
    /**
     * The bucket number ("ntile") of the current row within the partition.
     */
    long ntile(long buckets);

    /**
     * The number of elements in the partition.
     */
    long count();
  
//    TODO: Add support for these...
//
//    /**
//     * The number of distinct elements in the partition.
//     */
//    long countDistinct();
//    
//    /**
//     * The number of distinct elements in the partition.
//     */
//    <V> long countDistinctBy(Function<? super T, ? extends V> function);
    
    /**
     * The lowest value in the partition.
     */
    Optional<T> min();
     
    /**
     * The lowest value in the partition.
     */
    Optional<T> min(Comparator<? super T> comparator);
    
    /**
     * The lowest value in the partition.
     */
    <U extends Comparable<? super U>> Optional<T> minBy(Function<? super T, ? extends U> function);
     
    /**
     * The lowest value in the partition.
     */
    <U> Optional<T> minBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator);
    
    /**
     * The highest value in the partition.
     */
    Optional<T> max();
    
    /**
     * The highest value in the partition.
     */
    Optional<T> max(Comparator<? super T> comparator);
    
    /**
     * The highest value in the partition.
     */
    <U extends Comparable<? super U>> Optional<T> maxBy(Function<? super T, ? extends U> function);
    
    /**
     * The highest value in the partition.
     */
    <U> Optional<T> maxBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator);
    
    /**
     * The next value in the partition.
     * <p>
     * This is the same as calling <code>lead(1)</code>
     */
    Optional<T> lead();
    
    /**
     * The next value by <code>lead</code> in the partition.
     */
    Optional<T> lead(long lead);
    
    /**
     * The previous value in the partition.
     * <p>
     * This is the same as calling <code>lag(1)</code>
     */
    Optional<T> lag();
    
    /**
     * The previous value by <code>lag</code> in the partition.
     */
    Optional<T> lag(long lag);
    
    /**
     * The first value in the partition.
     */
    Optional<T> firstValue();

    /**
     * The first value in the partition.
     */
    <V> Optional<V> firstValue(Function<? super T, ? extends V> function);
    
    /**
     * The last value in the partition.
     */
    Optional<T> lastValue();
    
    /**
     * The last value in the partition.
     */
    <V> Optional<V> lastValue(Function<? super T, ? extends V> function);

    /**
     * The nth value in the partition.
     */
    Optional<T> nthValue(long n);

    /**
     * The nth value in the partition.
     */
    <V> Optional<V> nthValue(long n, Function<? super T, ? extends V> function);

    /**
     * Whether all elements in the partition match a given predicate.
     */ 
    boolean all(Predicate<? super T> predicate);
    
    /**
     * Whether any element in the partition matches a given predicate.
     */
    boolean any(Predicate<? super T> predicate);
    
    /**
     * Whether no element in the partition matches a given predicate.
     */
    boolean none(Predicate<? super T> predicate);
    
    /**
     * Apply any aggregate function (collector) to the partition.
     */
    <R, A> R collect(Collector<? super T, A, R> collector);

    /**
     * Collect the partition into an {@link ArrayList}
     */
    List<T> toList();
    
    /**
     * Collect the partition into a {@link List}
     */
    <L extends List<T>> L toList(Supplier<L> factory);
    
    /**
     * Collect the partition into a {@link LinkedHashSet}
     */
    Set<T> toSet();
    
    /**
     * Collect the partition into a {@link Set}
     */
    <S extends Set<T>> S toSet(Supplier<S> factory);
    
    /**
     * Collect the partition into a {@link Collection}
     */
    <C extends Collection<T>> C toCollection(Supplier<C> factory);
}
