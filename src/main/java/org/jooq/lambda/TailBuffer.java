package org.jooq.lambda;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Special class that handles skipping or limiting to last <code>n</code> elements.
 *
 * Note that this class does not truly handle <code>n</code> of type <code>long</code> - instead,
 * it just throws exceptions if <code>n</code> is greater than <code>Integer.MAX_VALUE - 10</code>.
 *
 * @author Tomasz Linkowski
 */
final class TailBuffer<T> {

    private static final long MAX_VALID_N = Integer.MAX_VALUE - 10;

    static <T> Seq<T> skipLast(Stream<? extends T> stream, long n) {
        return Seq.seq(new TailBuffer<>(stream.spliterator(), n).new SkipLastSpliterator());
    }

    static <T> Seq<T> limitLast(Stream<? extends T> stream, long maxSize) {
        return Seq.seq(new TailBuffer<>(stream.spliterator(), maxSize).new LimitLastSpliterator());
    }

    private final Spliterator<T> source;
    private final Deque<T> buffer;
    /**
     * Number of elements that should be skipped/limited from the end.
     */
    private final int n;
    /**
     * <code>True</code> while <code>source</code> hasn't been exhausted.
     */
    private boolean buffering = true;

    private TailBuffer(Spliterator<T> source, long n) {
        if (n <= 0)
            throw new IllegalArgumentException("N must be positive (" + n + " given)");
        if (n > MAX_VALID_N)
            throw new IllegalArgumentException("N must not be greater than " + MAX_VALID_N + "(" + n + " given)");

        this.source = source;
        this.n = (int) n;

        // we have to buffer one more element than "n" in order to make a decision whether
        // to start (in case of "limitLast") or stop (in case of "skipLast") returning elements
        this.buffer = new ArrayDeque<>(this.n + 1);
    }

    /**
     * @return - <code>true</code> if next element fetched into <code>buffer</code>; <code>false</code> otherwise
     */
    private boolean fetchNext() {
        assert buffering;
        buffering = source.tryAdvance(buffer::addLast);
        return buffering;
    }

    /**
     * @return - <code>true</code> if <code>buffer</code> is full (i.e. contains <code>n+1</code> elements);
     * <code>false</code> otherwise
     */
    private boolean fillBuffer() {
        if (!buffering)
            return false;

        while (buffer.size() <= n) {
            if (!fetchNext())
                return false;
        }
        return true;
    }

    /**
     * Implementation of <code>Spliterator</code> for <code>Seq.skipLast(int n)</code>.
     */
    private final class SkipLastSpliterator implements SequentialSpliterator<T> {

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (!fillBuffer()) {
                // skip last N elements
                buffer.clear(); // help GC
                return false;
            }

            assert buffer.size() == n + 1;
            action.accept(buffer.removeFirst());
            return true;
        }
    }

    /**
     * Implementation of <code>Spliterator</code> for <code>Seq.limitLast(int maxSize)</code>.
     */
    private final class LimitLastSpliterator implements SequentialSpliterator<T> {

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (buffering && fillBuffer()) // entered only once (when first element requested)
                skipInitialElements();

            if (buffer.isEmpty())
                return false;

            // return last N elements
            action.accept(buffer.removeFirst());
            return true;
        }

        private void skipInitialElements() {
            assert buffer.size() == n + 1;
            do { // skip elements until last N left
                buffer.removeFirst();
            } while (buffering && fetchNext());
            assert buffer.size() == n;
        }
    }

    @FunctionalInterface
    private interface SequentialSpliterator<T> extends Spliterator<T> {

        @Override
        default Spliterator<T> trySplit() {
            return null;
        }

        @Override
        default long estimateSize() {
            return Long.MAX_VALUE;
        }

        @Override
        default int characteristics() {
            return Spliterator.ORDERED;
        }
    }
}
