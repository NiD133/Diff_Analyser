package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiBackgroundInitializer}.
 * This test suite focuses on the successful initialization of multiple tasks and
 * the correct handling of exceptions from child initializers.
 */
public class MultiBackgroundInitializerTest extends AbstractLangTest {

    /** A constant for the name prefix of child initializers. */
    private static final String CHILD_INIT_NAME_PREFIX = "childInitializer";

    /** The initializer under test. */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * Tests that when multiple child initializers are added, they all execute
     * successfully, and their results are correctly collected.
     */
    @Test
    void shouldInitializeMultipleChildrenSuccessfully() throws ConcurrentException {
        // Arrange
        final int childCount = 5;
        for (int i = 0; i < childCount; i++) {
            initializer.addInitializer(CHILD_INIT_NAME_PREFIX + i, new TestChildInitializer());
        }

        // Act
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert
        assertNotNull(results, "The results object should not be null.");
        assertEquals(childCount, results.initializerNames().size(), "Should be one result per child initializer.");
        assertTrue(results.isSuccessful(), "The overall initialization should be successful.");

        for (int i = 0; i < childCount; i++) {
            final String childName = CHILD_INIT_NAME_PREFIX + i;
            assertChildSuccess(results, childName, initializer.getActiveExecutor());
        }
    }

    /**
     * Tests that if a child initializer throws an exception, it is correctly
     * captured in the results object without halting the entire process.
     */
    @Test
    void shouldCorrectlyHandleChildInitializerException() throws ConcurrentException {
        // Arrange
        final String failingChildName = "failingChild";
        final TestChildInitializer failingChild = new TestChildInitializer();
        final Exception cause = new Exception("Test exception from child initializer.");
        failingChild.setExceptionToThrow(cause);
        initializer.addInitializer(failingChildName, failingChild);

        // Act
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert
        assertNotNull(results);
        assertFalse(results.isSuccessful(), "Overall initialization should be marked as unsuccessful.");
        assertChildFailure(results, failingChildName, cause);
    }

    //<editor-fold desc="Assertion Helpers">
    /**
     * Asserts that a child initializer completed successfully.
     *
     * @param results The results from the main initializer.
     * @param childName The name of the child to check.
     * @param expectedExecutor The executor service that should have been used.
     */
    private void assertChildSuccess(final MultiBackgroundInitializer.MultiBackgroundInitializerResults results, final String childName, final ExecutorService expectedExecutor) throws ConcurrentException {
        final TestChildInitializer childInitializer = (TestChildInitializer) results.getInitializer(childName);

        assertAll("Successful Child: " + childName,
            () -> assertFalse(results.isException(childName), "Child should not have an exception flag."),
            () -> assertNull(results.getException(childName), "Child should not have an exception object."),
            () -> assertEquals(TestResult.withCount(1), results.getResultObject(childName), "Child should have the correct result object."),
            () -> assertEquals(1, childInitializer.getInitializeCallCount(), "initialize() should have been called once."),
            () -> assertEquals(expectedExecutor, childInitializer.getExecutorServiceUsed(), "Child should have used the correct executor service.")
        );
    }

    /**
     * Asserts that a child initializer failed as expected.
     *
     * @param results The results from the main initializer.
     * @param childName The name of the child to check.
     * @param expectedCause The expected exception cause.
     */
    private void assertChildFailure(final MultiBackgroundInitializer.MultiBackgroundInitializerResults results, final String childName, final Throwable expectedCause) {
        assertAll("Failed Child: " + childName,
            () -> assertTrue(results.isException(childName), "Child should have an exception flag."),
            () -> assertNull(results.getResultObject(childName), "Child should not have a result object."),
            () -> {
                final ConcurrentException capturedException = results.getException(childName);
                assertNotNull(capturedException, "Child should have an exception object.");
                assertEquals(expectedCause, capturedException.getCause(), "The captured exception should have the correct cause.");
            }
        );
    }
    //</editor-fold>

    //<editor-fold desc="Test Helper Classes">
    /**
     * A concrete {@code BackgroundInitializer} for testing purposes.
     * It produces a {@link TestResult} and allows simulating failures.
     */
    private static class TestChildInitializer extends BackgroundInitializer<TestResult> {
        private final TestResult result = new TestResult();
        private final CountDownLatch latch = new CountDownLatch(1);

        private volatile int initializeCallCount;
        private volatile ExecutorService executorServiceUsed;
        private Exception exceptionToThrow;
        private boolean latchEnabled;

        @Override
        protected TestResult initialize() throws Exception {
            initializeCallCount++;
            executorServiceUsed = getActiveExecutor();

            if (latchEnabled) {
                latch.await();
            }

            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }

            return result.increment();
        }

        public int getInitializeCallCount() {
            return initializeCallCount;
        }

        public ExecutorService getExecutorServiceUsed() {
            return executorServiceUsed;
        }

        public void setExceptionToThrow(final Exception ex) {
            this.exceptionToThrow = ex;
        }

        // Methods to control execution for more complex concurrency tests (unused in these specific tests)
        public void enableLatch() {
            latchEnabled = true;
        }

        public void releaseLatch() {
            latch.countDown();
        }
    }

    /**
     * A helper class representing the result of a {@link TestChildInitializer}.
     * It tracks an initialization count and a "closed" status.
     */
    private static class TestResult {
        private volatile int initializeCount;
        private volatile boolean closed;

        public static TestResult withCount(final int count) {
            final TestResult res = new TestResult();
            res.initializeCount = count;
            return res;
        }

        public TestResult increment() {
            initializeCount++;
            return this;
        }

        public void close() {
            closed = true;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            final TestResult that = (TestResult) obj;
            return initializeCount == that.initializeCount;
        }

        @Override
        public int hashCode() {
            return initializeCount;
        }
    }
    //</editor-fold>
}