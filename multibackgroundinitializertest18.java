package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for MultiBackgroundInitializer.
 * This version contains a refactored test case for improved clarity.
 */
public class MultiBackgroundInitializerTestTest18 extends AbstractLangTest {

    /**
     * Constant for the names of the child initializers.
     */
    private static final String CHILD_INIT = "childInitializer";

    /**
     * The initializer to be tested.
     */
    protected MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * A mostly complete implementation of {@code BackgroundInitializer} used for
     * defining background tasks for {@code MultiBackgroundInitializer}.
     */
    protected static class AbstractChildBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        volatile ExecutorService currentExecutor;
        final CloseableCounter counter = new Close.ableCounter();
        volatile int initializeCalls;
        Exception ex;
        final CountDownLatch latch = new CountDownLatch(1);
        boolean waitForLatch;

        public void enableLatch() {
            waitForLatch = true;
        }

        @Override
        protected CloseableCounter initialize() throws Exception {
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

    protected static class CloseableCounter {
        public static CloseableCounter wrapInteger(final int i) {
            return new CloseableCounter().setInitializeCalls(i);
        }
        volatile int initializeCalls;
        volatile boolean closed;
        public void close() { closed = true; }
        @Override
        public boolean equals(final Object other) {
            if (other instanceof CloseableCounter) {
                return initializeCalls == ((CloseableCounter) other).getInitializeCalls();
            }
            return false;
        }
        public int getInitializeCalls() { return initializeCalls; }
        @Override
        public int hashCode() { return initializeCalls; }
        public CloseableCounter increment() { initializeCalls++; return this; }
        public boolean isClosed() { return closed; }
        public CloseableCounter setInitializeCalls(final int i) { initializeCalls = i; return this; }
    }

    protected static class MethodChildBackgroundInitializer extends AbstractChildBackgroundInitializer {
        // Concrete implementation for tests
    }

    /**
     * An overrideable method to create concrete implementations of
     * {@code BackgroundInitializer} used for defining background tasks
     * for {@code MultiBackgroundInitializer}.
     */
    protected AbstractChildBackgroundInitializer createChildBackgroundInitializer() {
        return new MethodChildBackgroundInitializer();
    }

    // --------------------------------------------------------------------------------
    // Refactored Test Case
    // --------------------------------------------------------------------------------

    @Test
    @DisplayName("isException() should throw NoSuchElementException for an unknown initializer name")
    void isException_shouldThrowException_forUnknownInitializer() throws ConcurrentException {
        // Arrange: Set up the initializer with a single, successful child task to get a valid results object.
        initializer.addInitializer("known_child", createChildBackgroundInitializer());
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Act & Assert: Verify that querying the exception status for a non-existent
        // initializer name throws the expected exception.
        final String unknownInitializerName = "unknown_initializer";
        assertThrows(NoSuchElementException.class, () -> {
            results.isException(unknownInitializerName);
        }, "Expected a NoSuchElementException for an unknown initializer name.");
    }

    // --------------------------------------------------------------------------------
    // Helper methods from the original test, kept for context and for other potential tests.
    // Note: The refactored test no longer depends on checkInitialize().
    // --------------------------------------------------------------------------------

    /**
     * Tests whether a child initializer has been executed.
     */
    private void checkChild(final BackgroundInitializer<?> child, final ExecutorService expExec) throws ConcurrentException {
        final AbstractChildBackgroundInitializer cinit = (AbstractChildBackgroundInitializer) child;
        final Integer result = cinit.get().getInitializeCalls();
        assertEquals(1, result.intValue(), "Wrong result");
        assertEquals(1, cinit.initializeCalls, "Wrong number of executions");
        if (expExec != null) {
            assertEquals(expExec, cinit.currentExecutor, "Wrong executor service");
        }
    }

    /**
     * Helper method for testing the initialize() method.
     */
    private MultiBackgroundInitializer.MultiBackgroundInitializerResults checkInitialize() throws ConcurrentException {
        final int count = 5;
        for (int i = 0; i < count; i++) {
            initializer.addInitializer(CHILD_INIT + i, createChildBackgroundInitializer());
        }
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = initializer.get();
        assertEquals(count, res.initializerNames().size(), "Wrong number of child initializers");
        for (int i = 0; i < count; i++) {
            final String key = CHILD_INIT + i;
            assertTrue(res.initializerNames().contains(key), "Name not found: " + key);
            assertEquals(CloseableCounter.wrapInteger(1), res.getResultObject(key), "Wrong result object");
            assertFalse(res.isException(key), "Exception flag");
            assertNull(res.getException(key), "Got an exception");
            checkChild(res.getInitializer(key), initializer.getActiveExecutor());
        }
        return res;
    }
}