package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiBackgroundInitializer} focusing on scenarios with an
 * internally managed (temporary) executor service.
 */
public class MultiBackgroundInitializerTest extends AbstractLangTest {

    /**
     * Constant for the names of the child initializers.
     */
    private static final String CHILD_INIT_NAME_PREFIX = "childInitializer";

    /**
     * The number of child initializers to create for the test.
     */
    private static final int CHILD_INITIALIZER_COUNT = 5;

    /**
     * The initializer to be tested.
     */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * Tests that when MultiBackgroundInitializer uses its own temporary executor,
     * it successfully executes all child initializers and shuts down the executor upon completion.
     */
    @Test
    @DisplayName("start() should execute all children and shut down its internal executor when done")
    void initialize_withInternalExecutor_executesAllChildrenAndShutsDown() throws ConcurrentException {
        // Arrange: Add multiple child initializers to the main initializer.
        for (int i = 0; i < CHILD_INITIALIZER_COUNT; i++) {
            initializer.addInitializer(CHILD_INIT_NAME_PREFIX + i, createChildBackgroundInitializer());
        }

        // Act: Start the background processing and wait for it to complete.
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert: Verify that all child initializers were executed correctly.
        assertResultsAreCorrect(results, CHILD_INITIALIZER_COUNT);

        // Assert: Verify that the temporary executor service was shut down.
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Internal executor service should be shut down after completion.");
    }

    /**
     * Asserts that the results from the MultiBackgroundInitializer are correct.
     *
     * @param results       The results object from the completed initializer.
     * @param expectedCount The expected number of child initializers.
     * @throws ConcurrentException if the test fails.
     */
    private void assertResultsAreCorrect(final MultiBackgroundInitializer.MultiBackgroundInitializerResults results, final int expectedCount) throws ConcurrentException {
        assertEquals(expectedCount, results.initializerNames().size(), "Should be one result for each child initializer.");

        for (int i = 0; i < expectedCount; i++) {
            final String name = CHILD_INIT_NAME_PREFIX + i;
            assertTrue(results.initializerNames().contains(name), "Result should contain name: " + name);

            // Check that the result object is correct
            assertEquals(CloseableCounter.withInitializeCalls(1), results.getResultObject(name), "Result object for " + name + " is incorrect.");

            // Check that no exceptions were thrown
            assertFalse(results.isException(name), "Initializer " + name + " should not have thrown an exception.");
            assertNull(results.getException(name), "Exception for " + name + " should be null.");

            // Check the state of the child initializer itself
            final BackgroundInitializer<?> child = results.getInitializer(name);
            assertChildInitializerExecutedOnce(child, initializer.getActiveExecutor());
        }
    }

    /**
     * Checks that a child initializer has been executed exactly once.
     *
     * @param child            The child initializer to check.
     * @param expectedExecutor The executor service that should have been used.
     * @throws ConcurrentException if an error occurs during get().
     */
    private void assertChildInitializerExecutedOnce(final BackgroundInitializer<?> child, final ExecutorService expectedExecutor) throws ConcurrentException {
        final AbstractChildBackgroundInitializer childInitializer = (AbstractChildBackgroundInitializer) child;

        // Check the result from the child's perspective
        final Integer childResult = childInitializer.get().getInitializeCalls();
        assertEquals(1, childResult.intValue(), "The result from the child initializer's get() should indicate one execution.");

        // Check internal state of our test double
        assertEquals(1, childInitializer.initializeCalls, "The child's initialize() method should have been called exactly once.");
        assertEquals(expectedExecutor, childInitializer.currentExecutor, "The child initializer should have used the parent's executor service.");
    }

    /**
     * Creates a new instance of a test-specific child initializer.
     * This can be overridden by subclasses for different test scenarios.
     */
    protected AbstractChildBackgroundInitializer createChildBackgroundInitializer() {
        return new MethodChildBackgroundInitializer();
    }

    //<editor-fold desc="Test Fixture: Helper classes for testing">

    /**
     * A test-specific implementation of {@code BackgroundInitializer} used to
     * define background tasks for {@code MultiBackgroundInitializer}. This class
     * provides hooks to control and inspect its execution.
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

        /**
         * Records this invocation and returns the counter. Can be configured to
         * wait for a latch or throw an exception for testing purposes.
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
     * A concrete child initializer that uses a method override for its task.
     */
    protected static class MethodChildBackgroundInitializer extends AbstractChildBackgroundInitializer {
        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    /**
     * A simple counter class that acts as the result of a background task.
     * It also tracks whether it has been "closed".
     */
    protected static class CloseableCounter {
        volatile int initializeCalls;
        volatile boolean closed;

        /**
         * A factory method for creating a CloseableCounter instance with a specific
         * number of initialization calls. Useful for equality checks in tests.
         */
        public static CloseableCounter withInitializeCalls(final int i) {
            return new CloseableCounter().setInitializeCalls(i);
        }

        public CloseableCounter increment() {
            initializeCalls++;
            return this;
        }

        public int getInitializeCalls() {
            return initializeCalls;
        }

        public CloseableCounter setInitializeCalls(final int i) {
            this.initializeCalls = i;
            return this;
        }

        @Override
        public boolean equals(final Object other) {
            if (other instanceof CloseableCounter) {
                return initializeCalls == ((CloseableCounter) other).getInitializeCalls();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return initializeCalls;
        }
    }
    //</editor-fold>
}