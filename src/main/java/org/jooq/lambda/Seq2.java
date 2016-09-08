package org.jooq.lambda;

import org.jooq.lambda.function.Function3;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author Kirill Korgov (kirill@korgov.ru)
 *         30.09.2015 19:21
 */
public interface Seq2<T1, T2> extends Seq<Tuple2<T1, T2>> {

    /**
     * Wrap an <code>Iterable</code> into a <code>Seq2</code>.
     */
    static <T1, T2> Seq2<T1, T2> seq(final Iterable<Tuple2<T1, T2>> iterable) {
        return seq(Seq.seq(iterable));
    }

    /**
     * Wrap an <code>Iterator</code> into a <code>Seq2</code>.
     */
    static <T1, T2> Seq2<T1, T2> seq(final Iterator<Tuple2<T1, T2>> iterator) {
        return seq(Seq.seq(iterator));
    }

    /**
     * Wrap a <code>Spliterator</code> into a <code>Seq2</code>.
     */
    static <T1, T2> Seq2<T1, T2> seq(final Spliterator<Tuple2<T1, T2>> spliterator) {
        return seq(Seq.seq(spliterator));
    }

    /**
     * Wrap a <code>Map</code> into a <code>Seq2</code>.
     */
    static <K, V> Seq2<K, V> seq(final Map<K, V> map) {
        return Seq2.seq(Seq.seq(map));
    }

    /**
     * Wrap an <code>Optional</code> into a <code>Seq2</code>.
     */
    static <T1, T2> Seq2<T1, T2> seq(final Optional<Tuple2<T1, T2>> optional) {
        return seq(Seq.seq(optional));
    }

    static <T1, T2> Seq2<T1, T2> seq(final Stream<Tuple2<T1, T2>> stream) {
        return seq(Seq.seq(stream));
    }

    static <T1, T2> Seq2<T1, T2> seq(final Seq<Tuple2<T1, T2>> src) {
        if (src instanceof Seq2) {
            return (Seq2<T1, T2>) src;
        }
        return new Seq2Impl<>(src);
    }

    static <T1, T2> Seq2<T1, T2> zip(final Seq<T1> s1, final Seq<T2> s2) {
        return seq(s1.zip(s2));
    }

    static <T1, T2> Seq2<T1, T2> zip(final Stream<T1> s1, final Stream<T2> s2) {
        return zip(Seq.seq(s1), Seq.seq(s2));
    }

    static <T1, T2> Seq2<T1, T2> zip(final Iterable<T1> s1, final Iterable<T2> s2) {
        return zip(Seq.seq(s1), Seq.seq(s2));
    }

    static <T1, T2> Seq2<T1, T2> zip(final Iterator<T1> s1, final Iterator<T2> s2) {
        return zip(Seq.seq(s1), Seq.seq(s2));
    }

    static <T1, T2> Seq2<T1, T2> zipWithResult(final Seq<T1> s, final Function<T1, T2> f) {
        return Seq2.seq(s.map(element -> new Tuple2<>(element, f.apply(element))));
    }

    static <T1, T2> Seq2<T1, T2> zipWithResult(final Stream<T1> s, final Function<T1, T2> f) {
        return Seq2.zipWithResult(Seq.seq(s), f);
    }

    static <T1, T2> Seq2<T1, T2> zipWithResult(final Iterable<T1> s, final Function<T1, T2> f) {
        return Seq2.zipWithResult(Seq.seq(s), f);
    }

    static <T1, T2> Seq2<T1, T2> zipWithResult(final Iterator<T1> s, final Function<T1, T2> f) {
        return Seq2.zipWithResult(Seq.seq(s), f);
    }

    static <T> Seq2<T, Long> zipWithIndex(final Seq<T> stream) {
        return Seq2.seq(Seq.zipWithIndex(stream));
    }

    static <T> Seq2<T, Long> zipWithIndex(final Iterable<T> iterable) {
        return Seq2.zipWithIndex(Seq.seq(iterable));
    }

    static <T> Seq2<T, Long> zipWithIndex(final Stream<T> stream) {
        return Seq2.zipWithIndex(Seq.seq(stream));
    }

    default <U> U foldLeft(final U seed, final Function3<U, ? super T1, ? super T2, U> function) {
        return Seq.super.foldLeft(seed, (u, v) -> function.apply(u, v.v1, v.v2));
    }

    default Seq2<T1, T2> filter(final BiPredicate<? super T1, ? super T2> predicate) {
        return Seq2.seq(filter(t -> predicate.test(t.v1, t.v2)));
    }

    default Seq2<T1, T2> filter1(final Predicate<? super T1> predicate) {
        return Seq2.seq(filter(t -> predicate.test(t.v1)));
    }

    default Seq2<T1, T2> filter2(final Predicate<? super T2> predicate) {
        return Seq2.seq(filter(t -> predicate.test(t.v2)));
    }

    default <R> Seq<R> map(final BiFunction<? super T1, ? super T2, R> fu2) {
        return map(t -> fu2.apply(t.v1, t.v2));
    }

