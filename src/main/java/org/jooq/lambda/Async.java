package org.jooq.lambda;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public final class Async {
    private Async() {

    }

    public static <U> CompletionStage<U> supplyAsync(Supplier<U> supplier) {
        return SameExecutorCompletionStage.of(CompletableFuture.supplyAsync(supplier), null);
    }
    
    public static <U> CompletionStage<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return SameExecutorCompletionStage.of(CompletableFuture.supplyAsync(supplier, executor), executor);
    }

    public static CompletionStage<Void> runAsync(Runnable runnable, Executor executor) {
        return SameExecutorCompletionStage.of(CompletableFuture.runAsync(runnable, executor), executor);
    }
    
    public static CompletionStage<Void> runAsync(Runnable runnable) {
        return SameExecutorCompletionStage.of(CompletableFuture.runAsync(runnable), null);
    }
}
