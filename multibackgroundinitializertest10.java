package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the MultiBackgroundInitializer.MultiBackgroundInitializerResults class,
 * focusing on the success and failure states.
 */
public class MultiBackgroundInitializerResultsTest extends AbstractLangTest {

    /**
     * Constant for the names of the child initializers.
     */
    private static final String CHILD_INIT = "childInitializer";

    /**
     * The initializer to be tested.
     */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * Tests that the results object is marked as successful when all child
     * initializers complete without throwing an exception.
     *
     * @throws org.apache.commons.lang3.concurrent.ConcurrentException if an error occurs during initialization
     */
    @Test
    void isSuccessfulShouldReturnTrueWhenChildInitializerCompletesSuccessfully() throws ConcurrentException {
        // Arrange: Create an initializer with a single child that is designed to succeed.
        final BackgroundInitializer<?> successfulChild = new SuccessfulChildInitializer();
        initializer.addInitializer(CHILD_INIT, successfulChild);

        // Act: Start the main initializer and wait for it to complete.
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert: Verify that the overall result is marked as successful.
        assertTrue(results.isSuccessful(), "Results should be marked as successful when no child initializer fails.");
    }

    // NOTE: The helper classes below are kept from the original test suite to support
    // other potential test cases. For the specific test above, a much simpler
    // implementation would suffice.

    /**
     * A mostly complete implementation of {@code BackgroundInitializer} used for
     * defining background tasks for {@code MultiBackgroundInitializer}.
     *
     * Subclasses will contain the initializer, either as an method implementation
     * or by using a supplier.
     */
    protected static class AbstractChildBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        volatile ExecutorService currentExecutor;
        final CloseableCounter counter = new CloseableCounter();
        volatile int initializeCalls;
        Exception ex;
        final CountDownLatch latch = new CountDownLatch(1);
        boolean waitForLatch;

        public void enableLatch() {
            waitForLatch = true;
        }

        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        protected CloseableCounter initializeInternal() throws Exception {
            initializeCalls++;
            currentExecutor = getActiveExecutor();
            if (waitForLatch) {
                latch.await();
            }
            if (ex != null) {
                throw ex;
            }
            return counter.increment();
        }

        public void releaseLatch() {
            latch.countDown();
        }
    }

    /**
     * A concrete child initializer that succeeds by calling the base implementation.
     */
    private static class SuccessfulChildInitializer extends AbstractChildBackgroundInitializer {
        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    /**
     * A helper class to count initializations and simulate a result object.
     */
    protected static class CloseableCounter {
        volatile int initializeCalls;
        volatile boolean closed;

        public void close() {
            closed = true;
        }

        @Override
        public boolean equals(final Object other) {
            if (other instanceof CloseableCounter) {
                return initializeCalls == ((CloseableCounter) other).getInitializeCalls();
            }
            return false;
        }

        public int getInitializeCalls() {
            return initializeCalls;
        }

        @Override
        public int hashCode() {
            return initializeCalls;
        }

        public CloseableCounter increment() {
            initializeCalls++;
            return this;
        }
    }
}