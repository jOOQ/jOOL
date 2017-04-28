package org.jooq.lambda;

import java.util.Spliterator;

/**
 * Special subinterface of <code>Spliterator</code> that provides default implementations
 * for all the methods that are not relevant to sequential traversal.
 *
 * @see java.util.stream.BaseStream#sequential
 *
 * @author Tomasz Linkowski
 */
@FunctionalInterface
public interface SequentialSpliterator<T> extends Spliterator<T> {

    /**
     * This spliterator cannot be split.
     */
    @Override
    default Spliterator<T> trySplit() {
        return null;
    }

    /**
     * This spliterator has unknown estimate size.
     */
    @Override
    default long estimateSize() {
        return Long.MAX_VALUE;
    }

    /**
     * This spliterator is treated as ordered because it is <code>sequential</code>.
     */
    @Override
    default int characteristics() {
        return Spliterator.ORDERED;
    }
}
