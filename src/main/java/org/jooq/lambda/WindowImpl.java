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

import static java.util.Comparator.naturalOrder;
import static org.jooq.lambda.Seq.seq;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jooq.lambda.tuple.Tuple2;

/**
 * @author Lukas Eder
 */
class WindowImpl<T, U> implements OrderedWindow<T, U> {
    
    final Tuple2<T, Long>                  value;
    final int                              index;
    final List<Tuple2<T, Long>>            partition;
    final Function<? super T, ? extends U> partitionBy;
    final Comparator<? super T>            order;
    final long                             lower;
    final long                             upper;

    WindowImpl(
        Tuple2<T, Long> value,
        List<Tuple2<T, Long>> partition, 
        Function<? super T, ? extends U> partitionBy, 
        Comparator<? super T> order, 
        long lower, 
        long upper
    ) {
        this.value = value;
        // TODO: Speed this up by using binary search
        this.index = partition.indexOf(value);
        this.partition = partition;
        this.partitionBy = partitionBy;
        this.order = order;
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public T value() {
        return value.v1;
    }
    
    @Override
    public U partition() {
        return partitionBy.apply(value());
    }

    @Override
    public long count() {
        return 1 + upper() - lower();
    }

    @Override
    public Optional<T> min() {
        return frame().min((Comparator) naturalOrder());
    }

    @Override
    public Optional<T> max() {
        return frame().max((Comparator) naturalOrder());
    }

    @Override
    public Optional<T> lead() {
        return lead(1);
    }

    @Override
    public Optional<T> lead(long lead) {
        return lead0(lead);
    }

    @Override
    public Optional<T> lag() {
        return lag(1);
    }

    @Override
    public Optional<T> lag(long lag) {
        return lead0(-lag);
    }

    private Optional<T> lead0(long lead) {
        if (lead == 0)
            return Optional.of(value.v1);
        else if (index + lead >= 0 && index + lead < partition.size())
            return Optional.of(partition.get(index + (int) lead).v1);
        else
            return Optional.empty();
    }

    @Override
    public Optional<T> firstValue() {
        return firstValue(t -> t);
    }

    @Override
    public <V> Optional<V> firstValue(Function<? super T, ? extends V> function) {
        return lowerInPartition()
             ? Optional.of(function.apply(partition.get(lower()).v1))
             : upperInPartition()
             ? Optional.of(function.apply(partition.get(0).v1))
             : Optional.empty();
    }

    @Override
    public Optional<T> lastValue() {
        return lastValue(t -> t);
    }

    @Override
    public <V> Optional<V> lastValue(Function<? super T, ? extends V> function) {
        return upperInPartition()
             ? Optional.of(function.apply(partition.get(upper()).v1))
             : lowerInPartition()
             ? Optional.of(function.apply(partition.get(partition.size() - 1).v1))
             : Optional.empty();
    }

    @Override
    public Optional<T> nthValue(long n) {
        return nthValue(n, t -> t);
    }

    @Override
    public <V> Optional<V> nthValue(long n, Function<? super T, ? extends V> function) {
        return lower() + n <= upper()
             ? Optional.of(function.apply(partition.get(lower() + (int) n).v1))
             : Optional.empty();
    }
    
    @Override
    public long rowNumber() {
        return (long) index;
    }

    @Override
    public long rank() {
        return seq(partition).map(t -> t.v1).collect(Agg.rank(value.v1, order)).get();
    }

    @Override
    public long denseRank() {
        return seq(partition).map(t -> t.v1).collect(Agg.denseRank(value.v1, order)).get();
    }

    @Override
    public double percentRank() {
        return ((double) rank()) / ((double) (partition.size() - 1));
    }

    @Override
    public long ntile(long bucket) {
        return (bucket * rowNumber() / partition.size()) + 1;
    }

    @Override
    public boolean all(Predicate<? super T> predicate) {
        return frame().allMatch(predicate);
    }

    @Override
    public boolean any(Predicate<? super T> predicate) {
        return frame().anyMatch(predicate);
    }

    @Override
    public boolean none(Predicate<? super T> predicate) {
        return frame().noneMatch(predicate);
    }
    
    private int lower() {
        // TODO: What about under/overflows?
        return lower == Long.MIN_VALUE ? 0 : (int) Math.max(0L, index + lower);
    }
    
    private boolean lowerInPartition() {
        // TODO: What about under/overflows?
        return lower == Long.MIN_VALUE || (index + lower >= 0L && index + lower < partition.size());
    }
    
    private int upper() {
        // TODO: What about under/overflows?
        return upper == Long.MAX_VALUE ? partition.size() - 1 : (int) Math.min(partition.size() - 1, (index + upper));
    }
    
    private boolean upperInPartition() {
        // TODO: What about under/overflows?
        return upper == Long.MAX_VALUE || (index + upper >= 0L && index + upper < partition.size());
    }
    
    private Seq<T> frame() {
        return Seq.seq(partition.subList(lower(), upper() + 1)).map(t -> t.v1);
    }
}
