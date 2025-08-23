package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiBackgroundInitializer}.
 * This class focuses on the core functionality of adding and successfully running initializers.
 */
@DisplayName("MultiBackgroundInitializer Test")
class MultiBackgroundInitializerTest extends AbstractLangTest {

    /**
     * Constant for the names of the child initializers.
     */
    private static final String CHILD_INIT_NAME_PREFIX = "childInitializer";

    /**
     * A short time to wait for background threads to run.
     */
    protected static final long PERIOD_MILLIS = 50;

    /**
     * The initializer to be tested.
     */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    @Test
    @DisplayName("addInitializer() should throw NullPointerException when the initializer name is null")
    void addInitializer_whenNameIsNull_throwsNullPointerException() {
        // Arrange
        final BackgroundInitializer<?> childInitializer = createChildBackgroundInitializer();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            initializer.addInitializer(null, childInitializer);
        }, "Adding an initializer with a null name should throw a NullPointerException.");
    }

    @Test
    @DisplayName("get() should return results for all successfully completed child initializers")
    void get_whenAllChildrenCompleteSuccessfully_returnsAggregatedResults() throws ConcurrentException {
        // Arrange
        final int childCount = 5;
        for (int i = 0; i < childCount; i++) {
            initializer.addInitializer(CHILD_INIT_NAME_PREFIX + i, createChildBackgroundInitializer());
        }

        // Act
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert
        assertEquals(childCount, results.initializerNames().size(), "Should contain results for all children.");

        for (int i = 0; i < childCount; i++) {
            final String childName = CHILD_INIT_NAME_PREFIX + i;
            assertAll("Validate results for child: " + childName,
                () -> assertTrue(results.initializerNames().contains(childName), "Result set should contain the child's name."),
                () -> assertEquals(CloseableCounter.of(1), results.getResultObject(childName), "Result object should be correct."),
                () -> assertFalse(results.isException(childName), "Should not be marked as an exception."),
                () -> assertNull(results.getException(childName), "Should have no exception stored."),
                () -> checkChildInitializerState(results.getInitializer(childName), initializer.getActiveExecutor())
            );
        }
    }

    /**
     * Checks whether a child initializer has been executed correctly.
     *
     * @param child the child initializer to check.
     * @param expectedExecutor the expected executor service (can be null).
     */
    private void checkChildInitializerState(final BackgroundInitializer<?> child, final ExecutorService expectedExecutor) {
        final AbstractChildBackgroundInitializer childInit = (AbstractChildBackgroundInitializer) child;
        assertEquals(1, childInit.getInitializeCalls(), "Child's initialize() should be called exactly once.");
        if (expectedExecutor != null) {
            assertEquals(expectedExecutor, childInit.currentExecutor, "Child should have used the correct executor service.");
        }
    }

    /**
     * Factory method for creating a test implementation of {@code BackgroundInitializer}.
     */
    protected AbstractChildBackgroundInitializer createChildBackgroundInitializer() {
        return new MethodChildBackgroundInitializer();
    }

    /**
     * A test-specific implementation of {@code BackgroundInitializer} to control and inspect
     * its behavior during tests.
     */
    protected static abstract class AbstractChildBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        volatile ExecutorService currentExecutor;
        final CloseableCounter counter = new CloseableCounter();
        volatile int initializeCalls;
        Exception ex;
        final CountDownLatch latch = new CountDownLatch(1);
        boolean waitForLatch;

        public void enableLatch() {
            waitForLatch = true;
        }

        public int getInitializeCalls() {
            return initializeCalls;
        }

        @Override
        protected CloseableCounter initialize() throws Exception {
            initializeCalls++;
            currentExecutor = getActiveExecutor();
            if (waitForLatch) {
                latch.await(PERIOD_MILLIS, TimeUnit.MILLISECONDS);
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
     * A simple counter object that can be closed, used as the result of a background task.
     */
    protected static class CloseableCounter {
        private volatile int count;
        private volatile boolean closed;

        public static CloseableCounter of(final int initialValue) {
            return new CloseableCounter().setCount(initialValue);
        }

        public void close() {
            closed = true;
        }

        @Override
        public boolean equals(final Object other) {
            if (other instanceof CloseableCounter) {
                return count == ((CloseableCounter) other).getCount();
            }
            return false;
        }

        public int getCount() {
            return count;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(count);
        }

        public CloseableCounter increment() {
            count++;
            return this;
        }

        public boolean isClosed() {
            return closed;
        }

        public CloseableCounter setCount(final int value) {
            this.count = value;
            return this;
        }
    }

    /**
     * A concrete implementation of the abstract test initializer.
     */
    protected static class MethodChildBackgroundInitializer extends AbstractChildBackgroundInitializer {
        // No additional logic needed; inherits the test implementation.
    }
}