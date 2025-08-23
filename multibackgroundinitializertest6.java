package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiBackgroundInitializer} specifically focusing on its behavior
 * when using an external, user-provided executor service.
 *
 * Note: The original class name 'MultiBackgroundInitializerTestTest6' was preserved.
 * A more descriptive name like 'MultiBackgroundInitializerExternalExecutorTest' would be clearer.
 */
public class MultiBackgroundInitializerTestTest6 extends AbstractLangTest {

    /** The prefix for the names of the child initializers. */
    private static final String CHILD_INIT_NAME_PREFIX = "childInitializer";

    /** The number of child initializers to create for the tests. */
    private static final int CHILD_TASK_COUNT = 5;

    /** The initializer instance under test. */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * Tests that when an external executor is provided, it is used to run the
     * background tasks and is not shut down by the initializer upon completion.
     */
    @Test
    void initialize_withExternalExecutor_shouldCompleteTasksAndNotShutdownExecutor() throws ConcurrentException, InterruptedException {
        final ExecutorService externalExecutor = Executors.newCachedThreadPool();
        try {
            // Given: an initializer configured with an external executor
            initializer = new MultiBackgroundInitializer(externalExecutor);

            // When: the initializer is started and completes
            performAndVerifyInitialization();

            // Then: the external executor should have been used and left running
            assertEquals(externalExecutor, initializer.getActiveExecutor(), "The external executor should be the active one.");
            assertFalse(externalExecutor.isShutdown(), "The external executor should not be shut down by the initializer.");
        } finally {
            // Clean up the executor service
            externalExecutor.shutdown();
            externalExecutor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    /**
     * Adds a set number of child initializers, starts the main initializer,
     * and verifies that all children completed successfully.
     *
     * @return The results object from the completed initialization.
     * @throws ConcurrentException if initialization fails.
     */
    private MultiBackgroundInitializer.MultiBackgroundInitializerResults performAndVerifyInitialization() throws ConcurrentException {
        for (int i = 0; i < CHILD_TASK_COUNT; i++) {
            initializer.addInitializer(CHILD_INIT_NAME_PREFIX + i, createChildBackgroundInitializer());
        }

        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        assertEquals(CHILD_TASK_COUNT, results.initializerNames().size(), "Should be one result for each child initializer.");

        for (int i = 0; i < CHILD_TASK_COUNT; i++) {
            final String childName = CHILD_INIT_NAME_PREFIX + i;
            assertAll("Verify results for child: " + childName,
                () -> assertTrue(results.initializerNames().contains(childName), "Result should contain the initializer's name."),
                () -> assertEquals(CloseableCounter.forExpectedCount(1), results.getResultObject(childName), "Result object should indicate one successful execution."),
                () -> assertFalse(results.isException(childName), "Should be no exception for this initializer."),
                () -> assertNull(results.getException(childName), "Exception object should be null."),
                () -> assertChildInitializerCompleted(results.getInitializer(childName), initializer.getActiveExecutor())
            );
        }
        return results;
    }

    /**
     * Asserts that a specific child initializer has been executed exactly once
     * using the expected executor service.
     *
     * @param childInitializer The child initializer to check.
     * @param expectedExecutor The executor service that should have been used.
     * @throws ConcurrentException if getting the child's result fails.
     */
    private void assertChildInitializerCompleted(final BackgroundInitializer<?> childInitializer, final ExecutorService expectedExecutor) throws ConcurrentException {
        final AbstractChildBackgroundInitializer testChild = (AbstractChildBackgroundInitializer) childInitializer;
        final CloseableCounter result = testChild.get();

        assertAll("Verify state of child initializer",
            () -> assertEquals(1, result.getInitializeCalls(), "The result object's counter should be 1."),
            () -> assertEquals(1, testChild.initializeCalls, "The initialize() method should have been called exactly once."),
            () -> assertEquals(expectedExecutor, testChild.currentExecutor, "The child should have used the parent's active executor.")
        );
    }

    /**
     * Creates a new instance of a test-specific child initializer.
     */
    protected AbstractChildBackgroundInitializer createChildBackgroundInitializer() {
        return new MethodChildBackgroundInitializer();
    }

    // --- Test Fixtures: Inner classes for setting up test scenarios ---

    /**
     * A concrete implementation of {@link AbstractChildBackgroundInitializer} where
     * the initialize logic is in the overridden {@code initialize} method.
     */
    protected static class MethodChildBackgroundInitializer extends AbstractChildBackgroundInitializer {
        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    /**
     * A test-specific {@link BackgroundInitializer} that provides hooks for
     * verifying its execution state.
     */
    protected static abstract class AbstractChildBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        volatile ExecutorService currentExecutor;
        volatile int initializeCalls;
        final CloseableCounter counter = new CloseableCounter();
        Exception ex; // Can be set to simulate an error
        final CountDownLatch latch = new CountDownLatch(1); // For controlling execution timing
        boolean waitForLatch;

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
        // Methods to control the fixture's behavior in tests
        public void enableLatch() { waitForLatch = true; }
        public void releaseLatch() { latch.countDown(); }
    }

    /**
     * A simple counter class used as the result of a child initializer.
     * It tracks the number of times it has been incremented and whether it has been "closed".
     */
    protected static class CloseableCounter {
        private volatile int initializeCalls;
        private volatile boolean closed;

        /** Creates a CloseableCounter with a specific count, for use in equality tests. */
        public static CloseableCounter forExpectedCount(final int count) {
            return new CloseableCounter().setInitializeCalls(count);
        }

        public CloseableCounter increment() {
            initializeCalls++;
            return this;
        }

        public int getInitializeCalls() { return initializeCalls; }
        public CloseableCounter setInitializeCalls(final int i) {
            this.initializeCalls = i;
            return this;
        }
        public void close() { closed = true; }
        public boolean isClosed() { return closed; }

        @Override
        public boolean equals(final Object other) {
            if (other instanceof CloseableCounter) {
                return initializeCalls == ((CloseableCounter) other).getInitializeCalls();
            }
            return false;
        }
        @Override
        public int hashCode() { return initializeCalls; }
    }
}