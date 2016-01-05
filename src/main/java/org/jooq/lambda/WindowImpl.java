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

import static java.util.Comparator.naturalOrder;
import static org.jooq.lambda.Seq.seq;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;

import org.jooq.lambda.tuple.Tuple2;

/**
 * @author Lukas Eder
 */
class WindowImpl<T> implements Window<T> {
    
    final Tuple2<T, Long>                  value;
    final int                              index;
    final List<Tuple2<T, Long>>            partition;
    final Comparator<? super T>            order;
    final long                             lower;
    final long                             upper;

    WindowImpl(
        Tuple2<T, Long> value,
        List<Tuple2<T, Long>> partition, 
        WindowSpecification<T> specification
    ) {
        this.value = value;
        // TODO: Speed this up by using binary search
        this.index = partition.indexOf(value);
        this.partition = partition;
        this.order = specification.order().orElse((Comparator<? super T>) naturalOrder());
        this.lower = specification.lower();
        this.upper = specification.upper();
    }

    @Override
    public T value() {
        return value.v1;
    }
    
    @Override
    public Seq<T> window() {
        return Seq.seq(partition.subList(lower(), upper() + 1)).map(t -> t.v1);
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
        return (bucket * rowNumber() / partition.size());
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
    public <U> Optional<U> firstValue(Function<? super T, ? extends U> function) {
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
    public <U> Optional<U> lastValue(Function<? super T, ? extends U> function) {
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
    public <U> Optional<U> nthValue(long n, Function<? super T, ? extends U> function) {
        return lower() + n <= upper()
             ? Optional.of(function.apply(partition.get(lower() + (int) n).v1))
             : Optional.empty();
    }
    
    @Override
    public long count() {
        return 1 + upper() - lower();
    }

    @Override
    public long countDistinct() {
        return window().countDistinct();
    }

    @Override
    public <U> long countDistinctBy(Function<? super T, ? extends U> function) {
        return window().countDistinctBy(function);
    }

    @Override
    public Optional<T> sum() {
        return window().sum();
    }

    @Override
    public <U> Optional<U> sum(Function<? super T, ? extends U> function) {
        return window().sum(function);
    }

    @Override
    public int sumInt(ToIntFunction<? super T> function) {
        return window().sumInt(function);
    }

    @Override
    public long sumLong(ToLongFunction<? super T> function) {
        return window().sumLong(function);
    }

    @Override
    public double sumDouble(ToDoubleFunction<? super T> function) {
        return window().sumDouble(function);
    }

    @Override
    public Optional<T> avg() {
        return window().avg();
    }

    @Override
    public <U> Optional<U> avg(Function<? super T, ? extends U> function) {
        return window().avg(function);
    }

    @Override
    public double avgInt(ToIntFunction<? super T> function) {
        return window().avgInt(function);
    }

    @Override
    public double avgLong(ToLongFunction<? super T> function) {
        return window().avgLong(function);
    }

    @Override
    public double avgDouble(ToDoubleFunction<? super T> function) {
        return window().avgDouble(function);
    }

    @Override
    public Optional<T> min() {
        return window().min((Comparator) naturalOrder());
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return window().min(comparator);
    }

    @Override
    public <U extends Comparable<? super U>> Optional<T> minBy(Function<? super T, ? extends U> function) {
        return window().minBy(function);
    }

    @Override
    public <U> Optional<T> minBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return window().minBy(function, comparator);
    }

    @Override
    public Optional<T> max() {
        return window().max((Comparator) naturalOrder());
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return window().max(comparator);
    }

    @Override
    public <U extends Comparable<? super U>> Optional<T> maxBy(Function<? super T, ? extends U> function) {
        return window().maxBy(function);
    }

    @Override
    public <U> Optional<T> maxBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return window().maxBy(function, comparator);
    }

    @Override
    public Optional<T> median() {
        return window().median((Comparator) naturalOrder());
    }

    @Override
    public Optional<T> median(Comparator<? super T> comparator) {
        return window().median(comparator);
    }

    @Override
    public <U extends Comparable<? super U>> Optional<T> medianBy(Function<? super T, ? extends U> function) {
        return window().medianBy(function);
    }

    @Override
    public <U> Optional<T> medianBy(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return window().medianBy(function, comparator);
    }

    @Override
    public Optional<T> percentile(double percentile) {
        return window().percentile(percentile, (Comparator) naturalOrder());
    }

    @Override
    public Optional<T> percentile(double percentile, Comparator<? super T> comparator) {
        return window().percentile(percentile, comparator);
    }

    @Override
    public <U extends Comparable<? super U>> Optional<T> percentileBy(double percentile, Function<? super T, ? extends U> function) {
        return window().percentileBy(percentile, function);
    }

    @Override
    public <U> Optional<T> percentileBy(double percentile, Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return window().percentileBy(percentile, function, comparator);
    }

    @Override
    public Optional<T> mode() {
        return window().mode();
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return window().allMatch(predicate);
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return window().anyMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return window().noneMatch(predicate);
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return window().collect(collector);
    }

    @Override
    public List<T> toList() {
        return window().toList();
    }
    
    @Override
    public <L extends List<T>> L toList(Supplier<L> factory) {
        return window().toList(factory);
    }

    @Override
    public Set<T> toSet() {
        return window().toSet();
    }

    @Override
    public <S extends Set<T>> S toSet(Supplier<S> factory) {
        return window().toSet(factory);
    }

    @Override
    public <C extends Collection<T>> C toCollection(Supplier<C> factory) {
        return window().toCollection(factory);
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
}
