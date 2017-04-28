package org.jooq.lambda;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author Tomasz Linkowski
 */
public class LazyFlatMapperTest {

    @Test
    public void testSimpleSeq() {
        List<Integer> peekedRegular = obtainElementsPeekedInOrderToFindFirst(simpleFlatMapSeq(false));
        assertEquals(asList(2, 4, 6), peekedRegular); // at least 2 unnecessary elements

        List<Integer> peekedLazy = obtainElementsPeekedInOrderToFindFirst(simpleFlatMapSeq(true));
        assertEquals(asList(2), peekedLazy);

        testNecessaryEquality(this::simpleFlatMapSeq);
    }

    @Test
    public void testDoublyNestedSeq() {
        List<Integer> peekedRegular = obtainElementsPeekedInOrderToFindFirst(doublyNestedFlatMapSeq(false));
        assertEquals(asList(2, 4, 6, 4, 8, 12, 6, 12, 18), peekedRegular); // at least 8 unnecessary elements

        List<Integer> peekedLazy = obtainElementsPeekedInOrderToFindFirst(doublyNestedFlatMapSeq(true));
        assertEquals(asList(2), peekedLazy);

        testNecessaryEquality(this::doublyNestedFlatMapSeq);
    }

    @Test
    public void testVeryComplexSeq() {
        List<Integer> peekedRegular = obtainElementsPeekedInOrderToFindFirst(veryComplexFlatMapSeq(false));
        assertEquals(asList(8, 16, 24, 6, 12, 18, 10, 20, 30), peekedRegular); // at least 8 unnecessary elements

        List<Integer> peekedLazy = obtainElementsPeekedInOrderToFindFirst(veryComplexFlatMapSeq(true));
        assertEquals(asList(8), peekedLazy);

        testNecessaryEquality(this::veryComplexFlatMapSeq);
    }

    @Test
    public void testSeqClosesAllResources() {
        testCloseableFlatMapSeq(
              regularSeq -> {
                  List<Integer> peekedRegular = obtainElementsPeekedInOrderToFindFirst(regularSeq);
                  assertEquals(asList(2, 4, 6), peekedRegular);
              },
              lazySeq -> {
                  List<Integer> peekedLazy = obtainElementsPeekedInOrderToFindFirst(lazySeq);
                  assertEquals(asList(2), peekedLazy);
              }
        );

        testCloseableFlatMapSeq(Collectable::toList);
        testCloseableFlatMapSeq(seq -> seq.limit(5).toList());

        testNecessaryEquality(lazy -> simpleCloseableFlatMapSeq(lazy, createIdentitySet()));
    }

    /**
     * Returns all the elements traversed until <code>findFirst()</code> returned its result.
     *
     * This represents the difference between regular <code>flatMap</code> from proposed <code>flatMapLazily</code>
     * when it was the last operation perform on the stream.
     */
    private <T> List<T> obtainElementsPeekedInOrderToFindFirst(Stream<T> stream) {
        List<T> peeked = new ArrayList<>();
        stream.peek(peeked::add).findFirst();
        return peeked;
    }

    private void testCloseableFlatMapSeq(TestOperation<Integer> seqTestOp) {
        testCloseableFlatMapSeq(seqTestOp, seqTestOp);
    }

    /**
     * Tests consumption of simple (singly-nested) regular and lazy seqs within try-with-resources block,
     * calling provided test operations.
     *
     * Then, after both Seqs have been consumed, ensures the same number of Seqs have been closed in order to make
     * sure that the lazy implementation indeed closes all the resources it should.
     */
    private void testCloseableFlatMapSeq(TestOperation<Integer> regularSeqTestOp, TestOperation<Integer> lazySeqTestOp) {
        Set<Seq<Integer>> regularClosedSeqs = createIdentitySet();
        try (Seq<Integer> regularSeq = simpleCloseableFlatMapSeq(false, regularClosedSeqs)) {
            regularSeqTestOp.consumeSeq(regularSeq);
        }

        Set<Seq<Integer>> lazyClosedSeqs = createIdentitySet();
        try (Seq<Integer> lazySeq = simpleCloseableFlatMapSeq(true, lazyClosedSeqs)) {
            lazySeqTestOp.consumeSeq(lazySeq);
        }

        assertEquals("closed resources", regularClosedSeqs.size(), lazyClosedSeqs.size());
    }

