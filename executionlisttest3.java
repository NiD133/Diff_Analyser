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

public class ExecutionListTestTest3 extends TestCase {

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

    public void testExecute_idempotentConcurrently() throws InterruptedException {
        CountDownLatch okayToRun = new CountDownLatch(1);
        AtomicInteger runCalled = new AtomicInteger();
        list.add(new Runnable() {

            @Override
            public void run() {
                try {
                    okayToRun.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
                runCalled.getAndIncrement();
            }
        }, directExecutor());
        Runnable execute = new Runnable() {

            @Override
            public void run() {
                list.execute();
            }
        };
        Thread thread1 = new Thread(execute);
        Thread thread2 = new Thread(execute);
        thread1.start();
        thread2.start();
        assertEquals(0, runCalled.get());
        okayToRun.countDown();
        thread1.join();
        thread2.join();
        assertEquals(1, runCalled.get());
    }
}
