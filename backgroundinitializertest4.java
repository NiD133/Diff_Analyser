package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.AbstractLangTest;
import org.apache.commons.lang3.ThreadUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BackgroundInitializer} focusing on its interaction with an
 * external {@link ExecutorService}.
 */
public class BackgroundInitializerExternalExecutorTest extends AbstractLangTest {

    /**
     * A concrete implementation of BackgroundInitializer for testing purposes.
     * It provides hooks to control and inspect the initialization process.
     */
    private static class TestBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {

        /** An exception to be thrown by initialize(), null by default. */
        Exception ex;

        /** A flag to make the background task sleep, forcing a delay. */
        boolean shouldSleep;

        /** A latch to control when the initialize() method completes. */
        final CountDownLatch latch = new CountDownLatch(1);

        /** A flag to determine if initialize() should wait for the latch. */
        boolean waitForLatch;

        /** The stateful object that is "initialized" and returned by this initializer. */
        final CloseableCounter counter = new CloseableCounter();

        TestBackgroundInitializer() {
        }

        TestBackgroundInitializer(final ExecutorService exec) {
            super(exec);
        }

        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        /**
         * Simulates a background initialization.
         * <p>
         * This implementation can be controlled by test-specific flags to simulate
         * delays, errors, or race conditions. It increments a counter to track
         * invocations.
         * </p>
         *
         * @return the initialized CloseableCounter object.
         * @throws Exception if the 'ex' field is set.
         */
        @Override
        protected CloseableCounter initialize() throws Exception {
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
     * A simple counter class that tracks initialization and closure.
     */
    private static class CloseableCounter {

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
     * Tests that when a BackgroundInitializer is configured with an external
     * ExecutorService, it uses that service to run the background task.
     */
    @Test
    void start_whenUsingExternalExecutor_usesProvidedExecutor() throws InterruptedException, ConcurrentException {
        // Arrange
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final TestBackgroundInitializer initializer = new TestBackgroundInitializer(executorService);

        try {
            // Act
            initializer.start();

            // Assert
            // The initializer should report the provided executor as the active one.
            assertSame(executorService, initializer.getActiveExecutor(),
                "The active executor should be the one provided externally.");

            // After starting, the future for the background task should be available.
            assertNotNull(initializer.getFuture(), "Future should be created after start.");

            // Block until initialization completes and verify the result.
            // The get() method also ensures that the background task ran without exceptions.
            final CloseableCounter result = initializer.get();
            assertEquals(1, result.getInitializeCalls(), "initialize() should have been called exactly once.");

        } finally {
            // Cleanup
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        }
    }
}