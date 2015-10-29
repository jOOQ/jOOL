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

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author Lukas Eder
 */
class SeqImpl<T> implements Seq<T> {

    static final Object     NULL = new Object();

    private final Stream<T> stream;
    private Object[]        buffered;

    SeqImpl(Stream<T> stream) {
        this.stream = stream;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<T> stream() {
        return buffered == null ? stream : (Stream<T>) Stream.of(buffered);
    }

    @Override
    public Seq<T> filter(Predicate<? super T> predicate) {
        return Seq.seq(stream().filter(predicate));
    }

    @Override
    public <R> Seq<R> map(Function<? super T, ? extends R> mapper) {
        return Seq.seq(stream().map(mapper));
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return stream().mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return stream().mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return stream().mapToDouble(mapper);
    }

    @Override
    public <R> Seq<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return Seq.seq(stream().flatMap(mapper));
    }

    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return stream().flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return stream().flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return stream().flatMapToDouble(mapper);
    }

    @Override
    public Seq<T> distinct() {
        return Seq.seq(stream().distinct());
    }

    @Override
    public Seq<T> sorted() {
        return Seq.seq(stream().sorted());
    }

    @Override
    public Seq<T> sorted(Comparator<? super T> comparator) {
        return Seq.seq(stream().sorted(comparator));
    }

    @Override
    public Seq<T> peek(Consumer<? super T> action) {
        return Seq.seq(stream().peek(action));
    }

    @Override
    public Seq<T> limit(long maxSize) {
        return Seq.seq(stream().limit(maxSize));
    }

    @Override
    public Seq<T> skip(long n) {
        return Seq.seq(stream().skip(n));
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        stream().forEach(action);
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        stream().forEachOrdered(action);
    }

    @Override
    public Object[] toArray() {
        return stream().toArray();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return stream().toArray(generator);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return stream().reduce(identity, accumulator);
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return stream().reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return stream().reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return stream().collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return stream().collect(collector);
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return stream().min(comparator);
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return stream().max(comparator);
    }

    @Override
    public long count() {
        return stream().count();
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return stream().anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return stream().allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return stream().noneMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        return stream().findFirst();
    }

    @Override
    public Optional<T> findAny() {
        return stream().findAny();
    }

    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return stream().spliterator();
    }

    /**
     * Always returns false. Seq streams are always sequential and, as such,
     * doesn't support parallelization.
     *
     * @return false
     * @see <a href="https://github.com/jOOQ/jOOL/issues/130">jOOL Issue #130</a>
     */
    @Override
    public boolean isParallel() {
        return false;
    }

    @Override
    public Seq<T> onClose(Runnable closeHandler) {
        return Seq.seq(stream.onClose(closeHandler));
    }

    @Override
    public void close() {
        stream.close();
    }

    @Override
    public String toString() {
        buffered = toArray();
        return Seq.toString(stream());
    }
}
