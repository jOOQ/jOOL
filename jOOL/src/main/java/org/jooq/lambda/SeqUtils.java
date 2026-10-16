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

import static java.util.Comparator.comparing;
import static org.jooq.lambda.Seq.seq;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.jooq.lambda.tuple.Tuple2;

/**
 * @author Lukas Eder
 */
class SeqUtils {

    @SafeVarargs
    @SuppressWarnings("unchecked")
    static <T> Seq<T>[] seqs(Stream<? extends T>... streams) {
        if (streams == null)
            return null;

        return Seq.of(streams).map(Seq::seq).toArray(Seq[]::new);
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    static <T> Seq<T>[] seqs(Iterable<? extends T>... iterables) {
        if (iterables == null)
            return null;

        return Seq.of(iterables).map(Seq::seq).toArray(Seq[]::new);
    }

    static <T, U> Seq<U> transform(Stream<? extends T> stream, DelegatingSpliterator<T, U> delegating) {
        Spliterator<? extends T> delegate = stream.spliterator();

        return Seq.seq(new Spliterator<U>() {
            @Override
            public boolean tryAdvance(Consumer<? super U> action) {
                return delegating.tryAdvance(delegate, action);
            }

            @Override
            public Spliterator<U> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            @Override
            public int characteristics() {
                return delegate.characteristics() & Spliterator.ORDERED;
            }
            
            @Override
            @SuppressWarnings("unchecked")
            public Comparator<? super U> getComparator() {
                
                // This implementation works with the JDK 8, as the information
                // is really only used in 
                // java.util.stream.StreamOpFlag.fromCharacteristics(Spliterator<?> spliterator)
                // Currently, the point of this method is only to be used for
                // optimisations (e.g. to avoid sorting a stream twice in a row)
                return (Comparator) delegate.getComparator();
            }
        }).onClose(stream::close);
    }

    /**
     * Repeat a stream <code>times</code> times, or infinitely if <code>times == -1</code>.
     */
    @SuppressWarnings("unchecked")
    static <T> Seq<T> cycle(Seq<? extends T> stream, long times) {
        if (times == 0)
            return Seq.empty();
        if (times == 1)
            return (Seq<T>) stream;
        
        List<T> list = new ArrayList<>();
        Spliterator<T>[] sp = new Spliterator[1];
        long[] remaining = new long[] { times };
        
        return transform(stream, (delegate, action) -> {
            if (sp[0] == null) {
                if (delegate.tryAdvance(t -> {
                    list.add(t);
                    action.accept(t);
                }))
                    return true;
                else
                    sp[0] = list.spliterator();
            }

            if (!sp[0].tryAdvance(action)) {
                if (times != -1 && (remaining[0] = remaining[0] - 1) == 1)
                    return false;
                
                sp[0] = list.spliterator();

                if (!sp[0].tryAdvance(action))
                    return false;
            }

            return true;
        });
    }
    
    static <T> Map<?, Partition<T>> partitions(WindowSpecification<T> window, List<Tuple2<T, Long>> input) {
        return seq(input).groupBy(
            window.partition().compose(t -> t.v1), 
            Collector.<
                Tuple2<T, Long>,
                Collection<Tuple2<T, Long>>,
                Partition<T>
            >of(
                () -> window.order().isPresent()
                    ? new TreeSet<>(comparing((Tuple2<T, Long> t) -> t.v1, window.order().get()).thenComparing(t -> t.v2))
                    : new ArrayList<>(),
                Collection::add,
                (s1, s2) -> { s1.addAll(s2); return s1; },
                Partition::new
            )
        );
    }

    @SuppressWarnings("unchecked")
    static <T> OptionalLong indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
        for (long index = 0; iterator.hasNext(); index++)
            if (predicate.test(iterator.next()))
                return OptionalLong.of(index);

        return OptionalLong.empty();
    }
    
    /**
     * A range of <code>long</code> values that stops at the upper bound, or
     * when the next value would overflow.
     */
    static Seq<Long> range(long fromInclusive, long to, long step, boolean toInclusive) {
        if (step <= 0L)
            throw new IllegalArgumentException("The step must be positive: " + step);
        if (toInclusive ? to < fromInclusive : to <= fromInclusive)
            return Seq.empty();

        long[] next = { fromInclusive };
        boolean[] done = { false };

        return Seq.seq((FunctionalSpliterator<Long>) action -> {
            if (done[0])
                return false;

            long current = next[0];
            long n = current + step;

            // n < current only if the addition overflowed, as step > 0
            if (n < current || (toInclusive ? n > to : n >= to))
                done[0] = true;
            else
                next[0] = n;

            action.accept(current);
            return true;
        });
    }

    /**
     * A range of {@link Instant} values that stops at the upper bound, or
     * when the next value would exceed {@link Instant#MAX}.
     */
    static Seq<Instant> range(Instant fromInclusive, Instant to, Duration step, boolean toInclusive) {
        if (step.isZero() || step.isNegative())
            throw new IllegalArgumentException("The step must be positive: " + step);
        if (toInclusive ? to.isBefore(fromInclusive) : !to.isAfter(fromInclusive))
            return Seq.empty();

        Instant[] next = { fromInclusive };

        return Seq.seq((FunctionalSpliterator<Instant>) action -> {
            Instant current = next[0];

            if (current == null)
                return false;

            Instant n;
            try {
                n = current.plus(step);
            }
            catch (DateTimeException | ArithmeticException e) {
                n = null;
            }

            next[0] = n == null || (toInclusive ? n.isAfter(to) : !n.isBefore(to)) ? null : n;
            action.accept(current);
            return true;
        });
    }

    /**
     * Sneaky throw any type of Throwable.
     */
    static void sneakyThrow(Throwable throwable) {
        SeqUtils.<RuntimeException>sneakyThrow0(throwable);
    }

    /**
     * Sneaky throw any type of Throwable.
     */
    @SuppressWarnings("unchecked")
    static <E extends Throwable> void sneakyThrow0(Throwable throwable) throws E {
        throw (E) throwable;
    }

    @FunctionalInterface
    interface DelegatingSpliterator<T, U> {
        boolean tryAdvance(Spliterator<? extends T> delegate, Consumer<? super U> action);
    }
    
    static Runnable closeAll(AutoCloseable... closeables) {
        return () -> {
            Throwable t = null;
            
            for (AutoCloseable closeable : closeables) {
                try {
                    closeable.close();
                }
                catch (Throwable t1) {
                    if (t == null)
                        t = t1;
                    else if (t != t1)
                        t.addSuppressed(t1);
                }
            }
            
            if (t != null)
                sneakyThrow(t);
        };
    }
}
