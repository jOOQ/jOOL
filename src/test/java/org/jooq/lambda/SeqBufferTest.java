package org.jooq.lambda;

import org.jooq.lambda.tuple.Tuple2;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.Spliterator;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tomasz Linkowski
 */
public class SeqBufferTest {

    private final List<Integer> list = asList(1, 2, 3, 4, 5);

    private SeqBuffer<Integer> buffer() {
        return SeqBuffer.of(list.stream());
    }

    @Test
    public void testBufferingSpliteratorFollowedByNonBufferingOne() {
        SeqBuffer<Integer> buffer = buffer();

        assertEquals(list, buffer.seq().toList()); // call 1: buffering spliterator + complete consumption
        assertEquals(list, buffer.seq().toList()); // call 2: buffered spliterator + complete consumption
    }

    @Test
    public void testTwoBufferingSpliteratorsConsumedOneAfterAnother() {
        SeqBuffer<Integer> buffer = buffer();

        Seq<Integer> seq1 = buffer.seq(); // call 1: buffering spliterator
        Seq<Integer> seq2 = buffer.seq(); // call 2: buffering spliterator

        assertEquals(list, seq1.toList()); // complete consumption of 1
        assertEquals(list, seq2.toList()); // complete consumption of 2

        assertEquals(list, buffer.seq().toList()); // call 3: buffered spliterator + complete consumption
    }

    @Test
    public void testTwoBufferingSpliteratorsConsumedInParallel() {
        SeqBuffer<Integer> buffer = buffer();

        List<Tuple2<Integer, Integer>> expected = Seq.zip(list, list).toList();
        // calls 1 & 2: two buffering spliterators and parallel consumption of both
        List<Tuple2<Integer, Integer>> actual = Seq.zip(buffer.seq(), buffer.seq()).toList();
        assertEquals(expected, actual);

        assertEquals(list, buffer.seq().toList()); // call 3: buffered spliterator + complete consumption
    }

    @Test
    public void testTwoBufferedSpliteratorsConsumedInterchangeably() {
        SeqBuffer<Integer> buffer = buffer();

        // consume some x, then some y, then again some x, etc.
        Spliterator<Integer> x = buffer.seq().spliterator(); // call 1: buffering spliterator
        assertTrue(x.tryAdvance(i -> verifyInt(1, i)));
        assertTrue(x.tryAdvance(i -> verifyInt(2, i)));
        Spliterator<Integer> y = buffer.seq().spliterator(); // call 2: buffering spliterator
        assertTrue(y.tryAdvance(i -> verifyInt(1, i)));
        assertTrue(x.tryAdvance(i -> verifyInt(3, i)));
        assertTrue(y.tryAdvance(i -> verifyInt(2, i)));
        assertTrue(y.tryAdvance(i -> verifyInt(3, i)));
        assertTrue(y.tryAdvance(i -> verifyInt(4, i)));
        assertTrue(x.tryAdvance(i -> verifyInt(4, i)));
        assertTrue(x.tryAdvance(i -> verifyInt(5, i)));
        assertTrue(y.tryAdvance(i -> verifyInt(5, i)));
        assertFalse(x.tryAdvance(AssertionError::new));
        assertFalse(y.tryAdvance(AssertionError::new));

        assertEquals(list, buffer.seq().toList()); // call 3: buffered spliterator + complete consumption
    }

    @Test
    public void testThreadSafetyDuringParallelConsumption() {
        int numThreads = 10;
        int maxVariation = 100;
        int numElements = 10_000;

        Supplier<Seq<Integer>> s = () -> Seq.range(0, numElements);
        SeqBuffer<Integer> buffer = SeqBuffer.of(s.get());

        class TestThread extends Thread {
            private final Random random = new Random();
            volatile List<Integer> actualList;

            @Override
            public void run() {
                actualList = buffer.seq().peek(i -> randomWait()).toList();
            }

            private void randomWait() {
                for (int i = 0, variation = random.nextInt(maxVariation); i < variation; i++) {
                }
            }
        }

        List<TestThread> testThreads = Seq.generate(() -> new TestThread()).limit(numThreads).toList();
        testThreads.forEach(Thread::start);
        testThreads.forEach(Unchecked.consumer(Thread::join));

        List<Integer> expectedList = s.get().toList();
        for (TestThread testThread : testThreads) {
            assertEquals(expectedList, testThread.actualList);
        }
    }

    private void verifyInt(int expected, int actual) {
        assertEquals("Unexpected value", expected, actual);
    }
}
