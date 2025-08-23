package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;

// The original class name and inheritance are preserved to match the request context.
// In a real-world scenario, this would likely be part of a single, well-named
// test class like `BackgroundInitializerTest`.
public class BackgroundInitializer_ESTestTest8 extends BackgroundInitializer_ESTest_scaffolding {

    /**
     * Tests that an IllegalStateException is thrown when setExternalExecutor()
     * is called after the initializer has already been started.
     */
    @Test
    public void setExternalExecutorShouldThrowIllegalStateExceptionAfterStart() throws Exception {
        // Arrange: Create an initializer and an executor service.
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();
        final ExecutorService newExecutor = Executors.newSingleThreadExecutor();

        try {
            // Start the initializer and wait for it to complete its (default no-op) task.
            // This transitions the initializer to a "started" state, after which
            // its configuration should be immutable.
            initializer.start();
            initializer.get();

            // Act & Assert: Attempt to set the executor, which should fail.
            try {
                initializer.setExternalExecutor(newExecutor);
                fail("Expected an IllegalStateException because the initializer was already started.");
            } catch (final IllegalStateException e) {
                // Verify that the exception has the expected, documented message.
                assertEquals("Cannot set ExecutorService after start()!", e.getMessage());
            }
        } finally {
            // Clean up the executor service to prevent resource leaks.
            newExecutor.shutdown();
        }
    }
}