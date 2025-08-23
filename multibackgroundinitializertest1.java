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
 * An abstract base class for tests of {@link MultiBackgroundInitializer}.
 * <p>
 * This class provides common setup, helper methods, and test doubles needed to
 * test the functionality of {@code MultiBackgroundInitializer}. Concrete test
* suites can extend this class.
 * </p>
 */
public abstract class MultiBackgroundInitializerTestTest1 extends AbstractLangTest {

    /**
     * A name prefix for child initializers.
     */
    private static final String CHILD_INIT_NAME_PREFIX = "childInitializer";

    /**
     * The initializer under test.
     */
    protected MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * Tries to add another child initializer after the start() method has been
     * called. This should not be allowed.
     */
    @Test
    @DisplayName("addInitializer() should throw IllegalStateException if called after start()")
    void addInitializer_shouldThrowExceptionIfCalledAfterStart() throws ConcurrentException {
        // Arrange: Start the initializer
        initializer.start();

        // Act & Assert: Attempting to add another initializer should fail
        assertThrows(IllegalStateException.class, () -> {
            initializer.addInitializer(CHILD_INIT_NAME_PREFIX, createChildBackgroundInitializer());
        }, "Should not be able to add an initializer after start() has been called.");

        // Cleanup: Wait for the initializer to complete to ensure clean state
        initializer.get();
    }

    // --------------------------------------------------------------------------------
    // Helper methods and classes for subclasses
    // --------------------------------------------------------------------------------

    /**
     * A comprehensive check of the initializer's execution.
     * <p>
     * This helper adds several child initializers, runs them, and performs
     * detailed assertions on the results. It can be used by subclasses to
     * test various scenarios.
     * </p>
     *
     * @return The results object from the initializer.
     * @throws ConcurrentException if the background initialization fails.
     */
    private MultiBackgroundInitializer.MultiBackgroundInitializerResults checkInitialize() throws ConcurrentException {
        final int childCount = 5;
        for (int i = 0; i < childCount; i++) {
            initializer.addInitializer(CHILD_INIT_NAME_PREFIX + i, createChildBackgroundInitializer());
        }

        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        assertEquals(childCount, results.initializerNames().size(), "Wrong number of child initializers");
        for (int i = 0; i < childCount; i++) {
            final String key = CHILD_INIT_NAME_PREFIX + i;
            assertTrue(results.initializerNames().contains(key), "Initializer name not found: " + key);

            // Verify the result object
            final Object resultObject = results.getResultObject(key);
            assertTrue(resultObject instanceof CloseableCounter, "Result object should be a CloseableCounter");
            assertEquals(1, ((CloseableCounter) resultObject).getInitializeCalls(), "Wrong number of initializations in result object");

            // Verify exception status
            assertFalse(results.isException(key), "Exception flag should be false");
            assertNull(results.getException(key), "Should be no exception");

            // Verify the child initializer itself
            checkChild(results.getInitializer(key), initializer.getActiveExecutor());
        }
        return results;
    }

    /**
     * Checks if a child initializer was executed correctly.
     *
     * @param child           The child initializer to check.
     * @param expectedExecutor The expected executor service (can be null).
     * @throws ConcurrentException if fetching the result fails.
     */
    private void checkChild(final BackgroundInitializer<?> child, final ExecutorService expectedExecutor) throws ConcurrentException {
        final TestChildInitializer childInitializer = (TestChildInitializer) child;
        assertEquals(1, childInitializer.get().getInitializeCalls(), "Child initializer should have been called once.");
        assertEquals(1, childInitializer.initializeCalls, "Internal call count should be 1.");
        if (expectedExecutor != null) {
            assertEquals(expectedExecutor, childInitializer.currentExecutor, "Child was not executed by the correct executor service.");
        }
    }

    /**
     * Factory method for creating child initializers for tests.
     * Can be overridden by subclasses to provide different implementations.
     */
    protected TestChildInitializer createChildBackgroundInitializer() {
        return new TestChildInitializer();
    }

    /**
     * A test-specific {@code BackgroundInitializer} that allows fine-grained
     * control over its execution for testing purposes.
     */
    protected static class TestChildInitializer extends BackgroundInitializer<CloseableCounter> {
        /** The executor service used to run this initializer. */
        volatile ExecutorService currentExecutor;
        /** A counter for the number of times initialize() is called. */
        volatile int initializeCalls;
        /** An exception to be thrown by initialize() for testing error scenarios. */
        Exception exceptionToThrow;
        /** A latch to control the completion of the initialize() method. */
        final CountDownLatch completionLatch = new CountDownLatch(1);
        /** If true, the initialize() method will wait for the latch. */
        boolean shouldWaitForLatch;

        /** The result object, which also tracks state. */
        final CloseableCounter counter = new CloseableCounter();

        @Override
        protected CloseableCounter initialize() throws Exception {
            initializeCalls++;
            currentExecutor = getActiveExecutor();

            if (shouldWaitForLatch) {
                completionLatch.await();
            }

            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }

            return counter.increment();
        }

        public void enableLatch() {
            shouldWaitForLatch = true;
        }

        public void releaseLatch() {
            completionLatch.countDown();
        }
    }

    /**
     * A simple counter class used as the result of a {@link TestChildInitializer}.
     * It tracks the number of times it has been "initialized" and whether it has
     * been "closed".
     */
    protected static class CloseableCounter {
        /** The number of invocations of initialize(). */
        private volatile int initializeCalls;
        /** A flag indicating if the close() method was called. */
        private volatile boolean closed;

        public int getInitializeCalls() {
            return initializeCalls;
        }

        public CloseableCounter increment() {
            initializeCalls++;
            return this;
        }

        public void close() {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }
}