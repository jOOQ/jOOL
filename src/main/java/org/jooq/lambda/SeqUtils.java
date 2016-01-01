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

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Lukas Eder
 */
class SeqUtils {

    @SuppressWarnings("unchecked")
    static <T> Seq<T>[] seqs(Stream<T>... streams) {
        if (streams == null)
            return null;

        return Seq.of(streams).map(Seq::seq).toArray(Seq[]::new);
    }

    @SuppressWarnings("unchecked")
    static <T> Seq<T>[] seqs(Iterable<T>... iterables) {
        if (iterables == null)
            return null;

        return Seq.of(iterables).map(Seq::seq).toArray(Seq[]::new);
    }

    static <T, U> Seq<U> transform(Stream<T> stream, DelegatingSpliterator<T, U> delegating) {
        Spliterator<T> delegate = stream.spliterator();

        return Seq.seq(new Spliterator<U>() {
            @Override
            public boolean tryAdvance(Consumer<? super U> action) {
                return delegating.tryAdvance(delegate, action);
            }

            @Override
            public Spliterator<U> trySplit() {
                return this;
            }

            @Override
            public long estimateSize() {
                return delegate.estimateSize();
            }

            @Override
            public int characteristics() {
                return delegate.characteristics();
            }
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
    static <E extends Throwable> void sneakyThrow0(Throwable throwable) throws E {
        throw (E) throwable;
    }

    @FunctionalInterface
    interface DelegatingSpliterator<T, U> {
        boolean tryAdvance(Spliterator<T> delegate, Consumer<? super U> action);
    }
}
