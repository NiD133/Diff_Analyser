package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.AbstractLangTest;
import org.apache.commons.lang3.ThreadUtils;
import org.junit.jupiter.api.Test;

// The class was renamed for clarity, as the original name 'BackgroundInitializerTestTest13'
// appeared to be an artifact of test extraction.
public class BackgroundInitializerTest extends AbstractLangTest {

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

        /** An exception to be thrown by initialize(). */
        Exception ex;

        /** A flag whether the background task should sleep a while. */
        boolean shouldSleep;

        /** A latch tests can use to control when initialize completes. */
        final CountDownLatch latch = new CountDownLatch(1);

        boolean waitForLatch;

        /** An object containing the state we are testing. */
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

        /** The number of invocations of initialize(). */
        final AtomicInteger initializeCalls = new AtomicInteger();

        /** Has the close consumer successfully reached this object. */
        final AtomicBoolean closed = new AtomicBoolean();

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
     * Tests that isStarted() returns true even after the background task has completed.
     */
    @Test
    void isStarted_shouldReturnTrue_afterInitializationIsComplete() throws ConcurrentException {
        // Arrange: Create a test implementation of BackgroundInitializer.
        final AbstractBackgroundInitializerTestImpl initializer = getBackgroundInitializerTestImpl();

        // Act: Start the initializer and wait for it to complete by calling get().
        initializer.start();
        final CloseableCounter result = initializer.get(); // This call blocks until initialization is done.

        // Assert: Verify the state after completion.
        // First, confirm that initialization ran successfully as a precondition.
        assertNotNull(result, "The result of initialization should not be null.");
        assertEquals(1, result.getInitializeCalls(), "The initialize() method should have been called exactly once.");

        // Then, assert the main point of the test: isStarted() should still be true.
        assertTrue(initializer.isStarted(), "isStarted() should return true after the background task has finished.");
    }
}