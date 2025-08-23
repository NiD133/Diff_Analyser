package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiBackgroundInitializer}.
 *
 * <p>This class contains basic tests and a set of test fixtures (nested classes)
 * and helper methods used to construct more complex test scenarios for the
 * MultiBackgroundInitializer.
 * </p>
 */
@DisplayName("MultiBackgroundInitializer Test")
public class MultiBackgroundInitializerTest extends AbstractLangTest {

    /**
     * Constant for the names of the child initializers.
     */
    private static final String CHILD_INIT_NAME_PREFIX = "childInitializer";

    /**
     * The initializer to be tested.
     */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * Tests that addInitializer() throws a NullPointerException if the initializer
     * instance is null.
     */
    @Test
    @DisplayName("addInitializer() should throw NullPointerException for a null initializer")
    void addInitializerShouldThrowExceptionWhenInitializerIsNull() {
        // Expect a NullPointerException when a null initializer is added.
        assertThrows(NullPointerException.class, () -> {
            initializer.addInitializer("testInitializer", null);
        });
    }

    // -------------------------------------------------------------------------
    // Helper methods and test fixtures
    // -------------------------------------------------------------------------

    /**
     * A helper method to verify that a child initializer has been executed correctly.
     * It checks the execution count and optionally the executor service used.
     *
     * @param child            The child initializer to check.
     * @param expectedExecutor The expected executor service (or null if not checked).
     * @throws ConcurrentException if an error occurs during initialization.
     */
    private void checkChildInitializer(final BackgroundInitializer<?> child, final ExecutorService expectedExecutor)
            throws ConcurrentException {
        final TestChildBackgroundInitializer childInitializer = (TestChildBackgroundInitializer) child;

        // Check that the initializer's result indicates one execution.
        final CloseableCounter result = childInitializer.get();
        assertEquals(1, result.getInitializeCalls(), "Child initializer result should report 1 execution.");

        // Check that the initializer's internal counter also records one execution.
        assertEquals(1, childInitializer.initializeCalls, "Child initializer's internal call count should be 1.");

        if (expectedExecutor != null) {
            assertEquals(expectedExecutor, childInitializer.currentExecutor,
                    "Child initializer should have used the correct executor service.");
        }
    }

    /**
     * A helper method for testing a successful initialization sequence.
     * It adds several child initializers, starts the main initializer, and verifies the results.
     * Note: This helper is not used by tests in this file but is provided for
     * use in other, more complex test classes.
     *
     * @return The results object produced by the initializer.
     * @throws ConcurrentException if an error occurs during initialization.
     */
    private MultiBackgroundInitializer.MultiBackgroundInitializerResults checkSuccessfulInitialization() throws ConcurrentException {
        final int childCount = 5;
        for (int i = 0; i < childCount; i++) {
            initializer.addInitializer(CHILD_INIT_NAME_PREFIX + i, createChildBackgroundInitializer());
        }

        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        assertEquals(childCount, results.initializerNames().size(), "Should be one result per child initializer.");

        for (int i = 0; i < childCount; i++) {
            final String name = CHILD_INIT_NAME_PREFIX + i;
            assertTrue(results.initializerNames().contains(name), "Results should contain name: " + name);

            // Check that the result object is correct
            final Object resultObject = results.getResultObject(name);
            assertEquals(CloseableCounter.withInitializations(1), resultObject,
                    "Result object for " + name + " is incorrect.");

            // Check that no exception was thrown
            assertFalse(results.isException(name), "Initializer " + name + " should not have thrown an exception.");
            assertNull(results.getException(name), "Exception for " + name + " should be null.");

            // Further checks on the child initializer state
            checkChildInitializer(results.getInitializer(name), initializer.getActiveExecutor());
        }
        return results;
    }

    /**
     * Factory method for creating child initializers for tests.
     * Can be overridden by subclasses to test with different implementations.
     */
    protected TestChildBackgroundInitializer createChildBackgroundInitializer() {
        return new MethodBasedChildBackgroundInitializer();
    }

    /**
     * A test-specific implementation of {@link BackgroundInitializer} that serves as a
     * controllable task for {@link MultiBackgroundInitializer}. It allows tests to
     * track invocations, control execution flow, and simulate exceptions.
     */
    protected static abstract class TestChildBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
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

        /**
         * Simulates the initialization logic. Records the call, captures the executor,
         * and can be controlled to wait or throw an exception.
         */
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
     * A concrete {@link TestChildBackgroundInitializer} that implements the
     * {@code initialize()} method directly.
     */
    protected static class MethodBasedChildBackgroundInitializer extends TestChildBackgroundInitializer {
        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    /**
     * A helper class representing the result of a child initializer. It counts
     * initializations and tracks a "closed" state for testing resource cleanup.
     */
    protected static class CloseableCounter {
        private volatile int initializeCalls;
        private volatile boolean closed;

        /**
         * Factory method to create a {@code CloseableCounter} with a specific
         * number of initializations, useful for equality checks in tests.
         *
         * @param initializations The number of initializations.
         * @return A new {@code CloseableCounter} with the given count.
         */
        public static CloseableCounter withInitializations(final int initializations) {
            return new CloseableCounter().setInitializeCalls(initializations);
        }

        public void close() {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }

        public int getInitializeCalls() {
            return initializeCalls;
        }

        public CloseableCounter increment() {
            initializeCalls++;
            return this;
        }

        public CloseableCounter setInitializeCalls(final int i) {
            this.initializeCalls = i;
            return this;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CloseableCounter)) {
                return false;
            }
            final CloseableCounter that = (CloseableCounter) other;
            return initializeCalls == that.initializeCalls;
        }

        @Override
        public int hashCode() {
            return initializeCalls;
        }
    }
}