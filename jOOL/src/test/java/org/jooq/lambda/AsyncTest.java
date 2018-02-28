package org.jooq.lambda;

import static org.junit.Assert.assertNull;

import java.util.concurrent.CompletionStage;
import org.junit.Test;

public class AsyncTest {
    
    @Test
    public void testNoCustomExecutor() {
        CompletionStage<Void> completionStage = Async.runAsync(() -> {});
        assertNull(completionStage.toCompletableFuture().join());
        
        completionStage = Async.supplyAsync(() -> null);
        assertNull(completionStage.toCompletableFuture().join());
    }
}
