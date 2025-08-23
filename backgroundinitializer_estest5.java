package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link BackgroundInitializer} class.
 * This class was improved from an auto-generated test suite.
 */
public class BackgroundInitializer_ESTestTest5 extends BackgroundInitializer_ESTest_scaffolding {

    /**
     * Tests that the start() method returns true on its first invocation,
     * indicating that the background initialization has been successfully started.
     */
    @Test(timeout = 4000)
    public void startShouldReturnTrueOnFirstCall() {
        // Arrange: Create a BackgroundInitializer and provide it with an external executor.
        // The specific generic type <Object> is not relevant for this test's logic.
        BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();
        ExecutorService externalExecutor = ForkJoinPool.commonPool();
        initializer.setExternalExecutor(externalExecutor);

        // Act: Start the background initialization.
        boolean wasStarted = initializer.start();

        // Assert: The start() method should return true for the first time it is called.
        assertTrue("Expected start() to return true on the first call.", wasStarted);
    }
}