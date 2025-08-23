package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import com.google.common.testing.NullPointerTester;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

public class ExecutionListTestTest1 extends TestCase {

    private final ExecutionList list = new ExecutionList();

    private static final Runnable THROWING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            throw new RuntimeException();
        }
    };

    private class MockRunnable implements Runnable {

        final CountDownLatch countDownLatch;

        MockRunnable(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            countDownLatch.countDown();
        }
    }

    public void testRunOnPopulatedList() throws Exception {
        Executor exec = newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(3);
        list.add(new MockRunnable(countDownLatch), exec);
        list.add(new MockRunnable(countDownLatch), exec);
        list.add(new MockRunnable(countDownLatch), exec);
        assertEquals(3L, countDownLatch.getCount());
        list.execute();
        // Verify that all of the runnables execute in a reasonable amount of time.
        assertTrue(countDownLatch.await(1L, SECONDS));
    }
}
