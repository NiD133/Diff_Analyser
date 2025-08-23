package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertAll;
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
 * Tests for {@link MultiBackgroundInitializer}.
 * <p>
 * This test suite includes a comprehensive set of helpers and test doubles
 * to simulate various initialization scenarios, including successful execution,
 * exceptions, and concurrent behavior.
 * </p>
 */
@DisplayName("Tests for MultiBackgroundInitializer")
public class MultiBackgroundInitializerTest extends AbstractLangTest {

    /**
     * Constant for the base name of the child initializers.
     */
    private static final String CHILD_INIT_NAME_PREFIX = "childInitializer";

    /**
     * The initializer instance to be tested.
     */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    @Test
    @DisplayName("start() should succeed for an initializer with no children")
    void testStartWithNoChildrenSucceeds() throws ConcurrentException {
        // Act
        final boolean startResult = initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert
        assertTrue(startResult, "start() should return true to indicate successful initiation.");

        assertAll("Results for no-children initialization",
            () -> assertTrue(results.initializerNames().isEmpty(), "Result should contain no initializer names."),
            () -> assertTrue(results.isSuccessful(), "Overall result should be successful.")
        );

        // Verify that the temporary executor service was shut down
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Internal executor service should be shut down after completion.");
    }

    // =================================================================================
    // Test Infrastructure
    //
    // The following methods and classes are helpers for creating and verifying
    // complex test scenarios that may be used by other tests in a larger suite.
    // =================================================================================

    /**
     * A helper method that simulates a complete, successful initialization cycle
     * with a specified number of child initializers. It adds children, starts
     * the main initializer, and verifies that all children completed successfully.
     *
     * @param childCount the number of child initializers to add.
     * @return the results object for further optional assertions.
     * @throws ConcurrentException if the initialization fails.
     */
    private MultiBackgroundInitializer.MultiBackgroundInitializerResults performAndVerifySuccessfulInitialization(final int childCount) throws ConcurrentException {
        // Arrange: Add child initializers
        for (int i = 0; i < childCount; i++) {
            initializer.addInitializer(CHILD_INIT_NAME_PREFIX + i, createChildBackgroundInitializer());
        }

        // Act: Start and get results
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert: Check the overall results
        assertEquals(childCount, results.initializerNames().size(), "Should be one result for each child initializer.");
        assertTrue(results.isSuccessful(), "Overall initialization should be successful.");

        // Assert: Check each child's result individually
        for (int i = 0; i < childCount; i++) {
            final String childName = CHILD_INIT_NAME_PREFIX + i;
            final BackgroundInitializer<?> child = results.getInitializer(childName);

            assertAll("Verification for child: " + childName,
                () -> assertTrue(results.initializerNames().contains(childName), "Result should contain the child's name."),
                () -> assertEquals(CloseableCounter.withInitializeCalls(1), results.getResultObject(childName), "Child should return the correct result object."),
                () -> assertFalse(results.isException(childName), "Child should not have thrown an exception."),
                () -> assertNull(results.getException(childName), "Child's exception object should be null.")
            );

            assertChildInitializerWasExecuted(child, initializer.getActiveExecutor());
        }
        return results;
    }

    /**
     * Asserts that a child initializer has been executed exactly once.
     *
     * @param child            the child initializer to check.
     * @param expectedExecutor the executor service that should have been used, or null to skip check.
     * @throws ConcurrentException if an error occurs during {@code get()}.
     */
    private void assertChildInitializerWasExecuted(final BackgroundInitializer<?> child, final ExecutorService expectedExecutor) throws ConcurrentException {
        final AbstractChildBackgroundInitializer childInitializer = (AbstractChildBackgroundInitializer) child;

        // Check the result returned by get()
        final CloseableCounter result = childInitializer.get();
        assertEquals(1, result.getInitializeCalls(), "Child initializer should have been called once (checked via result object).");

        // Check the internal state of the initializer
        assertEquals(1, childInitializer.initializeCalls, "Child's initialize() method should have been invoked exactly once.");

        if (expectedExecutor != null) {
            assertEquals(expectedExecutor, childInitializer.currentExecutor, "Child should have used the correct executor service.");
        }
    }

    /**
     * Factory method for creating a test-specific child initializer.
     *
     * @return a new {@link MethodChildBackgroundInitializer}.
     */
    protected AbstractChildBackgroundInitializer createChildBackgroundInitializer() {
        return new MethodChildBackgroundInitializer();
    }

    /**
     * A test-specific implementation of {@link BackgroundInitializer}.
     * It provides hooks to control its execution, such as simulating failures
     * or delays, and records its state for verification.
     */
    protected static class AbstractChildBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        /** Stores the executor service used for initialization. */
        volatile ExecutorService currentExecutor;
        /** The result object, which also tracks state. */
        final CloseableCounter counter = new CloseableCounter();
        /** Counts the number of times initialize() is called. */
        volatile int initializeCalls;
        /** If not null, this exception is thrown by initialize(). */
        Exception exceptionToThrow;
        /** A latch to pause the initialize() method for concurrency tests. */
        final CountDownLatch latch = new CountDownLatch(1);
        /** A flag to enable waiting on the latch. */
        boolean latchEnabled;

        public void enableLatch() {
            latchEnabled = true;
        }

        public void releaseLatch() {
            latch.countDown();
        }

        /**
         * Simulates the initialization. Records the call, captures the executor,
         * and optionally waits on a latch or throws an exception.
         */
        protected CloseableCounter initializeInternal() throws Exception {
            initializeCalls++;
            currentExecutor = getActiveExecutor();
            if (latchEnabled) {
                latch.await();
            }
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            return counter.increment();
        }
    }

    /**
     * A concrete child initializer that uses the template method pattern.
     */
    protected static class MethodChildBackgroundInitializer extends AbstractChildBackgroundInitializer {
        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    /**
     * A helper class that serves as the result of a background initialization.
     * It counts initialization calls and can be "closed".
     */
    protected static class CloseableCounter {
        /** The number of invocations of initialize(). */
        volatile int initializeCalls;
        /** A flag to check if the close method has been called. */
        volatile boolean closed;

        /**
         * Factory method to create a {@code CloseableCounter} with a specific
         * number of initialization calls. Useful for creating expected objects in tests.
         * @param initialCalls the number of calls to set.
         * @return a new {@code CloseableCounter} instance.
         */
        public static CloseableCounter withInitializeCalls(final int initialCalls) {
            return new CloseableCounter().setInitializeCalls(initialCalls);
        }

        public void close() {
            closed = true;
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

        /**
         * Compares this counter to another object for equality.
         * <p>
         * Note: This implementation only compares the {@code initializeCalls} field.
         * This is intentional for simplifying test assertions.
         * </p>
         * @param other the object to compare to.
         * @return true if the other object is a {@code CloseableCounter} with the
         *         same number of {@code initializeCalls}.
         */
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
}