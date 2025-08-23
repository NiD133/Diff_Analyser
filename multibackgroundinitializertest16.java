package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiBackgroundInitializer.MultiBackgroundInitializerResults}.
 * This refactored test focuses on a single behavior for clarity.
 */
public class MultiBackgroundInitializerTest extends AbstractLangTest {

    /**
     * Constant for the names of the child initializers.
     */
    private static final String CHILD_INIT_PREFIX = "childInitializer";

    /**
     * The initializer to be tested.
     */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * Tests that attempting to retrieve a result for an initializer that does not exist
     * throws the expected exception.
     */
    @Test
    @DisplayName("getResultObject() should throw NoSuchElementException for an unknown initializer name")
    void getResultObjectForUnknownInitializerShouldThrowException() throws ConcurrentException {
        // Arrange
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializeWithChildrenAndGetResults(5);
        final String unknownInitializerName = "unknownInitializer";

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            results.getResultObject(unknownInitializerName);
        }, "Expected a NoSuchElementException for an unknown initializer name");
    }

    /**
     * Helper method to set up the MultiBackgroundInitializer with a given number of
     * child initializers, start it, and return the results. This method performs
     * no assertions.
     *
     * @param childCount the number of child initializers to add
     * @return the results object after initialization
     * @throws ConcurrentException if the initialization fails
     */
    private MultiBackgroundInitializer.MultiBackgroundInitializerResults initializeWithChildrenAndGetResults(final int childCount)
            throws ConcurrentException {
        for (int i = 0; i < childCount; i++) {
            initializer.addInitializer(CHILD_INIT_PREFIX + i, new ChildBackgroundInitializer());
        }
        initializer.start();
        return initializer.get();
    }

    // Inner classes below are test fixtures used to simulate child initializers.
    // They are kept from the original test to support the test case.

    /**
     * A concrete implementation of {@code BackgroundInitializer} for testing purposes.
     * It simulates a background task that produces a {@link CloseableCounter}.
     */
    protected static class ChildBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        private final CloseableCounter counter = new CloseableCounter();

        @Override
        protected CloseableCounter initialize() {
            return counter.increment();
        }
    }

    /**
     * A helper class to represent the result of an initialization and track its state.
     */
    protected static class CloseableCounter {
        private int initializeCalls;
        private boolean closed;

        public CloseableCounter increment() {
            initializeCalls++;
            return this;
        }

        public int getInitializeCalls() {
            return initializeCalls;
        }

        public void close() {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
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
}