package org.jooq.lambda;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

final class ThreadFactories {
    private ThreadFactories() {

    }

    public static ThreadFactory daemon() {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            }
        };
    }
}
