package cn.nukkit.scheduler;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nukkit Project Team
 */
public class AsyncPool extends ThreadPoolExecutor {

    public AsyncPool() {
        super(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<>(), new Factory());
    }

    private static class Factory implements ThreadFactory {

        private AtomicInteger count = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread("Plugin Task Thread #" + count.getAndIncrement());
        }

    }

}
