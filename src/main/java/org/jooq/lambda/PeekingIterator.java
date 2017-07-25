package org.jooq.lambda;

import java.util.Iterator;

public interface PeekingIterator<E> extends Iterator<E> {
    /**
     * Returns the current head of the iterator without consuming it.
     * Multiple sequential calls to {@code peek()} will return the same element.
     * @return The current head of the iterator
     * @throws java.util.NoSuchElementException if there are no more elements
     */
    E peek();
}
