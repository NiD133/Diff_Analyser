package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.ThreadUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BackgroundInitializer}.
 *
 * <p>This class verifies the core functionality of the BackgroundInitializer,
 * ensuring that the initialization process is started correctly and that the
 * result can be retrieved as expected.</p>
 */
public class BackgroundInitializerTest {

    /**
     * A controllable implementation of BackgroundInitializer for testing purposes.
     *
     * <p>This class allows tests to simulate various initialization scenarios,
     * such as delays or exceptions, by providing hooks like latches and flags.</p>
     */
    protected static class ControllableBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {

        /** An exception to be thrown by initialize() for testing error handling. */
        Exception ex;

        /** A flag to make the background task sleep, simulating a long-running task. */
        boolean shouldSleep;

        /** A latch that tests can use to control when the initialize() method completes. */
        final CountDownLatch completionLatch = new CountDownLatch(1);

        /** A flag to determine if the background task should wait for the latch. */
        boolean waitForLatch;

        /** The object that holds the state we are testing, created during initialization. */
        private final CloseableCounter counter = new CloseableCounter();

        ControllableBackgroundInitializer() {
        }

        ControllableBackgroundInitializer(final ExecutorService exec) {
            super(exec);
        }

        /**
         * The actual initialization logic.
         *
         * <p>This method increments the counter and can be configured to throw an
         * exception or wait for a latch to simulate different conditions.</p>
         *
         * @return the counter after incrementing its call count.
         * @throws Exception if a test exception is configured.
         */
        protected CloseableCounter initializeInternal() throws Exception {
            if (ex != null) {
                throw ex;
            }
            if (shouldSleep) {
                ThreadUtils.sleep(Duration.ofMinutes(1));
            }
            if (waitForLatch) {
                completionLatch.await();
            }
            return counter.increment();
        }

        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        public void enableLatch() {
            waitForLatch = true;
        }

        public void releaseLatch() {
            completionLatch.countDown();
        }
    }

    /**
     * A simple counter that can be "closed", used as the result of the background initialization.
     * It tracks the number of times it has been initialized and its closed state.
     */
    protected static class CloseableCounter {

        private final AtomicInteger initializeCalls = new AtomicInteger();
        private final AtomicBoolean closed = new AtomicBoolean();

        public CloseableCounter increment() {
            initializeCalls.incrementAndGet();
            return this;
        }

        public int getInitializeCalls() {
            return initializeCalls.get();
        }

        public void close() {
            closed.set(true);
        }

        public boolean isClosed() {
            return closed.get();
        }
    }

    /**
     * The concrete test implementation of BackgroundInitializer that overrides the
     * initialize() method to call our controllable logic.
     */
    protected static class TestBackgroundInitializer extends ControllableBackgroundInitializer {

        TestBackgroundInitializer() {
        }

        TestBackgroundInitializer(final ExecutorService exec) {
            super(exec);
        }

        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    /**
     * Factory method to create a test initializer.
     * @return a new {@link TestBackgroundInitializer}.
     */
    protected TestBackgroundInitializer createTestInitializer() {
        return new TestBackgroundInitializer();
    }

    /**
     * Factory method to create a test initializer with a specific executor.
     * @param exec The executor service to use.
     * @return a new {@link TestBackgroundInitializer}.
     */
    protected TestBackgroundInitializer createTestInitializer(final ExecutorService exec) {
        return new TestBackgroundInitializer(exec);
    }

    /**
     * Tests that calling start() on a BackgroundInitializer executes the
     * initialize() method exactly once.
     */
    @Test
    void whenStartIsCalled_initializeIsExecutedOnce() throws ConcurrentException {
        // Arrange: Create a new initializer.
        final ControllableBackgroundInitializer initializer = createTestInitializer();

        // Act: Start the background initialization and call get() to wait for it to complete.
        initializer.start();
        final CloseableCounter result = initializer.get();

        // Assert: Verify that the initialization was successful and happened only once.
        assertNotNull(initializer.getFuture(), "A Future object should be created after start().");
        assertNotNull(result, "The result of initialization should not be null.");

        assertEquals(1, result.getInitializeCalls(),
            "The initialize() method should have been called exactly once.");
        assertEquals(1, initializer.getCloseableCounter().getInitializeCalls(),
            "The internal counter should also reflect one initialization call.");
        assertSame(initializer.getCloseableCounter(), result,
            "The object returned by get() should be the same instance created during initialization.");
    }
}