    /**
     * Runs various terminal operations on both regular and lazy seq and tests that they return the same value.
     */
    private void testNecessaryEquality(TestSeqProvider<Integer> seqProvider) {
        testEquality(seqProvider, Collectable::toList);
        testEquality(seqProvider, Seq::findFirst); // short-circuiting
        testEquality(seqProvider, Seq::findLast);
        testEquality(seqProvider, Seq::sum);

        testEquality(seqProvider, seq -> seq.anyMatch(i -> i == 8)); // short-circuiting
        testEquality(seqProvider, seq -> seq.allMatch(i -> i == 8)); // short-circuiting
        testEquality(seqProvider, seq -> seq.allMatch(i -> i > 0)); // short-circuiting
        testEquality(seqProvider, seq -> seq.limit(5).anyMatch(i -> i == 8)); // short-circuiting
        testEquality(seqProvider, seq -> seq.limit(5).toList());
        testEquality(seqProvider, seq -> seq.filter(i -> i % 2 == 0).toList());
    }

    /**
     * Runs given terminal operation on both regular and lazy seq and test that they return the same value.
     */
    private <R> void testEquality(TestSeqProvider<Integer> seqProvider, TestValueOperation<Integer, R> operation) {
        Seq<Integer> regularSeq = seqProvider.provideSeq(false);
        Seq<Integer> lazySeq = seqProvider.provideSeq(true);
        assertEquals(operation.applyToSeq(regularSeq), operation.applyToSeq(lazySeq));
    }

    private <T> Set<Seq<T>> createIdentitySet() {
        return Collections.newSetFromMap(new IdentityHashMap<>());
    }

    private <T> Seq<T> registerCloseHandler(Seq<T> seq, Set<Seq<T>> closedSeqs) {
        return seq.onClose(() -> closedSeqs.add(seq));
    }

    /**
     * Represents any of the method with suffix <code>*FlatMapSeq</code>.
     */
    @FunctionalInterface
    private interface TestSeqProvider<T> {
        Seq<T> provideSeq(boolean lazy);
    }

    /**
     * Represents a function that is applied to get a result from both regular and lazy seq,
     * so that these results can be compared for equality.
     */
    @FunctionalInterface
    private interface TestValueOperation<T, R> {
        R applyToSeq(Seq<T> seq);
    }

    /**
     * Represents an operation testing this seq, e.g. using assertions.
     */
    @FunctionalInterface
    private interface TestOperation<T> {
        void consumeSeq(Seq<T> seq);
    }

    //
    // TEST DATA
    //
    private Seq<Integer> testSeq() {
        return Seq.of(2, 3, 4);
    }

    private Seq<Integer> testMap(Integer i) {
        return Seq.of(i, 2 * i, 3 * i);
    }

    /**
     * Simplest flat map operation (flat map called once).
     */
    private Seq<Integer> simpleFlatMapSeq(boolean lazy) {
        return lazy
              ? testSeq().flatMapLazily(this::testMap)
              : testSeq().flatMap(this::testMap);
    }

    /**
     * Flat map called twice (two levels of flat-mapping).
     */
    private Seq<Integer> doublyNestedFlatMapSeq(boolean lazy) {
        return lazy
              ? testSeq().flatMapLazily(this::testMap).flatMapLazily(this::testMap)
              : testSeq().flatMap(this::testMap).flatMap(this::testMap);
    }

    /**
     * Flat map called a few times, among many other operations.
     */
    private Seq<Integer> veryComplexFlatMapSeq(boolean lazy) {
        if (lazy) {
            return testSeq().flatMapLazily(this::testMap).limit(6)
                  .append(testSeq().filter(i -> i % 2 == 0).onEmpty(2).limitUntil(i -> i > 50))
                  .flatMapLazily(i -> testMap(i).flatMapLazily(this::testMap).skip(2).limitWhile(j -> j < 10))
                  .map(i -> i + 2)
                  .flatMapLazily(this::testMap);
        } else {
            return testSeq().flatMap(this::testMap).limit(6)
                  .append(testSeq().filter(i -> i % 2 == 0).onEmpty(2).limitUntil(i -> i > 50))
                  .flatMap(i -> testMap(i).flatMap(this::testMap).skip(2).limitWhile(j -> j < 10))
                  .map(i -> i + 2)
                  .flatMap(this::testMap);
        }
    }

    /**
     * Simple flat map but with registered close handlers for all Seqs. These close handlers trace whether given Seq
     * has been closed by adding such Seq to the <code>closedSeqs</code> identity set.
     */
    private Seq<Integer> simpleCloseableFlatMapSeq(boolean lazy, Set<Seq<Integer>> closedSeqs) {
        Seq<Integer> closeableSeq = registerCloseHandler(testSeq(), closedSeqs);
        Function<Integer, Stream<Integer>> closeableMapper = i -> registerCloseHandler(testMap(i), closedSeqs);
        return lazy
              ? closeableSeq.flatMapLazily(closeableMapper)
              : closeableSeq.flatMap(closeableMapper);
    }
}
