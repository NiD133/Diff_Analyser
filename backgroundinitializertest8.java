package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.AbstractLangTest;
import org.apache.commons.lang3.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class BackgroundInitializerTestTest8 extends AbstractLangTest {

    /**
     * Helper method for checking whether the initialize() method was correctly
     * called. start() must already have been invoked.
     *
     * @param init the initializer to test
     */
    private void checkInitialize(final AbstractBackgroundInitializerTestImpl init) throws ConcurrentException {
        final Integer result = init.get().getInitializeCalls();
        assertEquals(1, result.intValue(), "Wrong result");
        assertEquals(1, init.getCloseableCounter().getInitializeCalls(), "Wrong number of invocations");
        assertNotNull(init.getFuture(), "No future");
    }

    protected AbstractBackgroundInitializerTestImpl getBackgroundInitializerTestImpl() {
        return new MethodBackgroundInitializerTestImpl();
    }

    protected AbstractBackgroundInitializerTestImpl getBackgroundInitializerTestImpl(final ExecutorService exec) {
        return new MethodBackgroundInitializerTestImpl(exec);
    }

    /**
     * A concrete implementation of BackgroundInitializer. It also overloads
     * some methods that simplify testing.
     */
    protected static class AbstractBackgroundInitializerTestImpl extends BackgroundInitializer<CloseableCounter> {

        /**
         * An exception to be thrown by initialize().
         */
        Exception ex;

        /**
         * A flag whether the background task should sleep a while.
         */
        boolean shouldSleep;

        /**
         * A latch tests can use to control when initialize completes.
         */
        final CountDownLatch latch = new CountDownLatch(1);

        boolean waitForLatch;

        /**
         * An object containing the state we are testing
         */
        CloseableCounter counter = new CloseableCounter();

        AbstractBackgroundInitializerTestImpl() {
        }

        AbstractBackgroundInitializerTestImpl(final ExecutorService exec) {
            super(exec);
        }

        public void enableLatch() {
            waitForLatch = true;
        }

        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        /**
         * Records this invocation. Optionally throws an exception or sleeps a
         * while.
         *
         * @throws Exception in case of an error
         */
        protected CloseableCounter initializeInternal() throws Exception {
            if (ex != null) {
                throw ex;
            }
            if (shouldSleep) {
                ThreadUtils.sleep(Duration.ofMinutes(1));
            }
            if (waitForLatch) {
                latch.await();
            }
            return counter.increment();
        }

        public void releaseLatch() {
            latch.countDown();
        }
    }

    protected static class CloseableCounter {

        /**
         * The number of invocations of initialize().
         */
        AtomicInteger initializeCalls = new AtomicInteger();

        /**
         * Has the close consumer successfully reached this object.
         */
        AtomicBoolean closed = new AtomicBoolean();

        public void close() {
            closed.set(true);
        }

        public int getInitializeCalls() {
            return initializeCalls.get();
        }

        public CloseableCounter increment() {
            initializeCalls.incrementAndGet();
            return this;
        }

        public boolean isClosed() {
            return closed.get();
        }
    }

    protected static class MethodBackgroundInitializerTestImpl extends AbstractBackgroundInitializerTestImpl {

        MethodBackgroundInitializerTestImpl() {
        }

        MethodBackgroundInitializerTestImpl(final ExecutorService exec) {
            super(exec);
        }

        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    /**
     * Tests that a thread blocked on get() can be interrupted, resulting in a ConcurrentException.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("get() should throw ConcurrentException with an InterruptedException cause when the waiting thread is interrupted")
    void testGetWhenWaitingThreadIsInterrupted() throws InterruptedException {
        // Arrange
        // Create an initializer whose background task will block indefinitely to simulate a long-running initialization.
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final AbstractBackgroundInitializerTestImpl initializer = getBackgroundInitializerTestImpl(executorService);
        initializer.shouldSleep = true; // Makes the initialize() method block for a long time.
        initializer.start();

        // We need a separate thread to call get() so the main test thread can interrupt it.
        // An AtomicReference is used to capture the exception thrown in the other thread.
        // A CountDownLatch is used to safely wait for the thread to complete.
        final AtomicReference<ConcurrentException> capturedException = new AtomicReference<>();
        final CountDownLatch threadCompletionLatch = new CountDownLatch(1);

        final Thread waitingThread = new Thread(() -> {
            try {
                // This call will block until the thread is interrupted.
                initializer.get();
            } catch (final ConcurrentException e) {
                // This is the expected exception. We capture it for later assertions.
                capturedException.set(e);
            } finally {
                // The get() method should re-set the thread's interrupted status after catching InterruptedException.
                assertTrue(Thread.currentThread().isInterrupted(), "Thread's interrupted status should be true after get().");
                // Signal that this thread has finished its work.
                threadCompletionLatch.countDown();
            }
        });

        // Act
        waitingThread.start();
        // Give the thread a moment to enter the blocking get() call before we interrupt it.
        Thread.sleep(100);
        waitingThread.interrupt();

        // Assert
        // Wait for the waitingThread to complete. The @Timeout annotation prevents the test from hanging indefinitely.
        threadCompletionLatch.await();

        // Verify that the correct exception was caught and has the correct cause.
        final ConcurrentException thrownException = capturedException.get();
        assertNotNull(thrownException, "A ConcurrentException should have been thrown.");
        assertEquals(InterruptedException.class, thrownException.getCause().getClass(), "The cause should be an InterruptedException.");

        // Cleanup
        executorService.shutdownNow();
    }
}