    default <R1> Seq2<R1, T2> map1(final BiFunction<? super T1, ? super T2, R1> fu2) {
        return mapToT2((v1, v2) -> Tuple.tuple(fu2.apply(v1, v2), v2));
    }

    default <R2> Seq2<T1, R2> map2(final BiFunction<? super T1, ? super T2, R2> fu2) {
        return mapToT2((v1, v2) -> Tuple.tuple(v1, fu2.apply(v1, v2)));
    }

    default <R1> Seq2<R1, T2> map1(final Function<? super T1, R1> fu2) {
        return map1((v1, v2) -> fu2.apply(v1));
    }

    default <R2> Seq2<T1, R2> map2(final Function<? super T2, R2> fu2) {
        return map2((v1, v2) -> fu2.apply(v2));
    }

    default <R1, R2, R extends Tuple2<R1, R2>> Seq2<R1, R2> mapToT2(final BiFunction<? super T1, ? super T2, R> fu2) {
        return Seq2.seq(map(t -> fu2.apply(t.v1, t.v2)));
    }

    default Seq<T1> keys() {
        return v1s();
    }

    default Seq<T2> values() {
        return v2s();
    }

    default Seq<T1> v1s() {
        return map(Tuple2::v1);
    }

    default Seq<T2> v2s() {
        return map(Tuple2::v2);
    }

    default IntStream mapToInt(final ToIntBiFunction<? super T1, ? super T2> mapper) {
        return mapToInt(t -> mapper.applyAsInt(t.v1, t.v2));
    }

    default LongStream mapToLong(final ToLongBiFunction<? super T1, ? super T2> mapper) {
        return mapToLong(t -> mapper.applyAsLong(t.v1, t.v2));
    }

    default DoubleStream mapToDouble(final ToDoubleBiFunction<? super T1, ? super T2> mapper) {
        return mapToDouble(t -> mapper.applyAsDouble(t.v1, t.v2));
    }

    default <R> Seq<R> flatMap(final BiFunction<? super T1, ? super T2, ? extends Stream<? extends R>> fu2) {
        return flatMap(t -> fu2.apply(t.v1, t.v2));
    }

    default IntStream flatMapToInt(final BiFunction<? super T1, ? super T2, ? extends IntStream> mapper) {
        return flatMapToInt(t -> mapper.apply(t.v1, t.v2));
    }

    default LongStream flatMapToLong(final BiFunction<? super T1, ? super T2, ? extends LongStream> mapper) {
        return flatMapToLong(t -> mapper.apply(t.v1, t.v2));
    }

    default DoubleStream flatMapToDouble(final BiFunction<? super T1, ? super T2, ? extends DoubleStream> mapper) {
        return flatMapToDouble(t -> mapper.apply(t.v1, t.v2));
    }

    default Seq2<T1, T2> peek(final BiConsumer<? super T1, ? super T2> action) {
        return Seq2.seq(peek(t -> action.accept(t.v1, t.v2)));
    }

    default void forEach(final BiConsumer<? super T1, ? super T2> action) {
        Seq.super.forEach(t -> action.accept(t.v1, t.v2));
    }

    default Map<T1, T2> toMap() {
        return toMap(Tuple2::v1, Tuple2::v2);
    }

    default Seq2<Tuple2<T1, T2>, Long> zipWithIndex() {
        return Seq2.seq(Seq.super.zipWithIndex());
    }

    @Override
    default Seq2<T1, T2> sequential() {
        return this;
    }

    @Override
    default Seq2<T1, T2> parallel() {
        return this;
    }

    @Override
    default Seq2<T1, T2> unordered() {
        return this;
    }

    @Override
    Seq2<T1, T2> distinct();

    @Override
    Seq2<T1, T2> sorted();

    @Override
    Seq2<T1, T2> sorted(Comparator<? super Tuple2<T1, T2>> comparator);

    @Override
    Seq2<T1, T2> limit(long maxSize);

    @Override
    Seq2<T1, T2> skip(long n);

    @Override
    Seq2<T1, T2> onClose(Runnable closeHandler);

    class Seq2Impl<T1, T2> extends SeqImpl<Tuple2<T1, T2>> implements Seq2<T1, T2> {
        Seq2Impl(final Stream<Tuple2<T1, T2>> delegate) {
            super(delegate);
        }

        @Override
        public Seq2<T1, T2> distinct() {
            return Seq2.seq(super.distinct());
        }

        @Override
        public Seq2<T1, T2> sorted() {
            return Seq2.seq(super.sorted());
        }

        @Override
        public Seq2<T1, T2> sorted(final Comparator<? super Tuple2<T1, T2>> comparator) {
            return Seq2.seq(super.sorted(comparator));
        }

        @Override
        public Seq2<T1, T2> limit(final long maxSize) {
            return Seq2.seq(super.limit(maxSize));
        }

        @Override
        public Seq2<T1, T2> skip(final long n) {
            return Seq2.seq(super.skip(n));
        }

        @Override
        public Seq2<T1, T2> onClose(final Runnable closeHandler) {
            return Seq2.seq(super.onClose(closeHandler));
        }
    }

}
