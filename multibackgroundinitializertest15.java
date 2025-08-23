package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentException;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiBackgroundInitializer.MultiBackgroundInitializerResults}.
 * This version of the test focuses on a specific failure case.
 */
public class MultiBackgroundInitializerTest extends AbstractLangTest {

    /** A name for a child initializer used in tests. */
    private static final String CHILD_INIT_NAME = "childInitializer";

    /** The initializer to be tested. */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * Tests that getInitializer() on the results object throws an exception
     * when queried for a name that does not exist.
     */
    @Test
    @DisplayName("MultiBackgroundInitializer.Results.getInitializer() should throw NoSuchElementException for an unknown name")
    void getInitializerOnResultsShouldThrowExceptionForUnknownName() throws ConcurrentException {
        // Arrange: Create a results object with a known initializer.
        // We add one child initializer to ensure the results object is populated correctly.
        initializer.addInitializer(CHILD_INIT_NAME, new TestChildBackgroundInitializer());
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();
        assertNotNull(results.getInitializer(CHILD_INIT_NAME), "Sanity check: known initializer should be present.");

        // Act & Assert: Attempting to get an initializer with an unknown name should throw.
        final String unknownInitializerName = "unknownInitializer";
        assertThrows(NoSuchElementException.class, () -> {
            results.getInitializer(unknownInitializerName);
        }, "Expected a NoSuchElementException for an unknown initializer name.");
    }

    //<editor-fold desc="Helper classes for testing">

    /**
     * A basic implementation of BackgroundInitializer for testing purposes.
     * It simulates a simple background task that returns a counter object.
     */
    private static class TestChildBackgroundInitializer extends BackgroundInitializer<Integer> {
        @Override
        protected Integer initialize() {
            // Simulate some work and return a result.
            return 1;
        }
    }

    // The original test suite contained many complex helper classes.
    // For the specific test case provided, a much simpler helper is sufficient.
    // The original helpers are preserved below for context but are not used in the refactored test.
    // In a real-world scenario, these would be shared across multiple tests in this class.

    /**
     * A mostly complete implementation of {@code BackgroundInitializer} used for
     * defining background tasks for {@code MultiBackgroundInitializer}.
     *
     * Subclasses will contain the initializer, either as an method implementation
     * or by using a supplier.
     */
    protected static class AbstractChildBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        volatile ExecutorService currentExecutor;
        CloseableCounter counter = new CloseableCounter();
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

    protected static class CloseableCounter {
        public static CloseableCounter wrapInteger(final int i) {
            return new CloseableCounter().setInitializeCalls(i);
        }
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
        public boolean isClosed() {
            return closed;
        }
        public CloseableCounter setInitializeCalls(final int i) {
            initializeCalls = i;
            return this;
        }
    }

    protected static class MethodChildBackgroundInitializer extends AbstractChildBackgroundInitializer {
        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }
    //</editor-fold>
}