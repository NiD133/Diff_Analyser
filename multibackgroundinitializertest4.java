package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the executor management of {@link MultiBackgroundInitializer}.
 *
 * This test class verifies that if a child {@link BackgroundInitializer} is
 * configured with its own {@link ExecutorService}, {@link MultiBackgroundInitializer}
 * respects that and uses the dedicated executor for that child, while using its
 * own managed executor for other children.
 */
public class MultiBackgroundInitializerExecutorTest extends AbstractLangTest {

    /** The name for the child initializer that uses the main executor. */
    private static final String CHILD_USING_MAIN_EXECUTOR_NAME = "childInitializer";

    /** The name for the child initializer that uses its own dedicated executor. */
    private static final String CHILD_WITH_DEDICATED_EXECUTOR_NAME = "childInitializerWithExecutor";

    /** The main initializer under test. */
    private MultiBackgroundInitializer multiInitializer;

    /** A dedicated executor for a specific child initializer. */
    private ExecutorService dedicatedChildExecutor;

    @BeforeEach
    public void setUp() {
        multiInitializer = new MultiBackgroundInitializer();
        // A single-threaded executor is sufficient for this test's purpose.
        dedicatedChildExecutor = Executors.newSingleThreadExecutor();
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        // Ensure the dedicated executor is always shut down after a test.
        if (dedicatedChildExecutor != null) {
            dedicatedChildExecutor.shutdown();
            if (!dedicatedChildExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                fail("Executor service did not terminate in time.");
            }
        }
    }

    /**
     * Tests that a child initializer with its own executor uses it,
     * while other children use the main initializer's executor.
     */
    @Test
    void whenChildHasOwnExecutor_thenItIsUsedForThatChildOnly() throws ConcurrentException {
        // Arrange: Create two child initializers. One will use the main executor,
        // and the other will be assigned its own dedicated executor.
        final TestChildInitializer childUsingMainExecutor = new TestChildInitializer();
        final TestChildInitializer childWithOwnExecutor = new TestChildInitializer();
        childWithOwnExecutor.setExternalExecutor(dedicatedChildExecutor);

        multiInitializer.addInitializer(CHILD_USING_MAIN_EXECUTOR_NAME, childUsingMainExecutor);
        multiInitializer.addInitializer(CHILD_WITH_DEDICATED_EXECUTOR_NAME, childWithOwnExecutor);

        // Act: Start the main initializer and wait for all child tasks to complete.
        multiInitializer.start();
        multiInitializer.get(); // Throws ConcurrentException on errors.

        // Assert: Verify each child was executed by the correct executor service.
        assertChildExecution(childUsingMainExecutor, multiInitializer.getActiveExecutor());
        assertChildExecution(childWithOwnExecutor, dedicatedChildExecutor);
    }

    /**
     * Verifies that a child initializer was executed exactly once on the expected executor.
     *
     * @param child The child initializer to check.
     * @param expectedExecutor The executor service that should have run the child.
     * @throws ConcurrentException if the child's get() method fails.
     */
    private void assertChildExecution(final TestChildInitializer child, final ExecutorService expectedExecutor) throws ConcurrentException {
        final InitializationResult result = child.get();
        assertNotNull(result, "Child initializer should have produced a result.");
        assertEquals(1, result.getInitializeCalls(), "Child's initialize() should be called exactly once.");
        assertEquals(expectedExecutor, child.getExecutingExecutor(), "Child should run on the expected executor service.");
    }

    // Inner classes below are test fixtures for creating controllable child initializers.

    /**
     * A concrete implementation of {@link BackgroundInitializer} for testing purposes.
     * It records which executor it was run on and how many times it was initialized.
     */
    private static class TestChildInitializer extends BackgroundInitializer<InitializationResult> {

        /** Stores the executor service that ran the initialize() method. */
        private volatile ExecutorService executingExecutor;

        /** The result object that also tracks initialization state. */
        private final InitializationResult result = new InitializationResult();

        /** An optional latch to control the completion of the initialize() method for concurrency tests. */
        private final CountDownLatch completionLatch = new CountDownLatch(1);
        private boolean latchEnabled;

        @Override
        protected InitializationResult initialize() throws Exception {
            executingExecutor = getActiveExecutor();
            result.increment();

            if (latchEnabled) {
                completionLatch.await();
            }
            return result;
        }

        /** Returns the executor service captured during initialization. */
        public ExecutorService getExecutingExecutor() {
            return executingExecutor;
        }

        /** Call to make the initialize() method wait until releaseLatch() is called. */
        public void enableLatch() {
            latchEnabled = true;
        }

        /** Releases the latch, allowing a waiting initialize() method to complete. */
        public void releaseLatch() {
            completionLatch.countDown();
        }
    }

    /**
     * A simple class representing the result of an initialization.
     * It primarily serves as a counter for how many times initialization occurred.
     */
    private static class InitializationResult {
        private volatile int initializeCalls;

        public int getInitializeCalls() {
            return initializeCalls;
        }

        public void increment() {
            initializeCalls++;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final InitializationResult other = (InitializationResult) obj;
            return initializeCalls == other.initializeCalls;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(initializeCalls);
        }
    }
}