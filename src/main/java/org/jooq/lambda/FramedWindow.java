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

import java.util.Optional;
import java.util.function.Function;

/**
 * A window containing the data for its ordered partition, to perform
 * calculations upon.
 *
 * @author Lukas Eder
 */
public interface FramedWindow<T, U> {

    /**
     * The value of the current row in the window.
     */
    T value();
    
    /**
     * The partition of the current row.
     */
    U partition();
    
    /**
     * The number of elements in the partition.
     */
    long count();
    
    /**
     * The lowest value in the partition.
     */
    Optional<T> min();
    
    /**
     * The highest value in the partition.
     */
    Optional<T> max();
    
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
    
}
