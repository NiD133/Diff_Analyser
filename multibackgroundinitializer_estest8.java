package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link MultiBackgroundInitializer}.
 */
public class MultiBackgroundInitializerTest {

    /**
     * Tests that initialize() can be called successfully after close() if the child
     * initializers use an external ExecutorService.
     *
     * The close() method on a BackgroundInitializer only shuts down an ExecutorService
     * if it was created internally. If an external one is provided, close() is a no-op,
     * allowing subsequent initializations to proceed.
     */
    @Test(timeout = 4000)
    public void initializeShouldSucceedAfterCloseWhenChildUsesExternalExecutor() throws ConcurrentException {
        // Arrange
        // Create a child initializer with an external executor that won't be shut down by close().
        final ExecutorService childExecutor = Executors.newSingleThreadExecutor();
        final BackgroundInitializer<Object> childInitializer = new BackgroundInitializer<>(childExecutor);

        final MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();
        multiInitializer.addInitializer("child1", childInitializer);

        try {
            // Act
            // Calling close() should not affect the external executor.
            multiInitializer.close();

            // Therefore, initialize() should still be able to execute the child task.
            final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

            // Assert
            assertNotNull("The results object should not be null.", results);
            assertTrue("Initialization should be reported as successful.", results.isSuccessful());
        } finally {
            // Cleanup
            // Always ensure the external executor is shut down to prevent resource leaks.
            childExecutor.shutdown();
            try {
                childExecutor.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}