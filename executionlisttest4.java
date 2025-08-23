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

public class ExecutionListTestTest4 extends TestCase {

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

    public void testAddAfterRun() throws Exception {
        // Run the previous test
        testRunOnPopulatedList();
        // If it passed, then verify an Add will be executed without calling run
        CountDownLatch countDownLatch = new CountDownLatch(1);
        list.add(new MockRunnable(countDownLatch), newCachedThreadPool());
        assertTrue(countDownLatch.await(1L, SECONDS));
    }
}
