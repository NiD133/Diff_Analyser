package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the behavior of nested {@link MultiBackgroundInitializer} instances.
 *
 * This class verifies that a {@link MultiBackgroundInitializer} can correctly manage another
 * {@link MultiBackgroundInitializer} as one of its child tasks, ensuring all initializers in the
 * hierarchy are executed properly.
 */
public class MultiBackgroundInitializerNestedTest extends AbstractLangTest {

    /** The name for the direct child initializer. */
    private static final String CHILD_INIT_NAME = "childInitializer";

    /** The name for the nested MultiBackgroundInitializer. */
    private static final String NESTED_INITIALIZER_NAME = "nestedMultiInitializer";

    /** The number of child initializers within the nested initializer. */
    private static final int NESTED_CHILD_COUNT = 3;

    /** The main initializer instance under test. */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    @Test
    void whenNestedInitializerIsAdded_thenAllInitializersExecuteCorrectly() throws ConcurrentException {
        // Arrange
        // 1. Create a nested MultiBackgroundInitializer with its own set of children.
        final MultiBackgroundInitializer nestedInitializer = new MultiBackgroundInitializer();
        for (int i = 0; i < NESTED_CHILD_COUNT; i++) {
            nestedInitializer.addInitializer(CHILD_INIT_NAME + i, createChildBackgroundInitializer());
        }

        // 2. Add a direct child and the nested initializer to the main initializer.
        initializer.addInitializer(CHILD_INIT_NAME, createChildBackgroundInitializer());
        initializer.addInitializer(NESTED_INITIALIZER_NAME, nestedInitializer);

        // Act
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults mainResults = initializer.get();
        final ExecutorService usedExecutor = initializer.getActiveExecutor();

        // Assert
        // 1. Verify the direct child of the main initializer was executed correctly.
        assertInitializerExecutedOnce(mainResults.getInitializer(CHILD_INIT_NAME), usedExecutor);

        // 2. Retrieve and verify the results from the nested initializer.
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults nestedResults =
            (MultiBackgroundInitializer.MultiBackgroundInitializerResults) mainResults.getResultObject(NESTED_INITIALIZER_NAME);
        assertEquals(NESTED_CHILD_COUNT, nestedResults.initializerNames().size(),
            "Nested initializer should report the correct number of children.");

        // 3. Verify each child of the nested initializer was executed correctly.
        for (int i = 0; i < NESTED_CHILD_COUNT; i++) {
            final String childName = CHILD_INIT_NAME + i;
            assertInitializerExecutedOnce(nestedResults.getInitializer(childName), usedExecutor);
        }

        // 4. Verify the internally-created executor service was shut down after completion.
        assertTrue(usedExecutor.isShutdown(), "Executor service should be shut down after completion.");
    }

    /**
     * Asserts that a child initializer was executed exactly once using the provided executor.
     * <p>
     * This helper checks the internal state of the test-specific {@link AbstractChildBackgroundInitializer}.
     *
     * @param childInitializer the child initializer to check.
     * @param expectedExecutor the executor service that should have been used.
     * @throws ConcurrentException if accessing the initializer's result fails.
     */
    private void assertInitializerExecutedOnce(final BackgroundInitializer<?> childInitializer, final ExecutorService expectedExecutor) throws ConcurrentException {
        // Cast to our test-specific implementation to check its internal state.
        final AbstractChildBackgroundInitializer childImpl = (AbstractChildBackgroundInitializer) childInitializer;

        // Check 1: The result object from get() should reflect one execution.
        final CloseableCounter result = childImpl.get();
        assertEquals(1, result.getInitializeCalls(), "Result object should indicate one execution.");

        // Check 2: The internal counter of the initializer implementation should be 1.
        assertEquals(1, childImpl.initializeCalls, "initialize() should have been called exactly once.");

        // Check 3: The correct executor service should have been used.
        if (expectedExecutor != null) {
            assertEquals(expectedExecutor, childImpl.currentExecutor, "Initializer should have used the main executor service.");
        }
    }

    /**
     * Creates a new test-specific child initializer.
     *
     * @return a new {@link MethodChildBackgroundInitializer}.
     */
    protected AbstractChildBackgroundInitializer createChildBackgroundInitializer() {
        return new MethodChildBackgroundInitializer();
    }

    //region Test Helper Classes

    /**
     * A test-specific implementation of {@code BackgroundInitializer} for defining background tasks.
     * It tracks execution state like call counts and the executor used.
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

    /** A concrete child initializer that uses the {@code initialize} method. */
    protected static class MethodChildBackgroundInitializer extends AbstractChildBackgroundInitializer {
        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    /**
     * A simple counter class used as the result of a background initialization.
     * It tracks the number of times it has been "initialized" (incremented).
     */
    protected static class CloseableCounter {
        volatile int initializeCalls;
        volatile boolean closed;

        public static CloseableCounter wrapInteger(final int i) {
            return new CloseableCounter().setInitializeCalls(i);
        }

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

        public boolean isClosed() {
            return closed;
        }

        public CloseableCounter setInitializeCalls(final int i) {
            initializeCalls = i;
            return this;
        }
    }

    //endregion
}