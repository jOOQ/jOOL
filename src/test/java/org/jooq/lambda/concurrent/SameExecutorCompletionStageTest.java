package org.jooq.lambda.concurrent;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jooq.lambda.Unchecked;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SameExecutorCompletionStageTest {
    
    public SameExecutorCompletionStageTest() {
    }
    
    private static final CallCountingExecutor executorA = CallCountingExecutor.wrap(Executors.newFixedThreadPool(10, ThreadFactories.daemon()));
    private static final CallCountingExecutor executorB = CallCountingExecutor.wrap(Executors.newFixedThreadPool(10, ThreadFactories.daemon()));
    
    private CountDownLatch latch1;
    private CountDownLatch latch2;
    private CountDownLatch latch3;
    
    @Before
    public void setUp() {
        executorA.getCounter().set(0);
        executorB.getCounter().set(0);
        latch1 = new CountDownLatch(1);
        latch2 = new CountDownLatch(1);
        latch3 = new CountDownLatch(1);
    }

    @Test
    public void testThenApply() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        assertHits(executorA, 1);
        
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.thenApply(Function.identity());
        assertHits(executorA, 1);
        
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.thenApplyAsync(latchIdentity(latch2));
        latch2.await();
        assertHits(executorA, 2);
        
        thirdCompletionStage.thenApplyAsync(latchIdentity(latch3), executorB);
        latch3.await();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    @Test
    public void testThenAccept() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        assertHits(executorA, 1);
        
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.thenAccept(noopConsumer());
        assertHits(executorA, 1);
        
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.thenAcceptAsync(latchConsumer(latch2));
        latch2.await();
        assertHits(executorA, 2);
        
        thirdCompletionStage.thenAcceptAsync(latchConsumer(latch3), executorB);
        latch3.await();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    

    private Supplier<Void> latchSupplier(CountDownLatch latch) {
        return Unchecked.supplier(() -> {
            latch.countDown();
            return null;
        });
    }
    
    private <T> Function<T, T> latchIdentity(CountDownLatch latch) {
        return Unchecked.function(t -> {
            latch.countDown();
            return t;
        });
    }
    
    private <T> Consumer<T> noopConsumer() {
        return (x) -> {};
    }
    
    private <T> Consumer<T> latchConsumer(CountDownLatch latch) {
        return (x) -> latch.countDown();
    }
    
    private void assertHits(CallCountingExecutor exec, int hits) {
        assertEquals(hits, exec.getCounter().get());
    }
}
