package org.jooq.lambda;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
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
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.thenApply(Function.identity());
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.thenApplyAsync(latchIdentity(latch2));
        thirdCompletionStage.toCompletableFuture().join();
        latch2.await();
        assertHits(executorA, 2);
        
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.thenApplyAsync(latchIdentity(latch3), executorB);
        latch3.await();
        fourthCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    @Test
    public void testThenAccept() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.thenAccept(noopConsumer());
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.thenAcceptAsync(latchConsumer(latch2));
        latch2.await();
        thirdCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.thenAcceptAsync(latchConsumer(latch3), executorB);
        latch3.await();
        fourthCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    @Test
    public void testThenRun() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.thenRun(noopRunnable());
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.thenRunAsync(latchRunnable(latch2));
        latch2.await();
        thirdCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.thenRunAsync(latchRunnable(latch3), executorB);
        latch3.await();
        fourthCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    @Test
    public void testThenCombine() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        BiFunction<Void, Void, Void> bf = (Void x, Void y) -> null;
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.thenCombine(completedFuture(), bf);
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        BiFunction<Void, Void, Void> bf2 = (Void x, Void y) -> {latch2.countDown(); return null;};
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.thenCombineAsync(completedFuture(), bf2);
        latch2.await();
        thirdCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        
        BiFunction<Void, Void, Void> bf3 = (Void x, Void y) -> {latch3.countDown(); return null;};
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.thenCombineAsync(completedFuture(), bf3, executorB);
        latch3.await();
        
        fourthCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    @Test
    public void testThenAcceptBoth() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        BiConsumer<Void, Void> bc = (Void x, Void y) -> {};
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.thenAcceptBoth(completedFuture(), bc);
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        BiConsumer<Void, Void> bf2 = (Void x, Void y) -> {latch2.countDown();};
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.thenAcceptBothAsync(completedFuture(), bf2);
        latch2.await();
        thirdCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        
        BiConsumer<Void, Void> bf3 = (Void x, Void y) -> {latch3.countDown();};
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.thenAcceptBothAsync(completedFuture(), bf3, executorB);
        latch3.await();
        fourthCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    @Test
    public void testRunAfterBoth() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.runAfterBoth(completedFuture(), noopRunnable());
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.runAfterBothAsync(completedFuture(), latchRunnable(latch2));
        latch2.await();
        thirdCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.runAfterBothAsync(completedFuture(), latchRunnable(latch3), executorB);
        latch3.await();
        fourthCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    @Test
    public void testApplyToEither() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.applyToEither(neverCompleteFuture(), Function.identity());
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.applyToEitherAsync(neverCompleteFuture(), latchIdentity(latch2));
        latch2.await();
        thirdCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.applyToEitherAsync(neverCompleteFuture(), latchIdentity(latch3), executorB);
        latch3.await();
        fourthCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    @Test
    public void testAcceptEither() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.acceptEither(neverCompleteFuture(), noopConsumer());
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.acceptEitherAsync(neverCompleteFuture(), latchConsumer(latch2));
        latch2.await();
        thirdCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.acceptEitherAsync(neverCompleteFuture(), latchConsumer(latch3), executorB);
        latch3.await();
        fourthCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    @Test
    public void testRunAfterEither() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.runAfterEither(neverCompleteFuture(), noopRunnable());
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.runAfterEitherAsync(neverCompleteFuture(), latchRunnable(latch2));
        latch2.await();
        thirdCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.runAfterEitherAsync(neverCompleteFuture(), latchRunnable(latch3), executorB);
        latch3.await();
        fourthCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    @Test
    public void testThenCompose() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        Function<Void, CompletableFuture<Void>> fn = x -> CompletableFuture.completedFuture(x);
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.thenCompose(fn);
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        Function<Void, CompletableFuture<Void>> fn2 = x -> {
            latch2.countDown();
            return CompletableFuture.completedFuture(x);
        };
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.thenComposeAsync(fn2);
        latch2.await();
        thirdCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        
        Function<Void, CompletableFuture<Void>> fn3 = x -> {
            latch3.countDown();
            return CompletableFuture.completedFuture(x);
        };
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.thenComposeAsync(fn3, executorB);
        latch3.await();
        fourthCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }
    
    @Test
    public void testExceptionally() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        firstCompletionStage = firstCompletionStage.exceptionally(ex -> null);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
    }
    
    @Test
    public void testWhenComplete() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);

        BiConsumer<Void, Throwable> bc = (Void x, Throwable y) -> {};
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.whenComplete(bc);
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);

        BiConsumer<Void, Throwable> bc2 = (Void x, Throwable y) -> {latch2.countDown();};
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.whenCompleteAsync(bc2);
        latch2.await();
        thirdCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);

        BiConsumer<Void, Throwable> bc3 = (Void x, Throwable y) -> {latch3.countDown();};
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.whenCompleteAsync(bc3, executorB);
        latch3.await();
        fourthCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        assertHits(executorB, 1);
    }

    @Test
    public void testHandle() throws InterruptedException {
        CompletionStage<Void> firstCompletionStage = Async.supplyAsync(latchSupplier(latch1), executorA);
        latch1.await();
        firstCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        BiFunction<Void, Throwable, Void> bf = (Void x, Throwable y) -> null;
        CompletionStage<Void> secondCompletionStage = firstCompletionStage.handle(bf);
        secondCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 1);
        
        BiFunction<Void, Throwable, Void> bf2 = (Void x, Throwable y) -> {latch2.countDown(); return null;};
        CompletionStage<Void> thirdCompletionStage = secondCompletionStage.handleAsync(bf2);
        latch2.await();
        thirdCompletionStage.toCompletableFuture().join();
        assertHits(executorA, 2);
        
        BiFunction<Void, Throwable, Void> bf3 = (Void x, Throwable y) -> {latch3.countDown(); return null;};
        CompletionStage<Void> fourthCompletionStage = thirdCompletionStage.handleAsync(bf3, executorB);
        latch3.await();
        fourthCompletionStage.toCompletableFuture().join();
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
    
    private Runnable noopRunnable() {
         return () -> {};
    }
    
    private <T> Consumer<T> latchConsumer(CountDownLatch latch) {
        return (x) -> latch.countDown();
    }
    
    private Runnable latchRunnable(CountDownLatch latch) {
        return () -> latch.countDown();
    }
    
    private void assertHits(CallCountingExecutor exec, int hits) {
        assertEquals(hits, exec.getCounter().get());
    }
    
    private CompletableFuture<Void> completedFuture() {
        return CompletableFuture.completedFuture(null);
    }
    
    private CompletableFuture<Void> neverCompleteFuture() {
        return new CompletableFuture<Void>();
    }
}
