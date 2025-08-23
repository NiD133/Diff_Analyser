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
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link BackgroundInitializer}.
 * This class focuses on the state of the initializer before it is started.
 */
public class BackgroundInitializerTest extends AbstractLangTest {

    /**
     * A test-specific implementation of BackgroundInitializer that provides
     * control over the initialization process for testing purposes.
     */
    protected static class TestBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {

        /** An exception to be thrown by initialize(), null means no exception. */
        private Exception exceptionToThrow;

        /** A flag to make the background task sleep, simulating a long-running task. */
        private boolean shouldSleep;

        /** A latch to pause the initialization until explicitly released. */
        private final CountDownLatch completionLatch = new CountDownLatch(1);

        /** A flag to indicate if the initialize() method should wait for the latch. */
        private boolean shouldWaitForLatch;

        /** The object that tracks the state of the initialization. */
        private final CloseableCounter counter = new CloseableCounter();

        TestBackgroundInitializer() {
            // Default constructor
        }

        TestBackgroundInitializer(final ExecutorService exec) {
            super(exec);
        }

        /**
         * The main background task.
         * <p>
         * This implementation can be configured to throw an exception, sleep, or wait
         * on a latch to facilitate testing of various concurrent scenarios.
         * </p>
         *
         * @return the counter after incrementing its call count.
         * @throws Exception if an exception was configured to be thrown.
         */
        @Override
        protected CloseableCounter initialize() throws Exception {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            if (shouldSleep) {
                ThreadUtils.sleep(Duration.ofMinutes(1));
            }
            if (shouldWaitForLatch) {
                completionLatch.await();
            }
            return counter.increment();
        }

        // Methods to control the test behavior

        public void setExceptionToThrow(final Exception ex) {
            this.exceptionToThrow = ex;
        }

        public void setShouldSleep(final boolean sleep) {
            this.shouldSleep = sleep;
        }

        public void enableLatch() {
            shouldWaitForLatch = true;
        }

        public void releaseLatch() {
            completionLatch.countDown();
        }

        public CloseableCounter getCloseableCounter() {
            return counter;
        }
    }

    /**
     * A helper class for testing that tracks the number of initialization calls
     * and whether a 'close' operation has been performed.
     */
    protected static class CloseableCounter {
        private final AtomicInteger initializeCalls = new AtomicInteger();
        private final AtomicBoolean closed = new AtomicBoolean();

        public CloseableCounter increment() {
            initializeCalls.incrementAndGet();
            return this;
        }

        public void close() {
            closed.set(true);
        }

        public int getInitializeCalls() {
            return initializeCalls.get();
        }

        public boolean isClosed() {
            return closed.get();
        }
    }

    /**
     * Factory method to create a new test initializer.
     * @return a new instance of {@link TestBackgroundInitializer}.
     */
    protected TestBackgroundInitializer createTestInitializer() {
        return new TestBackgroundInitializer();
    }

    /**
     * Factory method to create a new test initializer with a specific executor.
     * @param exec The executor service to use.
     * @return a new instance of {@link TestBackgroundInitializer}.
     */
    protected TestBackgroundInitializer createTestInitializer(final ExecutorService exec) {
        return new TestBackgroundInitializer(exec);
    }

    /**
     * Helper method to verify that the initialize() method was called exactly once.
     * This check is performed after the initializer's get() method has been called.
     *
     * @param initializer the initializer to test.
     */
    private void checkInitialize(final TestBackgroundInitializer initializer) throws ConcurrentException {
        final CloseableCounter result = initializer.get();
        assertEquals(1, result.getInitializeCalls(), "initialize() should have been called once.");
        assertEquals(1, initializer.getCloseableCounter().getInitializeCalls(), "The internal counter should be 1.");
        assertNotNull(initializer.getFuture(), "A Future object should have been created.");
    }

    /**
     * Tests that getActiveExecutor() returns null before the initializer has been started.
     */
    @Test
    void getActiveExecutorShouldReturnNullBeforeStart() {
        // Arrange
        final TestBackgroundInitializer initializer = createTestInitializer();

        // Act & Assert
        assertNull(initializer.getActiveExecutor(), "The active executor should be null before start() is called.");
    }
}