package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link BackgroundInitializer}.
 */
public class BackgroundInitializerTest {

    /**
     * Tests that the initializer's state correctly transitions from not-initialized
     * to initialized after the background task completes.
     */
    @Test
    public void isInitializedShouldBeFalseBeforeStartAndTrueAfterCompletion() throws ConcurrentException {
        // Arrange: Create a new BackgroundInitializer.
        // The default implementation of initialize() returns null.
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Assert: Before starting, the initializer is neither started nor initialized.
        assertFalse("Initializer should not be in 'started' state before start() is called.", initializer.isStarted());
        assertFalse("Initializer should not be initialized before start() is called.", initializer.isInitialized());

        // Act: Start the background initialization process.
        initializer.start();

        // Assert: Immediately after calling start(), the initializer should be in the 'started' state.
        assertTrue("Initializer should be in 'started' state after start() is called.", initializer.isStarted());

        // Act: Wait for the background task to complete by calling get().
        // This is crucial for a stable test, as it synchronizes the test thread
        // with the completion of the background initialization.
        final Object result = initializer.get();

        // Assert: After the task has completed, the initializer should be fully initialized.
        assertTrue("Initializer should be initialized after get() has completed.", initializer.isInitialized());
        assertNull("The result of the default initialization should be null.", result);
    }
}