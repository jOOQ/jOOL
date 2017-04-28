package org.jooq.lambda;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Special class that implements a fully lazy <code>flatMap</code> operation.
 * Default <code>flatMap</code> in <code>Stream</code> is only a partially lazy operation:
 * @link http://stackoverflow.com/questions/29229373/why-filter-after-flatmap-is-not-completely-lazy-in-java-streams
 *
 * This class should not be instantiated outside of the provided static <code>flatMapLazily</code> method.
 *
 * @see Stream#flatMap
 *
 * @author Tomasz Linkowski
 */
final class LazyFlatMapper<T, R> {

    static <T, R> Seq<R> flatMapLazily(Stream<? extends T> stream, Function<? super T, ? extends Stream<? extends R>> mapper) {
        return new LazyFlatMapper<T, R>(mapper).flatMapLazily(stream);
    }

    /**
     * <code>Function</code> provided as the <code>flatMap()</code> argument.
     *
     * @see Stream#flatMap
     */
    private final Function<? super T, ? extends Stream<? extends R>> flatMapper;
    /**
     * Stores last unclosed output stream (or <code>null</code>) in order to close it in due time
     * (i.e. when the the <code>outStream</code> has been entirely consumed,
     * or when the resulting flat-mapped stream has been closed) as specified in:
     *
     * @see Stream#flatMap
     */
    private Stream<? extends R> outStream = null;

    private LazyFlatMapper(Function<? super T, ? extends Stream<? extends R>> flatMapper) {
        this.flatMapper = Objects.requireNonNull(flatMapper);
    }

    /**
     * Creates a <code>FlatMapSpliterator</code>, wraps it in <code>Seq</code>, and registers required close handlers.
     */
    private Seq<R> flatMapLazily(Stream<? extends T> inStream) {
        return Seq.seq(new FlatMapSpliterator(inStream.spliterator()))
              .onClose(this::closeLastOutStream)
              .onClose(inStream::close);
    }

    private void closeLastOutStream() {
        if (outStream != null) {
            outStream.close();
            outStream = null;
        }
    }

    /**
     * <code>SequentialSpliterator</code> performing the flat-map operation on given <code>in</code> spliterator
     * and using given <code>flatMapper</code> function.
     */
    private final class FlatMapSpliterator implements SequentialSpliterator<R> {

        /**
         * Source spliterator (<code>null</code> if this <code>FlatMapSpliterator</code> has no more elements).
         */
        private Spliterator<? extends T> in;
        /**
         * Target spliterator (<code>null</code> if this <code>FlatMapSpliterator</code> has not yet been initialized).
         */
        private Spliterator<? extends R> out = null;

        private FlatMapSpliterator(Spliterator<? extends T> in) {
            this.in = Objects.requireNonNull(in);
        }

        @Override
        public boolean tryAdvance(Consumer<? super R> action) {
            if (isExhausted())
                return false;

            while (!tryAdvanceOutput(action)) {
                if (!tryAdvanceInput()) {
                    markAsExhausted();
                    return false;
                }
            }
            return true; // output has been advanced
        }

        private boolean tryAdvanceOutput(Consumer<? super R> action) {
            return out != null && out.tryAdvance(action);
        }

        private boolean tryAdvanceInput() {
            return in.tryAdvance(this::initNewOutStream);
        }

        /**
         * Switch from current <code>out</code> spliterator (possibly <code>null</code>)
         * to the next one, flat-mapped from given <code>t</code>.
         */
        private void initNewOutStream(T t) {
            closeLastOutStream();
            outStream = Objects.requireNonNull(flatMapper.apply(t));
            out = Objects.requireNonNull(outStream.spliterator());
        }

        private boolean isExhausted() {
            return in == null;
        }

        private void markAsExhausted() {
            in = null;
            closeLastOutStream();
        }
    }
}
