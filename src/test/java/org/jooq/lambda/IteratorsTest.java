package org.jooq.lambda;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by billoneil on 7/26/17.
 */
public class IteratorsTest {

    @Test(expected = NoSuchElementException.class)
    public void testPeekingThrows() {
        Seq<Integer> seq1 = Seq.of();
        PeekingIterator<Integer> peeking1 = new Iterators.PeekingIteratorImpl<>(seq1.iterator());
        peeking1.next();
    }

    @Test
    public void testPeeking() {
        Seq<Integer> seq1 = Seq.of();
        PeekingIterator<Integer> peeking1 = new Iterators.PeekingIteratorImpl<>(seq1.iterator());
        assertFalse(peeking1.hasNext());

        Seq<Integer> seq2 = Seq.of(1, 2, 3);
        PeekingIterator<Integer> peeking2 = new Iterators.PeekingIteratorImpl<>(seq2.iterator());
        int i = 0;
        while (peeking2.hasNext()) {
            assertEquals(++i, (int) peeking2.next());
        }

        Seq<Integer> seq3 = Seq.of(1, 2, 3);
        PeekingIterator<Integer> peeking3 = new Iterators.PeekingIteratorImpl<>(seq3.iterator());
        assertEquals(1, (int) peeking3.peek());
        assertEquals(1, (int) peeking3.peek());
        assertEquals(1, (int) peeking3.next());
        assertEquals(2, (int) peeking3.next());
        assertEquals(3, (int) peeking3.peek());
        assertEquals(3, (int) peeking3.peek());
        assertEquals(3, (int) peeking3.next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountingPredicateThrowsIllegalArg() {
        Iterators.countingPredicate(0L);
    }

    @Test
    public void testCountingPredicate() {
        Predicate<Integer> oneCount = Iterators.countingPredicate(1L);
        assertTrue(oneCount.test(1));

        Predicate<Integer> tenCount = Iterators.countingPredicate(10L);
        Seq.range(0, 9).forEach(n -> {
            assertFalse(tenCount.test(n));
        });
        assertTrue(tenCount.test(1));
    }

    @Test
    public void testPredicateIterator() {
        Predicate<Integer> tenCount = Iterators.countingPredicate(10L);
        Seq<Integer> seq = Seq.range(0, 10).cycle();
        Iterator<Integer> it = Iterators.takeWhileIterator(seq.iterator(), tenCount);
        int i = 0;
        while (it.hasNext()) {
            // It should be safe to call hasNext multiple times.
            it.hasNext();
            it.next();
            i++;
        }
        assertEquals(10, i);


        Predicate<Integer> isEven = n -> n %2 == 0;
        Seq<Integer> seq2 = Seq.of(1,1,2,3).cycle();
        Iterator<Integer> it2 = Iterators.takeWhileIterator(seq2.iterator(), isEven);
        assertTrue(it2.hasNext());
        assertTrue(it2.hasNext());
        assertEquals(1, (int) it2.next());
        assertEquals(1, (int) it2.next());
        assertTrue(it2.hasNext());
        assertEquals(2, (int) it2.next());
        assertFalse(it2.hasNext());
    }
}
