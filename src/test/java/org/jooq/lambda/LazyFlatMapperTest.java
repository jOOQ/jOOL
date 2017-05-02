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
        List<Integer> peekedOld = obtainElementsPeekedInOrderToFindFirst(simpleFlatMapSeq(true));
        assertEquals(asList(2, 4, 6), peekedOld); // at least 2 unnecessary elements

        List<Integer> peekedNew = obtainElementsPeekedInOrderToFindFirst(simpleFlatMapSeq(false));
        assertEquals(asList(2), peekedNew);

        testNecessaryEquality(this::simpleFlatMapSeq);
    }

    @Test
    public void testDoublyNestedSeq() {
        List<Integer> peekedOld = obtainElementsPeekedInOrderToFindFirst(doublyNestedFlatMapSeq(true));
        assertEquals(asList(2, 4, 6, 4, 8, 12, 6, 12, 18), peekedOld); // at least 8 unnecessary elements

        List<Integer> peekedNew = obtainElementsPeekedInOrderToFindFirst(doublyNestedFlatMapSeq(false));
        assertEquals(asList(2), peekedNew);

        testNecessaryEquality(this::doublyNestedFlatMapSeq);
    }

    @Test
    public void testVeryComplexSeq() {
        List<Integer> peekedOld = obtainElementsPeekedInOrderToFindFirst(veryComplexFlatMapSeq(true));
        assertEquals(asList(8, 16, 24, 6, 12, 18, 10, 20, 30), peekedOld); // at least 8 unnecessary elements

        List<Integer> peekedNew = obtainElementsPeekedInOrderToFindFirst(veryComplexFlatMapSeq(false));
        assertEquals(asList(8), peekedNew);

        testNecessaryEquality(this::veryComplexFlatMapSeq);
    }

    @Test
    public void testSeqClosesAllResources() {
        testCloseableFlatMapSeq(
              oldSeq -> {
                  List<Integer> peekedOld = obtainElementsPeekedInOrderToFindFirst(oldSeq);
                  assertEquals(asList(2, 4, 6), peekedOld);
              },
              newSeq -> {
                  List<Integer> peekedNew = obtainElementsPeekedInOrderToFindFirst(newSeq);
                  assertEquals(asList(2), peekedNew);
              }
        );

        testCloseableFlatMapSeq(Collectable::toList);
        testCloseableFlatMapSeq(seq -> seq.limit(5).toList());

        testNecessaryEquality(lazy -> simpleCloseableFlatMapSeq(lazy, createIdentitySet()));
    }

    /**
     * Returns all the elements traversed until <code>findFirst()</code> returned its result.
     *
     * This represents the difference between old (regular) <code>flatMap</code> from proposed (lazy)
     * <code>flatMap</code> when it was the last operation performed on the stream.
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
     * Tests consumption of simple (singly-nested) old (regular) and new (lazy) seqs within try-with-resources block,
     * calling provided test operations.
     *
     * Then, after both Seqs have been consumed, ensures the same number of Seqs have been closed in order to make
     * sure that the lazy implementation indeed closes all the resources it should.
     */
    private void testCloseableFlatMapSeq(TestOperation<Integer> oldSeqTestOp, TestOperation<Integer> newSeqTestOp) {
        Set<Seq<Integer>> oldClosedSeqs = createIdentitySet();
        try (Seq<Integer> regularSeq = simpleCloseableFlatMapSeq(true, oldClosedSeqs)) {
            oldSeqTestOp.consumeSeq(regularSeq);
        }

        Set<Seq<Integer>> newClosedSeqs = createIdentitySet();
        try (Seq<Integer> lazySeq = simpleCloseableFlatMapSeq(false, newClosedSeqs)) {
            newSeqTestOp.consumeSeq(lazySeq);
        }

        assertEquals("closed resources", oldClosedSeqs.size(), newClosedSeqs.size());
    }

    /**
     * Runs various terminal operations on both old (regular) and new (lazy) seq
     * and tests that they return the same value.
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
     * Runs given terminal operation on both old (regular) and new (lazy) seq and test that they return the same value.
     */
    private <R> void testEquality(TestSeqProvider<Integer> seqProvider, TestValueOperation<Integer, R> operation) {
        Seq<Integer> oldSeq = seqProvider.provideSeq(true);
        Seq<Integer> newSeq = seqProvider.provideSeq(false);
        assertEquals(operation.applyToSeq(oldSeq), operation.applyToSeq(newSeq));
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
        Seq<T> provideSeq(boolean old);
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
    private Seq<Integer> simpleFlatMapSeq(boolean old) {
        return old
              ? testSeq().transform(flatMapO(this::testMap))
              : testSeq().flatMap(this::testMap);
    }

    /**
     * Flat map called twice (two levels of flat-mapping).
     */
    private Seq<Integer> doublyNestedFlatMapSeq(boolean old) {
        return old
              ? testSeq().transform(flatMapO(this::testMap)).transform(flatMapO(this::testMap))
              : testSeq().flatMap(this::testMap).flatMap(this::testMap);
    }

    /**
     * Flat map called a few times, among many other operations.
     */
    private Seq<Integer> veryComplexFlatMapSeq(boolean old) {
        if (old) {
            return testSeq().transform(flatMapO(this::testMap)).limit(6)
                  .append(testSeq().filter(i -> i % 2 == 0).onEmpty(2).limitUntil(i -> i > 50))
                  .transform(flatMapO(i -> testMap(i).transform(flatMapO(this::testMap)).skip(2).limitWhile(j -> j < 10)))
                  .map(i -> i + 2)
                  .transform(flatMapO(this::testMap));
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
    private Seq<Integer> simpleCloseableFlatMapSeq(boolean old, Set<Seq<Integer>> closedSeqs) {
        Seq<Integer> closeableSeq = registerCloseHandler(testSeq(), closedSeqs);
        Function<Integer, Stream<Integer>> closeableMapper = i -> registerCloseHandler(testMap(i), closedSeqs);
        return old
              ? closeableSeq.transform(flatMapO(closeableMapper))
              : closeableSeq.flatMap(closeableMapper);
    }

    /**
     * Transformation that perform the old (non-lazy) flat map on given argument Seq.
     */
    private static <T, R> Function<Seq<T>, Seq<R>> flatMapO(Function<T, ? extends Stream<? extends R>> mapper) {
        return seq -> Seq.seq(seq.stream().flatMap(mapper));
    }
}
