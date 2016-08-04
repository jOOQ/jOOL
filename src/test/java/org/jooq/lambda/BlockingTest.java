package org.jooq.lambda;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class BlockingTest {

    private static ForkJoinPool pool = new ForkJoinPool(1);

    /**
     * Test that {@link Blocking#runnable(java.lang.Runnable) } executes the given runnable.
     */
    @Test
    public void testRunnable_run() {
        AtomicBoolean executed = new AtomicBoolean(false);

        Runnable runnable = Blocking.runnable(() -> executed.set(true));

        CompletableFuture.runAsync(runnable, pool)
                .join();
        Assert.assertTrue("The given runnable has not been executed", executed.get());
    }


}
