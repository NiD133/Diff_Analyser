package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * Contains tests for the {@link BackgroundInitializer} class.
 *
 * Note: This class has been refactored from an auto-generated test.
 * The original test class name and scaffolding have been kept for context,
 * but in a real-world scenario, they would likely be simplified.
 */
public class BackgroundInitializer_ESTestTest13 extends BackgroundInitializer_ESTest_scaffolding {

    /**
     * Tests that getTaskCount() returns 1 by default. This is the expected
     * behavior for a standard BackgroundInitializer that has not been subclassed
     * to perform more complex work.
     */
    @Test
    public void getTaskCountShouldReturnOneByDefault() {
        // Arrange
        // Create a BackgroundInitializer instance. Using an external executor is a common
        // use case, though not strictly necessary for this specific test.
        final ExecutorService executor = ForkJoinPool.commonPool();
        final BackgroundInitializer<Object> backgroundInitializer = new BackgroundInitializer<>(executor);

        // Act
        // Call the method under test.
        final int taskCount = backgroundInitializer.getTaskCount();

        // Assert
        // Verify that the default task count is 1, as specified in the documentation.
        assertEquals("The default task count should be 1.", 1, taskCount);
    }
}