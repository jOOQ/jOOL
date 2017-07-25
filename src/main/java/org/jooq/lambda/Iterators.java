package org.jooq.lambda;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Created by billoneil on 7/26/17.
 */
public class Iterators {
    private enum PredicateCheckState { NOT_TESTED, MORE, ONE_MORE, NO_MORE }

    static <E> PeekingIterator<E> peeking(Iterator<E> iterator) {
        return new PeekingIteratorImpl<E>(iterator);
    }

    static <E> Iterator<E> takeWhileIterator(Iterator<E> iterator, Predicate<E> predicate) {
        return takeWhileIterator(peeking(iterator), predicate);
    }

    static <E> Iterator<E> takeWhileIterator(PeekingIterator<E> iterator, Predicate<E> predicate) {
        return new Iterator<E>() {

            private final PeekingIterator<E> peeking = iterator;
            private PredicateCheckState state = PredicateCheckState.NOT_TESTED;

            @Override
            public boolean hasNext() {
                // Short circuit since its not possible
                if (!peeking.hasNext()) {
                    state = PredicateCheckState.NOT_TESTED;
                    return false;
                }

                // We want the predicate test to be deterministic per element
                // so we need to cache the result until we advance the iterator.
                // An example predicate would be a counting predicate.
                if (PredicateCheckState.NOT_TESTED == state) {
                    boolean stop = predicate.test(peeking.peek());
                    if (stop) {
                        state = PredicateCheckState.ONE_MORE;
                    } else {
                        state = PredicateCheckState.MORE;
                    }
                }

                if (state == PredicateCheckState.NO_MORE) {
                    return false;
                }
                return true;
            }

            @Override
            public E next() {
                if (PredicateCheckState.ONE_MORE == state) {
                    state = PredicateCheckState.NO_MORE;
                } else {
                    state = PredicateCheckState.NOT_TESTED;
                }
                return peeking.next();
            }
        };
    }

    static class PeekingIteratorImpl<E> implements PeekingIterator<E> {
        private final Iterator<E> delegate;
        private E headElement;
        private boolean hasPeeked;

        PeekingIteratorImpl(Iterator<E> delegate) {
            this.delegate = delegate;
            this.headElement = null;
            this.hasPeeked = false;
        }

        @Override
        public E peek() {
            if (hasPeeked) {
                return headElement;
            }
            headElement = delegate.next();
            hasPeeked = true;
            return headElement;
        }

        @Override
        public boolean hasNext() {
            return hasPeeked || delegate.hasNext();
        }

        @Override
        public E next() {
            if (hasPeeked) {
                hasPeeked = false;
                return headElement;
            }
            return delegate.next();
        }
    }

    static <T> Predicate<T> countingPredicate(long count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must be greater than 1");
        }
        long[] countArray = new long[1];
        countArray[0] = 0L;
        long iterations = count -1;
        return (T t) -> {
            if (iterations == countArray[0]) {
                countArray[0] = 0L;
                return true;
            }
            countArray[0] = countArray[0] + 1;
            return false;
        };
    }
}
