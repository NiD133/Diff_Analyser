package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.AbstractLangTest;
import org.apache.commons.lang3.ThreadUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BackgroundInitializer} focusing on its executor management.
 */
public class BackgroundInitializerTest extends AbstractLangTest {

    /**
     * Verifies that the background initialization completed successfully. This method
     * waits for the initializer to finish and then checks its state.
     *
     * @param initializer the initializer to test, which must have been started.
     * @throws ConcurrentException if the background task threw a checked exception.
     */
    private void verifySuccessfulInitialization(final AbstractBackgroundInitializerTestImpl initializer) throws ConcurrentException {
        // The get() method blocks until initialization is complete and returns the result.
        final CloseableCounter result = initializer.get();

        // Assert that the initialization logic was executed exactly once.
        assertEquals(1, result.getInitializeCalls(), "The initialize() method should have been called once.");
        // For completeness, verify get() returns the same instance held by the initializer.
        assertSame(result, initializer.getCloseableCounter(), "get() should return the internally-held instance.");

        // Assert that the initializer's internal state is consistent.
        assertNotNull(initializer.getFuture(), "A Future object should be available after start().");
    }

    protected AbstractBackgroundInitializerTestImpl createInitializer() {
        return new MethodBackgroundInitializerTestImpl();
    }

    protected AbstractBackgroundInitializerTestImpl createInitializer(final ExecutorService exec) {
        return new MethodBackgroundInitializerTestImpl(exec);
    }

    /**
     * Tests that getActiveExecutor() returns the internally-created executor
     * when no external executor is provided.
     */
    @Test
    void getActiveExecutor_whenUsingInternalExecutor_returnsNonNullExecutor() throws ConcurrentException {
        // Arrange: Create an initializer without providing an external executor service.
        // This forces it to create and manage a temporary, internal one.
        final AbstractBackgroundInitializerTestImpl initializer = createInitializer();

        // Act: Start the background initialization process.
        initializer.start();

        // Assert: The active executor should be the temporary one created internally.
        assertNotNull(initializer.getActiveExecutor(), "Active executor should not be null after start()");

        // And: Verify that the initialization process completes successfully.
        verifySuccessfulInitialization(initializer);
    }

    //
    // Test Helper Classes
    // NOTE: The helper classes below are complex but are designed to provide fine-grained
    // control over the asynchronous initialization process for testing purposes.
    //

    /**
     * A concrete implementation of BackgroundInitializer for testing purposes.
     * It overloads methods to allow inspection and control of its state.
     */
    protected static class AbstractBackgroundInitializerTestImpl extends BackgroundInitializer<CloseableCounter> {

        /** An exception to be thrown by initialize(), for testing error scenarios. */
        Exception ex;

        /** A flag to make the background task sleep, for testing timing scenarios. */
        boolean shouldSleep;

        /** A latch that tests can use to control when initialize() completes. */
        final CountDownLatch latch = new CountDownLatch(1);
        boolean waitForLatch;

        /** The object that will be "initialized" in the background. */
        final CloseableCounter counter = new CloseableCounter();

        AbstractBackgroundInitializerTestImpl() {
        }

        AbstractBackgroundInitializerTestImpl(final ExecutorService exec) {
            super(exec);
        }

        /**
         * The core initialization logic. Records the invocation and can be configured
         * to throw an exception, sleep, or wait on a latch.
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

        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        public void enableLatch() {
            waitForLatch = true;
        }

        public void releaseLatch() {
            latch.countDown();
        }
    }

    /**
     * A simple counter class that acts as the result of the background initialization.
     * It tracks invocations and whether it has been "closed".
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

    /** A concrete subclass that implements the abstract initialize() method. */
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
}