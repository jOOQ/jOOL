package org.jooq.lambda;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

final class CallCountingExecutor implements Executor {
    private final AtomicInteger counter;
    private final Executor delegate;

    public static CallCountingExecutor wrap(Executor delegate) {
        return new CallCountingExecutor(new AtomicInteger(0), delegate);
    }
    
    CallCountingExecutor(AtomicInteger counter, Executor delegate) {
        this.counter = counter;
        this.delegate = delegate;
    }

    @Override
    public final void execute(Runnable command) {
        counter.accumulateAndGet(1, (left, right) -> {
            delegate.execute(command);
            return left + right;
        });
    }

    public final AtomicInteger getCounter() {
        return counter;
    }
}
