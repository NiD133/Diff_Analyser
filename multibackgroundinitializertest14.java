package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
 * Tests for the inner class {@link MultiBackgroundInitializer.MultiBackgroundInitializerResults}.
 */
@DisplayName("MultiBackgroundInitializer.MultiBackgroundInitializerResults")
class MultiBackgroundInitializerResultsTest extends AbstractLangTest {

    private MultiBackgroundInitializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * This test verifies that querying for an exception of a non-existent child
     * initializer in the results object correctly throws a NoSuchElementException.
     *
     * @throws ConcurrentException because the SUT's get() method can throw it.
     */
    @Test
    @DisplayName("should throw NoSuchElementException when getting an exception for an unknown initializer name")
    void getExceptionForUnknownNameShouldThrowNoSuchElementException() throws ConcurrentException {
        // Arrange: Create a results object by running an initializer with one child.
        // This provides a valid object to test against.
        final String knownInitializerName = "knownInitializer";
        initializer.addInitializer(knownInitializerName, new TestChildInitializer());
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Sanity check to ensure the setup is correct and the known initializer succeeded.
        assertTrue(results.isSuccessful(), "The initialization should have been successful.");
        assertFalse(results.isException(knownInitializerName), "The known initializer should not have an exception.");

        // Act & Assert: Attempting to get an exception for a non-existent initializer
        // should throw the expected exception.
        final String unknownInitializerName = "unknownInitializer";
        assertThrows(NoSuchElementException.class, () -> results.getException(unknownInitializerName));
    }

    // --- Test Fixtures ---

    /**
     * A minimal implementation of BackgroundInitializer for testing. It returns a
     * simple counter object upon successful initialization.
     */
    private static class TestChildInitializer extends BackgroundInitializer<Integer> {
        @Override
        protected Integer initialize() {
            // In this test, we only care that initialization completes successfully.
            return 1;
        }
    }

    /**
     * A more complex, controllable implementation of BackgroundInitializer, kept here
     * for its utility in other potential tests, though not used in the specific test above.
     * It allows for simulating delays and exceptions.
     */
    protected static class ControllableChildInitializer extends BackgroundInitializer<CloseableCounter> {
        volatile ExecutorService currentExecutor;
        final CloseableCounter counter = new CloseableCounter();
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

    /**
     * A helper class to act as a result object, tracking call counts and a "closed" state.
     */
    protected static class CloseableCounter {
        volatile int initializeCalls;
        volatile boolean closed;

        public CloseableCounter increment() {
            initializeCalls++;
            return this;
        }
    }
}