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

/**
 * Tests for {@link BackgroundInitializer}.
 */
// Renamed from BackgroundInitializerTestTest11 for clarity and correctness.
class BackgroundInitializerTest extends AbstractLangTest {

    /**
     * A test-specific implementation of BackgroundInitializer that provides
     * hooks for controlling and inspecting the initialization process.
     */
    protected static class TestBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {

        /** An exception to be thrown by initialize(), null means no exception. */
        Exception exceptionToThrow;

        /** A flag to make the background task sleep, for testing timeouts. */
        boolean shouldSleep;

        /** A latch to pause the initialize() method until released by the test. */
        final CountDownLatch completionLatch = new CountDownLatch(1);
        boolean waitForLatch;

        /** The object that will be "initialized" and returned as the result. */
        final CloseableCounter initializationResult = new CloseableCounter();

        TestBackgroundInitializer() {
            super();
        }

        TestBackgroundInitializer(final ExecutorService exec) {
            super(exec);
        }

        /**
         * The actual initialization logic.
         * It increments a counter to track invocations and can be configured
         * to throw an exception or wait for a latch.
         */
        @Override
        protected CloseableCounter initialize() throws Exception {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            if (shouldSleep) {
                ThreadUtils.sleep(Duration.ofMinutes(1));
            }
            if (waitForLatch) {
                completionLatch.await();
            }
            return initializationResult.increment();
        }
        
        public CloseableCounter getInitializationResult() {
            return initializationResult;
        }

        /** Makes the initialize() method wait until releaseLatch() is called. */
        public void enableLatch() {
            waitForLatch = true;
        }

        /** Allows the initialize() method to complete if it's waiting on the latch. */
        public void releaseLatch() {
            completionLatch.countDown();
        }
    }

    /**
     * A simple counter class used as the result of the background initialization.
     * It tracks the number of times it has been "initialized" (incremented)
     * and whether it has been "closed".
     */
    protected static class CloseableCounter {
        private final AtomicInteger initializeCalls = new AtomicInteger();
        private final AtomicBoolean closed = new AtomicBoolean();

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
     * Factory method to create a new test initializer.
     */
    private TestBackgroundInitializer createTestInitializer() {
        return new TestBackgroundInitializer();
    }

    /**
     * Tests that when no external executor is provided, the initializer
     * creates a temporary one and shuts it down after the task completes.
     */
    @Test
    void start_withoutExternalExecutor_createsAndShutsDownTemporaryExecutor() throws ConcurrentException {
        // Arrange
        final TestBackgroundInitializer initializer = createTestInitializer();

        // Act
        final boolean wasStarted = initializer.start();
        // Block until initialization is complete to get the result
        final CloseableCounter result = initializer.get();

        // Assert
        assertTrue(wasStarted, "start() should return true on its first invocation.");
        assertNotNull(result, "The result of the initialization should not be null.");
        assertEquals(1, result.getInitializeCalls(), "The initialize() method should be called exactly once.");
        
        // Verify the internal state of the initializer
        assertNotNull(initializer.getFuture(), "A Future should be available after starting.");
        assertTrue(initializer.getActiveExecutor().isShutdown(), "The temporary executor service should be shut down after completion.");
    }
}