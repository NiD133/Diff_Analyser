package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.AbstractLangTest;
import org.apache.commons.lang3.ThreadUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of the start() method in BackgroundInitializer.
 */
@DisplayName("BackgroundInitializer start() method tests")
class BackgroundInitializerTestTest18 extends AbstractLangTest {

    private AbstractBackgroundInitializerTestImpl initializer;

    @BeforeEach
    void setUp() {
        initializer = new MethodBackgroundInitializerTestImpl();
    }

    /**
     * Tests that the start() method is effective only on its first invocation.
     * Subsequent calls should be ignored and return false.
     */
    @Test
    @DisplayName("start() should return true on first call and false on subsequent calls")
    void startShouldBeEffectiveOnlyOnFirstCall() throws ConcurrentException {
        // Arrange: An initializer that has not been started.

        // Act: Call start() for the first time.
        final boolean firstStartResult = initializer.start();

        // Assert: The first call should be successful and return true.
        assertTrue(firstStartResult, "The first call to start() should return true.");

        // Act: Call start() again.
        final boolean secondStartResult = initializer.start();

        // Assert: Subsequent calls should be ignored and return false.
        assertFalse(secondStartResult, "Any subsequent call to start() should return false.");

        // Assert: The initialize() method should have been called only once.
        assertInitializationOccurredOnce(initializer);
    }

    /**
     * Verifies that the background initialization process was triggered exactly once
     * and that the initializer's state is consistent.
     *
     * @param init The initializer to check.
     * @throws ConcurrentException if the background task threw a checked exception.
     */
    private void assertInitializationOccurredOnce(final AbstractBackgroundInitializerTestImpl init) throws ConcurrentException {
        // Calling get() blocks until initialization is complete.
        final CloseableCounter result = init.get();

        // Verify that the initialize() method was called exactly once.
        // We check this on both the returned result and the internal counter for consistency.
        assertEquals(1, result.getInitializeCalls(),
            "The result object should confirm that initialize() was called once.");
        assertEquals(1, init.getCloseableCounter().getInitializeCalls(),
            "The initializer's internal counter should also confirm initialize() was called once.");

        // After start(), a Future object must be available.
        assertNotNull(init.getFuture(), "The future should not be null after start() has been called.");
    }

    //<editor-fold desc="Test Helper Classes and Methods">
    /**
     * A concrete test implementation of BackgroundInitializer.
     * It provides hooks to control and inspect the initialization process.
     */
    protected static class AbstractBackgroundInitializerTestImpl extends BackgroundInitializer<CloseableCounter> {
        Exception ex;
        boolean shouldSleep;
        final CountDownLatch latch = new CountDownLatch(1);
        boolean waitForLatch;
        final CloseableCounter counter = new CloseableCounter();

        AbstractBackgroundInitializerTestImpl() {
        }

        AbstractBackgroundInitializerTestImpl(final ExecutorService exec) {
            super(exec);
        }

        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        /**
         * Simulates the background initialization. Records the invocation and can be
         * configured to wait or throw an exception for testing purposes.
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

    /**
     * A simple counter that tracks if its initialization and close methods were called.
     */
    protected static class CloseableCounter {
        final AtomicInteger initializeCalls = new AtomicInteger();
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

    /**
     * The specific implementation used in tests, which delegates to the internal
     * initialization method.
     */
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
    //</editor-fold>
}